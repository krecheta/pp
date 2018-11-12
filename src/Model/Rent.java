package Model;

import Model.Vehicles.Bike;
import Model.Vehicles.Car;
import Model.Vehicles.Motorcycle;
import Model.Vehicles.Vehicle;

public class Rent {
    private final int rentID;
    private final Client client;
    private Car car;
    private Bike bike;
    private Motorcycle motorcycle;
    private final int priceForRent;
    private final String dateOfRental;
    private final String dateOfReturn;

    public Rent(Car car, Client client, int rentID, int priceForRent, String dateOfRental, String dateOfReturn) {
        this.car = car;
        this.client = client;
        this.rentID = rentID;
        this.priceForRent = priceForRent;
        this.dateOfRental = dateOfRental;
        this.dateOfReturn = dateOfReturn;
    }

    public Rent(Bike bike, Client client, int rentID, int priceForRent, String dateOfRental, String dateOfReturn) {
        this.bike = bike;
        this.client = client;
        this.rentID = rentID;
        this.priceForRent = priceForRent;
        this.dateOfRental = dateOfRental;
        this.dateOfReturn = dateOfReturn;
    }

    public Rent(Motorcycle motorcycle, Client client, int rentID, int priceForRent, String dateOfRental, String dateOfReturn) {
        this.motorcycle = motorcycle;
        this.client = client;
        this.rentID = rentID;
        this.priceForRent = priceForRent;
        this.dateOfRental = dateOfRental;
        this.dateOfReturn = dateOfReturn;
    }

    public Vehicle getVehicle() {
        if(this.car != null)
            return this.car;
        if(this.bike != null)
            return this.bike;
        return this.motorcycle;
    }

    public int getTypeOfVehicle() {
        if(this.car != null)
            return 1;
        if(this.bike != null)
            return 2;
        return 3;
    }

    public Client getClient() {
        return client;
    }

    public int getRentID() {
        return rentID;
    }

    @Override
    public String toString() {
        if(this.car != null){
            return "Rent[" +
                    "car = " + this.car.toString() +
                    ", client=" + this.client.toString() +
                    ", rentID=" + rentID +
                    ", priceForRent=" + priceForRent +
                    '}';
        }
        if(this.bike != null){
            return "Rent[" +
                    "bike = " + this.bike.toString() +
                    ", client=" + this.client.toString() +
                    ", rentID=" + rentID +
                    ", priceForRent=" + priceForRent +
                    '}';
        }
            return "Rent[" +
                    "motorcycle = " + this.motorcycle.toString() +
                    ", client=" + this.client.toString() +
                    ", rentID=" + rentID +
                    ", priceForRent=" + priceForRent +
                    '}';
        }

    public int getPriceForRent() {
        return priceForRent;
    }

    /**
     *
     * @return Date of rental in format dd-MM-yyyy
     */
    public String getDateOfRental() {
        return dateOfRental;
    }

    /**
     *
     * @return Date of return vehicle in format dd-MM-yyyy
     */
    public String getDateOfReturn() {
        return dateOfReturn;
    }
}
