package com.movie.database.movie_database.user.confirmation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.movie.database.movie_database.support.properties.MovieDbProperties;
import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import com.movie.database.movie_database.user.token.domain.ApplicationUserTokenRepository;
import com.movie.database.movie_database.user.token.model.TokenType;
import com.movie.database.movie_database.utils.MovieDbIntegrationTest;
import com.movie.database.movie_database.utils.UserTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@MovieDbIntegrationTest
class ConfirmationControllerTest {

    @Autowired
    private UserTokenProvider userTokenProvider;
    @Autowired
    private MovieDbProperties movieDbProperties;
    @Autowired
    private ApplicationUserTokenRepository applicationUserTokenRepository;
    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("Should activate user when token is valid")
    public void shouldActivateUserWhenTokenIsValid() {

        var token = userTokenProvider.getConfirmationToken();
        var user = applicationUserTokenRepository.findByTokenAndTokenType(token, TokenType.CONFIRMATION).get().getApplicationUser();

        given()
                .port(port)
                .when()
                .param("token", token)
                .get("/api/confirm")
                .then()
                .statusCode(200)
                .header("location", movieDbProperties.getFrontendUrl() + "/confirmed");

        var activatedUser = applicationUserRepository.findById(user.getId());

        assertThat(activatedUser).isPresent();
        assertThat(activatedUser.get().isActive()).isTrue();
    }


    @Test
    @DisplayName("Should return 400 when token is not valid")
    public void shouldReturn400WhenTokenIsNotValid() {

        var randomToken = JWT.create().sign(Algorithm.HMAC256("secret"));

        given()
                .port(port)
                .when()
                .param("token", randomToken)
                .get("/api/confirm")
                .then()
                .statusCode(400);
    }

}