package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import model.Customer;
import model.Employee;
import model.exceptions.ErrorMessageException;
import model.managers.CustomersManager;
import model.managers.RentsManager;
import model.managers.VehiclesManager;
import model.vehicles.Vehicle;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class NewRentController implements Initializable {

    @FXML
    private Button customerChoiceButton;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField peselTextField;

    @FXML
    private Button vehicleChoiceButton;

    @FXML
    private TextField vehicleNameTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField startDateTextField;

    @FXML
    private DatePicker endDateCHosoer;

    @FXML
    private TextField vehicleIdTextField;

    @FXML
    private Button createRentButton;

    private RentsManager rentsManager;
    private MainController mainController;
    private CustomersManager customersManager;
    private VehiclesManager vehiclesManager;
    private Customer selectedCustomer;
    private Vehicle selectedVehicle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
        startDateTextField.setText(currentDate.toString());

        endDateCHosoer.setConverter(
            new StringConverter<LocalDate>() {
                final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                @Override
                public String toString(LocalDate date) {
                    return (date != null) ? dateFormatter.format(date) : "";
                }

                @Override
                public LocalDate fromString(String string) {
                    return (string != null && !string.isEmpty())
                            ? LocalDate.parse(string, dateFormatter)
                            : null;
                }
            }
        );

        createRentButton.setDisable(true);
    }

    @FXML
    void createRentButtonOnAction(ActionEvent event) {
        java.sql.Date endDate = Date.valueOf(endDateCHosoer.getValue());
        java.sql.Date startDate = new java.sql.Date(System.currentTimeMillis());
        //TODO poprawić pracownika
        Employee emp = new Employee(1, "Zbigniew", "Boniek", "ul. Zagajnikowa 5\n" +
                " 91-810 Łódź", 515635787, "zibi.boniek@gmail.com", null);
        try {
            rentsManager.addRent(selectedVehicle, selectedCustomer, emp, startDate, endDate);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UTILITY);
            alert.setHeaderText("Informacja");
            alert.setTitle("Informacja");
            alert.setContentText("Wypożyczenie zrealizowane pomyślnie.");
            alert.showAndWait();
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
    void customerChoiceButtonOnAction(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/CustomerChoiceView.fxml"));
        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.initStyle(StageStyle.UTILITY);
            errorAlert.setHeaderText("Wystąpił błąd");
            errorAlert.setTitle("Błąd");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
            e.printStackTrace();
        }
        Scene scene = new Scene(parent);
        scene.getStylesheets().add(this.getClass().getResource("../view/css/modena_dark.css").toExternalForm());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Wybierz klienta");

        loader.<CustomerChoiceController>getController().initData(customersManager, this);
        stage.show();
    }

    @FXML
    void vehicleChoiceButtonOnAction(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/VehicleChoiceView.fxml"));
        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.initStyle(StageStyle.UTILITY);
            errorAlert.setHeaderText("Wystąpił błąd");
            errorAlert.setTitle("Błąd");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
            e.printStackTrace();
        }
        Scene scene = new Scene(parent);
        scene.getStylesheets().add(this.getClass().getResource("../view/css/modena_dark.css").toExternalForm());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Wybierz pojazd");

        loader.<VehicleChoiceController>getController().initData(vehiclesManager, this);
        stage.show();
    }

    public void initData(RentsManager rentsManager, MainController mainController,
                         CustomersManager customersManager, VehiclesManager vehiclesManager){
        this.rentsManager = rentsManager;
        this.mainController = mainController;
        this.customersManager = customersManager;
        this.vehiclesManager = vehiclesManager;
    }

    public void setCustomer(Customer customer){
        firstNameTextField.setText(customer.getFirstName());
        lastNameTextField.setText(customer.getLastName());
        peselTextField.setText(customer.getPeselNumber());
        selectedCustomer = customer;
        if(selectedVehicle != null){
            createRentButton.setDisable(false);
        }
    }

    public void setVehicle(Vehicle vehicle){
        vehicleNameTextField.setText(vehicle.getName());
        vehicleIdTextField.setText(vehicle.getId());
        priceTextField.setText(String.valueOf(vehicle.getDailyPrice()));
        selectedVehicle = vehicle;
        if(selectedCustomer != null){
            createRentButton.setDisable(false);
        }
    }
}
