package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.CustomExceptions.UnknownClientTypeException;
import Model.CustomExceptions.WrongPeselException;
import Model.Logs;
import Model.Rent;
import Model.Client;
import Model.CustomEnumValues.Fuel;
import Model.Vehicles.Bike;
import Model.Vehicles.Car;
import Model.Vehicles.Motorcycle;
import Model.Vehicles.Vehicle;

public class DataBase {
    private Connection conn;
    private Statement stat;
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:biblioteka.db";

    private final String ClientTableStruct = "(pesel varchar(11) PRIMARY KEY," +
                                "firstName varchar(30)," +
                                "lastName varchar(50)," +
                                "age int," +
                                "phone_number int," +
                                "address varchar(255)," +
                                "sum_paid_for_all_rents int"; //


    private final String RentTableStruct = "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                    "clientID varchar(11)," +
                                    "vehicleID varchar(10), " +
                                    "type_of_vehicle int," + // 1 - car, 2 - bike, 3 - motorcycle
                                    "price_for_rent int," +
                                    "date_of_rental varchar(10)," + // in format 01-23-2018 | DD-MM-YYYY
                                    "date_of_return varchar(10)," ;  // in format 01-23-2018 | DD-MM-YYYY


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
                                    "price_per_day int,";

    private final String BikeTableStruct = "(id varchar(10) PRIMARY KEY ," +
                                "name varchar(30)," +
                                "course int," +
                                "type_of_vehicle int," + // 1 - car, 2 - bike, 3 - motorcycle
                                "availability int," + // 0 - available, 1 - not available
                                "type_of_bike varchar(30)," +
                                "color varchar(20)," +
                                "tire_width int," +
                                "size_of_wheele int," +
                                "price_per_day int," ;

    private final String MotorcycleTableStruct = "(id varchar(10) PRIMARY KEY ," +
                                "name varchar(30)," +
                                "course int," +
                                "type_of_vehicle int," + // 1 - car, 2 - bike, 3 - motorcycle
                                "availability int," + // 0 - available, 1 - not available
                                "model varchar(30)," +
                                "engine_capacity int," +
                                "price_per_day int,";


    /**
     * Setup Database
     */
    private void createTables()  {

    //Actual Relations
        String relationRents_Clients = " foreign key (clientID) references clients(pesel)";
        String relationRents_vehicle1 = " foreign key (vehicleID) references cars(id)";
        String relationRents_vehicle2 = " foreign key (vehicleID) references bikes(id)";
        String relationRents_vehicle3 = " foreign key (vehicleID) references motorcycles(id)";
        String relationTzpeVehicle_Rents = " foreign key (type_of_vehicle) references actual_rents(type_of_vehicle)";

    //Archival relation
        String archivalRelationTypeVehicle_Rents = " foreign key (type_of_vehicle) references archival_rents(type_of_vehicle)";
        String ArchivalRelationRents_Clients = " foreign key (clientID) references archival_clients(pesel)";
        String ArchivalRelrelationRents_vehicle1 = " foreign key (vehicleID) references archival_cars(id)";
        String ArchivalRelrelationRents_vehicle2 = " foreign key (vehicleID) references archival_bikes(id)";
        String ArchivalRelrelationRents_vehicle3 = " foreign key (vehicleID) references archival_motorcycles(id)";

    // Creating tables
        String clientsTable = "CREATE TABLE IF NOT EXISTS clients" +  ClientTableStruct + ")";
        String actualRentTable = "CREATE TABLE IF NOT EXISTS actual_rents" +  RentTableStruct +  relationRents_Clients + relationRents_vehicle1 + relationRents_vehicle2 + relationRents_vehicle3 + ")";
        String CarTable = "CREATE TABLE IF NOT EXISTS cars" +  CarTableStruct + relationTzpeVehicle_Rents + ")";
        String bikeTable= "CREATE TABLE IF NOT EXISTS bikes" +  BikeTableStruct + relationTzpeVehicle_Rents + ")";
        String motorcycleTable = "CREATE TABLE IF NOT EXISTS motorcycles" +  MotorcycleTableStruct + relationTzpeVehicle_Rents + ")";

    // Creating archiwal tables
        String archivalClientsTable = "CREATE TABLE IF NOT EXISTS archival_clients" +  ClientTableStruct + ")";
        String archivalRentTable = "CREATE TABLE IF NOT EXISTS archival_rents" +  RentTableStruct + ArchivalRelationRents_Clients + ArchivalRelrelationRents_vehicle1 + ArchivalRelrelationRents_vehicle2 + ArchivalRelrelationRents_vehicle3 + ")";
        String archivalCarTable = "CREATE TABLE IF NOT EXISTS archival_cars" +  CarTableStruct + archivalRelationTypeVehicle_Rents + ")";
        String archivalBikeTable= "CREATE TABLE IF NOT EXISTS archival_bikes" +  BikeTableStruct + archivalRelationTypeVehicle_Rents + ")";
        String archivalMotorcycleTable = "CREATE TABLE IF NOT EXISTS archival_motorcycles" +  MotorcycleTableStruct + archivalRelationTypeVehicle_Rents + ")";

        try {
// Actual tables
            stat.execute(clientsTable);
            stat.execute(actualRentTable);
            stat.execute(CarTable);
            stat.execute(bikeTable);
            stat.execute(motorcycleTable);
// Archiwal tables
            stat.execute(archivalClientsTable);
            stat.execute(archivalRentTable);
            stat.execute(archivalCarTable);
            stat.execute(archivalBikeTable);
            stat.execute(archivalMotorcycleTable);

//            stat.execute(relation1);
//
        } catch (SQLException e) {
            Logs.logger.warning("Can't create tables properly");
            e.printStackTrace();
        }
    }

