package com.movie.database.movie_database.user.token.domain;

import com.movie.database.movie_database.support.Identifiable;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.token.model.TokenType;

import javax.persistence.*;

@Entity
public class ApplicationUserToken extends Identifiable {

    @ManyToOne
    @JoinColumn(name = "applicationUser", referencedColumnName = "id")
    private ApplicationUser applicationUser;
    private String token;
    @Enumerated(value = EnumType.STRING)
    private TokenType tokenType;

    public ApplicationUserToken() {
    }

    public ApplicationUserToken(ApplicationUser applicationUser, String token, TokenType tokenType) {
        this.applicationUser = applicationUser;
        this.token = token;
        this.tokenType = tokenType;
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

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }
}
