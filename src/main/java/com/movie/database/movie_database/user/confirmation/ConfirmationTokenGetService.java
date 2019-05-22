package com.movie.database.movie_database.user.confirmation;

import com.movie.database.movie_database.user.confirmation.domain.ConfirmationToken;
import com.movie.database.movie_database.user.confirmation.domain.ConfirmationTokenRepository;
import com.movie.database.movie_database.user.confirmation.exception.ConfirmationTokenNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenGetService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationTokenGetService(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public ConfirmationToken getByToken(String token) {
        return confirmationTokenRepository.findByToken(token)
                .orElseThrow(ConfirmationTokenNotFoundException::new);
    }
}
