package com.movie.database.movie_database.user;

import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import com.movie.database.movie_database.user.exception.ApplicationApplicationUserNotFoundException;
import com.movie.database.movie_database.user.model.ApplicationUserRest;
import com.movie.database.movie_database.user.model.ApplicationUserRestMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public ApplicationUserRest findByUsername(String username) {
        var applicationUser = applicationUserRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationApplicationUserNotFoundException(username));
        return applicationUserRestMapper.mapToRest(applicationUser);
    }
}
