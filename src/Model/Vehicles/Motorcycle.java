package Model.Vehicles;

import Model.CustomEnumValues.Color;
import Model.CustomEnumValues.VehicleStatus;
import Model.CustomEnumValues.VehicleType;

public class Motorcycle extends Vehicle {
    private final int mileage;
    private final double engineCapacity; //pojemność slinka
    private final double fuelUsage;



    public Motorcycle(String id, VehicleStatus vehicleStatus, VehicleType vehicleType, String name, double dailyPrice,
                      Color color, int productionYear, int mileage, double engineCapacity, double fuelUsage) {
        super(id, vehicleStatus, vehicleType, name, dailyPrice, color, productionYear);
        this.mileage = mileage;
        this.engineCapacity = engineCapacity;
        this.fuelUsage = fuelUsage;
    }

    @Override
    public String toString() {
        return "{" +
                super.toString() +
                ", przejechane kilometry = " + this.mileage +
                ", pojemność silnika = " + this.engineCapacity +
                ", zużycie paliwa = " + this.fuelUsage +
                '}';
    }

    public int getMileage() { return this.mileage; }
    public double getEngineCapacity() { return this.engineCapacity; }
    public double getFuelUsage() { return this.fuelUsage; }
}
