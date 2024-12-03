package com.example.controllers;

import com.example.model.AnimalShelter;
import com.example.utils.AlertUtils;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddShelterDialogController implements DialogController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField capacityField;

    private Stage dialogStage;
    private AnimalShelter shelter;
    private boolean okClicked = false;

    @Override
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            try {
                shelter = new AnimalShelter(
                        nameField.getText().trim(),
                        Integer.parseInt(capacityField.getText().trim()));
                okClicked = true;
                dialogStage.close();
            } catch (NumberFormatException e) {
                AlertUtils.showError("Input Error", "Please enter a valid number for capacity");
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

    public AnimalShelter getShelter() {
        return shelter;
    }

    private boolean isInputValid() {
        StringBuilder errorMessage = new StringBuilder();

        if (isNullOrEmpty(nameField.getText())) {
            errorMessage.append("Name is required!\n");
        }
        if (!isValidNumber(capacityField.getText())) {
            errorMessage.append("Please enter a valid capacity!\n");
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
}
