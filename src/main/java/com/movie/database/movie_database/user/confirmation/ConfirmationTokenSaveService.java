package com.movie.database.movie_database.user.confirmation;

import com.auth0.jwt.JWT;
import com.movie.database.movie_database.user.ApplicationUserGetService;
import com.movie.database.movie_database.user.token.domain.ApplicationUserToken;
import com.movie.database.movie_database.user.token.domain.ApplicationUserTokenRepository;
import com.movie.database.movie_database.user.token.model.TokenType;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ConfirmationTokenSaveService {

    private final ApplicationUserGetService applicationUserGetService;
    private final ApplicationUserTokenRepository applicationUserTokenRepository;

    public ConfirmationTokenSaveService(ApplicationUserGetService applicationUserGetService,
                                        ApplicationUserTokenRepository applicationUserTokenRepository) {
        this.applicationUserGetService = applicationUserGetService;
        this.applicationUserTokenRepository = applicationUserTokenRepository;
    }


    public void save(String token) {
        var userId = JWT.decode(token).getSubject();
        var applicationUser = applicationUserGetService.findById(UUID.fromString(userId));
        var applicationUserToken = applicationUserTokenRepository.findByApplicationUserAndTokenType(applicationUser, TokenType.CONFIRMATION)
                .orElse(new ApplicationUserToken(applicationUser, token, TokenType.CONFIRMATION));
        applicationUserToken.setToken(token);
        applicationUserTokenRepository.save(applicationUserToken);
    }
}
