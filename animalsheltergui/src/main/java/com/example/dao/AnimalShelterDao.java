package com.example.dao;

import java.util.List;

import com.example.model.AnimalShelter;

public interface AnimalShelterDao {
    void save(AnimalShelter animalShelter);

    void update(AnimalShelter animalShelter);

    void delete(AnimalShelter animalShelter);

    AnimalShelter findById(Long id);

    List<AnimalShelter> findAll();

    List<AnimalShelter> findByName(String name);

    void deleteAll();
}
