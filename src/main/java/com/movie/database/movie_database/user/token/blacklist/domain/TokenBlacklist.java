package com.movie.database.movie_database.user.token.blacklist.domain;

import com.movie.database.movie_database.support.Identifiable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TokenBlacklist extends Identifiable {

    private String token;

    @CreatedDate
    private LocalDateTime createdDate;

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
