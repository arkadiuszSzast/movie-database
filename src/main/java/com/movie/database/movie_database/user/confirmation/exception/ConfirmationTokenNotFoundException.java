package com.movie.database.movie_database.user.confirmation.exception;

public class ConfirmationTokenNotFoundException extends RuntimeException {

    private final static String MESSAGE = "Confirmation token not found";

    public ConfirmationTokenNotFoundException() {
        super(MESSAGE);
    }
}
