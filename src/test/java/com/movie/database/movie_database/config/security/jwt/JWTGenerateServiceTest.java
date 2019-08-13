package com.movie.database.movie_database.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.movie.database.movie_database.support.properties.AccessTokenProperties;
import com.movie.database.movie_database.support.properties.RefreshTokenProperties;
import com.movie.database.movie_database.support.properties.ResetPasswordTokenProperties;
import com.movie.database.movie_database.user.ApplicationUserGetService;
import com.movie.database.movie_database.user.confirmation.ConfirmationTokenSaveService;
import com.movie.database.movie_database.user.model.ApplicationUserRest;
import com.movie.database.movie_database.user.password.ResetPasswordTokenSaveService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JWTGenerateServiceTest {

    @Mock
    private AccessTokenProperties accessTokenProperties;
    @Mock
    private RefreshTokenProperties refreshTokenProperties;
    @Mock
    private ApplicationUserGetService applicationUserGetService;
    @Mock
    private ConfirmationTokenProperties confirmationTokenProperties;
    @Mock
    private ConfirmationTokenSaveService confirmationTokenSaveService;
    @Mock
    private ResetPasswordTokenProperties resetPasswordTokenProperties;
    @Mock
    private ResetPasswordTokenSaveService resetPasswordTokenSaveService;
    @InjectMocks
    JWTGenerateService jwtGenerateService;

    @Test
    @DisplayName("Should generate access token")
    public void shouldGenerateAccessToken() {
        //arrange
        var userId = UUID.fromString("6a9f766e-bd92-4397-bc22-98e61e388757");
        var userRest = new ApplicationUserRest(userId, "username", "email@email.ca", "default", List.of("USER"));
        when(applicationUserGetService.findRestById(userId)).thenReturn(userRest);
        when(accessTokenProperties.getSecret()).thenReturn("secret");
        when(accessTokenProperties.getExpirationTime()).thenReturn(1000L);

        //act
        var accessToken = jwtGenerateService.getAccessToken(userId);

        //assert
        assertDoesNotThrow(() -> assertThat(JWT.require(Algorithm.HMAC256("secret")).build().verify(accessToken)));
        assertThat(JWT.decode(accessToken).getSubject()).isEqualTo(userId.toString());
        assertThat(JWT.decode(accessToken).getClaim("roles").asArray(String.class)).containsExactly("USER");
        assertThat(JWT.decode(accessToken).getClaim("username").asString()).isEqualTo(userRest.getUsername());
        assertThat(JWT.decode(accessToken).getClaim("email").asString()).isEqualTo(userRest.getEmail());
    }

    @Test
    @DisplayName("Should generate refresh token")
    public void shouldGenerateRefreshToken() {
        //arrange
        var userId = UUID.fromString("6a9f766e-bd92-4397-bc22-98e61e388757");
        when(refreshTokenProperties.getSecret()).thenReturn("secret");
        when(refreshTokenProperties.getExpirationTime()).thenReturn(1000L);

        //act
        var accessToken = jwtGenerateService.getRefreshToken(userId);

        //assert
        assertDoesNotThrow(() -> assertThat(JWT.require(Algorithm.HMAC256("secret")).build().verify(accessToken)));
        assertThat(JWT.decode(accessToken).getSubject()).isEqualTo(userId.toString());
    }

    @Test
    @DisplayName("Should generate confirmation token")
    public void shouldGenerateConfirmationToken() {
        //arrange
        var userId = UUID.fromString("6a9f766e-bd92-4397-bc22-98e61e388757");
        when(confirmationTokenProperties.getSecret()).thenReturn("secret");
        when(confirmationTokenProperties.getExpirationTime()).thenReturn(1000L);

        //act
        var accessToken = jwtGenerateService.getConfirmationToken(userId);

        //assert
        assertDoesNotThrow(() -> assertThat(JWT.require(Algorithm.HMAC256("secret")).build().verify(accessToken)));
        assertThat(JWT.decode(accessToken).getSubject()).isEqualTo(userId.toString());
    }

    @Test
    @DisplayName("Should generate password reset token")
    public void shouldGeneratePasswordResetToken() {
        //arrange
        var userId = UUID.fromString("6a9f766e-bd92-4397-bc22-98e61e388757");
        when(resetPasswordTokenProperties.getSecret()).thenReturn("secret");
        when(resetPasswordTokenProperties.getExpirationTime()).thenReturn(1000L);

        //act
        var accessToken = jwtGenerateService.getResetPasswordToken(userId);

        //assert
        assertDoesNotThrow(() -> assertThat(JWT.require(Algorithm.HMAC256("secret")).build().verify(accessToken)));
        assertThat(JWT.decode(accessToken).getSubject()).isEqualTo(userId.toString());
    }

}