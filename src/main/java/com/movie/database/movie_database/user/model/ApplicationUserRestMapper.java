package com.movie.database.movie_database.user.model;

import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.role.domain.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApplicationUserRestMapper {

    public List<ApplicationUserRest> mapToRest(List<ApplicationUser> applicationUsers) {
        return applicationUsers
                .stream()
                .map(this::mapToRest)
                .collect(Collectors.toList());
    }

    public ApplicationUserRest mapToRest(ApplicationUser applicationUser) {
        var roles = applicationUser.getRoles()
                .stream()
                .map(Role::getRole)
                .collect(Collectors.toList());
        return new ApplicationUserRest(applicationUser.getId(), applicationUser.getUsername(),
                applicationUser.getEmail(), applicationUser.getAvatarUrl().orElse("default"), roles);
    }
}
