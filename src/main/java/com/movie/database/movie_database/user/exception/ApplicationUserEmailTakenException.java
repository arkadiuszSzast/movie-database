package com.movie.database.movie_database.user.exception;

public class ApplicationUserEmailTakenException extends ApplicationUserException {

    private static final String MESSAGE = "User with given email already exists";

    public ApplicationUserEmailTakenException() {
        super(MESSAGE);
    }
}
