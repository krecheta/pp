package Model.Vehicles;

import Model.CustomEnumValues.Color;
import Model.CustomEnumValues.VehicleStatus;
import Model.CustomEnumValues.VehicleType;

public abstract class Vehicle {
    private final String id;
    private final VehicleStatus vehicleStatus;
    private final VehicleType vehicleType;
    private final String name;
    private final double dailyPrice;
    private final Color color;
    private final int productionYear;

    public Vehicle(String id, VehicleStatus vehicleStatus, VehicleType vehicleType, String name, double dailyPrice, Color color, int productionYear) {
        this.id = id;
        this.vehicleStatus = vehicleStatus;
        this.vehicleType = vehicleType;
        this.name = name;
        this.dailyPrice = dailyPrice;
        this.color = color;
        this.productionYear = productionYear;
    }

    @Override
    public String toString() {
   return  "id = " + this.id +
           ", nazwa = " + this.name +
           ", kolor = " + this.color +
           ", rok produkcji = " + this.productionYear +
           ", dostepnosc = "  + this.vehicleStatus.toString() +
           ", cena za dzien = " + this.dailyPrice;
    }

    public String getId() {
        return this.id;
    }
    public VehicleStatus getVehicleStatus() { return this.vehicleStatus; }
    public VehicleType getVehicleType() { return this.vehicleType; }
    public String getName() {
        return this.name;
    }
    public double getDailyPrice() {
        return this.dailyPrice;
    }
    public Color getColor() { return this.color; }
    public int getProductionYear() { return this.productionYear; }
}
