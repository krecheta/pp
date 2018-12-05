package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.CustomExceptions.ErrorMessageException;
import Model.Employee;
import Model.Logs;
import Model.Rent;
import Model.Client;
import Model.CustomEnumValues.Fuel;
import Model.Vehicles.Bike;
import Model.Vehicles.Car;
import Model.Vehicles.Motorcycle;
import Model.Vehicles.Vehicle;

public class DataBase implements DataBaseManager{
    private Connection conn;
    private Statement stat;
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:biblioteka.db";

    public DataBase() throws ErrorMessageException {
        try {
            Class.forName(DataBase.DRIVER);
        } catch (ClassNotFoundException e) {
            Logs.logger.warning("No driver JBC found");
            throw new ErrorMessageException("Contact with administrator");
        }

        try {
            conn = DriverManager.getConnection(DB_URL);
            stat = conn.createStatement();
        } catch (SQLException e) {
            Logs.logger.warning("Problems with connection with dataBase");
            throw new ErrorMessageException("Contact with administrator");
        }
        createTables();
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            Logs.logger.warning("Close connection error");
        }
    }

    /**
     * @param pesel
     * @param firstName
     * @param lastName
     * @param age
     * @param phoneNumber
     * @param address
     * @return true if added properly, otherwise false
     */
    public boolean addClient(String pesel, String firstName, String lastName, int age, int phoneNumber, String address) throws ErrorMessageException {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "insert into clients(pesel, firstName, lastName, age, phone_number, address, sum_paid_for_all_rents, status) values (?, ?, ?, ?, ?, ?, ?, ?);");
            prepStmt.setString(1, pesel);
            prepStmt.setString(2, firstName);
            prepStmt.setString(3, lastName);
            prepStmt.setInt(4, age);
            prepStmt.setInt(5, phoneNumber);
            prepStmt.setString(6, address);
            prepStmt.setInt(7, 0); // sum paid for all rents
            prepStmt.setInt(8, 0); // status of client
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to add client");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Can't properly add client, contact with administrator");
        }
        return true;
    }

    public boolean updateClientSumPaid(String clientPesel, int cashPaid) throws ErrorMessageException {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "update clients set sum_paid_for_all_rents =" + cashPaid + " where pesel = " + "\"" + clientPesel + "\" ");
            prepStmt.execute();

        } catch (SQLException e) {
            Logs.logger.warning("Error when try to update client sum");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return true;
    }

    public boolean updateCientData(String pesel, String firstName, String lastName, int age, int phoneNumber, String address) throws ErrorMessageException{
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "update clients set" +
                            " firstName = " + "\"" + firstName + "\"" +
                            " ,lastName = " +"\""  + lastName + "\""  +
                            " ,age = " +"\""  + age +"\""  +
                            ", phone_number = " + "\"" + phoneNumber + "\"" +
                            " ,address = " + "\"" + address +"\""  +
                            " where pesel = " + "\"" + pesel + "\" ");
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to update client data");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return true;
    };

    public List<Client> getAllActualClients() throws ErrorMessageException {
        return getAllClients("actual");
    }

    public List<Client> getAllArchivalClients() throws ErrorMessageException {
        return getAllClients("archival");
    }

    public Client getClientByPesel(String pesel) throws  ErrorMessageException {
        ResultSet ClientResult;
        int phoneNumber, type, sumPaidForAllRents, age;
        boolean avail = false;
        String firstName, lastName, address;
        try {
            ClientResult = stat.executeQuery("SELECT * FROM clients where pesel=" + "\"" + pesel + "\"");
            firstName = ClientResult.getString("firstName");
            lastName = ClientResult.getString("lastName");
            age = ClientResult.getInt("age");
            phoneNumber = ClientResult.getInt("phone_number");
            address = ClientResult.getString("address");
            sumPaidForAllRents = ClientResult.getInt("sum_paid_for_all_rents");
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get client by clientPesel");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return new Client(pesel, firstName, lastName, age, phoneNumber, address, sumPaidForAllRents);
    }

     public boolean setClientAsArchival(String pesel) throws ErrorMessageException{
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                        "update clients set status = 1 where pesel = " + "\"" + pesel + "\"");
            prepStmt.execute();
            } catch (SQLException e) {
            Logs.logger.warning("Error when try to change client as archival");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return true;
    };

    public List<Rent> getClientActualRents(String clientPesel) throws ErrorMessageException {
        try {
            return getAllCLientsRents("actual", clientPesel);
        } catch (Exception e) {
            Logs.logger.warning("Error when try to get Client Actual Rents");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
    }

    public List<Rent> getClientArchivalRents(String clientPesel) throws ErrorMessageException {
        try {
            return getAllCLientsRents("archival", clientPesel);
        } catch (Exception e) {
            Logs.logger.warning("Error when try to get Client Actual Rents");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
    }

    public boolean addRent(String clientID, String vehicleID, int typeOfVehicle, int priceForRent, String dateOfRental, String dateOfReturn, int employeeID) throws ErrorMessageException {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "insert into rents(clientID, vehicleID, type_of_vehicle, price_for_rent, date_of_rental, date_of_return, status, employee) values ( ?, ?, ?, ?, ?, ?, ?, ?);");
            prepStmt.setString(1, clientID);
            prepStmt.setString(2, vehicleID);
            prepStmt.setInt(3, typeOfVehicle);
            prepStmt.setInt(4, priceForRent);
            prepStmt.setString(5, dateOfRental);
            prepStmt.setString(6, dateOfReturn);
            prepStmt.setInt(7, 0);
            prepStmt.setInt(8, employeeID);
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to add rent");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return true;
    }

    public boolean updateRentPrice(int rentID, int cashPaid) throws ErrorMessageException {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "update rents set price_for_rent =" + cashPaid + " where id = " + "\"" + rentID + "\"");
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to delete rent");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return true;
    }

    /**
     * Delete rent
     * @param rentID
     * @return
     */
    public boolean setRentAsArchival(int rentID) throws ErrorMessageException {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "update rents set status = \"1 \" where id = " + "\"" + rentID + "\"");
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to delete rent");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return true;
    }

    public List<Rent> getAllActualRents() throws  ErrorMessageException {
        return getAllRent("actual");
    }

    public List<Rent> getAllArchivalRents() throws ErrorMessageException {
        return getAllRent("archival");
    }

    public Rent getRentByRentID(int rentID) throws ErrorMessageException {
        int typeOfVehicle, priceForRent, employeeID;
        String dateOfRental, dateOfReturn, clientID, vehicleID;
        Car car = null; Bike bike = null; Motorcycle motorcycle = null; Client client;
        Employee employee;
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM rents where id = " + "\"" + rentID  + "\"");
            clientID = result.getString("clientID");
            vehicleID = result.getString("vehicleID");
            typeOfVehicle = result.getInt("type_of_vehicle");
            priceForRent = result.getInt("price_for_rent");
            dateOfRental = result.getString("date_of_rental");
            dateOfReturn = result.getString("date_of_return");
            employeeID = result.getInt("employee");
            employee = getEmployeeByID(employeeID);
            client  = getClientByPesel(clientID);

            switch (typeOfVehicle) {
                case 1:
                    car = getCarByID(vehicleID);
                    break;
                case 2:
                    bike = getbikeByID(vehicleID);
                    break;
                case 3:
                    motorcycle = getMotorcycleByID(vehicleID);
                    break;
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get rent by rentID");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        if (car != null)
            return new Rent(car, client, employee, rentID, priceForRent, dateOfRental, dateOfReturn);
        if(bike != null)
            return new Rent(bike, client, employee, rentID, priceForRent, dateOfRental, dateOfReturn);
        return new Rent(motorcycle, client, employee, rentID, priceForRent, dateOfRental, dateOfReturn);
    }

    /**
     *  Add car to database
     * @param id
     * @param name
     * @param course
     * @param model
     * @param fuel
     * @param engineCapacity
     * @param trunkCapacity
     * @param numberOfDoors
     * @param pricePerDay
     * @return true if added properly, otherwise false
     */
    public boolean addCar(String id, String name, int course, String model, Fuel fuel, int engineCapacity, int trunkCapacity, int numberOfDoors, int pricePerDay) throws ErrorMessageException {
        try {
            PreparedStatement prepStmt;

           prepStmt = conn.prepareStatement(
                   "insert into cars(id, name, course, availability, model, fuel, engine_capacity, trunk_capacity, number_of_doors, price_per_day, status) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            prepStmt.setString(1, id);
            prepStmt.setString(2, name);
            prepStmt.setInt(3, course);
            prepStmt.setInt(4, 0);
            prepStmt.setString(5, model);
            prepStmt.setString(6, fuel.name());
            prepStmt.setInt(7, engineCapacity);
            prepStmt.setInt(8, trunkCapacity);
            prepStmt.setInt(9, numberOfDoors);
            prepStmt.setInt(10, pricePerDay);
            prepStmt.setInt(11, 0);
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to add car to database");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Can't properly add car, contact with administrator");
        }
        return true;
    }

    public Car getCarByID (String carID) throws ErrorMessageException {
        ResultSet CarResult;
        int course, fuel, engineCapacity, trunkCapacity, numberOfDoors, pricePerDay, availability;
        boolean avail;
        Fuel fuelEnum;
        String name, model;
        try {
            CarResult = stat.executeQuery("SELECT * FROM cars where id=" + '\"' + carID  + '\"' );
            name = CarResult.getString("name");
            course = CarResult.getInt("course");
            model = CarResult.getString("model");
            fuel = CarResult.getInt("fuel");
            engineCapacity = CarResult.getInt("engine_capacity");
            trunkCapacity = CarResult.getInt("trunk_capacity");
            numberOfDoors = CarResult.getInt("number_of_doors");
            pricePerDay = CarResult.getInt("price_per_day");
            availability = CarResult.getInt("availability");
            if(fuel == 0)
                fuelEnum = Fuel.petrol;
            else
                fuelEnum = Fuel.diesel;

            avail = availability == 0;
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get car by carID");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return new Car (carID, name, course, avail, model, fuelEnum, engineCapacity, trunkCapacity, numberOfDoors, pricePerDay);
    }

    public List<Car> getAllAvaiableCars() throws ErrorMessageException{
        return getAllCars(0);

    }

    public List<Car> getAllABorrowedCars() throws ErrorMessageException{
        return getAllCars(1);

    }

    public List<Car> getAllActuallCars() throws ErrorMessageException{
        return getAllCars("actual");

    }

    public List<Car> getAllAArchivalCars() throws ErrorMessageException{
        return getAllCars("archival");
    }

    /**
     * @param id
     * @param name
     * @param course
     * @param typeOfBike
     * @param color
     * @param tireWidth
     * @param sizeOfWheele
     * @param pricePerDay
     * @return true if added properly, otherwise false
     */
    public boolean addBike(String id, String name, int course, String typeOfBike, String color, int tireWidth, int sizeOfWheele, int pricePerDay) throws ErrorMessageException {
        try {
            PreparedStatement prepStmt;
            prepStmt = conn.prepareStatement(
                "insert into bikes(id, name, course, availability, type_of_bike, color, tire_width, size_of_wheele, price_per_day, status) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            prepStmt.setString(1, id);
            prepStmt.setString(2, name);
            prepStmt.setInt(3, course);
            prepStmt.setInt(4, 0);
            prepStmt.setString(5, typeOfBike);
            prepStmt.setString(6, color);
            prepStmt.setInt(7, tireWidth);
            prepStmt.setInt(8, sizeOfWheele);
            prepStmt.setInt(9, pricePerDay);
            prepStmt.setInt(10, 0);
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to add bike");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Can't properly add bike, contact with administrator");
        }
        return true;
    }


    public Bike getbikeByID (String bikeID) throws ErrorMessageException {
        ResultSet BikeResult;
        int course, pricePerDay, tireWidth, sizeOfWheele, availability;
        boolean avail;
        String name, typeOfBike, color;
        try {
            BikeResult = stat.executeQuery("SELECT * FROM bikes where id=" + '\"' + bikeID + '\"');
            name = BikeResult.getString("name");
            course = BikeResult.getInt("course");
            typeOfBike = BikeResult.getString("type_of_bike");
            color = BikeResult.getString("color");
            tireWidth = BikeResult.getInt("tire_width");
            sizeOfWheele = BikeResult.getInt("size_of_wheele");
            pricePerDay = BikeResult.getInt("price_per_day");
            availability = BikeResult.getInt("availability");
            avail = availability == 0;
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get bike by bikeID");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return new Bike(bikeID, name, course, avail, typeOfBike, color, tireWidth, sizeOfWheele, pricePerDay);
    }

    public List<Bike> getAllAvaiableBikes() throws ErrorMessageException{
        return getAllBikes(0);
    }

    public List<Bike> getAllABorrowedBikes() throws ErrorMessageException{
        return getAllBikes(1);
    }

    public List<Bike> getAllActuallBikes() throws ErrorMessageException{
        return getAllBikes("actual");
    }

    public List<Bike> getAllAArchivalBikes() throws ErrorMessageException{
        return getAllBikes("archival");
    }

    /**
     * @param id
     * @param name
     * @param course
     * @param engineCapacity
     * @param pricePerDay
     * @return true if added properly, otherwise false
     */
    public boolean addMotorcycle(String id, String name, int course, String model, int engineCapacity, int pricePerDay) throws ErrorMessageException {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "insert into motorcycles(id, name, course, availability, model, engine_capacity, price_per_day, status) values (?, ?, ?, ?, ?, ?, ?, ?);");
            prepStmt.setString(1, id);
            prepStmt.setString(2, name);
            prepStmt.setInt(3, course);
            prepStmt.setInt(4, 0);
            prepStmt.setString(5, model);
            prepStmt.setInt(6, engineCapacity);
            prepStmt.setInt(7, pricePerDay);
            prepStmt.setInt(8, 0);
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to add motorcycle");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Can't properly add motorcycle, contact with administrator");
        }
        return true;
    }

    public Motorcycle getMotorcycleByID (String motorcycleID) throws ErrorMessageException {
        ResultSet MotorcycleResult;
        int course, pricePerDay, engineCapacity, availability;
        boolean avail;
        String name, model;
        try {
            MotorcycleResult = stat.executeQuery("SELECT * FROM motorcycles where id=" + "\"" + motorcycleID + "\"" );
            name = MotorcycleResult.getString("name");
            course = MotorcycleResult.getInt("course");
            model = MotorcycleResult.getString("model");
            engineCapacity = MotorcycleResult.getInt("engine_capacity");
            pricePerDay = MotorcycleResult.getInt("price_per_day");
            availability = MotorcycleResult.getInt("availability");
            avail = availability == 0;
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get motorcycle by motorcycleID");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return new Motorcycle(motorcycleID, name, course, avail, model, engineCapacity, pricePerDay);
    }


    public List<Motorcycle> getAllAvaiableMotorcycles() throws ErrorMessageException{
        return getAllMotorcycles(0);
    }

    public List<Motorcycle> getAllABorrowedMotorcycles() throws ErrorMessageException{
        return getAllMotorcycles(1);
    }

    public List<Motorcycle> getAllActuallMotorcycles() throws ErrorMessageException{
        return getAllMotorcycles("actual");
    }

    public List<Motorcycle> getAllAArchivalMotorcycles() throws ErrorMessageException{
        return getAllMotorcycles("archival");
    }

    public boolean setVehicleInaccessible(String vehicleID, int typeOfVehicle) throws ErrorMessageException {
        try {
            PreparedStatement prepStmt = null;
            switch(typeOfVehicle) {
                case 1:
                    prepStmt = conn.prepareStatement(
                            "update cars set availability = 1 where id = " + "\"" + vehicleID + "\"");
                    break;
                case 2:
                    prepStmt = conn.prepareStatement(
                            "update bikes set availability = 1 where id = " + "\"" + vehicleID + "\"");
                    break;
                case 3:
                    prepStmt = conn.prepareStatement(
                            "update motorcycles set availability = 1 where id = " + "\"" + vehicleID + "\"");
                    break;
            }
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to set vehicle inaccessible");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return true;
    }

    public boolean setVehicleAccessible(String vehicleID, int typeOfVehicle) throws ErrorMessageException {
        try {
            PreparedStatement prepStmt = null;
            switch(typeOfVehicle) {
                case 1:
                    prepStmt = conn.prepareStatement(
                            "update cars set availability = 0 where id = " + "\"" + vehicleID + "\"");
                    break;
                case 2:
                    prepStmt = conn.prepareStatement(
                            "update bikes set availability = 0 where id = " + "\"" + vehicleID + "\"");
                    break;
                case 3:
                    prepStmt = conn.prepareStatement(
                            "update motorcycles set availability = 0 where id = " +  "\"" + vehicleID + "\"");
                    break;
            }
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to set vehicle accessible");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return true;
    }

    public int getPricePerDayOfVehicle(String vehicleID, int typeOfVehicle) throws ErrorMessageException {
       int pricePerDay = 0;
       if(typeOfVehicle == 1)
           pricePerDay = getCarPricePerDay(vehicleID);

       else if(typeOfVehicle == 2)
           pricePerDay = getBikePricePerDay(vehicleID);

       if(typeOfVehicle == 3)
           pricePerDay = getMotorcyclePricePerDay(vehicleID);
      return pricePerDay;
    }

    public boolean setVehicleArchival(String vehicleID, int typeOfVehicle) throws ErrorMessageException{
        try {
            PreparedStatement prepStmt = null;
            switch(typeOfVehicle) {
                case 1:
                    prepStmt = conn.prepareStatement(
                            "update cars set status = 1 where id = " + "\"" + vehicleID  + "\"");
                    break;
                case 2:
                    prepStmt = conn.prepareStatement(
                            "update bikes set  status = 1 where id = " + "\"" + vehicleID  + "\"");
                    break;
                case 3:
                    prepStmt = conn.prepareStatement(
                            "update motorcycles set  status = 1 where id = " +  "\"" + vehicleID + "\"");
                    break;
            }
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to set vehicle accessible");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return true;
    }

    public boolean addEmployee(String firstName, String lastName, int phone_number) throws ErrorMessageException{
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                "insert into employees( firstName, lastName, phone_number) values (?, ?, ?);");
            prepStmt.setString(1, firstName);
            prepStmt.setString(2, lastName);
            prepStmt.setInt(3, phone_number);
            prepStmt.execute();
        } catch (SQLException e) {
        Logs.logger.warning("Error when try to add employee to database");
        Logs.logger.warning(e.getMessage());
        throw new ErrorMessageException("Can't properly add employee, contact with administrator");
        }
     return true;
    }

    public List<Employee> getAllEmployees() throws ErrorMessageException{
        List<Employee> employees = new ArrayList<>();
        String firstName, lastName;
        int phoneNumber, id ;
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM employees ");
            while (result.next()) {
                firstName = result.getString("firstName");
                lastName = result.getString("lastName");
                phoneNumber = result.getInt("phone_number");
                id = result.getInt("id");
                employees.add(new Employee(id, firstName, lastName, phoneNumber));
            }
        }catch(Exception e){
            Logs.logger.warning("Error when try to get all employees");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return employees;
    }

    public Employee getEmployeeByID(int id) throws ErrorMessageException{
        String firstName, lastName;
        Employee employee = null;
        int phoneNumber;
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM employees where id = " + "\"" + id + "\"");
            while (result.next()) {
                firstName = result.getString("firstName");
                lastName = result.getString("lastName");
                phoneNumber = result.getInt("phone_number");
                id = result.getInt("id");
                employee = new Employee(id, firstName, lastName, phoneNumber);
            }
        }catch(Exception e){
            Logs.logger.warning("Error when try to get employee by id ");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return employee;
    }


/***************************  Private *********************************/

    private List<Client> getAllClients(String where) throws ErrorMessageException {
        List<Client> clients = new ArrayList<>();
        try {
            ResultSet result;
            int status;
            if( where.equals("actual"))
                status = 0;
            else
                status = 1;
            result = stat.executeQuery("SELECT * FROM clients where status = " + "\"" + status + "\"");
            int age, phoneNumber, type, sumPaidForAllRents;
            String firstName, lastName, address, pesel;
            while(result.next()) {
                pesel = result.getString("pesel");
                firstName = result.getString("firstName");
                lastName = result.getString("lastName");
                age = result.getInt("age");
                phoneNumber = result.getInt("phone_number");
                address = result.getString("address");
                sumPaidForAllRents = result.getInt("sum_paid_for_all_rents");
                clients.add(new Client(pesel, firstName, lastName, age, phoneNumber, address, sumPaidForAllRents));
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get lists all client");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return clients;
    }

    private List<Rent> getAllRent(String typeOfRents) throws ErrorMessageException {
        List<Rent> rent = new ArrayList<>();
        int status;
        if (typeOfRents.equals("actual"))
            status = 0;
        else
            status =1;

        try {
            ResultSet result = stat.executeQuery("SELECT * FROM rents where status = \"" + status + "\"");
            int id, typeOfVehicle, priceForRent, employeeID;
            String dateOfRental, dateOfReturn, clientID, vehicleID;
            Employee employee;
            int x = 0;
            int resultSize = countSizeResultSet(result);
            result = stat.executeQuery("SELECT * FROM rents where status = \"" + status + "\"");
            while(x< resultSize) {
                for (int i=0; i<x; i++ )
                    result.next();
                id = result.getInt("id");
                clientID = result.getString("clientID");
                vehicleID = result.getString("vehicleID");
                typeOfVehicle = result.getInt("type_of_vehicle");
                priceForRent = result.getInt("price_for_rent");
                dateOfRental = result.getString("date_Of_Rental");
                dateOfReturn = result.getString("date_Of_Return");
                employeeID = result.getInt("employee");
                employee = getEmployeeByID(employeeID);

                Client client = getClientByPesel(clientID);
                switch(typeOfVehicle) {
                    case 1: // vehicle is a car
                        Car car = getCarByID(vehicleID);
                        rent.add(new Rent(car, client, employee, id, priceForRent, dateOfRental, dateOfReturn));
                        break;

                    case 2: // vehicle is a bike
                        Bike bike = getbikeByID(vehicleID);
                        rent.add(new Rent(bike, client, employee, id, priceForRent, dateOfRental, dateOfReturn));
                        break;

                    case 3: // vehicle is a motorcycle
                        Motorcycle motorcycle = getMotorcycleByID(vehicleID);
                        rent.add(new Rent(motorcycle, client, employee, id, priceForRent, dateOfRental, dateOfReturn));
                        break;
                }
                x++;
                result = stat.executeQuery("SELECT * FROM rents where status = \"" + status + "\"");
                result.next();
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get lists all client");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return rent;
    }

    private List<Rent> getAllCLientsRents(String typeOfRents, String pesel) throws  ErrorMessageException {
        List<Rent> rent = new ArrayList<>();
        int status;
        String sql;
        if (typeOfRents.equals("actual"))
            status = 0;
        else
            status = 1;

        sql = "SELECT * FROM rents where clientID = " + "\"" + pesel  + "\" and status = "  + "\"" + status + "\"" ;
        try {
            ResultSet result = stat.executeQuery(sql);
            int id, typeOfVehicle, priceForRent, employeeID;
            String dateOfRental, dateOfReturn, clientID, vehicleID;
            Employee employee;
            int x = 0;
            int resultSize = countSizeResultSet(result);
            result = stat.executeQuery(sql);
            while(x< resultSize) {
                for (int i=0; i<x; i++ )
                    result.next();
                id = result.getInt("id");
                clientID = result.getString("clientID");
                vehicleID = result.getString("vehicleID");
                typeOfVehicle = result.getInt("type_of_vehicle");
                priceForRent = result.getInt("price_for_rent");
                dateOfRental = result.getString("date_Of_Rental");
                dateOfReturn = result.getString("date_Of_Return");
                employeeID = result.getInt("employee");
                employee = getEmployeeByID(employeeID);
                // get information About Client
                Client client = getClientByPesel(clientID);
                switch(typeOfVehicle) {
                    case 1: // vehicle is a car
                        Car car = getCarByID(vehicleID);
                        rent.add(new Rent(car, client, employee, id, priceForRent, dateOfRental, dateOfReturn));
                        break;
                    case 2: // vehicle is a bike
                        Bike  bike = getbikeByID(vehicleID);
                        rent.add(new Rent(bike, client, employee, id, priceForRent, dateOfRental, dateOfReturn));
                        break;

                    case 3: // vehicle is a motorcycle
                        Motorcycle motorcycle = getMotorcycleByID(vehicleID);
                        rent.add(new Rent(motorcycle, client, employee, id, priceForRent, dateOfRental, dateOfReturn));
                        break;
                }
                x++;
                result = stat.executeQuery(sql);
                result.next();
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get lists all client");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return rent;
    }

    private List<Car> getAllCars(String where) throws ErrorMessageException {
        List<Car> cars = new ArrayList<>();
        try {
            int status;
            if( where.equals("actual"))
                status = 0;
            else
                status = 1;
            ResultSet result = stat.executeQuery("SELECT * FROM cars where status = " + "\"" + status + "\"");
            int course, engineCapacity, trunkCapacity, numberOfDoors, pricePerDay, fuelInt, avaIntt;
            String id, name, model;
            boolean availability; Fuel fuel;
                while(result.next()) {
                    id = result.getString("id");
                    name = result.getString("name");
                    course = result.getInt("course");
                    avaIntt = result.getInt("availability");
                    model = result.getString("model");
                    fuelInt = result.getInt("fuel");
                    engineCapacity = result.getInt("engine_capacity");
                    trunkCapacity = result.getInt("trunk_capacity");
                    numberOfDoors = result.getInt("number_of_doors");
                    pricePerDay = result.getInt("price_per_day");

                    if (fuelInt == 0)
                        fuel = Fuel.petrol;
                    else
                        fuel = Fuel.diesel;
                    if (avaIntt == 0)
                        availability = true;
                    else
                        availability = false;
                    cars.add(new Car(id, name, course, availability, model, fuel, engineCapacity, trunkCapacity, numberOfDoors, pricePerDay));
                }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get lists all cars");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return cars;
    }

    private List<Car> getAllCars(int statusOfRent) throws ErrorMessageException {
        List<Car> cars = new ArrayList<>();
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM cars where availability = " + "\"" + statusOfRent + "\"");
            int course, engineCapacity, trunkCapacity, numberOfDoors, pricePerDay, fuelInt;
            String id, name, model, pesel;
            boolean availability; Fuel fuel;
            while(result.next()) {
                id = result.getString("id");
                name = result.getString("name");
                course = result.getInt("course");
                model = result.getString("model");
                fuelInt = result.getInt("fuel");
                engineCapacity = result.getInt("engine_capacity");
                trunkCapacity = result.getInt("trunk_capacity");
                numberOfDoors = result.getInt("number_of_doors");
                pricePerDay = result.getInt("price_per_day");

                if (fuelInt == 0)
                    fuel = Fuel.petrol;
                else
                    fuel = Fuel.diesel;
                if (statusOfRent == 0)
                    availability = true;
                else
                    availability = false;
                cars.add(new Car(id, name, course, availability, model, fuel, engineCapacity, trunkCapacity, numberOfDoors, pricePerDay));
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get lists all cars");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return cars;
    }

    private List<Bike> getAllBikes(String where) throws ErrorMessageException {
        List<Bike> bikes = new ArrayList<>();
        try {
            int status;
            if( where.equals("actual"))
                status = 0;
            else
                status = 1;
            ResultSet result = stat.executeQuery("SELECT * FROM bikes where status = " + "\"" + status + "\"");
            int course, pricePerDay, tireWidth, sizeOfWheele, ava;
            String id, name, typeOfBike, color;
            boolean availability;
            while(result.next()) {
                id = result.getString("id");
                name = result.getString("name");
                course = result.getInt("course");
                ava = result.getInt("availability");
                typeOfBike = result.getString("type_of_bike");
                color = result.getString("color");
                tireWidth = result.getInt("tire_width");
                sizeOfWheele = result.getInt("size_of_wheele");
                pricePerDay = result.getInt("price_per_day");

                if (ava == 0)
                    availability = true;
                else
                    availability = false;
                bikes.add(new Bike(id, name, course, availability, typeOfBike, color, tireWidth, sizeOfWheele,  pricePerDay));
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get lists all Bikes");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return bikes;
    }

    private List<Bike> getAllBikes(int statusOfRent) throws ErrorMessageException {
        List<Bike> bikes = new ArrayList<>();
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM bikes where availability = " + "\"" + statusOfRent + "\"");
            int course, pricePerDay, tireWidth, sizeOfWheele;
            String id, name, typeOfBike, color;
            boolean availability;
            while(result.next()) {
                id = result.getString("id");
                name = result.getString("name");
                course = result.getInt("course");
                typeOfBike = result.getString("type_of_bike");
                color = result.getString("color");
                tireWidth = result.getInt("tire_width");
                sizeOfWheele = result.getInt("size_of_wheele");
                pricePerDay = result.getInt("price_per_day");

                if (statusOfRent == 0)
                    availability = true;
                else
                    availability = false;
                bikes.add(new Bike(id, name, course, availability, typeOfBike, color, tireWidth, sizeOfWheele,  pricePerDay));
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get lists all Bikes");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return bikes;
    }

    private List<Motorcycle> getAllMotorcycles(String where) throws ErrorMessageException {
        List<Motorcycle> motorcycles = new ArrayList<>();
        try {
            int status;
            if( where.equals("actual"))
                status = 0;
            else
                status = 1;
            ResultSet result = stat.executeQuery("SELECT * FROM motorcycles where status = " + "\"" + status + "\"");
            int course, engineCapacity, pricePerDay, ava;
            String id, name, model;
            boolean availability;
            while(result.next()) {
                id = result.getString("id");
                name = result.getString("name");
                course = result.getInt("course");
                ava = result.getInt("availability");
                model = result.getString("model");
                engineCapacity = result.getInt("engine_capacity");
                pricePerDay = result.getInt("price_per_day");

                if (ava == 0)
                    availability = true;
                else
                    availability = false;
                motorcycles.add(new Motorcycle(id, name, course, availability, model, engineCapacity, pricePerDay));
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get lists all motorcycles");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return motorcycles;
    }

    private List<Motorcycle> getAllMotorcycles(int statusOfRent) throws ErrorMessageException {
        List<Motorcycle> motorcycles = new ArrayList<>();
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM motorcycles where availability = " + "\"" + statusOfRent + "\"");
            int course, engineCapacity, pricePerDay;
            String id, name, model;
            boolean availability;
            while(result.next()) {
                id = result.getString("id");
                name = result.getString("name");
                course = result.getInt("course");
                model = result.getString("model");
                engineCapacity = result.getInt("engine_capacity");
                pricePerDay = result.getInt("price_per_day");

                if (statusOfRent == 0)
                    availability = true;
                else
                    availability = false;
                motorcycles.add(new Motorcycle(id, name, course, availability, model, engineCapacity, pricePerDay));
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get lists all motorcycles");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
        return motorcycles;
    }

    private int getCarPricePerDay(String carID) throws ErrorMessageException {
        try {
            ResultSet CarResult = stat.executeQuery("SELECT * FROM cars where id=" + "\"" + carID + "\"");
            return CarResult.getInt("price_per_day");
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get price per day from cars table ");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
    }

    private int getBikePricePerDay(String bikeID) throws ErrorMessageException {
        try {
            ResultSet CarResult = stat.executeQuery("SELECT * FROM bikes where id=" + "\"" + bikeID + "\"");
            return CarResult.getInt("price_per_day");
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get price per day from bikes table ");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
    }

    private int getMotorcyclePricePerDay(String motorcycleID) throws ErrorMessageException {
        try {
            ResultSet CarResult = stat.executeQuery("SELECT * FROM motorcycles where id=" + "\"" +motorcycleID + "\"" );
            return CarResult.getInt("price_per_day");
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get price per day from motorcycles table ");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Error, please contact with administrator");
        }
    }

    private int countSizeResultSet(ResultSet result) throws SQLException {
        int size = 0;
        while(result.next()){
            size++;
        }
        return size;
    }

    private final String ClientTableStruct = "(pesel varchar(11) PRIMARY KEY," +
            "firstName varchar(30)," +
            "lastName varchar(50)," +
            "age int," +
            "phone_number int," +
            "address varchar(255)," +
            "sum_paid_for_all_rents int," +
            "status int"; // status 0 - actual, 1 - archival

    private final String EmployeeTableStruct = "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "firstName varchar(30)," +
            "lastName varchar(50)," +
            "phone_number int," +
            "status int" ;

    private final String RentTableStruct = "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "clientID varchar(11)," +
            "vehicleID varchar(10), " +
            "employee int," +
            "type_of_vehicle int," + // 1 - car, 2 - bike, 3 - motorcycle
            "price_for_rent int," +
            "date_of_rental varchar(10)," + // in format 01-23-2018 | DD-MM-YYYY
            "date_of_return varchar(10)," +  // in format 01-23-2018 | DD-MM-YYYY
            "status int,"; // status 0 - actual, 1 - archival;

    private final String CarTableStruct = "(id varchar(10) PRIMARY KEY ," +
            "name varchar(30)," +
            "course int," +
            "availability int," + // 0 - available, 1 - not available
            "type_of_vehicle int," + // 1 - car, 2 - bike, 3 - motorcycle
            "model varchar(30)," +
            "fuel int," + // 0 - petrol, 1 - diesel
            "engine_capacity int," +
            "trunk_capacity int," +
            "number_of_doors int," +
            "price_per_day int," +
            "status int,"; // status 0 - actual, 1 - archival;

    private final String BikeTableStruct = "(id varchar(10) PRIMARY KEY ," +
            "name varchar(30)," +
            "course int," +
            "type_of_vehicle int," + // 1 - car, 2 - bike, 3 - motorcycle
            "availability int," + // 0 - available, 1 - not available
            "type_of_bike varchar(30)," +
            "color varchar(20)," +
            "tire_width int," +
            "size_of_wheele int," +
            "price_per_day int," +
            "status int,"; // status 0 - actual, 1 - archival;

    private final String MotorcycleTableStruct = "(id varchar(10) PRIMARY KEY ," +
            "name varchar(30)," +
            "course int," +
            "type_of_vehicle int," + // 1 - car, 2 - bike, 3 - motorcycle
            "availability int," + // 0 - available, 1 - not available
            "model varchar(30)," +
            "engine_capacity int," +
            "price_per_day int," +
            "status int,"; // status 0 - actual, 1 - archival;

    /**
     * Setup Database
     */
    private void createTables() throws ErrorMessageException {
        // Relations
        String relationRents_Clients = " foreign key (clientID) references clients(pesel)";
        String relationRents_vehicle1 = " foreign key (vehicleID) references cars(id)";
        String relationRents_vehicle2 = " foreign key (vehicleID) references bikes(id)";
        String relationRents_vehicle3 = " foreign key (vehicleID) references motorcycles(id)";
        String relationTzpeVehicle_Rents = " foreign key (type_of_vehicle) references rents(type_of_vehicle)";
        String relationEmpolyee_Rent = " foreign key (employee) references employee(id)";

        // Creating tables definition
        String employeeTable = "CREATE TABLE IF NOT EXISTS employees" +  EmployeeTableStruct + ")";
        String clientsTable = "CREATE TABLE IF NOT EXISTS clients" +  ClientTableStruct + ")";
        String actualRentTable = "CREATE TABLE IF NOT EXISTS rents" +  RentTableStruct +  relationRents_Clients + relationRents_vehicle1 + relationRents_vehicle2 + relationRents_vehicle3 + relationEmpolyee_Rent + ")";
        String CarTable = "CREATE TABLE IF NOT EXISTS cars" +  CarTableStruct + relationTzpeVehicle_Rents + ")";
        String bikeTable= "CREATE TABLE IF NOT EXISTS bikes" +  BikeTableStruct + relationTzpeVehicle_Rents + ")";
        String motorcycleTable = "CREATE TABLE IF NOT EXISTS motorcycles" +  MotorcycleTableStruct + relationTzpeVehicle_Rents + ")";

        try {
            // Create tables
            stat.execute(employeeTable);
            stat.execute(clientsTable);
            stat.execute(actualRentTable);
            stat.execute(CarTable);
            stat.execute(bikeTable);
            stat.execute(motorcycleTable);
        } catch (SQLException e) {
            Logs.logger.warning("Can't create tables properly");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Contact with administrator");
        }
    }
}

