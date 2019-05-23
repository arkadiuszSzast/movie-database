package com.movie.database.movie_database.user.exception;

import java.util.UUID;

public class ApplicationApplicationUserNotFoundException extends ApplicationUserException {

    private static final String USER_WITH_ID_NOT_FOUND = "ApplicationUser with id %s not found";
    private static final String USER_WITH_USERNAME_NOT_FOUND = "ApplicationUser with username %s not found";
    private static final String USER_WITH_EMAIL_NOT_FOUND = "ApplicationUser with email %s not found";

    public ApplicationApplicationUserNotFoundException(UUID id) {
        super(String.format(USER_WITH_ID_NOT_FOUND, id));
    }

    public ApplicationApplicationUserNotFoundException(String username) {
        super(String.format(USER_WITH_USERNAME_NOT_FOUND, username));
    }
}
