package com.movie.database.movie_database.user.role;

import com.movie.database.movie_database.user.role.domain.Role;
import com.movie.database.movie_database.user.role.domain.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.relation.RoleNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleGetServiceTest {

    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleGetService roleGetService;

    @Test
    @DisplayName("Should get user role")
    public void shouldGetUserRole() throws RoleNotFoundException {
        //arrange
        var userRoleName = "USER";
        var role = new Role(userRoleName);
        when(roleRepository.findByRole(userRoleName)).thenReturn(Optional.of(role));

        //act
        var returnedRole = roleGetService.getUserRole();

        //assert
        assertThat(returnedRole).isEqualTo(role);
    }

    @Test
    @DisplayName("Should get user role")
    public void shouldThrowExceptionWhenRoleNotFound() {
        //arrange
        var userRoleName = "USER";
        when(roleRepository.findByRole(userRoleName)).thenReturn(Optional.empty());

        //act && assert
        assertThrows(RoleNotFoundException.class, () -> roleGetService.getUserRole());
    }


}