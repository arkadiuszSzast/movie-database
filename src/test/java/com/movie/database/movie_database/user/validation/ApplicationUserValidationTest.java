package com.movie.database.movie_database.user.validation;

import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class ApplicationUserValidationTest {

    @Mock
    private ApplicationUserRepository applicationUserRepository;

    @InjectMocks
    ApplicationUserValidation applicationUserValidation;

    @Test
    @DisplayName("When username is taken should get invalid")
    public void shouldGetInvalidWhenUsernameIsTaken() {
        //arrange
        var applicationUser = new ApplicationUser("admin", "admin", "mail");
        when(applicationUserRepository.findByUsername(applicationUser.getUsername())).thenReturn(Optional.of(applicationUser));
        //act
        var applicationUserValidationResult = applicationUserValidation.usernameNotTaken(applicationUser);
        //assert
        assertThat(applicationUserValidationResult.isValid()).isFalse();
    }

    @Test
    @DisplayName("When email is taken should get invalid")
    public void shouldGetInvalidWhenEmailIsTaken() {
        //arrange
        var applicationUser = new ApplicationUser("admin", "admin", "mail");
        when(applicationUserRepository.findByEmail(applicationUser.getEmail())).thenReturn(Optional.of(applicationUser));
        //act
        var applicationUserValidationResult = applicationUserValidation.emailNotTaken(applicationUser);
        //assert
        assertThat(applicationUserValidationResult.isValid()).isFalse();
    }

    @Test
    @DisplayName("When username is not taken should get valid")
    public void shouldGetValidWhenUsernameIsNotTaken() {
        //arrange
        var applicationUser = new ApplicationUser("admin", "admin", "mail");
        when(applicationUserRepository.findByUsername(applicationUser.getUsername())).thenReturn(Optional.empty());
        //act
        var applicationUserValidationResult = applicationUserValidation.usernameNotTaken(applicationUser);
        //assert
        assertThat(applicationUserValidationResult.isValid()).isTrue();
    }

    @Test
    @DisplayName("When email is not taken should get valid")
    public void shouldGetValidWhenEmailIsNotTaken() {
        //arrange
        var applicationUser = new ApplicationUser("admin", "admin", "mail");
        when(applicationUserRepository.findByEmail(applicationUser.getEmail())).thenReturn(Optional.empty());
        //act
        var applicationUserValidationResult = applicationUserValidation.emailNotTaken(applicationUser);
        //assert
        assertThat(applicationUserValidationResult.isValid()).isTrue();
    }

}