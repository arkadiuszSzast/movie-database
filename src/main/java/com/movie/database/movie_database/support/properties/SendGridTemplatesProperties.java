package com.movie.database.movie_database.support.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties("mail.template")
@PropertySource("classpath:application.yml")
public class SendGridTemplatesProperties {

    private String confirmRegistration;
    private String resetPassword;

    public String getConfirmRegistration() {
        return confirmRegistration;
    }

    public void setConfirmRegistration(String confirmRegistration) {
        this.confirmRegistration = confirmRegistration;
    }

    public String getResetPassword() {
        return resetPassword;
    }

    public void setResetPassword(String resetPassword) {
        this.resetPassword = resetPassword;
    }
}
