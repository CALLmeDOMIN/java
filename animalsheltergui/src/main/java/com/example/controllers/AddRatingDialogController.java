package com.example.controllers;

import com.example.model.AnimalShelter;
import com.example.model.Rating;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;

public class AddRatingDialogController {
    @FXML
    private Slider ratingSlider;
    @FXML
    private TextArea commentArea;

    private Rating result;

    @FXML
    private void initialize() {
        ratingSlider.setValue(5);
    }

    public Rating getResult() {
        return result;
    }

    public void createRating(AnimalShelter shelter) {
        int value = (int) ratingSlider.getValue();
        String comment = commentArea.getText().trim();
        if (comment.isEmpty()) {
            comment = null;
        }
        result = new Rating(value, shelter, comment);
    }
}