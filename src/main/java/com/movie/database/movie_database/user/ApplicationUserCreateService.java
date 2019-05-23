package com.movie.database.movie_database.user;

import com.movie.database.movie_database.user.confirmation.ConfirmationMailSenderService;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ApplicationUserCreateService {

    private final ApplicationUserRepository applicationUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationMailSenderService confirmationMailSenderService;

    public ApplicationUserCreateService(ApplicationUserRepository applicationUserRepository,
                                        BCryptPasswordEncoder bCryptPasswordEncoder,
                                        ConfirmationMailSenderService confirmationMailSenderService) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.confirmationMailSenderService = confirmationMailSenderService;
    }

    public void create(ApplicationUser applicationUser) throws IOException {
        var encryptedPassword = bCryptPasswordEncoder.encode(applicationUser.getPassword());
        applicationUser.setPassword(encryptedPassword);
        var savedApplicationUser = applicationUserRepository.save(applicationUser);
        confirmationMailSenderService.send(applicationUser, savedApplicationUser);
    }
}