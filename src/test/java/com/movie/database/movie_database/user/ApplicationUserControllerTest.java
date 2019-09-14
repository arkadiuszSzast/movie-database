package com.movie.database.movie_database.user;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import com.movie.database.movie_database.user.role.domain.Role;
import com.movie.database.movie_database.user.role.domain.RoleRepository;
import com.movie.database.movie_database.user.token.blacklist.domain.TokenBlacklistRepository;
import com.movie.database.movie_database.utils.LogInProvider;
import com.movie.database.movie_database.utils.MovieDbIntegrationTest;
import com.movie.database.movie_database.utils.RolesProvider;
import com.movie.database.movie_database.utils.UserProvider;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@MovieDbIntegrationTest
public class ApplicationUserControllerTest {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;
    @Autowired
    private LogInProvider logInProvider;
    @Autowired
    private RolesProvider rolesProvider;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("Should not add new user when password does not have at least 8 character")
    public void shouldNotAddUserWhenPasswordShortenThanEight() {
        var account = new ApplicationUser("joe", "short", "mail@mail.ca");
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(account)
                .when()
                .post("/api/users/sign-up?recaptchaResponse=test")
                .then()
                .statusCode(400);

        var optionalAddedAccount = applicationUserRepository.findByUsername(account.getUsername());
        assertThat(optionalAddedAccount).isEmpty();
    }

    @Test
    @DisplayName("Should not add new user when password does not have at least 8 character")
    public void shouldAddNewUser() {
        var account = new ApplicationUser("joe", "secretPassword", "mail@mail.ca");
        var role = new Role("USER");
        roleRepository.save(role);
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(account)
                .when()
                .post("/api/users/sign-up?recaptchaResponse=test")
                .then()
                .statusCode(201);

        var optionalAddedAccount = applicationUserRepository.findByUsername(account.getUsername());
        assertThat(optionalAddedAccount).isNotEmpty();
    }

