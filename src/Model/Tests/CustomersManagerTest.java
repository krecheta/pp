package Model.Tests;

import DataBase.DatabaseManager;
import Model.CustomExceptions.ErrorMessageException;
import Model.Customer;
import Model.Logs;
import Model.ModelManagers.CustomersManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomersManagerTest {
    static CustomersManager customersManager;
    @AfterEach
    void closeDatabaseConnection() {
        DatabaseManager.closeAndClearConnection();
    }

    @org.junit.jupiter.api.BeforeAll
    static void setupLogger(){
        new Logs();
         customersManager = new CustomersManager();
    }

    @org.junit.jupiter.api.BeforeEach
    void setupDatabase() throws ErrorMessageException {
        DatabaseManager.connect();
    }

    @Test
    void addCustomer() throws ErrorMessageException {
        customersManager.addCustomer("Dominik", "Janiak", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress");
        List<Customer> clients = DatabaseManager.getAllActualCustomers();
        String Response = "{pesel = 95062910555, imię = Dominik, nazwisko = Janiak, numer telefonu = 151515664, adres = address, email = dominik1116@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";

        assertEquals(1, clients.size());
        assertEquals(Response, clients.get(0).toString());
        assertEquals("companyAdress",clients.get(0).getCompanyAddress());
        assertEquals("comakdk",clients.get(0).getCompanyName());
        assertEquals("9956-3565-9656",clients.get(0).getNipNumber());
    }

    @Test
    void editCustomer() throws ErrorMessageException {
        customersManager.addCustomer("Dominik", "Janiak", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress");
        Customer customer = new Customer("Dawid", "Janiak", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress",0.0);
        customersManager.editCustomer(customer);

        List<Customer> clients = DatabaseManager.getAllActualCustomers();
        String Response = "{pesel = 95062910555, imię = Dawid, nazwisko = Janiak, numer telefonu = 151515664, adres = address, email = dominik1116@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";

        assertEquals(1, clients.size());
        assertEquals(Response, clients.get(0).toString());
        assertEquals("companyAdress",clients.get(0).getCompanyAddress());
        assertEquals("comakdk",clients.get(0).getCompanyName());
        assertEquals("9956-3565-9656",clients.get(0).getNipNumber());
    }

    @Test
    void getAllCustomers() throws ErrorMessageException {
        DatabaseManager.addCustomer("Dominik", "Janiak", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress");
        DatabaseManager.addCustomer("Dawid", "Janiak", "95062910554", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress");
        DatabaseManager.addCustomer("Zbyszek", "Janiak", "95062910553", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress");
        DatabaseManager.addCustomer("Rafal", "Janiak", "95062910552", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress");
        DatabaseManager.markCustomerAsArchival("95062910555");
        DatabaseManager.markCustomerAsArchival("95062910554");
        String response4 = "{pesel = 95062910555, imię = Dominik, nazwisko = Janiak, numer telefonu = 151515664, adres = address, email = dominik1116@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";
        String response3 = "{pesel = 95062910554, imię = Dawid, nazwisko = Janiak, numer telefonu = 151515664, adres = address, email = dominik1116@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";
        String response2 = "{pesel = 95062910553, imię = Zbyszek, nazwisko = Janiak, numer telefonu = 151515664, adres = address, email = dominik1116@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";
        String response1 = "{pesel = 95062910552, imię = Rafal, nazwisko = Janiak, numer telefonu = 151515664, adres = address, email = dominik1116@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";

        List<Customer> customers = customersManager.getAllCustomers();

        assertEquals(4, customers.size());
        assertEquals(response1, customers.get(0).toString());
        assertEquals(response2, customers.get(1).toString());
        assertEquals(response3, customers.get(2).toString());
        assertEquals(response4, customers.get(3).toString());
    }

    @Test
    void getActiveCustomers() throws ErrorMessageException {
        DatabaseManager.addCustomer("Dominik", "Janiak", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress");
        DatabaseManager.addCustomer("Dawid", "Janiak", "95062910554", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress");
        DatabaseManager.addCustomer("Zbyszek", "Janiak", "95062910553", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress");
        DatabaseManager.addCustomer("Rafal", "Janiak", "95062910552", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress");
        DatabaseManager.markCustomerAsArchival("95062910555");
        DatabaseManager.markCustomerAsArchival("95062910554");
        String response1 = "{pesel = 95062910553, imię = Zbyszek, nazwisko = Janiak, numer telefonu = 151515664, adres = address, email = dominik1116@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";
        String response2 = "{pesel = 95062910552, imię = Rafal, nazwisko = Janiak, numer telefonu = 151515664, adres = address, email = dominik1116@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";

        List<Customer> customers = customersManager.getActiveCustomers();

        assertEquals(2, customers.size());
        assertEquals(response2, customers.get(0).toString());
        assertEquals(response1, customers.get(1).toString());
    }

    @Test
    void getAllArchivalCustomers() throws ErrorMessageException {
        DatabaseManager.addCustomer("Dominik", "Janiak", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress");
        DatabaseManager.addCustomer("Dawid", "Janiak", "95062910554", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress");
        DatabaseManager.addCustomer("Zbyszek", "Janiak", "95062910553", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress");
        DatabaseManager.addCustomer("Rafal", "Janiak", "95062910552", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress");
        DatabaseManager.markCustomerAsArchival("95062910555");
        DatabaseManager.markCustomerAsArchival("95062910554");
        String response1 = "{pesel = 95062910555, imię = Dominik, nazwisko = Janiak, numer telefonu = 151515664, adres = address, email = dominik1116@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";
        String response2 = "{pesel = 95062910554, imię = Dawid, nazwisko = Janiak, numer telefonu = 151515664, adres = address, email = dominik1116@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";

        List<Customer> customers = customersManager.getAllArchivalCustomers();

        assertEquals(2, customers.size());
        assertEquals(response2, customers.get(0).toString());
        assertEquals(response1, customers.get(1).toString());

    }

    @Test
    void getCustomerByPesel() throws ErrorMessageException {
        DatabaseManager.addCustomer("Dawid", "Janiak", "95062910554", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress");
        DatabaseManager.addCustomer("Dominik", "Janiak", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress");
        String response1 = "{pesel = 95062910555, imię = Dominik, nazwisko = Janiak, numer telefonu = 151515664, adres = address, email = dominik1116@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";

        Customer customer = customersManager.getCustomerByPesel("95062910555");
        assertEquals(response1, customer.toString());
    }

    @Test
    void transferCustomerToArchivalCustomers() throws ErrorMessageException {
        DatabaseManager.addCustomer("Dominik", "Janiak", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress");
        DatabaseManager.addCustomer("Dawid", "Janiak", "95062910554", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress");
        DatabaseManager.addCustomer("Zbyszek", "Janiak", "95062910553", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress");
        DatabaseManager.addCustomer("Rafal", "Janiak", "95062910552", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress");
        customersManager.transferCustomerToArchivalCustomers("95062910555");
        String response1 = "{pesel = 95062910555, imię = Dominik, nazwisko = Janiak, numer telefonu = 151515664, adres = address, email = dominik1116@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";

        List<Customer> customers = DatabaseManager.getAllArchivalCustomers();

        assertEquals(1, customers.size());
        assertEquals(response1, customers.get(0).toString());
    }
}