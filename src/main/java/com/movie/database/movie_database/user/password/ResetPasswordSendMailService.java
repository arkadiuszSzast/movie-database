package com.movie.database.movie_database.user.password;

import com.movie.database.movie_database.support.mail.MailSenderService;
import com.movie.database.movie_database.support.mail.SendGridMailProvider;
import com.movie.database.movie_database.user.ApplicationUserGetService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ResetPasswordSendMailService {

    private final ApplicationUserGetService applicationUserGetService;
    private final SendGridMailProvider sendGridMailProvider;
    private final ResetPasswordUrlCreateService resetPasswordUrlCreateService;
    private final MailSenderService mailSenderService;

    public ResetPasswordSendMailService(ApplicationUserGetService applicationUserGetService,
                                        SendGridMailProvider sendGridMailProvider,
                                        ResetPasswordUrlCreateService resetPasswordUrlCreateService,
                                        MailSenderService mailSenderService) {
        this.applicationUserGetService = applicationUserGetService;
        this.sendGridMailProvider = sendGridMailProvider;
        this.resetPasswordUrlCreateService = resetPasswordUrlCreateService;
        this.mailSenderService = mailSenderService;
    }

    public void send(String email) throws IOException {
        var applicationUserRest = applicationUserGetService.findRestByEmail(email);
        var resetPasswordUrl = resetPasswordUrlCreateService.create(applicationUserRest.getId());
        var mail = sendGridMailProvider.createResetPasswordMail(applicationUserRest, resetPasswordUrl);
        mailSenderService.send(mail);
    }
}
