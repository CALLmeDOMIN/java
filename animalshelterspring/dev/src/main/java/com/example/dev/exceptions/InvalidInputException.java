package com.example.dev.exceptions;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, String additionalInfo) {
        super(message + ". Additional info: " + additionalInfo);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
