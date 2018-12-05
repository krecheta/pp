package DataBase;

import Model.Client;
import Model.CustomEnumValues.Fuel;
import Model.CustomExceptions.ErrorMessageException;
import Model.CustomExceptions.WrongPeselException;
import Model.Employee;
import Model.Logs;
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

    @org.junit.jupiter.api.BeforeAll
   static void setupLogger(){
        new Logs();
    }

    @org.junit.jupiter.api.BeforeEach
    void setupDatabase() throws ErrorMessageException {
        File file = new File("biblioteka.db");
        if (file.delete())
            System.out.println("File deleted");
        else
            System.out.println("Can't find file");
        database = new DataBase();
    }

    //// Add to Acutal Database
    @org.junit.jupiter.api.Test
    void addOneClient_Testt() throws ErrorMessageException {
        database.addClient("95062910555", "ajsdli", "oiasjdoij", 15, 669669669, "Adress");
        List<Client> clients = database.getAllActualClients();
        String Response = "{pesel = 95062910555, imię = ajsdli, nazwisko = oiasjdoij, wiek = 15, numer telefonu = 669669669, adres = Adress, łączna zapłacona kwota za wypożyczenia = 0}";

        assertEquals(1, clients.size());
        assertEquals(Response, clients.get(0).toString());
    }

    @org.junit.jupiter.api.Test
    void addThreeClients__Test() throws ErrorMessageException {
        List<Client> clients = database.getAllActualClients();
        assertEquals(0, clients.size());

        database.addClient("95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        database.addClient("95062910553", "Lukasz", "Y", 16, 669669660, "Adress1");
        database.addClient("95062910552", "Grzegorz", "Z", 17, 669669661, "Adress2");

        clients = database.getAllActualClients();
        String Response0 = "{pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}";
        String Response1 = "{pesel = 95062910553, imię = Lukasz, nazwisko = Y, wiek = 16, numer telefonu = 669669660, adres = Adress1, łączna zapłacona kwota za wypożyczenia = 0}";
        String Response2 = "{pesel = 95062910552, imię = Grzegorz, nazwisko = Z, wiek = 17, numer telefonu = 669669661, adres = Adress2, łączna zapłacona kwota za wypożyczenia = 0}";

        assertEquals(3, clients.size());
        assertEquals(Response0, clients.get(0).toString());
        assertEquals(Response1, clients.get(1).toString());
        assertEquals(Response2, clients.get(2).toString());
    }


    @org.junit.jupiter.api.Test
    void updateClientSumPaid_Test() throws ErrorMessageException {
        database.addClient("95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        Client client = database.getClientByPesel("95062910555");
        assertEquals(0, client.getSumPaidForAllRents());

        database.updateClientSumPaid("95062910555", 100);
        client = database.getClientByPesel("95062910555");
        assertEquals(100, client.getSumPaidForAllRents());

        database.updateClientSumPaid("95062910555", 500);
        client = database.getClientByPesel("95062910555");
        assertEquals(500, client.getSumPaidForAllRents());
    }

    @org.junit.jupiter.api.Test
    void updateCientData_Test() throws ErrorMessageException {
        database.addClient("95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        database.addClient("95062910553", "Lukasz", "Y", 16, 669669660, "Adress1");
        database.addClient("95062910552", "Grzegorz", "Z", 17, 669669661, "Adress2");

        database.updateCientData("95062910555", "Kamil", "A", 5, 69669669, "Adress7");
        database.updateCientData("95062910553", "Rafal", "B", 6, 69669660, "Adress8");
        database.updateCientData("95062910552", "Ryszard", "C", 7, 69669661, "Adress9");

        List<Client> clients = database.getAllActualClients();
        String Response0 = "{pesel = 95062910555, imię = Kamil, nazwisko = A, wiek = 5, numer telefonu = 69669669, adres = Adress7, łączna zapłacona kwota za wypożyczenia = 0}";
        String Response1 = "{pesel = 95062910553, imię = Rafal, nazwisko = B, wiek = 6, numer telefonu = 69669660, adres = Adress8, łączna zapłacona kwota za wypożyczenia = 0}";
        String Response2 = "{pesel = 95062910552, imię = Ryszard, nazwisko = C, wiek = 7, numer telefonu = 69669661, adres = Adress9, łączna zapłacona kwota za wypożyczenia = 0}";

        assertEquals(3, clients.size());
        assertEquals(Response0, clients.get(0).toString());
        assertEquals(Response1, clients.get(1).toString());
        assertEquals(Response2, clients.get(2).toString());
    }

    @org.junit.jupiter.api.Test
    void getClientByPesel__Test() throws ErrorMessageException {
        String Response0 = "{pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}";
        String Response1 = "{pesel = 95062910553, imię = Lukasz, nazwisko = Y, wiek = 16, numer telefonu = 669669660, adres = Adress1, łączna zapłacona kwota za wypożyczenia = 0}";
        String Response2 = "{pesel = 95062910552, imię = Grzegorz, nazwisko = Z, wiek = 17, numer telefonu = 669669661, adres = Adress2, łączna zapłacona kwota za wypożyczenia = 0}";

        database.addClient("95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        database.addClient("95062910553", "Lukasz", "Y", 16, 669669660, "Adress1");
        database.addClient("95062910552", "Grzegorz", "Z", 17, 669669661, "Adress2");

        Client client = database.getClientByPesel("95062910555");
        assertEquals(Response0, client.toString());

        client = database.getClientByPesel("95062910553");
        assertEquals(Response1, client.toString());

        client = database.getClientByPesel("95062910552");
        assertEquals(Response2, client.toString());
    }


    @org.junit.jupiter.api.Test
    void setClientAsArchival__Test() throws ErrorMessageException {
        String Response0 = "{pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}";
        String Response1 = "{pesel = 95062910553, imię = Lukasz, nazwisko = Y, wiek = 16, numer telefonu = 669669660, adres = Adress1, łączna zapłacona kwota za wypożyczenia = 0}";
        String Response2 = "{pesel = 95062910552, imię = Grzegorz, nazwisko = Z, wiek = 17, numer telefonu = 669669661, adres = Adress2, łączna zapłacona kwota za wypożyczenia = 0}";

        List<Client> clients = database.getAllActualClients();
        assertEquals(0, clients.size());

        clients = database.getAllArchivalClients();
        assertEquals(0, clients.size());

        database.addClient("95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        database.addClient("95062910553", "Lukasz", "Y", 16, 669669660, "Adress1");
        database.addClient("95062910552", "Grzegorz", "Z", 17, 669669661, "Adress2");

        database.setClientAsArchival("95062910555");
        clients = database.getAllArchivalClients();
        assertEquals(1, clients.size());

        clients = database.getAllActualClients();
        assertEquals(2, clients.size());

        database.setClientAsArchival("95062910553");
        database.setClientAsArchival("95062910552");
        clients = database.getAllArchivalClients();
        assertEquals(3, clients.size());
        assertEquals(Response0, clients.get(0).toString());
        assertEquals(Response1, clients.get(1).toString());
        assertEquals(Response2, clients.get(2).toString());

        clients = database.getAllActualClients();
        assertEquals(0, clients.size());
    }

    @org.junit.jupiter.api.Test
    void getClientActualRents() throws ErrorMessageException {
        String response_1 = "Wypożyczenie{Samochód = {id = EZD9506, nazwa = nameCar, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

        String response_2 = "Wypożyczenie{Samochód = {id = XX, nazwa = XX, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

        String response_3 = "Wypożyczenie{Samochód = {id = ZZ, nazwa = ZZ, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";
        database.addEmployee("Dawid", "Dawidziak", 888777666);
        database.addClient("95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        database.addCar("EZD9506", "nameCar", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addCar("XX", "XX", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addCar("ZZ", "ZZ", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);

        List<Rent> rents = database.getClientActualRents("95062910555");
        assertEquals(0, rents.size());

        database.addRent("95062910555", "EZD9506", 1, 40, "10-10-2018", "11-11-2018", 1);
        rents = database.getClientActualRents("95062910555");
        assertEquals(1, rents.size());
        assertEquals(response_1, rents.get(0).toString());


        database.addRent("95062910555", "XX", 1, 40, "10-10-2018", "11-11-2018", 1);
        database.addRent("95062910555", "ZZ", 1, 40, "10-10-2018", "11-11-2018", 1);
        rents = database.getClientActualRents("95062910555");
        assertEquals(3, rents.size());
        assertEquals(response_1, rents.get(0).toString());
        assertEquals(response_2, rents.get(1).toString());
        assertEquals(response_3, rents.get(2).toString());

        database.addRent("95062910545", "XX", 1, 40, "10-10-2018", "11-11-2018", 1);
        database.addRent("95062911555", "ZZ", 1, 40, "10-10-2018", "11-11-2018", 1);
        rents = database.getClientActualRents("95062910555");
        assertEquals(3, rents.size());
        assertEquals(response_1, rents.get(0).toString());
        assertEquals(response_2, rents.get(1).toString());
        assertEquals(response_3, rents.get(2).toString());
    }

    @org.junit.jupiter.api.Test
    void getClientArchivalRents_Test() throws ErrorMessageException {
        String response_1 = "Wypożyczenie{Samochód = {id = EZD9506, nazwa = nameCar, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

        String response_2 = "Wypożyczenie{Samochód = {id = XX, nazwa = XX, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

        String response_3 = "Wypożyczenie{Samochód = {id = ZZ, nazwa = ZZ, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

        database.addEmployee("Dawid", "Dawidziak", 888777666);
        database.addClient( "95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        database.addCar( "EZD9506", "nameCar", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addCar("XX", "XX", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addCar("ZZ", "ZZ", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);

        List<Rent> rents = database.getClientArchivalRents("95062910555");
        assertEquals(0, rents.size());

        database.addRent("95062910555", "EZD9506", 1, 40, "10-10-2018", "11-11-2018", 1);
        database.setRentAsArchival(1);
        rents = database.getClientArchivalRents("95062910555");
        assertEquals(1, rents.size());
        assertEquals(response_1, rents.get(0).toString());


        database.addRent("95062910555", "XX", 1, 40, "10-10-2018", "11-11-2018", 1);
        database.addRent("95062910555", "ZZ", 1, 40, "10-10-2018", "11-11-2018", 1);
        database.setRentAsArchival(2);
        database.setRentAsArchival(3);

        rents = database.getClientArchivalRents("95062910555");
        assertEquals(3, rents.size());
        assertEquals(response_1, rents.get(0).toString());
        assertEquals(response_2, rents.get(1).toString());
        assertEquals(response_3, rents.get(2).toString());

        database.addRent("95062910545", "XX", 1, 40, "10-10-2018", "11-11-2018", 1);
        database.addRent("95062911555", "ZZ", 1, 40, "10-10-2018", "11-11-2018", 1);
        rents = database.getClientArchivalRents("95062910555");
        database.setRentAsArchival(3);
        database.setRentAsArchival(4);

        assertEquals(3, rents.size());
        assertEquals(response_1, rents.get(0).toString());
        assertEquals(response_2, rents.get(1).toString());
        assertEquals(response_3, rents.get(2).toString());
    }

    @org.junit.jupiter.api.Test
    void addRent() throws ErrorMessageException {
        database.addEmployee("Dawid", "Dawidziak", 888777666);
        database.addClient("95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        database.addCar("EZD9506", "nameCar", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addRent("95062910555", "EZD9506", 1, 40, "10-10-2018", "11-11-2018", 1);
        String response_1 = "Wypożyczenie{Samochód = {id = EZD9506, nazwa = nameCar, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

//
        List<Rent> rents = database.getAllActualRents();
        assertEquals(1, rents.size());
        assertEquals(response_1, rents.get(0).toString());

        database.addCar("XX", "XX", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addRent("95062910555", "XX", 1, 40, "10-10-2018", "11-11-2018", 1);
        String response_2 = "Wypożyczenie{Samochód = {id = XX, nazwa = XX, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

        rents = database.getAllActualRents();
        assertEquals(2, rents.size());
        assertEquals(response_1, rents.get(0).toString());
        assertEquals(response_2, rents.get(1).toString());


        database.addCar("ZZ", "ZZ", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addRent("95062910555", "ZZ", 1, 40, "10-10-2018", "11-11-2018", 1);
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
    void updateRentPrice_Test() throws ErrorMessageException {
        database.addEmployee("Dawid", "Dawidziak", 888777666);
        database.addClient("95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        database.addCar("EZD9506", "nameCar", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addRent("95062910555", "EZD9506", 1, 40, "10-10-2018", "11-11-2018", 1);
        String response_1 = "Wypożyczenie{Samochód = {id = EZD9506, nazwa = nameCar, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

//
        Rent rent = database.getRentByRentID(1);
        assertEquals(40, rent.getPriceForRent());

        database.updateRentPrice(1,100);
        rent = database.getRentByRentID(1);
        assertEquals(100, rent.getPriceForRent());

        database.updateRentPrice(1,300);
        rent = database.getRentByRentID(1);
        assertEquals(300, rent.getPriceForRent());
    }

    @org.junit.jupiter.api.Test
    void addRent_Archival() throws ErrorMessageException {
        database.addEmployee("Dawid", "Dawidziak", 888777666);
        database.addClient( "95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        database.addCar( "EZD9506", "nameCar", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addRent("95062910555", "EZD9506", 1, 40, "10-10-2018", "11-11-2018", 1);
        String response_1 = "Wypożyczenie{Samochód = {id = EZD9506, nazwa = nameCar, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

        database.setRentAsArchival(1);
        List<Rent> rents = database.getAllArchivalRents();
        assertEquals(1, rents.size());
        assertEquals(response_1, rents.get(0).toString());

        database.addCar( "XX", "XX", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addRent("95062910555", "XX", 1, 40, "10-10-2018", "11-11-2018", 1);
        String response_2 = "Wypożyczenie{Samochód = {id = XX, nazwa = XX, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";
        database.setRentAsArchival(2);

        rents = database.getAllArchivalRents();
        assertEquals(2, rents.size());
        assertEquals(response_1, rents.get(0).toString());
        assertEquals(response_2, rents.get(1).toString());


        database.addCar( "ZZ", "ZZ", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addRent("95062910555", "ZZ", 1, 40, "10-10-2018", "11-11-2018", 1);
        String response_3 = "Wypożyczenie{Samochód = {id = ZZ, nazwa = ZZ, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}," +
                " klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                "koszt wypożyczenia = 40, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";
        database.setRentAsArchival(3);
        rents = database.getAllArchivalRents();
        assertEquals(3, rents.size());
        assertEquals(response_1, rents.get(0).toString());
        assertEquals(response_2, rents.get(1).toString());
        assertEquals(response_3, rents.get(2).toString());
        rents = database.getAllActualRents();
        assertEquals(0,rents.size());


 }

    @org.junit.jupiter.api.Test
    void addCarTest() throws ErrorMessageException {
        database.addCar("EZD9506", "nameCar", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        String response = "{id = EZD9506, nazwa = nameCar, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}";
        Car car = database.getCarByID("EZD9506");
        assertEquals(response, car.toString());

        database.addCar("XXX1919", "XXX1919", 1111, "car model", Fuel.petrol, 2000, 2000, 3, 1000);
        car = database.getCarByID("EZD9506");
        assertEquals(response, car.toString());

        database.addCar("AAA", "XXX1919", 1111, "car model", Fuel.petrol, 2000, 3000, 4, 1000);
        car = database.getCarByID("XXX1919");
        response = "{id = XXX1919, nazwa = XXX1919, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 2000, liczba drzwi = 3}";
        assertEquals(response, car.toString());
    }

    @org.junit.jupiter.api.Test
    void setCarAsArchival_Test() throws ErrorMessageException {
        database.addCar("EZD9506", "nameCar", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        String response = "{id = EZD9506, nazwa = nameCar, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}";
        database.setVehicleArchival("EZD9506", 1);
        List<Car> cars = database.getAllAArchivalCars();
        assertEquals(response, cars.get(0).toString());
        assertEquals(1,cars.size());

        database.addCar("XXX1919", "XXX1919", 1111, "car model", Fuel.petrol, 2000, 2000, 3, 1000);


        cars = database.getAllAArchivalCars();
        assertEquals(response, cars.get(0).toString());
        assertEquals(1, cars.size());
        cars = database.getAllActuallCars();
        assertEquals(1,cars.size());
    }

    @org.junit.jupiter.api.Test
    void setCarInacesible_Test() throws ErrorMessageException {
        database.addCar("EZD9506", "nameCar", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        String response = "{id = EZD9506, nazwa = nameCar, przebieg = 1111, dostepnosc = false, cena za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}";
        database.setVehicleInaccessible("EZD9506", 1);
        List<Car> cars = database.getAllAvaiableCars();
        assertEquals(0,cars.size());

        database.addCar("XXX1919", "XXX1919", 1111, "car model", Fuel.petrol, 2000, 2000, 3, 1000);
        cars = database.getAllAvaiableCars();
        assertEquals(1, cars.size());
        database.setVehicleInaccessible("EZD9506", 1);
        cars = database.getAllAvaiableCars();
        assertEquals(1,cars.size());
        cars = database.getAllABorrowedCars();
        assertEquals(1,cars.size());
        assertEquals(response, cars.get(0).toString());
    }

    @org.junit.jupiter.api.Test
    void addBikeTest() throws ErrorMessageException {
        database.addBike("AAA", "XXX1919", 1111, "bike model", "Ted", 2000, 3000, 400);
        String response = "{id = AAA, nazwa = XXX1919, przebieg = 1111, dostepnosc = true, cena za dzien = 400, typ = bike model, kolor = Ted, szerokość opon = 2000, rozmiar kola = 3000}";
        Bike bike = database.getbikeByID("AAA");
        assertEquals(response, bike.toString());

        database.addBike("xxxaaas", "XXX1919", 1111, "bike model", "Ted", 2000, 3000, 400);
        bike = database.getbikeByID("AAA");
        assertEquals(response, bike.toString());

        database.addBike("XXX", "XXX1919", 2222, "bike model", "Ted", 1, 1, 1);
        response = "{id = xxxaaas, nazwa = XXX1919, przebieg = 1111, dostepnosc = true, cena za dzien = 400, typ = bike model, kolor = Ted, szerokość opon = 2000, rozmiar kola = 3000}";
        bike = database.getbikeByID("xxxaaas");
        assertEquals(response, bike.toString());
    }

    @org.junit.jupiter.api.Test
    void addBikeTest_Archival() throws ErrorMessageException {
        database.addBike( "AAA", "XXX1919", 1111, "bike model", "Ted", 2000, 3000, 400);
        database.setVehicleArchival("AAA", 2);
        String response = "{id = AAA, nazwa = XXX1919, przebieg = 1111, dostepnosc = true, cena za dzien = 400, typ = bike model, kolor = Ted, szerokość opon = 2000, rozmiar kola = 3000}";
        List<Bike> bike = database.getAllAArchivalBikes();
        assertEquals(response, bike.get(0).toString());
        assertEquals(1,bike.size());

        database.addBike("xxxaaas", "XXX1919", 1111, "bike model", "Ted", 2000, 3000, 400);
        bike = database.getAllAArchivalBikes();
        assertEquals(response, bike.get(0).toString());
        assertEquals(1,bike.size());
        bike = database.getAllActuallBikes();
        assertEquals(1,bike.size());
    }

    @org.junit.jupiter.api.Test
    void addMotorcycleTest() throws ErrorMessageException {
        database.addMotorcycle("AAA", "XXX1919", 1111, "motorcycle model", 1111, 2000);
        String response = "{id = AAA, nazwa = XXX1919, przebieg = 1111, dostepnosc = true, cena za dzien = 2000, model = motorcycle model, pojemność silnika = 1111}";
        Motorcycle motorcycle = database.getMotorcycleByID("AAA");
        assertEquals(response, motorcycle.toString());

        database.addMotorcycle("XXX", "XXX1919", 222, "motorcycle model", 222, 222);
        motorcycle = database.getMotorcycleByID("AAA");
        assertEquals(response, motorcycle.toString());

        database.addMotorcycle("ZZZ", "XXX1919", 1111, "motorcycle model", 1111, 2000);
        response = "{id = XXX, nazwa = XXX1919, przebieg = 222, dostepnosc = true, cena za dzien = 222, model = motorcycle model, pojemność silnika = 222}";
        motorcycle = database.getMotorcycleByID("XXX");
        assertEquals(response, motorcycle.toString());
    }

    @org.junit.jupiter.api.Test
    void setMotorcycleAsArchival_Test() throws ErrorMessageException {
        database.addMotorcycle( "AAA", "XXX1919", 1111, "motorcycle model", 1111, 2000);
        database.setVehicleArchival("AAA",3);
        String response = "{id = AAA, nazwa = XXX1919, przebieg = 1111, dostepnosc = true, cena za dzien = 2000, model = motorcycle model, pojemność silnika = 1111}";
        List<Motorcycle>motorcycle = database.getAllAArchivalMotorcycles();
        assertEquals(response, motorcycle.get(0).toString());
        assertEquals(1, motorcycle.size());

        database.addMotorcycle( "XXX", "XXX1919", 222, "motorcycle model", 222, 222);
        motorcycle = database.getAllAArchivalMotorcycles();
        assertEquals(response, motorcycle.get(0).toString());
        assertEquals(1, motorcycle.size());
    }



    @org.junit.jupiter.api.Test
    void setVehicleInaccessible() throws ErrorMessageException {
        database.addCar("XX", "XX", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addBike("ZZZ", "XXX1919", 1111, "bike model", "Ted", 2000, 3000, 400);
        database.addMotorcycle("AAA", "XXX1919", 1111, "motorcycle model", 1111, 2000);
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
    void setVehicleAccessible() throws ErrorMessageException {
        database.addCar("XX", "XX", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addBike("ZZZ", "XXX1919", 1111, "bike model", "Ted", 2000, 3000, 400);
        database.addMotorcycle("AAA", "XXX1919", 1111, "motorcycle model", 1111, 2000);

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
    void getPricePerDay_Test() throws ErrorMessageException {
        database.addCar("EZD9506", "nameCar", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        database.addBike("AAA", "XXX1919", 1111, "bike model", "Ted", 2000, 3000, 400);
        database.addMotorcycle("AAA", "XXX1919", 1111, "motorcycle model", 1111, 2000);

        int price = database.getPricePerDayOfVehicle("EZD9506",1);
        assertEquals(1000, price);
        price = database.getPricePerDayOfVehicle("AAA",2);
        assertEquals(400, price);
        price = database.getPricePerDayOfVehicle("AAA",3);
        assertEquals(2000, price);
    }

    @org.junit.jupiter.api.Test
    void getEmployeeById() throws ErrorMessageException {
        database.addEmployee("Dawid", "Dawidziak", 888777666);
        Employee employee = database.getEmployeeByID(1);
        assertEquals("Imie = Dawid, nazwisko = Dawidziak, numer telefonu = 888777666", employee.toString());
    }

}