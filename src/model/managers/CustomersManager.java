package model.managers;

import database.DatabaseManager;
import model.exceptions.ErrorMessageException;
import model.Customer;
import java.util.List;

public class CustomersManager {

    public void addCustomer(String firstName, String lastName, String peselNumber, String address, int phoneNumber, String email, String companyName,
                             String nipNumber, String companyAddress) throws ErrorMessageException {
        if(DatabaseManager.getCustomerByPesel(peselNumber).getPeselNumber() == null)
            DatabaseManager.addCustomer(firstName, lastName, peselNumber, address, phoneNumber, email, companyName,
                                    nipNumber, companyAddress);
         else
             throw new ErrorMessageException("Klient o podanym numerze pesel już istnieje.");
    }

    public void editCustomer(Customer customer) throws ErrorMessageException{
        Customer customerBeforUpdate = getCustomerByPesel(customer.getPeselNumber());
        if (customer == null)
            throw new ErrorMessageException("Klient nie istnieje");

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
            throw new ErrorMessageException("Klient nie istnieje");

        DatabaseManager.markCustomerAsArchival(peselNumber);
    }

    public List<Customer> getFilteredCustomers(String peselNumber, String firstName, String lastName) throws ErrorMessageException{
       return DatabaseManager.getFilteredCustomers(peselNumber, firstName, lastName);
    }
}
