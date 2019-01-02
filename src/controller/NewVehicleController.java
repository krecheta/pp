package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;
import model.enums.Color;
import model.enums.FuelType;
import model.enums.VehicleStatus;
import model.enums.VehicleType;
import model.exceptions.ErrorMessageException;
import model.managers.VehiclesManager;
import model.vehicles.Bike;
import model.vehicles.Car;
import model.vehicles.Motorcycle;
import model.vehicles.Vehicle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NewVehicleController implements Initializable {

    @FXML
    private ComboBox<String> vehicleTypeComboBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField colourTextField;

    @FXML
    private TextField yearTextField;

    @FXML
    private TextField mileageTextField;

    @FXML
    private TextField engineCapacityTextField;

    @FXML
    private ComboBox<String> fuelTypeComboBox;

    @FXML
    private TextField fuelUsageTextField;

    @FXML
    private TextField numOfPeopleTextField;

    @FXML
    private TextField idTextField;

    @FXML
    private Button saveButton;

    private VehiclesManager vehiclesManager;
    private VehiclesController vehiclesController;

    ObservableList<String> vehicleTypes = FXCollections.observableArrayList(
            "samochód",
            "motocykl",
            "rower"
    );

    ObservableList<String> fuelTypes = FXCollections.observableArrayList(
            "benzyna",
            "diesel",
            "elektryczny",
            "benzyna + LPG"
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vehicleTypeComboBox.setItems(vehicleTypes);
        vehicleTypeComboBox.setValue(vehicleTypes.get(0));

        fuelTypeComboBox.setItems(fuelTypes);
        fuelTypeComboBox.setValue(fuelTypes.get(0));
    }

    @FXML
    void saveButtonOnAction(ActionEvent event) {
        List<String> errors = new ArrayList<>();
        String type = vehicleTypeComboBox.getValue();
        String id = idTextField.getText();
        String name = nameTextField.getText();
        double price = 0;
        String colour = colourTextField.getText();
        int prodYear = 0;
        double mileage = 0;
        double engCapacity = 0;
        double fuelUsage = 0;
        int numOfPeople = 0;
        Vehicle newVehicle;

        if(id.length() == 0){
            errors.add("Proszę wprowadzić numer rejestracyjny.");
        }

        if(name.length() == 0){
            errors.add("Proszę wprowadzić nazwę.");
        }

        try{
            price = Double.parseDouble(priceTextField.getText().replaceAll(",","."));
            if(price <= 0) {
                throw new Exception();
            }
        } catch (Exception ex){
            errors.add("Nieprawidłowa cena.");
        }

        if(colour.length() == 0){
            errors.add("Proszę podać kolor.");
        }

        try{
            prodYear = Integer.parseInt(yearTextField.getText());
            if(prodYear < 1800){
                throw new Exception();
            }
        } catch (Exception ex){
            errors.add("Nieprawidłowy rok produkcji.");
        }

        if(type.equals("samochód") || type.equals("motocykl")){
            try{
                mileage = Double.parseDouble(mileageTextField.getText().replaceAll(",","."));
                if(mileage < 0) {
                    throw new Exception();
                }
            } catch (Exception ex){
                errors.add("Nieprawidłowy przebieg.");
            }

            try{
                engCapacity = Double.parseDouble(engineCapacityTextField.getText().replaceAll(",","."));
                if(engCapacity < 0) {
                    throw new Exception();
                }
            } catch (Exception ex){
                errors.add("Nieprawidłowa pojemność silnika.");
            }

            try{
                fuelUsage = Double.parseDouble(fuelUsageTextField.getText().replaceAll(",","."));
                if(fuelUsage < 0) {
                    throw new Exception();
                }
            } catch (Exception ex){
                errors.add("Nieprawidłowe spalanie.");
            }

            if(type.equals("samochód")){
                try{
                    numOfPeople = Integer.parseInt(numOfPeopleTextField.getText().replaceAll(",","."));
                    if(numOfPeople < 0) {
                        throw new Exception();
                    }
                } catch (Exception ex){
                    errors.add("Nieprawidłowa liczba osób.");
                }

                FuelType fuelType;
                String selectedFuelType = fuelTypeComboBox.getValue();
                if(selectedFuelType.equals("diesel")) {
                    fuelType = FuelType.diesel;
                } else if(selectedFuelType.equals("benzyna")){
                    fuelType = FuelType.petrol;
                } else if(selectedFuelType.equals("elektryczny")){
                    fuelType = FuelType.electric;
                } else {
                    fuelType = FuelType.gas;
                }

                newVehicle = new Car(id, VehicleStatus.avaiable, VehicleType.car, name, price, Color.czerwony, prodYear,
                        (int) mileage, engCapacity, fuelUsage, fuelType, numOfPeople);
            } else {
                newVehicle = new Motorcycle(id, VehicleStatus.avaiable, VehicleType.car, name, price, Color.czerwony, prodYear,
                        (int) mileage, engCapacity, fuelUsage);
            }
        } else {
            newVehicle = new Bike(id, VehicleStatus.avaiable, VehicleType.car, name, price, Color.czerwony, prodYear);
        }

        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.initStyle(StageStyle.UTILITY);
        errorAlert.setHeaderText("Wystąpił błąd");
        errorAlert.setTitle("Błąd");

        if(errors.size() > 0){
            StringBuilder sb = new StringBuilder();
            errors.forEach( x -> sb.append(x).append(System.lineSeparator()) );

            errorAlert.setContentText(sb.toString());
            errorAlert.showAndWait();
        } else {
            try {

                vehiclesManager.addVehicle(newVehicle);
                clearFields();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UTILITY);
                alert.setHeaderText("Informacja");
                alert.setTitle("Informacja");
                alert.setContentText("Pojazd dodany pomyślnie.");
                alert.showAndWait();
                vehiclesController.refreshTable();

            } catch (ErrorMessageException e) {
                errorAlert.setContentText(e.getMessage());
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    private void clearFields() {
        vehicleTypeComboBox.setValue(vehicleTypes.get(0));
        nameTextField.clear();
        idTextField.clear();
        priceTextField.clear();
        yearTextField.clear();
        colourTextField.clear();
        mileageTextField.clear();
        engineCapacityTextField.clear();
        fuelTypeComboBox.setValue(fuelTypes.get(0));
        fuelUsageTextField.clear();
        numOfPeopleTextField.clear();
    }

    @FXML
    void comboBoxAction(ActionEvent event) {
        String selectedType = vehicleTypeComboBox.getValue();

        if(selectedType.equals("motocykl")){
            mileageTextField.setDisable(false);
            engineCapacityTextField.setDisable(false);
            fuelTypeComboBox.setDisable(true);
            fuelUsageTextField.setDisable(false);
            numOfPeopleTextField.setDisable(true);
        } else if(selectedType.equals("rower")){
            mileageTextField.setDisable(true);
            engineCapacityTextField.setDisable(true);
            fuelTypeComboBox.setDisable(true);
            fuelUsageTextField.setDisable(true);
            numOfPeopleTextField.setDisable(true);
        } else {
            mileageTextField.setDisable(false);
            engineCapacityTextField.setDisable(false);
            fuelTypeComboBox.setDisable(false);
            fuelUsageTextField.setDisable(false);
            numOfPeopleTextField.setDisable(false);
        }
    }

    public void initData(VehiclesManager vehiclesManager, VehiclesController vehiclesController){
        this.vehiclesManager = vehiclesManager;
        this.vehiclesController = vehiclesController;
    }
}
