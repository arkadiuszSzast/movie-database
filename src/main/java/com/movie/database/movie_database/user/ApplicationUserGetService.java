package com.movie.database.movie_database.user;

import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import com.movie.database.movie_database.user.exception.ApplicationUserNotFoundException;
import com.movie.database.movie_database.user.model.ApplicationUserRest;
import com.movie.database.movie_database.user.model.ApplicationUserRestMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ApplicationUserGetService {

    private final ApplicationUserRepository applicationUserRepository;
    private final ApplicationUserRestMapper applicationUserRestMapper;

    public ApplicationUserGetService(ApplicationUserRepository applicationUserRepository,
                                     ApplicationUserRestMapper applicationUserRestMapper) {
        this.applicationUserRepository = applicationUserRepository;
        this.applicationUserRestMapper = applicationUserRestMapper;
    }

    public List<ApplicationUserRest> findAll() {
        return applicationUserRestMapper.mapToRest(applicationUserRepository.findAll());
    }

    public ApplicationUserRest findRestById(UUID id) {
        var applicationUser = findById(id);
        return applicationUserRestMapper.mapToRest(applicationUser);
    }

    public ApplicationUser findById(UUID id) {
        return applicationUserRepository.findById(id)
                .orElseThrow(() -> new ApplicationUserNotFoundException(id));
    }

    public ApplicationUserRest findRestByEmail(String email) {
        var applicationUser = applicationUserRepository.findByEmail(email)
                .orElseThrow(() -> new ApplicationUserNotFoundException(email));
        return applicationUserRestMapper.mapToRest(applicationUser);
    }
}
