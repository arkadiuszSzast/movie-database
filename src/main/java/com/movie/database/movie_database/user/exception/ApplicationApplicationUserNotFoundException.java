package com.movie.database.movie_database.user.exception;

public class ApplicationApplicationUserNotFoundException extends ApplicationUserException {

    public ApplicationApplicationUserNotFoundException(String username) {
        super("ApplicationUser with username " + username + " not found");
    }
}
