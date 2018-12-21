package Model.ModelManagers;

import DataBase.DatabaseManager;
import Model.CustomExceptions.ErrorMessageException;
import Model.Employee;

import java.util.List;

public class EmployeesManager {

    public void addEmployee(String firstName, String lastName, String address, int phoneNumber, String email, String login, String password) throws ErrorMessageException {
        DatabaseManager.addEmployee(firstName, lastName, address, phoneNumber, email);
    }

    public void editEmployee(Employee employee) throws  ErrorMessageException{

        Employee employeeBeforUpdate= getEmployeeById(employee.getUUID());
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

//    public List<Employee> getFilteredEmployees(String firstName, String lastName){
//        return DatabaseManager.getFilteredEmployees(firstName, lastName);
//    }

    public Employee getEmployeeById(int id) throws ErrorMessageException {
        return DatabaseManager.getEmployeeByID(id);
    }
}
