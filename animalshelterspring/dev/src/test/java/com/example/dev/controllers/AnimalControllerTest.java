package com.example.dev.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.example.dev.config.TestConfig;
import com.example.dev.model.Animal;
import com.example.dev.model.AnimalCondition;
import com.example.dev.model.AnimalRepository;
import com.example.dev.model.AnimalShelter;
import com.example.dev.model.AnimalShelterRepository;
import com.example.dev.requests.AnimalRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
@AutoConfigureMockMvc
@Import(AnimalControllerTest.MockConfig.class)
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
public class AnimalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private AnimalShelterRepository shelterRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void testGetAnimal() throws Exception {
        Animal animal = new Animal("Max", "Dog", 4, AnimalCondition.HEALTHY);
        animal.setId(1L);

        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));

        mockMvc.perform(get("/animal/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(animal)));
    }

    @Test
    public void testGetAnimalNotFound() throws Exception {
        when(animalRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/animal/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testNewAnimal() throws Exception {
        AnimalShelter shelter = new AnimalShelter("Happy Paws Shelter", 20);
        shelter.setId(1L);

        Animal animal = new Animal("Max", "Dog", 4, AnimalCondition.HEALTHY);
        animal.setShelter(shelter);
        animal.setId(1L);

        AnimalRequest animalRequest = new AnimalRequest();
        animalRequest.setName("Max");
        animalRequest.setSpecies("Dog");
        animalRequest.setAge(4);
        animalRequest.setCondition("HEALTHY");
        animalRequest.setShelterId(1L);

        when(shelterRepository.findById(1L)).thenReturn(Optional.of(shelter));
        when(animalRepository.save(any(Animal.class))).thenReturn(animal);

        mockMvc.perform(post("/animal")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(animalRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animal)));
    }

    @Test
    public void testNewAnimalInvalidInput() throws Exception {
        AnimalRequest animalRequest = new AnimalRequest();
        animalRequest.setName(null);
        animalRequest.setSpecies("Dog");
        animalRequest.setAge(4);
        animalRequest.setCondition("HEALTHY");
        animalRequest.setShelterId(1L);

        mockMvc.perform(post("/animal")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(animalRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testNewAnimalShelterNotFound() throws Exception {
        AnimalRequest animalRequest = new AnimalRequest();
        animalRequest.setName("Max");
        animalRequest.setSpecies("Dog");
        animalRequest.setAge(4);
        animalRequest.setCondition("HEALTHY");
        animalRequest.setShelterId(1L);

        when(shelterRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/animal")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(animalRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testNewAnimalInvalidCondition() throws Exception {
        AnimalRequest animalRequest = new AnimalRequest();
        animalRequest.setName("Max");
        animalRequest.setSpecies("Dog");
        animalRequest.setAge(4);
        animalRequest.setCondition("INVALID");
        animalRequest.setShelterId(1L);

        AnimalShelter shelter = new AnimalShelter("Happy Paws Shelter", 20);
        shelter.setId(1L);

        when(shelterRepository.findById(1L)).thenReturn(Optional.of(shelter));

        mockMvc.perform(post("/animal")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(animalRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteAnimal() throws Exception {
        when(animalRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/animal/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteAnimalNotFound() throws Exception {
        when(animalRepository.existsById(1L)).thenReturn(false);

        mockMvc.perform(delete("/animal/1"))
                .andExpect(status().isNotFound());
    }

    @Import(TestConfig.class)
    static class MockConfig {
        @Bean
        @Primary
        public AnimalRepository mockAnimalRepository() {
            return mock(AnimalRepository.class);
        }

        @Bean
        @Primary
        public AnimalShelterRepository mockAnimalShelterRepository() {
            return mock(AnimalShelterRepository.class);
        }
    }
}
