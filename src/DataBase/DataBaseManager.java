package DataBase;

import Model.Client;
import Model.CustomEnumValues.Fuel;
import Model.CustomExceptions.ErrorMessageException;
import Model.Employee;
import Model.Rent;
import Model.Vehicles.Bike;
import Model.Vehicles.Car;
import Model.Vehicles.Motorcycle;
import java.util.List;

public interface DataBaseManager {
// CLients
    boolean addClient(String pesel, String firstName, String lastName, int age, int phoneNumber, String address) throws ErrorMessageException;
    boolean updateClientSumPaid(String clientPesel, int cashPaid) throws ErrorMessageException;
    boolean updateCientData(String pesel, String firstName, String lastName, int age, int phoneNumber, String address) throws ErrorMessageException;
    List<Client> getAllActualClients() throws ErrorMessageException;
    List<Client> getAllArchivalClients() throws ErrorMessageException;
    Client getClientByPesel(String pesel) throws ErrorMessageException;
    boolean setClientAsArchival(String pesel) throws ErrorMessageException;
    List<Rent> getClientActualRents(String clientPesel) throws ErrorMessageException;
    List<Rent> getClientArchivalRents(String clientPesel) throws ErrorMessageException;

// Rents
    boolean addRent(String clientID, String vehicleID, int typeOfVehicle, int priceForRent, String dateOfRental, String dateOfReturn, int employeeID) throws ErrorMessageException;
    boolean updateRentPrice(int rentID, int cashPaid) throws ErrorMessageException;
    boolean setRentAsArchival(int rentID) throws ErrorMessageException;
    List<Rent> getAllActualRents() throws ErrorMessageException;
    List<Rent> getAllArchivalRents() throws ErrorMessageException;
    Rent getRentByRentID(int rentID) throws ErrorMessageException;

// Car
    boolean addCar(String id, String name, int course, String model, Fuel fuel, int engineCapacity, int trunkCapacity, int numberOfDoors, int pricePerDay) throws ErrorMessageException;
    Car getCarByID (String carID) throws ErrorMessageException;
    List<Car> getAllAvaiableCars() throws ErrorMessageException;
    List<Car> getAllABorrowedCars() throws ErrorMessageException;
    List<Car> getAllActuallCars() throws ErrorMessageException;
    List<Car> getAllAArchivalCars() throws ErrorMessageException;


// Bike
    boolean addBike(String id, String name, int course, String typeOfBike, String color, int tireWidth, int sizeOfWheele, int pricePerDay) throws ErrorMessageException;
    Bike getbikeByID (String bikeID) throws ErrorMessageException;
    List<Bike> getAllAvaiableBikes() throws ErrorMessageException;
    List<Bike> getAllABorrowedBikes() throws ErrorMessageException;
    List<Bike> getAllActuallBikes() throws ErrorMessageException;
    List<Bike> getAllAArchivalBikes() throws ErrorMessageException;

// Motorcycle
    boolean addMotorcycle(String id, String name, int course, String model, int engineCapacity, int pricePerDay) throws ErrorMessageException;
    Motorcycle getMotorcycleByID (String motorcycleID) throws ErrorMessageException;
    List<Motorcycle> getAllAvaiableMotorcycles() throws ErrorMessageException;
    List<Motorcycle> getAllABorrowedMotorcycles() throws ErrorMessageException;
    List<Motorcycle> getAllActuallMotorcycles() throws ErrorMessageException;
    List<Motorcycle> getAllAArchivalMotorcycles() throws ErrorMessageException;

// Vehicle
    boolean setVehicleInaccessible(String vehicleID, int typeOfVehicle) throws ErrorMessageException;
    boolean setVehicleAccessible(String vehicleID, int typeOfVehicle) throws ErrorMessageException;
    int getPricePerDayOfVehicle(String vehicleID, int typeOfVehicle) throws ErrorMessageException;
    boolean setVehicleArchival(String vehicleID, int typeOfVehicle) throws ErrorMessageException;

//Employee
    boolean addEmployee(String firstName, String lastName, int phoneNumber) throws ErrorMessageException;
    List<Employee> getAllEmployees() throws ErrorMessageException;
    Employee getEmployeeByID(int id) throws ErrorMessageException;
}
