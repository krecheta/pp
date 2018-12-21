package Model;

public class Employee {
    private final int UUID;
    private final String firstName;
    private final String lastName;
    private final String address;
    private final int phoneNumber;
    private final String email;
    //login haslo



    @Override
    public String toString() {
        return   "{UUID = " + this.UUID +
                ", imię = " + this.firstName +
                ", nazwisko = " + this.lastName +
                ", numer telefonu = " + this.phoneNumber +
                ", adres = " + this.address +
                ", email = " + this.email +
                '}';
    }

    public Employee(int UUID, String firstName, String lastName, String address, int phoneNumber, String email) {
        this.UUID = UUID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public int getUUID() {return this.UUID; }
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
    public String getAddress() { return this.address; }
    public int getPhoneNumber() { return this.phoneNumber;}
    public String getEmail() { return this.email; }
}
