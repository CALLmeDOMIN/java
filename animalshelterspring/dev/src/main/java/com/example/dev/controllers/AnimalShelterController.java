package com.example.dev.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.dev.exceptions.AnimalShelterNotFoundException;
import com.example.dev.exceptions.InvalidInputException;
import com.example.dev.model.Animal;
import com.example.dev.model.AnimalShelter;
import com.example.dev.model.AnimalShelterRepository;

@RestController
public class AnimalShelterController {
    private final AnimalShelterRepository repository;

    AnimalShelterController(AnimalShelterRepository repository) {
        this.repository = repository;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/animalshelter")
    ResponseEntity<List<AnimalShelter>> all() {
        List<AnimalShelter> shelters = repository.findAll();
        return new ResponseEntity<>(shelters, HttpStatus.OK);
    }
    // end::get-aggregate-root[]

    @GetMapping("/animalshelter/csv")
    String allCsv() {
        List<AnimalShelter> shelters = repository.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("id,name,capacity\n");
        for (AnimalShelter shelter : shelters) {
            sb.append(shelter.getId()).append(",");
            sb.append(shelter.getName()).append(",");
            sb.append(shelter.getCapacity()).append("\n");
        }
        return sb.toString();
    }

    @GetMapping("/animalshelter/{id}/animal")
    ResponseEntity<Set<Animal>> getAnimals(@PathVariable Long id) {
        AnimalShelter shelter = repository.findById(id).orElseThrow(() -> new AnimalShelterNotFoundException(id));
        return new ResponseEntity<>(shelter.getAnimals(), HttpStatus.OK);
    }

    @GetMapping("/animalshelter/{id}/fill")
    ResponseEntity<Double> fill(@PathVariable Long id) {
        AnimalShelter shelter = repository.findById(id).orElseThrow(() -> new AnimalShelterNotFoundException(id));
        int capacity = shelter.getCapacity();
        int animals = shelter.getAnimals().size();
        double fill = (animals / (double) capacity) * 100;

        return new ResponseEntity<>(fill, HttpStatus.OK);
    }

    @PostMapping("/animalshelter")
    ResponseEntity<AnimalShelter> newAnimalShelter(@RequestBody AnimalShelter newAnimalShelter) {
        if (newAnimalShelter.getName() == null || newAnimalShelter.getCapacity() == null) {
            throw new InvalidInputException("Missing required fields");
        }

        AnimalShelter shelter = repository.save(newAnimalShelter);
        return new ResponseEntity<>(shelter, HttpStatus.CREATED);
    }

    @DeleteMapping("/animalshelter/{id}")
    ResponseEntity<Void> deleteAnimalShelter(@PathVariable Long id) {
        repository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
