import com.macewan305.*;
import javafx.application.Application;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

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

    // Tab 2 variables
    private TextField radiusInput;
    private TextField accInputTab2;

    /**
     *
     * Starts the GUI
     *
     * @param args: Starting the javafx application
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     *
     * This is the creation of the stage, and any scenes.
     *
     * @param primaryStage: The main stage that is shown
     */
    @Override
    public void start(Stage primaryStage) {

        propData = FXCollections.observableArrayList();

        BorderPane mainWindow = new BorderPane();
        mainWindow.setCenter(createTabs());
        mainWindow.setLeft(createFilterArea());

        Scene layout = new Scene(mainWindow, WIDTH, HEIGHT);

        Stage stage = new Stage();

        // Configure stage
        stage.setTitle("Edmonton Property Assessments");

        stage.setScene(layout);

        stage.show();
    }

    private TabPane createTabs() {

        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.valueOf("UNAVAILABLE"));

        // Creating the tabs
        Tab readAPIorCSV = new Tab("Property Data");
        Tab nearbyLocations = new Tab("Points of Interest");

        // Setting data to tabs
        readAPIorCSV.setContent(createTableVbox());
        nearbyLocations.setContent(createExtraInfo());

        tabs.getTabs().addAll(readAPIorCSV, nearbyLocations);

        return tabs;
    }

    /**
     *
     * This function creates the TableView (the display of data).
     *
     */
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

    /**
     * This makes the assessment value section of the tableview into currency display
     */
    private static class CurrencyFormat extends TableCell<PropertyAssessment, Integer> {

        private final NumberFormat currency = NumberFormat.getCurrencyInstance();

        @Override
        protected void updateItem(Integer value, boolean empty) {
            super.updateItem(value, empty);
            currency.setMaximumFractionDigits(0);
            setText(empty ? "" : currency.format(value));
        }
    }

    /**
     *
     * This creates the right half of the scene (the title and the tableview)
     *
     * @return A vertical box consisting of the tbale title, and the tableview.
     */
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


    /**
     *
     * This creates the left half of the scene (the filtering area)
     *
     * @return A vertical box that has all the filter types, and buttons
     */
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
        assessDropdown.getSelectionModel().selectFirst();
        assessDropdown.setMinSize(300, 0);

        Label valRange = new Label ("Assessed Value Range:");
        HBox minMax = minMax();
        HBox buttons = buttons();

        // Create export button
        Button export = new Button("Export");
        export.setOnAction(exportClick);
        export.setMinSize(300,0);

        vboxFilter.getChildren().addAll(dataTitle, dataDropdown, readData, new Separator(), filterTitle, accNum,
                accInput, address, addressInput, neigh, neighInput,
                assessClass, assessDropdown, valRange, minMax, buttons, new Separator(), export);

        return vboxFilter;
    }

    /**
     *
     * Creates the text fields for the assessment value filters.
     *
     * @return A horizontal box for the min and max filter inputs
     */
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

    /**
     *
     * This function creates the search and reset buttons, and gives them their event handlers.
     *
     * @return A horizontal box that displays the search and reset buttons
     */
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

    /**
     * This handler updates the dao when read data is pressed and updates the tableview based off the chosen
     * dao type
     */
    EventHandler<ActionEvent> updateData = new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {

            isCSV = dataDropdown.getValue().compareTo("CSV File") == 0; // Check to see if CSV was picked

            // Whichever option is picked, populate the table with properties
            // API Only limits to 1000 for quicker use. Searching with no filters will grab everything, may change later
            try {

                if (isCSV) {
                    try {
                        dao = new CsvPropertyAssessmentDAO(Paths.get("Property_Assessment_Data_2022.csv"));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    propData.setAll(FXCollections.observableArrayList(dao.getAll()));
                } else {
                    dao = new ApiPropertyAssessmentDAO();
                    propData.setAll(FXCollections.observableArrayList(dao.getData(1000)));
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    };

    /**
     * This event resets all the filters when reset is pressed.
     */
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

    /**
     * This event handler handles all the searches when search is pressed.
     * Each filter type is done one by one, and then all the property lists are intersected to find those in common.
     * The table view is then updated with the new values.
     *
     */
    EventHandler<ActionEvent> searchFunction = new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {

            try {

                List<List<PropertyAssessment>> allProps = new ArrayList<>();

                if (!accInput.getText().isEmpty() && accInput.getText().trim().matches("[0-9]+")) {

                    List<PropertyAssessment> singleProp = new ArrayList<>();
                    if (dao.getAccountNum(Integer.parseInt(accInput.getText().trim())) != null) {
                        singleProp.add(dao.getAccountNum(Integer.parseInt(accInput.getText().trim())));
                    }
                    allProps.add(singleProp);
                }

                if (!addressInput.getText().isEmpty()) {
                    // Cannot know if house num, suite, or street name is being passed, so much check them all
                    List<PropertyAssessment> all3Checks = new ArrayList<>();

                    all3Checks.addAll(dao.getSuite(addressInput.getText().trim()));
                    all3Checks.addAll(dao.getHouse(addressInput.getText().trim()));
                    all3Checks.addAll(dao.getStreet(addressInput.getText().trim()));

                    allProps.add(all3Checks);
                }

                if (!neighInput.getText().isEmpty()) {
                    allProps.add(dao.getNeighbourhood(neighInput.getText().trim()));
                }

                if(assessDropdown.getValue().compareTo("") != 0) {
                    allProps.add(dao.getAssessClass(assessDropdown.getValue()));
                }
                if (!min.getText().isEmpty() && min.getText().trim().matches("[0-9]+") && !max.getText().isEmpty() && max.getText().trim().matches("[0-9]+")) {
                    allProps.add(dao.getRange(Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim())));
                }

                List<PropertyAssessment> filtersProps = PropertyAssessments.intersectProperties(allProps);

                propData.setAll(FXCollections.observableArrayList(filtersProps));

                if (filtersProps.size() == 0) {
                    noInfo();
                }

            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    };

    /**
     * This function creates an alert popup that tells the user no info was found
     */
    private void noInfo() {
        Alert prompt = new Alert(Alert.AlertType.INFORMATION);

        prompt.setTitle("Property Filter");
        prompt.setHeaderText(null);
        prompt.setContentText("No properties match the specified filters");

        prompt.showAndWait();
    }

    private void exportInfo() throws IOException {

        Path filePath = Paths.get("Exported_Assessment_Data.csv");

        Files.deleteIfExists(filePath);

        Files.createFile(filePath);

        FileWriter file = new FileWriter("Exported_Assessment_Data.csv");

        // Create the headers of each column
        file.write("Account Number,Suite,House Number,Street Name,Garage,Neighbourhood ID," +
                "Neighbourhood,Ward,Assessed Value,Latitude,Longitude,Point Location,Assessment Class % 1," +
                "Assessment Class % 2,Assessment Class % 3,Assessment Class 1,Assessment Class 2,Assessment Class 3\n");


        for (PropertyAssessment property: propData) {

            // Create the basic template of each line in a csv property assessment file
            StringJoiner separators = new StringJoiner(",","","\n");

            separators.add(Integer.toString(property.getAccountNum()));
            separators.add(property.getSuite());
            separators.add(property.getHouseNum());
            separators.add(property.getStreet());
            separators.add(property.getGarage());

            separators.add(property.getNeighbourhoodID());
            separators.add(property.getNeighbourhoodName());

            separators.add(property.getWard());
            separators.add(Integer.toString(property.getAssessmentVal()));

            separators.add(property.getLatitude());
            separators.add(property.getLongitude());
            separators.add(property.getPoint());

            separators.add(property.getAssess1P());
            separators.add(property.getAssess2P());
            separators.add(property.getAssess3P());

            separators.add(property.getAssess1Name());
            separators.add(property.getAssess2Name());
            separators.add(property.getAssess3Name());

            file.write(separators.toString());


        }
        file.close();

    }

    EventHandler<ActionEvent> exportClick = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {

            try {
                exportInfo();

                Alert prompt = new Alert(Alert.AlertType.CONFIRMATION);

                prompt.setTitle("Property Filter");
                prompt.setHeaderText(null);
                prompt.setContentText("Property information successfully exported.");

                prompt.showAndWait();

            } catch (IOException e) {
                Alert prompt = new Alert(Alert.AlertType.ERROR);

                prompt.setTitle("Property Filter");
                prompt.setHeaderText(null);
                prompt.setContentText("Could not export filtered properties.");

                prompt.showAndWait();
            }

        }
    };

    private VBox createExtraInfo() {

        VBox allNewInfo = new VBox();
        Border border = new Border( new BorderStroke(Paint.valueOf("grey"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        allNewInfo.setBorder(border);

        allNewInfo.setSpacing(10);
        allNewInfo.setPadding(new Insets(20,20,20,20));



        allNewInfo.getChildren().addAll(extraInfoInputFields(), new Separator());


        return allNewInfo;
    }

    private HBox extraInfoInputFields() {

        HBox fieldsAndLabels = new HBox();

        // Account Number
        VBox accountNumberInput = new VBox();
        accountNumberInput.setSpacing(30); // This spacing MUST be 53 more than HBox inputs spacing.
        Label accNumSearch = new Label("Account Number: ");
        accInputTab2 = new TextField();


        // radius
        VBox radiusGet = new VBox();
        radiusGet.setSpacing(30);

        Label radius = new Label("Radius around property:");
        radiusInput = new TextField();

        accountNumberInput.getChildren().addAll(accNumSearch, accInputTab2);
        radiusGet.getChildren().addAll(radius, radiusInput);

        // Search Button
        Button search = new Button("Search");
        search.setOnAction(extraInfoClick);

        fieldsAndLabels.getChildren().addAll(accountNumberInput, radiusGet, search);
        fieldsAndLabels.setSpacing(60);
        fieldsAndLabels.setAlignment(Pos.CENTER);

        return fieldsAndLabels;
    }

    EventHandler<ActionEvent> extraInfoClick = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {

            PublicSchoolsDAO publicSchools = new PublicSchoolsDAO();
            try {
                PropertyAssessment singleProp = dao.getAccountNum(Integer.parseInt(accInputTab2.getText()));
                String radius = radiusInput.getText();
                List<PublicSchool> test = publicSchools.findSchools(singleProp.getLatitude(), singleProp.getLongitude(), radius);
                System.out.println(test);

            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }




        }
    };





}