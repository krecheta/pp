package model.managers;

import database.DatabaseManager;
import model.exceptions.ErrorMessageException;
import model.Employee;

import java.util.List;

public class EmployeesManager {

    public void addEmployee(String firstName, String lastName, String address, int phoneNumber, String email, String login, String password, boolean managerPermission) throws ErrorMessageException {
        String salt = PasswordsManager.getSalt(30);
        String encrzptedPassword = encrypt(salt, password);
        if(DatabaseManager.getEmployeeByLogin(login).getLogin() == null)
                DatabaseManager.addEmployee(firstName, lastName, address, phoneNumber, email, login, encrzptedPassword, salt, managerPermission);
        else
            throw new ErrorMessageException("Pracownik o takim loginie już istnieje.");
    }

    public void editEmployee(Employee employee) throws  ErrorMessageException{
        Employee employeeBeforUpdate = getEmployeeById(employee.getUUID());
        if (employeeBeforUpdate == null)
            throw new ErrorMessageException("Pracownik nie istnieje");

        try{
            DatabaseManager.editEmployee(employee.getUUID(), employee.getFirstName(), employee.getLastName(), employee.getAddress(), employee.getPhoneNumber(),
                                                employee.getEmail(), employee.getLogin());
        }catch (Exception e){
            DatabaseManager.editEmployee(employee.getUUID(), employeeBeforUpdate.getFirstName(), employeeBeforUpdate.getLastName(), employeeBeforUpdate.getAddress(), employeeBeforUpdate.getPhoneNumber(),
                                         employeeBeforUpdate.getEmail(), employeeBeforUpdate.getLogin());
            throw e;
        }
    }

    public void editEmployee(Employee employee, String password) throws  ErrorMessageException{
        Employee employeeBeforUpdate = getEmployeeById(employee.getUUID());

        if (employeeBeforUpdate == null)
            throw new ErrorMessageException("Employee doesn't exist");
        String salt = PasswordsManager.getSalt(30);
        String encrzptedPassword = encrypt(salt, password);
            DatabaseManager.editEmployee(employee.getUUID(), employee.getFirstName(), employee.getLastName(), employee.getAddress(), employee.getPhoneNumber(),
                    employee.getEmail(), employee.getLogin(), encrzptedPassword, salt);
    }

    public List<Employee> getAllEmployees() throws ErrorMessageException {
        return DatabaseManager.getAllEmployees();
    }

    public List<Employee> getFilteredEmployees(String firstName, String lastName) throws ErrorMessageException {
        return DatabaseManager.getFilteredEmployees(firstName, lastName);
    }

    public Employee getEmployeeById(int id) throws ErrorMessageException {
        return DatabaseManager.getEmployeeByID(id);
    }

    public String encrypt(String salt, String password) throws ErrorMessageException {
        //in Base64
         return PasswordsManager.generateSecurePassword(password, salt);
    }

    public boolean hasLoggedEmployeeManagerPermission() throws ErrorMessageException {
        Employee loggedEmployee = DatabaseManager.getLoggedEmployee();
        return DatabaseManager.hasEmployeeManagerPermission(loggedEmployee.getUUID());
    }

    public Employee getLoggedEmployee() throws ErrorMessageException {
        return DatabaseManager.getLoggedEmployee();
    }
}
