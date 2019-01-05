package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Customer;
import model.exceptions.ErrorMessageException;
import model.managers.CustomersManager;
import model.managers.RentsManager;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerChoiceController implements Initializable {

    @FXML
    private TableView<Customer> customersTableView;

    @FXML
    private TableColumn<Customer, String> firstNameColum;

    @FXML
    private TableColumn<Customer, String> lastNameColumn;

    @FXML
    private TableColumn<Customer, String> peselColumn;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField peselTextField;

    @FXML
    private Button choiceButton;

    @FXML
    private Button filterButton;

    @FXML
    private Button clearFilterButton;

    private CustomersManager customersManager;
    private NewRentController rentsController;
    private boolean filtered;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filtered = false;
        choiceButton.setDisable(true);

        firstNameColum.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        peselColumn.setCellValueFactory(new PropertyValueFactory<>("peselNumber"));

        customersTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null){
                choiceButton.setDisable(true);
            } else {
                choiceButton.setDisable(false);
            }
        });
    }

    @FXML
    void choiceButtonOnAction(ActionEvent event) {
        Customer selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();
        rentsController.setCustomer(selectedCustomer);
        Stage stage = (Stage) choiceButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void clearFilterButtonOnAction(ActionEvent event) {
        filtered = false;
        refreshTable();
    }

    @FXML
    void filterButtonOnAction(ActionEvent event) {
        filtered = true;
        refreshTable();
    }

    public void initData(CustomersManager customersManager, NewRentController rentsController){
        this.customersManager = customersManager;
        this.rentsController = rentsController;
        refreshTable();
    }

    private void refreshTable(){
        try {
            if(!filtered){
                peselTextField.clear();
                firstNameTextField.clear();
                lastNameTextField.clear();
                customersTableView.setItems(FXCollections.observableArrayList(customersManager.getAllCustomers()));
            } else {
                customersTableView.setItems(FXCollections.observableArrayList(customersManager.getFilteredCustomers(
                        peselTextField.getText(), firstNameTextField.getText(), lastNameTextField.getText())));
            }
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
}
