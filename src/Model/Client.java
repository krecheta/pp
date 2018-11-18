package Model;

import Model.CustomExceptions.UnknownClientTypeException;
import Model.CustomExceptions.WrongPeselException;

public class Client {
    private String pesel;
    private String firstName;
    private String lastName;
    private int age;
    private int phoneNumber;
    private String address;
    private  int type;
    private int sumPaidForAllRents;

    /**
     *
     * @param pesel this is KEY in database
     * @param firstName
     * @param lastName
     * @param age
     * @param phoneNumber
     * @param address
     * @param type client can have types from 0 to 5, if it is bigger that gives him bigger discounts
     * @param sumPaidForAllRents Sum for all rents that client paid
     * @throws UnknownClientTypeException
     * @throws WrongPeselException
     */
    public Client(String pesel, String firstName, String lastName, int age, int phoneNumber, String address, int type, int sumPaidForAllRents) throws UnknownClientTypeException, WrongPeselException {
        if (type <0 || type >5)
            throw new UnknownClientTypeException();
        if (pesel.length() != 11)
            throw new WrongPeselException();
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.pesel = pesel;
        this.type = type;
        this.sumPaidForAllRents = sumPaidForAllRents;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPesel() {
        return pesel;
    }

    public int getType() {
        return type;
    }

    public int getSumPaidForAllRents() {
        return sumPaidForAllRents;
    }

    @Override
    public String toString() {
        return "Client{" +
                "pesel='" + pesel + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", phoneNumber=" + phoneNumber +
                ", address='" + address + '\'' +
                ", type=" + type +
                ", sumPaidForAllRents=" + sumPaidForAllRents +
                '}';
    }
}
