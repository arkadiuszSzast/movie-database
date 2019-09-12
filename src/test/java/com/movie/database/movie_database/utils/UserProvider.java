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
        var role = new Role("ADMIN");
        var savedRole = roleRepository.save(role);
        var savedAccount = createActivatedUser("emailWithAdminRole@email.com", "admin");
        savedAccount.setRoles(List.of(savedRole));
        var accountWithRole = applicationUserRepository.save(savedAccount);
        accountWithRole.setPassword("secretPassword");
        return accountWithRole;
    }

    public ApplicationUser createActivatedUserWithUserRole() {
        var role = new Role("USER");
        var savedRole = roleRepository.save(role);
        var savedAccount = createActivatedUser("emailWithUserRole@email.com", "user");
        savedAccount.setRoles(List.of(savedRole));
        var accountWithRole = applicationUserRepository.save(savedAccount);
        accountWithRole.setPassword("secretPassword");
        return accountWithRole;
    }

    public ApplicationUser createActivatedUserWithoutAnyRole() {
        var savedAccount = createActivatedUser("emailWithoutRole@email.com", "noRole");
        savedAccount.setPassword("secretPassword");
        return savedAccount;
    }

    public ApplicationUser createRandomUserWithUserRole() {
        var role = new Role("USER");
        var savedRole = roleRepository.save(role);
        var username = RandomStringUtils.random(10);
        var savedAccount = createActivatedUser(username + "@email.com", username);
        savedAccount.setRoles(List.of(savedRole));
        var accountWithRole = applicationUserRepository.save(savedAccount);
        accountWithRole.setPassword("secretPassword");
        return accountWithRole;
    }

    private ApplicationUser createActivatedUser(String email, String username) {
        var account = new ApplicationUser(username, "secretPassword", email);
        var encryptedPassword = bCryptPasswordEncoder.encode(account.getPassword());
        account.setPassword(encryptedPassword);
        applicationUserRepository.save(account);
        var addedAccount = applicationUserRepository.findByUsername(account.getUsername()).get();
        addedAccount.setActive(true);
        return applicationUserRepository.save(addedAccount);
    }
}
