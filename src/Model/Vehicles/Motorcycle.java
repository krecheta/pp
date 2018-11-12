package Model.Vehicles;

public class Motorcycle extends Vehicle {
    private final String model;
    private final int engineCapacity; // pojemność slinka

    /**
     *
     * @param id KEY in database, is like EL "3D33"
     * @param name
     * @param course
     * @param availability
     * @param model
     * @param engineCapacity
     * @param pricePerDay
     */
    public Motorcycle(String id, String name, int course, boolean availability, String model, int engineCapacity, int pricePerDay) {
        super(id, name, course, availability, pricePerDay);
        this.engineCapacity = engineCapacity;
        this.model = model;
    }

    public int getEngineCapacity() {
        return engineCapacity;
    }

    public String getModel() {
        return model;
    }

    @Override
    public String toString() {
        return "Motorcycle{" +
                "model='" + model + '\'' +
                ", engineCapacity=" + engineCapacity +
                '}';
    }
}
