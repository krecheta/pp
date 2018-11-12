package Model.Vehicles;

public abstract class Vehicle {
    private final String id;
    private final String name;
    private final int course; // przebieg pojazdu w km
    private final boolean availability;
    private final int pricePerDay;

    /**
     * Basic abstract class all of vehicles
     * @param id KEY in database, is like EL "3D33"
     * @param name
     * @param course
     * @param availability
     * @param pricePerDay
     */
    Vehicle(String id, String name, int course, boolean availability, int pricePerDay) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.availability = availability;
        this.pricePerDay = pricePerDay;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCourse() {
        return course;
    }

    /**
     * Is possible to rent a vehicle
     * @return true is vehicle can be rent, otherwise flase
     */
    public boolean isAvailability() {
        return availability;
    }

    public int getPricePerDay() {
        return pricePerDay;
    }
}
