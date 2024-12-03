package com.example.animalshelter;

import static org.junit.jupiter.api.Assertions.*;

import com.example.exceptions.AnimalAlreadyExistsException;
import com.example.exceptions.AnimalNotFoundException;
import com.example.exceptions.ShelterFullException;
import com.example.model.Animal;
import com.example.model.AnimalCondition;
import com.example.model.AnimalShelter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AnimalShelterTest {
    private AnimalShelter shelter;
    private Animal animal1;
    private Animal animal2;
    private Animal animal3;

    @BeforeEach
    void setUp() {
        shelter = new AnimalShelter("Happy Tails", 2);
        animal1 = new Animal("Charlie", "Dog", AnimalCondition.HEALTHY, 3, 100.0);
        animal2 = new Animal("Bella", "Cat", AnimalCondition.SICK, 2, 50.0);
        animal3 = new Animal("Max", "Parrot", AnimalCondition.QUARANTINED, 1, 200.0);
    }

    @Test
    void testAddAnimal() throws ShelterFullException,
            AnimalAlreadyExistsException {
        shelter.addAnimal(animal1);
        assertEquals(1, shelter.getAnimalCount());
        assertTrue(shelter.getAnimalList().contains(animal1));
    }

    @Test
    void testAddAnimalThrowsShelterFullException() throws ShelterFullException,
            AnimalAlreadyExistsException {
        shelter.addAnimal(animal1);
        shelter.addAnimal(animal2);
        assertThrows(ShelterFullException.class, () -> shelter.addAnimal(animal3));
    }

    @Test
    void testAddAnimalThrowsAnimalAlreadyExistsException() throws ShelterFullException, AnimalAlreadyExistsException {
        shelter.addAnimal(animal1);
        assertThrows(AnimalAlreadyExistsException.class, () -> shelter.addAnimal(animal1));
    }

    @Test
    void testRemoveAnimalThrowsAnimalNotFoundException() {
        assertThrows(AnimalNotFoundException.class, () -> shelter.removeAnimal(animal1));
    }

    @Test
    void testGetAnimalThrowsAnimalNotFoundException() {
        assertThrows(AnimalNotFoundException.class, () -> shelter.getAnimal(animal1));
    }

    @Test
    void testChangeConditionThrowsAnimalNotFoundException() {
        assertThrows(AnimalNotFoundException.class, () -> shelter.changeCondition(animal1, AnimalCondition.SICK));
    }

    @Test
    void testChangeAgeThrowsAnimalNotFoundException() {
        assertThrows(AnimalNotFoundException.class, () -> shelter.changeAge(animal1,
                5));
    }

    @Test
    void testCountByCondition() throws ShelterFullException,
            AnimalAlreadyExistsException {
        shelter.addAnimal(animal1);
        shelter.addAnimal(animal2);
        assertEquals(1, shelter.countByCondition(AnimalCondition.HEALTHY));
        assertEquals(1, shelter.countByCondition(AnimalCondition.SICK));
    }

    @Test
    void testSortByName() throws ShelterFullException,
            AnimalAlreadyExistsException {
        shelter.addAnimal(animal2);
        shelter.addAnimal(animal1);
        List<Animal> sortedList = shelter.sortByName();
        assertEquals("Bella", sortedList.get(0).getName());
        assertEquals("Charlie", sortedList.get(1).getName());
    }

    @Test
    void testSortByPrice() throws ShelterFullException,
            AnimalAlreadyExistsException {
        shelter.addAnimal(animal2);
        shelter.addAnimal(animal1);
        List<Animal> sortedList = shelter.sortByPrice();
        assertEquals(50.0, sortedList.get(0).getWeight());
        assertEquals(100.0, sortedList.get(1).getWeight());
    }

    @Test
    void testSearch() throws ShelterFullException, AnimalAlreadyExistsException,
            AnimalNotFoundException {
        shelter.addAnimal(animal1);
        Animal foundAnimal = shelter.search("Charlie");
        assertEquals(animal1, foundAnimal);
    }

    @Test
    void testSearchThrowsAnimalNotFoundException() {
        assertThrows(AnimalNotFoundException.class, () -> shelter.search("NonExistentAnimal"));
    }

    @Test
    void testMax() throws ShelterFullException, AnimalAlreadyExistsException {
        shelter.addAnimal(animal1);
        shelter.addAnimal(animal3);
        Animal mostExpensiveAnimal = shelter.max();
        assertEquals(animal3, mostExpensiveAnimal);
    }

    @Test
    void testGetCapacity() {
        assertEquals(shelter.getCapacity(), 2);
    }

    @Test
    void testGetName() {
        assertEquals(shelter.getName(), "Happy Tails");
    }

    @Test
    void testSetMaxCapacity() {
        shelter.setCapacity(3);
        assertEquals(shelter.getCapacity(), 3);
    }

    @Test
    void testSetShelterName() {
        shelter.setName("New Name");
        assertEquals(shelter.getName(), "New Name");
    }

    @Test
    void testRemoveAnimal()
            throws ShelterFullException, AnimalAlreadyExistsException, AnimalNotFoundException {
        shelter.addAnimal(animal1);
        shelter.addAnimal(animal2);
        shelter.removeAnimal(animal1);
        assertEquals(1, shelter.getAnimalCount());
        assertFalse(shelter.getAnimalList().contains(animal1));

        assertThrows(AnimalNotFoundException.class, () -> shelter.removeAnimal(animal3));
    }

    @Test
    void testGetAnimal()
            throws ShelterFullException, AnimalAlreadyExistsException, AnimalNotFoundException {
        shelter.addAnimal(animal2);
        shelter.addAnimal(animal1);

        Animal identicalAnimal = new Animal("Charlie", "Dog", AnimalCondition.HEALTHY, 3, 100.0);

        shelter.getAnimal(identicalAnimal);

        assertEquals(1, shelter.getAnimalCount());
        assertEquals(AnimalCondition.ADOPTED, animal1.getCondition());

        assertThrows(AnimalNotFoundException.class, () -> shelter.getAnimal(animal3));
    }

    @Test
    void testChangeCondition()
            throws ShelterFullException, AnimalAlreadyExistsException, AnimalNotFoundException {
        shelter.addAnimal(animal1);
        shelter.changeCondition(animal1, AnimalCondition.SICK);
        assertEquals(AnimalCondition.SICK, animal1.getCondition());

        assertThrows(AnimalNotFoundException.class, () -> shelter.changeCondition(animal3, AnimalCondition.HEALTHY));
    }

    @Test
    void testChangeAge()
            throws ShelterFullException, AnimalAlreadyExistsException, AnimalNotFoundException {
        shelter.addAnimal(animal1);
        shelter.changeAge(animal1, 5);
        assertEquals(5, animal1.getAge());

        assertThrows(AnimalNotFoundException.class, () -> shelter.changeAge(animal3, 4));
    }

    @Test
    void testSearchPartial() throws ShelterFullException, AnimalAlreadyExistsException {
        shelter.addAnimal(animal1);
        shelter.addAnimal(animal2);

        List<Animal> results = shelter.searchPartial("Char");
        assertEquals(1, results.size());
        assertEquals("Charlie", results.get(0).getName());

        results = shelter.searchPartial("cat");
        assertEquals(1, results.size());
        assertEquals("Bella", results.get(0).getName());

        results = shelter.searchPartial("unknown");
        assertEquals(0, results.size());
    }
}
