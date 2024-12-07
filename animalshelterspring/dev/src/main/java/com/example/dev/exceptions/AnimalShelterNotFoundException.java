package com.example.dev.exceptions;

public class AnimalShelterNotFoundException extends RuntimeException {
    public AnimalShelterNotFoundException(Long id) {
        super("Could not find animal shelter with id: " + id);
    }
}
