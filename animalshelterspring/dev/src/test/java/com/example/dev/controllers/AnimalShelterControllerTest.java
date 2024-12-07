package com.example.dev.controllers;

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
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
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
}
