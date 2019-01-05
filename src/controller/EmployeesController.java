package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.StageStyle;
import model.Customer;
import model.Employee;
import model.exceptions.ErrorMessageException;
import model.managers.EmployeesManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeesController implements Initializable {

    @FXML
    private TableView<Employee> employeesTableView;

    @FXML
    private TableColumn<Employee, String> firstNameColumn;

    @FXML
    private TableColumn<Employee, String> lastNameColumn;

    @FXML
    private Button addEmpButton;

    @FXML
    private TextField firstNameFilterTextField;

    @FXML
    private TextField lastNameFilterTextField;

    @FXML
    private Button filterButton;

    @FXML
    private Button clearFilterButton;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextArea addressTextArea;

    @FXML
    private TextField loginTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField repeatPasswordField;

    @FXML
    private Button editEmpButton;

    @FXML
    private Button generateReportButton;

    @FXML
    private Button clearFieldsButton;

    private EmployeesManager employeesManager;
    private boolean filtered;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filtered = false;
        editEmpButton.setDisable(true);

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        employeesTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null){
                editEmpButton.setDisable(true);
                clearFields();
            } else {
                editEmpButton.setDisable(false);
                Employee selectedEmp = employeesTableView.getSelectionModel().getSelectedItem();
                firstNameTextField.setText(selectedEmp.getFirstName());
                lastNameTextField.setText(selectedEmp.getLastName());
                addressTextArea.setText(selectedEmp.getAddress());
                phoneTextField.setText(String.valueOf(selectedEmp.getPhoneNumber()));
                emailTextField.setText(selectedEmp.getEmail());
                //loginTextField.setText(selectedEmp.getLogin());
            }
        });
    }

    @FXML
    void addEmpButtonOnAction(ActionEvent event) {
        List<String> errors;
        String name = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String address = addressTextArea.getText();
        Integer phone = null;
        String email = emailTextField.getText();
        String login = loginTextField.getText();

        errors = validateFields(name, lastName, address, email, login);
        try {
            phone = Integer.parseInt(phoneTextField.getText());
        } catch(NumberFormatException e){
            errors.add("Nieprawidłowy numer telefonu");
        }
        if(repeatPasswordField.getText().length() == 0){
            errors.add("Proszę podać hasło.");
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
                employeesManager.addEmployee(name, lastName, address, phone, email, login, passwordField.getText(), false);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UTILITY);
                alert.setHeaderText("Informacja");
                alert.setTitle("Informacja");
                alert.setContentText("Pracownik dodany pomyślnie.");
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
        refreshTable();
    }

    @FXML
    void editEmpButtonOnAction(ActionEvent event) {
        List<String> errors;
        String name = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String address = addressTextArea.getText();
        Integer phone = null;
        String email = emailTextField.getText();
        String login = loginTextField.getText();

        errors = validateFields(name, lastName, address, email, login);
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
                int id = employeesTableView.getSelectionModel().getSelectedItem().getUUID();
                Employee employee = new Employee(id, name, lastName, address, phone, email, login);
                employeesManager.editEmployee(employee);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UTILITY);
                alert.setHeaderText("Informacja");
                alert.setTitle("Informacja");
                alert.setContentText("Pracownik edytowany pomyślnie.");
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
    void generateReportButtonOnAction(ActionEvent event) {

    }

    public void initData(EmployeesManager employeesManager){
        this.employeesManager = employeesManager;
        refreshTable();
    }

    private void refreshTable(){
        clearFields();
        try{
            if(!filtered){
                employeesTableView.setItems(FXCollections.observableArrayList(employeesManager.getAllEmployees()));
            } else {
                employeesTableView.setItems(FXCollections.observableArrayList(employeesManager.getFilteredEmployees(
                        firstNameFilterTextField.getText(), lastNameFilterTextField.getText())));
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

    private void clearFields() {
        firstNameTextField.clear();
        lastNameTextField.clear();
        addressTextArea.clear();
        phoneTextField.clear();
        emailTextField.clear();
        loginTextField.clear();
        passwordField.clear();
        repeatPasswordField.clear();
        employeesTableView.getSelectionModel().select(null);
    }

    private List<String> validateFields(String name, String lastName, String address, String email, String login) {
        List<String> errors = new ArrayList<>();

        if(name.length() == 0){
            errors.add("Proszę wprowadzić imie.");
        }

        if(lastName.length() == 0){
            errors.add("Proszę wprowadzić nazwisko.");
        }

        if(address.length() == 0){
            errors.add("Proszę wprowadzić adres.");
        }

        if(email.length() == 0){
            errors.add("Proszę wprowadzić adres email.");
        }

        if(login.length() == 0){
            errors.add("Proszę wprowadzić login.");
        }

        if(!passwordField.getText().equals(repeatPasswordField.getText())){
            errors.add("Hasła się różnią.");
        }

        if(passwordField.getText().length() > 1 && passwordField.getText().length() < 5 ){
            errors.add("Hasło musi składać się z przynajmniej 5 znaków");
        }

        return errors;
    }
}
