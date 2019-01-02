package model.tests;

import database.DatabaseManager;
import model.enums.Color;
import model.enums.FuelType;
import model.enums.VehicleStatus;
import model.enums.VehicleType;
import model.exceptions.ErrorMessageException;
import model.Logs;
import model.managers.VehiclesManager;
import model.vehicles.Bike;
import model.vehicles.Car;
import model.vehicles.Motorcycle;
import model.vehicles.Vehicle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VehiclesManagerTest {

    static VehiclesManager vehiclesManager;
    @AfterEach
    void closeDatabaseConnection() {
        DatabaseManager.closeAndClearConnection();
    }

    @org.junit.jupiter.api.BeforeAll
    static void setupLogger(){
        new Logs();
        vehiclesManager = new VehiclesManager();
    }

    @org.junit.jupiter.api.BeforeEach
    void setupDatabase() throws ErrorMessageException {
        DatabaseManager.connect();
    }

    @Test
    void getAllAvaiableVehicles() throws ErrorMessageException {
        Car car1 = new Car("a", VehicleStatus.avaiable, VehicleType.car,"nameCar1", 1111, Color.zielony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        Car car2 = new Car("b", VehicleStatus.avaiable, VehicleType.car,"nameCar2", 1111, Color.zielony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        Bike bike1 = new Bike("c",  VehicleStatus.avaiable, VehicleType.bike, "bike1", 1111.11, Color.zielony, 2000);
        Bike bike2 = new Bike("d",  VehicleStatus.avaiable, VehicleType.bike, "bike2", 1111.11, Color.zielony, 2000);
        Motorcycle motorcycl1 = new Motorcycle("e",  VehicleStatus.avaiable,VehicleType.motorcycle,"XXX19191", 1111.01, Color.zielony, 2000, 300000, 2.5,9.9);
        Motorcycle motorcycl2 = new Motorcycle("f",  VehicleStatus.avaiable,VehicleType.motorcycle,"XXX19192", 1111.01, Color.zielony, 2000, 300000, 2.5,9.9);
        String response4 = "{id = b, nazwa = nameCar2, kolor = zielony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.0, paliwo = diesel, pojemność silnika = 1.9, przejechane kilometry = 2999998, zużycie paliwa = 5.7, liczba osób = 5}";
        String response5 = "{id = d, nazwa = bike2, kolor = zielony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.11}";
        String response6 = "{id = f, nazwa = XXX19192, kolor = zielony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.01, przejechane kilometry = 300000, pojemność silnika = 2.5, zużycie paliwa = 9.9}";

        vehiclesManager.addVehicle(car1);
        vehiclesManager.addVehicle(car2);
        vehiclesManager.addVehicle(bike1);
        vehiclesManager.addVehicle(bike2);
        vehiclesManager.addVehicle(motorcycl1);
        vehiclesManager.addVehicle(motorcycl2);
        vehiclesManager.archiveVehicle(car1);
        vehiclesManager.archiveVehicle(bike1);
        vehiclesManager.archiveVehicle(motorcycl1);

        List<Vehicle> vehicleList = vehiclesManager.getAllAvaiableVehicles();

        assertEquals(3,vehicleList.size());
        assertEquals(response4, vehicleList.get(0).toString());
        assertEquals(response5, vehicleList.get(1).toString());
        assertEquals(response6, vehicleList.get(2).toString());
    }

    @Test
    void getAllVehicles() throws ErrorMessageException {
        Car car1 = new Car("a", VehicleStatus.avaiable, VehicleType.car,"nameCar1", 1111, Color.zielony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        Car car2 = new Car("b", VehicleStatus.avaiable, VehicleType.car,"nameCar2", 1111, Color.zielony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        Bike bike1 = new Bike("c",  VehicleStatus.avaiable, VehicleType.bike, "bike1", 1111.11, Color.zielony, 2000);
        Bike bike2 = new Bike("d",  VehicleStatus.avaiable, VehicleType.bike, "bike2", 1111.11, Color.zielony, 2000);
        Motorcycle motorcycl1 = new Motorcycle("e",  VehicleStatus.avaiable,VehicleType.motorcycle,"XXX19191", 1111.01, Color.zielony, 2000, 300000, 2.5,9.9);
        Motorcycle motorcycl2 = new Motorcycle("f",  VehicleStatus.avaiable,VehicleType.motorcycle,"XXX19192", 1111.01, Color.zielony, 2000, 300000, 2.5,9.9);
        String response1 = "{id = a, nazwa = nameCar1, kolor = zielony, rok produkcji = 2000, dostepnosc = archived, cena za dzien = 1111.0, paliwo = diesel, pojemność silnika = 1.9, przejechane kilometry = 2999998, zużycie paliwa = 5.7, liczba osób = 5}";
        String response2 = "{id = c, nazwa = bike1, kolor = zielony, rok produkcji = 2000, dostepnosc = archived, cena za dzien = 1111.11}";
        String response3 = "{id = e, nazwa = XXX19191, kolor = zielony, rok produkcji = 2000, dostepnosc = archived, cena za dzien = 1111.01, przejechane kilometry = 300000, pojemność silnika = 2.5, zużycie paliwa = 9.9}";
        String response4 = "{id = b, nazwa = nameCar2, kolor = zielony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.0, paliwo = diesel, pojemność silnika = 1.9, przejechane kilometry = 2999998, zużycie paliwa = 5.7, liczba osób = 5}";
        String response5 = "{id = d, nazwa = bike2, kolor = zielony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.11}";
        String response6 = "{id = f, nazwa = XXX19192, kolor = zielony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.01, przejechane kilometry = 300000, pojemność silnika = 2.5, zużycie paliwa = 9.9}";

        vehiclesManager.addVehicle(car1);
        vehiclesManager.addVehicle(car2);
        vehiclesManager.addVehicle(bike1);
        vehiclesManager.addVehicle(bike2);
        vehiclesManager.addVehicle(motorcycl1);
        vehiclesManager.addVehicle(motorcycl2);
        vehiclesManager.archiveVehicle(car1);
        vehiclesManager.archiveVehicle(bike1);
        vehiclesManager.archiveVehicle(motorcycl1);

        List<Vehicle> vehicleList = vehiclesManager.getAllVehicles();
        assertEquals(6,vehicleList.size());
        assertEquals(response4,vehicleList.get(0).toString());
        assertEquals(response1,vehicleList.get(1).toString());
        assertEquals(response5,vehicleList.get(2).toString());
        assertEquals(response2,vehicleList.get(3).toString());
        assertEquals(response6,vehicleList.get(4).toString());
        assertEquals(response3,vehicleList.get(5).toString());
    }

    @Test
    void addVehicle() throws ErrorMessageException {
        Car car1 = new Car("a", VehicleStatus.avaiable, VehicleType.car,"nameCar1", 1111, Color.czerwony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        Car car2 = new Car("b", VehicleStatus.avaiable, VehicleType.car,"nameCar2", 1111, Color.czerwony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        Bike bike1 = new Bike("c",  VehicleStatus.avaiable, VehicleType.bike, "bike1", 1111.11, Color.czerwony, 2000);
        Bike bike2 = new Bike("d",  VehicleStatus.avaiable, VehicleType.bike, "bike2", 1111.11, Color.czerwony, 2000);
        Motorcycle motorcycl1 = new Motorcycle("e",  VehicleStatus.avaiable,VehicleType.motorcycle,"XXX19191", 1111.01, Color.czerwony, 2000, 300000, 2.5,9.9);
        Motorcycle motorcycl2 = new Motorcycle("f",  VehicleStatus.avaiable,VehicleType.motorcycle,"XXX19192", 1111.01, Color.czerwony, 2000, 300000, 2.5,9.9);
        String response1 = "{id = a, nazwa = nameCar1, kolor = czerwony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.0, paliwo = diesel, pojemność silnika = 1.9, przejechane kilometry = 2999998, zużycie paliwa = 5.7, liczba osób = 5}";
        String response2 = "{id = b, nazwa = nameCar2, kolor = czerwony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.0, paliwo = diesel, pojemność silnika = 1.9, przejechane kilometry = 2999998, zużycie paliwa = 5.7, liczba osób = 5}";
        String response3 = "{id = c, nazwa = bike1, kolor = czerwony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.11}";
        String response4 = "{id = d, nazwa = bike2, kolor = czerwony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.11}";
        String response5 = "{id = e, nazwa = XXX19191, kolor = czerwony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.01, przejechane kilometry = 300000, pojemność silnika = 2.5, zużycie paliwa = 9.9}";
        String response6 = "{id = f, nazwa = XXX19192, kolor = czerwony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.01, przejechane kilometry = 300000, pojemność silnika = 2.5, zużycie paliwa = 9.9}";

        vehiclesManager.addVehicle(car1);
        vehiclesManager.addVehicle(car2);
        vehiclesManager.addVehicle(bike1);
        vehiclesManager.addVehicle(bike2);
        vehiclesManager.addVehicle(motorcycl1);
        vehiclesManager.addVehicle(motorcycl2);

        assertEquals(response1, DatabaseManager.getCarByID("a").toString());
        assertEquals(response2, DatabaseManager.getCarByID("b").toString());
        assertEquals(response3, DatabaseManager.getbikeByID("c").toString());
        assertEquals(response4, DatabaseManager.getbikeByID("d").toString());
        assertEquals(response5, DatabaseManager.getMotorcycleByID("e").toString());
        assertEquals(response6, DatabaseManager.getMotorcycleByID("f").toString());
    }

    @Test
    void editVehicle() throws ErrorMessageException {

        Car car1 = new Car("a", VehicleStatus.avaiable, VehicleType.car,"nameCar1", 1111, Color.czerwony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        Car car2 = new Car("b", VehicleStatus.avaiable, VehicleType.car,"nameCar2", 1111, Color.czerwony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        Bike bike1 = new Bike("c",  VehicleStatus.avaiable, VehicleType.bike, "bike1", 1111.11, Color.czerwony, 2000);
        Bike bike2 = new Bike("d",  VehicleStatus.avaiable, VehicleType.bike, "bike2", 1111.11, Color.czerwony, 2000);
        Motorcycle motorcycl1 = new Motorcycle("e",  VehicleStatus.avaiable,VehicleType.motorcycle,"XXX19191", 1111.01, Color.czerwony, 2000, 300000, 2.5,9.9);
        Motorcycle motorcycl2 = new Motorcycle("f",  VehicleStatus.avaiable,VehicleType.motorcycle,"XXX19192", 1111.01, Color.czerwony, 2000, 300000, 2.5,9.9);

        Car car11 = new Car("a", VehicleStatus.avaiable, VehicleType.car,"nameCar1", 1111, Color.zielony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        Car car22 = new Car("b", VehicleStatus.avaiable, VehicleType.car,"nameCar2", 1111, Color.zielony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        Bike bike11 = new Bike("c",  VehicleStatus.avaiable, VehicleType.bike, "bike1", 1111.11, Color.zielony, 2000);
        Bike bike22 = new Bike("d",  VehicleStatus.avaiable, VehicleType.bike, "bike2", 1111.11, Color.zielony, 2000);
        Motorcycle motorcycl11 = new Motorcycle("e",  VehicleStatus.avaiable,VehicleType.motorcycle,"XXX19191", 1111.01, Color.zielony, 2000, 300000, 2.5,9.9);
        Motorcycle motorcycl22 = new Motorcycle("f",  VehicleStatus.avaiable,VehicleType.motorcycle,"XXX19192", 1111.01, Color.zielony, 2000, 300000, 2.5,9.9);

        String response1 = "{id = a, nazwa = nameCar1, kolor = zielony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.0, paliwo = diesel, pojemność silnika = 1.9, przejechane kilometry = 2999998, zużycie paliwa = 5.7, liczba osób = 5}";
        String response2 = "{id = b, nazwa = nameCar2, kolor = zielony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.0, paliwo = diesel, pojemność silnika = 1.9, przejechane kilometry = 2999998, zużycie paliwa = 5.7, liczba osób = 5}";
        String response3 = "{id = c, nazwa = bike1, kolor = zielony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.11}";
        String response4 = "{id = d, nazwa = bike2, kolor = zielony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.11}";
        String response5 = "{id = e, nazwa = XXX19191, kolor = zielony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.01, przejechane kilometry = 300000, pojemność silnika = 2.5, zużycie paliwa = 9.9}";
        String response6 = "{id = f, nazwa = XXX19192, kolor = zielony, rok produkcji = 2000, dostepnosc = avaiable, cena za dzien = 1111.01, przejechane kilometry = 300000, pojemność silnika = 2.5, zużycie paliwa = 9.9}";

        vehiclesManager.addVehicle(car1);
        vehiclesManager.addVehicle(car2);
        vehiclesManager.addVehicle(bike1);
        vehiclesManager.addVehicle(bike2);
        vehiclesManager.addVehicle(motorcycl1);
        vehiclesManager.addVehicle(motorcycl2);
        vehiclesManager.editVehicle(car11);
        vehiclesManager.editVehicle(car22);
        vehiclesManager.editVehicle(bike11);
        vehiclesManager.editVehicle(bike22);
        vehiclesManager.editVehicle(motorcycl11);
        vehiclesManager.editVehicle(motorcycl22);

        assertEquals(response1, DatabaseManager.getCarByID("a").toString());
        assertEquals(response2, DatabaseManager.getCarByID("b").toString());
        assertEquals(response3, DatabaseManager.getbikeByID("c").toString());
        assertEquals(response4, DatabaseManager.getbikeByID("d").toString());
        assertEquals(response5, DatabaseManager.getMotorcycleByID("e").toString());
        assertEquals(response6, DatabaseManager.getMotorcycleByID("f").toString());
    }

    @Test
    void archiveVehicle() throws ErrorMessageException {
        Car car1 = new Car("a", VehicleStatus.avaiable, VehicleType.car,"nameCar1", 1111, Color.zielony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        Car car2 = new Car("b", VehicleStatus.avaiable, VehicleType.car,"nameCar2", 1111, Color.zielony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        Bike bike1 = new Bike("c",  VehicleStatus.avaiable, VehicleType.bike, "bike1", 1111.11, Color.zielony, 2000);
        Bike bike2 = new Bike("d",  VehicleStatus.avaiable, VehicleType.bike, "bike2", 1111.11, Color.zielony, 2000);
        Motorcycle motorcycl1 = new Motorcycle("e",  VehicleStatus.avaiable,VehicleType.motorcycle,"XXX19191", 1111.01, Color.zielony, 2000, 300000, 2.5,9.9);
        Motorcycle motorcycl2 = new Motorcycle("f",  VehicleStatus.avaiable,VehicleType.motorcycle,"XXX19192", 1111.01, Color.zielony, 2000, 300000, 2.5,9.9);
        String response1 = "{id = a, nazwa = nameCar1, kolor = zielony, rok produkcji = 2000, dostepnosc = archived, cena za dzien = 1111.0, paliwo = diesel, pojemność silnika = 1.9, przejechane kilometry = 2999998, zużycie paliwa = 5.7, liczba osób = 5}";
        String response2 = "{id = c, nazwa = bike1, kolor = zielony, rok produkcji = 2000, dostepnosc = archived, cena za dzien = 1111.11}";
        String response3 = "{id = e, nazwa = XXX19191, kolor = zielony, rok produkcji = 2000, dostepnosc = archived, cena za dzien = 1111.01, przejechane kilometry = 300000, pojemność silnika = 2.5, zużycie paliwa = 9.9}";

        vehiclesManager.addVehicle(car1);
        vehiclesManager.addVehicle(car2);
        vehiclesManager.addVehicle(bike1);
        vehiclesManager.addVehicle(bike2);
        vehiclesManager.addVehicle(motorcycl1);
        vehiclesManager.addVehicle(motorcycl2);
        vehiclesManager.archiveVehicle(car1);
        vehiclesManager.archiveVehicle(bike1);
        vehiclesManager.archiveVehicle(motorcycl1);

        List<Car> archivedCasr = DatabaseManager.getAllAArchivalCars();
        List<Bike> archivedBikes = DatabaseManager.getAllAArchivalBikes();
        List<Motorcycle> archivedMotorcycles = DatabaseManager.getAllAArchivalMotorcycles();

        assertEquals(response1, archivedCasr.get(0).toString());
        assertEquals(1, archivedCasr.size());
        assertEquals(response2, archivedBikes.get(0).toString());
        assertEquals(1, archivedBikes.size());
        assertEquals(response3, archivedMotorcycles.get(0).toString());
        assertEquals(1, archivedMotorcycles.size());
    }

    @Test
    void getFilteredVehicles() throws ErrorMessageException {
        Car car1 = new Car("a", VehicleStatus.avaiable, VehicleType.car,"nameCar1", 1111, Color.zielony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        Car car2 = new Car("b", VehicleStatus.avaiable, VehicleType.car,"nameCar2", 2000, Color.zielony, 2000, 2999998, 1.9, 5.7, FuelType.diesel,4);
        Bike bike1 = new Bike("c",  VehicleStatus.avaiable, VehicleType.bike, "bike1", 1000, Color.zielony, 2000);
        Bike bike2 = new Bike("d",  VehicleStatus.avaiable, VehicleType.bike, "bike2", 999, Color.zielony, 2000);
        Motorcycle motorcycl1 = new Motorcycle("e",  VehicleStatus.avaiable,VehicleType.motorcycle,"XXX19191", 500, Color.zielony, 2000, 300000, 2.5,9.9);
        Motorcycle motorcycl2 = new Motorcycle("f",  VehicleStatus.avaiable,VehicleType.motorcycle,"XXX19192", 300, Color.zielony, 2000, 300000, 2.5,9.9);
        vehiclesManager.addVehicle(car1);
        vehiclesManager.addVehicle(car2);
        vehiclesManager.addVehicle(bike1);
        vehiclesManager.addVehicle(bike2);
        vehiclesManager.addVehicle(motorcycl1);
        vehiclesManager.addVehicle(motorcycl2);
        vehiclesManager.archiveVehicle(car1);
        vehiclesManager.archiveVehicle(bike1);
        vehiclesManager.archiveVehicle(motorcycl1);

        List<Vehicle> vehicleList = new ArrayList<>();
        vehicleList =  vehiclesManager.getFilteredVehicles("nameCar", null, null, null,-1,-1,
                        null, null, -1, -1,
                        null, null, -1, -1, null,
                        null, -1, -1,null,
                        null,null, -1, -1, -1);
        assertEquals(2, vehicleList.size());
        assertEquals(true,  vehicleList.get(0) instanceof Car);
        assertEquals(true,  vehicleList.get(1) instanceof Car);


        vehicleList =  vehiclesManager.getFilteredVehicles("nameCar", null, null, null,-1,-1,
                null, null, -1, -1,
                null, null, -1, -1, null,
                null, -1, -1,null,
                null,null, -1, -1, 4);
        assertEquals(1, vehicleList.size());
        assertEquals(true,  vehicleList.get(0) instanceof Car);


        vehicleList =  vehiclesManager.getFilteredVehicles(null, null, ">", null,1000,-1,
                null, null, -1, -1,
                null, null, -1, -1, null,
                null, -1, -1,null,
                null,null, -1, -1, -1);
        assertEquals(2, vehicleList.size());
        assertEquals(true,  vehicleList.get(0) instanceof Car);
        assertEquals(true,  vehicleList.get(1) instanceof Car);

        vehicleList =  vehiclesManager.getFilteredVehicles(null, null, "<", null,1000,-1,
                null, null, -1, -1,
                null, null, -1, -1, null,
                null, -1, -1,null,
                null,null, -1, -1, -1);
        assertEquals(3, vehicleList.size());
        assertEquals(true,  vehicleList.get(0) instanceof Motorcycle);
        assertEquals(true,  vehicleList.get(1) instanceof Motorcycle);
        assertEquals(true,  vehicleList.get(2) instanceof Bike);

        vehicleList =  vehiclesManager.getFilteredVehicles(null, null, "<=", null,1000,-1,
                null, null, -1, -1,
                null, null, -1, -1, null,
                null, -1, -1,null,
                null,null, -1, -1, -1);
        assertEquals(4, vehicleList.size());
        assertEquals(true,  vehicleList.get(0) instanceof Motorcycle);
        assertEquals(true,  vehicleList.get(1) instanceof Motorcycle);
        assertEquals(true,  vehicleList.get(2) instanceof Bike);
        assertEquals(true,  vehicleList.get(3) instanceof Bike);

        vehicleList =  vehiclesManager.getFilteredVehicles(null, null, ">=", "<",1000,1500,
                null, null, -1, -1,
                null, null, -1, -1, null,
                null, -1, -1,null,
                null,null, -1, -1, -1);
        assertEquals(2, vehicleList.size());
        assertEquals(true,  vehicleList.get(0) instanceof Car);
        assertEquals(true,  vehicleList.get(1) instanceof Bike);
    }
}