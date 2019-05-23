package com.movie.database.movie_database.support.mail;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MailSenderService {

    private final SendGrid sendGrid;

    public MailSenderService(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    @Async
    public void send(Mail sendGridMail) throws IOException {
        var mail = sendGridMail.build();
        var request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail);
        sendGrid.api(request);
    }
}
