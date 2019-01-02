package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.StageStyle;
import model.Customer;
import model.exceptions.ErrorMessageException;
import model.managers.CustomersManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CustomersController implements Initializable {

    @FXML
    private TableView<Customer> customersTableView;

    @FXML
    private TableColumn<Customer, String> firstNameColumn;

    @FXML
    private TableColumn<Customer, String> lastNameColumn;

    @FXML
    private TableColumn<Customer, String> peselColumn;

    @FXML
    private Button addCustomerButton;

    @FXML
    private TextField firstNameFilterTextField;

    @FXML
    private TextField lastNameFilterTextField;

    @FXML
    private TextField peselFilterTextField;

    @FXML
    private Button filterButton;

    @FXML
    private Button clearFilterButton;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField peselTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField companyNameTextField;

    @FXML
    private TextArea companyAddressTextArea;

    @FXML
    private TextArea addressTextArea;

    @FXML
    private TextField nipTextField;

    @FXML
    private Button editCustomerButton;

    @FXML
    private Button reportButton;

    @FXML
    private Button clearFieldsButton;

    private CustomersManager customersManager;
    private boolean filtered;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filtered = false;
        editCustomerButton.setDisable(true);

        //setting up table
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        peselColumn.setCellValueFactory(new PropertyValueFactory<>("peselNumber"));

        //table listener
        customersTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null){
                editCustomerButton.setDisable(true);
                clearFields();
                peselTextField.setDisable(false);
            } else {
                editCustomerButton.setDisable(false);
                Customer selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();
                firstNameTextField.setText(selectedCustomer.getFirstName());
                lastNameTextField.setText(selectedCustomer.getLastName());
                peselTextField.setText(selectedCustomer.getPeselNumber());
                addressTextArea.setText(selectedCustomer.getAddress());
                phoneTextField.setText(String.valueOf(selectedCustomer.getPhoneNumber()));
                emailTextField.setText(selectedCustomer.getEmail());
                companyNameTextField.setText(selectedCustomer.getCompanyName());
                nipTextField.setText(selectedCustomer.getNipNumber());
                companyAddressTextArea.setText(selectedCustomer.getCompanyAddress());
                peselTextField.setDisable(true);
            }
        });
    }

    @FXML
    void addCustomerButtonOnAction(ActionEvent event) {
        List<String> errors;
        String name = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String pesel = peselTextField.getText();
        String address = addressTextArea.getText();
        Integer phone = null;
        String email = emailTextField.getText();
        String companyName = companyNameTextField.getText();
        String nip = nipTextField.getText();
        String companyAddress = companyAddressTextArea.getText();

        errors = validateFields(name, lastName, pesel, address, email);
        try {
            phone = Integer.parseInt(phoneTextField.getText());
        } catch(NumberFormatException e){
            errors.add("Nieprawidłowy numer telefonu");
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
                customersManager.addCustomer(name, lastName, pesel, address, phone, email, companyName, nip, companyAddress);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UTILITY);
                alert.setHeaderText("Informacja");
                alert.setTitle("Informacja");
                alert.setContentText("Klient dodany pomyślnie.");
                alert.showAndWait();
                refreshTable();
                clearFields();

            } catch (ErrorMessageException e) {
                errorAlert.setContentText(e.getMessage());
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    @FXML
    void clearFieldsButtonOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void clearFilterButtonOnAction(ActionEvent event) {
        filtered = false;
        firstNameFilterTextField.clear();
        lastNameFilterTextField.clear();
        peselFilterTextField.clear();
        refreshTable();
    }

    @FXML
    void editCustomerButtonOnAction(ActionEvent event) {
        List<String> errors;
        String name = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String pesel = peselTextField.getText();
        String address = addressTextArea.getText();
        Integer phone = null;
        String email = emailTextField.getText();
        String companyName = companyNameTextField.getText();
        String nip = nipTextField.getText();
        String companyAddress = companyAddressTextArea.getText();

        errors = validateFields(name, lastName, pesel, address, email);
        try {
            phone = Integer.parseInt(phoneTextField.getText());
        } catch(NumberFormatException e){
            errors.add("Nieprawidłowy numer telefonu");
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
            double sumPaid = customersTableView.getSelectionModel().getSelectedItem().getSumPaidForAllRents();
            Customer customer = new Customer(name, lastName, pesel, address, phone, email, companyName, nip, companyAddress, sumPaid);
            try {
                customersManager.editCustomer(customer);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UTILITY);
                alert.setHeaderText("Informacja");
                alert.setTitle("Informacja");
                alert.setContentText("Klient edytowany pomyślnie.");
                alert.showAndWait();
                refreshTable();
                clearFields();

            } catch (ErrorMessageException e) {
                errorAlert.setContentText(e.getMessage());
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    @FXML
    void filterButtonOnAction(ActionEvent event) {
        filtered = true;
        refreshTable();
    }

    @FXML
    void reportButtonOnAction(ActionEvent event) {

    }

    public void initData(CustomersManager customersManager){
        this.customersManager = customersManager;
        refreshTable();
    }

    private void refreshTable(){
        clearFields();
        try {
            if(filtered == false){
                customersTableView.setItems(FXCollections.observableArrayList(customersManager.getAllCustomers()));
            } else {
                customersTableView.setItems(FXCollections.observableArrayList(customersManager.getFilteredCustomers(
                        peselFilterTextField.getText(), firstNameFilterTextField.getText(), lastNameFilterTextField.getText())));
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

    private void clearFields() {
        firstNameTextField.clear();
        lastNameTextField.clear();
        peselTextField.clear();
        addressTextArea.clear();
        phoneTextField.clear();
        emailTextField.clear();
        companyNameTextField.clear();
        nipTextField.clear();
        companyAddressTextArea.clear();
        customersTableView.getSelectionModel().select(null);
    }

    private List<String> validateFields(String name, String lastName, String pesel, String address, String email) {
        List<String> errors = new ArrayList<>();

        if(name.length() == 0){
            errors.add("Proszę wprowadzić imie.");
        }

        if(lastName.length() == 0){
            errors.add("Proszę wprowadzić nazwisko.");
        }

        if(pesel.length() == 0){
            errors.add("Proszę wprowadzić pesel.");
        }

        if(pesel.length() != 11){
            errors.add("Nieprawidłowy numer pesel.");
        }

        if(address.length() == 0){
            errors.add("Proszę wprowadzić adres.");
        }

        if(email.length() == 0){
            errors.add("Proszę wprowadzić adres email.");
        }
        return errors;
    }

}
