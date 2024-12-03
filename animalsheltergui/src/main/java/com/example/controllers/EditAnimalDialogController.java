package com.example.controllers;

import com.example.model.Animal;
import com.example.model.AnimalCondition;
import com.example.utils.AlertUtils;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EditAnimalDialogController implements DialogController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField speciesField;
    @FXML
    private ComboBox<AnimalCondition> conditionComboBox;
    @FXML
    private TextField ageField;
    @FXML
    private TextField weightField;

    private Stage dialogStage;
    private Animal animal;
    private boolean okClicked = false;

    @Override
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize() {
        conditionComboBox.getItems().addAll(
                Arrays.stream(AnimalCondition.values())
                        .filter(condition -> condition != AnimalCondition.ADOPTED)
                        .collect(Collectors.toList()));
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
        nameField.setText(animal.getName());
        speciesField.setText(animal.getSpecies());
        conditionComboBox.setValue(animal.getCondition());
        ageField.setText(String.valueOf(animal.getAge()));
        weightField.setText(String.valueOf(animal.getWeight()));
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            try {
                animal.setName(nameField.getText().trim());
                animal.setSpecies(speciesField.getText().trim());
                animal.setCondition(conditionComboBox.getValue());
                animal.setAge(Integer.parseInt(ageField.getText().trim()));
                animal.setWeight(Double.parseDouble(weightField.getText().trim()));
                okClicked = true;
                dialogStage.close();
            } catch (NumberFormatException e) {
                AlertUtils.showError("Input Error", "Please enter valid numbers for age and weight");
            }
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public Animal getAnimal() {
        return animal;
    }

    private boolean isInputValid() {
        StringBuilder errorMessage = new StringBuilder();

        if (isNullOrEmpty(nameField.getText())) {
            errorMessage.append("Name is required!\n");
        }
        if (isNullOrEmpty(speciesField.getText())) {
            errorMessage.append("Species is required!\n");
        }
        if (conditionComboBox.getValue() == null) {
            errorMessage.append("Please select a condition!\n");
        }
        if (!isValidNumber(ageField.getText())) {
            errorMessage.append("Please enter a valid age!\n");
        }
        if (!isValidDecimalNumber(weightField.getText())) {
            errorMessage.append("Please enter a valid weight!\n");
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            AlertUtils.showError("Validation Error", errorMessage.toString());
            return false;
        }
    }

    private boolean isNullOrEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    private boolean isValidNumber(String text) {
        if (isNullOrEmpty(text)) {
            return false;
        }
        try {
            int value = Integer.parseInt(text);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidDecimalNumber(String text) {
        if (isNullOrEmpty(text)) {
            return false;
        }
        try {
            double value = Double.parseDouble(text);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}