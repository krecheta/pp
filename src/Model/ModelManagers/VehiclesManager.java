package Model.ModelManagers;

import DataBase.DatabaseManager;
import Model.CustomEnumValues.Color;
import Model.CustomEnumValues.FuelType;
import Model.CustomExceptions.ErrorMessageException;
import Model.Vehicles.Bike;
import Model.Vehicles.Car;
import Model.Vehicles.Motorcycle;
import Model.Vehicles.Vehicle;

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

    public List<Vehicle> getFilteredVehicles(){
        return new ArrayList<>();
    }
}
