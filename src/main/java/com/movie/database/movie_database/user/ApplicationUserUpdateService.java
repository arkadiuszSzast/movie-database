package com.movie.database.movie_database.user;

import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import com.movie.database.movie_database.user.role.domain.Role;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ApplicationUserUpdateService {

    private final ApplicationUserGetService applicationUserGetService;
    private final ApplicationUserRepository applicationUserRepository;

    public ApplicationUserUpdateService(ApplicationUserGetService applicationUserGetService,
                                        ApplicationUserRepository applicationUserRepository) {
        this.applicationUserGetService = applicationUserGetService;
        this.applicationUserRepository = applicationUserRepository;
    }

    public void updateRoles(UUID applicationUserId, List<Role> roles) {
        var applicationUser = applicationUserGetService.findById(applicationUserId);
        applicationUser.setRoles(roles);
        applicationUserRepository.save(applicationUser);
    }
}