    @Test
    @DisplayName("Should not log in when user not exists")
    public void shouldNotLogLogIn() {
        var account = new ApplicationUser("joe", "secretPassword", "mail@mail.ca");

        given()
                .port(port)
                .when()
                .param("username", account.getUsername())
                .param("password", account.getPassword())
                .post("/api/auth/login")
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Should log in when user exists")
    public void shouldLogIn() {
        var account = userProvider.createActivatedUserWithUserRole();

        given()
                .port(port)
                .when()
                .param("username", account.getUsername())
                .param("password", account.getPassword())
                .post("/api/auth/login")
                .then()
                .statusCode(200);

        var optionalAddedAccount = applicationUserRepository.findByUsername(account.getUsername());
        assertThat(optionalAddedAccount).isNotEmpty();
    }

    @Test
    @DisplayName("Should add token to blacklist when logging out")
    public void shouldAddTokenToBlacklistWhenLogin() {
        var authToken = logInProvider.logInAsUser();

        given()
                .port(port)
                .when()
                .header("Authorization", authToken)
                .post("/api/auth/logout")
                .then()
                .statusCode(200);

        assertThat(tokenBlacklistRepository.existsByToken(authToken)).isTrue();
    }

    @Test
    @DisplayName("Should not be able to do any action using blacklisted token")
    public void shouldNotBeAbleToDoAnyActionUsingBlacklistedToken() {
        var account = userProvider.createActivatedUserWithAdminRole();
        var authToken = logInProvider.logIn(account);

        given()
                .port(port)
                .when()
                .header("Authorization", authToken)
                .post("/api/auth/logout")
                .then()
                .statusCode(200);

        given()
                .port(port)
                .when()
                .header("Authorization", authToken)
                .get("/api/users")
                .then()
                .statusCode(401);

    }

    @Test
    @DisplayName("Should not add token to blacklist when user is not log in")
    public void shouldNotAddTokenToBlacklistWhenUserNotLogIn() {
        var authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        given()
                .port(port)
                .when()
                .header("Authorization", authToken)
                .post("/api/auth/logout")
                .then()
                .statusCode(401);

        assertThat(tokenBlacklistRepository.existsByToken(authToken)).isFalse();
    }

    @Test
    @DisplayName("Should get application user list when logged as admin")
    public void shouldGetApplicationUserListWhenRoleAdmin() throws IOException {
        var authToken = logInProvider.logInAsAdmin();

        var admin = applicationUserRepository.findById(UUID.fromString(JWT.decode(authToken).getSubject())).get();
        var user = userProvider.createActivatedUserWithUserRole();

        var response = given()
                .port(port)
                .when()
                .header("Authorization", authToken)
                .get("/api/users")
                .then()
                .statusCode(200)
                .extract()
                .response();

        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        var list = (List<ApplicationUser>) mapper.readValue(response.asByteArray(), new TypeReference<List<ApplicationUser>>() {
        });

        assertThat(list).hasSize(2);

        assertThat(list.stream().anyMatch(applicationUser -> applicationUser.getId().equals(user.getId()))).isTrue();
        assertThat(list.stream().anyMatch(applicationUser -> applicationUser.getEmail().equals(user.getEmail()))).isTrue();
        assertThat(list.stream().anyMatch(applicationUser -> applicationUser.getUsername().equals(user.getUsername()))).isTrue();

        assertThat(list.stream().anyMatch(applicationUser -> applicationUser.getId().equals(admin.getId()))).isTrue();
        assertThat(list.stream().anyMatch(applicationUser -> applicationUser.getEmail().equals(admin.getEmail()))).isTrue();
        assertThat(list.stream().anyMatch(applicationUser -> applicationUser.getUsername().equals(admin.getUsername()))).isTrue();
    }

    @Test
    @DisplayName("Should get 403 when getting application users logged as user")
    public void shouldGet403WhenGettingApplicationUsersLoggedAsUser() {
        var authToken = logInProvider.logInAsUser();

        given()
                .port(port)
                .when()
                .header("Authorization", authToken)
                .get("/api/users")
                .then()
                .statusCode(403);
    }

    @Test
    @DisplayName("Should return 404 deleting not existing application user when logged as admin")
    public void shouldReturn404WhenDeletingNotExistingDirectorWhenLoggedAsAdmin() {
        var authToken = logInProvider.logInAsAdmin();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization", authToken)
                .delete("/api/users/" + UUID.randomUUID())
                .then()
                .statusCode(404);

        var users = applicationUserRepository.findAll();

        assertThat(users).hasSize(1);
    }

    @Test
    @DisplayName("Should remove application user when logged as admin")
    public void shouldRemoveApplicationUserWhenLoggedAsAdmin() {
        var authToken = logInProvider.logInAsAdmin();
        var userToRemove = userProvider.createActivatedUserWithUserRole();

        given()
                .port(port)
                .when()
                .header("Authorization", authToken)
                .delete("/api/users/" + userToRemove.getId())
                .then()
                .statusCode(200);

        assertThat(applicationUserRepository.findById(userToRemove.getId())).isEmpty();
    }

    @Test
    @DisplayName("Should not remove application user when logged as user")
    public void shouldNotRemoveApplicationUserWhenLoggedAsUser() {
        var authToken = logInProvider.logInAsUser();
        var userToRemove = userProvider.createActivatedUserWithoutAnyRole();

        given()
                .port(port)
                .when()
                .header("Authorization", authToken)
                .delete("/api/users/" + userToRemove.getId())
                .then()
                .statusCode(403);

        assertThat(applicationUserRepository.findById(userToRemove.getId())).isNotEmpty();
    }

    @Test
    @DisplayName("Should return 401 when removing application user without login")
    public void shouldNotRemoveApplicationUserWhenNotLoggedIn() {
        var userToRemove = userProvider.createActivatedUserWithoutAnyRole();

        given()
                .port(port)
                .when()
                .delete("/api/users/" + userToRemove.getId())
                .then()
                .statusCode(401);

        assertThat(applicationUserRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("Should update roles of application user when logged as admin")
    public void shouldUpdateRolesOfApplicationUserWhenLoggedAsAdmin() {
        var authToken = logInProvider.logInAsAdmin();
        var userToUpdate = userProvider.createActivatedUserWithoutAnyRole();

        var userRole = rolesProvider.createUserRole();

        given()
                .port(port)
                .when()
                .header("Authorization", authToken)
                .contentType(ContentType.JSON)
                .body(List.of(userRole))
                .put("/api/users/" + userToUpdate.getId() + "/roles")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Should return 403 when updating roles of application user as admin")
    public void shouldNotUpdateRolesOfApplicationUserWhenLoggedAsUser() {
        var authToken = logInProvider.logInAsUser();
        var userToUpdate = userProvider.createActivatedUserWithoutAnyRole();

        var userRole = rolesProvider.createUserRole();

        given()
                .port(port)
                .when()
                .header("Authorization", authToken)
                .contentType(ContentType.JSON)
                .body(List.of(userRole))
                .put("/api/users/" + userToUpdate.getId() + "/roles")
                .then()
                .statusCode(403);
    }

    @Test
    @DisplayName("Should return 401 when updating roles of application user without login")
    public void shouldReturn401WhenUpdatingRolesOfApplicationUserWithoutLogin() {
        var userToUpdate = userProvider.createActivatedUserWithoutAnyRole();

        var userRole = rolesProvider.createUserRole();

        given()
                .port(port)
                .when()
                .contentType(ContentType.JSON)
                .body(List.of(userRole))
                .put("/api/users/" + userToUpdate.getId() + "/roles")
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Should refresh token")
    public void shouldRefreshToken() {
        var refreshToken = logInProvider.getRefreshToken();

        var refreshedToken = given()
                .port(port)
                .when()
                .header("Refresh-token", refreshToken)
                .post("/api/auth/refresh")
                .then()
                .statusCode(204)
                .extract()
                .header("Authorization");

        assertThat(refreshToken).isNotEqualTo(refreshedToken);
        assertThat(JWT.decode(refreshToken).getSubject()).isEqualTo(JWT.decode(refreshedToken).getSubject());
    }
}