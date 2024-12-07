package com.example.dev.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.dev.config.TestConfig;
import com.example.dev.model.AnimalShelter;
import com.example.dev.model.Rating;
import com.example.dev.model.RatingRepository;
import com.example.dev.model.AnimalShelterRepository;
import com.example.dev.requests.RatingRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@WebMvcTest(RatingController.class)
@Import(TestConfig.class)
public class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private AnimalShelterRepository shelterRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void testNewRating() throws Exception {
        AnimalShelter shelter = new AnimalShelter("Happy Paws Shelter", 20);
        shelter.setId(1L);

        Rating rating = new Rating(5, "Great shelter");
        rating.setShelter(shelter);
        rating.setRatingdate(LocalDateTime.now());

        RatingRequest ratingRequest = new RatingRequest();
        ratingRequest.setValue(5);
        ratingRequest.setComment("Great shelter");
        ratingRequest.setShelterId(1L);

        when(shelterRepository.findById(1L)).thenReturn(Optional.of(shelter));
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

        mockMvc.perform(post("/rating")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ratingRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(rating)));
    }

    @Test
    public void testNewRatingShelterNotFound() throws Exception {
        RatingRequest ratingRequest = new RatingRequest();
        ratingRequest.setValue(5);
        ratingRequest.setComment("Great shelter");
        ratingRequest.setShelterId(1L);

        when(shelterRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(post("/rating")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ratingRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testNewRatingInvalidInput() throws Exception {
        RatingRequest ratingRequest = new RatingRequest();
        ratingRequest.setValue(6);
        ratingRequest.setComment("Great shelter");
        ratingRequest.setShelterId(1L);

        mockMvc.perform(post("/rating")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ratingRequest)))
                .andExpect(status().isBadRequest());
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public RatingRepository mockRatingRepository() {
            return mock(RatingRepository.class);
        }

        @Bean
        public AnimalShelterRepository mockAnimalShelterRepository() {
            return mock(AnimalShelterRepository.class);
        }
    }
}
