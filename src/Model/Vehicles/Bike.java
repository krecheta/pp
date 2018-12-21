package Model.Vehicles;

import Model.CustomEnumValues.Color;
import Model.CustomEnumValues.VehicleStatus;
import Model.CustomEnumValues.VehicleType;

public class Bike extends Vehicle{
    public Bike(String id, VehicleStatus vehicleStatus, VehicleType vehicleType, String name, double dailyPrice,
                Color color, int productionYear) {
        super(id, vehicleStatus, vehicleType, name, dailyPrice, color, productionYear);
    }

    @Override
    public String toString() {
        return "{" +
                super.toString() +
                '}';
    }
}
