package com.movie.database.movie_database.user.password.exception;

public class ResetPasswordTokenNotFoundException extends RuntimeException {

    private final static String MESSAGE = "Reset password token not found";

    public ResetPasswordTokenNotFoundException() {
        super(MESSAGE);
    }
}
