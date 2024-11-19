package com.example.exceptions;

public class AnimalAlreadyExistsException extends Exception {
    public AnimalAlreadyExistsException(String message) {
        super(message);
    }
}
