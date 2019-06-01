package com.movie.database.movie_database.user.role;

import com.movie.database.movie_database.user.role.domain.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleController {

    private final RoleGetService roleGetService;

    public RoleController(RoleGetService roleGetService) {
        this.roleGetService = roleGetService;
    }

    @GetMapping("/api/roles")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Role> getRoles() {
        return roleGetService.getRoles();
    }
}
