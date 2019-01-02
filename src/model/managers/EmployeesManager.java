package Model.ModelManagers;

import DataBase.DatabaseManager;
import Model.CustomExceptions.ErrorMessageException;
import Model.Employee;

import java.util.List;

public class EmployeesManager {

    public void addEmployee(String firstName, String lastName, String address, int phoneNumber, String email, String login, String password) throws ErrorMessageException {
        String salt = PasswordsManager.getSalt(30);
        String encrzptedPassword = encrypt(salt, password);
        DatabaseManager.addEmployee(firstName, lastName, address, phoneNumber, email, login, encrzptedPassword, salt);
    }

    public void editEmployee(Employee employee) throws  ErrorMessageException{
        Employee employeeBeforUpdate = getEmployeeById(employee.getUUID());
        if (employeeBeforUpdate == null)
            throw new ErrorMessageException("Employee doesn't exist");

        try{
            DatabaseManager.editEmployee(employee.getUUID(), employee.getFirstName(), employee.getLastName(), employee.getAddress(), employee.getPhoneNumber(),
                                                employee.getEmail());
        }catch (Exception e){
            DatabaseManager.editEmployee(employee.getUUID(), employeeBeforUpdate.getFirstName(), employeeBeforUpdate.getLastName(), employeeBeforUpdate.getAddress(), employeeBeforUpdate.getPhoneNumber(),
                                         employeeBeforUpdate.getEmail());
            throw e;
        }
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

    public boolean checkEmployeePassword(String login, String providedPassword) throws ErrorMessageException {
    // Encrypted and Base64 encoded password read from database
        String securePassword = DatabaseManager.getPassword(login);
    // Salt read from database
        String salt = DatabaseManager.getSalt(login);

        if (login == null)
            return false;
        if (providedPassword == null)
            return false;
        if (securePassword == null)
            return false;
        if (salt == null)
            return false;
        if (PasswordsManager.verifyUserPassword(providedPassword, securePassword, salt))
            return true;
        else
            return false;
    }
}
