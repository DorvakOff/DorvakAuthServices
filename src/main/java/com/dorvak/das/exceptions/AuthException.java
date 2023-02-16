package com.dorvak.das.exceptions;

public class AuthException extends ApplicationException {

    public AuthException(String message, Object... args) {
        super(message, args);
    }
}
