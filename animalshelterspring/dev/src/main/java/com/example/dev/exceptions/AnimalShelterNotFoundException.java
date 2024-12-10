package com.example.dev.exceptions;

public class AnimalShelterNotFoundException extends RuntimeException {
    public AnimalShelterNotFoundException(Long id) {
        super("Could not find animal shelter with ID: " + id);
    }

    public AnimalShelterNotFoundException(Long id, String additionalInfo) {
        super("Could not find animal shelter with ID: " + id + ". " + additionalInfo);
    }

    public AnimalShelterNotFoundException(Long id, Throwable cause) {
        super("Could not find animal shelter with ID: " + id, cause);
    }
}
