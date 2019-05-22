package com.movie.database.movie_database.user.confirmation;

import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserConfirmationService {

    private final ConfirmationTokenGetService confirmationTokenGetService;
    private final ApplicationUserRepository applicationUserRepository;
    private final ConfirmationTokenRemoveService confirmationTokenRemoveService;

    public ApplicationUserConfirmationService(ConfirmationTokenGetService confirmationTokenGetService,
                                              ApplicationUserRepository applicationUserRepository,
                                              ConfirmationTokenRemoveService confirmationTokenRemoveService) {
        this.confirmationTokenGetService = confirmationTokenGetService;
        this.applicationUserRepository = applicationUserRepository;
        this.confirmationTokenRemoveService = confirmationTokenRemoveService;
    }

    public void confirm(String token) {
        var confirmationToken = confirmationTokenGetService.getByToken(token);
        var applicationUser = confirmationToken.getApplicationUser();
        applicationUser.setActive(true);
        applicationUserRepository.save(applicationUser);
        confirmationTokenRemoveService.remove(confirmationToken);
    }
}
