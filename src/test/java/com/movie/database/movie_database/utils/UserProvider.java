package com.movie.database.movie_database.utils;

import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import com.movie.database.movie_database.user.role.domain.Role;
import com.movie.database.movie_database.user.role.domain.RoleRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

import java.util.List;

@Component
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
        var role = createRole("ADMIN");
        var savedAccount = createUser("emailWithAdminRole@email.com", "admin", true);
        savedAccount.setRoles(List.of(role));
        var accountWithRole = applicationUserRepository.save(savedAccount);
        accountWithRole.setPassword("secretPassword");
        return accountWithRole;
    }

    public ApplicationUser createActivatedUserWithUserRole() {
        var role = createRole("USER");
        var savedAccount = createUser("emailWithUserRole@email.com", "user", true);
        savedAccount.setRoles(List.of(role));
        var accountWithRole = applicationUserRepository.save(savedAccount);
        accountWithRole.setPassword("secretPassword");
        return accountWithRole;
    }

    public ApplicationUser createActivatedUserWithoutAnyRole() {
        var savedAccount = createUser("emailWithoutRole@email.com", "noRole", true);
        savedAccount.setPassword("secretPassword");
        return savedAccount;
    }

    public ApplicationUser createRandomActivatedUserWithUserRole() {
        var role = createRole("USER");
        var username = RandomStringUtils.random(10);
        var savedAccount = createUser(username + "@email.com", username, true);
        savedAccount.setRoles(List.of(role));
        var accountWithRole = applicationUserRepository.save(savedAccount);
        accountWithRole.setPassword("secretPassword");
        return accountWithRole;
    }

    public ApplicationUser createRandomInactivUser() {
        var role = createRole("USER");
        var username = RandomStringUtils.random(10);
        var savedAccount = createUser(username + "@email.com", username, false);
        savedAccount.setRoles(List.of(role));
        var accountWithRole = applicationUserRepository.save(savedAccount);
        accountWithRole.setPassword("secretPassword");
        return accountWithRole;
    }

    private Role createRole(String user) {
        var role = new Role(user);
        return roleRepository.save(role);
    }

    private ApplicationUser createUser(String email, String username, boolean isActivated) {
        var account = new ApplicationUser(username, "secretPassword", email);
        var encryptedPassword = bCryptPasswordEncoder.encode(account.getPassword());
        account.setPassword(encryptedPassword);
        applicationUserRepository.save(account);
        var addedAccount = applicationUserRepository.findByUsername(account.getUsername()).get();
        addedAccount.setActive(isActivated);
        return applicationUserRepository.save(addedAccount);
    }
}
