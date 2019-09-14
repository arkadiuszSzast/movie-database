package com.movie.database.movie_database.user;

import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import com.movie.database.movie_database.user.role.domain.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationUserUpdateServiceTest {

    @Mock
    private ApplicationUserGetService applicationUserGetService;
    @Mock
    private ApplicationUserRepository applicationUserRepository;
    @InjectMocks
    private ApplicationUserUpdateService applicationUserUpdateService;

    @Test
    @DisplayName("Should update user roles")
    public void shouldUpdateUserRoles() {
        //arrange
        var userId = UUID.fromString("b1f134c8-0229-4297-ac7f-ad41044706d2");
        var userRole = new Role("USER");
        var adminRole = new Role("ADMIN");
        var user = new ApplicationUser("username", "password", "email@email.ca");
        user.setRoles(List.of(userRole));
        when(applicationUserGetService.findById(userId)).thenReturn(user);

        //act
        applicationUserUpdateService.updateRoles(userId, List.of(userRole, adminRole));

        //assert
        assertThat(user.getRoles()).containsExactly(userRole, adminRole);
    }

    @Test
    @DisplayName("Should update user")
    public void shouldUpdateUser() {
        //arrange
        var user = new ApplicationUser("username", "password", "email@email.ca");

        //act
        applicationUserUpdateService.update(user);

        //assert
        verify(applicationUserRepository, times(1)).save(user);
    }
}