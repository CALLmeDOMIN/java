package com.example.animalshelter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ShelterManagerTest {
    private ShelterManager manager;
    private AnimalShelter shelter1;
    private AnimalShelter shelter2;
    private AnimalShelter shelter3;

    @BeforeEach
    void setUp() {
        manager = new ShelterManager();
        shelter1 = new AnimalShelter("Happy Tails", 10);
        shelter2 = new AnimalShelter("Furry Friends", 5);
        shelter3 = new AnimalShelter("Paw Palace", 15);
    }

    @Test
    void testAddShelter() {
        manager.addShelter(shelter1);
        assertEquals(1, manager.shelters.size());
        assertTrue(manager.shelters.containsKey("Happy Tails"));
        assertEquals(shelter1, manager.shelters.get("Happy Tails"));
    }

    @Test
    void testRemoveShelter() {
        manager.addShelter(shelter1);
        manager.addShelter(shelter2);
        manager.removeShelter("Happy Tails");

        assertEquals(1, manager.shelters.size());
        assertFalse(manager.shelters.containsKey("Happy Tails"));
        assertTrue(manager.shelters.containsKey("Furry Friends"));
    }

    @Test
    void testFindEmptyWhenEmpty() {
        manager.addShelter(shelter1);
        manager.addShelter(shelter2);

        List<AnimalShelter> emptyShelters = manager.findEmpty();
        assertEquals(2, emptyShelters.size());
        assertTrue(emptyShelters.contains(shelter1));
        assertTrue(emptyShelters.contains(shelter2));
    }

    @Test
    void testFindEmptyWhenNotEmpty() throws Exception {
        manager.addShelter(shelter1);
        manager.addShelter(shelter2);

        shelter1.addAnimal(new Animal("Charlie", "Dog", AnimalCondition.HEALTHY, 3, 100.0));
        shelter2.addAnimal(new Animal("Bella", "Cat", AnimalCondition.SICK, 2, 50.0));

        List<AnimalShelter> emptyShelters = manager.findEmpty();
        assertEquals(0, emptyShelters.size());
    }

    @Test
    void testFindEmptyMixed() throws Exception {
        manager.addShelter(shelter1);
        manager.addShelter(shelter2);
        manager.addShelter(shelter3);

        shelter1.addAnimal(new Animal("Charlie", "Dog", AnimalCondition.HEALTHY, 3, 100.0));

        List<AnimalShelter> emptyShelters = manager.findEmpty();
        assertEquals(2, emptyShelters.size());
        assertTrue(emptyShelters.contains(shelter2));
        assertTrue(emptyShelters.contains(shelter3));
        assertFalse(emptyShelters.contains(shelter1));
    }
}
