package com.example.dev.controllers;

import com.example.dev.advices.GlobalExceptionHandler;
import com.example.dev.model.Animal;
import com.example.dev.model.AnimalCondition;
import com.example.dev.model.AnimalShelter;
import com.example.dev.model.AnimalShelterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AnimalShelterControllerTest {

    private MockMvc mockMvc;

    private AnimalShelterRepository shelterRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        shelterRepository = Mockito.mock(AnimalShelterRepository.class);
        AnimalShelterController controller = new AnimalShelterController(shelterRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllAnimalShelters() throws Exception {
        when(shelterRepository.findAll()).thenReturn(Arrays.asList(
                new AnimalShelter("Happy Paws", 20),
                new AnimalShelter("Friendly Tails", 30)));

        mockMvc.perform(get("/animalshelter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Happy Paws"))
                .andExpect(jsonPath("$[1].name").value("Friendly Tails"));
    }

    @Test
    public void testGetAnimalsInShelter() throws Exception {
        AnimalShelter shelter = new AnimalShelter("Happy Paws", 20);
        shelter.setId(1L);

        when(shelterRepository.findById(1L)).thenReturn(Optional.of(shelter));

        mockMvc.perform(get("/animalshelter/1/animal"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateAnimalShelter() throws Exception {
        AnimalShelter shelter = new AnimalShelter("Happy Paws", 50);
        when(shelterRepository.save(any(AnimalShelter.class))).thenReturn(shelter);

        mockMvc.perform(post("/animalshelter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shelter)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Happy Paws"))
                .andExpect(jsonPath("$.capacity").value(50));
    }

    @Test
    public void testDeleteAnimalShelter() throws Exception {
        doNothing().when(shelterRepository).deleteById(anyLong());

        mockMvc.perform(delete("/animalshelter/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetAllAnimalSheltersCsv() throws Exception {
        when(shelterRepository.findAll()).thenReturn(Arrays.asList(
                new AnimalShelter("Happy Paws", 20),
                new AnimalShelter("Friendly Tails", 30)));

        mockMvc.perform(get("/animalshelter/csv"))
                .andExpect(status().isOk())
                .andExpect(content().string("id,name,capacity\n" +
                        "null,Happy Paws,20\n" +
                        "null,Friendly Tails,30\n"));
    }

    @Test
    public void testGetShelterFillPercentage() throws Exception {
        Animal animal1 = new Animal("Buddy", "Dog", 3, AnimalCondition.HEALTHY);
        Animal animal2 = new Animal("Whiskers", "Cat", 2, AnimalCondition.SICK);
        AnimalShelter shelter = new AnimalShelter("Happy Paws", 20);
        shelter.setId(1L);
        shelter.setAnimals(Set.of(animal1, animal2));

        when(shelterRepository.findById(1L)).thenReturn(Optional.of(shelter));

        mockMvc.perform(get("/animalshelter/1/fill"))
                .andExpect(status().isOk())
                .andExpect(content().string("10.0")); // Assuming 2/20 = 10%
    }

    @Test
    public void testGetShelterFillPercentageNotFound() throws Exception {
        when(shelterRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/animalshelter/1/fill"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Could not find animal shelter with id: 1"))
                .andExpect(jsonPath("$.error").value("Animal Shelter Not Found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    public void testCreateAnimalShelterInvalidInput() throws Exception {
        AnimalShelter invalidShelter = new AnimalShelter(null, null);

        mockMvc.perform(post("/animalshelter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidShelter)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Missing required fields"))
                .andExpect(jsonPath("$.error").value("Invalid Input"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void testGetAllAnimalSheltersEmptyList() throws Exception {
        when(shelterRepository.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/animalshelter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
