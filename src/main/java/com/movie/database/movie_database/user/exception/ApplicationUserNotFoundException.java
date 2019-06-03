package com.movie.database.movie_database.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ApplicationUserNotFoundException extends ApplicationUserException {

    private static final String USER_WITH_ID_NOT_FOUND = "ApplicationUser with id %s not found";
    private static final String USER_WITH_USERNAME_NOT_FOUND = "ApplicationUser with username %s not found";

    public ApplicationUserNotFoundException(UUID id) {
        super(String.format(USER_WITH_ID_NOT_FOUND, id));
    }

    public ApplicationUserNotFoundException(String username) {
        super(String.format(USER_WITH_USERNAME_NOT_FOUND, username));
    }
}
