import com.macewan305.*;
import javafx.application.Application;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class PropertyGUI extends Application {

    // Constant variables for relative calculations with the overall stage
    private final int WIDTH = 1600;
    private final int HEIGHT = 700;
    private ObservableList<PropertyAssessment> propData; // Observable lists can be tracked by other items for changes
    private ObservableList<PropertyAssessment> currData; // Observable list for second tableview
    private List<PropertyAssessment> table2List = new ArrayList<>(); //<----testing
    private TableView<PropertyAssessment> tableProp;
    private TableView<PropertyAssessment> tableProp1;
    private Scene layout;
    private VBox vboxFilter;
    private BorderPane mainWindow;

    // Buttons
    private Button search;
    private Button reset;
    private Button copy;

    // Text field labels
    private Label tableName;
    private Label tableName1;
    private Label dataTitle;
    private Label filterTitle;
    private Label accNum;
    private Label address;
    private Label neigh;
    private Label assessClass;
    private Label valRange;
    private Label accNumSearch;
    private Label radius;
    private Label pubSchoolTitle;
    private Label attractionsTitle;
    private Label playgroundTitle;

    // Textfields for input
    //private TextField accInputTab2;
    //private TextField radiusInput;
    //private TextField accInput;
    //private TextField addressInput;
    //private TextField neighInput;
    //private ComboBox assessDropdown;

    // Flag for dark mode
    private Integer flag = 0;

    private boolean isCSV = false;
    private PropertyAssessmentDAO dao = null;

    // Section for input fields to obtain data from
    private ComboBox<String> dataDropdown;
    private ComboBox<String> assessDropdown;
    private TextField accInput;
    private TextField addressInput;
    private TextField neighInput;
    private TextField min;
    private TextField max;

    // Tab 2 fields
    private TextField radiusInput;
    private TextField accInputTab2;
    private TableView publicSchoolsView;
    private ObservableList<PublicSchool> publicSchoolObservableList;
    private TableView attractionsView;
    private ObservableList<Attractions> attractionsObservableList;
    private TableView playgroundView;
    private ObservableList<Playgrounds> playgroundsObservableList;

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
        publicSchoolObservableList = FXCollections.observableArrayList();
        attractionsObservableList = FXCollections.observableArrayList();
        playgroundsObservableList = FXCollections.observableArrayList();

        currData = FXCollections.observableArrayList();

        mainWindow = new BorderPane();
        mainWindow.setCenter(createTabs());
        mainWindow.setLeft(createFilterArea());

        layout = new Scene(mainWindow, WIDTH, HEIGHT);

        Stage stage = new Stage();

        // Configure stage
        stage.setTitle("Edmonton Property Assessments");

        stage.setScene(layout);

        stage.show();
    }

    private TabPane createTabs() {

        TabPane tabs = new TabPane();
        //tabs.getStyleClass().add("floating"); //<-----could add to get rid of tab header
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
        //------------------testing just adding filtered PropertyAssessment objects with just double clicking the row
        tableProp.setRowFactory(tv -> {
                    TableRow<PropertyAssessment> row = new TableRow<>();
                    //temp list to hold double click PropertyAssessment obj
                    List<PropertyAssessment> temp = new ArrayList<>();
                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                            //System.out.println("Double click on: "+rowData.getAccountNum()); dev check
                            temp.add(row.getItem());
                            List<PropertyAssessment> tempList = PropertyAssessments.removeFilteredDuplicates(temp,table2List);
                            table2List = tempList;
                            currData = FXCollections.observableArrayList(table2List);
                            tableProp1.setItems(currData);
                        }
                    }); return row;
        });
        //------------------testing
        tableProp.setItems(propData);

        // Creating all the columns
        TableColumn<PropertyAssessment, Integer> accountNum = new TableColumn<>("Account");
        accountNum.setStyle("-FX-background-color: lightgray");
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
     * This function creates the TableView (the display of data).
     *
     */
    private void makeTable1() {

        tableProp1 = new TableView<>();
        tableProp1.setItems(currData);

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

        tableProp1.getColumns().addAll(accountNum, address, assessVal, classes, neighbourhood, location);

        tableProp1.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);   // Ensure no empty columns (i.e no titles)

        tableProp1.setPlaceholder(new Label("No data given"));

        tableProp1.setPrefSize(WIDTH, HEIGHT);   // Ensures the table always takes all space


    }

    /**
     *
     * This creates the right half of the scene (the title and the tableview)
     *
     * @return A vertical box consisting of the table title, and the tableview.
     */
    private VBox createTableVbox() {

        VBox vboxFinish = new VBox();

        // Editing the spacing and such between the table and title
        vboxFinish.setPadding(new Insets(20,20,20,20));
        vboxFinish.setSpacing(30);

        // Editing the title of the Table
        tableName = new Label("Property Assessment Data"); //<-----
        tableName.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Editing the tile of the Second Table
        tableName1 = new Label( "Export Data List");
        tableName1.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Creating the table
        makeTable();
        makeTable1();

        vboxFinish.getChildren().addAll(tableName, tableProp, tableName1, tableProp1);

        return vboxFinish;
    }

    /**
     *
     * This creates the left half of the scene (the filtering area)
     *
     * @return A vertical box that has all the filter types, and buttons
     */
    private VBox createFilterArea() {

        vboxFilter = new VBox();

        // Create the Border
        Border border = new Border( new BorderStroke(Paint.valueOf("grey"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        vboxFilter.setBorder(border);

        vboxFilter.setSpacing(10);
        vboxFilter.setPadding(new Insets(20,20,20,20));

        dataTitle = new Label("Select Data Source");
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
        filterTitle = new Label("Find Property Assessment");
        filterTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        accNum = new Label ("Account Number:");
        accInput = new TextField();

        address = new Label ("Address (#suite #house street):");
        addressInput = new TextField();

        neigh = new Label ("Neighbourhood:");
        neighInput = new TextField();

        assessClass = new Label ("Assessment Class:");
        assessDropdown = new ComboBox<>(FXCollections.observableArrayList(
                "","RESIDENTIAL", "COMMERCIAL", "FARMLAND"
        ));
        assessDropdown.getSelectionModel().selectFirst();
        assessDropdown.setMinSize(300, 0);

        valRange = new Label ("Assessed Value Range:");
        HBox minMax = minMax();
        HBox buttons = buttons();

        // Create export button
        Button export = new Button("Export");
        export.setOnAction(exportClick);
        export.setMinSize(300,0);

        // Create copy button
        Button copy = new Button("Copy");
        copy.setOnAction(copyFunction);
        copy.setMinSize(300,0);

        // Create night button
        Button 夜 = new Button("잘 자");
        夜.setOnAction(夜Function);
        夜.setMinSize(300, 0);

        Label valRange1 = new Label ("Assessed Value Range1:");

        vboxFilter.getChildren().addAll(dataTitle, dataDropdown, readData, new Separator(), filterTitle, accNum,
                accInput, address, addressInput, neigh, neighInput,
                assessClass, assessDropdown, valRange, minMax, buttons, new Separator(), export, copy, 夜);

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

        search = new Button("Search");
        reset = new Button("Reset");
        copy = new Button ("Copy");

        search.setMinSize(140,0);
        reset.setMinSize(140,0);
        copy.setMinSize(140,0);

        search.setOnAction(searchFunction);
        reset.setOnAction(clearFilters);
        copy.setOnAction(copyFunction);

        userInteraction.setSpacing(20);

        userInteraction.getChildren().addAll(search, reset); //<-----was going to add copy

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

            if (dao == null) {
                noDAO();
                return;
            }

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
     * This event handler handles all the filtered searches when copy is pressed.
     * The table view 2 is updated with the new PropertyAssessment objects
     * Calls method removeFilteredDuplicates(propData, table2List)
     * propData is filtered list of objects for tableview1
     * table2List is a list of objects copied from propData list
     *
     */
    EventHandler<ActionEvent> copyFunction = new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            List<PropertyAssessment> tempList = PropertyAssessments.removeFilteredDuplicates(propData,table2List);
            table2List = tempList;
            currData = FXCollections.observableArrayList(table2List);
            tableProp1.setItems(currData);
        }
    };

    EventHandler<ActionEvent> 夜Function = new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {
//            layout.setFill(new LinearGradient(
//                    0, 0, 1, 1, true,                      //sizing
//                    CycleMethod.NO_CYCLE,                  //cycling
//                    new Stop(0, Color.web("#81c483")),     //colors
//                    new Stop(1, Color.web("#fcc200")))
//            );
            if (flag == 0) {
                //layout.getRoot().setStyle("-fx-base:gray");
                //layout.getRoot().setStyle(String.valueOf(Color.rgb(54, 57, 63)));
                mainWindow.setBackground(new Background(new BackgroundFill(Color.rgb(54,57,63), CornerRadii.EMPTY, Insets.EMPTY)));
                vboxFilter.setBackground(new Background(new BackgroundFill(Color.rgb(47,49,54), CornerRadii.EMPTY, Insets.EMPTY)));

                List<Label> labelList = Arrays.asList(tableName, tableName1, dataTitle, filterTitle, accNum, address, neigh, assessClass,
                        valRange, accNumSearch, radius, pubSchoolTitle, attractionsTitle, playgroundTitle);
                labelNightMode(labelList);

                List<TextField> textFieldList = Arrays.asList(accInputTab2, radiusInput, accInput, addressInput, neighInput, min, max);
                textFieldNightMode(textFieldList);

                List<Button> buttonList = Arrays.asList(search, reset, copy);
                //buttonNightMode(buttonList);

                assessDropdown.setBackground(new Background(new BackgroundFill(Color.rgb(32,34,37), CornerRadii.EMPTY, Insets.EMPTY)));

                flag = 1;
            }
            else if (flag == 1){
                //layout.getRoot().setStyle("-fx-base: ivory");
                //layout.getRoot().setStyle(String.valueOf(Color.rgb(221, 221, 221)));
                mainWindow.setBackground(new Background(new BackgroundFill(Color.rgb(221,221,221), CornerRadii.EMPTY, Insets.EMPTY)));
                vboxFilter.setBackground(new Background(new BackgroundFill(Color.rgb(221,221,221), CornerRadii.EMPTY, Insets.EMPTY)));

                List<Label> labelList = Arrays.asList(tableName, tableName1, dataTitle, filterTitle, accNum, address, neigh, assessClass,
                        valRange, accNumSearch, radius, pubSchoolTitle, attractionsTitle, playgroundTitle);
                labelLightMode(labelList);

                // label and text fields in a list and set them with a for loop
                List<TextField> textFieldList = Arrays.asList(accInputTab2, radiusInput, accInput, addressInput, neighInput, min, max);
                textFieldLightMode(textFieldList);

                List<Button> buttonList = Arrays.asList(search, reset, copy);
                //buttonLightMode(buttonList);

                assessDropdown.setBackground(new Background(new BackgroundFill(Color.rgb(255,255,255), CornerRadii.EMPTY, Insets.EMPTY)));
                flag = 0;
            }
        }
    };

    private void labelNightMode(List<Label> list)
    {
        for (int i = 0; i < list.size(); i++)
        {
            list.get(i).setTextFill(Color.rgb(150,152,157));
        }
    }

    private void textFieldNightMode(List<TextField> list)
    {
        for (int i = 0; i < list.size(); i++)
        {
            list.get(i).setBackground(new Background(new BackgroundFill(Color.rgb(32,34,37), CornerRadii.EMPTY, Insets.EMPTY)));
            list.get(i).setStyle("-fx-text-fill: gray");
        }
    }

    private void labelLightMode(List<Label> list)
    {
        for (int i = 0; i < list.size(); i++)
        {
            list.get(i).setTextFill(Color.rgb(0,0,0));
        }
    }

    private void textFieldLightMode(List<TextField> list)
    {
        for (int i = 0; i < list.size(); i++)
        {
            list.get(i).setBackground(new Background(new BackgroundFill(Color.rgb(255,255,255), CornerRadii.EMPTY, Insets.EMPTY)));
            list.get(i).setStyle("-fx-text-fill: black");
        }
    }

    private void buttonNightMode(List<Button> list)
    {
        for (int i = 0; i < list.size(); i++)
        {
            list.get(i).setBackground(Background.fill(Color.rgb(32, 34, 37)));
            list.get(i).setStyle("-fx-text-fill: gray");
        }
    }

    private void buttonLightMode(List<Button> list)
    {
        for (int i = 0; i < list.size(); i++)
        {
            //testing colors
            list.get(i).setBackground(Background.fill(Color.rgb(229, 229, 229)));
            list.get(i).setStyle("-fx-text-fill: black");
        }
    }


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

    private void noDAO() {
        Alert prompt = new Alert(Alert.AlertType.INFORMATION);

        prompt.setTitle("Property Filter");
        prompt.setHeaderText(null);
        prompt.setContentText("No data source selected. Please click Read Data.");

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

    EventHandler<ActionEvent> exportClick = new EventHandler<>() {
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



        allNewInfo.getChildren().addAll(extraInfoInputFields(), new Separator(), makeTablesRow1(), makePubSchoolTable());


        return allNewInfo;
    }

    private HBox extraInfoInputFields() {

        HBox fieldsAndLabels = new HBox();

        // Account Number
        VBox accountNumberInput = new VBox();
        accountNumberInput.setSpacing(30); // This spacing MUST be 53 more than HBox inputs spacing.
        accNumSearch = new Label("Account Number: ");
        accInputTab2 = new TextField();

        // radius
        VBox radiusGet = new VBox();
        radiusGet.setSpacing(30);

        radius = new Label("Radius around property (m):");
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

    private HBox makeTablesRow1() {
        HBox tables = new HBox();
        tables.setSpacing(20);

        tables.getChildren().addAll(makePlaygroundTable(), makeAttractionsTable());

        return tables;

    }
    private VBox makePubSchoolTable() {
        VBox pubSchoolTab = new VBox();
        pubSchoolTab.setSpacing(20);

        pubSchoolTitle = new Label("Public Schools");
        pubSchoolTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        publicSchoolsView = new TableView<>();
        publicSchoolsView.setItems(publicSchoolObservableList);

        // Creating all the columns
        TableColumn<PublicSchool, String> name = new TableColumn<>("School Name");
        TableColumn<PublicSchool, String> gradeRange = new TableColumn<>("Grades");
        TableColumn<PublicSchool, String> address = new TableColumn<>("Address");
        TableColumn<PublicSchool, String> contact = new TableColumn<>("Contact Info");


        // Associating each column to extract respective data getters
        name.setCellValueFactory( new PropertyValueFactory<>("Name"));
        gradeRange.setCellValueFactory( new PropertyValueFactory<>("Grades"));
        address.setCellValueFactory( new PropertyValueFactory<>("Address"));
        contact.setCellValueFactory( new PropertyValueFactory<>("ContactInfo"));


        publicSchoolsView.getColumns().addAll(name, gradeRange, address, contact);

        publicSchoolsView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);   // Ensure no empty columns (i.e no titles)

        publicSchoolsView.setPlaceholder(new Label("No data given"));

        publicSchoolsView.setPrefSize(WIDTH, HEIGHT);   // Ensures the table always takes all space

        pubSchoolTab.getChildren().addAll(pubSchoolTitle, publicSchoolsView);

        return pubSchoolTab;

    }
    private VBox makeAttractionsTable() {
        VBox attractionsTab = new VBox();
        attractionsTab.setSpacing(20);

        attractionsTitle = new Label("Attractions");
        attractionsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        attractionsView = new TableView<>();
        attractionsView.setItems(attractionsObservableList);

        // Creating all the columns
        TableColumn<Attractions, String> name = new TableColumn<>("Attraction");
        TableColumn<Attractions, String> type = new TableColumn<>("Type");
        TableColumn<Attractions, String> address = new TableColumn<>("Address");
        TableColumn<Attractions, String> website = new TableColumn<>("Website");


        // Associating each column to extract respective data getters
        name.setCellValueFactory( new PropertyValueFactory<>("Name"));
        type.setCellValueFactory( new PropertyValueFactory<>("Type"));
        address.setCellValueFactory( new PropertyValueFactory<>("Address"));
        website.setCellValueFactory( new PropertyValueFactory<>("URL"));


        attractionsView.getColumns().addAll(name, type, address, website);

        attractionsView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);   // Ensure no empty columns (i.e no titles)

        attractionsView.setPlaceholder(new Label("No data given"));

        attractionsView.setPrefSize(WIDTH, HEIGHT);   // Ensures the table always takes all space

        attractionsTab.getChildren().addAll(attractionsTitle, attractionsView);

        return attractionsTab;

    }
    private VBox makePlaygroundTable() {
        VBox playgroundTab = new VBox();
        playgroundTab.setSpacing(20);

        playgroundTitle = new Label("Playgrounds");
        playgroundTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        playgroundView = new TableView<>();
        playgroundView.setItems(playgroundsObservableList);

        // Creating all the columns
        TableColumn<Playgrounds, String> name = new TableColumn<>("Playground Name");
        TableColumn<Playgrounds, String> address = new TableColumn<>("Address");
        TableColumn<Playgrounds, String> surface = new TableColumn<>("Surface Type");
        TableColumn<Playgrounds, String> access = new TableColumn<>("Accessibility");


        // Associating each column to extract respective data getters
        name.setCellValueFactory( new PropertyValueFactory<>("Name"));
        address.setCellValueFactory( new PropertyValueFactory<>("Address"));
        surface.setCellValueFactory( new PropertyValueFactory<>("Surface"));
        access.setCellValueFactory( new PropertyValueFactory<>("Access"));


        playgroundView.getColumns().addAll(name, address, surface, access);

        playgroundView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);   // Ensure no empty columns (i.e no titles)

        playgroundView.setPlaceholder(new Label("No data given"));

        playgroundView.setPrefSize(WIDTH, HEIGHT);   // Ensures the table always takes all space

        playgroundTab.getChildren().addAll(playgroundTitle, playgroundView);

        return playgroundTab;

    }

    private void invalidInfoTab2(String type) {
        Alert prompt = new Alert(Alert.AlertType.INFORMATION);

        prompt.setTitle("Property Filter");
        prompt.setHeaderText(null);
        prompt.setContentText(type + " must be only numbers.");

        prompt.showAndWait();
    }

    EventHandler<ActionEvent> extraInfoClick = new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {

            if (dao == null) {
                noDAO();
                return;
            }


            // Make the DAOs
            PublicSchoolsDAO publicSchools = new PublicSchoolsDAO();
            AttractionsDAO attractions = new AttractionsDAO();
            PlaygroundsDAO playgrounds = new PlaygroundsDAO();

            // Make the Lists
            List<PublicSchool> schoolFound = new ArrayList<>();
            List<Attractions> attractionsFound = new ArrayList<>();
            List<Playgrounds> playgroundsFound = new ArrayList<>();

            // Check to see if the input is empty, or if it contains non-numbers
            if(accInputTab2.getText().isEmpty() || !accInputTab2.getText().trim().matches("[0-9]+")) {

                invalidInfoTab2("Account number");
                return;
            }
            // Same as accInputTab2, but for radius input
            if(radiusInput.getText().isEmpty() || !radiusInput.getText().trim().matches("[0-9]+")) {

                invalidInfoTab2("Radius");
                return;
            }

            String accNum = accInputTab2.getText().trim();
            String radius = radiusInput.getText().trim();


            try {
                // Getting the specific property
                PropertyAssessment singleProp = dao.getAccountNum(Integer.parseInt(accNum));

                // If it's not null (i.e. not valid property), then find respective info
                if(singleProp != null) {
                    schoolFound = publicSchools.findSchools(singleProp.getLatitude(), singleProp.getLongitude(), radius);
                    attractionsFound = attractions.findAttractions(singleProp.getLatitude(), singleProp.getLongitude(), radius);
                    playgroundsFound = playgrounds.findPlaygrounds(singleProp.getLatitude(), singleProp.getLongitude(), radius);
                }

                publicSchoolObservableList.setAll(FXCollections.observableArrayList(schoolFound));
                attractionsObservableList.setAll(FXCollections.observableArrayList(attractionsFound));
                playgroundsObservableList.setAll(FXCollections.observableArrayList(playgroundsFound));

            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }




        }
    };
    EventHandler<ActionEvent> copyClick = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {

            try {
                exportInfo();

                Alert prompt = new Alert(Alert.AlertType.CONFIRMATION);

                prompt.setTitle("Copy Filter");
                prompt.setHeaderText(null);
                prompt.setContentText("Property information successfully copied.");

                prompt.showAndWait();

            } catch (IOException e) {
                Alert prompt = new Alert(Alert.AlertType.ERROR);

                prompt.setTitle("Copy Filter");
                prompt.setHeaderText(null);
                prompt.setContentText("Could not copy filtered properties.");

                prompt.showAndWait();
            }
        }
    };
}
