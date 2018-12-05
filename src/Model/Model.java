package Model;

import DataBase.DataBase;
import Model.CustomEnumValues.Fuel;
import Model.CustomExceptions.ErrorMessageException;
import Model.Vehicles.Bike;
import Model.Vehicles.Car;
import Model.Vehicles.Motorcycle;
import Model.Vehicles.Vehicle;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Model {

    private final DataBase database;
    public Model() throws ErrorMessageException {
        this.database = new DataBase();

    }
    /**
     *  This function add client to table named 'clients'
     * @param pesel
     * @param firstName
     * @param lastName
     * @param age
     * @param phoneNumber
     * @param address
     * @return Return true if user was added properly, otherwise false
     */
    public boolean addClient(String pesel, String firstName, String lastName, int age, int phoneNumber, String address) throws ErrorMessageException {
        return database.addClient( pesel, firstName, lastName, age, phoneNumber, address);
    }

    public boolean updateCientData(String pesel, String firstName, String lastName, int age, int phoneNumber, String address) throws ErrorMessageException{
        Client client = getClientByPesel(pesel);
        if (client == null){
            throw new ErrorMessageException("Client doesn't exist");
        }

    // When exception appears when updating client trnsaction should be aborted... but we don't have transaction... so that is why we are using catches in that case :)
        try{
            return database.updateCientData(pesel, firstName, lastName, age, phoneNumber, address);
        }catch (Exception e){
            database.updateCientData(client.getPesel(), client.getFirstName(), client.getLastName(), client.getAge(), client.getPhoneNumber(), client.getAddress());
            throw e;
        }
    }

    public List<Client> getAllActualClients() throws ErrorMessageException{
       return database.getAllActualClients();
    }

    public  List<Client> getAllArchivalClients() throws ErrorMessageException{
        return database.getAllArchivalClients();
    }

    public  Client getClientByPesel(String pesel) throws ErrorMessageException {
        return database.getClientByPesel(pesel);
    }

    public boolean transferClientToArchivalClients(String pesel) throws ErrorMessageException {
        Client client = getClientByPesel(pesel);
        if (client == null){
            throw new ErrorMessageException("Client doesn't exist");
        }
        return database.setClientAsArchival(pesel);
    }

    public List<Rent> getClientActualRents(String pesel) throws ErrorMessageException {
        return database.getClientActualRents(pesel);
    }

    public List<Rent> getClientArchivalRents(String pesel) throws ErrorMessageException {
        return database.getClientArchivalRents(pesel);
    }

    /**
     *  This function add rent to table named 'actual_rents', which contains all not ended rents of users
     * @param client
     * @param vehicle
     * @param dateOfRental Date when client want to rent a vehicle in format dd-MM-yyyy
     * @param dateOfReturn Date when client want to return a vehicle in format dd-MM-yyyy
     * @return Return true if was added properly, otherwise false
     */
    public boolean addRent(Client client, Vehicle vehicle, String dateOfRental, String dateOfReturn, int employeeID) throws ErrorMessageException {
        String vehicleID = vehicle.getId();
        int priceForRent = calculatePriceForRent(client, vehicle, dateOfRental, dateOfReturn);
        int typeOfVehicle;
        boolean canRent ;

    // check if vehicle can be rent
        if(vehicle instanceof Car){
            Car car = database.getCarByID(vehicleID);
            canRent = car.isAvailability();
            typeOfVehicle = 1;
        }
        else if(vehicle instanceof Bike){
            Bike bike = database.getbikeByID(vehicleID);
            canRent = bike.isAvailability();
            typeOfVehicle = 2;
        }
        else{
            Motorcycle motorcycle = database.getMotorcycleByID(vehicleID);
            canRent = motorcycle.isAvailability();
            typeOfVehicle = 3;
        }

//rent a vehicle
        if (canRent) {
            try {
                database.setVehicleInaccessible(vehicleID, typeOfVehicle);
            } catch (ErrorMessageException e) {
                database.setVehicleAccessible(vehicleID, typeOfVehicle);
                throw e;
            }
            return database.addRent(client.getPesel(), vehicleID, typeOfVehicle, priceForRent, dateOfRental, dateOfReturn, employeeID);
        }
        else
            throw new ErrorMessageException("Vehicle is inaccessible");
    }

    public  List<Rent> getAllActualRents() throws ErrorMessageException {
        return database.getAllActualRents();
    }

    public  List<Rent> getAllArchivalRents() throws ErrorMessageException {
        return database.getAllArchivalRents();
    }

    public Rent getRentByRentID(int rentID) throws ErrorMessageException {
        return database.getRentByRentID(rentID);
    }

    /**
     * This function add car to table named 'cars', which contains all cars in company
     * @param id
     * @param name
     * @param course
     * @param model
     * @param fuel
     * @param engineCapacity
     * @param trunkCapacity
     * @param numberOfDoors
     * @param pricePerDay
     * @return Return true if was added properly, otherwise false
     */
    public boolean addCar(String id, String name, int course, String model, Fuel fuel, int engineCapacity, int trunkCapacity, int numberOfDoors, int pricePerDay) throws ErrorMessageException {
        return database.addCar( id, name, course, model, fuel, engineCapacity, trunkCapacity, numberOfDoors, pricePerDay);
    }

    public Car getCarByID(String carID) throws ErrorMessageException {
        return database.getCarByID(carID);
    }

    public  List<Car> getAllAvaiableCars() throws ErrorMessageException {
        return database.getAllAvaiableCars();
    }

    public List<Car> getAllABorrowedCars() throws ErrorMessageException {
        return database.getAllABorrowedCars();
    }

    public List<Car> getAllActuallCars() throws ErrorMessageException {
        return database.getAllActuallCars();
    }

    public List<Car> getAllAArchivalCars() throws ErrorMessageException {
        return database.getAllAArchivalCars();
    }


    /**
     * This function add bike to table named 'bikes', which contains all bikes in company
     * @param id
     * @param name
     * @param course
     * @param typeOfBike
     * @param color
     * @param tireWidth
     * @param sizeOfWheele
     * @param pricePerDay
     * @return Return true if was added properly, otherwise false
     */
    public boolean addBike(String id, String name, int course, String typeOfBike, String color, int tireWidth, int sizeOfWheele, int pricePerDay) throws ErrorMessageException {
        return database.addBike( id, name, course, typeOfBike, color, tireWidth, sizeOfWheele, pricePerDay);
    }

    public Bike getbikeByID(String bikeID) throws ErrorMessageException {
        return database.getbikeByID(bikeID);
    }

    public List<Bike> getAllAvaiableBikes() throws ErrorMessageException {
        return database.getAllAvaiableBikes();
    }

    public List<Bike> getAllABorrowedBikes() throws ErrorMessageException {
        return database.getAllABorrowedBikes();
    }

    public List<Bike> getAllActuallBikes() throws ErrorMessageException {
        return database.getAllActuallBikes();
    }

    public List<Bike> getAllAArchivalBikes() throws ErrorMessageException {
        return database.getAllAArchivalBikes();
    }

    /**
     * This function add motorcycle to table named 'motorcycles', which contains all motorcycles in company
     * @param id
     * @param name
     * @param course
     * @param model
     * @param engineCapacity
     * @param pricePerDay
     * @return
     */
    public boolean addMotorcycle(String id, String name, int course, String model, int engineCapacity, int pricePerDay) throws ErrorMessageException {
        return database.addMotorcycle(id, name, course, model, engineCapacity, pricePerDay);
    }

    public Motorcycle getMotorcycleByID(String motorcycleID) throws ErrorMessageException {
        return database.getMotorcycleByID(motorcycleID);
    }

    public List<Motorcycle> getAllAvaiableMotorcycles() throws ErrorMessageException {
        return database.getAllAvaiableMotorcycles();
    }

    public List<Motorcycle> getAllABorrowedMotorcycles() throws ErrorMessageException {
        return database.getAllABorrowedMotorcycles();
    }

    public List<Motorcycle> getAllActuallMotorcycles() throws ErrorMessageException {
        return database.getAllActuallMotorcycles();
    }

    public List<Motorcycle> getAllAArchivalMotorcycles() throws ErrorMessageException {
        return database.getAllAArchivalMotorcycles();
    }


    /**
     * Return of vehicle. It change status of returnet car to available, add archival rents, and calculate onece again clients payments
     * @param rentID
     * @param penalty use this parameter when user returned crashed car
     * @return
     */
    public boolean returnVehicle(int rentID, int penalty) throws ErrorMessageException {
        Rent rent = database.getRentByRentID(rentID);
        database.setRentAsArchival(rentID);
        database.setVehicleAccessible(rent.getVehicle().getId(), getTypeOfVehicle(rent.getVehicle()));

        int totalPriceForRent = rent.getPriceForRent() + additionalPayment(rent) + penalty;
        database.updateRentPrice(rentID,  totalPriceForRent);

        database.updateClientSumPaid(rent.getClient().getPesel(), rent.getClient().getSumPaidForAllRents() + totalPriceForRent);
        return true;
    }

    /**
     * @param client
     * @return return percent of client discount, for each 10 000 spent
     *          clients get 5% of discount, maximum discount is 25%
     */
    public int getClientPercentOfDiscount(Client client){
        int sumPaideByClient = client.getSumPaidForAllRents();
        int percentOfDiscount = (sumPaideByClient / 10000) * 5;
        if(percentOfDiscount > 25)
            return 25;
        return percentOfDiscount;
    }

    /**
     *
     * @param vehicle
     * @param dateOfRental
     * @param dateOfReturn
     * @return
     */
    public int calculatePriceForRent(Client client, Vehicle vehicle, String dateOfRental, String dateOfReturn) throws ErrorMessageException {
        int pricePerDay = database.getPricePerDayOfVehicle(vehicle.getId(), getTypeOfVehicle(vehicle));
        int discount = getClientPercentOfDiscount(client);
        long days = countDays(dateOfRental, dateOfReturn);
        int percentDiscountPricePerDay = ((100 - discount) % 100);


        System.out.println(days + " " + discount + " " + pricePerDay + " " +  ((100 - discount)%100));
        if(percentDiscountPricePerDay == 0)
            return Math.round(days * pricePerDay);
        else
            return Math.round((days * pricePerDay) * percentDiscountPricePerDay/100);
    }

    public boolean addEmployee(String firstName, String lastName, int phoneNumber) throws ErrorMessageException {
        return database.addEmployee(firstName, lastName, phoneNumber);
    }

    public List<Employee> getAllEmployees() throws ErrorMessageException {
        return database.getAllEmployees();
    }

    public Employee getEmployeeById(int id) throws ErrorMessageException {
        return database.getEmployeeByID(id);
    }

    /**
     *
     * @param dateOfRental in format dd-MM-yyyy
     * @param dateOfReturn in format dd-MM-yyyy
     * @return number of rental days
     */
    private long countDays(String dateOfRental, String dateOfReturn ){
        long diff;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            Date firstDate = sdf.parse(dateOfRental);
            Date secondDate = sdf.parse(dateOfReturn);
            long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
            diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        } catch (ParseException e){
            Logs.logger.warning("Error when try to count days of rent");
            Logs.logger.warning(e.getMessage());
            return 0;
        }
        return diff + 1;
    }

    /**
     *
     * @return actual date in format dd-MM-yyyy
     */
    private String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return sdf.format(date);
    }

    /**
     * Calculate amount of cash after vehicle return
     * @param rent
     * @return
     */
    private int additionalPayment(Rent rent){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String returnDate= sdf.format(date);
        long days = countDays(returnDate, rent.getDateOfReturn());
        if (days == 0)
            return 0;
        else
            return (int)Math.round((rent.getVehicle().getPricePerDay() + (rent.getVehicle().getPricePerDay() * 0.1)) * (int)days);
        // Jezeli klient nie oddał w okraślonym terminie doliczamy karę za kazdy dzien w wysokośći 10% stawki dziennej
    }

    private int getTypeOfVehicle(Vehicle vehicle){
        if (vehicle instanceof Car)
            return 1;
        else if(vehicle instanceof Bike)
            return 2;
        else
            return 3;
    }


    public void closeConnection(){
        database.closeConnection();
    }


    //For test only
    public void discount(String pesel,int paid) throws ErrorMessageException {
        database.updateClientSumPaid(pesel, paid);
    }
}
