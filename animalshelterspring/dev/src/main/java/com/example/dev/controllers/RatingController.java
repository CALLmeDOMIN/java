package com.example.dev.controllers;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.dev.exceptions.AnimalShelterNotFoundException;
import com.example.dev.exceptions.InvalidInputException;
import com.example.dev.model.AnimalShelter;
import com.example.dev.model.AnimalShelterRepository;
import com.example.dev.model.Rating;
import com.example.dev.model.RatingRepository;
import com.example.dev.requests.RatingRequest;

@RestController
public class RatingController {
    private final RatingRepository ratingRepository;
    private final AnimalShelterRepository shelterRepository;

    RatingController(RatingRepository ratingRepository, AnimalShelterRepository shelterRepository) {
        this.ratingRepository = ratingRepository;
        this.shelterRepository = shelterRepository;
    }

    @PostMapping("/rating")
    ResponseEntity<Rating> newRating(@RequestBody RatingRequest newRatingRequest) {
        if (newRatingRequest.getShelterId() == null || newRatingRequest.getValue() < 1
                || newRatingRequest.getValue() > 5) {
            throw new InvalidInputException(
                    "Invalid input: value must be between 1 and 5 and shelterId must not be null");
        }

        AnimalShelter shelter = shelterRepository.findById(newRatingRequest.getShelterId())
                .orElseThrow(() -> new AnimalShelterNotFoundException(newRatingRequest.getShelterId()));

        Rating rating = new Rating(newRatingRequest.getValue(), newRatingRequest.getComment());
        rating.setShelter(shelter);
        rating.setRatingdate(LocalDateTime.now());

        Rating savedRating = ratingRepository.save(rating);
        return new ResponseEntity<>(savedRating, HttpStatus.CREATED);
    }
}