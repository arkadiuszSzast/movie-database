package com.movie.database.movie_database.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.movie.database.movie_database.config.security.jwt.exception.RefreshTokenExpiredException;
import com.movie.database.movie_database.support.properties.AccessTokenProperties;
import com.movie.database.movie_database.support.properties.RefreshTokenProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenProperties refreshTokenProperties;
    @Mock
    private AccessTokenProperties accessTokenProperties;
    @Mock
    private JWTGenerateService jwtGenerateService;
    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    @DisplayName("Should return response entity with refreshed token when token is valid")
    public void shouldRefreshTokenWhenValid() {
        //arrange
        var validRefreshToken = JWT.create().withSubject("78688d94-86ea-11e9-bc42-526af7764f64").withExpiresAt(Date.from(Instant.now().plusSeconds(30))).sign(Algorithm.HMAC256("secret"));
        var refreshedAccessToken = JWT.create().withSubject("78688d94-86ea-11e9-bc42-526af7764f64").withExpiresAt(Date.from(Instant.now().plusSeconds(40))).sign(Algorithm.HMAC256("secret"));
        var refreshedRefreshToken = JWT.create().withSubject("78688d94-86ea-11e9-bc42-526af7764f64").withExpiresAt(Date.from(Instant.now().plusSeconds(50))).sign(Algorithm.HMAC256("secret"));
        var tokenSecret = "secret";
        var subject = UUID.fromString("78688d94-86ea-11e9-bc42-526af7764f64");
        var accessTokenHeader = "Authorization";
        var refreshTokenHeader = "Refresh-token";
        when(refreshTokenProperties.getSecret()).thenReturn(tokenSecret);
        when(jwtGenerateService.getAccessToken(subject)).thenReturn(refreshedAccessToken);
        when(jwtGenerateService.getRefreshToken(subject)).thenReturn(refreshedRefreshToken);
        when(accessTokenProperties.getHeaderName()).thenReturn(accessTokenHeader);
        when(refreshTokenProperties.getHeaderName()).thenReturn(refreshTokenHeader);
        //act
        var responseEntity = refreshTokenService.refreshToken(validRefreshToken);

        //assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(responseEntity.getHeaders()).containsKeys(accessTokenHeader, refreshTokenHeader);
        assertThat(responseEntity.getHeaders().get(accessTokenHeader).get(0)).isEqualTo(refreshedAccessToken);
        assertThat(responseEntity.getHeaders().get(refreshTokenHeader).get(0)).isEqualTo(refreshedRefreshToken);
    }

    @Test
    @DisplayName("Should throw exception when refresh token is not valid")
    public void shouldThrowExceptionWhenRefreshTokenIsNotValid() {
        //arrange
        var validRefreshToken = JWT.create().withSubject("78688d94-86ea-11e9-bc42-526af7764f64").withExpiresAt(Date.from(Instant.now().plusSeconds(30))).sign(Algorithm.HMAC256("secret"));
        var tokenSecret = "invalidSecret";
        when(refreshTokenProperties.getSecret()).thenReturn(tokenSecret);

        //act && assert
        assertThrows(SignatureVerificationException.class, () -> refreshTokenService.refreshToken(validRefreshToken));
    }

    @Test
    @DisplayName("Should throw exception when refresh token is expired")
    public void shouldThrowExceptionWhenRefreshTokenIsExpired() {
        //arrange
        var validRefreshToken = JWT.create().withSubject("78688d94-86ea-11e9-bc42-526af7764f64").withExpiresAt(Date.from(Instant.now().minusSeconds(30))).sign(Algorithm.HMAC256("secret"));
        var tokenSecret = "secret";
        when(refreshTokenProperties.getSecret()).thenReturn(tokenSecret);

        //act && assert
        assertThrows(RefreshTokenExpiredException.class, () -> refreshTokenService.refreshToken(validRefreshToken));
    }
}