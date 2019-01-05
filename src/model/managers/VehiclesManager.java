package model.managers;

import database.DatabaseManager;
import model.enums.FuelType;
import model.enums.VehicleStatus;
import model.enums.VehicleType;
import model.exceptions.ErrorMessageException;
import model.vehicles.Bike;
import model.vehicles.Car;
import model.vehicles.Motorcycle;
import model.vehicles.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehiclesManager {

    public List<Vehicle> getAllAvaiableVehicles() throws ErrorMessageException {
        List<Vehicle> avaiableVehicles = new ArrayList<>();
        avaiableVehicles.addAll(DatabaseManager.getAllAvaiableCars());
        avaiableVehicles.addAll(DatabaseManager.getAllAvaiableBikes());
        avaiableVehicles.addAll(DatabaseManager.getAllAvaiableMotorcycles());
        return avaiableVehicles;
    }

    public List<Vehicle> getAllVehicles() throws ErrorMessageException {
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.addAll(DatabaseManager.getAllAvaiableCars());
        vehicles.addAll(DatabaseManager.getAllABorrowedCars());
        vehicles.addAll(DatabaseManager.getAllAArchivalCars());

        vehicles.addAll(DatabaseManager.getAllAvaiableBikes());
        vehicles.addAll(DatabaseManager.getAllAArchivalBikes());
        vehicles.addAll(DatabaseManager.getAllBorrowedBikes());

        vehicles.addAll(DatabaseManager.getAllAvaiableMotorcycles());
        vehicles.addAll(DatabaseManager.getAllAArchivalMotorcycles());
        vehicles.addAll(DatabaseManager.getAllABorrowedMotorcycles());

        return vehicles;
    }

    public void addVehicle(Vehicle vehicle) throws ErrorMessageException{
        if (vehicle instanceof Car){
            String id = vehicle.getId();
            String name = vehicle.getName();
            double dailyPrice = vehicle.getDailyPrice();
            String color = vehicle.getColor();
            int productionYear = vehicle.getProductionYear();
            int mileage = ((Car) vehicle).getMileage();
            double engineCapacity = ((Car) vehicle).getEngineCapacity();
            double fuelUsage = ((Car) vehicle).getFuelUsage();
            FuelType fuelType = ((Car) vehicle).getFuelType();
            int numberOffPersons = ((Car) vehicle).getNumberOffPersons();
            if(DatabaseManager.getCarByID(id).getId() == null)
                DatabaseManager.addCar(id, name, dailyPrice, color, productionYear, mileage, engineCapacity, fuelUsage, fuelType, numberOffPersons);
            else
                throw new ErrorMessageException("Pojazd o podanym numerze rejestracyjnym już istnieje.");
        }

        else if( vehicle instanceof Bike){
            String id = vehicle.getId();
            String name = vehicle.getName();
            double dailyPrice = vehicle.getDailyPrice();
            String color = vehicle.getColor();
            int productionYear = vehicle.getProductionYear();
            if(DatabaseManager.getbikeByID(id).getId() == null)
                DatabaseManager.addBike(id, name, dailyPrice, color, productionYear);
            else
                throw new ErrorMessageException("Pojazd o podanym numerze rejestracyjnym już istnieje.");

        }

        else {
            String id = vehicle.getId();
            String name = vehicle.getName();
            double dailyPrice = vehicle.getDailyPrice();
            String color = vehicle.getColor();
            int productionYear = vehicle.getProductionYear();
            int mileage = ((Motorcycle) vehicle).getMileage();
            double engineCapacity = ((Motorcycle) vehicle).getEngineCapacity();
            double fuelUsage = ((Motorcycle) vehicle).getFuelUsage();
            if(DatabaseManager.getMotorcycleByID(id).getId() == null)
                DatabaseManager.addMotorcycle(id, name, dailyPrice, color, productionYear, mileage, engineCapacity, fuelUsage);
            else
                throw new ErrorMessageException("Pojazd o podanym numerze rejestracyjnym już istnieje.");
            }
    }

    public void editVehicle(Vehicle vehicle) throws ErrorMessageException{
        if (vehicle instanceof Car){
            String id = vehicle.getId();
            String name = vehicle.getName();
            double dailyPrice = vehicle.getDailyPrice();
            String color = vehicle.getColor();
            int productionYear = vehicle.getProductionYear();
            int mileage = ((Car) vehicle).getMileage();
            double engineCapacity = ((Car) vehicle).getEngineCapacity();
            double fuelUsage = ((Car) vehicle).getFuelUsage();
            FuelType fuelType = ((Car) vehicle).getFuelType();
            int numberOffPersons = ((Car) vehicle).getNumberOffPersons();
            DatabaseManager.updateCar(id, name, dailyPrice, color, productionYear, mileage, engineCapacity, fuelUsage, fuelType, numberOffPersons);
        }

        else if( vehicle instanceof Bike){
            String id = vehicle.getId();
            String name = vehicle.getName();
            double dailyPrice = vehicle.getDailyPrice();
            String color = vehicle.getColor();
            int productionYear = vehicle.getProductionYear();
            DatabaseManager.updateBike(id, name, dailyPrice, color, productionYear);
        }

        else {
            String id = vehicle.getId();
            String name = vehicle.getName();
            double dailyPrice = vehicle.getDailyPrice();
            String color = vehicle.getColor();
            int productionYear = vehicle.getProductionYear();
            int mileage = ((Motorcycle) vehicle).getMileage();
            double engineCapacity = ((Motorcycle) vehicle).getEngineCapacity();
            double fuelUsage = ((Motorcycle) vehicle).getFuelUsage();
            DatabaseManager.updateMotorcycle(id, name, dailyPrice, color, productionYear, mileage, engineCapacity, fuelUsage);
        }
    }

    public void archiveVehicle(Vehicle vehicle) throws ErrorMessageException{
        DatabaseManager.markVehicleAsArchival(vehicle.getId(), vehicle.getVehicleType());
    }

    public List<Vehicle> getFilteredVehicles(String name, String color, String typeOfComparePriceMin, String typeOfComparePriceMax, double minPrice, double maxPrice,
                                             String typeOfCompareProductionYeatMin, String typeOfCompareProductionYeatMax, int minProductionYear, int maxProductionYear,
                                             String typeOfCompareMileageMin, String typeOfCompareMileageMax, long minMileage, long maxMileage, String typeOfCompareEngineCapacityMin,
                                             String typeOfCompareEngineCapacityMax, double minCapacity, double maxCapacity, FuelType fuelType,
                                             String typeOfCompareFuelUsageMin, String typeOfCompareFuelUsageMax, double minFuelUsage, double maxFuelUsage, int quantityOfPerson, String vehicleID,
                                             VehicleType vehicleType, VehicleStatus vehicleStatus) throws ErrorMessageException{
        List<Vehicle> vehicleList = new ArrayList<>();
        if (vehicleType == VehicleType.car || vehicleType == null)
            vehicleList.addAll(DatabaseManager.getFilteredCars(name, color, typeOfComparePriceMin,typeOfComparePriceMax, minPrice, maxPrice,
                            typeOfCompareProductionYeatMin, typeOfCompareProductionYeatMax, minProductionYear, maxProductionYear,
                            typeOfCompareMileageMin, typeOfCompareMileageMax, minMileage, maxMileage, typeOfCompareEngineCapacityMin,
                            typeOfCompareEngineCapacityMax, minCapacity, maxCapacity, fuelType,
                            typeOfCompareFuelUsageMin, typeOfCompareFuelUsageMax, minFuelUsage, maxFuelUsage, quantityOfPerson, vehicleStatus, vehicleID));

        if(fuelType == null && quantityOfPerson == -1 && (vehicleType == VehicleType.motorcycle || vehicleType == null))
            vehicleList.addAll(DatabaseManager.getFilteredMotorcycles(name, color, typeOfComparePriceMin,typeOfComparePriceMax, minPrice, maxPrice,
                                typeOfCompareProductionYeatMin, typeOfCompareProductionYeatMax, minProductionYear, maxProductionYear,
                                typeOfCompareMileageMin, typeOfCompareMileageMax, minMileage, maxMileage, typeOfCompareEngineCapacityMin,
                                typeOfCompareEngineCapacityMax, minCapacity, maxCapacity,
                                typeOfCompareFuelUsageMin, typeOfCompareFuelUsageMax, minFuelUsage, maxFuelUsage, vehicleStatus, vehicleID));


        if(fuelType == null && quantityOfPerson == -1 && typeOfCompareMileageMin == null && typeOfCompareMileageMax == null  && typeOfCompareEngineCapacityMax == null
            && typeOfCompareFuelUsageMin == null && (vehicleType == VehicleType.bike || vehicleType == null))
            vehicleList.addAll(DatabaseManager.getFilteredBikes(name, color, typeOfComparePriceMin,typeOfComparePriceMax, minPrice, maxPrice,
                    typeOfCompareProductionYeatMin, typeOfCompareProductionYeatMax, minProductionYear, maxProductionYear, vehicleStatus, vehicleID));

        return vehicleList;
    }
}
