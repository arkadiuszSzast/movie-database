package com.movie.database.movie_database.user.domain;

import com.movie.database.movie_database.support.Identifiable;
import com.movie.database.movie_database.user.role.domain.Role;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class ApplicationUser extends Identifiable {

    private String email;
    private String username;
    @Size(min = 8, max = 255)
    private String password;
    private boolean isActive;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "application_user_role",
            joinColumns = {@JoinColumn(name = "application_user", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role", referencedColumnName = "id")}
    )
    private List<Role> roles;

    public ApplicationUser() {
    }

    public ApplicationUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @PrePersist
    public void prePersist() {
        this.isActive = false;
    }

}