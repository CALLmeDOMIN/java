package com.example.controllers;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import com.example.dao.RatingDao;
import com.example.model.AnimalShelter;
import com.example.model.Rating;
import com.example.utils.AlertUtils;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RatingsDialogController implements DialogController {
    @FXML
    private VBox contentVBox;
    @FXML
    private ScrollPane scrollPane;

    private AnimalShelter shelter;
    private RatingDao ratingDao;
    private Stage dialogStage;

    @FXML
    private void initialize() {
    }

    @Override
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;

        Platform.runLater(() -> {
            DialogPane dialogPane = (DialogPane) dialogStage.getScene().getRoot();
            dialogPane.getButtonTypes().stream()
                    .filter(buttonType -> "Add Rating".equals(buttonType.getText()))
                    .findFirst()
                    .ifPresent(buttonType -> {
                        Node addButton = dialogPane.lookupButton(buttonType);
                        if (addButton != null) {
                            addButton.addEventFilter(ActionEvent.ACTION, event -> {
                                event.consume();
                                handleAddRating();
                            });
                        }
                    });
        });
    }

    public void setShelter(AnimalShelter shelter) {
        this.shelter = shelter;
        refreshRatings();
    }

    public void setRatingDao(RatingDao ratingDao) {
        this.ratingDao = ratingDao;
        refreshRatings();
    }

    private void refreshRatings() {
        if (shelter == null || ratingDao == null || contentVBox == null) {
            return;
        }

        contentVBox.getChildren().clear();
        List<Rating> ratings = ratingDao.findAllByShelter(shelter);

        if (ratings.isEmpty()) {
            Label noRatingsLabel = new Label("No ratings yet");
            noRatingsLabel.setStyle("-fx-text-fill: gray;");
            contentVBox.getChildren().add(noRatingsLabel);
        } else {
            for (Rating rating : ratings) {
                VBox ratingBox = createRatingBox(rating);
                contentVBox.getChildren().add(ratingBox);
            }
        }
    }

    @FXML
    private void handleAddRating() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/addRatingDialog.fxml"));
            DialogPane dialogPane = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Add Rating for " + shelter.getName());
            dialog.setDialogPane(dialogPane);

            AddRatingDialogController controller = loader.getController();

            Optional<ButtonType> clickedButton = dialog.showAndWait();

            if (clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
                controller.createRating(shelter);
                Rating newRating = controller.getResult();

                if (newRating != null) {
                    ratingDao.save(newRating);
                    refreshRatings();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showError("Error", "Could not open add rating dialog: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.showError("Error", "Could not save rating: " + e.getMessage());
        }
    }

    private VBox createRatingBox(Rating rating) {
        VBox ratingBox = new VBox(5);
        ratingBox.setStyle("-fx-padding: 10; -fx-background-color: #f5f5f5; -fx-background-radius: 5;");

        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        double ratingValue = rating.getValue();
        Label ratingLabel = new Label(String.format("%.1f ★", ratingValue));
        ratingLabel.setStyle("-fx-font-weight: bold;");

        Label dateLabel = new Label(rating.getRatingDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        dateLabel.setStyle("-fx-text-fill: gray;");

        headerBox.getChildren().addAll(ratingLabel, dateLabel);

        VBox contentBox = new VBox(5);
        if (rating.getComment() != null && !rating.getComment().isEmpty()) {
            Label commentLabel = new Label(rating.getComment());
            commentLabel.setWrapText(true);
            contentBox.getChildren().add(commentLabel);
        }

        ratingBox.getChildren().addAll(headerBox, contentBox);
        return ratingBox;
    }
}