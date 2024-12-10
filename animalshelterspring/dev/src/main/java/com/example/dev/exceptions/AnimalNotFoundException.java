package com.example.dev.exceptions;

public class AnimalNotFoundException extends RuntimeException {
    public AnimalNotFoundException(Long id) {
        super("Could not find animal with ID: " + id);
    }

    public AnimalNotFoundException(Long id, String additionalInfo) {
        super("Could not find animal with ID: " + id + ". " + additionalInfo);
    }

    public AnimalNotFoundException(Long id, Throwable cause) {
        super("Could not find animal with ID: " + id, cause);
    }
}
