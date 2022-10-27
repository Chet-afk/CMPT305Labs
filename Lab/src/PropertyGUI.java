import com.macewan305.CsvPropertyAssessmentDAO;
import com.macewan305.PropertyAssessment;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.List;

public class PropertyGUI extends Application {

    // Constant variables for relative calculations with the overall stage
    private final int WIDTH = 1400;
    private final int HEIGHT = 700;

    private ObservableList<PropertyAssessment> propData; // Observable lists can be tracked by other items for changess

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(createTableVbox());
        borderPane.setLeft(createFilterArea());

        Scene layout = new Scene(borderPane, WIDTH, HEIGHT);

        Stage stage = new Stage();



        // Configure stage
        stage.setTitle("Edmonton Property Assessments");


        stage.setScene(layout);

        stage.show();
    }

    private TableView makeTable() throws Exception {

        TableView table = new TableView<PropertyAssessment>();

        // Creating all the columns
        TableColumn<PropertyAssessment, Integer> accountNum = new TableColumn<>("Account");
        TableColumn<PropertyAssessment, String> address = new TableColumn<>("Address");
        TableColumn<PropertyAssessment, Integer> assessVal = new TableColumn<>("Assessed Value");
        TableColumn<PropertyAssessment, String> classes = new TableColumn<>("Assessment Class");
        TableColumn<PropertyAssessment, String> neighbourhood = new TableColumn<>("Neighbourhood");
        TableColumn<PropertyAssessment, String> location = new TableColumn<>("(Latitude, Longitude)");

        // Associating each column to extract respective data getters
        accountNum.setCellValueFactory( new PropertyValueFactory<>("AccountNum"));
        address.setCellValueFactory( new PropertyValueFactory<>("Address"));
        assessVal.setCellValueFactory( new PropertyValueFactory<>("AssessmentVal"));
        classes.setCellValueFactory( new PropertyValueFactory<>("AllClasses"));
        neighbourhood.setCellValueFactory( new PropertyValueFactory<>("NeighbourhoodName"));
        location.setCellValueFactory( new PropertyValueFactory<>("Location"));

        // make assessVal column be in currency format
        assessVal.setCellFactory(cell -> new CurrencyFormat());

        table.getColumns().addAll(accountNum, address, assessVal, classes, neighbourhood, location);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);   // Ensure no empty columns (i.e no titles)

        table.setPlaceholder(new Label("No data given"));

        table.setPrefSize(WIDTH, HEIGHT);   // Ensures the table always takes all space


        // Testing if data can be read
        List<PropertyAssessment> data = new CsvPropertyAssessmentDAO(Paths.get("Property_Assessment_Data_2022.csv")).getData(100);
        propData = FXCollections.observableArrayList(data);
        table.setItems(propData);

        return table;
    }

    private static class CurrencyFormat extends TableCell<PropertyAssessment, Integer> {

        private final NumberFormat currency = NumberFormat.getCurrencyInstance();

        @Override
        protected void updateItem(Integer value, boolean empty) {
            super.updateItem(value, empty);
            currency.setMaximumFractionDigits(0);
            setText(empty ? "" : currency.format(value));
        }
    }

    private VBox createTableVbox() throws Exception {

        VBox vboxFinish = new VBox();

        // Editing the spacing and such between the table and title
        vboxFinish.setPadding(new Insets(20,20,20,20));
        vboxFinish.setSpacing(30);

        // Editing the title of the Table
        Label tableName = new Label("Property Assessment Data");
        tableName.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Creating the table
        TableView infoSpread = makeTable();

        vboxFinish.getChildren().addAll(tableName, infoSpread);

        return vboxFinish;
    }

    private VBox createFilterArea() {

        VBox vboxFilter = new VBox();
        vboxFilter.setSpacing(10);
        vboxFilter.setPadding(new Insets(20,20,20,20));

        Label filterTitle = new Label("Find Property Assessment");
        filterTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label accNum = new Label ("Account Number:");
        TextField accInput = new TextField();

        Label address = new Label ("Address (#suite #house street):");
        TextField addressInput = new TextField();

        Label neigh = new Label ("Neighbourhood:");
        TextField neighInput = new TextField();

        Label assessClass = new Label ("Assessment Class:");
        ComboBox assessDropdown = new ComboBox(FXCollections.observableArrayList(
                "","RESIDENTIAL", "COMMERCIAL", "FARMLAND"
        ));
        assessDropdown.setMinSize(300, 0);

        Label valRange = new Label ("Assessed Value Range:");
        // HBox minMax = minMax();
        // HBox buttons = buttons();

        vboxFilter.getChildren().addAll(filterTitle, accNum, accInput, address, addressInput, neigh, neighInput, assessClass, valRange);

        return vboxFilter;
    }
}

