package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * JavaFX App
 */
public class Main extends Application {
    private static Scene scene;
    private int width = 640;
    private int height = 480;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login"), width, height);
        stage.setScene(scene);

        stage.setResizable(false);
        stage.initStyle(StageStyle.UNIFIED);
        stage.setTitle("Animal Shelter");

        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        try {
            scene.setRoot(loadFXML(fxml));
        } catch (IOException e) {
            e.printStackTrace();
            // TODO Error handling
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}