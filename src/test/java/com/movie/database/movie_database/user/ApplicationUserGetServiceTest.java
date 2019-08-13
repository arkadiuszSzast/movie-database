package com.movie.database.movie_database.user;

import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import com.movie.database.movie_database.user.exception.ApplicationUserNotFoundException;
import com.movie.database.movie_database.user.model.ApplicationUserRest;
import com.movie.database.movie_database.user.model.ApplicationUserRestMapper;
import com.movie.database.movie_database.user.role.domain.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationUserGetServiceTest {

    @Mock
    private ApplicationUserRepository applicationUserRepository;
    @Mock
    private ApplicationUserRestMapper applicationUserRestMapper;
    @InjectMocks
    private ApplicationUserGetService applicationUserGetService;

    @Test
    @DisplayName("Should get list of application users rest")
    public void shouldGetListOfApplicationUsersRest() {
        //arrange
        var userId = UUID.fromString("3cf84d2c-e9c5-43d1-867b-d8e5ed97557d");
        var adminId = UUID.fromString("cd7084ab-fa97-4342-94ef-0d03adb1efcd");
        var userRole = new Role("USER");
        var adminRole = new Role("ADMIN");
        var applicationUser = new ApplicationUser("username", "password", "email@email.ca");
        applicationUser.setRoles(List.of(userRole));
        applicationUser.setId(userId);
        var adminUser = new ApplicationUser("adminUsername", "adminPassword", "adminEmail@email.ca");
        adminUser.setRoles(List.of(userRole, adminRole));
        adminUser.setId(adminId);
        var userRest = new ApplicationUserRest(userId, applicationUser.getUsername(), applicationUser.getEmail(), applicationUser.getAvatarUrl().orElse("default"), List.of("USER"));
        var adminRest = new ApplicationUserRest(adminId, adminUser.getUsername(), adminUser.getEmail(), adminUser.getAvatarUrl().orElse("default"), List.of("ADMIN"));
        when(applicationUserRepository.findAll()).thenReturn(List.of(applicationUser, adminUser));
        when(applicationUserRestMapper.mapToRest(List.of(applicationUser, adminUser))).thenReturn(List.of(userRest, adminRest));

        //act
        var applicationUsersRest = applicationUserGetService.findAll();

        //assert
        assertThat(applicationUsersRest).containsExactlyInAnyOrder(userRest, adminRest);
    }

    @Test
    @DisplayName("Should get application user rest by id when exists")
    public void shouldGetApplicationUserRestByIdWhenExists() {
        //arrange
        var userId = UUID.fromString("3cf84d2c-e9c5-43d1-867b-d8e5ed97557d");
        var userRole = new Role("USER");
        var user = new ApplicationUser("username", "password", "email@email.ca");
        user.setRoles(List.of(userRole));
        var userRest = new ApplicationUserRest(userId, user.getUsername(), user.getEmail(), user.getAvatarUrl().orElse("default"), List.of("USER"));
        when(applicationUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(applicationUserRestMapper.mapToRest(user)).thenReturn(userRest);

        //act
        var applicationUserRest = applicationUserGetService.findRestById(userId);

        //assert
        assertThat(applicationUserRest).isEqualTo(userRest);
    }

    @Test
    @DisplayName("Should throw exception when application user rest with given id not exists")
    public void shouldThrowExceptionWhenApplicationUserRestWithGivenIdNotExists() {
        //arrange
        var userId = UUID.fromString("3cf84d2c-e9c5-43d1-867b-d8e5ed97557d");
        when(applicationUserRepository.findById(userId)).thenReturn(Optional.empty());

        //act && assert
        assertThrows(ApplicationUserNotFoundException.class, () -> applicationUserGetService.findRestById(userId));

    }

    @Test
    @DisplayName("Should get application user rest by email when exists")
    public void shouldGetApplicationUserRestByEmailWhenExists() {
        //arrange
        var userId = UUID.fromString("3cf84d2c-e9c5-43d1-867b-d8e5ed97557d");
        var userRole = new Role("USER");
        var user = new ApplicationUser("username", "password", "email@email.ca");
        user.setRoles(List.of(userRole));
        var userRest = new ApplicationUserRest(userId, user.getUsername(), user.getEmail(), user.getAvatarUrl().orElse("default"), List.of("USER"));
        when(applicationUserRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(applicationUserRestMapper.mapToRest(user)).thenReturn(userRest);

        //act
        var applicationUserRest = applicationUserGetService.findRestByEmail(user.getEmail());

        //assert
        assertThat(applicationUserRest).isEqualTo(userRest);
    }

    @Test
    @DisplayName("Should throw exception when application user with given email not exists")
    public void shouldThrowExceptionWhenApplicationUserWithGivenEmailNotExists() {
        //arrange
        var userId = UUID.fromString("3cf84d2c-e9c5-43d1-867b-d8e5ed97557d");
        var user = new ApplicationUser("username", "password", "email@email.ca");

        when(applicationUserRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        //act && assert
        assertThrows(ApplicationUserNotFoundException.class, () -> applicationUserGetService.findRestByEmail(user.getEmail()));
    }

    @Test
    @DisplayName("Should get application user by id when exists")
    public void shouldGetApplicationUserByIdWhenExists() {
        //arrange
        var userId = UUID.fromString("3cf84d2c-e9c5-43d1-867b-d8e5ed97557d");
        var userRole = new Role("USER");
        var user = new ApplicationUser("username", "password", "email@email.ca");
        user.setRoles(List.of(userRole));
        when(applicationUserRepository.findById(userId)).thenReturn(Optional.of(user));

        //act
        var applicationUser = applicationUserGetService.findById(userId);

        //assert
        assertThat(applicationUser).isEqualTo(user);
    }

    @Test
    @DisplayName("Should throw exception when application user with given id not exists")
    public void shouldThrowExceptionWhenApplicationUserWithGivenIdNotExists() {
        //arrange
        var userId = UUID.fromString("3cf84d2c-e9c5-43d1-867b-d8e5ed97557d");
        when(applicationUserRepository.findById(userId)).thenReturn(Optional.empty());

        //act && assert
        assertThrows(ApplicationUserNotFoundException.class, () -> applicationUserGetService.findById(userId));
    }

}