package com.example.dev.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class RatingTest {

    private Rating rating;

    @BeforeEach
    public void setUp() {
        rating = new Rating();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(rating);
        assertNull(rating.getId());
        assertEquals(0, rating.getValue());
        assertNull(rating.getComment());
        assertNull(rating.getRatingdate());
        assertNull(rating.getShelter());
    }

    @Test
    public void testParameterizedConstructor() {
        Rating rating = new Rating(5, "Great shelter!");
        assertEquals(5, rating.getValue());
        assertEquals("Great shelter!", rating.getComment());
        assertNotNull(rating.getRatingdate());
    }

    @Test
    public void testSettersAndGetters() {
        rating.setId(1L);
        rating.setValue(4);
        rating.setComment("Good shelter");
        LocalDateTime now = LocalDateTime.now();
        rating.setRatingdate(now);
        AnimalShelter shelter = new AnimalShelter("Happy Paws", 20);
        rating.setShelter(shelter);

        assertEquals(1L, rating.getId());
        assertEquals(4, rating.getValue());
        assertEquals("Good shelter", rating.getComment());
        assertEquals(now, rating.getRatingdate());
        assertEquals(shelter, rating.getShelter());
    }

    @Test
    public void testEqualsMethod() {
        Rating rating1 = new Rating(5, "Great shelter!");
        Rating rating2 = new Rating(5, "Great shelter!");
        rating1.setRatingdate(LocalDateTime.of(2023, 12, 10, 10, 0));
        rating2.setRatingdate(LocalDateTime.of(2023, 12, 10, 10, 0));

        assertEquals(rating1, rating2);

        rating2.setComment("Different comment");
        assertNotEquals(rating1, rating2);
    }

    @Test
    public void testHashCodeMethod() {
        Rating rating1 = new Rating(5, "Great shelter!");
        Rating rating2 = new Rating(5, "Great shelter!");
        rating1.setRatingdate(LocalDateTime.of(2023, 12, 10, 10, 0));
        rating2.setRatingdate(LocalDateTime.of(2023, 12, 10, 10, 0));

        assertEquals(rating1.hashCode(), rating2.hashCode());

        rating2.setComment("Different comment");
        assertNotEquals(rating1.hashCode(), rating2.hashCode());
    }

    @Test
    public void testToStringMethod() {
        rating.setId(1L);
        rating.setValue(5);
        rating.setComment("Great shelter!");
        rating.setRatingdate(LocalDateTime.of(2023, 12, 10, 10, 0));

        String expected = "Rating{id=1, value=5, comment='Great shelter!', ratingDate=2023-12-10T10:00}";
        assertEquals(expected, rating.toString());
    }
}
