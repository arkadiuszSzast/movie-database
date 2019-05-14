package com.movie.database.movie_database.user;

public class UserNotFoundException extends UserException {

    public UserNotFoundException(String username) {
        super("ApplicationUser with username " + username + " not found");
    }
}
