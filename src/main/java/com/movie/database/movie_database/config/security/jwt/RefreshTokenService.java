package com.movie.database.movie_database.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        var username = JWT.require(Algorithm.HMAC256(refreshTokenProperties.getSecret())).build()
                .verify(oldRefreshToken).getSubject();
        var accessToken = jwtGenerateService.getAccessToken(username);
        var refreshToken = jwtGenerateService.getRefreshToken(username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .header(accessTokenProperties.getHeaderName(), accessToken)
                .header(refreshTokenProperties.getHeaderName(), refreshToken)
                .build();
    }
}
