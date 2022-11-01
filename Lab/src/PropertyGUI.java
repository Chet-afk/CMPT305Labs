import com.macewan305.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.List;

public class PropertyGUI extends Application {

    // Constant variables for relative calculations with the overall stage
    private final int WIDTH = 1400;
    private final int HEIGHT = 700;
    private ObservableList<PropertyAssessment> propData; // Observable lists can be tracked by other items for changes
    private TableView<PropertyAssessment> tableProp;

    private boolean isCSV = false;
    private PropertyAssessmentDAO dao;

    // Section for input fields to obtain data from
    private ComboBox<String> dataDropdown;
    private ComboBox<String> assessDropdown;
    private TextField accInput;
    private TextField addressInput;
    private TextField neighInput;
    private TextField min;
    private TextField max;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        propData = FXCollections.observableArrayList();

        BorderPane mainWindow = new BorderPane();
        mainWindow.setCenter(createTableVbox());
        mainWindow.setLeft(createFilterArea());

        Scene layout = new Scene(mainWindow, WIDTH, HEIGHT);

        Stage stage = new Stage();

        // Configure stage
        stage.setTitle("Edmonton Property Assessments");

        stage.setScene(layout);

        stage.show();
    }

    private void makeTable() {

        tableProp = new TableView<>();
        tableProp.setItems(propData);

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

        tableProp.getColumns().addAll(accountNum, address, assessVal, classes, neighbourhood, location);

        tableProp.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);   // Ensure no empty columns (i.e no titles)

        tableProp.setPlaceholder(new Label("No data given"));

        tableProp.setPrefSize(WIDTH, HEIGHT);   // Ensures the table always takes all space


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

    private VBox createTableVbox() {

        VBox vboxFinish = new VBox();

        // Editing the spacing and such between the table and title
        vboxFinish.setPadding(new Insets(20,20,20,20));
        vboxFinish.setSpacing(30);

        // Editing the title of the Table
        Label tableName = new Label("Property Assessment Data");
        tableName.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Creating the table
        makeTable();

        vboxFinish.getChildren().addAll(tableName, tableProp);

        return vboxFinish;
    }

    private VBox createFilterArea() {

        VBox vboxFilter = new VBox();

        // Create the Border
        Border border = new Border( new BorderStroke(Paint.valueOf("grey"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        vboxFilter.setBorder(border);

        vboxFilter.setSpacing(10);
        vboxFilter.setPadding(new Insets(20,20,20,20));

        Label dataTitle = new Label("Select Data Source");
        dataTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));


        // Section for picking data type
        dataDropdown = new ComboBox<>(FXCollections.observableArrayList(
                "CSV File", "Edmonton's Open Data Portal"
        ));
        dataDropdown.setMinSize(300, 0);
        dataDropdown.getSelectionModel().selectFirst();

        Button readData = new Button("Read Data");
        readData.setMinSize(300,0);

        readData.setOnAction(updateData);


        // Section for user filters
        Label filterTitle = new Label("Find Property Assessment");
        filterTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label accNum = new Label ("Account Number:");
        accInput = new TextField();

        Label address = new Label ("Address (#suite #house street):");
        addressInput = new TextField();

        Label neigh = new Label ("Neighbourhood:");
        neighInput = new TextField();

        Label assessClass = new Label ("Assessment Class:");
        assessDropdown = new ComboBox<>(FXCollections.observableArrayList(
                "","RESIDENTIAL", "COMMERCIAL", "FARMLAND"
        ));
        assessDropdown.setMinSize(300, 0);

        Label valRange = new Label ("Assessed Value Range:");
        HBox minMax = minMax();
        HBox buttons = buttons();

        vboxFilter.getChildren().addAll(dataTitle, dataDropdown, readData, new Separator(), filterTitle, accNum,
                accInput, address, addressInput, neigh, neighInput,
                assessClass, assessDropdown, valRange, minMax, buttons, new Separator());

        return vboxFilter;
    }

    private HBox minMax() {

        HBox range = new HBox();

        min = new TextField();
        max = new TextField();

        min.setMaxSize(140,0);
        max.setMaxSize(140,0);

        min.setPromptText("Min Value");
        max.setPromptText("Max Value");

        range.getChildren().addAll(min,max);

        range.setSpacing(20);

        return range;
    }

    private HBox buttons() {

        HBox userInteraction = new HBox();

        Button search = new Button("Search");
        Button reset = new Button("Reset");

        search.setMinSize(140,0);
        reset.setMinSize(140,0);

        search.setOnAction(searchFunction);
        reset.setOnAction(clearFilters);

        userInteraction.setSpacing(20);

        userInteraction.getChildren().addAll(search, reset);

        return userInteraction;
    }

    EventHandler<ActionEvent> updateData = new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {

            isCSV = dataDropdown.getValue().compareTo("CSV File") == 0; // Check to see if CSV was picked

            // Whichever option is picked, populate the table with properties
            // API Only limits to 1000 for quicker use. Searching with no filters will grab everything, may change later
            if (isCSV) {
                CsvPropertyAssessmentDAO data;
                try {
                    dao = new CsvPropertyAssessmentDAO(Paths.get("Property_Assessment_Data_2022.csv"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                propData.setAll(FXCollections.observableArrayList(dao.getAll()));
            }

            else {
                dao = new ApiPropertyAssessmentDAO();
                propData.setAll(FXCollections.observableArrayList(dao.getData(1000)));
            }
        }
    };

    EventHandler<ActionEvent> clearFilters = new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {

            assessDropdown.getSelectionModel().selectFirst();
            accInput.clear();
            addressInput.clear();
            neighInput.clear();
            min.clear();
            max.clear();
        }
    };

    EventHandler<ActionEvent> searchFunction = new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {

            if (!accInput.getText().isEmpty() && accInput.getText().trim().matches("[0-9]+")) {
                propData.setAll(dao.getAccountNum(Integer.parseInt(accInput.getText().trim())));
            }
        }
    };

}