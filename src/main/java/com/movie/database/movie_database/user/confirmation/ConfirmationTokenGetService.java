package com.movie.database.movie_database.user.confirmation;

import com.movie.database.movie_database.user.confirmation.exception.ConfirmationTokenNotFoundException;
import com.movie.database.movie_database.user.token.domain.ApplicationUserToken;
import com.movie.database.movie_database.user.token.domain.ApplicationUserTokenRepository;
import com.movie.database.movie_database.user.token.model.TokenType;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenGetService {

    private final ApplicationUserTokenRepository applicationUserTokenRepository;

    public ConfirmationTokenGetService(ApplicationUserTokenRepository applicationUserTokenRepository) {
        this.applicationUserTokenRepository = applicationUserTokenRepository;
    }

    public ApplicationUserToken getByToken(String token) {
        return applicationUserTokenRepository.findByTokenAndTokenType(token, TokenType.CONFIRMATION)
                .orElseThrow(ConfirmationTokenNotFoundException::new);
    }
}
