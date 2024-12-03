package com.example.animalshelter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.model.Animal;
import com.example.model.AnimalCondition;

public class AnimalTest {
    private Animal animal1;
    private Animal animal2;
    private Animal animal3;
    private Animal animal4;

    @BeforeEach
    void setUp() {
        animal1 = new Animal("Charlie", "Dog", AnimalCondition.HEALTHY, 3, 100.0);
        animal2 = new Animal("Bella", "Cat", AnimalCondition.SICK, 2, 50.0);
        animal3 = new Animal("Charlie", "Dog", AnimalCondition.HEALTHY, 5, 150.0);
        animal4 = new Animal("Charlie", "Cat", AnimalCondition.HEALTHY, 5, 150.0);
    }

    @Test
    void testGetName() {
        assertEquals("Charlie", animal1.getName());
    }

    @Test
    void testSetName() {
        animal1.setName("Max");
        assertEquals("Max", animal1.getName());
    }

    @Test
    void testGetSpecies() {
        assertEquals("Dog", animal1.getSpecies());
    }

    @Test
    void testSetSpecies() {
        animal1.setSpecies("Wolf");
        assertEquals("Wolf", animal1.getSpecies());
    }

    @Test
    void testGetCondition() {
        assertEquals(AnimalCondition.HEALTHY, animal1.getCondition());
    }

    @Test
    void testSetCondition() {
        animal1.setCondition(AnimalCondition.SICK);
        assertEquals(AnimalCondition.SICK, animal1.getCondition());
    }

    @Test
    void testGetAge() {
        assertEquals(3, animal1.getAge());
    }

    @Test
    void testSetAge() {
        animal1.setAge(4);
        assertEquals(4, animal1.getAge());
    }

    @Test
    void testGetWeight() {
        assertEquals(100.0, animal1.getWeight());
    }

    @Test
    void testSetPrice() {
        animal1.setWeight(120.0);
        assertEquals(120.0, animal1.getWeight());
    }

    @Test
    void testCompareToDifferentNames() {
        assertTrue(((Comparable<Animal>) animal1).compareTo(animal2) > 0);
    }

    @Test
    void testCompareToSameNameDifferentSpecies() {
        assertTrue(((Comparable<Animal>) animal1).compareTo(animal4) != 0);
    }

    @Test
    void testCompareToSameNameAndSpeciesDifferentAge() {
        assertTrue(((Comparable<Animal>) animal1).compareTo(animal3) < 0);
    }
}
