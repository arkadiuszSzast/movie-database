package com.movie.database.movie_database.user.validation;

import com.movie.database.movie_database.support.ValidationResult;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import org.springframework.stereotype.Component;

@Component
public class ApplicationUserValidation {

    private final ApplicationUserRepository applicationUserRepository;

    public ApplicationUserValidation(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    public ValidationResult<ApplicationUser> usernameNotTaken(ApplicationUser applicationUser) {
        usernameIsNotTaken(applicationUser);
        return ValidationResult.from(applicationUser, this::usernameIsNotTaken);
    }

    public ValidationResult<ApplicationUser> emailNotTaken(ApplicationUser applicationUser) {
        usernameIsNotTaken(applicationUser);
        return ValidationResult.from(applicationUser, this::emailIsNotTaken);
    }

    private boolean usernameIsNotTaken(ApplicationUser applicationUser) {
        return applicationUserRepository.findByUsername(applicationUser.getUsername()).isEmpty();
    }

    private boolean emailIsNotTaken(ApplicationUser applicationUser) {
        return applicationUserRepository.findByEmail(applicationUser.getEmail()).isEmpty();
    }
}
