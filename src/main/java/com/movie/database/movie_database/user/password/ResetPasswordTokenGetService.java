package com.movie.database.movie_database.user.password;

import com.movie.database.movie_database.user.password.exception.ResetPasswordTokenNotFoundException;
import com.movie.database.movie_database.user.token.domain.ApplicationUserToken;
import com.movie.database.movie_database.user.token.domain.ApplicationUserTokenRepository;
import com.movie.database.movie_database.user.token.model.TokenType;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordTokenGetService {

    private final ApplicationUserTokenRepository applicationUserTokenRepository;

    public ResetPasswordTokenGetService(ApplicationUserTokenRepository applicationUserTokenRepository) {
        this.applicationUserTokenRepository = applicationUserTokenRepository;
    }

    public ApplicationUserToken getByToken(String token) {
        return applicationUserTokenRepository.findByTokenAndTokenType(token, TokenType.RESET_PASSWORD)
                .orElseThrow(ResetPasswordTokenNotFoundException::new);
    }
}
