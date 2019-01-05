package database;

import java.sql.*;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import model.enums.*;
import model.exceptions.ErrorMessageException;
import model.Customer;
import model.Employee;
import model.Logs;
import model.Rent;
import model.vehicles.Bike;
import model.vehicles.Car;
import model.vehicles.Motorcycle;
import model.vehicles.Vehicle;

public class DatabaseManager  {
    private static Connection conn;
    private static Statement stat;

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "root";


    public static void connect() throws ErrorMessageException {
        try {
            Class.forName(DatabaseManager.JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            Logs.logger.warning("No driver JBC found");
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }

        try {

            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stat = conn.createStatement();
        } catch (SQLException e) {
            Logs.logger.warning("Problems with connection with dataBase");
            Logs.logger.warning(e.toString());

            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        createTables();
    }

    public static void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            Logs.logger.warning("Close connection error");
        }
    }
    public static void closeAndClearConnection(){
        String dropDatabase = "drop database vehicle_rental;";
        try {
            stat.execute(dropDatabase);
        } catch (SQLException e) {
            e.toString();
        }

    }



    public static void addCustomer(String firstName, String lastName, String peselNumber, String address, int phoneNumber, String email, String companyName,
                                   String nipNumber, String companyAddres) throws ErrorMessageException {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "insert into customers(peselNumber, firstName, lastName, address, phoneNumber, email, companyName, nipNumber, companyAddress," +
                            " sum_paid_for_all_rents, status) values (?,?,?,?,?,?,?,?,?,?,?);");
            prepStmt.setString(1, peselNumber);
            prepStmt.setString(2, firstName);
            prepStmt.setString(3, lastName);
            prepStmt.setString(4, address);
            prepStmt.setInt(5, phoneNumber);
            prepStmt.setString(6, email);
            prepStmt.setString(7, companyName);
            prepStmt.setString(8, nipNumber);
            prepStmt.setString(9, companyAddres);
            prepStmt.setDouble(10, 0); // sum paid for all rents
            prepStmt.setInt(11, 0); // status of customer
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to add customer");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    public static void updateCustomerSumPaid(String peselNumber, double cashPaid) throws ErrorMessageException {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "update customers set sum_paid_for_all_rents =" + cashPaid + " where peselNumber = " + "\"" + peselNumber + "\" ");
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to update customer sum");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    public static void editCustomer(String firstName, String lastName, String peselNumber, String address, int phoneNumber, String email, String companyName,
                                    String nipNumber, String companyAddres) throws ErrorMessageException{
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "update customers set" +
                            " firstName = " + "\"" + firstName + "\"" +
                            " ,lastName = " +"\""  + lastName + "\""  +
                            " ,address = " + "\"" + address +"\""  +
                            ", phoneNumber = " + "\"" + phoneNumber + "\"" +
                            " ,email = " +"\""  + email +"\""  +
                            " ,companyName = " +"\""  + companyName +"\""  +
                            " ,nipNumber = " +"\""  + nipNumber +"\""  +
                            " ,companyAddress = " +"\""  + companyAddres +"\""  +
                            " where peselNumber = " + "\"" + peselNumber + "\" ");
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to update customer data");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    public static List<Customer> getAllActualCustomers() throws ErrorMessageException {
        return getAllCustomers("actual");
    }

    public static List<Customer> getAllArchivalCustomers() throws ErrorMessageException {
        return getAllCustomers("archival");
    }

    public static List<Customer> getFilteredCustomers(String peselNumber, String firstName, String lastName) throws ErrorMessageException {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customers  ";
        if(!(peselNumber == null))
            query += "where peselNumber LIKE \"%" + peselNumber + "%\" ";
        if (!(firstName == null) && query.contains("LIKE"))
            query += " AND firstName LIKE \"%" + firstName + "%\" ";
        if (!(firstName == null) && !query.contains("LIKE"))
            query += " where firstName LIKE \"%" + firstName + "%\" ";
        if (!(lastName == null) && query.contains("LIKE"))
            query += " AND lastName LIKE \"%" + lastName+ "%\" ";
        if (!(lastName == null) && !query.contains("LIKE"))
            query += " where lastName LIKE \"%" + lastName + "%\" ";
        try {
            ResultSet result = stat.executeQuery(query);
            int phoneNumber;
            String firstName_, lastName_, address, pesel, email, companyName, nipNumber, companyAddress;
            double sumPaidForAllRents;
            while (result.next()) {
                pesel = result.getString("peselNumber");
                firstName_ = result.getString("firstName");
                lastName_ = result.getString("lastName");
                address = result.getString("address");
                phoneNumber = result.getInt("phoneNumber");
                email = result.getString("email");
                companyName = result.getString("companyName");
                nipNumber = result.getString("nipNumber");
                companyAddress = result.getString("companyAddress");
                sumPaidForAllRents = result.getDouble("sum_paid_for_all_rents");
                customers.add(new Customer(firstName_, lastName_, pesel, address, phoneNumber, email, companyName,
                        nipNumber, companyAddress, sumPaidForAllRents));
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get filtered lists all customer");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        return customers;
    }


    public static Customer getCustomerByPesel(String peselNumber) throws  ErrorMessageException {
        ResultSet customerResult;
        int phoneNumber = -1; double sumPaidForAllRents = -1;
        String peselNumber_  = null, firstName = null, lastName = null, address = null, email = null, companyName = null, nipNumber = null, companyAddress = null;
        try {
            customerResult = stat.executeQuery("SELECT * FROM customers where peselNumber =" + "\"" + peselNumber + "\"");
            while(customerResult.next()) {
                peselNumber_ = customerResult.getString("peselNumber");
                firstName = customerResult.getString("firstName");
                lastName = customerResult.getString("lastName");
                address = customerResult.getString("address");
                phoneNumber = customerResult.getInt("phoneNumber");
                email = customerResult.getString("email");
                companyName = customerResult.getString("companyName");
                nipNumber = customerResult.getString("nipNumber");
                companyAddress = customerResult.getString("companyAddress");
                sumPaidForAllRents = customerResult.getDouble("sum_paid_for_all_rents");
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get Customer by Customer Pesel");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        return new Customer(firstName, lastName, peselNumber_, address, phoneNumber, email, companyName, nipNumber, companyAddress, sumPaidForAllRents);
    }

    public static void markCustomerAsArchival(String peselNumber) throws ErrorMessageException{
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "update customers set status = 1 where peselNumber = " + "\"" + peselNumber + "\"");
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to change Customer as archival");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    public static List<Rent> getCustomerDuringRents(String peselNumber) throws ErrorMessageException {
        try {
            return getAllCustomerRents(RentStatus.during, peselNumber);
        } catch (Exception e) {
            Logs.logger.warning("Error when try to get Customer Actual Rents");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    public static List<Rent> getCustomerEndedRents(String peselNumber) throws ErrorMessageException {
        try {
            return getAllCustomerRents(RentStatus.ended, peselNumber);
        } catch (Exception e) {
            Logs.logger.warning("Error when try to get Customer Actual Rents");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    public static void addRent(String customerID, String vehicleID, VehicleType typeOfVehicle, double totalPrice, Date startDate, Date endDate, int employeeID) throws ErrorMessageException {
        final long hours12 = 12L * 60L * 60L * 1000L;

        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        Date start;
        if(hour < 12 )
            start =  new Date(startDate.getTime() + hours12);
       else
            start =  new Date(startDate.getTime());
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "insert into rents(customerID, vehicleID, vehicleType, totalPrice, startDate, endDate, status, employeeID) values ( ?, ?, ?, ?, ?, ?, ?, ?);");
            prepStmt.setString(1, customerID);
            prepStmt.setString(2, vehicleID);
            prepStmt.setString(3, typeOfVehicle.name());
            prepStmt.setDouble(4, totalPrice);
            prepStmt.setDate(5, start);
            prepStmt.setDate(6, new Date(endDate.getTime() + hours12));
            prepStmt.setString(7, RentStatus.during.name());
            prepStmt.setInt(8, employeeID);
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to add rent");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    public static void updateRentPrice(int rentID, double cashPaid) throws ErrorMessageException {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "update rents set totalPrice =" + cashPaid + " where id = " + "\"" + rentID + "\"");
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to delete rent");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }
    public static void updateRentDate(int rentID, Date returnDate) throws ErrorMessageException{
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "update rents set endDate =\"" + returnDate + "\" where id = " + "\"" + rentID + "\"");
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to update rent end date");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    public static void markRentAsArchival(int rentID) throws ErrorMessageException {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "update rents set status = \"" + RentStatus.ended+  "\" where id = " + "\"" + rentID + "\"");
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to delete rent");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }


    public static List<Rent> getAllDuringRents() throws  ErrorMessageException {
        return getAllRent(RentStatus.during);
    }

    public static List<Rent> getAllEndedRents() throws ErrorMessageException {
        return getAllRent(RentStatus.ended);
    }

    public static Rent getRentByRentID(int rentID) throws ErrorMessageException {
        int employeeID; double priceForRent;
        String typeOfVehicle, customerID, vehicleID, status;
        Customer customer; Vehicle vehicle = null; Date dateOfRental, dateOfReturn;
        Employee employee;
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM rents where id = " + "\"" + rentID  + "\"");
            result.next();
            customerID = result.getString("customerID");
            vehicleID = result.getString("vehicleID");
            typeOfVehicle = result.getString("vehicleType");
            status = result.getString("status");
            priceForRent = result.getDouble("totalPrice");
            dateOfRental = result.getDate("startDate");
            dateOfReturn = result.getDate("endDate");
            employeeID = result.getInt("employeeID");
            employee = getEmployeeByID(employeeID);
            customer  = getCustomerByPesel(customerID);

            switch (VehicleType.valueOf(typeOfVehicle)) {
                case car:
                    vehicle = getCarByID(vehicleID);
                    break;
                case bike:
                    vehicle = getbikeByID(vehicleID);
                    break;
                case motorcycle:
                    vehicle = getMotorcycleByID(vehicleID);
                    break;
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get rent by rentID");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        return new Rent(vehicle, customer, employee, rentID, priceForRent, dateOfRental, dateOfReturn, RentStatus.valueOf(status));
    }

    public static List<Rent> getFilteredRents(String vehicleID, String peselNumber, int employeeID, String rentTypeStartMin, String rentTypeStartMax ,Date startDateMin,
                                        Date startDateMax, String rentTypeEndMin, String rentTypeEndMax, Date endDateMin, Date endDateMax, String costTypeMin,
                                        String costTypeMax, double costMin, double costMax, String customerLastName, String employeeLastName, RentStatus rentStatus) throws  ErrorMessageException{

        String query = "Select * from rents ";

        if (!(customerLastName == null))
        query += " JOIN customers ON (customerID = peselNumber) ";

        if (!(employeeLastName == null))
            query += " JOIN employees ON (employeeID = UUID) ";

        if (!(employeeLastName == null) && !query.contains("where"))
            query += " where lastName LIKE \"%" + employeeLastName + "%\" ";
        if (!(employeeLastName == null) && query.contains("where"))
            query += " AND lastName LIKE \"%" + employeeLastName + "%\" ";

        if (!(customerLastName == null) && !query.contains("where"))
            query += " where lastName LIKE \"%" + customerLastName + "%\" ";
        if (!(customerLastName == null) && query.contains("where"))
            query += " AND lastName LIKE \"%" + customerLastName + "%\" ";

    //vehicle id
        if (!(vehicleID == null))
            query += "where vehicleID LIKE \"%" + vehicleID + "%\" ";
    //pesel number
        if (!(peselNumber == null) && !query.contains("where"))
            query += "where customerID LIKE \"%" + peselNumber + "%\" ";
        if (!(peselNumber == null) && query.contains("where"))
            query += " AND customerID LIKE \"%" + peselNumber + "%\" ";
    // employeeID
        if (!(employeeID == -1) && !query.contains("where"))
            query += "where employeeID=\"" + employeeID + "\" ";
        if (!(employeeID == -1) && query.contains("where"))
            query += " AND employeeID=\"" + employeeID + "\" ";

    // rentTypeStartMin
        if(!(rentTypeStartMin == null) && !(startDateMin == null) && !query.contains("where"))
            query += " where startDate " + rentTypeStartMin + " \"" + startDateMin + "\" ";
        if(!(rentTypeStartMin == null) && !(startDateMin == null) && query.contains("where"))
            query += " AND startDate " + rentTypeStartMin + " \"" + startDateMin + "\" ";

    // rentTypeStartMax
        if(!(rentTypeStartMax == null) && !(startDateMax == null) && !query.contains("where"))
            query += " where startDate " + rentTypeStartMax + " \"" + startDateMax + "\" ";
        if(!(rentTypeStartMax == null) && !(startDateMax == null) && query.contains("where"))
            query += " AND startDate " + rentTypeStartMax + " \"" + startDateMax + "\" ";

    // rentTypeEndMin
        if(!(rentTypeEndMin == null) && !(endDateMin == null) && !query.contains("where"))
            query += " where endDate " + rentTypeEndMin + " \"" + endDateMin + "\" ";
        if(!(rentTypeEndMin == null) && !(endDateMin == null) && query.contains("where"))
            query += " AND endDate " + rentTypeEndMin + " \"" + endDateMin + "\" ";

    // rentTypeEndMax
        if(!(rentTypeEndMax == null) && !(endDateMax == null) && !query.contains("where"))
            query += " where endDate " + rentTypeEndMax + " \"" + endDateMax + "\" ";
        if(!(rentTypeEndMax == null) && !(endDateMax == null) && query.contains("where"))
            query += " AND endDate " + rentTypeEndMax + " \"" + endDateMax + "\" ";

    // priceMin
        if(!(costTypeMin == null) && !(costMin == -1) && !query.contains("where"))
            query += " where totalPrice " + costTypeMin + " \"" + costMin + "\" ";
        if(!(costTypeMin == null) && !(costMin == -1) && query.contains("where"))
            query += " AND totalPrice " + costTypeMin + " \"" + costMin + "\" ";

    // priceMax
        if(!(costTypeMax == null) && !(costMax == -1) && !query.contains("where"))
            query += " where totalPrice " + costTypeMax + " \"" + costMax + "\" ";
        if(!(costTypeMax == null) && !(costMax == -1) && query.contains("where"))
            query += " AND totalPrice " + costTypeMax + " \"" + costMax + "\" ";

        if (!(rentStatus == null) && !query.contains("where"))
            query += " where status=\"" + rentStatus.name() + "\" ";
        if (!(rentStatus == null) && query.contains("where"))
            query += " AND status=\"" + rentStatus.name() + "\" ";

        List<Rent> rent = new ArrayList<>();
        try {
            ResultSet result = stat.executeQuery(query);
            int id, employeeID_; double priceForRent;
            String typeOfVehicle, customerID, vehicleID_, status;
            Employee employee; Customer customer; Vehicle vehicle= null; Date dateOfRental, dateOfReturn;
            int x = 0;
            int resultSize = countSizeResultSet(result);
            result = stat.executeQuery(query);
            result.next();
            while(x< resultSize) {
                for (int i=0; i<x; i++ )
                    result.next();
                id = result.getInt("id");
                customerID = result.getString("customerID");
                vehicleID_ = result.getString("vehicleID");
                typeOfVehicle = result.getString("vehicleType");
                priceForRent = result.getDouble("totalPrice");
                dateOfRental = result.getDate("startDate");
                dateOfReturn = result.getDate("endDate");
                employeeID_ = result.getInt("employeeID");
                status = result.getString("status");
                employee = getEmployeeByID(employeeID_);
                customer  = getCustomerByPesel(customerID);


                switch (VehicleType.valueOf(typeOfVehicle)) {
                    case car:
                        vehicle = getCarByID(vehicleID_);
                        break;
                    case bike:
                        vehicle = getbikeByID(vehicleID_);
                        break;
                    case motorcycle:
                        vehicle = getMotorcycleByID(vehicleID_);
                        break;
                }
                rent.add(new Rent(vehicle, customer, employee, id, priceForRent, dateOfRental, dateOfReturn, RentStatus.valueOf(status)));
                x++;
                result = stat.executeQuery(query);
                result.next();
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get filtered rents");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        return rent;

    }

    public static void addCar(String id, String name, double dailyPrice, String color, int productionYear,
                                 int mileage, double engineCapacity, double fuelUsage, FuelType fuelType,
                                 int numberOffPersons) throws ErrorMessageException {
        try {
            PreparedStatement prepStmt;
            prepStmt = conn.prepareStatement(
                    "insert into cars(id, vehicleStatus, vehicleType, name, dailyPrice, color, productionYear, mileage," +
                            "engineCapacity, FuelType, fuelUsage, numberOffPersons) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? );");
            prepStmt.setString(1, id);
            prepStmt.setString(2, VehicleStatus.avaiable.name());
            prepStmt.setString(3, VehicleType.car.name());
            prepStmt.setString(4, name);
            prepStmt.setDouble(5, dailyPrice);
            prepStmt.setString(6, color);
            prepStmt.setInt(7, productionYear);
            prepStmt.setInt(8, mileage);
            prepStmt.setDouble(9, engineCapacity);
            prepStmt.setString(10,fuelType.name());
            prepStmt.setDouble(11, fuelUsage);
            prepStmt.setInt(12, numberOffPersons);
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to add car to database");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    public static void updateCar(String id, String name, double dailyPrice, String color, int productionYear,
                                 int mileage, double engineCapacity, double fuelUsage, FuelType fuelType,
                                 int numberOffPersons) throws ErrorMessageException{
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "update cars set" +
                            " name = " + "\"" + name + "\"" +
                            " ,dailyPrice = " +"\""  + dailyPrice + "\""  +
                            " ,color = " + "\"" + color +"\""  +
                            ", productionYear = " + "\"" + productionYear + "\"" +
                            " ,mileage = " +"\""  + mileage +"\""  +
                            " ,engineCapacity = " +"\""  + engineCapacity +"\""  +
                            " ,fuelUsage = " +"\""  + fuelUsage +"\""  +
                            " ,fuelType = " +"\""  + fuelType.name() +"\""  +
                            " ,numberOffPersons = " +"\""  + numberOffPersons +"\""  +
                            " where id  = " + "\"" + id + "\" ");

            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to edit car ");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    public static Car getCarByID (String carID) throws ErrorMessageException {
        ResultSet CarResult;
        int productionYear = -1,numberOffPersons = -1, mileage = -1;
        double dailyPrice= -1, engineCapacity = -1, fuelUsage = -1;
        String vehicleStatus =  null, vehicleType = null ,name = null, color = null, fuelType = null, id = null;
        try {
            CarResult = stat.executeQuery("SELECT * FROM cars where id=" + '\"' + carID  + '\"' );
            while(CarResult.next()) {
                id = CarResult.getString("id");
                vehicleStatus = CarResult.getString("vehicleStatus");
                vehicleType = CarResult.getString("vehicleType");
                name = CarResult.getString("name");
                dailyPrice = CarResult.getDouble("dailyPrice");
                color = CarResult.getString("color");
                productionYear = CarResult.getInt("productionYear");
                mileage = CarResult.getInt("mileage");
                engineCapacity = CarResult.getDouble("engineCapacity");
                fuelType = CarResult.getString("FuelType");
                fuelUsage = CarResult.getDouble("fuelUsage");
                numberOffPersons = CarResult.getInt("numberOffPersons");
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get car by carID");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        if (id == null)
        return new Car(id, VehicleStatus.archived, VehicleType.car, name, dailyPrice,
                color, productionYear, mileage, engineCapacity, fuelUsage, FuelType.diesel,numberOffPersons);
        return new Car(id, VehicleStatus.valueOf(vehicleStatus), VehicleType.valueOf(vehicleType), name, dailyPrice,
                String.valueOf(color),productionYear, mileage, engineCapacity, fuelUsage, FuelType.valueOf(fuelType),numberOffPersons);
    }

    public static List<Car> getAllAvaiableCars() throws ErrorMessageException{
        return getAllCars(VehicleStatus.avaiable);

    }

    public static List<Car> getAllABorrowedCars() throws ErrorMessageException{
        return getAllCars(VehicleStatus.unavaiable);

    }

    public static List<Car> getAllAArchivalCars() throws ErrorMessageException{
        return getAllCars(VehicleStatus.archived);
    }

    public static void addBike(String id, String name, double dailyPrice,
                               String color, int productionYear) throws ErrorMessageException {
        try {
            PreparedStatement prepStmt;
            prepStmt = conn.prepareStatement(
                    "insert into bikes(id, vehicleStatus, vehicleType, name, dailyPrice, color, productionYear) values (?, ?, ?, ?, ?, ?, ? );");
            prepStmt.setString(1, id);
            prepStmt.setString(2, VehicleStatus.avaiable.name());
            prepStmt.setString(3, VehicleType.bike.name());
            prepStmt.setString(4, name);
            prepStmt.setDouble(5, dailyPrice);
            prepStmt.setString(6, color);
            prepStmt.setInt(7, productionYear);
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to add bike");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Can't properly add bike, contact with administrator");
        }
    }


    public static void updateBike(String id, String name, double dailyPrice, String color, int productionYear) throws ErrorMessageException{
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "update bikes set" +
                            " name = " + "\"" + name + "\"" +
                            " ,dailyPrice = " +"\""  + dailyPrice + "\""  +
                            " ,color = " + "\"" + color +"\""  +
                            ", productionYear = " + "\"" + productionYear + "\"" +
                            " where id  = " + "\"" + id + "\" ");
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to edit bike ");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    public static Bike getbikeByID (String bikeID) throws ErrorMessageException {
        ResultSet BikeResult;
        int productionYear = -1;
        double dailyPrice = -1;
        String name = null, vehicleStatus =null, vehicleType=null, color = null, id = null;
        try {
            BikeResult = stat.executeQuery("SELECT * FROM bikes where id =" + '\"' + bikeID + '\"');
            while(BikeResult.next()) {
                id = BikeResult.getString("id");
                vehicleStatus = BikeResult.getString("vehicleStatus");
                vehicleType = BikeResult.getString("vehicleType");
                name = BikeResult.getString("name");
                dailyPrice = BikeResult.getDouble("dailyPrice");
                color = BikeResult.getString("color");
                productionYear = BikeResult.getInt("productionYear");
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get bike by bikeID");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        if(id == null)
            return new Bike(id, VehicleStatus.archived, VehicleType.bike, name,dailyPrice, color, productionYear);

        return new Bike(id, VehicleStatus.valueOf(vehicleStatus), VehicleType.valueOf(vehicleType), name,dailyPrice, String.valueOf(color), productionYear);
    }


    public static List<Car> getFilteredCars(String name, String color, String typeOfComparePriceMin,String typeOfComparePriceMax, double minPrice, double maxPrice,
                                            String typeOfCompareProductionYeatMin, String typeOfCompareProductionYeatMax, int minProductionYear, int maxProductionYear,
                                            String typeOfCompareMileageMin, String typeOfCompareMileageMax, long minMileage, long maxMileage, String typeOfCompareEngineCapacityMin,
                                            String typeOfCompareEngineCapacityMax, double minCapacity, double maxCapacity, FuelType fuelType,
                                            String typeOfCompareFuelUsageMin, String typeOfCompareFuelUsageMax, double minFuelUsage, double maxFuelUsage, int quantityOfPerson,
                                            VehicleStatus vehicleStatus, String vehicleID) throws ErrorMessageException {
        String query = "Select * from cars ";
        if (!(name == null))
            query += "where name LIKE \"%" + name + "%\" ";

    // price min
        if(!(typeOfComparePriceMin == null) && !(minPrice == -1) && !query.contains("where"))
            query += " where dailyPrice " + typeOfComparePriceMin + " \"" + minPrice + "\" ";
        if(!(typeOfComparePriceMin == null) && !(minPrice == -1) && query.contains("where"))
            query += " AND dailyPrice " + typeOfComparePriceMin + " \"" + minPrice + "\" ";

    // price max
        if(!(typeOfComparePriceMax == null) && !(maxPrice == -1) && !query.contains("where"))
            query += " where dailyPrice " + typeOfComparePriceMax + " \"" + maxPrice + "\" ";
        if(!(typeOfComparePriceMax == null) && !(maxPrice == -1) && query.contains("where"))
            query += " AND dailyPrice " + typeOfComparePriceMax + " \"" + maxPrice + "\" ";

    // mileage min
        if(!(typeOfCompareMileageMin == null) && !(minMileage == -1) && !query.contains("where"))
            query += " where mileage " + typeOfCompareMileageMin + " \"" + minMileage + "\" ";
        if(!(typeOfCompareMileageMin == null) && !(minMileage == -1) && query.contains("where"))
            query += " AND mileage " + typeOfCompareMileageMin + " \"" + minMileage + "\" ";

    // mileage max
        if(!(typeOfCompareMileageMax == null) && !(maxMileage == -1) && !query.contains("where"))
            query += " where mileage " + typeOfCompareMileageMax + " \"" + maxMileage + "\" ";
        if(!(typeOfCompareMileageMax == null) && !(maxMileage == -1) && query.contains("where"))
            query += " AND mileage " + typeOfCompareMileageMax + " \"" + maxMileage + "\" ";

    // production year  min
        if(!(typeOfCompareProductionYeatMin == null) && !(minProductionYear == -1) && !query.contains("where"))
            query += " where productionYear " + typeOfCompareProductionYeatMin + " \"" + minProductionYear + "\" ";
        if(!(typeOfCompareProductionYeatMin == null) && !(minProductionYear == -1) && query.contains("where"))
            query += " AND productionYear " + typeOfCompareProductionYeatMin + " \"" + minProductionYear + "\" ";

    // production year max
        if(!(typeOfCompareProductionYeatMax == null) && !(maxProductionYear == -1) && !query.contains("where"))
            query += " where productionYear " + typeOfCompareProductionYeatMax + " \"" + maxProductionYear + "\" ";
        if(!(typeOfCompareProductionYeatMax == null) && !(maxProductionYear == -1) && query.contains("where"))
            query += " AND productionYear " + typeOfCompareProductionYeatMax + " \"" + maxProductionYear + "\" ";

    // engine capacity min
        if(!(typeOfCompareEngineCapacityMin == null) && !(minCapacity == -1) && !query.contains("where"))
            query += " where engineCapacity " + typeOfCompareEngineCapacityMin + " \"" + minCapacity + "\" ";
        if(!(typeOfCompareEngineCapacityMin == null) && !(minCapacity == -1) && query.contains("where"))
            query += " AND engineCapacity " + typeOfCompareEngineCapacityMin + " \"" + minCapacity + "\" ";

    // engine capacity max
        if(!(typeOfCompareEngineCapacityMax == null) && !(maxCapacity == -1) && !query.contains("where"))
            query += " where engineCapacity " + typeOfCompareEngineCapacityMax + " \"" + maxCapacity + "\" ";
        if(!(typeOfCompareEngineCapacityMax == null) && !(maxCapacity == -1) && query.contains("where"))
            query += " AND engineCapacity " + typeOfCompareEngineCapacityMax + " \"" + maxCapacity + "\" ";

    // fueltype
        if(!(fuelType == null) && !query.contains("where"))
            query += " where FuelType = \"" + fuelType.toString() + "\" ";
        if(!(typeOfCompareEngineCapacityMax == null) && !(maxCapacity == -1) && query.contains("where"))
            query += " AND FuelType= \"" +  fuelType.toString()  + "\" ";

    // fuelUsage min
        if(!(typeOfCompareFuelUsageMin == null) && !(minFuelUsage == -1) && !query.contains("where"))
            query += " where engineCapacity " + typeOfCompareFuelUsageMin + " \"" + minFuelUsage + "\" ";
        if(!(typeOfCompareFuelUsageMin == null) && !(minFuelUsage == -1) && query.contains("where"))
            query += " AND engineCapacity " + typeOfCompareFuelUsageMin + " \"" + minFuelUsage + "\" ";

    // fuelUsage max
        if(!(typeOfCompareFuelUsageMax == null) && !(maxFuelUsage == -1) && !query.contains("where"))
            query += " where fuelUsage " + typeOfCompareFuelUsageMax + " \"" + maxFuelUsage + "\" ";
        if(!(typeOfCompareFuelUsageMax == null) && !(maxFuelUsage == -1) && query.contains("where"))
            query += " AND fuelUsage " + typeOfCompareFuelUsageMax + " \"" + maxFuelUsage + "\" ";

        if (!(quantityOfPerson == -1) && !query.contains("where"))
            query += " where numberOffPersons=\"" + quantityOfPerson + "\" ";
        if (!(quantityOfPerson == -1) && query.contains("where"))
            query += " AND numberOffPersons=\"" + quantityOfPerson + "\" ";

        if (!(vehicleStatus == null) && !query.contains("where"))
            query += " where vehicleStatus=\"" + vehicleStatus + "\" ";
        if (!(vehicleStatus == null) && query.contains("where"))
            query += " AND vehicleStatus=\"" + vehicleStatus + "\" ";

        if (!(vehicleID == null) && !query.contains("where"))
            query += " where id LIKE\"%" + vehicleID + "%\" ";
        if (!(vehicleID == null) && query.contains("where"))
            query += " AND id LIKE \"%" + vehicleID + "%\" ";

        List<Car> cars = new ArrayList<>();
        try {
            ResultSet result = stat.executeQuery(query);
            int productionYear, numberOffPersons, mileage; double dailyPrice, engineCapacity,fuelUsage;
            String id, name_, vehicleType, color_, fuelType_, vehicleStatusString;
            while(result.next()) {
                id = result.getString("id");
                vehicleType = result.getString("vehicleType");
                vehicleStatusString = result.getString("vehicleStatus");
                name_ = result.getString("name");
                dailyPrice = result.getDouble("dailyPrice");
                color_ = result.getString("color");
                productionYear = result.getInt("productionYear");
                mileage = result.getInt("mileage");
                engineCapacity = result.getDouble("engineCapacity");
                fuelType_ = result.getString("FuelType");
                fuelUsage = result.getDouble("fuelUsage");
                numberOffPersons = result.getInt("numberOffPersons");

                cars.add(new Car(id, VehicleStatus.valueOf(vehicleStatusString), VehicleType.valueOf(vehicleType), name_, dailyPrice,
                        String.valueOf(color_), productionYear, mileage, engineCapacity, fuelUsage, FuelType.valueOf(fuelType_),
                        numberOffPersons));
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get lists all cars");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        return cars;
    }

    public static List<Motorcycle> getFilteredMotorcycles(String name, String color, String typeOfComparePriceMin,String typeOfComparePriceMax, double minPrice, double maxPrice,
                                            String typeOfCompareProductionYeatMin, String typeOfCompareProductionYeatMax, int minProductionYear, int maxProductionYear,
                                            String typeOfCompareMileageMin, String typeOfCompareMileageMax, long minMileage, long maxMileage, String typeOfCompareEngineCapacityMin,
                                            String typeOfCompareEngineCapacityMax, double minCapacity, double maxCapacity,
                                            String typeOfCompareFuelUsageMin, String typeOfCompareFuelUsageMax, double minFuelUsage, double maxFuelUsage,
                                            VehicleStatus vehicleStatus, String vehicleID) throws ErrorMessageException {
        String query = "Select * from motorcycles ";
        if (!(name == null))
            query += "where name LIKE \"%" + name + "%\" ";
        if (!(color == null) && !query.contains("where"))
            query += "where color LIKE \"%" + color + "%\" ";
        if (!(color == null) && query.contains("where"))
            query += " AND color LIKE \"%" + color + "%\" ";

        // price min
        if(!(typeOfComparePriceMin == null) && !(minPrice == -1) && !query.contains("where"))
            query += " where dailyPrice " + typeOfComparePriceMin + " \"" + minPrice + "\" ";
        if(!(typeOfComparePriceMin == null) && !(minPrice == -1) && query.contains("where"))
            query += " AND dailyPrice " + typeOfComparePriceMin + " \"" + minPrice + "\" ";

        // price max
        if(!(typeOfComparePriceMax == null) && !(maxPrice == -1) && !query.contains("where"))
            query += " where dailyPrice " + typeOfComparePriceMax + " \"" + maxPrice + "\" ";
        if(!(typeOfComparePriceMax == null) && !(maxPrice == -1) && query.contains("where"))
            query += " AND dailyPrice " + typeOfComparePriceMax + " \"" + maxPrice + "\" ";

        // mileage min
        if(!(typeOfCompareMileageMin == null) && !(minMileage == -1) && !query.contains("where"))
            query += " where mileage " + typeOfCompareMileageMin + " \"" + minMileage + "\" ";
        if(!(typeOfCompareMileageMin == null) && !(minMileage == -1) && query.contains("where"))
            query += " AND mileage " + typeOfCompareMileageMin + " \"" + minMileage + "\" ";

        // mileage max
        if(!(typeOfCompareMileageMax == null) && !(maxMileage == -1) && !query.contains("where"))
            query += " where mileage " + typeOfCompareMileageMax + " \"" + maxMileage + "\" ";
        if(!(typeOfCompareMileageMax == null) && !(maxMileage == -1) && query.contains("where"))
            query += " AND mileage " + typeOfCompareMileageMax + " \"" + maxMileage + "\" ";

        // production year  min
        if(!(typeOfCompareProductionYeatMin == null) && !(minProductionYear == -1) && !query.contains("where"))
            query += " where productionYear " + typeOfCompareProductionYeatMin + " \"" + minProductionYear + "\" ";
        if(!(typeOfCompareProductionYeatMin == null) && !(minProductionYear == -1) && query.contains("where"))
            query += " AND productionYear " + typeOfCompareProductionYeatMin + " \"" + minProductionYear + "\" ";

        // production year max
        if(!(typeOfCompareProductionYeatMax == null) && !(maxProductionYear == -1) && !query.contains("where"))
            query += " where productionYear " + typeOfCompareProductionYeatMax + " \"" + maxProductionYear + "\" ";
        if(!(typeOfCompareProductionYeatMax == null) && !(maxProductionYear == -1) && query.contains("where"))
            query += " AND productionYear " + typeOfCompareProductionYeatMax + " \"" + maxProductionYear + "\" ";

        // engine capacity min
        if(!(typeOfCompareEngineCapacityMin == null) && !(minCapacity == -1) && !query.contains("where"))
            query += " where engineCapacity " + typeOfCompareEngineCapacityMin + " \"" + minCapacity + "\" ";
        if(!(typeOfCompareEngineCapacityMin == null) && !(minCapacity == -1) && query.contains("where"))
            query += " AND engineCapacity " + typeOfCompareEngineCapacityMin + " \"" + minCapacity + "\" ";

        // engine capacity max
        if(!(typeOfCompareEngineCapacityMax == null) && !(maxCapacity == -1) && !query.contains("where"))
            query += " where engineCapacity " + typeOfCompareEngineCapacityMax + " \"" + maxCapacity + "\" ";
        if(!(typeOfCompareEngineCapacityMax == null) && !(maxCapacity == -1) && query.contains("where"))
            query += " AND engineCapacity " + typeOfCompareEngineCapacityMax + " \"" + maxCapacity + "\" ";

    // fuelUsage min
        if(!(typeOfCompareFuelUsageMin == null) && !(minFuelUsage == -1) && !query.contains("where"))
            query += " where engineCapacity " + typeOfCompareFuelUsageMin + " \"" + minFuelUsage + "\" ";
        if(!(typeOfCompareFuelUsageMin == null) && !(minFuelUsage == -1) && query.contains("where"))
            query += " AND engineCapacity " + typeOfCompareFuelUsageMin + " \"" + minFuelUsage + "\" ";

    // fuelUsage max
        if(!(typeOfCompareFuelUsageMax == null) && !(maxFuelUsage == -1) && !query.contains("where"))
            query += " where fuelUsage " + typeOfCompareFuelUsageMax + " \"" + maxFuelUsage + "\" ";
        if(!(typeOfCompareFuelUsageMax == null) && !(maxFuelUsage == -1) && query.contains("where"))
            query += " AND fuelUsage " + typeOfCompareFuelUsageMax + " \"" + maxFuelUsage + "\" ";

        if (!(vehicleStatus == null) && !query.contains("where"))
            query += " where vehicleStatus=\"" + vehicleStatus + "\" ";
        if (!(vehicleStatus == null) && query.contains("where"))
            query += " AND vehicleStatus=\"" + vehicleStatus + "\" ";

        if (!(vehicleID == null) && !query.contains("where"))
            query += " where id LIKE\"%" + vehicleID + "%\" ";
        if (!(vehicleID == null) && query.contains("where"))
            query += " AND id LIKE \"%" + vehicleID + "%\" ";
        List<Motorcycle> motorcycles = new ArrayList<>();
        try {
            ResultSet result = stat.executeQuery(query);
            double dailyPrice, fuelUsage, engineCapacity; int productionYear, mileage;
            String id, name_, vehicleType, color_, vehicleStatus_;
            while(result.next()) {
                id = result.getString("id");
                vehicleType = result.getString("vehicleType");
                name_ = result.getString("name");
                vehicleStatus_ = result.getString("vehicleStatus");
                dailyPrice = result.getDouble("dailyPrice");
                color_ = result.getString("color");
                productionYear = result.getInt("productionYear");
                mileage = result.getInt("mileage");
                engineCapacity = result.getDouble("engineCapacity");
                fuelUsage = result.getDouble("fuelUsage");

                motorcycles.add(new Motorcycle(id, VehicleStatus.valueOf(vehicleStatus_), VehicleType.valueOf(vehicleType), name_, dailyPrice,
                        String.valueOf(color_), productionYear, mileage, engineCapacity, fuelUsage));
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get lists all motorcycles");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        return motorcycles;
    }


    public static List<Bike> getFilteredBikes(String name, String color, String typeOfComparePriceMin,String typeOfComparePriceMax, double minPrice, double maxPrice,
                                              String typeOfCompareProductionYeatMin, String typeOfCompareProductionYeatMax, int minProductionYear, int maxProductionYear,
                                              VehicleStatus vehicleStatus, String vehicleID  ) throws ErrorMessageException {
        String query = "Select * from bikes ";
        if (!(name == null))
            query += "where name LIKE \"%" + name + "%\" ";
        if (!(color == null) && !query.contains("where"))
            query += "where color LIKE \"%" + color + "%\" ";
        if (!(color == null) && query.contains("where"))
            query += " AND color LIKE \"%" + color + "%\" ";

    // price min
        if(!(typeOfComparePriceMin == null) && !(minPrice == -1) && !query.contains("where"))
            query += " where dailyPrice " + typeOfComparePriceMin + " \"" + minPrice + "\" ";
        if(!(typeOfComparePriceMin == null) && !(minPrice == -1) && query.contains("where"))
            query += " AND dailyPrice " + typeOfComparePriceMin + " \"" + minPrice + "\" ";

    // price max
        if(!(typeOfComparePriceMax == null) && !(maxPrice == -1) && !query.contains("where"))
            query += " where dailyPrice " + typeOfComparePriceMax + " \"" + maxPrice + "\" ";
        if(!(typeOfComparePriceMax == null) && !(maxPrice == -1) && query.contains("where"))
            query += " AND dailyPrice " + typeOfComparePriceMax + " \"" + maxPrice + "\" ";

    // production year  min
        if(!(typeOfCompareProductionYeatMin == null) && !(minProductionYear == -1) && !query.contains("where"))
            query += " where productionYear " + typeOfCompareProductionYeatMin + " \"" + minProductionYear + "\" ";
        if(!(typeOfCompareProductionYeatMin == null) && !(minProductionYear == -1) && query.contains("where"))
            query += " AND productionYear " + typeOfCompareProductionYeatMin + " \"" + minProductionYear + "\" ";

    // production year max
        if(!(typeOfCompareProductionYeatMax == null) && !(maxProductionYear == -1) && !query.contains("where"))
            query += " where productionYear " + typeOfCompareProductionYeatMax + " \"" + maxProductionYear + "\" ";
        if(!(typeOfCompareProductionYeatMax == null) && !(maxProductionYear == -1) && query.contains("where"))
            query += " AND productionYear " + typeOfCompareProductionYeatMax + " \"" + maxProductionYear + "\" ";

        if (!(vehicleStatus == null) && !query.contains("where"))
            query += " where vehicleStatus=\"" + vehicleStatus + "\" ";
        if (!(vehicleStatus == null) && query.contains("where"))
            query += " AND vehicleStatus=\"" + vehicleStatus + "\" ";

        if (!(vehicleID == null) && !query.contains("where"))
            query += " where id LIKE\"%" + vehicleID + "%\" ";
        if (!(vehicleID == null) && query.contains("where"))
            query += " AND id LIKE \"%" + vehicleID + "%\" ";
        List<Bike> bikes = new ArrayList<>();
        try {
            ResultSet result = stat.executeQuery(query);
            int productionYear; double dailyPrice;
            String vehicleType, name_, color_, id, vehicleStatus_;
            while(result.next()) {
                id = result.getString("id");
                vehicleStatus_ = result.getString("vehicleStatus");
                vehicleType = result.getString("vehicleType");
                name_ = result.getString("name");
                dailyPrice = result.getDouble("dailyPrice");
                color_ = result.getString("color");
                productionYear = result.getInt("productionYear");

                bikes.add(new Bike(id, VehicleStatus.valueOf(vehicleStatus_), VehicleType.valueOf(vehicleType), name_, dailyPrice, String.valueOf(color_), productionYear));
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get lists all Bikes");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        return bikes;
    }


    public static List<Bike> getAllAvaiableBikes() throws ErrorMessageException{
        return getAllBikes(VehicleStatus.avaiable);
    }

    public static List<Bike> getAllBorrowedBikes() throws ErrorMessageException{
        return getAllBikes(VehicleStatus.unavaiable);
    }

    public static List<Bike> getAllAArchivalBikes() throws ErrorMessageException{
        return getAllBikes(VehicleStatus.archived);
    }

    public static void addMotorcycle(String id, String name, double dailyPrice, String color, int productionYear, int mileage, double engineCapacity, double fuelUsage)
            throws ErrorMessageException {
        try {
            PreparedStatement prepStmt;
            prepStmt = conn.prepareStatement(
                    "insert into motorcycles(id, vehicleStatus, vehicleType, name, dailyPrice, color, productionYear, mileage," +
                            "engineCapacity, fuelUsage) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ? );");
            prepStmt.setString(1, id);
            prepStmt.setString(2, VehicleStatus.avaiable.name());
            prepStmt.setString(3, VehicleType.motorcycle.name());
            prepStmt.setString(4, name);
            prepStmt.setDouble(5, dailyPrice);
            prepStmt.setString(6, color);
            prepStmt.setInt(7, productionYear);
            prepStmt.setInt(8, mileage);
            prepStmt.setDouble(9, engineCapacity);
            prepStmt.setDouble(10, fuelUsage);
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to add motorcycle");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    public static void updateMotorcycle(String id, String name, double dailyPrice, String color, int productionYear,
                                        int mileage, double engineCapacity, double fuelUsage) throws ErrorMessageException{
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "update motorcycles set" +
                            " name = " + "\"" + name + "\"" +
                            " ,dailyPrice = " +"\""  + dailyPrice + "\""  +
                            " ,color = " + "\"" + color +"\""  +
                            ", productionYear = " + "\"" + productionYear + "\"" +
                            " ,mileage = " +"\""  + mileage +"\""  +
                            " ,engineCapacity = " +"\""  + engineCapacity +"\""  +
                            " ,fuelUsage = " +"\""  + fuelUsage +"\""  +
                            " where id  = " + "\"" + id + "\" ");

            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to edit car ");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    public static Motorcycle getMotorcycleByID (String motorcycleID) throws ErrorMessageException {
        ResultSet MotorcycleResult;
        int productionYear = -1, mileage = -1;
        double dailyPrice = -1, engineCapacity = -1, fuelUsage = -1;
        String name = null, vehicleStatus = null, vehicleType = null, color = null, id = null;
        try {
            MotorcycleResult = stat.executeQuery("SELECT * FROM motorcycles where id=" + "\"" + motorcycleID + "\"" );
            while(MotorcycleResult.next()) {
                id = MotorcycleResult.getString("id");
                vehicleStatus = MotorcycleResult.getString("vehicleStatus");
                vehicleType = MotorcycleResult.getString("vehicleType");
                name = MotorcycleResult.getString("name");
                dailyPrice = MotorcycleResult.getDouble("dailyPrice");
                color = MotorcycleResult.getString("color");
                productionYear = MotorcycleResult.getInt("productionYear");
                mileage = MotorcycleResult.getInt("mileage");
                engineCapacity = MotorcycleResult.getDouble("engineCapacity");
                fuelUsage = MotorcycleResult.getDouble("fuelUsage");
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get motorcycle by motorcycleID");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        if(id == null)
            return new Motorcycle( id, VehicleStatus.archived, VehicleType.motorcycle, name, dailyPrice,
                    color, productionYear, mileage, engineCapacity, fuelUsage);

        return new Motorcycle( id, VehicleStatus.valueOf(vehicleStatus), VehicleType.valueOf(vehicleType), name, dailyPrice,
                String.valueOf(color), productionYear, mileage, engineCapacity, fuelUsage);
    }


    public static List<Motorcycle> getAllAvaiableMotorcycles() throws ErrorMessageException{
        return getAllMotorcycles(VehicleStatus.avaiable);
    }

    public static List<Motorcycle> getAllABorrowedMotorcycles() throws ErrorMessageException{
        return getAllMotorcycles(VehicleStatus.unavaiable);
    }

    public static List<Motorcycle> getAllAArchivalMotorcycles() throws ErrorMessageException{
        return getAllMotorcycles(VehicleStatus.archived);
    }

    public static void setVehicleUnavaiable(String vehicleID, VehicleType typeOfVehicle) throws ErrorMessageException {
        try {
            PreparedStatement prepStmt = null;
            switch(typeOfVehicle) {
                case car:
                    prepStmt = conn.prepareStatement(
                            "update cars set vehicleStatus= \"" + VehicleStatus.unavaiable + "\"where id = " + "\"" + vehicleID + "\"");
                    break;
                case bike:
                    prepStmt = conn.prepareStatement(
                            "update bikes set vehicleStatus= \"" + VehicleStatus.unavaiable + "\"where id = " + "\"" + vehicleID + "\"");
                    break;
                case motorcycle:
                    prepStmt = conn.prepareStatement(
                            "update motorcycles set vehicleStatus= \"" + VehicleStatus.unavaiable + "\"where id = " + "\"" + vehicleID + "\"");
                    break;
            }
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to set vehicle inaccessible");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    public static void setVehicleAvaiable(String vehicleID, VehicleType typeOfVehicle) throws ErrorMessageException {
        try {
            PreparedStatement prepStmt = null;
            switch(typeOfVehicle) {
                case car:
                    prepStmt = conn.prepareStatement(
                            "update cars set vehicleStatus= \"" + VehicleStatus.avaiable + "\"where id = " + "\"" + vehicleID + "\"");
                    break;
                case bike:
                    prepStmt = conn.prepareStatement(
                            "update bikes set vehicleStatus= \"" + VehicleStatus.avaiable + "\"where id = " + "\"" + vehicleID + "\"");
                    break;
                case motorcycle:
                    prepStmt = conn.prepareStatement(
                            "update motorcycles set vehicleStatus=\"" + VehicleStatus.avaiable + "\"where id = " + "\"" + vehicleID + "\"");
                    break;
            }
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to set vehicle accessible");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    public static void updateVehicleMileage(String vehicleID, VehicleType typeOfVehicle, long mileage) throws ErrorMessageException {
        try {
            PreparedStatement prepStmt = null;
            switch(typeOfVehicle) {
                case car:
                    prepStmt = conn.prepareStatement(
                            "update cars set mileage= \"" + mileage + "\"where id = " + "\"" + vehicleID + "\"");
                    prepStmt.execute();
                    break;
                case bike:
                    break;
                case motorcycle:
                    prepStmt = conn.prepareStatement(
                            "update motorcycles set mileage=\"" + mileage + "\"where id = " + "\"" + vehicleID + "\"");
                    prepStmt.execute();
                    break;
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to set vehicle accessible");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    public static double getPricePerDayOfVehicle(String vehicleID, VehicleType typeOfVehicle) throws ErrorMessageException {
        double pricePerDay = 0;
        if(typeOfVehicle == VehicleType.car)
            pricePerDay = getCarPricePerDay(vehicleID);

        else if(typeOfVehicle == VehicleType.bike)
            pricePerDay = getBikePricePerDay(vehicleID);

        if(typeOfVehicle == VehicleType.motorcycle)
            pricePerDay = getMotorcyclePricePerDay(vehicleID);
        return pricePerDay;
    }

    public static void markVehicleAsArchival(String vehicleID, VehicleType typeOfVehicle) throws ErrorMessageException{
        try {
            PreparedStatement prepStmt = null;
            switch(typeOfVehicle) {
                case car:
                    prepStmt = conn.prepareStatement(
                            "update cars set vehicleStatus =\""  + VehicleStatus.archived + "\" where id = " + "\"" + vehicleID  + "\"");
                    break;
                case bike:
                    prepStmt = conn.prepareStatement(
                            "update bikes set vehicleStatus =\""  + VehicleStatus.archived + "\" where id = " + "\"" + vehicleID  + "\"");
                    break;
                case motorcycle:
                    prepStmt = conn.prepareStatement(
                            "update motorcycles set vehicleStatus = \""  + VehicleStatus.archived + "\" where id = " +  "\"" + vehicleID + "\"");
                    break;
            }
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to set vehicle accessible");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    public static void addEmployee(String firstName, String lastName, String address, int phoneNumber, String email, String login, String password, String salt, boolean managerPermission) throws ErrorMessageException{
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "insert into employees( firstName, lastName, address, phoneNumber, email, status, login, password, salt, managerPermission) values (?,?,?,?,?,?,?,?,?,?);");
            prepStmt.setString(1, firstName);
            prepStmt.setString(2, lastName);
            prepStmt.setString(3, address);
            prepStmt.setInt(4, phoneNumber);
            prepStmt.setString(5, email);
            prepStmt.setInt(6, 0);
            prepStmt.setString(7, login);
            prepStmt.setString(8, password);
            prepStmt.setString(9, salt);
            prepStmt.setBoolean(10, managerPermission);
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to add employee to database");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    public static void editEmployee(int UUID, String firstName, String lastName, String address, int phoneNumber, String email, String login) throws ErrorMessageException{
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "update employees set" +
                            " firstName = " + "\"" + firstName + "\"" +
                            " ,lastName = " +"\""  + lastName + "\""  +
                            " ,address = " + "\"" + address +"\""  +
                            ", phoneNumber = " + "\"" + phoneNumber + "\"" +
                            " ,email = " +"\""  + email +"\""  +
                            " ,login = " +"\""  + login +"\""  +
                            " where UUID= " + "\"" + UUID + "\" ");

            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to edit employee ");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }
    public static void editEmployee(int UUID, String firstName, String lastName, String address, int phoneNumber, String email, String login, String password, String salt) throws ErrorMessageException{
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "update employees set" +
                            " firstName = " + "\"" + firstName + "\"" +
                            " ,lastName = " +"\""  + lastName + "\""  +
                            " ,address = " + "\"" + address +"\""  +
                            ", phoneNumber = " + "\"" + phoneNumber + "\"" +
                            " ,email = " +"\""  + email +"\""  +
                            " ,login = " +"\""  + login +"\""  +
                            " ,password = " +"\""  + password +"\""  +
                            " ,salt = " +"\""  + salt +"\""  +
                            " where UUID= " + "\"" + UUID + "\" ");

            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to edit employee ");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    public static void setEmployeeActive(int UUID ) throws ErrorMessageException{
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "update employees set" +
                            " active = " + "\"" + 1 + "\"" +
                            " where UUID= " + "\"" + UUID + "\" ");
            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to edit employee ");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    public static void setEmployeeInactive(int UUID ) throws ErrorMessageException{
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "update employees set" +
                                " active = " + "\"" + "0"+ "\"" +
                            " where UUID= " + "\"" + UUID + "\" ");

            prepStmt.execute();
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to edit employee ");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    public static List<Employee> getAllEmployees() throws ErrorMessageException{
        List<Employee> employees = new ArrayList<>();
        String firstName, login,  lastName, address, email;
        int phoneNumber, UUID ;
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM employees ");
            while (result.next()) {
                UUID =  result.getInt("UUID");
                login = result.getString("login");
                firstName = result.getString("firstName");
                lastName = result.getString("lastName");
                address = result.getString("address");
                phoneNumber = result.getInt("phoneNumber");
                email = result.getString("email");
                employees.add(new Employee(UUID, firstName, lastName, address, phoneNumber, email, login));
            }
        }catch(Exception e){
            Logs.logger.warning("Error when try to get all employees");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        return employees;
    }

    public static List<Employee> getFilteredEmployees(String firstName, String lastName) throws ErrorMessageException {
        List<Employee> employees = new ArrayList<>();
        String firstName_, lastName_, address, email, login;
        int phoneNumber, UUID ;

        String query = "SELECT * FROM employees  ";
        if (!(firstName == null))
            query += " where  firstName LIKE \"%" + firstName + "%\" ";
        if (!(lastName == null) && query.contains("LIKE"))
            query += " AND lastName LIKE \"%" + lastName+ "%\" ";
        if (!(lastName == null) && !query.contains("LIKE"))
            query += " where lastName LIKE \"%" + lastName + "%\" ";
        try {
            ResultSet result = stat.executeQuery(query);
            while (result.next()) {
                UUID =  result.getInt("UUID");
                login = result.getString("login");
                firstName_ = result.getString("firstName");
                lastName_ = result.getString("lastName");
                address = result.getString("address");
                phoneNumber = result.getInt("phoneNumber");
                email = result.getString("email");
                employees.add(new Employee(UUID, firstName_, lastName_, address, phoneNumber, email, login));
            }
        }catch(Exception e){
            Logs.logger.warning("Error when try to get list filetred employees");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        return employees;
    }

    public static Employee getEmployeeByID(int UUID) throws ErrorMessageException{
        String firstName, lastName, address, email, login;
        Employee employee = null;
        int phoneNumber;
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM employees where UUID = " + "\"" + UUID + "\"");
            while (result.next()) {
                login = result.getString("login");
                firstName = result.getString("firstName");
                lastName = result.getString("lastName");
                address = result.getString("address");
                phoneNumber = result.getInt("phoneNumber");
                email = result.getString("email");
                employee = new Employee(UUID, firstName, lastName, address, phoneNumber, email, login);
            }
        }catch(Exception e){
            Logs.logger.warning("Error when try to get employee by id ");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        return employee;
    }

    public static Employee getLoggedEmployee() throws ErrorMessageException{
        String firstName, lastName, address, email, login;
        Employee employee = null;
        int phoneNumber, UUID;
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM employees where active = " + "\"" + 1 + "\"");
            while (result.next()) {
                UUID = result.getInt("UUID");
                login = result.getString("login");
                firstName = result.getString("firstName");
                lastName = result.getString("lastName");
                address = result.getString("address");
                phoneNumber = result.getInt("phoneNumber");
                email = result.getString("email");
                employee = new Employee(UUID, firstName, lastName, address, phoneNumber, email, login);
            }
        }catch(Exception e){
            Logs.logger.warning("Error when try to get employee by id ");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        return employee;
    }
    public static boolean hasEmployeeManagerPermission(int UUID) throws ErrorMessageException{
      boolean permission = false;
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM employees where UUID = " + "\"" + UUID + "\"");
            while (result.next()) {
                permission = result.getBoolean("managerPermission");
            }
        }catch(Exception e){
            Logs.logger.warning("Error when try to get employee permission");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        return permission;
    }

    public static Employee getEmployeeByLogin(String login) throws ErrorMessageException{
        String firstName = null, lastName  = null, address = null, email = null, login_ = null;
        Employee employee = null;
        int phoneNumber = -1, UUID = -1;
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM employees where login = " + "\"" + login + "\"");
            while (result.next()) {
                UUID = result.getInt("UUID");
                login_ = result.getString("login");
                firstName = result.getString("firstName");
                lastName = result.getString("lastName");
                address = result.getString("address");
                phoneNumber = result.getInt("phoneNumber");
                email = result.getString("email");
            }
            employee = new Employee(UUID, firstName, lastName, address, phoneNumber, email, login_);

        }catch(Exception e){
            Logs.logger.warning("Error when try to get employee by id ");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        return employee;
    }

    public static String getPassword(String login) throws ErrorMessageException {
        String password = null;
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM employees where login =\"" + login + "\"");
            while(result.next()) {
                password = result.getString("password");
            }

        }catch(Exception e){
            Logs.logger.warning("Error when try to get password by login ");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        return password;
    }

    public static String getSalt(String login) throws ErrorMessageException {
        String salt = null;
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM employees where login = " + "\"" + login + "\"");
            while(result.next()) {
                salt = result.getString("salt");
            }
        }catch(Exception e){
            Logs.logger.warning("Error when try to get salt by login ");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        return salt;
    }


    /***************************  Private *********************************/

    private static List<Customer> getAllCustomers(String where) throws ErrorMessageException {
        List<Customer> customers = new ArrayList<>();
        try {
            ResultSet result;
            int status;
            if( where.equals("actual"))
                status = 0;
            else
                status = 1;
            result = stat.executeQuery("SELECT * FROM customers where status = " + "\"" + status + "\"");
            int phoneNumber;
            String firstName, lastName, address, pesel, email, companyName, nipNumber, companyAddress;
            double sumPaidForAllRents;
            while(result.next()) {
                pesel = result.getString("peselNumber");
                firstName = result.getString("firstName");
                lastName = result.getString("lastName");
                address = result.getString("address");
                phoneNumber = result.getInt("phoneNumber");
                email = result.getString("email");
                companyName = result.getString("companyName");
                nipNumber = result.getString("nipNumber");
                companyAddress = result.getString("companyAddress");
                sumPaidForAllRents = result.getDouble("sum_paid_for_all_rents");
                customers.add(new Customer(firstName, lastName, pesel, address, phoneNumber, email, companyName,
                        nipNumber, companyAddress, sumPaidForAllRents));
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get lists all customer");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        return customers;
    }

    private static List<Rent> getAllRent(RentStatus rentStatus) throws ErrorMessageException {
        List<Rent> rent = new ArrayList<>();
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM rents where status = \"" + rentStatus.name() + "\"");
            int id, employeeID; double priceForRent;
            String typeOfVehicle, customerID, vehicleID, status;
            Employee employee; Customer customer; Vehicle vehicle= null; Date dateOfRental, dateOfReturn;
            int x = 0;
            int resultSize = countSizeResultSet(result);
            result = stat.executeQuery("SELECT * FROM rents where status = \"" + rentStatus.name() + "\"");
            result.next();
            while(x< resultSize) {
                for (int i=0; i<x; i++ )
                    result.next();
                id = result.getInt("id");
                customerID = result.getString("customerID");
                vehicleID = result.getString("vehicleID");
                typeOfVehicle = result.getString("vehicleType");
                priceForRent = result.getDouble("totalPrice");
                dateOfRental = result.getDate("startDate");
                dateOfReturn = result.getDate("endDate");
                employeeID = result.getInt("employeeID");
                status = result.getString("status");
                employee = getEmployeeByID(employeeID);
                customer  = getCustomerByPesel(customerID);

                switch (VehicleType.valueOf(typeOfVehicle)) {
                    case car:
                        vehicle = getCarByID(vehicleID);
                        break;
                    case bike:
                        vehicle = getbikeByID(vehicleID);
                        break;
                    case motorcycle:
                        vehicle = getMotorcycleByID(vehicleID);
                        break;
                }
                rent.add(new Rent(vehicle, customer, employee, id, priceForRent, dateOfRental, dateOfReturn, RentStatus.valueOf(status)));
                x++;
                result = stat.executeQuery("SELECT * FROM rents where status = \"" + rentStatus.name() + "\"");
                result.next();
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get all rents");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        return rent;
    }

    private static List<Rent> getAllCustomerRents(RentStatus rentStatus, String pesel) throws  ErrorMessageException {
        List<Rent> rent = new ArrayList<>();
        String sql;
        sql = "SELECT * FROM rents where customerID = " + "\"" + pesel  + "\" and status = "  + "\"" + rentStatus.name() + "\"" ;
        try {
            ResultSet result = stat.executeQuery(sql);
            int id, employeeID; double priceForRent;
            String customerID, vehicleID, typeOfVehicle, status;
            Employee employee; Customer customer; Vehicle vehicle = null; Date dateOfRental, dateOfReturn;
            int x = 0;
            int resultSize = countSizeResultSet(result);
            result = stat.executeQuery(sql);
            result.next();
            while(x< resultSize) {
                for (int i=0; i<x-1; i++ )
                    result.next();
                id = result.getInt("id");
                customerID = result.getString("customerID");
                vehicleID = result.getString("vehicleID");
                typeOfVehicle = result.getString("vehicleType");
                priceForRent = result.getDouble("totalPrice");
                dateOfRental = result.getDate("startDate");
                dateOfReturn = result.getDate("endDate");
                employeeID = result.getInt("employeeID");
                status = result.getString("status");
                employee = getEmployeeByID(employeeID);
                customer  = getCustomerByPesel(customerID);

                switch (VehicleType.valueOf(typeOfVehicle)) {
                    case car:
                        vehicle = getCarByID(vehicleID);
                        break;
                    case bike:
                        vehicle = getbikeByID(vehicleID);
                        break;
                    case motorcycle:
                        vehicle = getMotorcycleByID(vehicleID);
                        break;
                }
                rent.add(new Rent(vehicle, customer, employee, id, priceForRent, dateOfRental, dateOfReturn, RentStatus.valueOf(status)));
                x++;
                result = stat.executeQuery(sql);
                result.next();
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get lists all customer rents");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        return rent;
    }

    private static List<Car> getAllCars(VehicleStatus vehicleStatus) throws ErrorMessageException {
        List<Car> cars = new ArrayList<>();
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM cars where vehicleStatus = " + "\"" + vehicleStatus.name() + "\"");
            int productionYear, numberOffPersons, mileage; double dailyPrice, engineCapacity,fuelUsage;
            String id, name, vehicleType, color, fuelType;
            while(result.next()) {
                id = result.getString("id");
                vehicleType = result.getString("vehicleType");
                name = result.getString("name");
                dailyPrice = result.getDouble("dailyPrice");
                color = result.getString("color");
                productionYear = result.getInt("productionYear");
                mileage = result.getInt("mileage");
                engineCapacity = result.getDouble("engineCapacity");
                fuelType = result.getString("FuelType");
                fuelUsage = result.getDouble("fuelUsage");
                numberOffPersons = result.getInt("numberOffPersons");

                cars.add(new Car(id, vehicleStatus, VehicleType.valueOf(vehicleType), name, dailyPrice,
                        String.valueOf(color), productionYear, mileage, engineCapacity, fuelUsage, FuelType.valueOf(fuelType),
                        numberOffPersons));
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get lists all cars");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        return cars;
    }

    private static List<Bike> getAllBikes(VehicleStatus vehicleStatus) throws ErrorMessageException {
        List<Bike> bikes = new ArrayList<>();
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM bikes where vehicleStatus=" + "\"" + vehicleStatus.name() + "\"");
            int productionYear; double dailyPrice;
            String vehicleType, name, color, id;
            boolean availability;
            while(result.next()) {
                id = result.getString("id");
                vehicleType = result.getString("vehicleType");
                name = result.getString("name");
                dailyPrice = result.getDouble("dailyPrice");
                color = result.getString("color");
                productionYear = result.getInt("productionYear");

                bikes.add(new Bike(id, vehicleStatus, VehicleType.valueOf(vehicleType), name, dailyPrice, String.valueOf(color), productionYear));
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get lists all Bikes");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        return bikes;
    }

    private static List<Motorcycle> getAllMotorcycles(VehicleStatus vehicleStatus) throws ErrorMessageException {
        List<Motorcycle> motorcycles = new ArrayList<>();
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM motorcycles where vehicleStatus = " + "\"" + vehicleStatus + "\"");
            double dailyPrice, fuelUsage, engineCapacity; int productionYear, mileage;
            String id, name, vehicleType, color;
            boolean availability;
            while(result.next()) {
                id = result.getString("id");
                vehicleType = result.getString("vehicleType");
                name = result.getString("name");
                dailyPrice = result.getDouble("dailyPrice");
                color = result.getString("color");
                productionYear = result.getInt("productionYear");
                mileage = result.getInt("mileage");
                engineCapacity = result.getDouble("engineCapacity");
                fuelUsage = result.getDouble("fuelUsage");

                motorcycles.add(new Motorcycle(id, vehicleStatus, VehicleType.valueOf(vehicleType), name, dailyPrice,
                        String.valueOf(color), productionYear, mileage, engineCapacity, fuelUsage));
            }
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get lists all motorcycles");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
        return motorcycles;
    }

    private static double getCarPricePerDay(String carID) throws ErrorMessageException {
        try {
            ResultSet carResult = stat.executeQuery("SELECT * FROM cars where id=" + "\"" + carID + "\"");
            carResult.next();
            return carResult.getDouble("dailyPrice");
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get price per day from cars table ");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    private static double getBikePricePerDay(String bikeID) throws ErrorMessageException {
        try {
            ResultSet bikeResult = stat.executeQuery("SELECT * FROM bikes where id=" + "\"" + bikeID + "\"");
            bikeResult.next();
            return bikeResult.getDouble("dailyPrice");
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get price per day from bikes table ");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    private static double getMotorcyclePricePerDay(String motorcycleID) throws ErrorMessageException {
        try {
            ResultSet motorcycleResult = stat.executeQuery("SELECT * FROM motorcycles where id=" + "\"" +motorcycleID + "\"" );
            motorcycleResult.next();
            return motorcycleResult.getDouble("dailyPrice");
        } catch (SQLException e) {
            Logs.logger.warning("Error when try to get price per day from motorcycles table ");
            Logs.logger.warning(e.getMessage());
            throw new ErrorMessageException("Błąd systemu, skontaktuj się z administratorem");
        }
    }

    private static int countSizeResultSet(ResultSet result) throws SQLException {
        int size = 0;
        while(result.next()){
            size++;
        }
        return size;
    }


    private static final String CustomerTableStruct = "(peselNumber varchar(11)," +
            "firstName varchar(30)," +
            "lastName varchar(50)," +
            "address varchar(255)," +
            "phoneNumber int," +
            "email varchar(255)," +
            "companyName varchar(255)," +
            "nipNumber varchar(255)," +
            "companyAddress varchar(255)," +
            "sum_paid_for_all_rents double," +
            "status int," +  // status 0 - actual, 1 - archival
            " PRIMARY KEY (peselNumber)" ;

    private static final String EmployeeTableStruct = "(UUID  int NOT NULL AUTO_INCREMENT," +
            "firstName varchar(30)," +
            "lastName varchar(50)," +
            "address varchar(50)," +
            "phoneNumber int," +
            "email varchar(50)," +
            "status int, " +
            "login varchar(50) NOT NULL UNIQUE, " +
            "password varchar(70) NOT NULL, " +
            "salt varchar(700) NOT NULL, " +
            "active int DEFAULT  NULL , " +
            "managerPermission BOOLEAN NOT NULL , " +
            " PRIMARY KEY (UUID)" ;

    private static final String RentTableStruct = "(id  int NOT NULL AUTO_INCREMENT," +
            "customerID varchar(11)," +
            "vehicleID varchar(10), " +
            "employeeID int," +
            "vehicleType varchar(30)," + // 1 - car, 2 - bike, 3 - motorcycle
            "totalPrice double," +
            "startDate DATE," + // in format 01-23-2018 | DD-MM-YYYY
            "endDate DATE   ," +  // in format 01-23-2018 | DD-MM-YYYY
            "status varchar(30)," + // status 0 - actual, 1 - archival;
            " PRIMARY KEY (id)," ;

    private static final String CarTableStruct = "(id varchar(10)," +
            "vehicleStatus  varchar(30)," +
            "vehicleType  varchar(30)," + // 1 - car, 2 - bike, 3 - motorcycle
            "name varchar(30)," + // 0 - available, 1 - not available ,2 archived
            "dailyPrice double," +
            "color varchar(20)," +
            "productionYear int,"+
            "mileage long,"+
            "engineCapacity double,"+
            "FuelType varchar(30),"+ //0 -4
            "fuelUsage double,"+
            "numberOffPersons int," +
            " PRIMARY KEY (id)" ;

    private static final String BikeTableStruct = "(id varchar(10)," +
            "vehicleStatus  varchar(30)," +
            "vehicleType  varchar(30)," + // 1 - car, 2 - bike, 3 - motorcycle
            "name varchar(30)," + // 0 - available, 1 - not available ,2 archived
            "dailyPrice double," +
            "color varchar(20)," +
            "productionYear int," +
            " PRIMARY KEY (id)" ;

    private static final String MotorcycleTableStruct = "(id varchar(10)," +
            "vehicleStatus  varchar(30)," +
            "vehicleType  varchar(30)," + // 1 - car, 2 - bike, 3 - motorcycle
            "name varchar(30)," + // 0 - available, 1 - not available ,2 archived
            "dailyPrice double," +
            "color varchar(20)," +
            "productionYear int,"+
            "mileage long,"+
            "engineCapacity double,"+
            "fuelUsage double," +
            " PRIMARY KEY (id)" ;

    /**
     * Setup Database
     */
    private static void createTables() throws ErrorMessageException {
        // Relations
        String databaseDefinition= "CREATE DATABASE IF NOT EXISTS vehicle_rental; ";
        String relationRents_Customers = "  FOREIGN KEY (customerID) REFERENCES customers(peselNumber),";
        String relationEmpolyee_Rent = "  FOREIGN KEY (employeeID) REFERENCES employees(UUID)";

        // Creating tables definition
        String employeeTable = "CREATE TABLE IF NOT EXISTS employees " +  EmployeeTableStruct + "); ";
        String customersTable = "CREATE TABLE IF NOT EXISTS customers" +  CustomerTableStruct + ");";
        String CarTable = "CREATE TABLE IF NOT EXISTS cars" +  CarTableStruct  + ")";
        String bikeTable= "CREATE TABLE IF NOT EXISTS bikes" +  BikeTableStruct  + ")";
        String motorcycleTable = "CREATE TABLE IF NOT EXISTS motorcycles" +  MotorcycleTableStruct  + ")";
        String RentTable = "CREATE TABLE IF NOT EXISTS rents" +  RentTableStruct +  relationRents_Customers + relationEmpolyee_Rent + ");";

        try {
            // Create tables
            stat.execute(databaseDefinition);
            stat.execute("use vehicle_rental ;");
            stat.execute(employeeTable);
            stat.execute(customersTable);
            stat.execute(CarTable);
            stat.execute(bikeTable);
            stat.execute(motorcycleTable);
            stat.execute(RentTable);

        } catch (SQLException e) {
            Logs.logger.warning("Can't create tables properly");
            Logs.logger.warning(e.toString());
            throw new ErrorMessageException("Contact with administrator");
        }
    }
}

