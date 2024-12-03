package com.example.dao;

import com.example.model.AnimalShelter;
import com.example.model.Rating;
import java.util.List;

public interface RatingDao {
    void save(Rating rating);

    void update(Rating rating);

    void delete(Rating rating);

    Rating findById(Long id);

    List<Rating> findAllByShelter(AnimalShelter shelter);

    Double getAverageRatingForShelter(AnimalShelter shelter);
}