package model.vehicles;

import model.enums.VehicleStatus;
import model.enums.VehicleType;

public abstract class Vehicle {
    private final String id;
    private final VehicleStatus vehicleStatus;
    private final VehicleType vehicleType;
    private final String name;
    private final double dailyPrice;
    private final String color;
    private final int productionYear;

    public Vehicle(String id, VehicleStatus vehicleStatus, VehicleType vehicleType, String name, double dailyPrice, String color, int productionYear) {
        this.id = id;
        this.vehicleStatus = vehicleStatus;
        this.vehicleType = vehicleType;
        this.name = name;
        this.dailyPrice = dailyPrice;
        this.color = color;
        this.productionYear = productionYear;

        if(vehicleStatus.equals(VehicleStatus.avaiable)){
            statusProperty = "dostępny";
        } else if(vehicleStatus.equals(VehicleStatus.unavaiable)){
            statusProperty = "niedostępny";
        } else {
            statusProperty = "zarchiwizowany";
        }

        if(vehicleType.equals(VehicleType.car)){
            typeProperty = "samochód";
        } else if(vehicleType.equals(VehicleType.bike)){
            typeProperty = "rower";
        } else {
            typeProperty = "motocykl";
        }
    }

    @Override
    public String toString() {
        return name;
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
    public String getColor() { return this.color; }
    public int getProductionYear() { return this.productionYear; }

    protected String statusProperty;
    protected String typeProperty;
    protected String mileageProperty;
    protected String engineCapacityProperty;
    protected String fuelTypeProperty;
    protected String fuelUsageProperty;
    protected String numOfPersonsProperty;

    public String getMileageProperty() {
        return mileageProperty;
    }

    public String getEngineCapacityProperty() {
        return engineCapacityProperty;
    }

    public String getFuelTypeProperty() {
        return fuelTypeProperty;
    }

    public String getFuelUsageProperty() {
        return fuelUsageProperty;
    }

    public String getNumOfPersonsProperty() {
        return numOfPersonsProperty;
    }

    public String getStatusProperty() {
        return statusProperty;
    }

    public String getTypeProperty() {
        return typeProperty;
    }
}
