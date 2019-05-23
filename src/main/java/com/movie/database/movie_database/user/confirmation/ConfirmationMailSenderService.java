package com.movie.database.movie_database.user.confirmation;

import com.movie.database.movie_database.support.mail.MailSenderService;
import com.movie.database.movie_database.support.mail.SendGridMailProvider;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ConfirmationMailSenderService {

    private final ConfirmationUrlCreateService confirmationUrlCreateService;
    private final SendGridMailProvider sendGridMailProvider;
    private final MailSenderService mailSenderService;

    public ConfirmationMailSenderService(ConfirmationUrlCreateService confirmationUrlCreateService, SendGridMailProvider sendGridMailProvider, MailSenderService mailSenderService) {
        this.confirmationUrlCreateService = confirmationUrlCreateService;
        this.sendGridMailProvider = sendGridMailProvider;
        this.mailSenderService = mailSenderService;
    }

    public void send(ApplicationUser applicationUser, ApplicationUser savedApplicationUser) throws IOException {
        var confirmationUrl = confirmationUrlCreateService.create(savedApplicationUser.getId());
        var mail = sendGridMailProvider.createConfirmationMail(applicationUser, confirmationUrl);
        mailSenderService.send(mail);
    }
}
