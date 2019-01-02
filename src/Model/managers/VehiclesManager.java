package model.managers;

import database.DatabaseManager;
import model.enums.Color;
import model.enums.FuelType;
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
        vehicles.addAll(DatabaseManager.getAllAArchivalCars());
        vehicles.addAll(DatabaseManager.getAllAvaiableBikes());
        vehicles.addAll(DatabaseManager.getAllAArchivalBikes());
        vehicles.addAll(DatabaseManager.getAllAvaiableMotorcycles());
        vehicles.addAll(DatabaseManager.getAllAArchivalMotorcycles());
        return vehicles;
    }

    public void addVehicle(Vehicle vehicle) throws ErrorMessageException{
        if (vehicle instanceof Car){
            String id = vehicle.getId();
            String name = vehicle.getName();
            double dailyPrice = vehicle.getDailyPrice();
            Color color = vehicle.getColor();
            int productionYear = vehicle.getProductionYear();
            int mileage = ((Car) vehicle).getMileage();
            double engineCapacity = ((Car) vehicle).getEngineCapacity();
            double fuelUsage = ((Car) vehicle).getFuelUsage();
            FuelType fuelType = ((Car) vehicle).getFuelType();
            int numberOffPersons = ((Car) vehicle).getNumberOffPersons();
            DatabaseManager.addCar(id, name, dailyPrice, color, productionYear, mileage, engineCapacity, fuelUsage, fuelType, numberOffPersons);
        }

        else if( vehicle instanceof Bike){
            String id = vehicle.getId();
            String name = vehicle.getName();
            double dailyPrice = vehicle.getDailyPrice();
            Color color = vehicle.getColor();
            int productionYear = vehicle.getProductionYear();
            DatabaseManager.addBike(id, name, dailyPrice, color, productionYear);
        }

        else {
            String id = vehicle.getId();
            String name = vehicle.getName();
            double dailyPrice = vehicle.getDailyPrice();
            Color color = vehicle.getColor();
            int productionYear = vehicle.getProductionYear();
            int mileage = ((Motorcycle) vehicle).getMileage();
            double engineCapacity = ((Motorcycle) vehicle).getEngineCapacity();
            double fuelUsage = ((Motorcycle) vehicle).getFuelUsage();
            DatabaseManager.addMotorcycle(id, name, dailyPrice, color, productionYear, mileage, engineCapacity, fuelUsage);
            }
    }


    public void editVehicle(Vehicle vehicle) throws ErrorMessageException{
        if (vehicle instanceof Car){
            String id = vehicle.getId();
            String name = vehicle.getName();
            double dailyPrice = vehicle.getDailyPrice();
            Color color = vehicle.getColor();
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
            Color color = vehicle.getColor();
            int productionYear = vehicle.getProductionYear();
            DatabaseManager.updateBike(id, name, dailyPrice, color, productionYear);
        }

        else {
            String id = vehicle.getId();
            String name = vehicle.getName();
            double dailyPrice = vehicle.getDailyPrice();
            Color color = vehicle.getColor();
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

    public List<Vehicle> getFilteredVehicles(String name, String color, String typeOfComparePriceMin,String typeOfComparePriceMax, double minPrice, double maxPrice,
                                             String typeOfCompareProductionYeatMin, String typeOfCompareProductionYeatMax, int minProductionYear, int maxProductionYear,
                                             String typeOfCompareMileageMin, String typeOfCompareMileageMax, long minMileage, long maxMileage, String typeOfCompareEngineCapacityMin,
                                             String typeOfCompareEngineCapacityMax, double minCapacity, double maxCapacity, FuelType fuelType,
                                             String typeOfCompareFuelUsageMin, String typeOfCompareFuelUsageMax, double minFuelUsage, double maxFuelUsage, int quantityOfPerson) throws ErrorMessageException{
        List<Vehicle> vehicleList = new ArrayList<>();
        vehicleList.addAll(DatabaseManager.getFilteredCars(name, color, typeOfComparePriceMin,typeOfComparePriceMax, minPrice, maxPrice,
                            typeOfCompareProductionYeatMin, typeOfCompareProductionYeatMax, minProductionYear, maxProductionYear,
                            typeOfCompareMileageMin, typeOfCompareMileageMax, minMileage, maxMileage, typeOfCompareEngineCapacityMin,
                            typeOfCompareEngineCapacityMax, minCapacity, maxCapacity, fuelType,
                            typeOfCompareFuelUsageMin, typeOfCompareFuelUsageMax, minFuelUsage, maxFuelUsage, quantityOfPerson));

        if(fuelType == null && quantityOfPerson == -1)
            vehicleList.addAll(DatabaseManager.getFilteredMotorcycles(name, color, typeOfComparePriceMin,typeOfComparePriceMax, minPrice, maxPrice,
                                typeOfCompareProductionYeatMin, typeOfCompareProductionYeatMax, minProductionYear, maxProductionYear,
                                typeOfCompareMileageMin, typeOfCompareMileageMax, minMileage, maxMileage, typeOfCompareEngineCapacityMin,
                                typeOfCompareEngineCapacityMax, minCapacity, maxCapacity,
                                typeOfCompareFuelUsageMin, typeOfCompareFuelUsageMax, minFuelUsage, maxFuelUsage));


        if(fuelType == null && quantityOfPerson == -1 && typeOfCompareMileageMin == null && typeOfCompareMileageMax == null  && typeOfCompareEngineCapacityMax == null
            && typeOfCompareFuelUsageMin == null )
            vehicleList.addAll(DatabaseManager.getFilteredBikes(name, color, typeOfComparePriceMin,typeOfComparePriceMax, minPrice, maxPrice,
                    typeOfCompareProductionYeatMin, typeOfCompareProductionYeatMax, minProductionYear, maxProductionYear));

        return vehicleList;
    }
}
