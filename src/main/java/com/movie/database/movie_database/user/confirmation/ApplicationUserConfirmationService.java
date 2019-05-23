package com.movie.database.movie_database.user.confirmation;

import com.movie.database.movie_database.config.security.jwt.ConfirmationTokenService;
import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import com.movie.database.movie_database.user.token.domain.ApplicationUserToken;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserConfirmationService {

    private final ConfirmationTokenGetService confirmationTokenGetService;
    private final ApplicationUserRepository applicationUserRepository;
    private final ConfirmationTokenRemoveService confirmationTokenRemoveService;
    private final ConfirmationTokenService confirmationTokenService;

    public ApplicationUserConfirmationService(ConfirmationTokenGetService confirmationTokenGetService,
                                              ApplicationUserRepository applicationUserRepository,
                                              ConfirmationTokenRemoveService confirmationTokenRemoveService,
                                              ConfirmationTokenService confirmationTokenService) {
        this.confirmationTokenGetService = confirmationTokenGetService;
        this.applicationUserRepository = applicationUserRepository;
        this.confirmationTokenRemoveService = confirmationTokenRemoveService;
        this.confirmationTokenService = confirmationTokenService;
    }

    public void confirm(String token) {
        var confirmationToken = confirmationTokenGetService.getByToken(token);
        confirmationTokenService.validate(token);
        activateUser(confirmationToken);
        confirmationTokenRemoveService.remove(confirmationToken);
    }

    private void activateUser(ApplicationUserToken confirmationToken) {
        var applicationUser = confirmationToken.getApplicationUser();
        applicationUser.setActive(true);
        applicationUserRepository.save(applicationUser);
    }
}
