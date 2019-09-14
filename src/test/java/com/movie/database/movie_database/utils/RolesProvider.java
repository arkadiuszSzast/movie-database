package com.movie.database.movie_database.utils;

import com.movie.database.movie_database.user.role.domain.Role;
import com.movie.database.movie_database.user.role.domain.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RolesProvider {

    @Autowired
    private RoleRepository roleRepository;

    public Role createUserRole() {
        return roleRepository.save(new Role("USER"));
    }

    public Role createAdminRole() {
        return roleRepository.save(new Role("ADMIN"));
    }
}
