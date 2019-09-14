package com.movie.database.movie_database.user.confirmation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ConfirmationTokenNotFoundException extends RuntimeException {

    private final static String MESSAGE = "Confirmation token not found";

    public ConfirmationTokenNotFoundException() {
        super(MESSAGE);
    }
}
