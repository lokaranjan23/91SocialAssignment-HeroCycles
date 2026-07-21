package com.finalProject.exception;

public class PartNotFoundException extends RuntimeException{
    public PartNotFoundException(String message) {
        super(message);
    }
}
