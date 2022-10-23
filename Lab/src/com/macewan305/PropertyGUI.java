package com.macewan305;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PropertyGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane borderPane = new BorderPane();

        Scene scene = new Scene(borderPane, 1400, 700);

        Stage stage = new Stage();

        // Configure stage
        stage.setTitle("Edmonton Property Assessments");
        stage.setScene(scene);
        stage.show();
    }
}
