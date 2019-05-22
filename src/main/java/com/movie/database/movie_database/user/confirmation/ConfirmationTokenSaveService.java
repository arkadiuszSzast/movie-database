package com.movie.database.movie_database.user.confirmation;

import com.auth0.jwt.JWT;
import com.movie.database.movie_database.user.ApplicationUserGetService;
import com.movie.database.movie_database.user.confirmation.domain.ConfirmationToken;
import com.movie.database.movie_database.user.confirmation.domain.ConfirmationTokenRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ConfirmationTokenSaveService {

    private final ApplicationUserGetService applicationUserGetService;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationTokenSaveService(ApplicationUserGetService applicationUserGetService,
                                        ConfirmationTokenRepository confirmationTokenRepository) {
        this.applicationUserGetService = applicationUserGetService;
        this.confirmationTokenRepository = confirmationTokenRepository;
    }


    public void save(String token) {
        var userId = JWT.decode(token).getSubject();
        var applicationUser = applicationUserGetService.findById(UUID.fromString(userId));
        confirmationTokenRepository.save(new ConfirmationToken(applicationUser, token));
    }
}
