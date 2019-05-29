package com.movie.database.movie_database.user.token.blacklist.domain;

import com.movie.database.movie_database.support.Identifiable;

import javax.persistence.Entity;

@Entity
public class TokenBlacklist extends Identifiable {

    private String token;

    public TokenBlacklist() {
    }

    public TokenBlacklist(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
