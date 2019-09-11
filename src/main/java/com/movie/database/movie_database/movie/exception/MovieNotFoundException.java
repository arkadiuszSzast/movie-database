package com.movie.database.movie_database.movie.exception;

import java.util.UUID;

public class MovieNotFoundException extends RuntimeException {

    private static final String MOVIE_WITH_ID_NOT_FOUND = "Movie with id %s not found";

    public MovieNotFoundException(UUID movieId) {
        super(String.format(MOVIE_WITH_ID_NOT_FOUND, movieId));
    }
}
