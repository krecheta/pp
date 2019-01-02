package controller;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import com.sun.xml.internal.bind.v2.TODO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.enums.Color;
import model.enums.FuelType;
import model.enums.VehicleStatus;
import model.enums.VehicleType;
import model.exceptions.ErrorMessageException;
import model.managers.VehiclesManager;
import model.vehicles.Vehicle;

import javax.swing.plaf.synth.ColorType;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class VehiclesController implements Initializable {

    @FXML
    private TableView<Vehicle> vehiclesTableView;

    @FXML
    private TableColumn<Vehicle, String> nameColumn;

    @FXML
    private TableColumn<Vehicle, String> idColumn;

    @FXML
    private TableColumn<Vehicle, String> typeColumn;

    @FXML
    private TableColumn<Vehicle, String> statusColumn;

    @FXML
    private TableColumn<Vehicle, Double> priceColumn;

    @FXML
    private TableColumn<Vehicle, Color> colourColumn;

    @FXML
    private TableColumn<Vehicle, Integer> yearColumn;

    @FXML
    private TableColumn<Vehicle, String> mileageColumn;

    @FXML
    private TableColumn<Vehicle, String> engCapacityColumn;

    @FXML
    private TableColumn<Vehicle, String> fuelTypeColumn;

    @FXML
    private TableColumn<Vehicle, String> fuelUsageColumn;

    @FXML
    private TableColumn<Vehicle, String> numOfPersonsColumn;

    @FXML
    private MenuItem archiveVehicleMenuItem;

    @FXML
    private ChoiceBox<String> priceComboBox;

    @FXML
    private TextField minPriceFieldText;

    @FXML
    private TextField maxPriceFieldText;

    @FXML
    private ChoiceBox<String> yearComboBox;

    @FXML
    private TextField minYearFieldText;

    @FXML
    private TextField maxYearFieldText;

    @FXML
    private ChoiceBox<String> mileageComboBox;

    @FXML
    private TextField minMileageFieldText;

    @FXML
    private TextField maxMileageFieldText;

    @FXML
    private ChoiceBox<String> capacityComboBox;

    @FXML
    private TextField minCapacityFieldText;

    @FXML
    private TextField maxCapacityFieldText;

    @FXML
    private ChoiceBox<String> fuelUseComboBox;

    @FXML
    private TextField minFuelUseFieldText;

    @FXML
    private TextField maxFuelUseFieldText;

    @FXML
    private TextField nameFieldText;

    @FXML
    private TextField colourFieldText;

    @FXML
    private ComboBox<String> fuelTypeComboBox;

    @FXML
    private ComboBox<Integer> personsComboBox;

    @FXML
    private Button filterButtons;

    @FXML
    private Button clearFiltersButton;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private Button addVehicleButton;

    @FXML
    private Button generateRaportButton;

    @FXML
    private TextField vehicleIdTextField;

    @FXML
    private ComboBox<String> statusComboBox;

    private VehiclesManager vehiclesManager;
    private boolean filtered;

    ObservableList<String> comparisionChars = FXCollections.observableArrayList(
            " ",
            ">",
            ">=",
            "=",
            "<",
            "<="
    );

    ObservableList<String> possibleVehicleTypes = FXCollections.observableArrayList(
            "dowolny",
            "samochód",
            "motocykl",
            "rower"
    );

    ObservableList<String> possibleFuelTypes = FXCollections.observableArrayList(
            "dowolny",
            "benzyna",
            "diesiel",
            "elektryczny",
            "benzyna + LPG"
    );

    ObservableList<String> possibleStatus = FXCollections.observableArrayList(
            "dowolny",
            "dostępny",
            "niedostępny",
            "zarchiwizowany"
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //setting up table
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeProperty"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusProperty"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("dailyPrice"));
        colourColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("productionYear"));
        mileageColumn.setCellValueFactory(new PropertyValueFactory<>("mileageProperty"));
        engCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("engineCapacityProperty"));
        fuelTypeColumn.setCellValueFactory(new PropertyValueFactory<>("fuelTypeProperty"));
        fuelUsageColumn.setCellValueFactory(new PropertyValueFactory<>("fuelUsageProperty"));
        numOfPersonsColumn.setCellValueFactory(new PropertyValueFactory<>("numOfPersonsProperty"));

        //setting up combo boxes
        statusComboBox.setItems(possibleStatus);
        statusComboBox.setValue(possibleStatus.get(0));

        typeComboBox.setItems(possibleVehicleTypes);
        typeComboBox.setValue(possibleVehicleTypes.get(0));
        typeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue) && newValue.equals("motocykl")){
                enableMotorcycleFields();
            } else if(!newValue.equals(oldValue) && newValue.equals("rower")){
                enableBikeFields(true);
            } else {
                enableBikeFields(false);
            }
        });

        fuelTypeComboBox.setItems(possibleFuelTypes);
        fuelTypeComboBox.setValue(possibleFuelTypes.get(0));

        priceComboBox.setItems(comparisionChars);
        priceComboBox.setValue(comparisionChars.get(0));
        priceComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue) && !newValue.equals(" ")){
                maxPriceFieldText.setDisable(true);
                maxPriceFieldText.setText("");
            } else {
                maxPriceFieldText.setDisable(false);
            }
        });

        yearComboBox.setItems(comparisionChars);
        yearComboBox.setValue(comparisionChars.get(0));
        yearComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue) && !newValue.equals(" ")){
                maxYearFieldText.setDisable(true);
                maxYearFieldText.setText("");
            } else {
                maxYearFieldText.setDisable(false);
            }
        });

        mileageComboBox.setItems(comparisionChars);
        mileageComboBox.setValue(comparisionChars.get(0));
        mileageComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue) && !newValue.equals(" ")){
                maxMileageFieldText.setDisable(true);
                maxMileageFieldText.setText("");
            } else {
                maxMileageFieldText.setDisable(false);
            }
        });

        capacityComboBox.setItems(comparisionChars);
        capacityComboBox.setValue(comparisionChars.get(0));
        capacityComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue) && !newValue.equals(" ")){
                maxCapacityFieldText.setDisable(true);
                maxCapacityFieldText.setText("");
            } else {
                maxCapacityFieldText.setDisable(false);
            }
        });

        fuelUseComboBox.setItems(comparisionChars);
        fuelUseComboBox.setValue(comparisionChars.get(0));
        fuelUseComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue) && !newValue.equals(" ")){
                maxFuelUseFieldText.setDisable(true);
                maxFuelUseFieldText.setText("");
            } else {
                maxFuelUseFieldText.setDisable(false);
            }
        });
        filtered = false;
    }

    @FXML
    void addVehicleButtonOnAction(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/NewVehicleView.fxml"));
        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Dodaj pojazd");

        loader.<NewVehicleController>getController().initData(vehiclesManager, this);
        stage.show();
    }

    @FXML
    void archiveVehicleMenuItemOnAction(ActionEvent event) {
        try {
            vehiclesManager.archiveVehicle(vehiclesTableView.getSelectionModel().getSelectedItem());
            refreshTable();
        } catch (ErrorMessageException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.initStyle(StageStyle.UTILITY);
            errorAlert.setHeaderText("Wystąpił błąd");
            errorAlert.setTitle("Błąd");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
            e.printStackTrace();
        }

    }

    @FXML
    void clearFiltersButtonOnAction(ActionEvent event) {
        filtered = false;
    }

    @FXML
    void filterButtonsOnAction(ActionEvent event) {
        filtered = true;
    }

    @FXML
    void generateRaportButtonOnAction(ActionEvent event) {

    }

    public void initData(VehiclesManager vehiclesManager){
        this.vehiclesManager = vehiclesManager;
        refreshTable();
    }

    public void refreshTable(){
        if(filtered == false){
            try {
                vehiclesTableView.setItems(FXCollections.observableArrayList(vehiclesManager.getAllVehicles()));
            } catch (ErrorMessageException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.initStyle(StageStyle.UTILITY);
                errorAlert.setHeaderText("Wystąpił błąd");
                errorAlert.setTitle("Błąd");
                errorAlert.setContentText(e.getMessage());
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        } else {
            //TODO FILTROWANIE
        }
    }

    private void enableBikeFields(boolean b) {
        mileageComboBox.setDisable(b);
        mileageComboBox.setValue(comparisionChars.get(0));
        minMileageFieldText.setDisable(b);
        minMileageFieldText.setText("");
        maxMileageFieldText.setDisable(b);
        maxMileageFieldText.setText("");

        capacityComboBox.setDisable(b);
        capacityComboBox.setValue(comparisionChars.get(0));
        minCapacityFieldText.setDisable(b);
        minCapacityFieldText.setText("");
        maxCapacityFieldText.setDisable(b);
        maxCapacityFieldText.setText("");

        fuelTypeComboBox.setDisable(b);

        fuelUseComboBox.setDisable(b);
        fuelUseComboBox.setValue(comparisionChars.get(0));
        minFuelUseFieldText.setDisable(b);
        minCapacityFieldText.setText("");
        maxFuelUseFieldText.setDisable(b);
        maxCapacityFieldText.setText("");

        personsComboBox.setDisable(b);
    }

    private void enableMotorcycleFields() {
        mileageComboBox.setDisable(false);
        minMileageFieldText.setDisable(false);
        maxMileageFieldText.setDisable(false);

        capacityComboBox.setDisable(false);
        minCapacityFieldText.setDisable(false);
        maxCapacityFieldText.setDisable(false);

        fuelTypeComboBox.setDisable(true);

        fuelUseComboBox.setDisable(false);
        minFuelUseFieldText.setDisable(false);
        maxFuelUseFieldText.setDisable(false);

        personsComboBox.setDisable(true);
    }
}
