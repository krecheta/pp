package model.vehicles;

import model.enums.VehicleStatus;
import model.enums.VehicleType;

public class Bike extends Vehicle{
    public Bike(String id, VehicleStatus vehicleStatus, VehicleType vehicleType, String name, double dailyPrice,
                String color, int productionYear) {
        super(id, vehicleStatus, vehicleType, name, dailyPrice, color, productionYear);

        mileageProperty = "-";
        engineCapacityProperty = "-";
        fuelTypeProperty = "-";
        fuelUsageProperty = "-";
        numOfPersonsProperty = "-";
    }
}
