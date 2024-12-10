package com.example.dev.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {

    private Animal animal;

    @BeforeEach
    public void setUp() {
        animal = new Animal();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(animal);
        assertNull(animal.getId());
        assertNull(animal.getName());
        assertNull(animal.getSpecies());
        assertEquals(0, animal.getAge());
        assertNull(animal.getCondition());
        assertNull(animal.getShelter());
    }

    @Test
    public void testParameterizedConstructor() {
        Animal animal = new Animal("Buddy", "Dog", 3, AnimalCondition.HEALTHY);
        assertEquals("Buddy", animal.getName());
        assertEquals("Dog", animal.getSpecies());
        assertEquals(3, animal.getAge());
        assertEquals(AnimalCondition.HEALTHY, animal.getCondition());
        assertNull(animal.getShelter());
    }

    @Test
    public void testSettersAndGetters() {
        animal.setId(1L);
        animal.setName("Whiskers");
        animal.setSpecies("Cat");
        animal.setAge(2);
        animal.setCondition(AnimalCondition.SICK);

        AnimalShelter shelter = new AnimalShelter("Happy Paws", 50);
        animal.setShelter(shelter);

        assertEquals(1L, animal.getId());
        assertEquals("Whiskers", animal.getName());
        assertEquals("Cat", animal.getSpecies());
        assertEquals(2, animal.getAge());
        assertEquals(AnimalCondition.SICK, animal.getCondition());
        assertEquals(shelter, animal.getShelter());
    }

    @Test
    public void testEqualsMethod() {
        Animal animal1 = new Animal("Buddy", "Dog", 3, AnimalCondition.HEALTHY);
        animal1.setId(1L);

        Animal animal2 = new Animal("Buddy", "Dog", 3, AnimalCondition.HEALTHY);
        animal2.setId(1L);

        assertEquals(animal1, animal2);

        animal2.setSpecies("Cat");
        assertNotEquals(animal1, animal2);
    }

    @Test
    public void testHashCodeMethod() {
        Animal animal1 = new Animal("Buddy", "Dog", 3, AnimalCondition.HEALTHY);
        animal1.setId(1L);

        Animal animal2 = new Animal("Buddy", "Dog", 3, AnimalCondition.HEALTHY);
        animal2.setId(1L);

        assertEquals(animal1.hashCode(), animal2.hashCode());

        animal2.setSpecies("Cat");
        assertNotEquals(animal1.hashCode(), animal2.hashCode());
    }

    @Test
    public void testToStringMethod() {
        animal.setId(1L);
        animal.setName("Buddy");
        animal.setSpecies("Dog");
        animal.setAge(3);
        animal.setCondition(AnimalCondition.HEALTHY);

        String expected = "Animal{id=1, name='Buddy', species='Dog', age=3, condition=HEALTHY}";
        assertEquals(expected, animal.toString());
    }
}
