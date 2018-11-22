package Model;

import DataBase.DataBase;
import Model.CustomEnumValues.Fuel;
import Model.CustomExceptions.UnknownClientTypeException;
import Model.CustomExceptions.WrongPeselException;
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

    public Model() {
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
    public boolean addClient(String pesel, String firstName, String lastName, int age, int phoneNumber, String address) {
        return database.addClient("actual", pesel, firstName, lastName, age, phoneNumber, address);
    }

    /**
     *  This function add rent to table named 'actual_rents', which contains all not ended rents of users
     * @param client
     * @param vehicle
     * @param dateOfRental Date when client want to rent a vehicle in format dd-MM-yyyy
     * @param dateOfReturn Date when client want to return a vehicle in format dd-MM-yyyy
     * @return Return true if was added properly, otherwise false
     */
    public boolean addRent(Client client, Vehicle vehicle, String dateOfRental, String dateOfReturn) {
       String vehicleID = vehicle.getId();
    // calculate price for rent a vehicle
        int priceForRent = calculatePriceForRent(client, vehicle, dateOfRental, dateOfReturn);
        int typeOfVehicle;
    // check if vehicle can be rent
        boolean canRent = false;
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
            database.setVehicleInaccessible(vehicleID, typeOfVehicle);
            return database.addActualRent(client.getPesel(), vehicleID, typeOfVehicle, priceForRent, dateOfRental, dateOfReturn);
        }
        else
            return false;
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
    public boolean addCar(String id, String name, int course, String model, Fuel fuel, int engineCapacity, int trunkCapacity, int numberOfDoors, int pricePerDay) {
        return database.addCar("actual", id, name, course, model, fuel, engineCapacity, trunkCapacity, numberOfDoors, pricePerDay);
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
    public boolean addBike(String id, String name, int course, String typeOfBike, String color, int tireWidth, int sizeOfWheele, int pricePerDay) {
        return database.addBike("actual", id, name, course, typeOfBike, color, tireWidth, sizeOfWheele, pricePerDay);
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
    public boolean addMotorcycle(String id, String name, int course, String model, int engineCapacity, int pricePerDay) {
        return database.addMotorcycle("actual", id, name, course, model, engineCapacity, pricePerDay);
    }

    /**
     * Return of vehicle. It change status of returnet car to available, add archival rents, and calculate onece again clients payments
     * @param rentID
     * @param penalty use this parameter when user returned crashed car
     * @return
     */
    public boolean returnVehicle(int rentID, int penalty){
        Rent rent = database.getRentByRentID(rentID);
        addArchivalRent(rent, penalty);
        switch (rent.getTypeOfVehicle()){
            case 1:
                addArchivalVehicle((Car)rent.getVehicle());
                break;
            case 2:
                addArchivalVehicle((Bike)rent.getVehicle());
                break;
            case 3:
                addArchivalVehicle((Motorcycle)rent.getVehicle());
                break;
        }
        int priceForRent = rent.getPriceForRent() + additionalPayment(rent) + penalty;
        addArchivalClient(rent.getClient());
        database.deleteRentFromActualRents(rentID);
        database.setVehicleAccessible(rent.getVehicle().getId(), rent.getTypeOfVehicle());
        database.updateClientSumPaid(rent.getClient().getPesel(), priceForRent);
        return true;
    }

    /**
     *
     * @return List of all clients in database
     */
    public List<Client> getAllClients() {
        try {
            return database.getAllClients("actual");
        } catch(Exception e){
            Logs.logger.warning("Error when try to get all clients");
            Logs.logger.warning(e.getMessage());
            return new ArrayList<Client>();
        }
    }

    /**
     *
     * @param clientPesel
     * @return all not returned vehicles rented by client
     */
    public List<Rent> getClientActualRents(String clientPesel){
      return database.getClientActualRents(clientPesel);
    }

    /**
     *
     * @param clientPesel
     * @return all returned vehicles rented by client
     */
    public List<Rent> getClientArchivalRents(String clientPesel){
        return database.getClientArchivalRents(clientPesel);
    }

    /**
     *
     * @return all not returned vehicles
     */
    public List<Rent> getAllActualRents() {
        try {
            return database.getAllActualRents();
        } catch(Exception e){
            return new ArrayList<Rent>();
        }
    }

    /**
     *
     * @return all returned vehicles
     */
    public List<Rent> getAllArchivalRents() {
        try {
            return database.getAllArchivalRents();
        } catch(Exception e){
            Logs.logger.warning("Error when try to get all archival rents");
            Logs.logger.warning(e.getMessage());
            return new ArrayList<Rent>();
        }
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
    private int calculatePriceForRent(Client client, Vehicle vehicle, String dateOfRental, String dateOfReturn){
        int pricePerDay = database.getPricePerDayOfVehicle(vehicle);
        int discount = getClientPercentOfDiscount(client);
        long days = countDays(dateOfRental, dateOfReturn);
        return Math.round((days * pricePerDay) * (100 - discount)) ;
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
        return diff;
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
     * Add ended rent to database archiwum
     * @param rent
     * @param priceForRent calculeted once again after returned car by client
     * @return true if added properly, otherwise false
     */
    private boolean addArchivalRent(Rent rent, int priceForRent) {
        String clientPesel = rent.getClient().getPesel();
        String vehicleID = rent.getVehicle().getId();
        int type_of_vehicle = rent.getTypeOfVehicle();
        String dateOfRental = rent.getDateOfRental();
        String dateOfReturn = getDate();
        return database.addArchivalRent(clientPesel, vehicleID, type_of_vehicle, priceForRent, dateOfRental, dateOfReturn);
    }

    /**
     * Add client to archiwum, needed for archval rents
     * @param client
     * @return true if added properly, otherwise false
     */
    private boolean addArchivalClient(Client client){
        if(!database.isClientExist(client.getPesel())){
            String firstName = client.getFirstName();
            String lastName = client.getLastName();
            int age = client.getAge();
            int phoneNumber = client.getPhoneNumber();
            String address = client.getAddress();
            String pesel = client.getPesel();
            return database.addClient("archival", pesel, firstName, lastName, age, phoneNumber, address);
        }
        else
            return true;
    }

    /**
     * Add car to archiwum, needed for archval rents
     * @param car
     * @return true if added properly, otherwise false
     */
    private boolean addArchivalVehicle(Car car){
        if(!database.isVehicleExists(car.getId(), 1)) {
            String carID = car.getId();
            String name = car.getName();
            int course = car.getCourse();
            String model = car.getModel();
            Fuel fuel = car.getFuel();
            int engineCapacity = car.getEngineCapacity();
            int trunkCapacity = car.getTrunkCapacity();
            int numberOfDoors = car.getNumberOfDoors();
            int pricePerDay = car.getPricePerDay();
            return database.addCar("archival", carID, name, course, model, fuel, engineCapacity, trunkCapacity, numberOfDoors, pricePerDay);
        }
        else
            return true;
        }

    /**
     * Add bike to archiwum, needed for archval rents
     * @param bike
     * @return true if added properly, otherwise false
     */
    private boolean addArchivalVehicle(Bike bike){
        if(!database.isVehicleExists(bike.getId(), 2)) {
            String bikeID = bike.getId();
            String name = bike.getName();
            int course = bike.getCourse();
            String typeOfBike = bike.getTypeOfBike();
            String color = bike.getColor();
            int tireWidth = bike.getTireWidth();
            int sizeOfWheele = bike.getSizeOfWheele();
            int price_per_day = bike.getPricePerDay();
            return database.addBike("archival", bikeID, name, course, typeOfBike, color, tireWidth, sizeOfWheele, price_per_day);
        }
        else
            return true;
        }

    /**
     * Add motorcycle to archiwum, needed for archval rents
     * @param motorcycle
     * @return true if added properly, otherwise false
     */
    private boolean addArchivalVehicle(Motorcycle motorcycle){
       if(!database.isVehicleExists(motorcycle.getId(), 3)) {
           String motorcycleID = motorcycle.getId();
           String name = motorcycle.getName();
           int course = motorcycle.getCourse();
           String model = motorcycle.getModel();
           int engineCapacity = motorcycle.getEngineCapacity();
           int pricePerDay = motorcycle.getPricePerDay();
           return database.addMotorcycle("archival", motorcycleID, name, course, model, engineCapacity, pricePerDay);
       }
       else
           return true;
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
}
