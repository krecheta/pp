package model;

public class Employee {
    private final int UUID;
    private final String firstName;
    private final String lastName;
    private final String address;
    private final int phoneNumber;
    private final String email;
    private final String login;

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public Employee(int UUID, String firstName, String lastName, String address, int phoneNumber, String email, String login) {
        this.UUID = UUID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.login = login;
    }

    public int getUUID() {return this.UUID; }
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
    public String getAddress() { return this.address; }
    public int getPhoneNumber() { return this.phoneNumber;}
    public String getEmail() { return this.email; }
    public String getLogin() { return login; }
}
