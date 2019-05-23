package com.movie.database.movie_database.user.password;

import com.auth0.jwt.JWT;
import com.movie.database.movie_database.user.ApplicationUserGetService;
import com.movie.database.movie_database.user.token.domain.ApplicationUserToken;
import com.movie.database.movie_database.user.token.domain.ApplicationUserTokenRepository;
import com.movie.database.movie_database.user.token.model.TokenType;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ResetPasswordTokenSaveService {

    private final ApplicationUserTokenRepository applicationUserTokenRepository;
    private final ApplicationUserGetService applicationUserGetService;

    public ResetPasswordTokenSaveService(ApplicationUserTokenRepository applicationUserTokenRepository,
                                         ApplicationUserGetService applicationUserGetService) {
        this.applicationUserTokenRepository = applicationUserTokenRepository;
        this.applicationUserGetService = applicationUserGetService;
    }

    public void save(String token) {
        var userId = JWT.decode(token).getSubject();
        var applicationUser = applicationUserGetService.findById(UUID.fromString(userId));
        var applicationUserToken = applicationUserTokenRepository.findByApplicationUserAndTokenType(applicationUser, TokenType.RESET_PASSWORD)
                .orElse(new ApplicationUserToken(applicationUser, token, TokenType.RESET_PASSWORD));
        applicationUserToken.setToken(token);
        applicationUserTokenRepository.save(applicationUserToken);
    }
}
