package com.seyran.authservice.exception;

import javax.naming.AuthenticationException;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
