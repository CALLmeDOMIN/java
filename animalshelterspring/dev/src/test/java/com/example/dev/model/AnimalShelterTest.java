package com.example.dev.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalShelterTest {

    private AnimalShelter shelter;

    @BeforeEach
    public void setUp() {
        shelter = new AnimalShelter();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(shelter);
        assertNull(shelter.getId());
        assertNull(shelter.getName());
        assertNull(shelter.getCapacity());
        assertNotNull(shelter.getAnimals());
        assertNotNull(shelter.getRatings());
        assertTrue(shelter.getAnimals().isEmpty());
        assertTrue(shelter.getRatings().isEmpty());
    }

    @Test
    public void testParameterizedConstructor() {
        AnimalShelter shelter = new AnimalShelter("Happy Paws", 50);
        assertEquals("Happy Paws", shelter.getName());
        assertEquals(50, shelter.getCapacity());
        assertNotNull(shelter.getAnimals());
        assertTrue(shelter.getAnimals().isEmpty());
        assertNotNull(shelter.getRatings());
        assertTrue(shelter.getRatings().isEmpty());
    }

    @Test
    public void testSettersAndGetters() {
        shelter.setId(1L);
        shelter.setName("Friendly Tails");
        shelter.setCapacity(100);

        Set<Animal> animals = new HashSet<>();
        Animal animal = new Animal("Buddy", "Dog", 3, AnimalCondition.HEALTHY);
        animals.add(animal);

        Set<Rating> ratings = new HashSet<>();
        Rating rating = new Rating(5, "Great shelter!");
        ratings.add(rating);

        shelter.setAnimals(animals);
        shelter.setRatings(ratings);

        assertEquals(1L, shelter.getId());
        assertEquals("Friendly Tails", shelter.getName());
        assertEquals(100, shelter.getCapacity());
        assertEquals(animals, shelter.getAnimals());
        assertEquals(ratings, shelter.getRatings());
    }

    @Test
    public void testAddAnimal() {
        Animal animal = new Animal("Whiskers", "Cat", 2, AnimalCondition.SICK);
        shelter.getAnimals().add(animal);

        assertTrue(shelter.getAnimals().contains(animal));
    }

    @Test
    public void testAddRating() {
        Rating rating = new Rating(4, "Good service!");
        shelter.getRatings().add(rating);

        assertTrue(shelter.getRatings().contains(rating));
    }

    @Test
    public void testEqualsMethod() {
        AnimalShelter shelter1 = new AnimalShelter("Happy Paws", 50);
        shelter1.setId(1L);
        AnimalShelter shelter2 = new AnimalShelter("Happy Paws", 50);
        shelter2.setId(1L);

        assertEquals(shelter1, shelter2);

        shelter2.setCapacity(60);
        assertNotEquals(shelter1, shelter2);
    }

    @Test
    public void testHashCodeMethod() {
        AnimalShelter shelter1 = new AnimalShelter("Happy Paws", 50);
        shelter1.setId(1L);
        AnimalShelter shelter2 = new AnimalShelter("Happy Paws", 50);
        shelter2.setId(1L);

        assertEquals(shelter1.hashCode(), shelter2.hashCode());

        shelter2.setCapacity(60);
        assertNotEquals(shelter1.hashCode(), shelter2.hashCode());
    }

    @Test
    public void testToStringMethod() {
        shelter.setId(1L);
        shelter.setName("Happy Paws");
        shelter.setCapacity(50);

        String expected = "AnimalShelter{id=1, name='Happy Paws', capacity=50}";
        assertEquals(expected, shelter.toString());
    }
}
