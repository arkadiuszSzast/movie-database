package com.movie.database.movie_database.user.domain;

import com.movie.database.movie_database.movie.domain.Movie;
import com.movie.database.movie_database.support.Identifiable;
import com.movie.database.movie_database.user.role.domain.Role;
import com.movie.database.movie_database.user.token.domain.ApplicationUserToken;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;

@Entity
public class ApplicationUser extends Identifiable {

    private String email;
    private String username;
    @Size(min = 8, max = 255)
    private String password;
    private boolean isActive;
    private String avatarUrl;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "application_user_role",
            joinColumns = {@JoinColumn(name = "application_user", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role", referencedColumnName = "id")}
    )
    private List<Role> roles;
    @ManyToMany
    @JoinTable(name = "application_user_movie",
            joinColumns = {@JoinColumn(name = "application_user", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "movie", referencedColumnName = "id")}
    )
    private List<Movie> favouriteMovies;
    @OneToMany(mappedBy = "applicationUser", orphanRemoval = true)
    private List<ApplicationUserToken> tokens;

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

    public List<Movie> getFavouriteMovies() {
        return favouriteMovies;
    }

    public void setFavouriteMovies(List<Movie> favouriteMovies) {
        this.favouriteMovies = favouriteMovies;
    }

    public List<ApplicationUserToken> getTokens() {
        return tokens;
    }

    public void setTokens(List<ApplicationUserToken> tokens) {
        this.tokens = tokens;
    }

    public Optional<String> getAvatarUrl() {
        return Optional.ofNullable(avatarUrl);
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @PrePersist
    public void prePersist() {
        this.isActive = false;
    }

}