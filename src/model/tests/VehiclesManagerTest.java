package model.tests;

import database.DatabaseManager;
import model.Customer;
import model.Employee;
import model.enums.FuelType;
import model.enums.VehicleStatus;
import model.enums.VehicleType;
import model.exceptions.ErrorMessageException;
import model.Logs;
import model.managers.RentsManager;
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
    static void setupLogger() {
        new Logs();
        vehiclesManager = new VehiclesManager();
    }

    @org.junit.jupiter.api.BeforeEach
    void setupDatabase() throws ErrorMessageException {
        DatabaseManager.connect();
    }

    @Test
    void getAllAvaiableVehicles() throws ErrorMessageException {
        Car car1 = new Car("a", VehicleStatus.avaiable, VehicleType.car, "nameCar1", 1111, "zielony", 2000, 2999998, 1.9, 5.7, FuelType.diesel, 5);
        Car car2 = new Car("b", VehicleStatus.avaiable, VehicleType.car, "nameCar2", 1111, "zielony", 2000, 2999998, 1.9, 5.7, FuelType.diesel, 5);
        Bike bike1 = new Bike("c", VehicleStatus.avaiable, VehicleType.bike, "bike1", 1111.11, "zielony", 2000);
        Bike bike2 = new Bike("d", VehicleStatus.avaiable, VehicleType.bike, "bike2", 1111.11, "zielony", 2000);
        Motorcycle motorcycl1 = new Motorcycle("e", VehicleStatus.avaiable, VehicleType.motorcycle, "XXX19191", 1111.01, "zielony", 2000, 300000, 2.5, 9.9);
        Motorcycle motorcycl2 = new Motorcycle("f", VehicleStatus.avaiable, VehicleType.motorcycle, "XXX19192", 1111.01, "zielony", 2000, 300000, 2.5, 9.9);

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

        assertEquals(3, vehicleList.size());
        assertEquals("nameCar2", vehicleList.get(0).toString());
        assertEquals("bike2", vehicleList.get(1).toString());
        assertEquals("XXX19192", vehicleList.get(2).toString());
    }

    @Test
    void getAllVehicles() throws ErrorMessageException {
        Car car1 = new Car("a", VehicleStatus.avaiable, VehicleType.car, "nameCar1", 1111, "zielony", 2000, 2999998, 1.9, 5.7, FuelType.diesel, 5);
        Car car2 = new Car("b", VehicleStatus.avaiable, VehicleType.car, "nameCar2", 1111, "zielony", 2000, 2999998, 1.9, 5.7, FuelType.diesel, 5);
        Bike bike1 = new Bike("c", VehicleStatus.avaiable, VehicleType.bike, "bike1", 1111.11, "zielony", 2000);
        Bike bike2 = new Bike("d", VehicleStatus.avaiable, VehicleType.bike, "bike2", 1111.11, "zielony", 2000);
        Motorcycle motorcycl1 = new Motorcycle("e", VehicleStatus.avaiable, VehicleType.motorcycle, "XXX19191", 1111.01, "zielony", 2000, 300000, 2.5, 9.9);
        Motorcycle motorcycl2 = new Motorcycle("f", VehicleStatus.avaiable, VehicleType.motorcycle, "XXX19192", 1111.01, "zielony", 2000, 300000, 2.5, 9.9);



        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres",888777666, "email1", "1", "", "", false);
        DatabaseManager.addCustomer("Dawid", "car", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress1");
        Employee employee = new Employee(1,"Dawidziak", "Dawidziak", "addres",888777666, "email1", null);
        Customer customer = new Customer("Dawid", "Janiak", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress",0.0);
        vehiclesManager.addVehicle(car1);
        vehiclesManager.addVehicle(car2);
        vehiclesManager.addVehicle(bike1);
        vehiclesManager.addVehicle(bike2);
        vehiclesManager.addVehicle(motorcycl1);
        vehiclesManager.addVehicle(motorcycl2);
        RentsManager rentsManager = new RentsManager();
        rentsManager.addRent(car1, customer, employee, java.sql.Date.valueOf("2018-10-10"), java.sql.Date.valueOf("2018-10-11"));
        rentsManager.addRent(bike1, customer, employee, java.sql.Date.valueOf("2018-10-10"), java.sql.Date.valueOf("2018-10-11"));
        rentsManager.addRent(motorcycl1, customer, employee, java.sql.Date.valueOf("2018-10-10"), java.sql.Date.valueOf("2018-10-11"));

        List<Vehicle> vehicleList = vehiclesManager.getAllVehicles();
        assertEquals(6, vehicleList.size());
        assertEquals("nameCar2", vehicleList.get(0).toString());
        assertEquals("nameCar1", vehicleList.get(1).toString());
        assertEquals("bike2", vehicleList.get(2).toString());
        assertEquals("bike1", vehicleList.get(3).toString());
        assertEquals("XXX19192", vehicleList.get(4).toString());
        assertEquals("XXX19191", vehicleList.get(5).toString());
    }

    @Test
    void addVehicle() throws ErrorMessageException {
        Car car1 = new Car("a", VehicleStatus.avaiable, VehicleType.car, "nameCar1", 1111, "czerwony", 2000, 2999998, 1.9, 5.7, FuelType.diesel, 5);
        Car car2 = new Car("b", VehicleStatus.avaiable, VehicleType.car, "nameCar2", 1111, "czerwony", 2000, 2999998, 1.9, 5.7, FuelType.diesel, 5);
        Bike bike1 = new Bike("c", VehicleStatus.avaiable, VehicleType.bike, "bike1", 1111.11, "czerwony", 2000);
        Bike bike2 = new Bike("d", VehicleStatus.avaiable, VehicleType.bike, "bike2", 1111.11, "czerwony", 2000);
        Motorcycle motorcycl1 = new Motorcycle("e", VehicleStatus.avaiable, VehicleType.motorcycle, "XXX19191", 1111.01, "czerwony", 2000, 300000, 2.5, 9.9);
        Motorcycle motorcycl2 = new Motorcycle("f", VehicleStatus.avaiable, VehicleType.motorcycle, "XXX19192", 1111.01, "czerwony", 2000, 300000, 2.5, 9.9);

        vehiclesManager.addVehicle(car1);
        vehiclesManager.addVehicle(car2);
        vehiclesManager.addVehicle(bike1);
        vehiclesManager.addVehicle(bike2);
        vehiclesManager.addVehicle(motorcycl1);
        vehiclesManager.addVehicle(motorcycl2);

        assertEquals(car1.toString(), DatabaseManager.getCarByID("a").toString());
        assertEquals(car2.toString(), DatabaseManager.getCarByID("b").toString());
        assertEquals(bike1.toString(), DatabaseManager.getbikeByID("c").toString());
        assertEquals(bike2.toString(), DatabaseManager.getbikeByID("d").toString());
        assertEquals(motorcycl1.toString(), DatabaseManager.getMotorcycleByID("e").toString());
        assertEquals(motorcycl2.toString(), DatabaseManager.getMotorcycleByID("f").toString());
    }

    @Test
    void editVehicle() throws ErrorMessageException {

        Car car1 = new Car("a", VehicleStatus.avaiable, VehicleType.car, "nameCar1", 1111, "czerwony", 2000, 2999998, 1.9, 5.7, FuelType.diesel, 5);
        Car car2 = new Car("b", VehicleStatus.avaiable, VehicleType.car, "nameCar2", 1111, "czerwony", 2000, 2999998, 1.9, 5.7, FuelType.diesel, 5);
        Bike bike1 = new Bike("c", VehicleStatus.avaiable, VehicleType.bike, "bike1", 1111.11, "czerwony", 2000);
        Bike bike2 = new Bike("d", VehicleStatus.avaiable, VehicleType.bike, "bike2", 1111.11, "czerwony", 2000);
        Motorcycle motorcycl1 = new Motorcycle("e", VehicleStatus.avaiable, VehicleType.motorcycle, "XXX19191", 1111.01, "czerwony", 2000, 300000, 2.5, 9.9);
        Motorcycle motorcycl2 = new Motorcycle("f", VehicleStatus.avaiable, VehicleType.motorcycle, "XXX19192", 1111.01, "czerwony", 2000, 300000, 2.5, 9.9);

        Car car11 = new Car("a", VehicleStatus.avaiable, VehicleType.car, "nameCar1", 1111, "zielony", 2000, 2999998, 1.9, 5.7, FuelType.diesel, 5);
        Car car22 = new Car("b", VehicleStatus.avaiable, VehicleType.car, "nameCar2", 1111, "zielony", 2000, 2999998, 1.9, 5.7, FuelType.diesel, 5);
        Bike bike11 = new Bike("c", VehicleStatus.avaiable, VehicleType.bike, "bike1", 1111.11, "zielony", 2000);
        Bike bike22 = new Bike("d", VehicleStatus.avaiable, VehicleType.bike, "bike2", 1111.11, "zielony", 2000);
        Motorcycle motorcycl11 = new Motorcycle("e", VehicleStatus.avaiable, VehicleType.motorcycle, "XXX19191", 1111.01, "zielony", 2000, 300000, 2.5, 9.9);
        Motorcycle motorcycl22 = new Motorcycle("f", VehicleStatus.avaiable, VehicleType.motorcycle, "XXX19192", 1111.01, "zielony", 2000, 300000, 2.5, 9.9);

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

        assertEquals(car1.toString(), DatabaseManager.getCarByID("a").toString());
        assertEquals("zielony", DatabaseManager.getCarByID("a").getColor());

        assertEquals(car2.toString(), DatabaseManager.getCarByID("b").toString());
        assertEquals("zielony", DatabaseManager.getCarByID("b").getColor());

        assertEquals(bike1.toString(), DatabaseManager.getbikeByID("c").toString());
        assertEquals("zielony", DatabaseManager.getbikeByID("c").getColor());

        assertEquals(bike2.toString(), DatabaseManager.getbikeByID("d").toString());
        assertEquals("zielony", DatabaseManager.getbikeByID("d").getColor());

        assertEquals(motorcycl1.toString(), DatabaseManager.getMotorcycleByID("e").toString());
        assertEquals("zielony", DatabaseManager.getMotorcycleByID("e").getColor());

        assertEquals(motorcycl2.toString(), DatabaseManager.getMotorcycleByID("f").toString());
        assertEquals("zielony", DatabaseManager.getMotorcycleByID("f").getColor());
    }

    @Test
    void archiveVehicle() throws ErrorMessageException {
        Car car1 = new Car("a", VehicleStatus.avaiable, VehicleType.car, "nameCar1", 1111, "zielony", 2000, 2999998, 1.9, 5.7, FuelType.diesel, 5);
        Car car2 = new Car("b", VehicleStatus.avaiable, VehicleType.car, "nameCar2", 1111, "zielony", 2000, 2999998, 1.9, 5.7, FuelType.diesel, 5);
        Bike bike1 = new Bike("c", VehicleStatus.avaiable, VehicleType.bike, "bike1", 1111.11, "zielony", 2000);
        Bike bike2 = new Bike("d", VehicleStatus.avaiable, VehicleType.bike, "bike2", 1111.11, "zielony", 2000);
        Motorcycle motorcycl1 = new Motorcycle("e", VehicleStatus.avaiable, VehicleType.motorcycle, "XXX19191", 1111.01, "zielony", 2000, 300000, 2.5, 9.9);
        Motorcycle motorcycl2 = new Motorcycle("f", VehicleStatus.avaiable, VehicleType.motorcycle, "XXX19192", 1111.01, "zielony", 2000, 300000, 2.5, 9.9);

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

        assertEquals("nameCar1", archivedCasr.get(0).toString());
        assertEquals(1, archivedCasr.size());
        assertEquals("bike1", archivedBikes.get(0).toString());
        assertEquals(1, archivedBikes.size());
        assertEquals("XXX19191", archivedMotorcycles.get(0).toString());
        assertEquals(1, archivedMotorcycles.size());
    }

    @Test
    void getFilteredVehicles() throws ErrorMessageException {
        Car car1 = new Car("a", VehicleStatus.avaiable, VehicleType.car, "nameCar1", 1111, "zielony", 2000, 2999998, 1.9, 5.7, FuelType.diesel, 5);
        Car car2 = new Car("b", VehicleStatus.avaiable, VehicleType.car, "nameCar2", 2000, "zielony", 2000, 2999998, 1.9, 5.7, FuelType.diesel, 4);
        Bike bike1 = new Bike("c", VehicleStatus.avaiable, VehicleType.bike, "bike1", 1000, "zielony", 2000);
        Bike bike2 = new Bike("d", VehicleStatus.avaiable, VehicleType.bike, "bike2", 999, "zielony", 2000);
        Motorcycle motorcycl1 = new Motorcycle("e", VehicleStatus.avaiable, VehicleType.motorcycle, "XXX19191", 500, "zielony", 2000, 300000, 2.5, 9.9);
        Motorcycle motorcycl2 = new Motorcycle("f", VehicleStatus.avaiable, VehicleType.motorcycle, "XXX19192", 300, "zielony", 2000, 300000, 2.5, 9.9);
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
        vehicleList = vehiclesManager.getFilteredVehicles("nameCar", null, null, null, -1, -1,
                null, null, -1, -1,
                null, null, -1, -1, null,
                null, -1, -1, null,
                null, null, -1, -1, -1, null, null, null);
        assertEquals(2, vehicleList.size());
        assertEquals(true, vehicleList.get(0) instanceof Car);
        assertEquals(true, vehicleList.get(1) instanceof Car);


        vehicleList = vehiclesManager.getFilteredVehicles("nameCar", null, null, null, -1, -1,
                null, null, -1, -1,
                null, null, -1, -1, null,
                null, -1, -1, null,
                null, null, -1, -1, 4, null, null, null);
        assertEquals(1, vehicleList.size());
        assertEquals(true, vehicleList.get(0) instanceof Car);


        vehicleList = vehiclesManager.getFilteredVehicles(null, null, ">", null, 1000, -1,
                null, null, -1, -1,
                null, null, -1, -1, null,
                null, -1, -1, null,
                null, null, -1, -1, -1, null, null, null);
        assertEquals(2, vehicleList.size());
        assertEquals(true, vehicleList.get(0) instanceof Car);
        assertEquals(true, vehicleList.get(1) instanceof Car);

        vehicleList = vehiclesManager.getFilteredVehicles(null, null, "<", null, 1000, -1,
                null, null, -1, -1,
                null, null, -1, -1, null,
                null, -1, -1, null,
                null, null, -1, -1, -1, null, null, null);
        assertEquals(3, vehicleList.size());
        assertEquals(true, vehicleList.get(0) instanceof Motorcycle);
        assertEquals(true, vehicleList.get(1) instanceof Motorcycle);
        assertEquals(true, vehicleList.get(2) instanceof Bike);

        vehicleList = vehiclesManager.getFilteredVehicles(null, null, "<=", null, 1000, -1,
                null, null, -1, -1,
                null, null, -1, -1, null,
                null, -1, -1, null,
                null, null, -1, -1, -1, null, null, null);
        assertEquals(4, vehicleList.size());
        assertEquals(true, vehicleList.get(0) instanceof Motorcycle);
        assertEquals(true, vehicleList.get(1) instanceof Motorcycle);
        assertEquals(true, vehicleList.get(2) instanceof Bike);
        assertEquals(true, vehicleList.get(3) instanceof Bike);

        vehicleList = vehiclesManager.getFilteredVehicles(null, null, ">=", "<", 1000, 1500,
                null, null, -1, -1,
                null, null, -1, -1, null,
                null, -1, -1, null,
                null, null, -1, -1, -1, null, null, null);
        assertEquals(2, vehicleList.size());

        vehicleList = vehiclesManager.getFilteredVehicles(null, null, null, null, -1, -1,
                null, null, -1, -1,
                null, null, -1, -1, null,
                null, -1, -1, null,
                null, null, -1, -1, -1, null, VehicleType.car, null);
        assertEquals(2, vehicleList.size());
        assertEquals(true, vehicleList.get(0) instanceof Car);
        assertEquals(true, vehicleList.get(1) instanceof Car);

        vehicleList = vehiclesManager.getFilteredVehicles(null, null, null, null, -1, -1,
                null, null, -1, -1,
                null, null, -1, -1, null,
                null, -1, -1, null,
                null, null, -1, -1, -1, null, VehicleType.bike, null);
        assertEquals(2, vehicleList.size());
        assertEquals(true, vehicleList.get(0) instanceof Bike);
        assertEquals(true, vehicleList.get(1) instanceof Bike);

        vehicleList = vehiclesManager.getFilteredVehicles(null, null, null, null, -1, -1,
                null, null, -1, -1,
                null, null, -1, -1, null,
                null, -1, -1, null,
                null, null, -1, -1, -1, null, VehicleType.motorcycle, null);
        assertEquals(2, vehicleList.size());
        assertEquals(true, vehicleList.get(0) instanceof Motorcycle);
        assertEquals(true, vehicleList.get(1) instanceof Motorcycle);

        vehicleList = vehiclesManager.getFilteredVehicles(null, null, null, null, -1, -1,
                null, null, -1, -1,
                null, null, -1, -1, null,
                null, -1, -1, null,
                null, null, -1, -1, -1, "e", null, null);
        assertEquals(1, vehicleList.size());
        assertEquals(true, vehicleList.get(0) instanceof Motorcycle);

        vehicleList = vehiclesManager.getFilteredVehicles(null, null, null, null, -1, -1,
                null, null, -1, -1,
                null, null, -1, -1, null,
                null, -1, -1, null,
                null, null, -1, -1, -1, null, null, VehicleStatus.avaiable);
        assertEquals(3, vehicleList.size());

        vehicleList = vehiclesManager.getFilteredVehicles(null, null, null, null, -1, -1,
                null, null, -1, -1,
                null, null, -1, -1, null,
                null, -1, -1, null,
                null, null, -1, -1, -1, null, null, VehicleStatus.archived);
        assertEquals(3, vehicleList.size());
        assertEquals(true, vehicleList.get(0) instanceof Car);
        assertEquals(true, vehicleList.get(1) instanceof Motorcycle);
        assertEquals(true, vehicleList.get(2) instanceof Bike);
    }

    @Test
    void addTwoSameVehicles() throws ErrorMessageException {
        Car car1 = new Car("a", VehicleStatus.avaiable, VehicleType.car, "nameCar1", 1111, "zielony", 2000, 2999998, 1.9, 5.7, FuelType.diesel, 5);
        Car car2 = new Car("a", VehicleStatus.avaiable, VehicleType.car, "nameCar2", 2000, "zielony", 2000, 2999998, 1.9, 5.7, FuelType.diesel, 4);
        Bike bike1 = new Bike("b", VehicleStatus.avaiable, VehicleType.bike, "bike1", 1000, "zielony", 2000);
        Bike bike2 = new Bike("b", VehicleStatus.avaiable, VehicleType.bike, "bike2", 999, "zielony", 2000);
        Motorcycle motorcycl1 = new Motorcycle("c", VehicleStatus.avaiable, VehicleType.motorcycle, "XXX19191", 500, "zielony", 2000, 300000, 2.5, 9.9);
        Motorcycle motorcycl2 = new Motorcycle("c", VehicleStatus.avaiable, VehicleType.motorcycle, "XXX19192", 300, "zielony", 2000, 300000, 2.5, 9.9);

        vehiclesManager.addVehicle(car1);
        try {
            vehiclesManager.addVehicle(car2);
            assertEquals(true, false);
        } catch (ErrorMessageException e) {
            assertEquals("Pojazd o podanym numerze rejestracyjnym już istnieje.", e.getMessage());
        }

        vehiclesManager.addVehicle(bike1);
        try {
            vehiclesManager.addVehicle(bike2);
            assertEquals(true, false);
        } catch (ErrorMessageException e) {
            assertEquals("Pojazd o podanym numerze rejestracyjnym już istnieje.", e.getMessage());
        }

        vehiclesManager.addVehicle(motorcycl1);
        try {
            vehiclesManager.addVehicle(motorcycl2);
            assertEquals(true, false);
        } catch (ErrorMessageException e) {
            assertEquals("Pojazd o podanym numerze rejestracyjnym już istnieje.", e.getMessage());
        }
    }
}