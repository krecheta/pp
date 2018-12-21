package Model;

import Model.CustomEnumValues.VehicleType;
import Model.Vehicles.Bike;
import Model.Vehicles.Car;
import Model.Vehicles.Vehicle;

public class Rent {
    private final int rentID;
    private Vehicle vehicle;
    private final Customer customer;
    private final Employee employee ;
    private final String startDate;
    private final String endDate;
    private final double totalPrice;


    public Rent(Vehicle vehicle, Customer customer, Employee employee, int rentID, double totalPrice, String startDate, String endDate) {
        this.rentID = rentID;
        this.vehicle = vehicle;
        this.customer = customer;
        this.employee = employee;
        this.totalPrice = totalPrice;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Wypożyczenie{" +
                this.getNameOfVehicle() + " = " + this.vehicle.toString() +
                ", klient = " + this.customer.toString() +
                ", koszt wypożyczenia = " + totalPrice +
                ", data wypożyczenia = " + this.startDate +
                ", planowana data zwrotu = " + this.endDate +
                '}';
    }

    public VehicleType getTypeOfVehicle() {
        return vehicle.getVehicleType();
    }

    public Customer getCustomer() { return customer; }
    public Vehicle getVehicle() {
        return this.vehicle;
    }
    public int getRentID() {
        return rentID;
    }
    public double getPriceForRent() {
        return totalPrice;
    }
    public String getStartDate() {
        return startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public int getEmployeeID(){ return this.employee.getUUID(); }

    private String getNameOfVehicle() {
        if(this.vehicle instanceof Car)
            return "Samochód";
        if(this.vehicle instanceof Bike)
            return "Rower";
        return "Motocykl";
    }

}
