package com.movie.database.movie_database.user;

import com.movie.database.movie_database.user.confirmation.ConfirmationMailSenderService;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import com.movie.database.movie_database.user.exception.ApplicationUserEmailTakenException;
import com.movie.database.movie_database.user.exception.ApplicationUserUsernameTakenException;
import com.movie.database.movie_database.user.validation.ApplicationUserValidation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ApplicationUserCreateService {

    private final ApplicationUserRepository applicationUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationMailSenderService confirmationMailSenderService;
    private final ApplicationUserValidation applicationUserValidation;

    public ApplicationUserCreateService(ApplicationUserRepository applicationUserRepository,
                                        BCryptPasswordEncoder bCryptPasswordEncoder,
                                        ConfirmationMailSenderService confirmationMailSenderService,
                                        ApplicationUserValidation applicationUserValidation) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.confirmationMailSenderService = confirmationMailSenderService;
        this.applicationUserValidation = applicationUserValidation;
    }

    public void create(ApplicationUser applicationUser) throws IOException {
        applicationUserValidation.emailNotTaken(applicationUser).throwIfInvalid(ApplicationUserEmailTakenException::new);
        applicationUserValidation.usernameNotTaken(applicationUser).throwIfInvalid(ApplicationUserUsernameTakenException::new);
        var encryptedPassword = bCryptPasswordEncoder.encode(applicationUser.getPassword());
        applicationUser.setPassword(encryptedPassword);
        var savedApplicationUser = applicationUserRepository.save(applicationUser);
        confirmationMailSenderService.send(applicationUser, savedApplicationUser);
    }
}