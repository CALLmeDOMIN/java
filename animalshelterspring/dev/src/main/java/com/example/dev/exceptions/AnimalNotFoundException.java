package com.example.dev.exceptions;

public class AnimalNotFoundException extends RuntimeException {
    public AnimalNotFoundException(Long id) {
        super("Could not find animal with id: " + id);
    }
}
