package model;

import model.enums.RentStatus;
import model.enums.VehicleType;
import model.vehicles.Bike;
import model.vehicles.Car;
import model.vehicles.Vehicle;
import java.util.Date;

public class Rent {
    private final int rentID;
    private Vehicle vehicle;
    private final Customer customer;
    private final Employee employee;
    private final Date startDate;
    private final Date endDate;
    private final double totalPrice;
    private final RentStatus rentStatus;


    public Rent(Vehicle vehicle, Customer customer, Employee employee, int rentID, double totalPrice, Date startDate, Date endDate, RentStatus rentStatus) {
        this.rentID = rentID;
        this.vehicle = vehicle;
        this.customer = customer;
        this.employee = employee;
        this.totalPrice = totalPrice;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rentStatus = rentStatus;
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
    public double getTotalPrice() {
        return totalPrice;
    }
    public Date getStartDate() {
        return startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public int getEmployeeID(){ return this.employee.getUUID(); }
    public Employee getEmployee() {
        return employee;
    }
    public RentStatus getRentStatus() { return rentStatus; }


    private String getNameOfVehicle() {
        if(this.vehicle instanceof Car)
            return "Samochód";
        if(this.vehicle instanceof Bike)
            return "Rower";
        return "Motocykl";
    }

}
