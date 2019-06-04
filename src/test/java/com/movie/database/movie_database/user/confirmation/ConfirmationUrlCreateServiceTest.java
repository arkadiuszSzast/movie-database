package com.movie.database.movie_database.user.confirmation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.movie.database.movie_database.config.security.jwt.JWTGenerateService;
import com.movie.database.movie_database.support.properties.MovieDbProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class ConfirmationUrlCreateServiceTest {

    @Mock
    private JWTGenerateService jwtGenerateService;
    @Mock
    private MovieDbProperties movieDbProperties;
    @InjectMocks
    private ConfirmationUrlCreateService confirmationUrlCreateService;

    @Test
    @DisplayName("Should create confirmation url")
    public void shouldCreateConfirmationUrl() {
        //arrange
        var baseUrl = "http://localhost:8080/";
        var userId = UUID.fromString("78688d94-86ea-11e9-bc42-526af7764f64");
        var token = JWT.create().withSubject(userId.toString()).withExpiresAt(Date.from(Instant.now().plusSeconds(30))).sign(Algorithm.HMAC256("secret"));
        when(jwtGenerateService.getConfirmationToken(userId)).thenReturn(token);
        when(movieDbProperties.getApplicationUrl()).thenReturn(baseUrl);

        //act
        var confirmationUrl = confirmationUrlCreateService.create(userId);

        //assert
        assertThat(confirmationUrl).isEqualTo(baseUrl + "/api/confirm?token=" + token);
    }

}