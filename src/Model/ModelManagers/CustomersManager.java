package Model.ModelManagers;

import DataBase.DatabaseManager;
import Model.CustomExceptions.ErrorMessageException;
import Model.Customer;
import java.util.List;

public class CustomersManager {

    public void addCustomer(String firstName, String lastName, String peselNumber, String address, int phoneNumber, String email, String companyName,
                             String nipNumber, String companyAddress) throws ErrorMessageException {

        DatabaseManager.addCustomer(firstName, lastName, peselNumber, address, phoneNumber, email, companyName,
                                    nipNumber, companyAddress);
    }

    public void editCustomer(Customer customer) throws ErrorMessageException{
        Customer customerBeforUpdate = getCustomerByPesel(customer.getPeselNumber());
        if (customer == null)
            throw new ErrorMessageException("Client doesn't exist");

        try{
            DatabaseManager.editCustomer(customer.getFirstName(), customer.getLastName(), customer.getPeselNumber(), customer.getAddress(), customer.getPhoneNumber(),
                                                      customer.getEmail(), customer.getCompanyName(), customer.getNipNumber(), customer.getCompanyAddress());
        }catch (Exception e){
            DatabaseManager.editCustomer(
                    customerBeforUpdate.getFirstName(), customerBeforUpdate.getLastName(), customerBeforUpdate.getPeselNumber(), customerBeforUpdate.getAddress(), customerBeforUpdate.getPhoneNumber(),
                    customerBeforUpdate.getEmail(), customerBeforUpdate.getCompanyName(), customerBeforUpdate.getNipNumber(), customerBeforUpdate.getCompanyAddress());
            throw e;
        }
    }

    public List<Customer> getAllCustomers() throws ErrorMessageException {
        List<Customer> customers = DatabaseManager.getAllActualCustomers();
        customers.addAll(DatabaseManager.getAllArchivalCustomers());
        return customers;
    }

    // This return only acitve customers not archived
    public List<Customer> getActiveCustomers() throws ErrorMessageException {
        return DatabaseManager.getAllActualCustomers();
    }

    public  List<Customer> getAllArchivalCustomers() throws ErrorMessageException{
        return DatabaseManager.getAllArchivalCustomers();
    }

    public  Customer getCustomerByPesel(String peselNumber) throws ErrorMessageException {
        return DatabaseManager.getCustomerByPesel(peselNumber);
    }

    // Not used for now
    public void transferCustomerToArchivalCustomers(String peselNumber) throws ErrorMessageException {
        Customer client = getCustomerByPesel(peselNumber);
        if (client == null)
            throw new ErrorMessageException("Client doesn't exist");

        DatabaseManager.markCustomerAsArchival(peselNumber);
    }

//    public List<Customer> getFilteredCustomers(String peselNumber, String firstName, String lastName) throws ErrorMessageException{
//       return DatabaseManager.getFilteredCustomers(peselNumber, firstName, lastName);
//    }
}
