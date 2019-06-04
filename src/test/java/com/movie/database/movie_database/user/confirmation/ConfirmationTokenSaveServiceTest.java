package com.movie.database.movie_database.user.confirmation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.movie.database.movie_database.user.ApplicationUserGetService;
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

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfirmationTokenSaveServiceTest {

    @Mock
    private ApplicationUserGetService applicationUserGetService;
    @Mock
    private ApplicationUserTokenRepository applicationUserTokenRepository;
    @InjectMocks
    private ConfirmationTokenSaveService confirmationTokenSaveService;

    @Test
    @DisplayName("Should update confirmation token when already exists")
    public void shouldUpdateConfirmationTokenWhenAlreadyExists() {
        //arrange
        var userId = UUID.fromString("78688d94-86ea-11e9-bc42-526af7764f64");
        var oldToken = JWT.create().withSubject(userId.toString()).withExpiresAt(new Date()).sign(Algorithm.HMAC256("secret"));
        var token = JWT.create().withSubject(userId.toString()).withExpiresAt(Date.from(Instant.now().plusSeconds(30))).sign(Algorithm.HMAC256("secret"));
        var user = new ApplicationUser("username", "password", "email@email.ca");
        var applicationUserToken = new ApplicationUserToken(user, oldToken, TokenType.CONFIRMATION);
        when(applicationUserGetService.findById(userId)).thenReturn(user);
        when(applicationUserTokenRepository.findByApplicationUserAndTokenType(user, TokenType.CONFIRMATION)).thenReturn(Optional.of(applicationUserToken));

        //act
        confirmationTokenSaveService.save(token);

        //assert
        assertThat(applicationUserToken.getToken()).isEqualTo(token);
    }

    @Test
    @DisplayName("Should create new confirmation token when not exists")
    public void shouldCreateNewConfirmationTokenWhenNotExist() {
        //arrange
        var userId = UUID.fromString("78688d94-86ea-11e9-bc42-526af7764f64");
        var token = JWT.create().withSubject(userId.toString()).withExpiresAt(Date.from(Instant.now().plusSeconds(30))).sign(Algorithm.HMAC256("secret"));
        var user = new ApplicationUser("username", "password", "email@email.ca");
        var applicationUserToken = new ApplicationUserToken(user, token, TokenType.CONFIRMATION);
        when(applicationUserGetService.findById(userId)).thenReturn(user);
        when(applicationUserTokenRepository.findByApplicationUserAndTokenType(user, TokenType.CONFIRMATION)).thenReturn(Optional.empty());

        //act
        confirmationTokenSaveService.save(token);

        //assert
        assertThat(applicationUserToken.getToken()).isEqualTo(token);
    }
}