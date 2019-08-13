package com.movie.database.movie_database.user.model;

import java.util.List;
import java.util.UUID;

public class ApplicationUserRest {

    private final UUID id;
    private final String username;
    private final String email;
    private final String avatar;
    private final List<String> roles;

    public ApplicationUserRest(UUID id, String username, String email, String avatar, List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.avatar = avatar;
        this.roles = roles;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }
}
