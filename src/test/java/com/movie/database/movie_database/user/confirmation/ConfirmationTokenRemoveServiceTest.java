package com.movie.database.movie_database.user.confirmation;

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

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConfirmationTokenRemoveServiceTest {

    @Mock
    private ApplicationUserTokenRepository applicationUserTokenRepository;
    @InjectMocks
    private ConfirmationTokenRemoveService confirmationTokenRemoveService;


    @Test
    @DisplayName("Should remove confirmation token")
    public void shouldRemoveConfirmationToken() {
        //arrange
        var user = new ApplicationUser("username", "password", "email@email.com");
        var token = UUID.randomUUID().toString();
        var applicationUserToken = new ApplicationUserToken(user, token, TokenType.RESET_PASSWORD);

        //act
        confirmationTokenRemoveService.remove(applicationUserToken);

        //assert
        verify(applicationUserTokenRepository, times(1)).delete(applicationUserToken);
    }
}