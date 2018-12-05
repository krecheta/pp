package Model;

public class Employee {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final int phone_number;

    public Employee(int id, String firstName, String lastName, int phone_number) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone_number = phone_number;
    }

    @Override
    public String toString() {
        return "Imie = " + firstName +
                ", nazwisko = " + lastName +
                 ", numer telefonu = " + phone_number;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getPhone_number() {
        return phone_number;
    }

    public int getId() {
        return id;
    }
}
