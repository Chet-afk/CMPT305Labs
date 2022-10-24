package com.macewan305;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class PropertyGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();

        Scene background = new Scene(borderPane, 1400, 700);

        Stage stage = new Stage();



        // Configure stage
        stage.setTitle("Edmonton Property Assessments");
        stage.setScene(background);

        stage.setScene(new Scene(makeTable(), 1400, 700));

        stage.show();
    }

    private TableView makeTable(){

        TableView table = new TableView<>();

        TableColumn<PropertyAssessment, Integer> accountNum = new TableColumn<>("Account");
        table.getColumns().add(accountNum);

        TableColumn<PropertyAssessment, String> address = new TableColumn<>("Address");
        table.getColumns().add(address);

        TableColumn<PropertyAssessment, Integer> assessVal = new TableColumn<>("Assessed Value");
        table.getColumns().add(assessVal);

        TableColumn<PropertyAssessment, String> classes = new TableColumn<>("Assessment Class");
        table.getColumns().add(classes);

        TableColumn<PropertyAssessment, String> neighbourhood = new TableColumn<>("Neighbourhood");
        table.getColumns().add(neighbourhood);

        TableColumn<PropertyAssessment, String> location = new TableColumn<>("(Latitude, Longitude)");
        table.getColumns().add(location);

        return table;
    }
}
