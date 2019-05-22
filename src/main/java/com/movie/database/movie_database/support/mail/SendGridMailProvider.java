package com.movie.database.movie_database.support.mail;

import com.movie.database.movie_database.support.properties.MovieDbProperties;
import com.movie.database.movie_database.support.properties.SendGridTemplatesProperties;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SendGridMailProvider {

    private final SendGridTemplatesProperties sendGridTemplatesProperties;
    private final MovieDbProperties movieDbProperties;

    public SendGridMailProvider(SendGridTemplatesProperties sendGridTemplatesProperties, MovieDbProperties movieDbProperties) {
        this.sendGridTemplatesProperties = sendGridTemplatesProperties;
        this.movieDbProperties = movieDbProperties;
    }

    public Mail createConfirmationMail(ApplicationUser applicationUser, String confirmationUrl) {
        var sendTo = new Email(applicationUser.getEmail());
        var dynamicVariables = Map.of("username", applicationUser.getUsername(),
                "confirm-url", confirmationUrl);
        var basicMail = createBasicMail(sendTo, dynamicVariables);
        basicMail.setTemplateId(sendGridTemplatesProperties.getConfirmRegistration());
        return basicMail;
    }

    private Mail createBasicMail(Email to, Map<String, String> dynamicVariables) {
        var from = new Email(movieDbProperties.getMailSender());
        var basicMail = new Mail(from, "subject", to, new Content("text/plain", "content"));
        var mailPersonalization = basicMail.getPersonalization().get(0);
        dynamicVariables.forEach(mailPersonalization::addDynamicTemplateData);
        return basicMail;
    }
}
