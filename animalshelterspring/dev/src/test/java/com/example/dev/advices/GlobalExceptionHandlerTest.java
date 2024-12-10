package com.example.dev.advices;

import com.example.dev.exceptions.*;
import com.example.dev.responses.ErrorResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    public void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    public void testAnimalNotFoundHandlerWithCause() {
        Throwable cause = new RuntimeException("Underlying database issue");
        AnimalNotFoundException exception = new AnimalNotFoundException(1L, cause);

        ResponseEntity<ErrorResponse> response = exceptionHandler.animalNotFoundHandler(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Could not find animal with ID: 1", response.getBody().getMessage());
        assertEquals("Animal Not Found", response.getBody().getError());
        assertEquals(404, response.getBody().getStatus());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testAnimalShelterNotFoundHandlerWithCause() {
        Throwable cause = new RuntimeException("Database timeout");
        AnimalShelterNotFoundException exception = new AnimalShelterNotFoundException(10L, cause);

        ResponseEntity<ErrorResponse> response = exceptionHandler.animalShelterNotFoundHandler(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Could not find animal shelter with ID: 10", response.getBody().getMessage());
        assertEquals("Animal Shelter Not Found", response.getBody().getError());
        assertEquals(404, response.getBody().getStatus());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testInvalidInputHandlerWithCause() {
        Throwable cause = new IllegalArgumentException("Name must not be empty");
        InvalidInputException exception = new InvalidInputException("Invalid input for field 'name'", cause);

        ResponseEntity<ErrorResponse> response = exceptionHandler.invalidInputHandler(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid input for field 'name'", response.getBody().getMessage());
        assertEquals("Invalid Input", response.getBody().getError());
        assertEquals(400, response.getBody().getStatus());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testDatabaseExceptionHandlerWithCause() {
        Throwable cause = new RuntimeException("Connection pool exhausted");
        DatabaseException exception = new DatabaseException("Database connection error", cause);

        ResponseEntity<ErrorResponse> response = exceptionHandler.databaseExceptionHandler(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Database connection error", response.getBody().getMessage());
        assertEquals("Database Error", response.getBody().getError());
        assertEquals(500, response.getBody().getStatus());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testGenericExceptionHandlerWithCause() {
        Throwable cause = new IllegalStateException("Unexpected state encountered");
        Exception exception = new Exception("Unexpected error occurred", cause);

        ResponseEntity<ErrorResponse> response = exceptionHandler.exceptionHandler(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Unexpected error occurred", response.getBody().getMessage());
        assertEquals("Unexpected Error", response.getBody().getError());
        assertEquals(500, response.getBody().getStatus());
        assertEquals(cause, exception.getCause());
    }
}
