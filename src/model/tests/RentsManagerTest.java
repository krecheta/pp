package model.tests;

import database.DatabaseManager;
import model.enums.FuelType;
import model.enums.RentStatus;
import model.enums.VehicleStatus;
import model.enums.VehicleType;
import model.exceptions.ErrorMessageException;
import model.Customer;
import model.Employee;
import model.Logs;
import model.managers.RentsManager;
import model.Rent;
import model.vehicles.Bike;
import model.vehicles.Car;
import model.vehicles.Motorcycle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres",888777666, "email1", "1", "", "", false);
        DatabaseManager.addCustomer("Dawid", "car", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress1");
        DatabaseManager.addCar("a", "nameCar", 1111, "czerwony", 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        DatabaseManager.addBike("b", "bike", 1111.11, "czerwony", 2000);
        DatabaseManager.addMotorcycle("c", "XXX1919", 1111.01, "czerwony", 2000, 300000, 2.5,9.9);
        Employee employee = new Employee(1,"Dawidziak", "Dawidziak", "addres",888777666, "email1", null);
        Customer customer = new Customer("Dawid", "Janiak", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress",0.0);
        Car car = new Car("a", VehicleStatus.avaiable, VehicleType.car,"nameCar", 1111, "czerwony", 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        Bike bike = new Bike("b",  VehicleStatus.avaiable, VehicleType.bike, "bike", 1111.11, "czerwony", 2000);
        Motorcycle motorcycl= new Motorcycle("c",  VehicleStatus.avaiable,VehicleType.motorcycle,"XXX1919", 1111.01, "czerwony", 2000, 300000, 2.5,9.9);
        String response1 = "Wypożyczenie{Samochód = nameCar, klient = Dawid car, koszt wypożyczenia = 2222.0, data wypożyczenia = 2018-10-10, planowana data zwrotu = 2018-10-11}";
        String response2 = "Wypożyczenie{Rower = bike, klient = Dawid car, koszt wypożyczenia = 2222.22, data wypożyczenia = 2018-10-10, planowana data zwrotu = 2018-10-11}";
        String response3 = "Wypożyczenie{Motocykl = XXX1919, klient = Dawid car, koszt wypożyczenia = 2222.02, data wypożyczenia = 2018-10-10, planowana data zwrotu = 2018-10-11}";

        rentsManager.addRent(car, customer, employee, parseDate("2018-10-10"), parseDate("2018-10-11"));
        rentsManager.addRent(bike, customer, employee, parseDate("2018-10-10"), parseDate("2018-10-11"));
        rentsManager.addRent(motorcycl, customer, employee, parseDate("2018-10-10"), parseDate("2018-10-11"));

        try {
            rentsManager.addRent(car, customer, employee, parseDate("2018-10-10"), parseDate("2018-10-11"));
            assertEquals(false,true);
        }catch(ErrorMessageException e){
            assertEquals(e.getMessage(),"Pojazd jest niedostepny");
        }
        try {
            rentsManager.addRent(bike, customer, employee, parseDate("2018-10-10"),parseDate("2018-10-11"));
            assertEquals(false,true);
        }catch(ErrorMessageException e){
            assertEquals(e.getMessage(),"Pojazd jest niedostepny");
        }
        try {
            rentsManager.addRent(motorcycl, customer, employee, parseDate("2018-10-10"), parseDate("2018-10-11"));
            assertEquals(false,true);
        }catch(ErrorMessageException e){
            assertEquals(e.getMessage(),"Pojazd jest niedostepny");
        }

        List<Rent> rents = DatabaseManager.getAllDuringRents();

       assertEquals(3, rents.size());
       assertEquals(response1, rents.get(0).toString());
       assertEquals(response2, rents.get(1).toString());
       assertEquals(response3, rents.get(2).toString());
    }

    @Test
    void addRent2() throws ErrorMessageException {
        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres",888777666, "email1", "1", "", "", false);
        DatabaseManager.addCustomer("Dawid", "car", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress1");
        DatabaseManager.addCar("a", "nameCar", 1010, "czerwony", 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        DatabaseManager.addBike("b", "bike", 1111.11, "czerwony", 2000);
        DatabaseManager.addMotorcycle("c", "XXX1919", 1111.01, "czerwony", 2000, 300000, 2.5,9.9);
        Employee employee = new Employee(1,"Dawidziak", "Dawidziak", "addres",888777666, "email1", null);
        Customer customer = new Customer("Dawid", "Janiak", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress",0.0);
        Car car = new Car("a", VehicleStatus.avaiable, VehicleType.car,"nameCar", 1010, "czerwony", 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        Bike bike = new Bike("b",  VehicleStatus.avaiable, VehicleType.bike, "bike", 1111.11, "czerwony", 2000);
        Motorcycle motorcycl= new Motorcycle("c",  VehicleStatus.avaiable,VehicleType.motorcycle,"XXX1919", 1111.01, "czerwony", 2000, 300000, 2.5,9.9);
        String response1 = "Wypożyczenie{Samochód = nameCar, klient = Dawid car, koszt wypożyczenia = 2222.0, data wypożyczenia = 2018-10-10, planowana data zwrotu = 2018-10-11}";
        String response2 = "Wypożyczenie{Rower = bike, klient = Dawid car, koszt wypożyczenia = 2222.22, data wypożyczenia = 2018-10-10, planowana data zwrotu = 2018-10-11}";
        String response3 = "Wypożyczenie{Motocykl = XXX1919, klient = Dawid car, koszt wypożyczenia = 2222.02, data wypożyczenia = 2018-10-10, planowana data zwrotu = 2018-10-11}";

        rentsManager.addRent(car, customer, employee, parseDate("2019-01-04"), parseDate("2019-01-04") );
        Rent rent = rentsManager.getRentByRentID(1);
       // rentsManager.endRent(rent, 0, 0);
    }


    @Test
    void checkCalculatepriceForRent() throws ErrorMessageException {
        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres",888777666, "email1", "1", "", "", false);
        DatabaseManager.addCustomer("Dawid", "car", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress1");
        DatabaseManager.addCar("a", "nameCar", 550.55, "czerwony", 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        DatabaseManager.addBike("b", "bike", 998.11, "czerwony", 2000);
        DatabaseManager.addMotorcycle("c", "XXX1919", 123.77, "czerwony", 2000, 300000, 2.5,9.9);
        Employee employee = new Employee(1,"Dawidziak", "Dawidziak", "addres",888777666, "email1", null);
        Customer customer = new Customer("Dawid", "Janiak", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656","companyAdress",0.0);
        Car car = new Car("a", VehicleStatus.avaiable, VehicleType.car,"nameCar", 1111, "czerwony", 2000, 2999998, 1.9, 5.7, FuelType.diesel,5);
        Bike bike = new Bike("b",  VehicleStatus.avaiable, VehicleType.bike, "bike", 1111.11, "czerwony", 2000);
        Motorcycle motorcycl= new Motorcycle("c",  VehicleStatus.avaiable,VehicleType.motorcycle,"XXX1919", 1111.01, "czerwony", 2000, 300000, 2.5,9.9);

        rentsManager.addRent(car, customer, employee, parseDate("2018-10-10"), parseDate("2018-10-19"));
        rentsManager.addRent(bike, customer, employee,parseDate("2018-10-10"),  parseDate("2018-10-24"));
        rentsManager.addRent(motorcycl, customer, employee,parseDate("2018-10-10"),  parseDate("2018-10-14"));
        List<Rent> rents = DatabaseManager.getAllDuringRents();

        assertEquals(5505.5,rents.get(0).getTotalPrice());
        assertEquals(14971.65,rents.get(1).getTotalPrice());
        assertEquals(618.85,rents.get(2).getTotalPrice());
    }


    @Test
    void endRent() throws ErrorMessageException {

        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres", 888777666, "email1", "1", "", "", false);
        DatabaseManager.addCustomer("Dawid", "car", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656", "companyAdress1");
        DatabaseManager.addCar("a", "nameCar", 1111, "czerwony", 2000, 2999998, 1.9, 5.7, FuelType.diesel, 5);
        DatabaseManager.addBike("b", "bike", 1111.11, "czerwony", 2000);
        DatabaseManager.addMotorcycle("c", "XXX1919", 1111.01, "czerwony", 2000, 200000, 2.5, 9.9);
        Employee employee = new Employee(1, "Dawidziak", "Dawidziak", "addres", 888777666, "email1", null);
        Customer customer = new Customer("Dawid", "Janiak", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656", "companyAdress", 0.0);
        Car car = new Car("a", VehicleStatus.avaiable, VehicleType.car, "nameCar", 1111, "czerwony", 2000, 2999998, 1.9, 5.7, FuelType.diesel, 5);
        Bike bike = new Bike("b", VehicleStatus.avaiable, VehicleType.bike, "bike", 1111.11, "czerwony", 2000);
        Motorcycle motorcycl = new Motorcycle("c", VehicleStatus.avaiable, VehicleType.motorcycle, "XXX1919", 1111.01, "czerwony", 2000, 300000, 2.5, 9.9);

        rentsManager.addRent(car, customer, employee, parseDate("2018-12-12"),  parseDate("2018-12-21"));
        rentsManager.addRent(bike, customer, employee, parseDate("2018-10-10"), parseDate("2018-10-24"));
        rentsManager.addRent(motorcycl, customer, employee, parseDate("2018-10-10"), parseDate("2018-10-14"));
        List<Rent> rents = DatabaseManager.getAllDuringRents();

        rentsManager.endRent(rents.get(0), 300000, 0);
        rentsManager.endRent(rents.get(1), 300000, 0);
        rentsManager.endRent(rents.get(2), 300000, 0);

        rents = DatabaseManager.getAllEndedRents();
        assertEquals(3, rents.size());
        rents = DatabaseManager.getAllDuringRents();
        assertEquals(0, rents.size());
    }

    private static java.sql.Date parseDate(String date) {
        try {
            return new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    @Test
    void getFilteredRents() throws ErrorMessageException {
        DatabaseManager.addEmployee("Dawid", "Dawidziak", "addres", 888777666, "email1", "1", "", "", false);
        DatabaseManager.addEmployee("X", "X", "addres", 11233, "email2", "2", "", "", false);
        DatabaseManager.addCustomer("Dawid", "Janiak", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656", "companyAdress1");
        DatabaseManager.addCustomer("Z", "Z", "95062910554", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656", "companyAdress1");
        DatabaseManager.addCar("a", "nameCar", 1111, "czerwony", 2000, 2999998, 1.9, 5.7, FuelType.diesel, 5);
        DatabaseManager.addBike("b", "bike", 1111.11, "czerwony", 2000);
        DatabaseManager.addMotorcycle("c", "XXX1919", 1111.01, "czerwony", 2000, 200000, 2.5, 9.9);
        Employee employee1 = new Employee(1, "Dawidziak", "Dawidziak", "addres", 888777666, "email1", null);
        Employee employee2 = new Employee(2, "X", "X", "addres", 11233, "email2", null);
        Customer customer1 = new Customer("Dawid", "Janiak", "95062910555", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656", "companyAdress", 0.0);
        Customer customer2 = new Customer("Z", "Z", "95062910554", "address", 151515664, "dominik1116@one.eu", "comakdk", "9956-3565-9656", "companyAdress", 0.0);
        Car car = new Car("a", VehicleStatus.avaiable, VehicleType.car, "nameCar", 1111, "czerwony", 2000, 2999998, 1.9, 5.7, FuelType.diesel, 5);
        Bike bike = new Bike("b", VehicleStatus.avaiable, VehicleType.bike, "bike", 1111.11, "czerwony", 2000);
        Motorcycle motorcycl = new Motorcycle("c", VehicleStatus.avaiable, VehicleType.motorcycle, "XXX1919", 1111.01, "czerwony", 2000, 300000, 2.5, 9.9);


        rentsManager.addRent(car, customer1, employee1, parseDate("2018-12-12"),  parseDate("2018-12-21"));
        rentsManager.addRent(bike, customer1, employee1, parseDate("2018-10-10"), parseDate("2018-10-24"));
        rentsManager.addRent(motorcycl, customer2, employee2, parseDate("2018-10-10"), parseDate("2018-10-14"));
        List<Rent> rents = DatabaseManager.getAllDuringRents();

        rentsManager.endRent(rents.get(0), 300000, 0);
        rentsManager.endRent(rents.get(1), 300000, 0);
        rentsManager.endRent(rents.get(2), 300000, 0);
        rentsManager.addRent(car, customer2, employee2, parseDate("2019-12-12"),  parseDate("2019-12-21"));
        rentsManager.addRent(bike, customer2, employee2, parseDate("2019-10-10"), parseDate("2019-10-24"));

        rents = rentsManager.getFilteredRents(null,null,-1,null, null, null, null,
                null, null, null, null, null, null, -1, -1, null, "ania", null,null);
        assertEquals(2, rents.size());
        assertEquals("Dawid Janiak", rents.get(0).getCustomer().toString());
        assertEquals("Dawid Janiak", rents.get(1).getCustomer().toString());

        rents = rentsManager.getFilteredRents(null,null,-1,null, null, null, null,
                null, null, null, null, null, null, -1, -1, null, null, "awidzia",null);
        assertEquals(2, rents.size());
        assertEquals("Dawid Dawidziak", rents.get(0).getEmployee().toString());
        assertEquals("Dawid Dawidziak", rents.get(1).getEmployee().toString());


        rents = rentsManager.getFilteredRents(null,null,-1,null, null, null, null,
                null, null, null, null, null, null, -1, -1, null, null, null, RentStatus.during);
        assertEquals(2, rents.size());
        assertEquals("Z Z", rents.get(0).getCustomer().toString());
        assertEquals("Z Z", rents.get(1).getCustomer().toString());

        rents = rentsManager.getFilteredRents(null,null,-1,null, null, null, null,
                null, null, null, null, null, null, -1, -1, "a", null, null, null);
        assertEquals(2, rents.size());
        assertEquals("Dawid Janiak", rents.get(0).getCustomer().toString());
        assertEquals("nameCar", rents.get(0).getVehicle().getName());

        assertEquals("Z Z", rents.get(1).getCustomer().toString());
        assertEquals("nameCar", rents.get(1).getVehicle().getName());

        rents = rentsManager.getFilteredRents(null,"95062910555",-1,null, null, null, null,
                null, null, null, null, null, null, -1, -1, null, null, null,null);
        assertEquals(2, rents.size());

        rents = rentsManager.getFilteredRents(null,"95062910554",-1,null, null, null, null,
                null, null, null, null, null, null, -1, -1, null, null, null,null);
        assertEquals(3, rents.size());

        rents = rentsManager.getFilteredRents("a",null,-1,null, null, null, null,
                null, null, null, null, null, null, -1, -1, null, null, null,null);
        assertEquals(2, rents.size());
        assertEquals(true, rents.get(0).getVehicle() instanceof Car);
        assertEquals(true, rents.get(1).getVehicle() instanceof Car);

        rents = rentsManager.getFilteredRents("a","95062910554",-1,null, null, null, null,
                null, null, null, null, null, null, -1, -1, null, null, null,null);
        assertEquals(1, rents.size());
        assertEquals(true, rents.get(0).getVehicle() instanceof Car);

        rents = rentsManager.getFilteredRents("a","95062910555",-1,null, null, null, null,
                null, null, null, null, null, null, -1, -1, null, null, null,null);
        assertEquals(1, rents.size());
        assertEquals(true, rents.get(0).getVehicle() instanceof Car);

        rents = rentsManager.getFilteredRents(null,null,2,null, null, null, null,
                null, null, null, null, null, null, -1, -1, null, null, null,null);
        assertEquals(3, rents.size());

        rents = rentsManager.getFilteredRents(null,null,1,null, null, null, null,
                null, null, null, null, null, null, -1, -1, null, null, null,null);
        assertEquals(2, rents.size());

        rents = rentsManager.getFilteredRents(null,null,2,">", null, parseDate("2018-12-29"), null,
                null, null, null, null, null, null, -1, -1, null, null, null,null);
        assertEquals(2, rents.size());

        rents = rentsManager.getFilteredRents(null,null,-1,null, null, null, null,
                null, null, null, null, ">", null, 12000, -1, null, null, null,null);
        assertEquals(4, rents.size());
    }

}