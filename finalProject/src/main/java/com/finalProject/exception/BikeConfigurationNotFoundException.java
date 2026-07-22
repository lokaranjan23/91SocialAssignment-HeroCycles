package com.finalProject.exception;

public class BikeConfigurationNotFoundException extends RuntimeException {
    public BikeConfigurationNotFoundException(String message) {
        super(message);
    }
}
