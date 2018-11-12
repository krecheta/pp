package Model.Vehicles;

import Model.CustomEnumValues.Fuel;

public class Car extends Vehicle {

    private final String model;
    private final Fuel fuel;
    private final int engineCapacity; //pojemność slinka
    private final int trunkCapacity; //pojemność bagażnika
    private final int numberOfDoors;

    /**
     *
     * @param id KEY in database, is like EL "3D33"
     * @param name
     * @param course
     * @param availability
     * @param model
     * @param fuel type of fuel is it petrol or diesel
     * @param engineCapacity
     * @param trunkCapacity
     * @param numberOfDoors
     * @param pricePerDay
     */
    public Car(String id, String name, int course, boolean availability, String model, Fuel fuel, int engineCapacity, int trunkCapacity, int numberOfDoors, int pricePerDay) {
        super(id, name, course, availability, pricePerDay);
        this.model = model;
        this.fuel = fuel;
        this.engineCapacity = engineCapacity;
        this.trunkCapacity = trunkCapacity;
        this.numberOfDoors = numberOfDoors;
    }

    public String getModel() {
        return model;
    }

    public Fuel getFuel() {
        return fuel;
    }

    public int getEngineCapacity() {
        return engineCapacity;
    }

    public int getTrunkCapacity() {
        return trunkCapacity;
    }

    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    @Override
    public String toString() {
        return "Car{" +
                "model='" + model + '\'' +
                ", fuel=" + fuel +
                ", engineCapacity=" + engineCapacity +
                ", trunkCapacity=" + trunkCapacity +
                ", numberOfDoors=" + numberOfDoors +
                '}';
    }
}