    public DataBase() {
        try {
            Class.forName(DataBase.DRIVER);
        } catch (ClassNotFoundException e) {
            Logs.logger.warning("No driver JBC found");
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection(DB_URL);
            stat = conn.createStatement();
        } catch (SQLException e) {
            Logs.logger.warning("Problems with connection with dataBase");
            e.printStackTrace();
        }
        createTables();
    }

    /**
     *
     * @param where
     * @param pesel
     * @param firstName
     * @param lastName
     * @param age
     * @param phoneNumber
     * @param address
     * @return true if added properly, otherwise false
     */
    public boolean addClient(String where, String pesel, String firstName, String lastName, int age, int phoneNumber, String address) {
        try {
            PreparedStatement prepStmt;
            if(where.equals("actual"))
                 prepStmt = conn.prepareStatement(
                        "insert into clients(pesel, firstName, lastName, age, phone_number, address, sum_paid_for_all_rents) values (?, ?, ?, ?, ?, ?, ?);");
            else
                prepStmt = conn.prepareStatement(
                        "insert into archival_clients(pesel, firstName, lastName, age, phone_number, address, sum_paid_for_all_rents) values (?, ?, ?, ?, ?, ?, ?);");
            prepStmt.setString(1, pesel);
            prepStmt.setString(2, firstName);
            prepStmt.setString(3, lastName);
            prepStmt.setInt(4, age);
            prepStmt.setInt(5, phoneNumber);
            prepStmt.setString(6, address);
            prepStmt.setInt(7, 0); // sum paid for all rents
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to add client");
            Logs.logger.warning(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Add rent to table named "actual_rents"
     * @param clientID
     * @param vehicleID
     * @param typeOfVehicle
     * @param priceForRent
     * @param dateOfRental
     * @param dateOfReturn
     * @return true if added properly, otherwise false
     */
    public boolean addActualRent(String clientID, String vehicleID, int typeOfVehicle, int priceForRent, String dateOfRental, String dateOfReturn) {
        return addRent( "actual", clientID, vehicleID, typeOfVehicle, priceForRent, dateOfRental, dateOfReturn);
    }

    /**
     * Add rent to table named "archival_rents"
     * @param clientID
     * @param vehicleID
     * @param typeOfVehicle
     * @param priceForRent
     * @param dateOfRental
     * @param dateOfReturn
     * @return true if added properly, otherwise false
     */
    public boolean addArchivalRent(String clientID, String vehicleID, int typeOfVehicle, int priceForRent, String dateOfRental, String dateOfReturn) {
        return addRent( "archival", clientID, vehicleID, typeOfVehicle, priceForRent, dateOfRental, dateOfReturn);
    }

    /**
     *  Add car to database
     * @param where when given "actual" then add to table named "cars"  otherwise to archival_cars
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
    public boolean addCar(String where, String id, String name, int course, String model, Fuel fuel, int engineCapacity, int trunkCapacity, int numberOfDoors, int pricePerDay) {
        try {
            PreparedStatement prepStmt;
           if(where.equals("actual"))
                prepStmt = conn.prepareStatement(
                    "insert into cars(id, name, course, availability, model, fuel, engine_capacity, trunk_capacity, number_of_doors, price_per_day) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
           else
               prepStmt = conn.prepareStatement(
                       "insert into archival_cars(id, name, course, availability, model, fuel, engine_capacity, trunk_capacity, number_of_doors, price_per_day) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
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
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to add car to database");
            Logs.logger.warning(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     *
     * @param where
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
    public boolean addBike(String where, String id, String name, int course, String typeOfBike, String color, int tireWidth, int sizeOfWheele, int pricePerDay) {
        try {
            PreparedStatement prepStmt;
            if(where.equals("actual"))
                prepStmt = conn.prepareStatement(
                    "insert into bikes(id, name, course, availability, type_of_bike, color, tire_width, size_of_wheele, price_per_day) values (?, ?, ?, ?, ?, ?, ?, ?, ?);");
            else
                prepStmt = conn.prepareStatement(
                    "insert into archival_bikes(id, name, course, availability, type_of_bike, color, tire_width, size_of_wheele, price_per_day) values (?, ?, ?, ?, ?, ?, ?, ?, ?);");
            prepStmt.setString(1, id);
            prepStmt.setString(2, name);
            prepStmt.setInt(3, course);
            prepStmt.setInt(4, 0);
            prepStmt.setString(5, typeOfBike);
            prepStmt.setString(6, color);
            prepStmt.setInt(7, tireWidth);
            prepStmt.setInt(8, sizeOfWheele);
            prepStmt.setInt(9, pricePerDay);
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to add bike");
            Logs.logger.warning(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     *
     * @param where
     * @param id
     * @param name
     * @param course
     * @param modelF
     * @param engineCapacity
     * @param pricePerDay
     * @return true if added properly, otherwise false
     */
    public boolean addMotorcycle(String where, String id, String name, int course, String model, int engineCapacity, int pricePerDay) {
        try {
            PreparedStatement prepStmt;
            if(where.equals("actual"))
                prepStmt = conn.prepareStatement(
                    "insert into motorcycles(id, name, course, availability, model, engine_capacity, price_per_day) values (?, ?, ?, ?, ?, ?, ?);");
            else
                prepStmt = conn.prepareStatement(
                        "insert into archival_motorcycles(id, name, course, availability, model, engine_capacity, price_per_day) values (?, ?, ?, ?, ?, ?, ?);");
            prepStmt.setString(1, id);
            prepStmt.setString(2, name);
            prepStmt.setInt(3, course);
            prepStmt.setInt(4, 0);
            prepStmt.setString(5, model);
            prepStmt.setInt(6, engineCapacity);
            prepStmt.setInt(7, pricePerDay);
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to add motorcycle");
            Logs.logger.warning(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     *
     * @param clientPesel
     * @return true if client exists in archival_clients table, otherwise false
     */
    public boolean isClientExist(String clientPesel){
        ResultSet result;
        try {
            result = stat.executeQuery("SELECT * FROM archival_clients where pesel = " +  "\"" + clientPesel + "\"");
        } catch (SQLException e) {
            Logs.logger.warning("Error in sql query");
            Logs.logger.warning(e.getMessage());
            return false;
        }

        int size;
        try {
            size = countSizeResultSet(result);
        } catch (SQLException e) {
            Logs.logger.warning("Error when checking is client exists");
            Logs.logger.warning(e.getMessage());
            return false;
        }

        return size > 0;
    }

    /**
     * @return true if vehicle exists in archival table, otherwise false
     */
    public boolean isVehicleExists(String vehicleID, int type){
        ResultSet result = null;
        try {
            switch(type){
                case 1:
                    result = stat.executeQuery("SELECT * FROM archival_cars where id = " + "\"" + vehicleID + "\"");
                    break;
                case 2:
                    result = stat.executeQuery("SELECT * FROM archival_bikes where id = " + "\"" + vehicleID + "\"");
                    break;
                case 3:
                    result = stat.executeQuery("SELECT * FROM archival_motorcycles where id = " + "\"" + vehicleID + "\"");
                    break;
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error in sql query");
            Logs.logger.warning(e.getMessage());
            return false;
        }
        int size;
        try {
            size = countSizeResultSet(result);
        } catch (SQLException e) {
            Logs.logger.warning("Error when checking is vehicle exists");
            Logs.logger.warning(e.getMessage());
            return false;        }

        return size > 0;
    }

    public boolean setVehicleInaccessible(String vehicleID, int typeOfVehicle){
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
            return false;
        }
        return true;
    }

    public boolean setVehicleAccessible(String vehicleID, int typeOfVehicle){
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
            return false;
        }
        return true;
    }


    /**
     * Update amount of cash which client paid company
     * @param clientPesel
     * @param cashPaid
     * @return
     */
    public boolean updateClientSumPaid(String clientPesel, int cashPaid){
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "update clients set sum_paid_for_all_rents =" + cashPaid + " where pesel = " + "\"" + clientPesel + "\"");
            PreparedStatement prepStmt2 = conn.prepareStatement(
                    "update archival_clients set sum_paid_for_all_rents =" + cashPaid + " where pesel = " + "\"" + clientPesel + "\"");
            prepStmt.execute();
            if(this.isClientExist(clientPesel))
            prepStmt2.execute();

        } catch (SQLException e) {
            Logs.logger.warning("Error when try to update client sum");
            Logs.logger.warning(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Delete rent
     * @param rentID
     * @return
     */
    public boolean deleteRentFromActualRents(int rentID){
        PreparedStatement prepStmt;
        try {
            prepStmt = conn.prepareStatement(
                    "delete from actual_rents where id = " + "\"" + rentID + "\"");
            prepStmt.execute();

        } catch (SQLException e) {
            Logs.logger.warning("Error when try to delete rent");
            Logs.logger.warning(e.getMessage());
            return false;
        }
        return true;
    }

    public List<Rent> getClientActualRents(String clientPesel){
        try {
            return getAllCLientsRent("actual", clientPesel);
        } catch (WrongPeselException e) {
            Logs.logger.warning("Error when try to get Client Actual Rents");
            Logs.logger.warning(e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Rent> getClientArchivalRents(String clientPesel){
        try {
            return getAllCLientsRent("archival", clientPesel);
        } catch (WrongPeselException e) {
            Logs.logger.warning("Error when try to get Client Actual Rents");
            Logs.logger.warning(e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Client> getAllClients(String where) {
        List<Client> clients = new ArrayList<>();
        try {
            ResultSet result;
            if( where.equals("actual"))
                result = stat.executeQuery("SELECT * FROM clients");
            else
                result = stat.executeQuery("SELECT * FROM archival_clients");
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
        } catch (SQLException | WrongPeselException e) {
            Logs.logger.warning("Error when try to get lists all client");
            Logs.logger.warning(e.getMessage());
            return new ArrayList<>();
        }
        return clients;
    }

    public List<Rent> getAllActualRents() throws  WrongPeselException {
      return getAllRent("actual");
    }

    public List<Rent> getAllArchivalRents() throws  WrongPeselException {
        return getAllRent("archival");
    }

    public Rent getRentByRentID(int rentID) {
        int id = -1, typeOfVehicle, priceForRent;
        String dateOfRental, dateOfReturn, clientID, vehicleID;
        Car car = null; Bike bike = null; Motorcycle motorcycle = null; Client client;
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM actual_rents where id = " + "\"" + rentID  + "\"");
                clientID = result.getString("clientID");
                vehicleID = result.getString("vehicleID");
                typeOfVehicle = result.getInt("type_of_vehicle");
                priceForRent = result.getInt("price_for_rent");
                dateOfRental = result.getString("date_of_rental");
                dateOfReturn = result.getString("date_of_return");
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
        } catch (SQLException | WrongPeselException e) {
            Logs.logger.warning("Error when try to get rent by rentID");
            Logs.logger.warning(e.getMessage());
            return null;
        }
        if (car != null)
            return new Rent(car, client, rentID, priceForRent, dateOfRental, dateOfReturn);
        if(bike != null)
            return new Rent(bike, client, rentID, priceForRent, dateOfRental, dateOfReturn);
        return new Rent(motorcycle, client, rentID, priceForRent, dateOfRental, dateOfReturn);
    }

    public Car getCarByID (String carID){
        ResultSet CarResult;
        int course, fuel, engineCapacity, trunkCapacity, numberOfDoors, pricePerDay, availability;
        boolean avail;
        Fuel fuelEnum;
        String name, model;
        try {
            CarResult = stat.executeQuery("SELECT * FROM cars where id=" + '\"' + carID  + '\"');
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
            return null;
        }
        return new Car (carID, name, course, avail, model, fuelEnum, engineCapacity, trunkCapacity, numberOfDoors, pricePerDay);
    }

    public Car getArchivalCarByID (String carID){
        ResultSet CarResult;
        int course, fuel, engineCapacity, trunkCapacity, numberOfDoors, pricePerDay, availability;
        boolean avail;
        Fuel fuelEnum;
        String name, model;
        try {
            CarResult = stat.executeQuery("SELECT * FROM archival_cars where id=" + '\"' + carID  + '\"');
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
            return null;
        }
        return new Car (carID, name, course, avail, model, fuelEnum, engineCapacity, trunkCapacity, numberOfDoors, pricePerDay);
    }

    public Bike getbikeByID (String bikeID){
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
            return null;
        }
        return new Bike(bikeID, name, course, avail, typeOfBike, color, tireWidth, sizeOfWheele, pricePerDay);
    }

    public Bike getArchivalBikeByID (String bikeID){
        ResultSet BikeResult;
        int course, pricePerDay, tireWidth, sizeOfWheele, availability;
        boolean avail;
        String name, typeOfBike, color;
        try {
            BikeResult = stat.executeQuery("SELECT * FROM archival_bikes where id=" + '\"' + bikeID + '\"');
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
            return null;
        }
        return new Bike(bikeID, name, course, avail, typeOfBike, color, tireWidth, sizeOfWheele, pricePerDay);
    }

    public Motorcycle getMotorcycleByID (String motorcycleID){
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
            return null;
        }
        return new Motorcycle(motorcycleID, name, course, avail, model, engineCapacity, pricePerDay);
    }

    public Motorcycle getArchivalMotorcycleByID (String motorcycleID){
        ResultSet MotorcycleResult;
        int course, pricePerDay, engineCapacity, availability;
        boolean avail;
        String name, model;
        try {
            MotorcycleResult = stat.executeQuery("SELECT * FROM archival_motorcycles where id=" + "\"" + motorcycleID + "\"" );
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
            return null;
        }
        return new Motorcycle(motorcycleID, name, course, avail, model, engineCapacity, pricePerDay);
    }

    public Client getClientByPesel(String pesel) throws WrongPeselException {
        ResultSet ClientResult;
        int phoneNumber, type, sumPaidForAllRents, age;
        boolean avail = false;
        String firstName, lastName, address;
        try {
            ClientResult = stat.executeQuery("SELECT * FROM clients where pesel=" + pesel);
                firstName = ClientResult.getString("firstName");
                lastName = ClientResult.getString("lastName");
                age = ClientResult.getInt("age");
                phoneNumber = ClientResult.getInt("phone_number");
                address = ClientResult.getString("address");
                sumPaidForAllRents = ClientResult.getInt("sum_paid_for_all_rents");
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get client by clientPesel");
            Logs.logger.warning(e.getMessage());
            return null;
        }
        return new Client(pesel, firstName, lastName, age, phoneNumber, address, sumPaidForAllRents);
    }

    public Client getArchivalClientByPesel(String pesel) throws WrongPeselException {
        ResultSet ClientResult;
        int phoneNumber, type, sumPaidForAllRents, age;
        boolean avail = false;
        String firstName, lastName, address;
        try {
            ClientResult = stat.executeQuery("SELECT * FROM archival_clients where pesel=" + pesel);
            firstName = ClientResult.getString("firstName");
            lastName = ClientResult.getString("lastName");
            age = ClientResult.getInt("age");
            phoneNumber = ClientResult.getInt("phone_number");
            address = ClientResult.getString("address");
            sumPaidForAllRents = ClientResult.getInt("sum_paid_for_all_rents");
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get client by clientPesel");
            Logs.logger.warning(e.getMessage());
            return null;
        }
        return new Client(pesel, firstName, lastName, age, phoneNumber, address, sumPaidForAllRents);
    }

    public int getPricePerDayOfVehicle(Vehicle vehicle){
       int pricePerDay = 0;
       String vehicleID = vehicle.getId();
       if(vehicle instanceof Car)
           pricePerDay = getCarPricePerDay(vehicleID);

       else if(vehicle instanceof Bike)
           pricePerDay = getBikePricePerDay(vehicleID);

       if(vehicle instanceof Motorcycle)
           pricePerDay = getMotorcyclePricePerDay(vehicleID);
      return pricePerDay;
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            Logs.logger.warning("Close connection error");
        }
    }


    private List<Rent> getAllRent(String typeOfRents) throws WrongPeselException {
        List<Rent> rent = new ArrayList<>();
        String sql;
        if (typeOfRents.equals("actual"))
            sql = "SELECT * FROM actual_rents";
        else
            sql = "SELECT * FROM archival_rents";

        try {
            ResultSet result = stat.executeQuery(sql);
            int id, typeOfVehicle, priceForRent;
            String dateOfRental, dateOfReturn, clientID, vehicleID;

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

                // get information About Client

                Client client;
                if (typeOfRents.equals("actual"))
                    client = getClientByPesel(clientID);
                else
                    client = getArchivalClientByPesel(clientID);
                switch(typeOfVehicle) {
                    case 1: // vehicle is a car
                        Car car;
                        if (typeOfRents.equals("actual"))
                            car = getCarByID(vehicleID);
                        else
                            car = getArchivalCarByID(vehicleID);
                        rent.add(new Rent(car, client, id, priceForRent, dateOfRental, dateOfReturn));
                        break;

                    case 2: // vehicle is a bike
                        Bike bike;
                        if (typeOfRents.equals("actual"))
                            bike = getbikeByID(vehicleID);
                        else
                            bike = getArchivalBikeByID(vehicleID);
                        rent.add(new Rent(bike, client, id, priceForRent, dateOfRental, dateOfReturn));
                        break;

                    case 3: // vehicle is a motorcycle
                        Motorcycle motorcycle;
                        if (typeOfRents.equals("actual"))
                            motorcycle = getMotorcycleByID(vehicleID);
                        else
                            motorcycle = getArchivalMotorcycleByID(vehicleID);
                        rent.add(new Rent(motorcycle, client, id, priceForRent, dateOfRental, dateOfReturn));
                        break;
                }
                x++;
                result = stat.executeQuery(sql);
                result.next();
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get lists all client");
            Logs.logger.warning(e.getMessage());
            return new ArrayList<>();
        }
        return rent;
    }

    private List<Rent> getAllCLientsRent(String typeOfRents, String pesel) throws WrongPeselException {
        List<Rent> rent = new ArrayList<>();
        String sql;
        if (typeOfRents.equals("actual"))
            sql = "SELECT * FROM actual_rents where clientID = " + "\"" + pesel  + "\"";
        else
            sql = "SELECT * FROM archival_rents where clientID = " + "\"" + pesel  + "\"";

        try {
            ResultSet result = stat.executeQuery(sql);
            int id, typeOfVehicle, priceForRent;
            String dateOfRental, dateOfReturn, clientID, vehicleID;

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

                // get information About Client

                Client client;
                if (typeOfRents.equals("actual"))
                    client = getClientByPesel(clientID);
                else
                    client = getArchivalClientByPesel(clientID);
                switch(typeOfVehicle) {
                    case 1: // vehicle is a car
                        Car car;
                        if (typeOfRents.equals("actual"))
                            car = getCarByID(vehicleID);
                        else
                            car = getArchivalCarByID(vehicleID);
                        rent.add(new Rent(car, client, id, priceForRent, dateOfRental, dateOfReturn));
                        break;

                    case 2: // vehicle is a bike
                        Bike bike;
                        if (typeOfRents.equals("actual"))
                            bike = getbikeByID(vehicleID);
                        else
                            bike = getArchivalBikeByID(vehicleID);
                        rent.add(new Rent(bike, client, id, priceForRent, dateOfRental, dateOfReturn));
                        break;

                    case 3: // vehicle is a motorcycle
                        Motorcycle motorcycle;
                        if (typeOfRents.equals("actual"))
                            motorcycle = getMotorcycleByID(vehicleID);
                        else
                            motorcycle = getArchivalMotorcycleByID(vehicleID);
                        rent.add(new Rent(motorcycle, client, id, priceForRent, dateOfRental, dateOfReturn));
                        break;
                }
                x++;
                result = stat.executeQuery(sql);
                result.next();
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get lists all client");
            Logs.logger.warning(e.getMessage());
            return new ArrayList<>();
        }
        return rent;
    }

    private boolean addRent(String typeOfRent, String clientID, String vehicleID, int typeOfVehicle, int priceForRent, String dateOfRental, String dateOfReturn) {

        PreparedStatement prepStmt;
        try {
            if(typeOfRent.equals("actual"))
                prepStmt = conn.prepareStatement(
                        "insert into actual_rents(clientID, vehicleID, type_of_vehicle, price_for_rent, date_of_rental, date_of_return) values ( ?, ?, ?, ?, ?, ?);");
            else

//                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
//                        "clientID varchar(11)," +
//                        "vehicleID varchar(10), " +
//                        "type_of_vehicle int," + // 1 - car, 2 - bike, 3 - motorcycle
//                        "price_for_rent int," +
//                        "date_of_rental varchar(10)," + // in format 01-23-2018 | DD-MM-YYYY
//                        "date_of_return varchar(10)," ;  //
                prepStmt = conn.prepareStatement(
                        "insert into archival_rents (clientID, vehicleID, type_of_vehicle, price_for_rent, date_of_rental, date_of_return) values (?, ?, ?, ?, ?, ?);");
            prepStmt.setString(1, clientID);
            prepStmt.setString(2, vehicleID);
            prepStmt.setInt(3, typeOfVehicle);
            prepStmt.setInt(4, priceForRent);
            prepStmt.setString(5, dateOfRental);
            prepStmt.setString(6, dateOfReturn);
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to add " + typeOfRent + " rent");
            Logs.logger.warning(e.getMessage());
            return false;
        }
        return true;
    }

    private int getCarPricePerDay(String carID) {
        try {
            ResultSet CarResult = stat.executeQuery("SELECT * FROM cars where id=" + carID);
            return CarResult.getInt("price_per_day");
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get price per day from cars table ");
            Logs.logger.warning(e.getMessage());
            return 0;
        }
    }

    private int getBikePricePerDay(String bikeID) {
        try {
            ResultSet CarResult = stat.executeQuery("SELECT * FROM bikes where id=" + bikeID);
            return CarResult.getInt("price_per_day");
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get price per day from bikes table ");
            Logs.logger.warning(e.getMessage());
            return 0;
        }
    }

    private int getMotorcyclePricePerDay(String motorcycleID) {
        try {
            ResultSet CarResult = stat.executeQuery("SELECT * FROM motorcycles where id=" + motorcycleID);
            return CarResult.getInt("price_per_day");
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get price per day from motorcycles table ");
            Logs.logger.warning(e.getMessage());
            return 0;
        }
    }

    private int countSizeResultSet(ResultSet result) throws SQLException {
        int size = 0;
        while(result.next()){
            size++;
        }
        return size;
    }
}

