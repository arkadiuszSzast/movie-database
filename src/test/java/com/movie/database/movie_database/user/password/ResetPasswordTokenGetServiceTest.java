package com.movie.database.movie_database.user.password;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.password.exception.ResetPasswordTokenNotFoundException;
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
class ResetPasswordTokenGetServiceTest {

    @Mock
    private ApplicationUserTokenRepository applicationUserTokenRepository;
    @InjectMocks
    private ResetPasswordTokenGetService resetPasswordTokenGetService;

    @Test
    @DisplayName("Should return reset password token")
    public void shouldReturnResetPasswordToken() {
        //arrange
        var token = JWT.create().withSubject("78688d94-86ea-11e9-bc42-526af7764f64").withExpiresAt(new Date()).sign(Algorithm.HMAC256("secret"));
        var user = new ApplicationUser("username", "password", "email@email.ca");
        var applicationUserToken = new ApplicationUserToken(user, token, TokenType.RESET_PASSWORD);
        when(applicationUserTokenRepository.findByTokenAndTokenType(token, TokenType.RESET_PASSWORD)).thenReturn(Optional.of(applicationUserToken));

        //act
        var returnedToken = resetPasswordTokenGetService.getByToken(token);

        //assert
        assertThat(returnedToken).isEqualTo(applicationUserToken);
    }

    @Test
    @DisplayName("Should throw exception when applicationUserToken not found")
    public void shouldThrowExceptionWhenApplicationUserTokenNotFound() {
        //arrange
        var token = JWT.create().withSubject("78688d94-86ea-11e9-bc42-526af7764f64").withExpiresAt(new Date()).sign(Algorithm.HMAC256("secret"));
        when(applicationUserTokenRepository.findByTokenAndTokenType(token, TokenType.RESET_PASSWORD)).thenReturn(Optional.empty());

        //act && assert
        assertThrows(ResetPasswordTokenNotFoundException.class, () -> resetPasswordTokenGetService.getByToken(token));
    }
}