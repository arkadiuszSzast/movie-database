package com.movie.database.movie_database.user;

import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ApplicationUserDeleteService {

    private final ApplicationUserRepository applicationUserRepository;

    public ApplicationUserDeleteService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    public void delete(UUID applicationUserId) {
        applicationUserRepository.deleteById(applicationUserId);
    }
}
