package Model.ModelManagers;

import DataBase.DatabaseManager;
import Model.CustomEnumValues.VehicleStatus;
import Model.CustomEnumValues.VehicleType;
import Model.CustomExceptions.ErrorMessageException;
import Model.Customer;
import Model.Employee;
import Model.Logs;
import Model.Rent;
import Model.Vehicles.Vehicle;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class RentsManager {

    public void addRent(Vehicle vehicle, Customer customer, Employee employee, java.sql.Date startDate, java.sql.Date endDate) throws ErrorMessageException {
        String vehicleID = vehicle.getId();
        double priceForRent = calculatePriceForRent(customer, vehicle, startDate, endDate);
        VehicleStatus vehicleStatus= null;
        VehicleType typeOfVehicle = vehicle.getVehicleType();
          switch (typeOfVehicle) {
              case car:
                  vehicleStatus = DatabaseManager.getCarByID(vehicleID).getVehicleStatus();
                  break;
              case bike:
                  vehicleStatus = DatabaseManager.getbikeByID(vehicleID).getVehicleStatus();
                  break;
              case motorcycle:
                  vehicleStatus = DatabaseManager.getMotorcycleByID(vehicleID).getVehicleStatus();
                  break;
          }

//rent a vehicle
          if (vehicleStatus == VehicleStatus.avaiable) {
              try {
                  DatabaseManager.setVehicleUnavaiable(vehicleID, typeOfVehicle);
              } catch (ErrorMessageException e) {
                  DatabaseManager.setVehicleAvaiable(vehicleID, typeOfVehicle);
                  throw e;
              }
              DatabaseManager.addRent(customer.getPeselNumber(), vehicleID, typeOfVehicle, priceForRent, startDate, endDate, employee.getUUID());
          } else
              throw new ErrorMessageException("Vehicle is inaccessible");
    }

    public void endRent(Rent rent, long mileage, double addCosts) throws ErrorMessageException {
        java.sql.Date returnDate= new java.sql.Date(new Date().getTime());
        Rent rentFromDatabase = DatabaseManager.getRentByRentID(rent.getRentID());
        DatabaseManager.markRentAsArchival(rent.getRentID());
        DatabaseManager.setVehicleAvaiable(rent.getVehicle().getId(), rent.getVehicle().getVehicleType());
        DatabaseManager.updateVehicleMileage(rent.getVehicle().getId(), rent.getTypeOfVehicle(), mileage);
        double additional = additionalPayment(rentFromDatabase);
        double totalPriceForRent = (double)Math.round((rentFromDatabase.getPriceForRent() + additional + addCosts)* 100) / 100;
        if(additional > 0)
            DatabaseManager.updateRentDate(rent.getRentID(), returnDate);
        DatabaseManager.updateRentPrice(rent.getRentID(),  totalPriceForRent);
        DatabaseManager.updateCustomerSumPaid(rent.getCustomer().getPeselNumber(), rent.getCustomer().getSumPaidForAllRents() + totalPriceForRent);

    }

    public List<Rent> getAllActualRents() throws ErrorMessageException {
        return DatabaseManager.getAllDuringRents();
    }

    public  List<Rent> getAllArchivalRents() throws ErrorMessageException {
        return DatabaseManager.getAllEndedRents();
    }

    public Rent getRentByRentID(int rentID) throws ErrorMessageException {
        return DatabaseManager.getRentByRentID(rentID);
    }
    private String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return sdf.format(date);
    }


    public List<Rent> getFilteredRents(String vehicleID, String peselNumber, int employeeID, String rentTypeStartMin, String rentTypeStartMax , java.sql.Date startDateMin,  java.sql.Date startDateMax,
                                       String rentTypeEndMin, String rentTypeMax,  java.sql.Date endDateMin,  java.sql.Date endDateMax, String costTypeMin, String costTypeMax, double costMin, double costMax) throws ErrorMessageException {

        return DatabaseManager.getFilteredRents(vehicleID, peselNumber, employeeID, rentTypeStartMin, rentTypeStartMax ,startDateMin, startDateMax,
                                                rentTypeEndMin, rentTypeMax, endDateMin, endDateMax, costTypeMin, costTypeMax, costMin, costMax);
    }


    private double additionalPayment(Rent rent){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        long days = countDays(rent.getEndDate(), date )-1;
        if (days == 0)
            return 0;
        else
            return (double)Math.round((rent.getVehicle().getDailyPrice() + (rent.getVehicle().getDailyPrice() * 0.1)) * (int)days* 100) / 100;
        // Jezeli klient nie oddał w okraślonym terminie doliczamy karę za kazdy dzien w wysokośći 10% stawki dziennej
    }

    private double calculatePriceForRent(Customer customer, Vehicle vehicle, Date startDate, Date endDate) throws ErrorMessageException {
        double pricePerDay = DatabaseManager.getPricePerDayOfVehicle(vehicle.getId(), vehicle.getVehicleType());
        double discount = getClientPercentOfDiscount(customer);
        long days = countDays(startDate, endDate);

        double percentDiscountPricePerDay = ((100 - discount) % 100);
        if(percentDiscountPricePerDay == 0)
            return  (double)Math.round((days * pricePerDay)* 100) / 100;
        else
            return   (double)Math.round(((days * pricePerDay) * percentDiscountPricePerDay/100) * 100) / 100;
    }

    private double getClientPercentOfDiscount(Customer client){
        double sumPaideByClient = client.getSumPaidForAllRents();
        double percentOfDiscount = (sumPaideByClient / 10000) * 5;
        if(percentOfDiscount > 25)
            return 25;
        return percentOfDiscount;
    }

    private long countDays(Date startDate, Date endDate ){
        long diff;
        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diff + 1;
    }
}
