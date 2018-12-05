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
    private int sumPaidForAllRents;

    /**
     *
     * @param pesel this is KEY in database
     * @param firstName
     * @param lastName
     * @param age
     * @param phoneNumber
     * @param address
     * @param sumPaidForAllRents Sum for all rents that client paid
     * @throws UnknownClientTypeException
     * @throws WrongPeselException
     */
    public Client(String pesel, String firstName, String lastName, int age, int phoneNumber, String address, int sumPaidForAllRents) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.pesel = pesel;
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

    public int getSumPaidForAllRents() {
        return sumPaidForAllRents;
    }

    @Override
    public String toString() {
        return   "{pesel = " + pesel +
                ", imię = " + firstName +
                ", nazwisko = " + lastName +
                ", wiek = " + age +
                ", numer telefonu = " + phoneNumber +
                ", adres = " + address +
                ", łączna zapłacona kwota za wypożyczenia = " + sumPaidForAllRents +
                '}';
    }
}
