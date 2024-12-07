package com.example.dev.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.dev.exceptions.AnimalNotFoundException;
import com.example.dev.exceptions.AnimalShelterNotFoundException;
import com.example.dev.exceptions.InvalidInputException;
import com.example.dev.model.Animal;
import com.example.dev.model.AnimalCondition;
import com.example.dev.model.AnimalRepository;
import com.example.dev.model.AnimalShelter;
import com.example.dev.model.AnimalShelterRepository;
import com.example.dev.requests.AnimalRequest;

@RestController
public class AnimalController {
    private final AnimalRepository animalRepository;
    private final AnimalShelterRepository shelterRepository;

    AnimalController(AnimalRepository animalRepository, AnimalShelterRepository shelterRepository) {
        this.animalRepository = animalRepository;
        this.shelterRepository = shelterRepository;
    }

    @GetMapping("/animal/{id}")
    ResponseEntity<Animal> getAnimal(@PathVariable Long id) {
        Animal animal = animalRepository.findById(id).orElseThrow(() -> new AnimalNotFoundException(id));
        return new ResponseEntity<>(animal, HttpStatus.OK);
    }

    @PostMapping("/animal")
    ResponseEntity<Animal> newAnimal(@RequestBody AnimalRequest newAnimalRequest) {
        if (newAnimalRequest.getShelterId() == null || newAnimalRequest.getName() == null
                || newAnimalRequest.getSpecies() == null
                || newAnimalRequest.getAge() <= 0 || newAnimalRequest.getCondition() == null) {
            throw new InvalidInputException("Missing required fields");
        }

        AnimalShelter shelter = shelterRepository.findById(newAnimalRequest.getShelterId())
                .orElseThrow(() -> new AnimalShelterNotFoundException(newAnimalRequest.getShelterId()));

        AnimalCondition condition;
        try {
            condition = AnimalCondition.valueOf(newAnimalRequest.getCondition().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid condition: " + newAnimalRequest.getCondition());
        }

        Animal animal = new Animal(newAnimalRequest.getName(), newAnimalRequest.getSpecies(),
                newAnimalRequest.getAge(), condition);
        animal.setShelter(shelter);

        Animal savedAnimal = animalRepository.save(animal);
        return new ResponseEntity<>(savedAnimal, HttpStatus.CREATED);
    }

    @DeleteMapping("/animal/{id}")
    ResponseEntity<Void> deleteAnimal(@PathVariable Long id) {
        if (!animalRepository.existsById(id)) {
            throw new AnimalNotFoundException(id);
        }
        animalRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}