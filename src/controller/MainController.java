package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.managers.CustomersManager;
import model.managers.VehiclesManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TableView<?> rentsTableView;

    @FXML
    private Button newRentButton;

    @FXML
    private Button vehiclesButton;

    @FXML
    private Button customersButton;

    @FXML
    private Button employeesButton;

    @FXML
    private Button programDetailsButton;

    @FXML
    private TextField vehicleIdTextField;

    @FXML
    private TextField vehicleNameTextField;

    @FXML
    private TextField customerNameTextField;

    @FXML
    private TextField peselTextField;

    @FXML
    private DatePicker minRentDatePicker;

    @FXML
    private DatePicker maxRentDatePicker;

    @FXML
    private DatePicker minReturnDatePicker;

    @FXML
    private DatePicker maxReturnDatePicker;

    @FXML
    private TextField minPriceTextField;

    @FXML
    private TextField maxPriceTextField;

    @FXML
    private ComboBox<?> statusComboBox;

    @FXML
    private Button filterButton;

    @FXML
    private Button clearFilterButton;

    @FXML
    private ChoiceBox<?> rentDateComboBox;

    @FXML
    private ChoiceBox<?> returnDateComboBox;

    @FXML
    private ChoiceBox<?> priceComboBox;

    @FXML
    private Button reportButton;

    @FXML
    private TextField employeeTextField;

    private VehiclesManager vehiclesManager;
    private CustomersManager customersManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        vehiclesManager = new VehiclesManager();
        customersManager = new CustomersManager();
    }

    @FXML
    void clearFilterButtonOnAction(ActionEvent event) {

    }

    @FXML
    void customersButtonOnAction(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/CustomersView.fxml"));
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
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Klienci");

        loader.<CustomersController>getController().initData(customersManager);
        stage.show();
    }

    @FXML
    void employeesButtonOnAction(ActionEvent event) {

    }

    @FXML
    void endRentMenuItemOnAction(ActionEvent event) {

    }

    @FXML
    void filterButtonOnAction(ActionEvent event) {

    }

    @FXML
    void newRentButtonOnAction(ActionEvent event) {

    }

    @FXML
    void programDetailsButtonOnAction(ActionEvent event) {

    }

    @FXML
    void rentDetailsMenuItemOnAction(ActionEvent event) {

    }

    @FXML
    void reportButtonOnAction(ActionEvent event) {

    }

    @FXML
    void vehiclesButtonOnAction(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/VehiclesView.fxml"));
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
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Pojazdy");

        loader.<VehiclesController>getController().initData(vehiclesManager);
        stage.show();
    }

}
