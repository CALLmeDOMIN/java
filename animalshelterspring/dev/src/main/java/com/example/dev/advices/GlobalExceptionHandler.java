package com.example.dev.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.dev.exceptions.AnimalNotFoundException;
import com.example.dev.exceptions.AnimalShelterNotFoundException;
import com.example.dev.exceptions.DatabaseException;
import com.example.dev.exceptions.InvalidInputException;
import com.example.dev.responses.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AnimalNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> animalNotFoundHandler(AnimalNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "Animal Not Found", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AnimalShelterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> animalShelterNotFoundHandler(AnimalShelterNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "Animal Shelter Not Found",
                HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> invalidInputHandler(InvalidInputException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "Invalid Input", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DatabaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> databaseExceptionHandler(DatabaseException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "Database Error",
                HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "Unexpected Error",
                HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
