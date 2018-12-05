package Model;

import DataBase.DataBase;
import Model.CustomEnumValues.Fuel;
import Model.CustomExceptions.ErrorMessageException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    static Model model;

    @org.junit.jupiter.api.BeforeAll
    static void setupLogger() {
        new Logs();
    }

    @org.junit.jupiter.api.BeforeEach
    void setupDatabase() throws ErrorMessageException {
        File file = new File("biblioteka.db");
        if (file.delete())
            System.out.println("File deleted");
        else
            System.out.println("Can't find file");
        model = new Model();
    }

    @AfterEach
    void closeDatabaseConnection() {
        model.closeConnection();
    }

    @Test
    void addRent() throws ErrorMessageException {
        String response_1 = "Wypożyczenie{Samochód = {id = AAA1111, nazwa = nameCar, przebieg = 1111, dostepnosc = false, cena " +
                            "za dzien = 1000, model = car model, paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000," +
                            " liczba drzwi = 5}, klient = {pesel = 95062910555, imię = Dominik, nazwisko = X, wiek = 15, " +
                            "numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                            "koszt wypożyczenia = 33000, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

        String response_2 = "Wypożyczenie{Rower = {id = BBB1111, nazwa = XXX1919, przebieg = 1111, dostepnosc = false, cena za dzien = 400," +
                            " typ = bike model, kolor = Ted, szerokość opon = 2000, rozmiar kola = 3000}, klient = {pesel = 95062910555, imię = Dominik," +
                            " nazwisko = X, wiek = 15, numer telefonu = 669669669, adres = Adress0, łączna zapłacona kwota za wypożyczenia = 0}, " +
                            "koszt wypożyczenia = 13200, data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";

        String response_3 = "Wypożyczenie{Motocykl = {id = CCC1111, nazwa = XXX1919, przebieg = 1111, dostepnosc = false, cena za dzien = 2000, model = motorcycle " +
                            "model, pojemność silnika = 1111}, klient = {pesel = 95062910554, imię = Dawid, nazwisko = y, wiek = 120, " +
                            "numer telefonu = 669669669, adres = Adress1, łączna zapłacona kwota za wypożyczenia = 0}, koszt wypożyczenia = 66000, " +
                            "data wypożyczenia = 10-10-2018, planowana data zwrotu = 11-11-2018}";


        model.addEmployee("Dawid", "Dawidziak", 888777666);
        model.addClient("95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        model.addClient("95062910554", "Dawid", "y", 120, 669669669, "Adress1");
        model.addCar("AAA1111", "nameCar", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        model.addBike("BBB1111", "XXX1919", 1111, "bike model", "Ted", 2000, 3000, 400);
        model.addMotorcycle("CCC1111", "XXX1919", 1111, "motorcycle model", 1111, 2000);

    //check if all actual and archival rents are empty on the begining of the test
        List<Rent> rents = model.getAllActualRents();
        assertEquals(0, rents.size());

        rents = model.getAllArchivalRents();
        assertEquals(0, rents.size());

    // Added one rents
        model.addRent(model.getClientByPesel("95062910555"), model.getCarByID("AAA1111"), "10-10-2018", "11-11-2018", 1);
        rents = model.getAllActualRents();
        assertEquals(1, rents.size());
        assertEquals(response_1, rents.get(0).toString());

    // Added two more rents
        model.addRent(model.getClientByPesel("95062910555"), model.getbikeByID("BBB1111"), "10-10-2018", "11-11-2018", 1);
        model.addRent(model.getClientByPesel("95062910554"), model.getMotorcycleByID("CCC1111"), "10-10-2018", "11-11-2018", 1);

        rents = model.getAllActualRents();
        assertEquals(3, rents.size());
        assertEquals(response_1, rents.get(0).toString());
        assertEquals(response_2, rents.get(1).toString());
        assertEquals(response_3, rents.get(2).toString());

    //Client 1 rents
        rents = model.getClientActualRents("95062910555");
        assertEquals(2, rents.size());
        assertEquals(response_1, rents.get(0).toString());
        assertEquals(response_2, rents.get(1).toString());
        assertEquals(0,model.getClientArchivalRents("95062910555").size());


        // Client 2 rents
        rents = model.getClientActualRents("95062910554");
        assertEquals(1, rents.size());
        assertEquals(response_3, rents.get(0).toString());
        assertEquals(0,model.getClientArchivalRents("95062910554").size());
    }

    @Test
    void returnVehicle() throws ErrorMessageException {
        String car = "{id = AAA1111, nazwa = nameCar, przebieg = 1111, dostepnosc = true, cena za dzien = 1000, model = car model, " +
                     "paliwo = petrol, pojemność silnika = 2000, pojemność bagażnika = 1000, liczba drzwi = 5}";
        String motorczcle = "{id = CCC1111, nazwa = XXX1919, przebieg = 1111, dostepnosc = true, cena za dzien = 2000, model = motorcycle model, pojemność silnika = 1111}";
        String bike = "{id = BBB1111, nazwa = XXX1919, przebieg = 1111, dostepnosc = true, cena za dzien = 400, typ = bike model, kolor = Ted, szerokość opon = 2000, rozmiar kola = 3000}";

        model.addEmployee("Dawid", "Dawidziak", 888777666);
        model.addClient("95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        model.addClient("95062910554", "Dawid", "y", 120, 669669669, "Adress1");
        model.addCar("AAA1111", "nameCar", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);
        model.addBike("BBB1111", "XXX1919", 1111, "bike model", "Ted", 2000, 3000, 400);
        model.addMotorcycle("CCC1111", "XXX1919", 1111, "motorcycle model", 1111, 2000);

        //check if all actual and archival rents are empty on the begining of the test
        List<Rent> rents = model.getAllActualRents();
        assertEquals(0, rents.size());

        rents = model.getAllArchivalRents();
        assertEquals(0, rents.size());

    // Return one rents
        model.addRent(model.getClientByPesel("95062910555"), model.getCarByID("AAA1111"), "10-10-2018", "11-11-2018", 1);
        model.returnVehicle(1,0);

        int clientPaidSum = model.getClientByPesel("95062910555").getSumPaidForAllRents();
        System.out.println(model.getClientByPesel("95062910555").toString());
        rents = model.getAllActualRents();
        assertEquals(0, rents.size());
        rents = model.getAllArchivalRents();
        assertEquals(1, rents.size());
        assertEquals(1, model.getAllAvaiableCars().size());
        assertEquals(car, model.getAllAvaiableCars().get(0).toString());
        assertEquals(true, clientPaidSum > 0 );

    // Return two more rents
        model.addRent(model.getClientByPesel("95062910555"), model.getbikeByID("BBB1111"), "10-10-2018", "11-11-2018", 1);
        model.addRent(model.getClientByPesel("95062910554"), model.getMotorcycleByID("CCC1111"), "10-10-2018", "11-11-2018", 1);
        model.returnVehicle(2,0);
        model.returnVehicle(3,0);

    // Check is price upper than on the begining
        int clientPaidSum2 = model.getClientByPesel("95062910555").getSumPaidForAllRents();
        int clientPaidSum3 = model.getClientByPesel("95062910554").getSumPaidForAllRents();

        System.out.println(model.getClientByPesel("95062910555").toString());

    // Check rents are archuival and values of them are properly
        rents = model.getAllActualRents();
        assertEquals(0, rents.size());
        rents = model.getAllArchivalRents();
        assertEquals(3, rents.size());

    // Cheks add vehicle are accesible after return
        assertEquals(1, model.getAllAvaiableCars().size());
        assertEquals(car, model.getAllAvaiableCars().get(0).toString());

        assertEquals(1, model.getAllAvaiableBikes().size());
        assertEquals(bike, model.getAllAvaiableBikes().get(0).toString());

        assertEquals(1, model.getAllAvaiableMotorcycles().size());
        assertEquals(motorczcle, model.getAllAvaiableMotorcycles().get(0).toString());


        assertEquals(true, clientPaidSum2 > clientPaidSum);
        assertEquals(true, clientPaidSum3 > 0);

        rents = model.getAllActualRents();
        assertEquals(0, rents.size());
        rents = model.getAllArchivalRents();
        assertEquals(3, rents.size());

        //Client 1 rents
        rents = model.getClientArchivalRents("95062910555");
        assertEquals(2, rents.size());
        assertEquals(0,model.getClientActualRents("95062910555").size());


        // Client 2 rents
        rents = model.getClientArchivalRents("95062910554");
        assertEquals(1, rents.size());
        assertEquals(0,model.getClientActualRents("95062910554").size());
    }

    @Test
    void getClientPercentOfDiscount() throws ErrorMessageException {
    model.addClient("95062910555", "Dominik", "X", 15, 669669669, "Adress0");
    int discoutn = model.getClientPercentOfDiscount(model.getClientByPesel("95062910555"));
    assertEquals(0, discoutn);

    model.discount("95062910555", 10001);
    discoutn = model.getClientPercentOfDiscount(model.getClientByPesel("95062910555"));
    assertEquals(5, discoutn);

    model.discount("95062910555", 20001);
    discoutn = model.getClientPercentOfDiscount(model.getClientByPesel("95062910555"));
    assertEquals(10, discoutn);

    model.discount("95062910555", 30001);
    discoutn = model.getClientPercentOfDiscount(model.getClientByPesel("95062910555"));
    assertEquals(15, discoutn);

    model.discount("95062910555", 40001);
    discoutn = model.getClientPercentOfDiscount(model.getClientByPesel("95062910555"));
    assertEquals(20, discoutn);

    model.discount("95062910555", 50001);
    discoutn = model.getClientPercentOfDiscount(model.getClientByPesel("95062910555"));
    assertEquals(25, discoutn);

    model.discount("95062910555", 60001);
    discoutn = model.getClientPercentOfDiscount(model.getClientByPesel("95062910555"));
    assertEquals(25, discoutn);

    }

    @Test
    void calculatePriceForRent() throws ErrorMessageException {
        model.addClient("95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        model.addCar("AAA1111", "nameCar", 1111, "car model", Fuel.petrol, 2000, 1000, 5, 1000);

        assertEquals(1000, model.calculatePriceForRent(model.getClientByPesel("95062910555"), model.getCarByID("AAA1111"), "01-01-2018", "01-01-2018"));
        assertEquals(30000, model.calculatePriceForRent(model.getClientByPesel("95062910555"), model.getCarByID("AAA1111"), "01-01-2018", "30-01-2018"));
        assertEquals(31000, model.calculatePriceForRent(model.getClientByPesel("95062910555"), model.getCarByID("AAA1111"), "01-01-2018", "31-01-2018"));
        assertEquals(41000, model.calculatePriceForRent(model.getClientByPesel("95062910555"), model.getCarByID("AAA1111"), "01-01-2018", "10-02-2018"));
        assertEquals(70000, model.calculatePriceForRent(model.getClientByPesel("95062910555"), model.getCarByID("AAA1111"), "01-01-2020", "10-03-2020"));

    // with 5% discount
        model.discount("95062910555", 10001);
        assertEquals(950, model.calculatePriceForRent(model.getClientByPesel("95062910555"), model.getCarByID("AAA1111"), "01-01-2018", "01-01-2018"));
        assertEquals(28500, model.calculatePriceForRent(model.getClientByPesel("95062910555"), model.getCarByID("AAA1111"), "01-01-2018", "30-01-2018"));
        assertEquals(29450, model.calculatePriceForRent(model.getClientByPesel("95062910555"), model.getCarByID("AAA1111"), "01-01-2018", "31-01-2018"));
        assertEquals(38950, model.calculatePriceForRent(model.getClientByPesel("95062910555"), model.getCarByID("AAA1111"), "01-01-2018", "10-02-2018"));
        assertEquals(66500, model.calculatePriceForRent(model.getClientByPesel("95062910555"), model.getCarByID("AAA1111"), "01-01-2020", "10-03-2020"));

        model.discount("95062910555", 20001);
        assertEquals(900, model.calculatePriceForRent(model.getClientByPesel("95062910555"), model.getCarByID("AAA1111"), "01-01-2018", "01-01-2018"));
        assertEquals(27000, model.calculatePriceForRent(model.getClientByPesel("95062910555"), model.getCarByID("AAA1111"), "01-01-2018", "30-01-2018"));
        assertEquals(27900, model.calculatePriceForRent(model.getClientByPesel("95062910555"), model.getCarByID("AAA1111"), "01-01-2018", "31-01-2018"));
        assertEquals(36900, model.calculatePriceForRent(model.getClientByPesel("95062910555"), model.getCarByID("AAA1111"), "01-01-2018", "10-02-2018"));
        assertEquals(63000, model.calculatePriceForRent(model.getClientByPesel("95062910555"), model.getCarByID("AAA1111"), "01-01-2020", "10-03-2020"));

        model.discount("95062910555", 70001);
        assertEquals(750, model.calculatePriceForRent(model.getClientByPesel("95062910555"), model.getCarByID("AAA1111"), "01-01-2018", "01-01-2018"));
        assertEquals(22500, model.calculatePriceForRent(model.getClientByPesel("95062910555"), model.getCarByID("AAA1111"), "01-01-2018", "30-01-2018"));
        assertEquals(23250, model.calculatePriceForRent(model.getClientByPesel("95062910555"), model.getCarByID("AAA1111"), "01-01-2018", "31-01-2018"));
        assertEquals(30750, model.calculatePriceForRent(model.getClientByPesel("95062910555"), model.getCarByID("AAA1111"), "01-01-2018", "10-02-2018"));
        assertEquals(52500, model.calculatePriceForRent(model.getClientByPesel("95062910555"), model.getCarByID("AAA1111"), "01-01-2020", "10-03-2020"));
    }
}