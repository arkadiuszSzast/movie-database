package com.movie.database.movie_database.user.confirmation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.movie.database.movie_database.user.confirmation.exception.ConfirmationTokenNotFoundException;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.token.domain.ApplicationUserToken;
import com.movie.database.movie_database.user.token.domain.ApplicationUserTokenRepository;
import com.movie.database.movie_database.user.token.model.TokenType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfirmationTokenGetServiceTest {

    @Mock
    private ApplicationUserTokenRepository applicationUserTokenRepository;
    @InjectMocks
    private ConfirmationTokenGetService confirmationTokenGetService;

    @Test
    @DisplayName("Should return confirmation token")
    public void shouldReturnConfirmationToken() {
        //arrange
        var token = JWT.create().withSubject("78688d94-86ea-11e9-bc42-526af7764f64").withExpiresAt(new Date()).sign(Algorithm.HMAC256("secret"));
        var user = new ApplicationUser("username", "password", "email@email.ca");
        var applicationUserToken = new ApplicationUserToken(user, token, TokenType.CONFIRMATION);
        when(applicationUserTokenRepository.findByTokenAndTokenType(token, TokenType.CONFIRMATION)).thenReturn(Optional.of(applicationUserToken));

        //act
        var returnedToken = confirmationTokenGetService.getByToken(token);

        //assert
        assertThat(returnedToken).isEqualTo(applicationUserToken);
    }

    @Test
    @DisplayName("Should throw exception when applicationUserToken not found")
    public void shouldThrowExceptionWhenApplicationUserTokenNotFound() {
        //arrange
        var token = JWT.create().withSubject("78688d94-86ea-11e9-bc42-526af7764f64").withExpiresAt(new Date()).sign(Algorithm.HMAC256("secret"));
        when(applicationUserTokenRepository.findByTokenAndTokenType(token, TokenType.CONFIRMATION)).thenReturn(Optional.empty());

        //act && assert
        assertThrows(ConfirmationTokenNotFoundException.class, () -> confirmationTokenGetService.getByToken(token));
    }

}