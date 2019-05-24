package com.movie.database.movie_database.support.exceptions;

public class ErrorInfo {

    private final String code;
    private final String defaultMessage;

    public ErrorInfo(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
