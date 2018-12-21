package Model;

public class Customer {
    private final String firstName;
    private final String lastName;
    private final String peselNumber;
    private final String address;
    private final int phoneNumber;
    private final String email;
    private final String companyName;
    private final String nipNumber;
    private final String companyAddress;
    private final double sumPaidForAllRents;

    public Customer(String firstName, String lastName, String peselNumber, String address, int phoneNumber, String email, String companyName,
                    String nipNumber, String companyAddress, double sumPaidForAllRents) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.peselNumber = peselNumber;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.companyName = companyName;
        this.nipNumber = nipNumber;
        this.companyAddress = companyAddress;
        this.sumPaidForAllRents = sumPaidForAllRents;
    }

    @Override
    public String toString() {
        return   "{pesel = " + this.peselNumber +
                ", imię = " + this.firstName +
                ", nazwisko = " + this.lastName +
                ", numer telefonu = " +this. phoneNumber +
                ", adres = " + this.address +
                ", email = " + this.email +
                ", łączna zapłacona kwota za wypożyczenia = " + sumPaidForAllRents +
                '}';
    }

    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return this.lastName;
    }
    public int getPhoneNumber() {
        return this.phoneNumber;
    }
    public String getAddress() {
        return this.address;
    }
    public String getPeselNumber() { return this.peselNumber; }
    public double getSumPaidForAllRents() {
        return this.sumPaidForAllRents;
    }
    public String getEmail() { return this.email; }
    public String getCompanyName() { return this.companyName; }
    public String getNipNumber() { return this.nipNumber; }
    public String getCompanyAddress() { return this.companyAddress; }
}
