package com.example.controllers;

import com.example.animalshelter.AnimalShelter;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddShelterDialogController {
    private AnimalShelter shelter;
    private Stage dialogStage;

    @FXML
    private TextField nameField;

    @FXML
    private TextField maxCapacityField;

    @FXML
    private Text errorText;

    @FXML
    public void initialize() {
        maxCapacityField.setOnAction(event -> handleSave());
    }

    @FXML
    private void handleSave() {
        if (!isValidInput()) {
            return;
        }

        shelter = new AnimalShelter(nameField.getText(), Integer.parseInt(maxCapacityField.getText()));
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public AnimalShelter getShelter() {
        return shelter;
    }

    public int getMaxCapacity() {
        return Integer.parseInt(maxCapacityField.getText());
    }

    public boolean isValidInput() {
        String name = nameField.getText();
        String maxCapacity = maxCapacityField.getText();

        if (name == null || name.isEmpty() || maxCapacity == null || maxCapacity.isEmpty()) {
            errorText.setText("Please fill in all fields");
            return false;
        }

        try {
            Integer.parseInt(maxCapacity);
        } catch (NumberFormatException e) {
            errorText.setText("Wrong input format");
            return false;
        }

        return true;
    }
}
