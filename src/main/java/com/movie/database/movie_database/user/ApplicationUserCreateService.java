package com.movie.database.movie_database.user;

import com.movie.database.movie_database.support.mail.MailSendService;
import com.movie.database.movie_database.support.mail.SendGridMailProvider;
import com.movie.database.movie_database.user.confirmation.ConfirmationUrlCreateService;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ApplicationUserCreateService {

    private final ApplicationUserRepository applicationUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MailSendService mailSendService;
    private final SendGridMailProvider sendGridMailProvider;
    private final ConfirmationUrlCreateService confirmationUrlCreateService;

    public ApplicationUserCreateService(ApplicationUserRepository applicationUserRepository,
                                        BCryptPasswordEncoder bCryptPasswordEncoder,
                                        MailSendService mailSendService,
                                        SendGridMailProvider sendGridMailProvider,
                                        ConfirmationUrlCreateService confirmationUrlCreateService) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mailSendService = mailSendService;
        this.sendGridMailProvider = sendGridMailProvider;
        this.confirmationUrlCreateService = confirmationUrlCreateService;
    }

    public void create(ApplicationUser applicationUser) throws IOException {
        var encryptedPassword = bCryptPasswordEncoder.encode(applicationUser.getPassword());
        applicationUser.setPassword(encryptedPassword);
        var savedApplicationUser = applicationUserRepository.save(applicationUser);
        var confirmationUrl = confirmationUrlCreateService.create(savedApplicationUser.getId());
        var mail = sendGridMailProvider.createConfirmationMail(applicationUser, confirmationUrl);
        mailSendService.send(mail);
    }


}