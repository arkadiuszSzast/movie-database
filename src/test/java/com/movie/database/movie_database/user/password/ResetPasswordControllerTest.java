package com.movie.database.movie_database.user.password;

import com.movie.database.movie_database.user.ApplicationUserGetService;
import com.movie.database.movie_database.user.token.domain.ApplicationUserTokenRepository;
import com.movie.database.movie_database.user.token.model.TokenType;
import com.movie.database.movie_database.utils.MovieDbIntegrationTest;
import com.movie.database.movie_database.utils.UserProvider;
import org.junit.Rule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@MovieDbIntegrationTest
class ResetPasswordControllerTest {

    @Autowired
    private UserProvider userProvider;
    @Autowired
    private ApplicationUserTokenRepository applicationUserTokenRepository;
    @Autowired
    private ApplicationUserGetService applicationUserGetService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("Should send email with reset password token when user exists")
    public void shouldSendChangePasswordEmailWhenUserExist() {
        var account = userProvider.createActivatedUserWithUserRole();

        given()
                .port(port)
                .when()
                .param("email", account.getEmail())
                .post("/api/reset-password/mail")
                .then()
                .statusCode(204);

        assertThat(applicationUserTokenRepository.findByApplicationUserAndTokenType(account, TokenType.RESET_PASSWORD)).isNotEmpty();
    }

    @Test
    @DisplayName("Should not send email but return 200 when user not exists")
    public void shouldNotSendEmailWhenUserNotExists() {

        var notExistingMail = "notExistingMail";

        given()
                .port(port)
                .when()
                .param("email", notExistingMail)
                .post("/api/reset-password/mail")
                .then()
                .statusCode(200);

        var applicationUserTokens = applicationUserTokenRepository.findAll();
        assertThat(applicationUserTokens.stream().anyMatch(applicationUserToken -> applicationUserToken.getApplicationUser().getEmail().equals(notExistingMail))).isFalse();
    }

    @Test
    @DisplayName("Should change user password")
    public void shouldChangeUserPassword() {
        var account = userProvider.createActivatedUserWithUserRole();

        given()
                .port(port)
                .when()
                .param("email", account.getEmail())
                .post("/api/reset-password/mail")
                .then()
                .statusCode(204);

        var token = applicationUserTokenRepository.findByApplicationUserAndTokenType(account, TokenType.RESET_PASSWORD).get().getToken();
        var changedPassword = "changedPassword";

        given()
                .port(port)
                .when()
                .param("password", changedPassword)
                .header("Reset-password-token", token)
                .post("/api/reset-password")
                .then()
                .statusCode(204);

        var accountAfterChangePassword = applicationUserGetService.findById(account.getId());

        assertThat(bCryptPasswordEncoder.matches(changedPassword, accountAfterChangePassword.getPassword())).isTrue();
    }
}