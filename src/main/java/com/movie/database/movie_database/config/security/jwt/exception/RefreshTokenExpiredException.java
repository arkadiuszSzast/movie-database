package com.movie.database.movie_database.config.security.jwt.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class RefreshTokenExpiredException extends TokenExpiredException {

    private static final String MESSAGE = "REFRESH_TOKEN_EXPIRED";

    public RefreshTokenExpiredException() {
        super(MESSAGE);
    }
}
