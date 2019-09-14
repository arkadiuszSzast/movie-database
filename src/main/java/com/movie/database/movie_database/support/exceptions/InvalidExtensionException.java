package com.movie.database.movie_database.support.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidExtensionException extends RuntimeException {

    private static final String MESSAGE = "Given file has invalid extension";

    public InvalidExtensionException() {
        super(MESSAGE);
    }
}
