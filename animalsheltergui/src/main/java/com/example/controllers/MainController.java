package com.example.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.function.Consumer;

import com.example.Role;
import com.example.UserSession;
import com.example.dao.RatingDao;
import com.example.exceptions.AnimalAlreadyExistsException;
import com.example.exceptions.ShelterFullException;
import com.example.model.Animal;
import com.example.model.AnimalCondition;
import com.example.model.AnimalShelter;
import com.example.service.SerializationService;
import com.example.service.ShelterService;
import com.example.utils.AlertUtils;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainController {
    private static final String ALL_CONDITIONS = "All";

    private final ShelterService shelterService;
    private ObservableList<Animal> animalList;
    private ObservableList<AnimalShelter> shelterList;
    private FilteredList<AnimalShelter> filteredShelters;
    private FilteredList<Animal> filteredAnimals;

    @FXML
    private TableView<Animal> tableView;
    @FXML
    private TableColumn<Animal, String> nameColumn;
    @FXML
    private TableColumn<Animal, String> speciesColumn;
    @FXML
    private TableColumn<Animal, String> conditionColumn;
    @FXML
    private TableColumn<Animal, Integer> ageColumn;
    @FXML
    private TableColumn<Animal, Double> weightColumn;
    @FXML
    private TableColumn<AnimalShelter, String> actionsColumn;

    @FXML
    private Button addAnimalButton;
    @FXML
    private ComboBox<String> conditionComboBox;
    @FXML
    private TextField animalSearchInput;
    @FXML
    private Button animalSearchButton;
    @FXML
    private Button animalSortButton;

    @FXML
    private TableView<AnimalShelter> shelterTableView;
    @FXML
    private TableColumn<AnimalShelter, String> shelterNameColumn;
    @FXML
    private TableColumn<AnimalShelter, Integer> shelterCapacityColumn;
    @FXML
    private TableColumn<AnimalShelter, String> shelterActionsColumn;
    @FXML
    private TableColumn<AnimalShelter, Double> shelterRatingColumn;
    @FXML
    private Button addShelterButton;
    @FXML
    private TextField shelterSearchInput;
    @FXML
    private Button shelterSearchButton;
    @FXML
    private Button shelterSortButton;

    @FXML
    private Text usernameText;
    @FXML
    private Button signoutButton;

    @FXML
    private TextField animalSearchField;
    @FXML
    private TextField shelterSearchField;

    @FXML
    private MenuBar menuBar;

    private final SerializationService serializationService;

    private final RatingDao ratingDao;

    public MainController(ShelterService shelterService, RatingDao ratingDao) {
        this.shelterService = shelterService;
        this.ratingDao = ratingDao;
        this.serializationService = new SerializationService();
        this.animalList = FXCollections.observableArrayList();
        this.shelterList = FXCollections.observableArrayList();
        this.filteredShelters = new FilteredList<>(shelterList, p -> true);
        this.filteredAnimals = new FilteredList<>(animalList, p -> true);
    }

    @FXML
    void initialize() {
        UserSession userSession = UserSession.getInstance();
        String username = userSession.getUsername();
        Role role = userSession.getRole();

        initializeTableColumns(role);

        shelterList.setAll(shelterService.getAllShelters());
        shelterTableView.setItems(filteredShelters);
        tableView.setItems(filteredAnimals);

        setupListeners();
        setupEventHandlers();

        usernameText.setText("Welcome, " + username);

        initializeConditionComboBox();

        setupSearchFields();

        refreshTableView("shelter");
        refreshTableView("animal");
    }

    private void setupListeners() {
        shelterTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                AnimalShelter refreshedShelter = shelterService.getShelterById(newValue.getId());
                animalList.setAll(new ArrayList<>(refreshedShelter.getAnimals()));
            } else {
                animalList.clear();
            }
        });
    }

    private void setupEventHandlers() {
        signoutButton.setOnAction(event -> handleLogout());
        addAnimalButton.setOnAction(event -> addAnimal());
        addShelterButton.setOnAction(event -> addShelter());
        shelterSortButton.setOnAction(event -> sortShelters());
        animalSortButton.setOnAction(event -> sortAnimals());
    }

    private void initializeConditionComboBox() {
        conditionComboBox.setItems(FXCollections.observableArrayList(
                ALL_CONDITIONS,
                AnimalCondition.HEALTHY.toString(),
                AnimalCondition.SICK.toString(),
                AnimalCondition.QUARANTINED.toString()));
        conditionComboBox.setValue(ALL_CONDITIONS);
        conditionComboBox.valueProperty()
                .addListener((observable, oldValue, newValue) -> filterAnimalsByCondition(newValue));
    }

    private void filterAnimalsByCondition(String condition) {
        AnimalShelter selectedShelter = shelterTableView.getSelectionModel().getSelectedItem();
        if (selectedShelter == null) {
            return;
        }

        if (condition.equals(ALL_CONDITIONS)) {
            animalList.setAll(selectedShelter.getAnimalList());
        } else {
            AnimalCondition selectedCondition = AnimalCondition.valueOf(condition);
            List<Animal> filteredList = selectedShelter.getAnimalList().stream()
                    .filter(animal -> animal.getCondition() == selectedCondition).collect(Collectors.toList());
            animalList.setAll(filteredList);
        }
    }

    private void sortShelters() {
        List<AnimalShelter> sorted = new ArrayList<>(shelterList);
        sorted.sort(Comparator.comparing(AnimalShelter::getName));
        shelterList.setAll(sorted);
    }

    private void sortAnimals() {
        List<Animal> sorted = new ArrayList<>(animalList);
        sorted.sort(Comparator.comparing(Animal::getName));
        animalList.setAll(sorted);
    }

    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/login.fxml"));
            Parent loginView = loader.load();

            Stage currentStage = (Stage) signoutButton.getScene().getWindow();

            Scene loginScene = new Scene(loginView);

            currentStage.setScene(loginScene);
            currentStage.show();
        } catch (IOException e) {
            AlertUtils.showError("Error", "Could not load login view: " + e.getMessage());
        }
    }

    private void refreshTableView(String type) {
        if (type.equals("animal")) {
            AnimalShelter selectedShelter = shelterTableView.getSelectionModel().getSelectedItem();
            if (selectedShelter != null) {
                List<Animal> animals = selectedShelter.getAnimalList();
                animalList.clear();

                if (animals == null || animals.isEmpty()) {
                    tableView.setPlaceholder(new Label("This shelter has no animals yet"));
                } else {
                    animalList.setAll(animals);
                }
            } else {
                tableView.setPlaceholder(new Label("Please select a shelter to see its animals"));
                animalList.clear();
            }
        } else if (type.equals("shelter")) {
            List<AnimalShelter> shelters = shelterService.getAllShelters();
            if (shelters.isEmpty()) {
                shelterTableView.setPlaceholder(new Label("No shelters found"));
            }
            shelterList.setAll(shelters);
        }
    }

    private void initializeTableColumns(Role role) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        speciesColumn.setCellValueFactory(new PropertyValueFactory<>("species"));
        conditionColumn.setCellValueFactory(new PropertyValueFactory<>("condition"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));

        shelterNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        shelterCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        shelterRatingColumn.setCellValueFactory(data -> {
            Double avgRating = ratingDao.getAverageRatingForShelter(data.getValue());
            return new SimpleDoubleProperty(avgRating != null ? avgRating : 0.0).asObject();
        });

        shelterRatingColumn.setCellFactory(column -> new TableCell<>() {
            private final Button commentsButton = new Button("View");

            {
                commentsButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #2196F3;");
                commentsButton.setOnAction(e -> {
                    if (getTableRow() != null && getTableRow().getItem() != null) {
                        showComments(getTableRow().getItem());
                    }
                });
            }

            @Override
            protected void updateItem(Double rating, boolean empty) {
                super.updateItem(rating, empty);
                if (empty || rating == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox container = new HBox(5);
                    container.setAlignment(Pos.CENTER);
                    Label ratingLabel = new Label(String.format("%.1f ★", rating));
                    container.getChildren().addAll(ratingLabel, commentsButton);
                    setGraphic(container);
                }
            }
        });

        if (role == Role.ADMIN) {
            shelterCapacityColumn.setVisible(true);
            shelterActionsColumn.setVisible(true);
        }

        if (role == Role.ADMIN) {
            actionsColumn.setCellFactory(getActionsCellFactory("animal", role));
            shelterActionsColumn.setVisible(true);
            shelterActionsColumn.setCellFactory(getActionsCellFactory("shelter", role));
            shelterCapacityColumn.setVisible(true);
            shelterCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));

            addAnimalButton.setVisible(true);
            addShelterButton.setVisible(true);
        } else {
            actionsColumn.setCellFactory(getActionsCellFactory("animal", role));
            shelterActionsColumn.setVisible(false);
            shelterCapacityColumn.setVisible(false);

            addAnimalButton.setVisible(false);
            addShelterButton.setVisible(false);
        }
    }

    private <T> Callback<TableColumn<T, String>, TableCell<T, String>> getActionsCellFactory(String type, Role role) {
        if (role == Role.USER) {
            return param -> new TableCell<T, String>() {
                private final Button adoptButton = new Button("Adopt");

                {
                    adoptButton.setOnAction(event -> handleAdopt(getTableView().getItems().get(getIndex())));
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : adoptButton);
                }
            };
        } else {
            return param -> new TableCell<T, String>() {
                private final Button editButton = new Button("\u270F");
                private final Button deleteButton = new Button("\uD83D\uDDD1");
                private final HBox container = new HBox(5, editButton, deleteButton);

                {
                    container.setAlignment(Pos.CENTER);
                    editButton.setOnAction(event -> handleEditAction(type, this));
                    deleteButton.setOnAction(event -> handleDeleteAction(type, this));
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : container);
                }
            };
        }
    }

    private <T> void handleEditAction(String type, TableCell<T, String> cell) {
        T item = cell.getTableRow().getItem();

        if (type.equals("animal")) {
            Animal animal = (Animal) item;
            if (animal == null) {
                AlertUtils.showError("Error", "Please select an animal to edit");
                return;
            }
            editAnimal(animal);
        } else if (type.equals("shelter")) {
            AnimalShelter shelter = (AnimalShelter) item;
            if (shelter == null) {
                AlertUtils.showError("Error", "Please select a shelter to edit");
                return;
            }
            editShelter(shelter);
        }
    }

    private <T> void handleDeleteAction(String type, TableCell<T, String> cell) {
        T item = cell.getTableView().getItems().get(cell.getIndex());
        if (type.equals("animal")) {
            handleAnimalDelete((Animal) item);
        } else if (type.equals("shelter")) {
            handleShelterDelete((AnimalShelter) item);
        }
    }

    private void handleAnimalDelete(Animal animal) {
        AnimalShelter selectedShelter = shelterTableView.getSelectionModel().getSelectedItem();
        if (selectedShelter == null) {
            AlertUtils.showError("Error", "No shelter selected");
            return;
        }

        if (AlertUtils.showConfirmation("Confirm Delete", "Are you sure you want to remove this animal?")) {
            try {
                shelterService.removeAnimalFromShelter(selectedShelter, animal);
                refreshTableView("shelter");
                shelterTableView.getSelectionModel().select(selectedShelter);
                refreshTableView("animal");
                AlertUtils.showInfo("Success", "Animal removed successfully");
            } catch (Exception e) {
                AlertUtils.showError("Error", "Failed to remove animal: " + e.getMessage());
            }
        }
    }

    private void handleShelterDelete(AnimalShelter shelter) {
        String message = shelter.getAnimals().isEmpty() ? "Are you sure you want to delete this empty shelter?"
                : "This shelter contains " + shelter.getAnimals().size() +
                        " animals. Are you sure you want to delete it and all its animals?";

        if (AlertUtils.showConfirmation("Confirm Delete", message)) {
            try {
                shelterService.deleteShelter(shelter);
                refreshTableView("shelter");
                AlertUtils.showInfo("Success", "Shelter deleted successfully");
            } catch (Exception e) {
                AlertUtils.showError("Error", "Failed to delete shelter: " + e.getMessage());
            }
        }
    }

    private <T> void handleAdopt(T item) {
        Animal animal = (Animal) item;
        AnimalShelter selectedShelter = shelterTableView.getSelectionModel().getSelectedItem();
        if (selectedShelter == null) {
            return;
        }

        try {
            shelterService.removeAnimalFromShelter(selectedShelter, animal);
            animal.setCondition(AnimalCondition.ADOPTED);
            shelterService.updateAnimal(animal);
            refreshTableView("animal");
        } catch (Exception e) {
            AlertUtils.showError("Error", "Failed to adopt animal: " + e.getMessage());
        }
    }

    private void editShelter(AnimalShelter shelterToEdit) {
        try {
            final AnimalShelter[] shelterHolder = { shelterService.getShelterById(shelterToEdit.getId()) };

            final EditShelterDialogController controller = (EditShelterDialogController) showDialog(
                    "editShelterDialog",
                    "Edit Shelter",
                    dialogController -> {
                        ((EditShelterDialogController) dialogController).setShelter(shelterHolder[0]);
                    });

            if (controller != null && controller.isOkClicked()) {
                try {
                    AnimalShelter updatedShelter = controller.getShelter();
                    shelterService.updateShelter(updatedShelter);
                    refreshTableView("shelter");
                    AlertUtils.showInfo("Success", "Shelter updated successfully");
                } catch (Exception e) {
                    AlertUtils.showError("Error", "Failed to update shelter: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            AlertUtils.showError("Error", "Failed to load shelter: " + e.getMessage());
        }
    }

    private void editAnimal(Animal animalToEdit) {
        try {
            final Animal[] animalHolder = { shelterService.getAnimalById(animalToEdit.getId()) };

            final EditAnimalDialogController controller = (EditAnimalDialogController) showDialog(
                    "editAnimalDialog",
                    "Edit Animal",
                    dialogController -> {
                        ((EditAnimalDialogController) dialogController).setAnimal(animalHolder[0]);
                    });

            if (controller != null && controller.isOkClicked()) {
                try {
                    Animal updatedAnimal = controller.getAnimal();
                    shelterService.updateAnimal(updatedAnimal);

                    refreshTableView("shelter");
                    AnimalShelter selectedShelter = shelterTableView.getSelectionModel().getSelectedItem();
                    if (selectedShelter != null) {
                        shelterTableView.getSelectionModel().select(selectedShelter);
                        refreshTableView("animal");
                    }

                    AlertUtils.showInfo("Success", "Animal updated successfully");
                } catch (Exception e) {
                    AlertUtils.showError("Error", "Failed to update animal: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            AlertUtils.showError("Error", "Failed to load animal: " + e.getMessage());
        }
    }

    private Object showDialog(String fxmlName, String title, Consumer<Object> controllerInitializer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/" + fxmlName + ".fxml"));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(signoutButton.getScene().getWindow());
            dialogStage.setScene(new Scene(root));

            Object controller = loader.getController();
            if (controller instanceof DialogController) {
                ((DialogController) controller).setDialogStage(dialogStage);
            }

            if (controllerInitializer != null) {
                controllerInitializer.accept(controller);
            }

            dialogStage.showAndWait();
            return controller;

        } catch (IOException e) {
            AlertUtils.showError("Error", "Could not load dialog: " + e.getMessage());
            return null;
        }
    }

    private void addAnimal() {
        AnimalShelter selectedShelter = shelterTableView.getSelectionModel().getSelectedItem();
        if (selectedShelter == null) {
            AlertUtils.showError("Error", "Please select a shelter first");
            return;
        }

        final AddAnimalDialogController controller = (AddAnimalDialogController) showDialog(
                "addAnimalDialog",
                "Add Animal",
                null);

        if (controller != null && controller.isOkClicked()) {
            Animal newAnimal = controller.getAnimal();
            try {
                shelterService.addAnimalToShelter(selectedShelter, newAnimal);
                refreshTableView("shelter");
                shelterTableView.getSelectionModel().select(selectedShelter);
                refreshTableView("animal");
                AlertUtils.showInfo("Success", "Animal added successfully");
            } catch (ShelterFullException | AnimalAlreadyExistsException e) {
                AlertUtils.showError("Error", e.getMessage());
            } catch (Exception e) {
                AlertUtils.showError("Error", "Failed to add animal: " + e.getMessage());
            }
        }
    }

    private void addShelter() {
        final AddShelterDialogController controller = (AddShelterDialogController) showDialog(
                "addShelterDialog",
                "Add Shelter",
                null);

        if (controller != null && controller.isOkClicked()) {
            try {
                AnimalShelter newShelter = controller.getShelter();
                shelterService.saveShelter(newShelter);
                refreshTableView("shelter");
                AlertUtils.showInfo("Success", "Shelter added successfully");
            } catch (Exception e) {
                AlertUtils.showError("Error", "Failed to add shelter: " + e.getMessage());
            }
        }
    }

    private void setupSearchFields() {
        Timer searchTimer = new Timer(true);
        TimerTask[] animalSearchTask = new TimerTask[1];
        TimerTask[] shelterSearchTask = new TimerTask[1];

        animalSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (animalSearchTask[0] != null) {
                animalSearchTask[0].cancel();
            }

            animalSearchTask[0] = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        filteredAnimals.setPredicate(animal -> {
                            if (newValue == null || newValue.isEmpty()) {
                                return true;
                            }
                            return animal.getName().toLowerCase().contains(newValue.toLowerCase());
                        });
                    });
                }
            };
            searchTimer.schedule(animalSearchTask[0], 300);
        });

        shelterSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (shelterSearchTask[0] != null) {
                shelterSearchTask[0].cancel();
            }

            shelterSearchTask[0] = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        filteredShelters.setPredicate(shelter -> {
                            if (newValue == null || newValue.isEmpty()) {
                                return true;
                            }
                            return shelter.getName().toLowerCase().contains(newValue.toLowerCase());
                        });
                    });
                }
            };
            searchTimer.schedule(shelterSearchTask[0], 300);
        });
    }

    @FXML
    private void handleExportSheltersToBinary() {
        File file = showFileChooser("Export Shelters", "bin", true);
        if (file != null) {
            try {
                List<AnimalShelter> shelters = shelterService.getAllShelters();
                serializationService.saveToFile(shelters, file.getAbsolutePath());
                AlertUtils.showInfo("Success", "Shelters exported successfully");
            } catch (IOException e) {
                AlertUtils.showError("Error", "Failed to export shelters: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleImportSheltersFromBinary() {
        File file = showFileChooser("Import Shelters", "bin", false);
        if (file != null) {
            try {
                List<AnimalShelter> shelters = serializationService.loadFromFile(file.getAbsolutePath());
                shelterService.importShelters(shelters);
                refreshTableView("shelter");
                AlertUtils.showInfo("Success", "Shelters imported successfully");
            } catch (Exception e) {
                AlertUtils.showError("Error", "Failed to import shelters: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleExportAnimalsToBinary() {
        AnimalShelter selectedShelter = shelterTableView.getSelectionModel().getSelectedItem();
        if (selectedShelter == null) {
            AlertUtils.showError("Error", "Please select a shelter first");
            return;
        }

        File file = showFileChooser("Export Animals", "bin", true);
        if (file != null) {
            try {
                serializationService.saveToFile(new ArrayList<>(selectedShelter.getAnimals()),
                        file.getAbsolutePath());
                AlertUtils.showInfo("Success", "Animals exported successfully");
            } catch (IOException e) {
                AlertUtils.showError("Error", "Failed to export animals: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleImportAnimalsFromBinary() {
        AnimalShelter selectedShelter = shelterTableView.getSelectionModel().getSelectedItem();
        if (selectedShelter == null) {
            AlertUtils.showError("Error", "Please select a shelter first");
            return;
        }

        File file = showFileChooser("Import Animals", "bin", false);
        if (file != null) {
            try {
                List<Animal> animals = serializationService.loadFromFile(file.getAbsolutePath());
                for (Animal animal : animals) {
                    try {
                        shelterService.addAnimalToShelter(selectedShelter, animal);
                    } catch (Exception e) {
                        AlertUtils.showError("Warning",
                                "Could not add animal " + animal.getName() + ": " + e.getMessage());
                    }
                }
                refreshTableView("animal");
                AlertUtils.showInfo("Success", "Animals imported successfully");
            } catch (Exception e) {
                AlertUtils.showError("Error", "Failed to import animals: " + e.getMessage());
            }
        }
    }

    private File showFileChooser(String title, String extension, boolean isSave) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        extension.toUpperCase() + " files (*." + extension + ")",
                        "*." + extension));

        return isSave ? fileChooser.showSaveDialog(shelterTableView.getScene().getWindow())
                : fileChooser.showOpenDialog(shelterTableView.getScene().getWindow());
    }

    @FXML
    private void handleExportSheltersToCsv() {
        File file = showFileChooser("Export Shelters", "csv", true);
        if (file != null) {
            try {
                shelterService.exportToCSV(file.getAbsolutePath());
                AlertUtils.showInfo("Success", "Shelters exported successfully");
            } catch (IOException e) {
                AlertUtils.showError("Error", "Failed to export shelters: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleImportSheltersFromCsv() {
        File file = showFileChooser("Import Shelters", "csv", false);
        if (file != null) {
            try {
                List<AnimalShelter> shelters = serializationService.importSheltersFromCsv(file.getAbsolutePath());
                shelterService.importShelters(shelters);
                refreshTableView("shelter");
                AlertUtils.showInfo("Success", "Shelters imported successfully");
            } catch (Exception e) {
                AlertUtils.showError("Error", "Failed to import shelters: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleExportAnimalsToCsv() {
        AnimalShelter selectedShelter = shelterTableView.getSelectionModel().getSelectedItem();
        if (selectedShelter == null) {
            AlertUtils.showError("Error", "Please select a shelter first");
            return;
        }

        File file = showFileChooser("Export Animals", "csv", true);
        if (file != null) {
            try {
                serializationService.exportAnimalsToCsv(new ArrayList<>(selectedShelter.getAnimals()),
                        file.getAbsolutePath());
                AlertUtils.showInfo("Success", "Animals exported successfully");
            } catch (IOException e) {
                AlertUtils.showError("Error", "Failed to export animals: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleImportAnimalsFromCsv() {
        AnimalShelter selectedShelter = shelterTableView.getSelectionModel().getSelectedItem();
        if (selectedShelter == null) {
            AlertUtils.showError("Error", "Please select a shelter first");
            return;
        }

        File file = showFileChooser("Import Animals", "csv", false);
        if (file != null) {
            try {
                List<Animal> animals = serializationService.importAnimalsFromCsv(file.getAbsolutePath());
                for (Animal animal : animals) {
                    try {
                        shelterService.addAnimalToShelter(selectedShelter, animal);
                    } catch (Exception e) {
                        AlertUtils.showError("Warning",
                                "Could not add animal " + animal.getName() + ": " + e.getMessage());
                    }
                }
                refreshTableView("animal");
                AlertUtils.showInfo("Success", "Animals imported successfully");
            } catch (Exception e) {
                AlertUtils.showError("Error", "Failed to import animals: " + e.getMessage());
            }
        }
    }

    private void showComments(AnimalShelter shelter) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ratingsDialog.fxml"));
            DialogPane dialogPane = loader.load();

            RatingsDialogController controller = loader.getController();
            controller.setShelter(shelter);
            controller.setRatingDao(ratingDao);

            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Ratings for " + shelter.getName());
            dialog.setDialogPane(dialogPane);

            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            controller.setDialogStage(stage);

            dialog.setOnCloseRequest(event -> {
                refreshTableView("shelter");
            });

            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showError("Error", "Could not load ratings dialog: " + e.getMessage());
        }
    }

    @FXML
    private void handleExportToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showSaveDialog(shelterTableView.getScene().getWindow());
        if (file != null) {
            try {
                shelterService.exportToCSV(file.getAbsolutePath());
                AlertUtils.showInfo("Success", "Data exported successfully to CSV!");
            } catch (IOException e) {
                AlertUtils.showError("Error", "Failed to export data: " + e.getMessage());
            }
        }
    }
}