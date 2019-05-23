package com.movie.database.movie_database.user.password;

import com.movie.database.movie_database.config.security.jwt.JWTGenerateService;
import com.movie.database.movie_database.support.properties.MovieDbProperties;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ResetPasswordUrlCreateService {

    private final JWTGenerateService jwtGenerateService;
    private final MovieDbProperties movieDbProperties;

    public ResetPasswordUrlCreateService(JWTGenerateService jwtGenerateService,
                                         MovieDbProperties movieDbProperties) {
        this.jwtGenerateService = jwtGenerateService;
        this.movieDbProperties = movieDbProperties;
    }

    public String create(UUID userId) {
        var token = jwtGenerateService.getResetPasswordToken(userId);
        return buildUrl(token);
    }

    private String buildUrl(String token) {
        var frontendUrl = movieDbProperties.getFrontendUrl();
        return String.format("%s/reset-password?token=%s", frontendUrl, token);
    }
}
