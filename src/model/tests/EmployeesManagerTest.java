package model.tests;

import database.DatabaseManager;
import model.exceptions.ErrorMessageException;
import model.Employee;
import model.Logs;
import model.managers.EmployeesManager;
import model.managers.PasswordsManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeesManagerTest {
    static EmployeesManager employeeManager;

    @AfterEach
    void closeDatabaseConnection() {

        DatabaseManager.closeAndClearConnection();
    }

    @org.junit.jupiter.api.BeforeAll
    static void setupLogger() {
        new Logs();
        employeeManager = new EmployeesManager();
    }

    @org.junit.jupiter.api.BeforeEach
    void setupDatabase() throws ErrorMessageException {
        DatabaseManager.connect();
    }

    @Test
    void addEmployee() throws ErrorMessageException {
        employeeManager.addEmployee("Dawid", "Dawidziak", "addres", 888777666, "email1", "1", "", false);
        employeeManager.addEmployee("Zbyszek", "Dawidziak", "addres", 888777666, "email2", "2", "", false);
        String response1 = "{UUID = 1, imię = Dawid, nazwisko = Dawidziak, numer telefonu = 888777666, adres = addres, email = email1}";
        String response2 = "{UUID = 2, imię = Zbyszek, nazwisko = Dawidziak, numer telefonu = 888777666, adres = addres, email = email2}";

        List<Employee> employeeList = DatabaseManager.getAllEmployees();
        assertEquals(2, employeeList.size());
        assertEquals("Dawid Dawidziak", employeeList.get(0).toString());
        assertEquals("Zbyszek Dawidziak", employeeList.get(1).toString());
    }

    @Test
    void addTwoSameEmployee() throws ErrorMessageException {
        employeeManager.addEmployee("Dawid", "Dawidziak", "addres", 888777666, "email1", "1", "", false);
        try {
            employeeManager.addEmployee("Dawid", "Dawidziak", "addres", 888777666, "email1", "1", "", false);
            assertEquals(true, false);
        } catch (ErrorMessageException e) {
            assertEquals("Pracownik o takim loginie już istnieje.", e.getMessage());
        }
    }

    @Test
    void editEmployee() throws ErrorMessageException {
        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres", 888777666, "email1", "1", "", "", false);
        Employee employee = new Employee(1, "Dawidziak", "Dawidziak", "addres", 888777666, "email1", null);

        employeeManager.editEmployee(employee);
        List<Employee> employeeList = DatabaseManager.getAllEmployees();
        assertEquals(1, employeeList.size());
        assertEquals("Dawidziak Dawidziak", employeeList.get(0).toString());
    }

    @Test
    void editEmployeeWithPassword() throws ErrorMessageException {

        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres", 888777666, "email1", "login", "dominik", "", false);
        Employee employee = new Employee(1, "Dawidziak", "Dawidziak", "addres", 888777666, "email1", "login2");
        DatabaseManager.addEmployee("X", "X", "addres", 888777666, "email1", "X", "", "", false);

        employeeManager.editEmployee(employee, "password");
        List<Employee> employeeList = DatabaseManager.getAllEmployees();
        assertEquals(2, employeeList.size());
        assertEquals("Dawidziak Dawidziak", employeeList.get(0).toString());
        assertEquals("login2", employeeList.get(0).getLogin());
        PasswordsManager.loginEmployee("login2", "password");
        assertEquals("Dawidziak Dawidziak", DatabaseManager.getLoggedEmployee().toString());
    }

    @Test
    void getAllEmployees() throws ErrorMessageException {
        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres", 888777666, "email1", "1", "", "", false);
        DatabaseManager.addEmployee("Janek", "Dawidziak", "addres", 888777666, "email1", "2", "", "", false);

        List<Employee> employeeList = employeeManager.getAllEmployees();

        assertEquals(2, employeeList.size());
        assertEquals("Dawid Dawidziak", employeeList.get(0).toString());
        assertEquals("Janek Dawidziak", employeeList.get(1).toString());
    }

    @Test
    void getEmployeeById() throws ErrorMessageException {
        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres", 888777666, "email1", "1", "", "", false);
        DatabaseManager.addEmployee("Janek", "Dawidziak", "addres", 888777666, "email1", "2", "", "", false);
        String response1 = "{UUID = 1, imię = Dawid, nazwisko = Dawidziak, numer telefonu = 888777666, adres = addres, email = email1}";
        String response2 = "{UUID = 2, imię = Janek, nazwisko = Dawidziak, numer telefonu = 888777666, adres = addres, email = email1}";

        Employee employee1 = employeeManager.getEmployeeById(1);
        Employee employee2 = employeeManager.getEmployeeById(2);
        assertEquals("Dawid Dawidziak", employee1.toString());
        assertEquals("Janek Dawidziak", employee2.toString());
    }

    @Test
    void getFilteredEmployees() throws ErrorMessageException {
        DatabaseManager.addEmployee("Dawid", "Dawidzin", "addres", 888777666, "email1", "1", "", "", false);
        DatabaseManager.addEmployee("Janek", "Dawidziak", "addres", 888777666, "email1", "2", "", "", false);

        List<Employee> employees = employeeManager.getFilteredEmployees("Dawid", null);
        assertEquals(1, employees.size());
        assertEquals("Dawid Dawidzin", employees.get(0).toString());

        employees = employeeManager.getFilteredEmployees("Dawid", null);
        assertEquals(1, employees.size());
        assertEquals("Dawid Dawidzin", employees.get(0).toString());

        employees = employeeManager.getFilteredEmployees(null, "ak");
        assertEquals(1, employees.size());
        assertEquals("Janek Dawidziak", employees.get(0).toString());

        employees = employeeManager.getFilteredEmployees("a", "wid");
        assertEquals(2, employees.size());

        employees = employeeManager.getFilteredEmployees(null, null);
        assertEquals(2, employees.size());

        employees = employeeManager.getFilteredEmployees("a", "widsasad");
        assertEquals(0, employees.size());
    }

    @Test
    void login() throws ErrorMessageException {
        employeeManager.addEmployee("Dawid", "Dawidzin", "addres", 888777666, "email1", "login1", "abcdefghijk", false);
        employeeManager.addEmployee("Janek", "Dawidziak", "addres", 888777666, "email1", "login2", "qwertyuiop", false);
        try {
            employeeManager.addEmployee("Janek", "Dawidziak", "addres", 888777666, "email1", "login2", "das", false);
            assertEquals(false, true);
        } catch (ErrorMessageException e) {
            assertEquals("Pracownik o takim loginie już istnieje.", e.getMessage());
        }

        try {
            PasswordsManager.loginEmployee("login1", "abcdefghijk");
            assertEquals(true, true);
        } catch (ErrorMessageException e) {
            assertEquals(false, true);
        }


        try {
            PasswordsManager.loginEmployee("login1", "qwertyuiop");
            assertEquals(false, true);
        } catch (ErrorMessageException e) {
            assertEquals("Błędny login bądz hasło.", e.getMessage());
        }

        try {
            PasswordsManager.loginEmployee("login2", "abcdefghijk");
            assertEquals(true, true);
        } catch (ErrorMessageException e) {
            assertEquals("Błędny login bądz hasło.", e.getMessage());
        }
    }

    @Test
    void setEmployeeLogged() throws ErrorMessageException {
        employeeManager.addEmployee("Dawid", "Dawidzin", "addres", 888777666, "email1", "login1", "abcdefghijk", false);
        employeeManager.addEmployee("Dawid2", "Dawidzin2", "addres", 888777666, "email1", "login2", "abcdefghijk", false);

        PasswordsManager.loginEmployee("login1", "abcdefghijk");
        assertEquals("Dawid Dawidzin", DatabaseManager.getLoggedEmployee().toString());
    }

    @Test
    void logoutEmployee() throws ErrorMessageException {
        employeeManager.addEmployee("Dawid", "Dawidzin", "addres", 888777666, "email1", "login1", "abcdefghijk", false);
        employeeManager.addEmployee("Dawid2", "Dawidzin2", "addres", 888777666, "email1", "login2", "abcdefghijk", false);
        Employee employee = new Employee(1,"Dawid", "Dawidzin", "addres", 888777666, "email1", "login1");
        Employee employee2 = new Employee(2,"Dawid", "Dawidzin", "addres", 888777666, "email1", "login1");

        PasswordsManager.loginEmployee("login1", "abcdefghijk");
        PasswordsManager.logoutEmployee(employee);
        assertEquals(null, DatabaseManager.getLoggedEmployee());
        PasswordsManager.loginEmployee("login2", "abcdefghijk");
        assertEquals("Dawid2 Dawidzin2", DatabaseManager.getLoggedEmployee().toString());
        PasswordsManager.logoutEmployee(employee2);
    }

    @Test
    void checkLoggedEmployee() throws ErrorMessageException {
        employeeManager.addEmployee("Dawid", "Dawidzin", "addres", 888777666, "email1", "login1", "abcdefghijk", false);
        employeeManager.addEmployee("Dawid2", "Dawidzin2", "addres", 888777666, "email1", "login2", "abcdefghijk", false);
        employeeManager.addEmployee("Dawid3", "Dawidzin3", "addres", 888777666, "email1", "login3", "abcdefghijk", false);

        Employee employee = new Employee(1,"Dawid", "Dawidzin", "addres", 888777666, "email1", "login1");
        Employee employee2 = new Employee(2,"Dawid", "Dawidzin", "addres", 888777666, "email1", "login1");

        PasswordsManager.loginEmployee("login1", "abcdefghijk");
        PasswordsManager.logoutEmployee(employee);
        assertEquals(null, employeeManager.getLoggedEmployee());
        PasswordsManager.loginEmployee("login2", "abcdefghijk");
        assertEquals("Dawid2 Dawidzin2", employeeManager.getLoggedEmployee().toString());
        PasswordsManager.logoutEmployee(employee2);

        PasswordsManager.loginEmployee("login1", "abcdefghijk");
        PasswordsManager.loginEmployee("login2", "abcdefghijk");
        assertEquals("Dawid2 Dawidzin2", employeeManager.getLoggedEmployee().toString());


        PasswordsManager.loginEmployee("login3", "abcdefghijk");
        assertEquals("Dawid3 Dawidzin3", employeeManager.getLoggedEmployee().toString());

        PasswordsManager.loginEmployee("login1", "abcdefghijk");
        assertEquals("Dawid Dawidzin", employeeManager.getLoggedEmployee().toString());
    }


    @Test
    void hasLoggedEmployeeManagerPermission() throws ErrorMessageException {
        employeeManager.addEmployee("Dawid", "Dawidzin", "addres", 888777666, "email1", "login1", "abcdefghijk", false);
        employeeManager.addEmployee("Dawid2", "Dawidzin2", "addres", 888777666, "email1", "login2", "abcdefghijk", true);
        employeeManager.addEmployee("Dawid3", "Dawidzin3", "addres", 888777666, "email1", "login3", "abcdefghijk", false);
        employeeManager.addEmployee("Dawid4", "Dawidzin4", "addres", 888777666, "email1", "login4", "abcdefghijk", true);

        Employee employee = new Employee(1,"Dawid", "Dawidzin", "addres", 888777666, "email1", "login1");
        Employee employee2 = new Employee(2,"Dawid", "Dawidzin", "addres", 888777666, "email1", "login1");
        Employee employee3 = new Employee(3,"Dawid", "Dawidzin", "addres", 888777666, "email1", "login1");
        Employee employee4 = new Employee(4,"Dawid", "Dawidzin", "addres", 888777666, "email1", "login1");

        PasswordsManager.loginEmployee("login1", "abcdefghijk");
        assertEquals(false, employeeManager.hasLoggedEmployeeManagerPermission());
        PasswordsManager.logoutEmployee(employee);

        PasswordsManager.loginEmployee("login2", "abcdefghijk");
        assertEquals(true, employeeManager.hasLoggedEmployeeManagerPermission());
        PasswordsManager.logoutEmployee(employee2);

        PasswordsManager.loginEmployee("login3", "abcdefghijk");
        assertEquals(false, employeeManager.hasLoggedEmployeeManagerPermission());
        PasswordsManager.logoutEmployee(employee3);

        PasswordsManager.loginEmployee("login4", "abcdefghijk");
        assertEquals(true, employeeManager.hasLoggedEmployeeManagerPermission());
        PasswordsManager.logoutEmployee(employee4);
    }
}