package com.movie.database.movie_database.user.confirmation;

import com.movie.database.movie_database.config.security.jwt.JWTGenerateService;
import com.movie.database.movie_database.support.properties.MovieDbProperties;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ConfirmationUrlCreateService {

    private final JWTGenerateService jwtGenerateService;
    private final MovieDbProperties movieDbProperties;

    public ConfirmationUrlCreateService(JWTGenerateService jwtGenerateService,
                                        MovieDbProperties movieDbProperties) {
        this.jwtGenerateService = jwtGenerateService;
        this.movieDbProperties = movieDbProperties;
    }

    public String create(UUID applicationUserId) {
        var token = jwtGenerateService.getConfirmationToken(applicationUserId);
        return buildUrl(token);
    }

    private String buildUrl(String token) {
        var applicationUrl = movieDbProperties.getApplicationUrl();
        return String.format("%s/api/confirm?token=%s", applicationUrl, token);
    }
}
