package com.example.dao;

import java.util.List;

import com.example.model.Animal;
import com.example.model.AnimalCondition;

public interface AnimalDao {
    void save(Animal animal);

    void update(Animal animal);

    void delete(Animal animal);

    Animal findById(Long id);

    List<Animal> findAll();

    List<Animal> findByName(String name);

    List<Animal> findBySpecies(String species);

    List<Animal> findByCondition(AnimalCondition condition);
}
