package com.example.dev.exceptions;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(String message, String additionalInfo) {
        super(message + ". Additional info: " + additionalInfo);
    }
}
