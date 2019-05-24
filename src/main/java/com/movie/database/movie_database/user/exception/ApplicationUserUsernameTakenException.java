package com.movie.database.movie_database.user.exception;

public class ApplicationUserUsernameTakenException extends ApplicationUserException {

    private static final String MESSAGE = "User with given username already exists";

    public ApplicationUserUsernameTakenException() {
        super(MESSAGE);
    }
}
