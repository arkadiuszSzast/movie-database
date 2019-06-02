package com.movie.database.movie_database.utils;

import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import com.movie.database.movie_database.user.role.domain.Role;
import com.movie.database.movie_database.user.role.domain.RoleRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.testcontainers.shaded.org.jvnet.hk2.annotations.Service;

import java.util.List;

@Service
public class UserProvider {

    private final ApplicationUserRepository applicationUserRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserProvider(ApplicationUserRepository applicationUserRepository, RoleRepository roleRepository,
                        BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ApplicationUser createActivatedUserWithAdminRole() {
        var role = new Role("ADMIN");
        var savedRole = roleRepository.save(role);
        var savedAccount = createActivatedUser();
        savedAccount.setRoles(List.of(savedRole));
        var accountWithRole = applicationUserRepository.save(savedAccount);
        accountWithRole.setPassword("secretPassword");
        return accountWithRole;
    }

    public ApplicationUser createActivatedUserWithUserRole() {
        var role = new Role("USER");
        var savedRole = roleRepository.save(role);
        var savedAccount = createActivatedUser();
        savedAccount.setRoles(List.of(savedRole));
        var accountWithRole = applicationUserRepository.save(savedAccount);
        accountWithRole.setPassword("secretPassword");
        return accountWithRole;
    }

    public ApplicationUser createActivatedUserWithoutAnyRole() {
        var savedAccount = createActivatedUser();
        savedAccount.setPassword("secretPassword");
        return savedAccount;
    }

    private ApplicationUser createActivatedUser() {
        var account = new ApplicationUser("joe", "secretPassword", "mail@mail.ca");
        var encryptedPassword = bCryptPasswordEncoder.encode(account.getPassword());
        account.setPassword(encryptedPassword);
        applicationUserRepository.save(account);
        var addedAccount = applicationUserRepository.findByUsername(account.getUsername()).get();
        addedAccount.setActive(true);
        return applicationUserRepository.save(addedAccount);
    }
}
