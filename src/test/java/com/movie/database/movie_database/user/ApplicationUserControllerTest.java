package com.movie.database.movie_database.user;

import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import com.movie.database.movie_database.utils.MovieDbIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@MovieDbIntegrationTest
public class ApplicationUserControllerTest {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("Should not add new user when password does not have at least 8 character")
    public void shouldNotAddUserWhenPasswordShortenThanEight() {
        var account = new ApplicationUser("joe", "short", "mail");
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
        var account = new ApplicationUser("joe", "secretPassword", "mail");
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
        var account = new ApplicationUser("joe", "secretPassword", "mail");

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
        var account = new ApplicationUser("joe", "secretPassword", "mail");
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(account)
                .when()
                .post("/api/users/sign-up?recaptchaResponse=test")
                .then()
                .statusCode(201);

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
}