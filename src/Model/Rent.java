package Model;

import Model.Vehicles.Bike;
import Model.Vehicles.Car;
import Model.Vehicles.Vehicle;

public class Rent {
    private final int rentID;
    private final Client client;
    private Vehicle vehicle;
    private final int priceForRent;
    private final String dateOfRental;
    private final String dateOfReturn;
    private final Employee employee ;

    public Rent(Vehicle vehicle, Client client, Employee employee, int rentID, int priceForRent, String dateOfRental, String dateOfReturn) {
        this.vehicle = vehicle;
        this.client = client;
        this.rentID = rentID;
        this.priceForRent = priceForRent;
        this.dateOfRental = dateOfRental;
        this.dateOfReturn = dateOfReturn;
        this.employee = employee;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public int getTypeOfVehicle() {
        if(this.vehicle instanceof Car)
            return 1;
        if(this.vehicle instanceof Bike)
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
        return "Wypożyczenie{" +
            this.getNameOfVehicle() + " = " + this.vehicle.toString() +
            ", klient = " + this.client.toString() +
            ", koszt wypożyczenia = " + priceForRent +
            ", data wypożyczenia = " + this.dateOfRental +
            ", planowana data zwrotu = " + this.dateOfReturn +
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

    private String getNameOfVehicle() {
        if(this.vehicle instanceof Car)
            return "Samochód";
        if(this.vehicle instanceof Bike)
            return "Rower";
        return "Motocykl";
    }

    public int getEmployeeID(){
        return this.employee.getId();
    }
}
