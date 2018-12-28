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
import Model.Rent;
import Model.Vehicles.Bike;
import Model.Vehicles.Car;
import Model.Vehicles.Motorcycle;
import com.mysql.cj.xdevapi.Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DataBaseTest {

    @AfterEach
    void closeDatabaseConnection() {
       DatabaseManager.closeAndClearConnection();
    }

    @org.junit.jupiter.api.BeforeAll
    static void setupLogger(){
        new Logs();
    }

    @org.junit.jupiter.api.BeforeEach
    void setupDatabase() throws ErrorMessageException {
        DatabaseManager.connect();
    }

    //// Add to Acutal Database
    @Test
    void addOneClient_Testt() throws ErrorMessageException {
        DatabaseManager.addCustomer("Dominik", "Janiak", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress");
        List<Customer> clients = DatabaseManager.getAllActualCustomers();
        String Response = "{pesel = 95062910555, imię = Dominik, nazwisko = Janiak, numer telefonu = 151515664, adres = address, email = dominik1116@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";

        assertEquals(1, clients.size());
        assertEquals(Response, clients.get(0).toString());
        assertEquals("companyAdress",clients.get(0).getCompanyAddress());
        assertEquals("comakdk",clients.get(0).getCompanyName());
        assertEquals("9956-3565-9656",clients.get(0).getNipNumber());
    }

    @org.junit.jupiter.api.Test
    void addThreeClients__Test() throws ErrorMessageException {
        List<Customer> customers = DatabaseManager.getAllActualCustomers();
        assertEquals(0, customers.size());
        DatabaseManager.addCustomer("Dominik", "Janiak", "95062910555", "address", 1111111, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress1");
        DatabaseManager.addCustomer("Dawid", "Y", "95062910554", "address", 222222, "y@one.eu", "comakdk", "9956-3565-9654","companyAdress2");
        DatabaseManager.addCustomer("X", "X", "95062910553", "address", 151533331, "x@one.eu", "comakdk", "9956-3565-9653","companyAdress3");

        customers = DatabaseManager.getAllActualCustomers();
        String Response0 = "{pesel = 95062910555, imię = Dominik, nazwisko = Janiak, numer telefonu = 1111111, adres = address, email = dominik1116@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";
        String Response1 = "{pesel = 95062910554, imię = Dawid, nazwisko = Y, numer telefonu = 222222, adres = address, email = y@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";
        String Response2 = "{pesel = 95062910553, imię = X, nazwisko = X, numer telefonu = 151533331, adres = address, email = x@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";

        assertEquals(3, customers.size());
        assertEquals(Response2, customers.get(0).toString());
        assertEquals(Response1, customers.get(1).toString());
        assertEquals(Response0, customers.get(2).toString());
    }

    @org.junit.jupiter.api.Test
    void updateClientSumPaid_Test() throws ErrorMessageException {
        DatabaseManager.addCustomer("Dominik", "Janiak", "95062910555", "address", 1111111, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress1");
        Customer customer = DatabaseManager.getCustomerByPesel("95062910555");
        assertEquals(0, customer.getSumPaidForAllRents());

        DatabaseManager.updateCustomerSumPaid("95062910555", 100);
        customer = DatabaseManager.getCustomerByPesel("95062910555");
        assertEquals(100, customer.getSumPaidForAllRents());

        DatabaseManager.updateCustomerSumPaid("95062910555", 500);
        customer = DatabaseManager.getCustomerByPesel("95062910555");
        assertEquals(500, customer.getSumPaidForAllRents());

        DatabaseManager.updateCustomerSumPaid("95062910555", 720.97);
        customer = DatabaseManager.getCustomerByPesel("95062910555");
        assertEquals(720.97, customer.getSumPaidForAllRents());

        DatabaseManager.updateCustomerSumPaid("95062910555", 7920.910);
        customer = DatabaseManager.getCustomerByPesel("95062910555");
        assertEquals(7920.910, customer.getSumPaidForAllRents());
    }

    @org.junit.jupiter.api.Test
    void updateCientData_Test() throws ErrorMessageException {
        DatabaseManager.addCustomer("Dominik", "Janiak", "95062910555", "address", 1111111, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress1");
        DatabaseManager.addCustomer("Dawid", "Y", "95062910554", "address", 222222, "y@one.eu", "comakdk", "9956-3565-9654","companyAdress2");
        DatabaseManager.addCustomer("X", "X", "95062910553", "address", 151533331, "x@one.eu", "comakdk", "9956-3565-9653","companyAdress3");

        DatabaseManager.editCustomer("zz", "zz", "95062910555", "address1", 1111111, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress1");
        DatabaseManager.editCustomer("a", "aa", "95062910554", "address2", 222222, "y@one.eu", "comakdk", "9956-3565-9654","companyAdress2");
        DatabaseManager.editCustomer("b", "bb", "95062910553", "address3", 151533331, "x@one.eu", "comakdk", "9956-3565-9653","companyAdress3");

        List<Customer> customers = DatabaseManager.getAllActualCustomers();
        String Response0 = "{pesel = 95062910555, imię = zz, nazwisko = zz, numer telefonu = 1111111, adres = address1, email = dominik1116@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";
        String Response1 = "{pesel = 95062910554, imię = a, nazwisko = aa, numer telefonu = 222222, adres = address2, email = y@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";
        String Response2 = "{pesel = 95062910553, imię = b, nazwisko = bb, numer telefonu = 151533331, adres = address3, email = x@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";

        assertEquals(3, customers.size());
        assertEquals(Response2, customers.get(0).toString());
        assertEquals(Response1, customers.get(1).toString());
        assertEquals(Response0, customers.get(2).toString());
    }

    @Test
    void getClientByPesel__Test() throws ErrorMessageException {
        DatabaseManager.addCustomer("zz", "zz", "95062910555", "address1", 1111111, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress1");
        DatabaseManager.addCustomer("a", "aa", "95062910554", "address2", 222222, "y@one.eu", "comakdk", "9956-3565-9654","companyAdress2");
        DatabaseManager.addCustomer("b", "bb", "95062910553", "address3", 151533331, "x@one.eu", "comakdk", "9956-3565-9653","companyAdress3");

        String Response0 = "{pesel = 95062910555, imię = zz, nazwisko = zz, numer telefonu = 1111111, adres = address1, email = dominik1116@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";
        String Response1 = "{pesel = 95062910554, imię = a, nazwisko = aa, numer telefonu = 222222, adres = address2, email = y@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";
        String Response2 = "{pesel = 95062910553, imię = b, nazwisko = bb, numer telefonu = 151533331, adres = address3, email = x@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";

        Customer customer = DatabaseManager.getCustomerByPesel("95062910555");
        assertEquals(Response0, customer.toString());

        customer = DatabaseManager.getCustomerByPesel("95062910554");
        assertEquals(Response1, customer.toString());

        customer = DatabaseManager.getCustomerByPesel("95062910553");
        assertEquals(Response2, customer.toString());
    }

    @Test
    void markCustomerAsArchival__Test() throws ErrorMessageException {
        String Response0 = "{pesel = 95062910555, imię = zz, nazwisko = zz, numer telefonu = 1111111, adres = address1, email = dominik1116@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";
        String Response1 = "{pesel = 95062910554, imię = a, nazwisko = aa, numer telefonu = 222222, adres = address2, email = y@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";
        String Response2 = "{pesel = 95062910553, imię = b, nazwisko = bb, numer telefonu = 151533331, adres = address3, email = x@one.eu, łączna zapłacona kwota za wypożyczenia = 0.0}";

        List<Customer> customers = DatabaseManager.getAllActualCustomers();
        assertEquals(0, customers.size());

        DatabaseManager.addCustomer("zz", "zz", "95062910555", "address1", 1111111, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress1");
        DatabaseManager.addCustomer("a", "aa", "95062910554", "address2", 222222, "y@one.eu", "comakdk", "9956-3565-9654","companyAdress2");
        DatabaseManager.addCustomer("b", "bb", "95062910553", "address3", 151533331, "x@one.eu", "comakdk", "9956-3565-9653","companyAdress3");

        customers = DatabaseManager.getAllArchivalCustomers();
        assertEquals(0, customers.size());

        DatabaseManager.markCustomerAsArchival("95062910555");
        customers = DatabaseManager.getAllArchivalCustomers();
        assertEquals(1, customers.size());

        customers = DatabaseManager.getAllActualCustomers();
        assertEquals(2, customers.size());

        DatabaseManager.markCustomerAsArchival("95062910554");
        DatabaseManager.markCustomerAsArchival("95062910553");
        customers = DatabaseManager.getAllArchivalCustomers();
        assertEquals(3, customers.size());
        assertEquals(Response2, customers.get(0).toString());
        assertEquals(Response1, customers.get(1).toString());
        assertEquals(Response0, customers.get(2).toString());

        customers = DatabaseManager.getAllActualCustomers();
        assertEquals(0, customers.size());
    }

    @org.junit.jupiter.api.Test
    void getCustomerDuringRents() throws ErrorMessageException {
        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres",888777666, "email", "1", "", "");
        DatabaseManager.addCustomer("zz", "zz", "95062910555", "address1", 1111111, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress1");
        DatabaseManager.addCustomer("zz", "zz", "95062910554", "address1", 1111111, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress1");
        DatabaseManager.addCar("AAA", "nameCar", 1111, Color.czerwony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        DatabaseManager.addCar("BBB", "nameCar", 222.98, Color.zielony, 2003, 2999998, 3.0, 9.7, FuelType.petrol,5);
        DatabaseManager.addCar("CCC", "nameCar", 198.29, Color.czarny, 2004, 2999998, 1.6, 7.7, FuelType.gas,5);

        List<Rent> rents = DatabaseManager.getCustomerDuringRents("95062910555");
        assertEquals(0, rents.size());

        DatabaseManager.addRent("95062910555", "AAA", VehicleType.car, 40, new java.sql.Date(10-10-2018),  new Date(11-11-2018), 1);

        rents = DatabaseManager.getCustomerDuringRents("95062910555");
        assertEquals(1, rents.size());

        DatabaseManager.addRent("95062910555", "BBB", VehicleType.car, 40, new java.sql.Date(10-10-2018), new Date(11-11-2018), 1);
        DatabaseManager.addRent("95062910555", "CCC", VehicleType.car, 40, new java.sql.Date(10-10-2018),  new Date(11-11-2018), 1);

        rents = DatabaseManager.getCustomerDuringRents("95062910555");
        assertEquals(3, rents.size());
        String response_1 = rents.get(0).toString();
        String response_2 = rents.get(1).toString();
        String response_3 = rents.get(2).toString();

        DatabaseManager.addRent("95062910554", "BBB", VehicleType.car, 40, new java.sql.Date(10-10-2018),  new Date(11-11-2018), 1);
        DatabaseManager.addRent("95062910554", "BBB", VehicleType.car, 40, new java.sql.Date(10-10-2018),  new Date(11-11-2018), 1);
        rents = DatabaseManager.getCustomerDuringRents("95062910555");
        assertEquals(3, rents.size());
        assertEquals(response_1, rents.get(0).toString());
        assertEquals(response_2, rents.get(1).toString());
        assertEquals(response_3, rents.get(2).toString());
    }

    @Test
    void getCustomerEndedents_Test() throws ErrorMessageException {
        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres",888777666, "email", "1", "", "");
        DatabaseManager.addCustomer("zz", "zz", "95062910555", "address1", 1111111, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress1");
        DatabaseManager.addCustomer("zz", "zz", "95062910554", "address1", 1111111, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress1");
        DatabaseManager.addCar("AAA", "nameCar", 1111, Color.czerwony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        DatabaseManager.addCar("BBB", "nameCar", 222.98, Color.zielony, 2003, 2999998, 3.0, 9.7, FuelType.petrol,5);
        DatabaseManager.addCar("CCC", "nameCar", 198.29, Color.czarny, 2004, 2999998, 1.6, 7.7, FuelType.gas,5);

        List<Rent> rents = DatabaseManager.getCustomerEndedRents("95062910555");
        assertEquals(0, rents.size());

        DatabaseManager.addRent("95062910555", "AAA", VehicleType.car, 40, new java.sql.Date(10-10-2018), new Date(11-11-2018), 1);
        DatabaseManager.markRentAsArchival(1);
        rents = DatabaseManager.getCustomerEndedRents("95062910555");
        assertEquals(1, rents.size());

        DatabaseManager.addRent("95062910555", "BBB", VehicleType.car, 40, new java.sql.Date(10-10-2018), new Date(11-11-2018), 1);
        DatabaseManager.addRent("95062910555", "CCC", VehicleType.car, 40, new java.sql.Date(10-10-2018), new Date(11-11-2018), 1);
        DatabaseManager.markRentAsArchival(2);
        DatabaseManager.markRentAsArchival(3);

        rents = DatabaseManager.getCustomerEndedRents("95062910555");
        assertEquals(3, rents.size());
        String response_1 = rents.get(0).toString();
        String response_2 = rents.get(1).toString();
        String response_3 = rents.get(2).toString();

        DatabaseManager.addRent("95062910554", "BBB", VehicleType.car, 40, new java.sql.Date(10-10-2018), new Date(11-11-2018), 1);
        DatabaseManager.addRent("95062910554", "CCC", VehicleType.car, 40, new java.sql.Date(10-10-2018), new Date(11-11-2018), 1);
        rents = DatabaseManager.getCustomerEndedRents("95062910555");
        DatabaseManager.markRentAsArchival(4);
        DatabaseManager.markRentAsArchival(5);

        assertEquals(3, rents.size());
        assertEquals(response_1, rents.get(0).toString());
        assertEquals(response_2, rents.get(1).toString());
        assertEquals(response_3, rents.get(2).toString());
    }


    @org.junit.jupiter.api.Test
    void updateRentPrice_Test() throws ErrorMessageException {
        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres",888777666, "email", "1", "", "");
        DatabaseManager.addCustomer("zz", "zz", "95062910555", "address1", 1111111, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress1");
        DatabaseManager.addCar("AAA", "nameCar", 1111, Color.czerwony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);

        DatabaseManager.addRent("95062910555", "AAA", VehicleType.car, 40, new java.sql.Date(10-10-2018), new Date(11-11-2018), 1);

        Rent rent = DatabaseManager.getRentByRentID(1);
        assertEquals(40, rent.getPriceForRent());
        assertEquals("AAA", rent.getVehicle().getId());

        DatabaseManager.updateRentPrice(1,100);
        rent = DatabaseManager.getRentByRentID(1);
        assertEquals(100, rent.getPriceForRent());

        DatabaseManager.updateRentPrice(1,300);
        rent = DatabaseManager.getRentByRentID(1);
        assertEquals(300, rent.getPriceForRent());
    }


    @org.junit.jupiter.api.Test
    void addCarTest() throws ErrorMessageException {
        DatabaseManager.addCar("AAA", "nameCar", 1111, Color.czerwony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        String response = "{id = AAA, nazwa = nameCar, kolor = czerwony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.0, paliwo = diesel, pojemność silnika = 1.9, przejechane kilometry = 2999998, zużycie paliwa = 5.7, liczba osób = 5}";
        Car car = DatabaseManager.getCarByID("AAA");
        assertEquals(response, car.toString());

        DatabaseManager.addCar("CCC", "nameCar", 1111, Color.czerwony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        car = DatabaseManager.getCarByID("AAA");
        assertEquals(response, car.toString());

        DatabaseManager.addCar("BBB", "nameCar", 1111, Color.czerwony, 3000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        car = DatabaseManager.getCarByID("BBB");
        response = "{id = BBB, nazwa = nameCar, kolor = czerwony, rok produkcji = 3000, dostepnosc = avaiable, cena za dzien = 1111.0, paliwo = diesel, pojemność silnika = 1.9, przejechane kilometry = 2999998, zużycie paliwa = 5.7, liczba osób = 5}";
        assertEquals(response, car.toString());
    }

    @org.junit.jupiter.api.Test
    void setCarAsArchival_Test() throws ErrorMessageException {
        DatabaseManager.addCar("BBB", "nameCar", 1111, Color.czerwony, 3000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        String response = "{id = BBB, nazwa = nameCar, kolor = czerwony, rok produkcji = 3000, dostepnosc = archived, cena za dzien = 1111.0, paliwo = diesel, pojemność silnika = 1.9, przejechane kilometry = 2999998, zużycie paliwa = 5.7, liczba osób = 5}";
        DatabaseManager.markVehicleAsArchival("BBB", VehicleType.car);
        List<Car> cars = DatabaseManager.getAllAArchivalCars();
        assertEquals(response, cars.get(0).toString());
        assertEquals(1,cars.size());

        DatabaseManager.addCar("AAA", "nameCar", 1111, Color.czerwony, 3000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        cars = DatabaseManager.getAllAArchivalCars();
        assertEquals(response, cars.get(0).toString());
        assertEquals(1, cars.size());
        cars = DatabaseManager.getAllAvaiableCars();
        assertEquals(1,cars.size());
    }

    @org.junit.jupiter.api.Test
    void setCarInacesible_Test() throws ErrorMessageException {
        DatabaseManager.addCar("BBB", "nameCar", 1111, Color.czerwony, 3000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        String response = "{id = BBB, nazwa = nameCar, kolor = czerwony, rok produkcji = 3000, dostepnosc = unavaiable, cena za dzien = 1111.0, paliwo = diesel, pojemność silnika = 1.9, przejechane kilometry = 2999998, zużycie paliwa = 5.7, liczba osób = 5}";
        DatabaseManager.setVehicleUnavaiable("BBB", VehicleType.car);
        List<Car> cars = DatabaseManager.getAllAvaiableCars();
        assertEquals(0,cars.size());

        DatabaseManager.addCar("CCC", "nameCar", 1111, Color.czerwony, 3000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        cars = DatabaseManager.getAllAvaiableCars();
        assertEquals(1, cars.size());
        DatabaseManager.setVehicleUnavaiable("CCC", VehicleType.car);
        cars = DatabaseManager.getAllAvaiableCars();
        assertEquals(0,cars.size());
        cars = DatabaseManager.getAllABorrowedCars();
        assertEquals(2,cars.size());
        assertEquals(response, cars.get(0).toString());
    }

    @org.junit.jupiter.api.Test
    void addBikeTest() throws ErrorMessageException {
        DatabaseManager.addBike("AAA", "bike", 1111.11, Color.czerwony, 2000);
        String response = "{id = AAA, nazwa = bike, kolor = czerwony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.11}";
        Bike bike = DatabaseManager.getbikeByID("AAA");
        assertEquals(response, bike.toString());

        DatabaseManager.addBike("BBB", "bike", 1111.11, Color.czerwony,  2000);
        bike = DatabaseManager.getbikeByID("AAA");
        assertEquals(response, bike.toString());

        DatabaseManager.addBike("CCC", "bike", 1111.11, Color.zielony,  2000);
        response = "{id = CCC, nazwa = bike, kolor = zielony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.11}";
        bike = DatabaseManager.getbikeByID("CCC");
        assertEquals(response, bike.toString());
    }

    @org.junit.jupiter.api.Test
    void addMotorcycleTest() throws ErrorMessageException {
        DatabaseManager.addMotorcycle("AAA", "XXX1919", 1111.01, Color.czerwony, 2000, 300000, 2.5,9.9);
        String response = "{id = AAA, nazwa = XXX1919, kolor = czerwony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.01, przejechane kilometry = 300000, pojemność silnika = 2.5, zużycie paliwa = 9.9}";
        Motorcycle motorcycle = DatabaseManager.getMotorcycleByID("AAA");
        assertEquals(response, motorcycle.toString());

        DatabaseManager.addMotorcycle("BBB", "XXX1919", 1111.01, Color.czerwony, 2000, 300000, 2.5,9.9);
        motorcycle = DatabaseManager.getMotorcycleByID("AAA");
        assertEquals(response, motorcycle.toString());

        DatabaseManager.addMotorcycle("CCC", "XXX1919", 1111.01, Color.czerwony, 2000, 300000, 2.5,9.9);
        response = "{id = BBB, nazwa = XXX1919, kolor = czerwony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.01, przejechane kilometry = 300000, pojemność silnika = 2.5, zużycie paliwa = 9.9}";
        motorcycle = DatabaseManager.getMotorcycleByID("BBB");
        assertEquals(response, motorcycle.toString());
    }

    @org.junit.jupiter.api.Test
    void setMotorcycleAsArchival_Test() throws ErrorMessageException {
        DatabaseManager.addMotorcycle("AAA", "XXX1919", 1111.01, Color.czerwony, 2000, 300000, 2.5,9.9);
        DatabaseManager.markVehicleAsArchival("AAA",VehicleType.motorcycle);
        String response = "{id = AAA, nazwa = XXX1919, kolor = czerwony, rok produkcji = 2000, dostepnosc = archived, cena za dzien = 1111.01, przejechane kilometry = 300000, pojemność silnika = 2.5, zużycie paliwa = 9.9}";
        List<Motorcycle>motorcycle = DatabaseManager.getAllAArchivalMotorcycles();
        assertEquals(response, motorcycle.get(0).toString());
        assertEquals(1, motorcycle.size());

        DatabaseManager.addMotorcycle("BBB", "XXX1919", 1111.01, Color.czerwony, 2000, 300000, 2.5,9.9);
        motorcycle = DatabaseManager.getAllAArchivalMotorcycles();
        assertEquals(response, motorcycle.get(0).toString());
        assertEquals(1, motorcycle.size());
    }

    @Test
    void setVehicleInaccessible() throws ErrorMessageException {
        DatabaseManager.addCar("AAA", "nameCar", 1111, Color.czerwony, 3000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        DatabaseManager.addMotorcycle("BBB", "XXX1919", 1111.01, Color.czerwony, 2000, 300000, 2.5,9.9);
        DatabaseManager.addBike("CCC", "bike", 1111.11, Color.czerwony, 2000);
        Car car = DatabaseManager.getCarByID("AAA");
        Bike bike = DatabaseManager.getbikeByID("CCC");
        Motorcycle motorcycle = DatabaseManager.getMotorcycleByID("BBB");

        assertEquals(VehicleStatus.avaiable, car.getVehicleStatus());
        assertEquals(VehicleStatus.avaiable, bike.getVehicleStatus());
        assertEquals(VehicleStatus.avaiable, motorcycle.getVehicleStatus());

        DatabaseManager.setVehicleUnavaiable("AAA", VehicleType.car);
        DatabaseManager.setVehicleUnavaiable("CCC", VehicleType.bike);
        DatabaseManager.setVehicleUnavaiable("BBB", VehicleType.motorcycle);

        car = DatabaseManager.getCarByID("AAA");
        bike = DatabaseManager.getbikeByID("CCC");
        motorcycle = DatabaseManager.getMotorcycleByID("BBB");

        assertEquals(VehicleStatus.unavaiable, car.getVehicleStatus());
        assertEquals(VehicleStatus.unavaiable, bike.getVehicleStatus());
        assertEquals(VehicleStatus.unavaiable, motorcycle.getVehicleStatus());
    }

    @Test
    void setVehicleAccessible() throws ErrorMessageException {
        DatabaseManager.addCar("AAA", "nameCar", 1111, Color.czerwony, 3000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        DatabaseManager.addMotorcycle("BBB", "XXX1919", 1111.01, Color.czerwony, 2000, 300000, 2.5,9.9);
        DatabaseManager.addBike("CCC", "bike", 1111.11, Color.czerwony, 2000);
        DatabaseManager.setVehicleUnavaiable("AAA", VehicleType.car);
        DatabaseManager.setVehicleUnavaiable("CCC", VehicleType.bike);
        DatabaseManager.setVehicleUnavaiable("BBB", VehicleType.motorcycle);

        Car car = DatabaseManager.getCarByID("AAA");
        Bike bike = DatabaseManager.getbikeByID("CCC");
        Motorcycle motorcycle = DatabaseManager.getMotorcycleByID("BBB");

        assertEquals(VehicleStatus.unavaiable, car.getVehicleStatus());
        assertEquals(VehicleStatus.unavaiable, bike.getVehicleStatus());
        assertEquals(VehicleStatus.unavaiable, motorcycle.getVehicleStatus());

        DatabaseManager.setVehicleAvaiable("AAA", VehicleType.car);
        DatabaseManager.setVehicleAvaiable("CCC", VehicleType.bike);
        DatabaseManager.setVehicleAvaiable("BBB", VehicleType.motorcycle);

        car = DatabaseManager.getCarByID("AAA");
        bike = DatabaseManager.getbikeByID("CCC");
        motorcycle = DatabaseManager.getMotorcycleByID("BBB");


        assertEquals(VehicleStatus.avaiable, car.getVehicleStatus());
        assertEquals(VehicleStatus.avaiable, bike.getVehicleStatus());
        assertEquals(VehicleStatus.avaiable, motorcycle.getVehicleStatus());
    }

    @org.junit.jupiter.api.Test
    void getPricePerDay_Test() throws ErrorMessageException {
        DatabaseManager.addCar("AAA", "nameCar", 100.01, Color.czerwony, 3000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        DatabaseManager.addMotorcycle("BBB", "XXX1919", 200.02, Color.czerwony, 2000, 300000, 2.5,9.9);
        DatabaseManager.addBike("CCC", "bike", 300.03, Color.czerwony, 2000);

        double price = DatabaseManager.getPricePerDayOfVehicle("AAA",VehicleType.car);
        assertEquals(100.01, price);
        price = DatabaseManager.getPricePerDayOfVehicle("BBB",VehicleType.motorcycle);
        assertEquals(200.02, price);
        price = DatabaseManager.getPricePerDayOfVehicle("CCC",VehicleType.bike);
        assertEquals(300.03, price);
    }

    @org.junit.jupiter.api.Test
    void getEmployeeById() throws ErrorMessageException {
        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres",888777666, "email", "1", "", "");
        Employee employee = DatabaseManager.getEmployeeByID(1);
        assertEquals("{UUID = 1, imię = Dawid, nazwisko = Dawidziak, numer telefonu = 888777666, adres = addres, email = email}", employee.toString());
    }
}