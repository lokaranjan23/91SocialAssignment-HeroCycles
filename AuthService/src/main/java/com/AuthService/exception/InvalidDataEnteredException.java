package com.AuthService.exception;

public class InvalidDataEnteredException extends RuntimeException {
    public InvalidDataEnteredException(String message) {
        super(message);
    }
}
