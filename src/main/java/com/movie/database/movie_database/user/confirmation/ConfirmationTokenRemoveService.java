package com.movie.database.movie_database.user.confirmation;

import com.movie.database.movie_database.user.confirmation.domain.ConfirmationToken;
import com.movie.database.movie_database.user.confirmation.domain.ConfirmationTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenRemoveService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationTokenRemoveService(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public void remove(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.delete(confirmationToken);
    }
}
