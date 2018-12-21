package Model.Tests;

import DataBase.DatabaseManager;
import Model.CustomEnumValues.Color;
import Model.CustomEnumValues.FuelType;
import Model.CustomEnumValues.VehicleStatus;
import Model.CustomEnumValues.VehicleType;
import Model.CustomExceptions.ErrorMessageException;
import Model.Customer;
import Model.Employee;
import Model.Logs;
import Model.ModelManagers.RentsManager;
import Model.ModelManagers.VehiclesManager;
import Model.Rent;
import Model.Vehicles.Bike;
import Model.Vehicles.Car;
import Model.Vehicles.Motorcycle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RentsManagerTest {
    static RentsManager rentsManager;
    @AfterEach
    void closeDatabaseConnection() {
        DatabaseManager.closeAndClearConnection();
    }

    @org.junit.jupiter.api.BeforeAll
    static void setupLogger(){
        new Logs();
        rentsManager = new RentsManager();
    }

    @org.junit.jupiter.api.BeforeEach
    void setupDatabase() throws ErrorMessageException {
        DatabaseManager.connect();
    }
    @Test
    void addRent() throws ErrorMessageException {
        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres",888777666, "email1");
        DatabaseManager.addCustomer("Dawid", "car", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress1");
        DatabaseManager.addCar("a", "nameCar", 1111, Color.czerwony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        DatabaseManager.addBike("b", "bike", 1111.11, Color.czerwony, 2000);
        DatabaseManager.addMotorcycle("c", "XXX1919", 1111.01, Color.czerwony, 2000, 300000, 2.5,9.9);
        Employee employee = new Employee(1,"Dawidziak", "Dawidziak", "addres",888777666, "email1");
        Customer customer = new Customer("Dawid", "Janiak", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress",0.0);
        Car car = new Car("a", VehicleStatus.avaiable, VehicleType.car,"nameCar", 1111, Color.czerwony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        Bike bike = new Bike("b",  VehicleStatus.avaiable, VehicleType.bike, "bike", 1111.11, Color.czerwony, 2000);
        Motorcycle motorcycl= new Motorcycle("c",  VehicleStatus.avaiable,VehicleType.motorcycle,"XXX1919", 1111.01, Color.czerwony, 2000, 300000, 2.5,9.9);
        String response1 = "Wypożyczenie{Samochód = {id = a, nazwa = nameCar, kolor = czerwony, rok produkcji = 2000, dostepnosc = unavaiable, cena za dzien = 1111.0, " +
                            "paliwo = diesel, pojemność silnika = 1.9, przejechane kilometry = 2999998, zużycie paliwa = 5.7, liczba osób = 5}, klient = {pesel = 95062910555, " +
                            "imię = Dawid, nazwisko = car, numer telefonu = 151515664, adres = address, email = dominik1116@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}," +
                            " koszt wypożyczenia = 2222.0, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-10-2018}";
        String response2 = "Wypożyczenie{Rower = {id = b, nazwa = bike, kolor = czerwony, rok produkcji = 2000, dostepnosc = unavaiable, cena za dzien = 1111.11}, " +
                            "klient = {pesel = 95062910555, imię = Dawid, nazwisko = car, numer telefonu = 151515664, adres = address, email = dominik1116@one.eu," +
                            " łączna zapłacona kwota za wypożyczenia = 0.0}, koszt wypożyczenia = 2222.22, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-10-2018}";
        String response3 = "Wypożyczenie{Motocykl = {id = c, nazwa = XXX1919, kolor = czerwony, rok produkcji = 2000, dostepnosc = unavaiable, cena za dzien = 1111.01," +
                            " przejechane kilometry = 300000, pojemność silnika = 2.5, zużycie paliwa = 9.9}, klient = {pesel = 95062910555," +
                            " imię = Dawid, nazwisko = car, numer telefonu = 151515664, adres = address, email = dominik1116@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}," +
                            " koszt wypożyczenia = 2222.02, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-10-2018}";


        rentsManager.addRent(car, customer, employee, "10-10-2018", "11-10-2018");
        rentsManager.addRent(bike, customer, employee, "10-10-2018", "11-10-2018");
        rentsManager.addRent(motorcycl, customer, employee, "10-10-2018", "11-10-2018");

        try {
            rentsManager.addRent(car, customer, employee, "10-10-2018", "11-10-2018");
            assertEquals(false,true);
        }catch(ErrorMessageException e){
            assertEquals(e.getMessage(),"Vehicle is inaccessible");
        }
        try {
            rentsManager.addRent(bike, customer, employee, "10-10-2018", "11-10-2018");
            assertEquals(false,true);
        }catch(ErrorMessageException e){
            assertEquals(e.getMessage(),"Vehicle is inaccessible");
        }
        try {
            rentsManager.addRent(motorcycl, customer, employee, "10-10-2018", "11-10-2018");
            assertEquals(false,true);
        }catch(ErrorMessageException e){
            assertEquals(e.getMessage(),"Vehicle is inaccessible");
        }


        List<Rent> rents = DatabaseManager.getAllDuringRents();

       assertEquals(3, rents.size());
       assertEquals(response1, rents.get(0).toString());
       assertEquals(response2, rents.get(1).toString());
       assertEquals(response3, rents.get(2).toString());
    }


    @Test
    void checkCalculatepriceForRent() throws ErrorMessageException {
        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres",888777666, "email1");
        DatabaseManager.addCustomer("Dawid", "car", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress1");
        DatabaseManager.addCar("a", "nameCar", 550.55, Color.czerwony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        DatabaseManager.addBike("b", "bike", 998.11, Color.czerwony, 2000);
        DatabaseManager.addMotorcycle("c", "XXX1919", 123.77, Color.czerwony, 2000, 300000, 2.5,9.9);
        Employee employee = new Employee(1,"Dawidziak", "Dawidziak", "addres",888777666, "email1");
        Customer customer = new Customer("Dawid", "Janiak", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress",0.0);
        Car car = new Car("a", VehicleStatus.avaiable, VehicleType.car,"nameCar", 1111, Color.czerwony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        Bike bike = new Bike("b",  VehicleStatus.avaiable, VehicleType.bike, "bike", 1111.11, Color.czerwony, 2000);
        Motorcycle motorcycl= new Motorcycle("c",  VehicleStatus.avaiable,VehicleType.motorcycle,"XXX1919", 1111.01, Color.czerwony, 2000, 300000, 2.5,9.9);


        rentsManager.addRent(car, customer, employee, "10-10-2018", "19-10-2018");
        rentsManager.addRent(bike, customer, employee, "10-10-2018", "24-10-2018");
        rentsManager.addRent(motorcycl, customer, employee, "10-10-2018", "14-10-2018");
        List<Rent> rents = DatabaseManager.getAllDuringRents();

        assertEquals(5505.5,rents.get(0).getPriceForRent());
        assertEquals(14971.65,rents.get(1).getPriceForRent());
        assertEquals(618.85,rents.get(2).getPriceForRent());
    }


    @Test
    void endRent() throws ErrorMessageException {

        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres", 888777666, "email1");
        DatabaseManager.addCustomer("Dawid", "car", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656", "companyAdress1");
        DatabaseManager.addCar("a", "nameCar", 1111, Color.czerwony, 2000, 2999998, 1.9, 5.7, FuelType.diesel, 5);
        DatabaseManager.addBike("b", "bike", 1111.11, Color.czerwony, 2000);
        DatabaseManager.addMotorcycle("c", "XXX1919", 1111.01, Color.czerwony, 2000, 200000, 2.5, 9.9);
        Employee employee = new Employee(1, "Dawidziak", "Dawidziak", "addres", 888777666, "email1");
        Customer customer = new Customer("Dawid", "Janiak", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656", "companyAdress", 0.0);
        Car car = new Car("a", VehicleStatus.avaiable, VehicleType.car, "nameCar", 1111, Color.czerwony, 2000, 2999998, 1.9, 5.7, FuelType.diesel, 5);
        Bike bike = new Bike("b", VehicleStatus.avaiable, VehicleType.bike, "bike", 1111.11, Color.czerwony, 2000);
        Motorcycle motorcycl = new Motorcycle("c", VehicleStatus.avaiable, VehicleType.motorcycle, "XXX1919", 1111.01, Color.czerwony, 2000, 300000, 2.5, 9.9);
        String response1 = "Wypożyczenie{Samochód = {id = a, nazwa = nameCar, kolor = czerwony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.0, " +
                "paliwo = diesel, pojemność silnika = 1.9, przejechane kilometry = 300000, zużycie paliwa = 5.7, liczba osób = 5}, klient = {pesel = 95062910555, " +
                "imię = Dawid, nazwisko = car, numer telefonu = 151515664, adres = address, email = dominik1116@one.eu, łączna zapłacona kwota za wypożyczenia = 88658.6}," +
                " koszt wypożyczenia = 11110.0, data wypożyczenia = 12-12-2018, planowana data zwrotu = 21-12-2018}";

        rentsManager.addRent(car, customer, employee, "12-12-2018", "21-12-2018");
        rentsManager.addRent(bike, customer, employee, "10-10-2018", "24-10-2018");
        rentsManager.addRent(motorcycl, customer, employee, "10-10-2018", "14-10-2018");
        List<Rent> rents = DatabaseManager.getAllDuringRents();

        rentsManager.endRent(rents.get(0), customer, 300000, 0);
        rentsManager.endRent(rents.get(1), customer, 300000, 0);
        rentsManager.endRent(rents.get(2), customer, 300000, 0);

        rents = DatabaseManager.getAllEndedRents();
        assertEquals(3, rents.size());
        assertEquals(response1, rents.get(0).toString());
    }


}