package com.movie.database.movie_database.user;

import com.movie.database.movie_database.user.confirmation.ConfirmationMailSenderService;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import com.movie.database.movie_database.user.exception.ApplicationUserEmailTakenException;
import com.movie.database.movie_database.user.exception.ApplicationUserUsernameTakenException;
import com.movie.database.movie_database.user.role.RoleGetService;
import com.movie.database.movie_database.user.validation.ApplicationUserValidation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public class ApplicationUserCreateService {

    private final ApplicationUserRepository applicationUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationMailSenderService confirmationMailSenderService;
    private final ApplicationUserValidation applicationUserValidation;
    private final RoleGetService roleGetService;

    public ApplicationUserCreateService(ApplicationUserRepository applicationUserRepository,
                                        BCryptPasswordEncoder bCryptPasswordEncoder,
                                        ConfirmationMailSenderService confirmationMailSenderService,
                                        ApplicationUserValidation applicationUserValidation,
                                        RoleGetService roleGetService) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.confirmationMailSenderService = confirmationMailSenderService;
        this.applicationUserValidation = applicationUserValidation;
        this.roleGetService = roleGetService;
    }

    public void create(ApplicationUser applicationUser) throws IOException, RoleNotFoundException {
        validateNewApplicationUser(applicationUser);
        setUserRole(applicationUser);
        var encryptedPassword = bCryptPasswordEncoder.encode(applicationUser.getPassword());
        applicationUser.setPassword(encryptedPassword);
        var savedApplicationUser = applicationUserRepository.save(applicationUser);
        confirmationMailSenderService.send(applicationUser, savedApplicationUser);
    }

    private void setUserRole(ApplicationUser applicationUser) throws RoleNotFoundException {
        var userRole = roleGetService.getUserRole();
        applicationUser.setRoles(List.of(userRole));
    }

    private void validateNewApplicationUser(ApplicationUser applicationUser) {
        applicationUserValidation.emailNotTaken(applicationUser).throwIfInvalid(ApplicationUserEmailTakenException::new);
        applicationUserValidation.usernameNotTaken(applicationUser).throwIfInvalid(ApplicationUserUsernameTakenException::new);
    }
}