package com.movie.database.movie_database.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.movie.database.movie_database.support.properties.AccessTokenProperties;
import com.movie.database.movie_database.support.properties.RefreshTokenProperties;
import com.movie.database.movie_database.user.ApplicationUserGetService;
import com.movie.database.movie_database.user.confirmation.ConfirmationTokenSaveService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class JWTGenerateService {

    private final AccessTokenProperties accessTokenProperties;
    private final RefreshTokenProperties refreshTokenProperties;
    private final ApplicationUserGetService applicationUserGetService;
    private final ConfirmationTokenProperties confirmationTokenProperties;
    private final ConfirmationTokenSaveService confirmationTokenSaveService;

    public JWTGenerateService(AccessTokenProperties accessTokenProperties,
                              RefreshTokenProperties refreshTokenProperties,
                              ApplicationUserGetService applicationUserGetService,
                              ConfirmationTokenProperties confirmationTokenProperties,
                              ConfirmationTokenSaveService confirmationTokenSaveService) {
        this.accessTokenProperties = accessTokenProperties;
        this.refreshTokenProperties = refreshTokenProperties;
        this.applicationUserGetService = applicationUserGetService;
        this.confirmationTokenProperties = confirmationTokenProperties;
        this.confirmationTokenSaveService = confirmationTokenSaveService;
    }

    public String getAccessToken(UUID userId) {
        var applicationUserRest = applicationUserGetService.findRestById(userId);
        return JWT.create()
                .withSubject(userId.toString())
                .withArrayClaim("ROLE", applicationUserRest.getRoles().toArray(new String[0]))
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenProperties.getExpirationTime()))
                .sign(Algorithm.HMAC256(accessTokenProperties.getSecret()));
    }

    public String getRefreshToken(UUID userId) {
        return JWT.create()
                .withSubject(userId.toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenProperties.getExpirationTime()))
                .sign(Algorithm.HMAC256(refreshTokenProperties.getSecret()));
    }

    public String getConfirmationToken(UUID userId) {
        var token = JWT.create()
                .withSubject(userId.toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + confirmationTokenProperties.getExpirationTime()))
                .sign(Algorithm.HMAC256(confirmationTokenProperties.getSecret()));
        confirmationTokenSaveService.save(token);
        return token;
    }
}
