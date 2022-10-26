package com.macewan305;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

        TableView table = new TableView<PropertyAssessment>();

        // Creating all the columns
        TableColumn<PropertyAssessment, Integer> accountNum = new TableColumn<>("Account");
        TableColumn<PropertyAssessment, String> address = new TableColumn<>("Address");
        TableColumn<PropertyAssessment, Integer> assessVal = new TableColumn<>("Assessed Value");
        TableColumn<PropertyAssessment, String> classes = new TableColumn<>("Assessment Class");
        TableColumn<PropertyAssessment, String> neighbourhood = new TableColumn<>("Neighbourhood");
        TableColumn<PropertyAssessment, String> location = new TableColumn<>("(Latitude, Longitude)");

        // Associating each column to extract respective data
        accountNum.setCellValueFactory( new PropertyValueFactory<PropertyAssessment, Integer>("AccountNum"));
        address.setCellValueFactory( new PropertyValueFactory<PropertyAssessment, String>("Address"));
        assessVal.setCellValueFactory( new PropertyValueFactory<PropertyAssessment, Integer>("AssessmentVal"));
        classes.setCellValueFactory( new PropertyValueFactory<PropertyAssessment, String>("AllClasses"));
        neighbourhood.setCellValueFactory( new PropertyValueFactory<PropertyAssessment, String>("NeighbourhoodName"));
        location.setCellValueFactory( new PropertyValueFactory<PropertyAssessment, String>("Location"));

        table.getColumns().addAll(accountNum, address, assessVal, classes, neighbourhood, location);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.getItems().add(new PropertyAssessment(101000 ,"3421","69230",
                "Test St.","Y","3515","Testing Neighbourhood","Honda Civic Ward",519603,"59.29503","102.352","POINT (102.352352, 59.29503319)",
                "50", "50", "","RESIDENTIAL", "COMMERCIAL" ,""));

        return table;
    }
}
