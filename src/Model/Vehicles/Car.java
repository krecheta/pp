package Model.Vehicles;

import Model.CustomEnumValues.Color;
import Model.CustomEnumValues.FuelType;
import Model.CustomEnumValues.VehicleStatus;
import Model.CustomEnumValues.VehicleType;

public class Car extends Vehicle {
    private final int mileage;
    private final double engineCapacity; //pojemność slinka
    private final FuelType fuelType;
    private final double fuelUsage;
    private final int numberOffPersons;

    public Car(String id, VehicleStatus vehicleStatus, VehicleType vehicleType, String name, double dailyPrice,
               Color color, int productionYear, int mileage, double engineCapacity, double fuelUsage, FuelType fuelType,
               int numberOffPersons) {
        super(id, vehicleStatus, vehicleType, name, dailyPrice, color, productionYear);
        this.mileage = mileage;
        this.engineCapacity = engineCapacity;
        this.fuelUsage = fuelUsage;
        this.fuelType = fuelType;
        this.numberOffPersons = numberOffPersons;
    }

    @Override
    public String toString() {
        return "{" +
            super.toString() +
            ", paliwo = " + this.fuelType +
            ", pojemność silnika = " + this.engineCapacity +
            ", przejechane kilometry = " + this.mileage +
                ", zużycie paliwa = " + this.fuelUsage +
            ", liczba osób = " + this.numberOffPersons+
            '}';
    }

    public int getMileage() {return this.mileage; }
    public double getEngineCapacity() { return this.engineCapacity; }
    public double getFuelUsage() { return this.fuelUsage; }
    public FuelType getFuelType() { return this.fuelType; }
    public int getNumberOffPersons() { return this.numberOffPersons; }

}

