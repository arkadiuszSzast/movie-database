package com.movie.database.movie_database.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTGenerateService {

    private final AccessTokenProperties accessTokenProperties;
    private final RefreshTokenProperties refreshTokenProperties;

    public JWTGenerateService(AccessTokenProperties accessTokenProperties, RefreshTokenProperties refreshTokenProperties) {
        this.accessTokenProperties = accessTokenProperties;
        this.refreshTokenProperties = refreshTokenProperties;
    }

    public String getAccessToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenProperties.getExpirationTime()))
                .sign(Algorithm.HMAC256(accessTokenProperties.getSecret()));
    }

    public String getRefreshToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenProperties.getExpirationTime()))
                .sign(Algorithm.HMAC256(refreshTokenProperties.getSecret()));
    }
}
