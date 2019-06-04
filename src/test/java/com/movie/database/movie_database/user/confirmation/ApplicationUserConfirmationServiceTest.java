package com.movie.database.movie_database.user.confirmation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.movie.database.movie_database.config.security.jwt.ConfirmationTokenService;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import com.movie.database.movie_database.user.token.domain.ApplicationUserToken;
import com.movie.database.movie_database.user.token.model.TokenType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationUserConfirmationServiceTest {

    @Mock
    private ConfirmationTokenGetService confirmationTokenGetService;
    @Mock
    private ApplicationUserRepository applicationUserRepository;
    @Mock
    private ConfirmationTokenRemoveService confirmationTokenRemoveService;
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @InjectMocks
    private ApplicationUserConfirmationService applicationUserConfirmationService;

    @Test
    @DisplayName("Should activate user")
    public void shouldActivateUser() {
        //arrange
        var token = JWT.create().withSubject("78688d94-86ea-11e9-bc42-526af7764f64").withExpiresAt(new Date()).sign(Algorithm.HMAC256("secret"));
        var user = new ApplicationUser("username", "password", "email@email.ca");
        var applicationUserToken = new ApplicationUserToken(user, token, TokenType.CONFIRMATION);
        when(confirmationTokenGetService.getByToken(token)).thenReturn(applicationUserToken);

        //act
        applicationUserConfirmationService.confirm(token);

        //assert
        assertThat(applicationUserToken.getApplicationUser().isActive()).isTrue();
    }

}