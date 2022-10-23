package com.macewan305;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PropertyGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();

        Parent root = FXMLLoader.load(getClass().getResource("PropertyAssessmentGUI.fxml"));

        Scene scene = new Scene(root, 1400, 700);

        Stage stage = new Stage();

        // Configure stage
        stage.setTitle("Edmonton Property Assessments");
        stage.setScene(scene);
        stage.show();
    }
}
