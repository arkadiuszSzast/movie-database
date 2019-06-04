package com.movie.database.movie_database.user;

import com.movie.database.movie_database.support.ValidationResult;
import com.movie.database.movie_database.user.confirmation.ConfirmationMailSenderService;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import com.movie.database.movie_database.user.exception.ApplicationUserEmailTakenException;
import com.movie.database.movie_database.user.exception.ApplicationUserUsernameTakenException;
import com.movie.database.movie_database.user.role.RoleGetService;
import com.movie.database.movie_database.user.role.domain.Role;
import com.movie.database.movie_database.user.validation.ApplicationUserValidation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.management.relation.RoleNotFoundException;
import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationUserCreateServiceTest {

    @Mock
    private ApplicationUserRepository applicationUserRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private ConfirmationMailSenderService confirmationMailSenderService;
    @Mock
    private ApplicationUserValidation applicationUserValidation;
    @Mock
    private RoleGetService roleGetService;
    @InjectMocks
    private ApplicationUserCreateService applicationUserCreateService;

    @Test
    @DisplayName("Should create application user")
    public void shouldCreateApplicationUser() throws IOException, RoleNotFoundException {
        //arrange
        var user = new ApplicationUser("username", "password", "email");
        var encodedPassword = "encodedPassword";
        var role = new Role("USER");
        when(applicationUserValidation.emailNotTaken(user)).thenReturn(new ValidationResult<>(true, user, Collections.emptyList()));
        when(applicationUserValidation.usernameNotTaken(user)).thenReturn(new ValidationResult<>(true, user, Collections.emptyList()));
        when(roleGetService.getUserRole()).thenReturn(role);
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn(encodedPassword);
        when(applicationUserRepository.save(user)).thenReturn(user);

        //act
        applicationUserCreateService.create(user);

        //assert
        assertThat(user.getPassword()).isEqualTo(encodedPassword);
        assertThat(user.isActive()).isFalse();
        assertThat(user.getRoles()).contains(role);
    }

    @Test
    @DisplayName("Should throw exception when username is taken")
    public void shouldThrowExceptionWhenUsernameIsTaken() {
        //arrange
        var user = new ApplicationUser("username", "password", "email");
        when(applicationUserValidation.emailNotTaken(user)).thenReturn(new ValidationResult<>(true, user, Collections.emptyList()));
        when(applicationUserValidation.usernameNotTaken(user)).thenReturn(new ValidationResult<>(false, user, Collections.emptyList()));

        //act && assert
        assertThrows(ApplicationUserUsernameTakenException.class, () -> applicationUserCreateService.create(user));
    }

    @Test
    @DisplayName("Should throw exception when email is taken")
    public void shouldThrowExceptionWhenEmailIsTaken() {
        //arrange
        var user = new ApplicationUser("username", "password", "email");
        when(applicationUserValidation.emailNotTaken(user)).thenReturn(new ValidationResult<>(false, user, Collections.emptyList()));

        //act && assert
        assertThrows(ApplicationUserEmailTakenException.class, () -> applicationUserCreateService.create(user));
    }

}