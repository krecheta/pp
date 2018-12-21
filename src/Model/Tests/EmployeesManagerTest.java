package Model.Tests;

import DataBase.DatabaseManager;
import Model.CustomExceptions.ErrorMessageException;
import Model.Employee;
import Model.Logs;
import Model.ModelManagers.CustomersManager;
import Model.ModelManagers.EmployeesManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeesManagerTest {
    static EmployeesManager employeeManager;
    @AfterEach
    void closeDatabaseConnection() {
        DatabaseManager.closeAndClearConnection();
    }

    @org.junit.jupiter.api.BeforeAll
    static void setupLogger(){
        new Logs();
        employeeManager = new EmployeesManager();
    }

    @org.junit.jupiter.api.BeforeEach
    void setupDatabase() throws ErrorMessageException {
        DatabaseManager.connect();
    }

    @Test
    void addEmployee() throws ErrorMessageException {
        employeeManager.addEmployee("Dawid", "Dawidziak", "addres",888777666, "email1","","");
        employeeManager.addEmployee("Zbyszek", "Dawidziak", "addres",888777666, "email2","","");
        String response1 = "{UUID = 1, imię = Dawid, nazwisko = Dawidziak, numer telefonu = 888777666, adres = addres, email = email1}";
        String response2 = "{UUID = 2, imię = Zbyszek, nazwisko = Dawidziak, numer telefonu = 888777666, adres = addres, email = email2}";

        List<Employee> employeeList = DatabaseManager.getAllEmployees();
        assertEquals(2, employeeList.size());
        assertEquals(response1, employeeList.get(0).toString());
        assertEquals(response2, employeeList.get(1).toString());
    }

    @Test
    void editEmployee() throws ErrorMessageException {
        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres",888777666, "email1");
        Employee employee = new Employee(1,"Dawidziak", "Dawidziak", "addres",888777666, "email1");
        String response1 = "{UUID = 1, imię = Dawidziak, nazwisko = Dawidziak, numer telefonu = 888777666, adres = addres, email = email1}";

        employeeManager.editEmployee(employee);
        List<Employee> employeeList = DatabaseManager.getAllEmployees();
        assertEquals(1, employeeList.size());
        assertEquals(response1, employeeList.get(0).toString());
    }

    @Test
    void getAllEmployees() throws ErrorMessageException {
        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres",888777666, "email1");
        DatabaseManager.addEmployee("Janek", "Dawidziak", "addres",888777666, "email1");

        List<Employee> employeeList = employeeManager.getAllEmployees();
        String response1 = "{UUID = 1, imię = Dawid, nazwisko = Dawidziak, numer telefonu = 888777666, adres = addres, email = email1}";
        String response2 = "{UUID = 2, imię = Janek, nazwisko = Dawidziak, numer telefonu = 888777666, adres = addres, email = email1}";

        assertEquals(2, employeeList.size());
        assertEquals(response1, employeeList.get(0).toString());
        assertEquals(response2, employeeList.get(1).toString());
    }

    @Test
    void getEmployeeById() throws ErrorMessageException {
        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres",888777666, "email1");
        DatabaseManager.addEmployee("Janek", "Dawidziak", "addres",888777666, "email1");
        String response1 = "{UUID = 1, imię = Dawid, nazwisko = Dawidziak, numer telefonu = 888777666, adres = addres, email = email1}";
        String response2 = "{UUID = 2, imię = Janek, nazwisko = Dawidziak, numer telefonu = 888777666, adres = addres, email = email1}";

        Employee employee1 = employeeManager.getEmployeeById(1);
        Employee employee2 = employeeManager.getEmployeeById(2);
        assertEquals(response1, employee1.toString());
        assertEquals(response2, employee2.toString());
    }
}