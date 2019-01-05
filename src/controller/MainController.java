package controller;

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
import model.Customer;
import model.Employee;
import model.Rent;
import model.exceptions.ErrorMessageException;
import model.managers.CustomersManager;
import model.managers.EmployeesManager;
import model.managers.RentsManager;
import model.managers.VehiclesManager;
import model.vehicles.Vehicle;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TableView<Rent> rentsTableView;

    @FXML
    private TableColumn<Rent, Vehicle> vehicleColumn;

    @FXML
    private TableColumn<Rent, Customer> customerColumn;

    @FXML
    private TableColumn<Rent, Employee> employeeColumn;

    @FXML
    private TableColumn<Rent, Date> startDateColumn;

    @FXML
    private TableColumn<Rent, Date> endDateColumn;

    @FXML
    private TableColumn<Rent, String> statusColumn;

    @FXML
    private TableColumn<Rent, Double> priceColumn;

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
    private EmployeesManager employeesManager;
    private RentsManager rentsManager;
    private boolean filtered;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vehiclesManager = new VehiclesManager();
        customersManager = new CustomersManager();
        employeesManager = new EmployeesManager();
        rentsManager = new RentsManager();
        filtered = false;

        vehicleColumn.setCellValueFactory(new PropertyValueFactory<>("vehicle"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employee"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        //TODO dodać status
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        refreshTable();
    }

    @FXML
    void clearFilterButtonOnAction(ActionEvent event) {
        filtered = false;
        refreshTable();
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
        scene.getStylesheets().add(this.getClass().getResource("../view/css/modena_dark.css").toExternalForm());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Klienci");

        loader.<CustomersController>getController().initData(customersManager);
        stage.show();
    }

    @FXML
    void employeesButtonOnAction(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/EmployeesView.fxml"));
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
        stage.setTitle("Pracownicy");

        loader.<EmployeesController>getController().initData(employeesManager);
        stage.show();
    }

    @FXML
    void endRentMenuItemOnAction(ActionEvent event) {

    }

    @FXML
    void filterButtonOnAction(ActionEvent event) {
        filtered = true;
        refreshTable();
    }

    @FXML
    void newRentButtonOnAction(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/NewRentView.fxml"));
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
        stage.setTitle("Nowe wypożyczenie");

        loader.<NewRentController>getController().initData(rentsManager, this, customersManager, vehiclesManager);
        stage.show();
    }

    @FXML
    void programDetailsButtonOnAction(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ProgramInfoView.fxml"));
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
        stage.setTitle("Info");

        stage.show();
    }

    @FXML
    void rentDetailsMenuItemOnAction(ActionEvent event) {
        Rent selectedRent = rentsTableView.getSelectionModel().getSelectedItem();
        if(selectedRent != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/RentDetailsView.fxml"));
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
            stage.setTitle("Szczegóły wypożyczenia");
            loader.<RentDetailsController>getController().initData(selectedRent);

            stage.show();
        }
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
        scene.getStylesheets().add(this.getClass().getResource("../view/css/modena_dark.css").toExternalForm());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Pojazdy");

        loader.<VehiclesController>getController().initData(vehiclesManager);
        stage.show();
    }

    public void refreshTable(){
        try{
            if(!filtered){
                ObservableList<Rent> rents = FXCollections.observableArrayList(rentsManager.getAllActualRents());
                rentsTableView.setItems(rents);
            } else {
                //TODO dodać filtrowanie
            }
        } catch(ErrorMessageException e){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.initStyle(StageStyle.UTILITY);
            errorAlert.setHeaderText("Wystąpił błąd");
            errorAlert.setTitle("Błąd");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
            e.printStackTrace();
        }

    }

}
