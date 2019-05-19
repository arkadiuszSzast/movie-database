package com.movie.database.movie_database.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.movie.database.movie_database.user.ApplicationUserGetService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTGenerateService {

    private final AccessTokenProperties accessTokenProperties;
    private final RefreshTokenProperties refreshTokenProperties;
    private final ApplicationUserGetService applicationUserGetService;

    public JWTGenerateService(AccessTokenProperties accessTokenProperties,
                              RefreshTokenProperties refreshTokenProperties,
                              ApplicationUserGetService applicationUserGetService) {
        this.accessTokenProperties = accessTokenProperties;
        this.refreshTokenProperties = refreshTokenProperties;
        this.applicationUserGetService = applicationUserGetService;
    }

    public String getAccessToken(String username) {
        var applicationUserRest = applicationUserGetService.findByUsername(username);
        return JWT.create()
                .withSubject(username)
                .withArrayClaim("ROLE", applicationUserRest.getRoles().toArray(new String[0]))
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
