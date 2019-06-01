package com.movie.database.movie_database.user.role;

import com.movie.database.movie_database.user.role.domain.Role;
import com.movie.database.movie_database.user.role.domain.RoleRepository;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

@Service
public class RoleGetService {

    public static final String USER_ROLE = "USER";
    private final RoleRepository roleRepository;

    public RoleGetService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public Role getUserRole() throws RoleNotFoundException {
        return roleRepository.findByRole(USER_ROLE).orElseThrow(RoleNotFoundException::new);
    }
}
