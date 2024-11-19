package com.example.animalshelter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.exceptions.AnimalAlreadyExistsException;
import com.example.exceptions.AnimalNotFoundException;
import com.example.exceptions.ShelterFullException;

public class AnimalShelter {
    private String shelterName;
    private List<Animal> animalList;
    private int maxCapacity;

    public AnimalShelter(String shelterName, int maxCapacity) {
        this.shelterName = shelterName;
        this.maxCapacity = maxCapacity;
        this.animalList = new ArrayList<>();
    }

    public String getShelterName() {
        return shelterName;
    }

    public String setShelterName(String shelterName) {
        return this.shelterName = shelterName;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getAnimalCount() {
        return animalList.size();
    }

    public List<Animal> getAnimalList() {
        return animalList;
    }

    public void addAnimal(Animal animal) throws ShelterFullException, AnimalAlreadyExistsException {
        if (animalList.size() == maxCapacity) {
            throw new ShelterFullException("Shelter is full");
        }

        for (Animal a : animalList) {
            if (a.compareTo(animal) == 0) {
                throw new AnimalAlreadyExistsException("Animal already exists");
            }
        }

        animalList.add(animal);
    }

    public void removeAnimal(Animal animal) throws AnimalNotFoundException {
        for (Animal a : animalList) {
            if (a.compareTo(animal) == 0) {
                animalList.remove(a);
                return;
            }
        }
        throw new AnimalNotFoundException("Animal not found");
    }

    public void getAnimal(Animal animal) throws AnimalNotFoundException {
        for (Animal a : animalList) {
            if (a.compareTo(animal) == 0) {
                a.setCondition(AnimalCondition.ADOPTED);
                animalList.remove(a);
                return;
            }
        }
        throw new AnimalNotFoundException("Animal not found");
    }

    public void changeCondition(Animal animal, AnimalCondition condition) throws AnimalNotFoundException {
        for (Animal a : animalList) {
            if (a.compareTo(animal) == 0) {
                a.setCondition(condition);
                return;
            }
        }
        throw new AnimalNotFoundException("Animal not found");
    }

    public void changeAge(Animal animal, int age) throws AnimalNotFoundException {
        for (Animal a : animalList) {
            if (a.compareTo(animal) == 0) {
                a.setAge(age);
                return;
            }
        }
        throw new AnimalNotFoundException("Animal not found");
    }

    public int countByCondition(AnimalCondition condition) {
        int count = 0;

        for (Animal a : animalList)
            if (a.getCondition() == condition)
                count++;

        return count;
    }

    public List<Animal> sortByName() {
        List<Animal> sortedByName = new ArrayList<>(animalList);
        Collections.sort(sortedByName);
        return sortedByName;
    }

    public List<Animal> sortByPrice() {
        List<Animal> sortedByPrice = new ArrayList<>(animalList);
        sortedByPrice.sort(Comparator.comparingDouble(Animal::getPrice));
        return sortedByPrice;
    }

    public Animal search(String name) throws AnimalNotFoundException {
        Collections.sort(animalList, Comparator.comparing(Animal::getName));
        Animal searchAnimal = new Animal(name, "", null, 0, 0.0);
        int index = Collections.binarySearch(animalList, searchAnimal, Comparator.comparing(Animal::getName));

        if (index >= 0) {
            return animalList.get(index);
        } else {
            throw new AnimalNotFoundException("Animal not found");
        }
    }

    public List<Animal> searchPartial(String phrase) {
        List<Animal> searchResults = new ArrayList<>();
        phrase = phrase.toLowerCase();
        for (Animal a : animalList) {
            String name = a.getName().toLowerCase();
            String species = a.getSpecies().toLowerCase();

            if (name.contains(phrase) || species.contains(phrase)) {
                searchResults.add(a);
            }
        }
        return searchResults;
    }

    public Animal max() {
        return Collections.max(animalList, Comparator.comparingDouble(Animal::getPrice));
    }
}
