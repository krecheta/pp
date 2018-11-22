package DataBase;

import Model.Client;
import Model.CustomEnumValues.Fuel;
import Model.CustomExceptions.WrongPeselException;
import Model.Rent;
import Model.Vehicles.Bike;
import Model.Vehicles.Car;
import Model.Vehicles.Motorcycle;
import org.junit.jupiter.api.AfterEach;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class DataBaseTest {
    static DataBase database;

    @AfterEach
    void closeDatabaseConnection() {
        database.closeConnection();
    }

    @org.junit.jupiter.api.BeforeEach
    void setupDatabase() {
        File file = new File("biblioteka.db");
        if (file.delete())
            System.out.println("File deleted");
        else
            System.out.println("Can't find file");
        database = new DataBase();
    }

    //// Add to Acutal Database
    @org.junit.jupiter.api.Test
    void addOneClient() {
        database.addClient("actual", "95062910555", "ajsdli", "oiasjdoij", 15, 669669669, "Adress");
        List<Client> clients = database.getAllClients("actual");
        String Response = "{pesel = 95062910555, imię = ajsdli, nazwisko = oiasjdoij, wiek = 15, numer telefonu = 669669669, adres = Adress, łączna zapłacona kwota za wypożyczenia = 0}";

        assertEquals(1, clients.size());
        assertEquals(Response, clients.get(0).toString());
    }


    @org.junit.jupiter.api.Test
    void addThreeClients() {
        database.addClient("actual", "95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        database.addClient("actual", "95062910553", "Lukasz", "Y", 16, 669669660, "Adress1");
        database.addClient("actual", "95062910552", "Grzegorz", "Z", 17, 669669661, "Adress2");

        List<Client> clients = database.getAllClients("actual");
        String Response0 = "{pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}";
        String Response1 = "{pesel = 95062910553, imię = Lukasz, nazwisko = Y, wiek = 16, numer telefonu = 669669660, adres = Adress1, łączna zapłacona kwota za wypożyczenia = 0}";
        String Response2 = "{pesel = 95062910552, imię = Grzegorz, nazwisko = Z, wiek = 17, numer telefonu = 669669661, adres = Adress2, łączna zapłacona kwota za wypożyczenia = 0}";

        assertEquals(3, clients.size());
        assertEquals(Response0, clients.get(0).toString());
        assertEquals(Response1, clients.get(1).toString());
        assertEquals(Response2, clients.get(2).toString());
    }

    @org.junit.jupiter.api.Test
    void addCarTest() {
        database.addCar("actual", "EZD9506", "nameCar", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        String response = "{id = EZD9506, nazwa = nameCar, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}";
        Car car = database.getCarByID("EZD9506");
        assertEquals(response, car.toString());

        database.addCar("actual", "XXX1919", "XXX1919", 1111, "car model", Fuel.petrol, 2000, 2000, 3, 1000);
        car = database.getCarByID("EZD9506");
        assertEquals(response, car.toString());

        database.addCar("actual", "AAA", "XXX1919", 1111, "car model", Fuel.petrol, 2000, 3000, 4, 1000);
        car = database.getCarByID("XXX1919");
        response = "{id = XXX1919, nazwa = XXX1919, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 2000, liczba drzwi = 3}";
        assertEquals(response, car.toString());
    }

    @org.junit.jupiter.api.Test
    void addBikeTest() {
        database.addBike("actual", "AAA", "XXX1919", 1111, "bike model", "Ted", 2000, 3000, 400);
        String response = "{id = AAA, nazwa = XXX1919, przebieg = 1111, dostepnosc = true, cena za dzien = 400, typ = bike model, kolor = Ted, szerokość opon = 2000, rozmiar kola = 3000}";
        Bike bike = database.getbikeByID("AAA");
        assertEquals(response, bike.toString());

        database.addBike("actual", "xxxaaas", "XXX1919", 1111, "bike model", "Ted", 2000, 3000, 400);
        bike = database.getbikeByID("AAA");
        assertEquals(response, bike.toString());

        database.addBike("actual", "XXX", "XXX1919", 2222, "bike model", "Ted", 1, 1, 1);
        response = "{id = xxxaaas, nazwa = XXX1919, przebieg = 1111, dostepnosc = true, cena za dzien = 400, typ = bike model, kolor = Ted, szerokość opon = 2000, rozmiar kola = 3000}";
        bike = database.getbikeByID("xxxaaas");
        assertEquals(response, bike.toString());
    }

    @org.junit.jupiter.api.Test
    void addMotorcycleTest() {
        database.addMotorcycle("actual", "AAA", "XXX1919", 1111, "motorcycle model", 1111, 2000);
        String response = "{id = AAA, nazwa = XXX1919, przebieg = 1111, dostepnosc = true, cena za dzien = 2000, model = motorcycle model, pojemność silnika = 1111}";
        Motorcycle motorcycle = database.getMotorcycleByID("AAA");
        assertEquals(response, motorcycle.toString());

        database.addMotorcycle("actual", "XXX", "XXX1919", 222, "motorcycle model", 222, 222);
        motorcycle = database.getMotorcycleByID("AAA");
        assertEquals(response, motorcycle.toString());

        database.addMotorcycle("actual", "ZZZ", "XXX1919", 1111, "motorcycle model", 1111, 2000);
        response = "{id = XXX, nazwa = XXX1919, przebieg = 222, dostepnosc = true, cena za dzien = 222, model = motorcycle model, pojemność silnika = 222}";
        motorcycle = database.getMotorcycleByID("XXX");
        assertEquals(response, motorcycle.toString());
    }


    @org.junit.jupiter.api.Test
    void addRent() throws WrongPeselException {
        database.addClient("actual", "95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        database.addCar("actual", "EZD9506", "nameCar", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addActualRent("95062910555", "EZD9506", 1, 40, "10-10-2018", "11-11-2018");
        String response_1 = "Wypożyczenie{Samochód = {id = EZD9506, nazwa = nameCar, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

//
        List<Rent> rents = database.getAllActualRents();
        assertEquals(1, rents.size());
        assertEquals(response_1, rents.get(0).toString());

        database.addCar("actual", "XX", "XX", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addActualRent("95062910555", "XX", 1, 40, "10-10-2018", "11-11-2018");
        String response_2 = "Wypożyczenie{Samochód = {id = XX, nazwa = XX, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

        rents = database.getAllActualRents();
        assertEquals(2, rents.size());
        assertEquals(response_1, rents.get(0).toString());
        assertEquals(response_2, rents.get(1).toString());


        database.addCar("actual", "ZZ", "ZZ", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addActualRent("95062910555", "ZZ", 1, 40, "10-10-2018", "11-11-2018");
        String response_3 = "Wypożyczenie{Samochód = {id = ZZ, nazwa = ZZ, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

        rents = database.getAllActualRents();
        assertEquals(3, rents.size());
        assertEquals(response_1, rents.get(0).toString());
        assertEquals(response_2, rents.get(1).toString());
        assertEquals(response_3, rents.get(2).toString());
    }

    @org.junit.jupiter.api.Test
    void setVehicleInaccessible() {
        database.addCar("actual", "XX", "XX", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addBike("actual", "ZZZ", "XXX1919", 1111, "bike model", "Ted", 2000, 3000, 400);
        database.addMotorcycle("actual", "AAA", "XXX1919", 1111, "motorcycle model", 1111, 2000);
        Car car = database.getCarByID("XX");
        Bike bike = database.getbikeByID("ZZZ");
        Motorcycle motorcycle = database.getMotorcycleByID("AAA");

        assertEquals(true, car.isAvailability());
        assertEquals(true, bike.isAvailability());
        assertEquals(true, motorcycle.isAvailability());

        database.setVehicleInaccessible("XX", 1);
        database.setVehicleInaccessible("ZZZ", 2);
        database.setVehicleInaccessible("AAA", 3);

        car = database.getCarByID("XX");
        bike = database.getbikeByID("ZZZ");
        motorcycle = database.getMotorcycleByID("AAA");

        assertEquals(false, car.isAvailability());
        assertEquals(false, bike.isAvailability());
        assertEquals(false, motorcycle.isAvailability());
    }

    @org.junit.jupiter.api.Test
    void setVehicleAccessible() {
        database.addCar("actual", "XX", "XX", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addBike("actual", "ZZZ", "XXX1919", 1111, "bike model", "Ted", 2000, 3000, 400);
        database.addMotorcycle("actual", "AAA", "XXX1919", 1111, "motorcycle model", 1111, 2000);

        database.setVehicleInaccessible("XX", 1);
        database.setVehicleInaccessible("ZZZ", 2);
        database.setVehicleInaccessible("AAA", 3);

        Car car = database.getCarByID("XX");
        Bike bike = database.getbikeByID("ZZZ");
        Motorcycle motorcycle = database.getMotorcycleByID("AAA");

        assertEquals(false, car.isAvailability());
        assertEquals(false, bike.isAvailability());
        assertEquals(false, motorcycle.isAvailability());

        database.setVehicleAccessible("XX", 1);
        database.setVehicleAccessible("ZZZ", 2);
        database.setVehicleAccessible("AAA", 3);

        car = database.getCarByID("XX");
        bike = database.getbikeByID("ZZZ");
        motorcycle = database.getMotorcycleByID("AAA");

        assertEquals(true, car.isAvailability());
        assertEquals(true, bike.isAvailability());
        assertEquals(true, motorcycle.isAvailability());
    }

    @org.junit.jupiter.api.Test
    void updateClientSumPaid() throws WrongPeselException {
        database.addClient("actual", "95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        database.addClient("archival", "95062910555", "Dominik", "X", 15, 669669669, "Adress0");

        Client client = database.getClientByPesel("95062910555");
        Client archivalClient = database.getArchivalClientByPesel("95062910555");
        assertEquals(0, client.getSumPaidForAllRents());
        assertEquals(0, archivalClient.getSumPaidForAllRents());

        database.updateClientSumPaid("95062910555", 100);

        client = database.getClientByPesel("95062910555");
        archivalClient = database.getArchivalClientByPesel("95062910555");
        assertEquals(100, client.getSumPaidForAllRents());
        assertEquals(100, archivalClient.getSumPaidForAllRents());

        database.updateClientSumPaid("95062910555", 500);

        client = database.getClientByPesel("95062910555");
        archivalClient = database.getArchivalClientByPesel("95062910555");
        assertEquals(500, client.getSumPaidForAllRents());
        assertEquals(500, archivalClient.getSumPaidForAllRents());
    }


    @org.junit.jupiter.api.Test
    void deleteRentFromActualRents() throws WrongPeselException {
        database.addClient("actual", "95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        database.addCar("actual", "EZD9506", "nameCar", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addCar("actual", "XX", "XX", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addCar("actual", "ZZ", "ZZ", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addActualRent("95062910555", "EZD9506", 1, 40, "10-10-2018", "11-11-2018");
        database.addActualRent("95062910555", "XX", 1, 40, "10-10-2018", "11-11-2018");
        database.addActualRent("95062910555", "ZZ", 1, 40, "10-10-2018", "11-11-2018");
        String response_1 = "Wypożyczenie{Samochód = {id = EZD9506, nazwa = nameCar, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

        String response_2 = "Wypożyczenie{Samochód = {id = XX, nazwa = XX, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

       String response_3 = "Wypożyczenie{Samochód = {id = ZZ, nazwa = ZZ, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

        List<Rent> rents = database.getAllActualRents();
        assertEquals(3, rents.size());
        assertEquals(response_1, rents.get(0).toString());
        assertEquals(response_2, rents.get(1).toString());
        assertEquals(response_3, rents.get(2).toString());

        database.deleteRentFromActualRents(1);

        rents = database.getAllActualRents();
        assertEquals(2, rents.size());
        assertEquals(response_2, rents.get(0).toString());
        assertEquals(response_3, rents.get(1).toString());

        database.deleteRentFromActualRents(3);

        rents = database.getAllActualRents();
        assertEquals(1, rents.size());
        assertEquals(response_2, rents.get(0).toString());

        database.deleteRentFromActualRents(2);

        rents = database.getAllActualRents();
        assertEquals(0, rents.size());
    }

    @org.junit.jupiter.api.Test
    void getClientActualRents() {
        String response_1 = "Wypożyczenie{Samochód = {id = EZD9506, nazwa = nameCar, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

        String response_2 = "Wypożyczenie{Samochód = {id = XX, nazwa = XX, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

        String response_3 = "Wypożyczenie{Samochód = {id = ZZ, nazwa = ZZ, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

        database.addClient("actual", "95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        database.addCar("actual", "EZD9506", "nameCar", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addCar("actual", "XX", "XX", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addCar("actual", "ZZ", "ZZ", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);

        List<Rent> rents = database.getClientActualRents("95062910555");
        assertEquals(0, rents.size());

        database.addActualRent("95062910555", "EZD9506", 1, 40, "10-10-2018", "11-11-2018");
        rents = database.getClientActualRents("95062910555");
        assertEquals(1, rents.size());
        assertEquals(response_1, rents.get(0).toString());


        database.addActualRent("95062910555", "XX", 1, 40, "10-10-2018", "11-11-2018");
        database.addActualRent("95062910555", "ZZ", 1, 40, "10-10-2018", "11-11-2018");
        rents = database.getClientActualRents("95062910555");
        assertEquals(3, rents.size());
        assertEquals(response_1, rents.get(0).toString());
        assertEquals(response_2, rents.get(1).toString());
        assertEquals(response_3, rents.get(2).toString());

        database.addActualRent("95062910545", "XX", 1, 40, "10-10-2018", "11-11-2018");
        database.addActualRent("95062911555", "ZZ", 1, 40, "10-10-2018", "11-11-2018");
        rents = database.getClientActualRents("95062910555");
        assertEquals(3, rents.size());
        assertEquals(response_1, rents.get(0).toString());
        assertEquals(response_2, rents.get(1).toString());
        assertEquals(response_3, rents.get(2).toString());
    }



/// Add to archival database
    @org.junit.jupiter.api.Test
    void addOneClient_Archival() {
        database.addClient("archival", "95062910555", "ajsdli", "oiasjdoij", 15, 669669669, "Adress");
        List<Client> clients = database.getAllClients("archival");
        String Response = "{pesel = 95062910555, imię = ajsdli, nazwisko = oiasjdoij, wiek = 15, numer telefonu = 669669669, adres = Adress, łączna zapłacona kwota za wypożyczenia = 0}";

        assertEquals(1, clients.size());
        assertEquals(Response, clients.get(0).toString());
    }


    @org.junit.jupiter.api.Test
    void addThreeClients_Archival() {
        database.addClient("archival", "95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        database.addClient("archival", "95062910553", "Lukasz", "Y", 16, 669669660, "Adress1");
        database.addClient("archival", "95062910552", "Grzegorz", "Z", 17, 669669661, "Adress2");

        List<Client> clients = database.getAllClients("archival");
        String Response0 = "{pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}";
        String Response1 = "{pesel = 95062910553, imię = Lukasz, nazwisko = Y, wiek = 16, numer telefonu = 669669660, adres = Adress1, łączna zapłacona kwota za wypożyczenia = 0}";
        String Response2 = "{pesel = 95062910552, imię = Grzegorz, nazwisko = Z, wiek = 17, numer telefonu = 669669661, adres = Adress2, łączna zapłacona kwota za wypożyczenia = 0}";

        assertEquals(3, clients.size());
        assertEquals(Response0, clients.get(0).toString());
        assertEquals(Response1, clients.get(1).toString());
        assertEquals(Response2, clients.get(2).toString());
    }

    @org.junit.jupiter.api.Test
    void addCarTest_Archival() {
        database.addCar("archival", "EZD9506", "nameCar", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        String response = "{id = EZD9506, nazwa = nameCar, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}";
        Car car = database.getArchivalCarByID("EZD9506");
        assertEquals(response, car.toString());


        database.addCar("archival", "XXX1919", "XXX1919", 1111, "car model", Fuel.petrol, 2000, 2000, 3, 1000);
        car = database.getArchivalCarByID("EZD9506");
        assertEquals(response, car.toString());

        database.addCar("archival", "AAA", "XXX1919", 1111, "car model", Fuel.petrol, 2000, 3000, 4, 1000);
        car = database.getArchivalCarByID("XXX1919");
        response = "{id = XXX1919, nazwa = XXX1919, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 2000, liczba drzwi = 3}";
        assertEquals(response, car.toString());
    }

    @org.junit.jupiter.api.Test
    void addBikeTest_Archival() {
        database.addBike("archival", "AAA", "XXX1919", 1111, "bike model", "Ted", 2000, 3000, 400);
        String response = "{id = AAA, nazwa = XXX1919, przebieg = 1111, dostepnosc = true, cena za dzien = 400, typ = bike model, kolor = Ted, szerokość opon = 2000, rozmiar kola = 3000}";
        Bike bike = database.getArchivalBikeByID("AAA");
        assertEquals(response, bike.toString());

        database.addBike("archival", "xxxaaas", "XXX1919", 1111, "bike model", "Ted", 2000, 3000, 400);
        bike = database.getArchivalBikeByID("AAA");
        assertEquals(response, bike.toString());

        database.addBike("archival", "XXX", "XXX1919", 2222, "bike model", "Ted", 1, 1, 1);
        response = "{id = xxxaaas, nazwa = XXX1919, przebieg = 1111, dostepnosc = true, cena za dzien = 400, typ = bike model, kolor = Ted, szerokość opon = 2000, rozmiar kola = 3000}";
        bike = database.getArchivalBikeByID("xxxaaas");
        assertEquals(response, bike.toString());
    }

    @org.junit.jupiter.api.Test
    void addMotorcycleTest_Archival() {
        database.addMotorcycle("archival", "AAA", "XXX1919", 1111, "motorcycle model", 1111, 2000);
        String response = "{id = AAA, nazwa = XXX1919, przebieg = 1111, dostepnosc = true, cena za dzien = 2000, model = motorcycle model, pojemność silnika = 1111}";
        Motorcycle motorcycle = database.getArchivalMotorcycleByID("AAA");
        assertEquals(response, motorcycle.toString());

        database.addMotorcycle("archival", "XXX", "XXX1919", 222, "motorcycle model", 222, 222);
        motorcycle = database.getArchivalMotorcycleByID("AAA");
        assertEquals(response, motorcycle.toString());

        database.addMotorcycle("archival", "ZZZ", "XXX1919", 1111, "motorcycle model", 1111, 2000);
        response = "{id = XXX, nazwa = XXX1919, przebieg = 222, dostepnosc = true, cena za dzien = 222, model = motorcycle model, pojemność silnika = 222}";
        motorcycle = database.getArchivalMotorcycleByID("XXX");
        assertEquals(response, motorcycle.toString());
    }


    @org.junit.jupiter.api.Test
    void addRent_Archival() throws WrongPeselException {
        database.addClient("archival", "95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        database.addCar("archival", "EZD9506", "nameCar", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addArchivalRent("95062910555", "EZD9506", 1, 40, "10-10-2018", "11-11-2018");
        String response_1 = "Wypożyczenie{Samochód = {id = EZD9506, nazwa = nameCar, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

        List<Rent> rents = database.getAllArchivalRents();
        assertEquals(1, rents.size());
        assertEquals(response_1, rents.get(0).toString());

        database.addCar("archival", "XX", "XX", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addArchivalRent("95062910555", "XX", 1, 40, "10-10-2018", "11-11-2018");
        String response_2 = "Wypożyczenie{Samochód = {id = XX, nazwa = XX, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

        rents = database.getAllArchivalRents();
        assertEquals(2, rents.size());
        assertEquals(response_1, rents.get(0).toString());
        assertEquals(response_2, rents.get(1).toString());


        database.addCar("archival", "ZZ", "ZZ", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addArchivalRent("95062910555", "ZZ", 1, 40, "10-10-2018", "11-11-2018");
        String response_3 = "Wypożyczenie{Samochód = {id = ZZ, nazwa = ZZ, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

        rents = database.getAllArchivalRents();
        assertEquals(3, rents.size());
        assertEquals(response_1, rents.get(0).toString());
        assertEquals(response_2, rents.get(1).toString());
        assertEquals(response_3, rents.get(2).toString());
    }


    @org.junit.jupiter.api.Test
    void isClientExists_Archival() {
        assertEquals(false, database.isClientExist("95062910555"));

        database.addClient("archival", "95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        assertEquals(true, database.isClientExist("95062910555"));
    }

    @org.junit.jupiter.api.Test
    void isVehicleExists_Archival() {
        assertEquals(false, database.isVehicleExists("XX", 1));
        assertEquals(false, database.isVehicleExists("AAA", 2));
        assertEquals(false, database.isVehicleExists("ZZZ", 3));


        database.addCar("archival", "XX", "XX", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addMotorcycle("archival", "AAA", "XXX1919", 1111, "motorcycle model", 1111, 2000);
        database.addBike("archival", "ZZZ", "XXX1919", 1111, "bike model", "Ted", 2000, 3000, 400);

        assertEquals(true, database.isVehicleExists("XX", 1));
        assertEquals(true, database.isVehicleExists("AAA", 3));
        assertEquals(true, database.isVehicleExists("ZZZ", 2));
    }

    @org.junit.jupiter.api.Test
    void getClientArchivalRents_Archival() {
        String response_1 = "Wypożyczenie{Samochód = {id = EZD9506, nazwa = nameCar, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

        String response_2 = "Wypożyczenie{Samochód = {id = XX, nazwa = XX, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

        String response_3 = "Wypożyczenie{Samochód = {id = ZZ, nazwa = ZZ, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

        database.addClient("archival", "95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        database.addCar("archival", "EZD9506", "nameCar", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addCar("archival", "XX", "XX", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addCar("archival", "ZZ", "ZZ", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);

        List<Rent> rents = database.getClientArchivalRents("95062910555");
        assertEquals(0, rents.size());

        database.addArchivalRent("95062910555", "EZD9506", 1, 40, "10-10-2018", "11-11-2018");
        rents = database.getClientArchivalRents("95062910555");
        assertEquals(1, rents.size());
        assertEquals(response_1, rents.get(0).toString());


        database.addArchivalRent("95062910555", "XX", 1, 40, "10-10-2018", "11-11-2018");
        database.addArchivalRent("95062910555", "ZZ", 1, 40, "10-10-2018", "11-11-2018");
        rents = database.getClientArchivalRents("95062910555");
        assertEquals(3, rents.size());
        assertEquals(response_1, rents.get(0).toString());
        assertEquals(response_2, rents.get(1).toString());
        assertEquals(response_3, rents.get(2).toString());

        database.addArchivalRent("95062910545", "XX", 1, 40, "10-10-2018", "11-11-2018");
        database.addArchivalRent("95062911555", "ZZ", 1, 40, "10-10-2018", "11-11-2018");
        rents = database.getClientArchivalRents("95062910555");
        assertEquals(3, rents.size());
        assertEquals(response_1, rents.get(0).toString());
        assertEquals(response_2, rents.get(1).toString());
        assertEquals(response_3, rents.get(2).toString());
    }
}