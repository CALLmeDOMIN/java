package com.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;

import com.example.exceptions.AnimalAlreadyExistsException;
import com.example.exceptions.AnimalNotFoundException;
import com.example.exceptions.ShelterFullException;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AnimalShelter")
@Getter
@Setter
@NoArgsConstructor
public class AnimalShelter implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int capacity;

    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Animal> animals = new HashSet<>();

    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Rating> ratings = new HashSet<>();

    public AnimalShelter(String shelterName, int maxCapacity) {
        this.name = shelterName;
        this.capacity = maxCapacity;
        this.animals = new HashSet<>();
        this.ratings = new HashSet<>();
    }

    public Set<Animal> getAnimals() {
        if (animals == null) {
            animals = new HashSet<>();
        }
        return animals;
    }

    public Set<Rating> getRatings() {
        if (ratings == null) {
            ratings = new HashSet<>();
        }
        return ratings;
    }

    public void setAnimals(Set<Animal> animals) {
        this.animals = animals != null ? animals : new HashSet<>();
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings != null ? ratings : new HashSet<>();
    }

    public int getAnimalCount() {
        return animals.size();
    }

    public List<Animal> getAnimalList() {
        return new ArrayList<>(animals);
    }

    public void addAnimal(Animal animal) throws ShelterFullException, AnimalAlreadyExistsException {
        if (animals == null) {
            animals = new HashSet<>();
        }

        if (animals.size() >= capacity) {
            throw new ShelterFullException("Shelter is at full capacity");
        }

        if (animals.stream().anyMatch(a -> a.getName().equals(animal.getName()))) {
            throw new AnimalAlreadyExistsException("Animal with this name already exists in the shelter");
        }

        animals.add(animal);
        animal.setShelter(this);
    }

    public void removeAnimal(Animal animal) {
        if (animals.contains(animal)) {
            animals.remove(animal);
            animal.setShelter(null);
        }
    }

    public void getAnimal(Animal animal) throws AnimalNotFoundException {
        for (Animal a : animals) {
            if (a.equals(animal)) {
                a.setCondition(AnimalCondition.ADOPTED);
                animals.remove(a);
                return;
            }
        }
        throw new AnimalNotFoundException("Animal not found");
    }

    public void changeCondition(Animal animal, AnimalCondition condition) throws AnimalNotFoundException {
        for (Animal a : animals) {
            if (a.equals(animal)) {
                a.setCondition(condition);
                return;
            }
        }
        throw new AnimalNotFoundException("Animal not found");
    }

    public void changeAge(Animal animal, int age) throws AnimalNotFoundException {
        for (Animal a : animals) {
            if (a.equals(animal)) {
                a.setAge(age);
                return;
            }
        }
        throw new AnimalNotFoundException("Animal not found");
    }

    public int countByCondition(AnimalCondition condition) {
        int count = 0;

        for (Animal a : animals)
            if (a.getCondition() == condition)
                count++;

        return count;
    }

    public List<Animal> sortByName() {
        List<Animal> sortedByName = new ArrayList<>(animals);
        sortedByName.sort(Comparator.comparing(Animal::getName));
        return sortedByName;
    }

    public List<Animal> sortByPrice() {
        List<Animal> sortedByPrice = new ArrayList<>(animals);
        sortedByPrice.sort(Comparator.comparingDouble(Animal::getWeight));
        return sortedByPrice;
    }

    public Animal search(String name) throws AnimalNotFoundException {
        List<Animal> sortedByName = new ArrayList<>(animals);
        sortedByName.sort(Comparator.comparing(Animal::getName));
        Animal searchAnimal = new Animal(name, "", null, 0, 0.0);
        int index = Collections.binarySearch(sortedByName, searchAnimal, Comparator.comparing(Animal::getName));

        if (index >= 0) {
            return sortedByName.get(index);
        } else {
            throw new AnimalNotFoundException("Animal not found");
        }
    }

    public List<Animal> searchPartial(String phrase) {
        List<Animal> searchResults = new ArrayList<>();
        phrase = phrase.toLowerCase();
        for (Animal a : animals) {
            String name = a.getName().toLowerCase();
            String species = a.getSpecies().toLowerCase();

            if (name.contains(phrase) || species.contains(phrase)) {
                searchResults.add(a);
            }
        }
        return searchResults;
    }

    public Animal max() {
        List<Animal> sortedByPrice = new ArrayList<>(animals);
        sortedByPrice.sort(Comparator.comparingDouble(Animal::getWeight));
        return sortedByPrice.get(sortedByPrice.size() - 1);
    }
}
