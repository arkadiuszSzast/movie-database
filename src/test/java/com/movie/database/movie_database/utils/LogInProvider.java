package com.movie.database.movie_database.utils;

import com.movie.database.movie_database.config.security.jwt.JWTGenerateService;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogInProvider {

    @Autowired
    private UserProvider userProvider;
    @Autowired
    private JWTGenerateService jwtGenerateService;

    public String logInAsUser() {
        var account = userProvider.createActivatedUserWithUserRole();
        return logIn(account);
    }

    public String logInAsAdmin() {
        var account = userProvider.createActivatedUserWithAdminRole();
        return logIn(account);
    }

    public String getRefreshToken() {
        var account = userProvider.createActivatedUserWithUserRole();
        return jwtGenerateService.getRefreshToken(account.getId());
    }

    private String logIn(ApplicationUser account) {
        return jwtGenerateService.getAccessToken(account.getId());
    }
}
