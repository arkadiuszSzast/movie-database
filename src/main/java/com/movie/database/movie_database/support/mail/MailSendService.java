package com.movie.database.movie_database.support.mail;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MailSendService {

    private final SendGrid sendGrid;

    public MailSendService(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    public void send(Mail sendGridMail) throws IOException {
        var mail = sendGridMail.build();
        var request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail);
        sendGrid.api(request);
    }
}
