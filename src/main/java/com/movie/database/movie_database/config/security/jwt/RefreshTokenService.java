package com.movie.database.movie_database.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.movie.database.movie_database.config.security.jwt.exception.RefreshTokenExpiredException;
import com.movie.database.movie_database.support.properties.AccessTokenProperties;
import com.movie.database.movie_database.support.properties.RefreshTokenProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenProperties refreshTokenProperties;
    private final AccessTokenProperties accessTokenProperties;
    private final JWTGenerateService jwtGenerateService;

    public RefreshTokenService(RefreshTokenProperties refreshTokenProperties,
                               AccessTokenProperties accessTokenProperties,
                               JWTGenerateService jwtGenerateService) {
        this.refreshTokenProperties = refreshTokenProperties;
        this.accessTokenProperties = accessTokenProperties;
        this.jwtGenerateService = jwtGenerateService;
    }

    public ResponseEntity refreshToken(String oldRefreshToken) {
        var userId = UUID.fromString(getSubject(oldRefreshToken));
        var accessToken = jwtGenerateService.getAccessToken((userId));
        var refreshToken = jwtGenerateService.getRefreshToken(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .header(accessTokenProperties.getHeaderName(), accessToken)
                .header(refreshTokenProperties.getHeaderName(), refreshToken)
                .build();
    }

    private String getSubject(String oldRefreshToken) {
        try {
            return JWT.require(Algorithm.HMAC256(refreshTokenProperties.getSecret())).build()
                    .verify(oldRefreshToken).getSubject();
        } catch (TokenExpiredException e) {
            throw new RefreshTokenExpiredException();
        }
    }
}
