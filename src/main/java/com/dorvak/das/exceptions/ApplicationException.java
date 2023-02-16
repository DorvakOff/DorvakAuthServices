package com.dorvak.das.exceptions;

public class ApplicationException extends RuntimeException {

    public ApplicationException(String message, Object... args) {
        super(String.format(message, args));
    }
}
