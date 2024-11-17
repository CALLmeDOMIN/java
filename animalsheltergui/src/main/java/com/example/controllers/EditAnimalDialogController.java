package com.example.controllers;

import com.example.animalshelter.Animal;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditAnimalDialogController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField speciesField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField priceField;

    private Animal animal;
    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
        nameField.setText(animal.getName());
        speciesField.setText(animal.getSpecies());
        ageField.setText(String.valueOf(animal.getAge()));
        priceField.setText(String.valueOf(animal.getPrice()));
    }

    @FXML
    private void handleSave() {
        try {
            animal.setName(nameField.getText());
            animal.setSpecies(speciesField.getText());
            animal.setAge(Integer.parseInt(ageField.getText()));
            animal.setPrice(Double.parseDouble(priceField.getText()));
            dialogStage.close();
        } catch (NumberFormatException e) {
            System.err.println("Invalid input: " + e.getMessage());
        }
    }
}