package com.movie.database.movie_database.user.confirmation.domain;

import com.movie.database.movie_database.support.Identifiable;
import com.movie.database.movie_database.user.domain.ApplicationUser;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ConfirmationToken extends Identifiable {

    @ManyToOne
    @JoinColumn(name = "applicationUser", referencedColumnName = "id")
    private ApplicationUser applicationUser;
    private String token;

    public ConfirmationToken() {
    }

    public ConfirmationToken(ApplicationUser applicationUser, String token) {
        this.applicationUser = applicationUser;
        this.token = token;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
