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
        employeeManager.addEmployee("Dawid", "Dawidziak", "addres",888777666, "email1","1","");
        employeeManager.addEmployee("Zbyszek", "Dawidziak", "addres",888777666, "email2","2","");
        String response1 = "{UUID = 1, imię = Dawid, nazwisko = Dawidziak, numer telefonu = 888777666, adres = addres, email = email1}";
        String response2 = "{UUID = 2, imię = Zbyszek, nazwisko = Dawidziak, numer telefonu = 888777666, adres = addres, email = email2}";

        List<Employee> employeeList = DatabaseManager.getAllEmployees();
        assertEquals(2, employeeList.size());
        assertEquals(response1, employeeList.get(0).toString());
        assertEquals(response2, employeeList.get(1).toString());
    }

    @Test
    void editEmployee() throws ErrorMessageException {
        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres",888777666, "email1","1", "", "");
        Employee employee = new Employee(1,"Dawidziak", "Dawidziak", "addres",888777666, "email1");
        String response1 = "{UUID = 1, imię = Dawidziak, nazwisko = Dawidziak, numer telefonu = 888777666, adres = addres, email = email1}";

        employeeManager.editEmployee(employee);
        List<Employee> employeeList = DatabaseManager.getAllEmployees();
        assertEquals(1, employeeList.size());
        assertEquals(response1, employeeList.get(0).toString());
    }

    @Test
    void getAllEmployees() throws ErrorMessageException {
        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres",888777666, "email1","1", "", "");
        DatabaseManager.addEmployee("Janek", "Dawidziak", "addres",888777666, "email1","2", "", "");

        List<Employee> employeeList = employeeManager.getAllEmployees();
        String response1 = "{UUID = 1, imię = Dawid, nazwisko = Dawidziak, numer telefonu = 888777666, adres = addres, email = email1}";
        String response2 = "{UUID = 2, imię = Janek, nazwisko = Dawidziak, numer telefonu = 888777666, adres = addres, email = email1}";

        assertEquals(2, employeeList.size());
        assertEquals(response1, employeeList.get(0).toString());
        assertEquals(response2, employeeList.get(1).toString());
    }

    @Test
    void getEmployeeById() throws ErrorMessageException {
        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres",888777666, "email1","1", "", "");
        DatabaseManager.addEmployee("Janek", "Dawidziak", "addres",888777666, "email1","2", "", "");
        String response1 = "{UUID = 1, imię = Dawid, nazwisko = Dawidziak, numer telefonu = 888777666, adres = addres, email = email1}";
        String response2 = "{UUID = 2, imię = Janek, nazwisko = Dawidziak, numer telefonu = 888777666, adres = addres, email = email1}";

        Employee employee1 = employeeManager.getEmployeeById(1);
        Employee employee2 = employeeManager.getEmployeeById(2);
        assertEquals(response1, employee1.toString());
        assertEquals(response2, employee2.toString());
    }

    @Test
    void getFilteredEmployees() throws ErrorMessageException {
        DatabaseManager.addEmployee("Dawid", "Dawidzin", "addres",888777666, "email1", "1", "", "");
        DatabaseManager.addEmployee("Janek", "Dawidziak", "addres",888777666, "email1", "2", "", "");
        String response1 = "{UUID = 1, imię = Dawid, nazwisko = Dawidzin, numer telefonu = 888777666, adres = addres, email = email1}";
        String response2 = "{UUID = 2, imię = Janek, nazwisko = Dawidziak, numer telefonu = 888777666, adres = addres, email = email1}";

        List<Employee> employees = employeeManager.getFilteredEmployees("Dawid", null);
        assertEquals(1, employees.size());
        assertEquals(response1, employees.get(0).toString());

        employees = employeeManager.getFilteredEmployees("Dawid", null);
        assertEquals(1, employees.size());
        assertEquals(response1, employees.get(0).toString());

        employees = employeeManager.getFilteredEmployees(null, "ak");
        assertEquals(1, employees.size());
        assertEquals(response2, employees.get(0).toString());

        employees = employeeManager.getFilteredEmployees("a", "wid");
        assertEquals(2, employees.size());

        employees = employeeManager.getFilteredEmployees(null, null);
        assertEquals(2, employees.size());

        employees = employeeManager.getFilteredEmployees("a", "widsasad");
        assertEquals(0, employees.size());
    }

    @Test
    void checkPassword() throws ErrorMessageException {
        employeeManager.addEmployee("Dawid", "Dawidzin", "addres",888777666, "email1", "login1", "abcdefghijk");
        employeeManager.addEmployee("Janek", "Dawidziak", "addres",888777666, "email1", "login2", "qwertyuiop");
        try{
            employeeManager.addEmployee("Janek", "Dawidziak", "addres",888777666, "email1", "login2", "das");
        assertEquals(false, true);
        }catch (ErrorMessageException e){
            assertEquals("Can't properly add employee, contact with administrator", e.getMessage());
        }

        assertEquals(true, employeeManager.checkEmployeePassword("login1", "abcdefghijk"));
        assertEquals(true, employeeManager.checkEmployeePassword("login2", "qwertyuiop"));
        assertEquals(false, employeeManager.checkEmployeePassword("login2", "abcdefghijk"));
        assertEquals(false, employeeManager.checkEmployeePassword("login1", "qwertyuiop"));
        assertEquals(false, employeeManager.checkEmployeePassword(null, "qwertyuiop"));
        assertEquals(false, employeeManager.checkEmployeePassword("asdad", "qwertyuiop"));
        assertEquals(false, employeeManager.checkEmployeePassword("login1", "asada"));
        assertEquals(false, employeeManager.checkEmployeePassword("login2", "asdasdsa"));
        assertEquals(false, employeeManager.checkEmployeePassword("login2", null));
        assertEquals(false, employeeManager.checkEmployeePassword("login1", null));
        assertEquals(false, employeeManager.checkEmployeePassword("login1", "aBcdefghiJk"));
        assertEquals(false, employeeManager.checkEmployeePassword("login2", "qwertyuioP"));
    }
}