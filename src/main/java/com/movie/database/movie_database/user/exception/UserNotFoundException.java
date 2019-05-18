package com.movie.database.movie_database.user.exception;

public class UserNotFoundException extends UserException {

    public UserNotFoundException(String username) {
        super("ApplicationUser with username " + username + " not found");
    }
}
