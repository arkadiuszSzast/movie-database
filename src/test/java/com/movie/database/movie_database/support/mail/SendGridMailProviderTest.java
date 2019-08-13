package com.movie.database.movie_database.support.mail;

import com.movie.database.movie_database.support.properties.MovieDbProperties;
import com.movie.database.movie_database.support.properties.SendGridTemplatesProperties;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.model.ApplicationUserRest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class SendGridMailProviderTest {

    @Mock
    private SendGridTemplatesProperties sendGridTemplatesProperties;
    @Mock
    private MovieDbProperties movieDbProperties;
    @InjectMocks
    private SendGridMailProvider sendGridMailProvider;

    @Test
    public void shouldGetConfirmationMail() {
        //arrange
        var applicationUser = new ApplicationUser("admin", "admin", "mail");
        var confirmationUrl = "confirmationUrl";
        var confirmationtemplateId = "confirmationTemplateId";
        when(sendGridTemplatesProperties.getConfirmRegistration()).thenReturn(confirmationtemplateId);

        //act
        var confirmationMail = sendGridMailProvider.createConfirmationMail(applicationUser, confirmationUrl);

        //assert
        assertThat(confirmationMail.getTemplateId()).isEqualTo(confirmationtemplateId);
        assertThat(confirmationMail.getPersonalization().get(0).getDynamicTemplateData().containsValue(confirmationUrl));
        assertThat(confirmationMail.getPersonalization().get(0).getDynamicTemplateData().containsValue(applicationUser.getUsername()));
    }

    @Test
    public void shouldGetResetPasswordMail() {
        //arrange
        var applicationUser = new ApplicationUserRest(UUID.randomUUID(), "admin", "mail", "default", Collections.emptyList());
        var resetPasswordUrl = "confirmationUrl";
        var resetPasswordtemplateId = "resetPasswordTemplateId";
        when(sendGridTemplatesProperties.getResetPassword()).thenReturn(resetPasswordtemplateId);

        //act
        var confirmationMail = sendGridMailProvider.createResetPasswordMail(applicationUser, resetPasswordUrl);

        //assert
        assertThat(confirmationMail.getTemplateId()).isEqualTo(resetPasswordtemplateId);
        assertThat(confirmationMail.getPersonalization().get(0).getDynamicTemplateData().containsValue(resetPasswordUrl));
        assertThat(confirmationMail.getPersonalization().get(0).getDynamicTemplateData().containsValue(applicationUser.getUsername()));
    }

}