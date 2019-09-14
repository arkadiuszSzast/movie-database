package com.movie.database.movie_database.utils;

import com.movie.database.movie_database.config.security.jwt.JWTGenerateService;
import org.springframework.stereotype.Component;

@Component
public class UserTokenProvider {

    private final UserProvider userProvider;
    private final JWTGenerateService jwtGenerateService;

    public UserTokenProvider(UserProvider userProvider, JWTGenerateService jwtGenerateService) {
        this.userProvider = userProvider;
        this.jwtGenerateService = jwtGenerateService;
    }

    public String getConfirmationToken() {
        var user = userProvider.createRandomInactivUser();
        return jwtGenerateService.getConfirmationToken(user.getId());
    }
}
