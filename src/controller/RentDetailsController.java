package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.PerspectiveCamera;
import javafx.scene.control.Label;
import model.Rent;

import java.net.URL;
import java.util.ResourceBundle;

public class RentDetailsController implements Initializable {

    @FXML
    private Label rentDateLabel;

    @FXML
    private Label returnDateLabel;

    @FXML
    private Label customerFirstNameLabel;

    @FXML
    private Label customerlastNameLabel;

    @FXML
    private Label PESELLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label addressLabel2;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label vehicleNameLabel;

    @FXML
    private Label vehicleTypeLabel;

    @FXML
    private Label colourLabel;

    @FXML
    private Label prodYearLabel;

    @FXML
    private Label mileageLabel;

    @FXML
    private Label engCapacityLabel;

    @FXML
    private Label fuelTypeLabel;

    @FXML
    private Label fuealUsageLabel;

    @FXML
    private Label seatsNumberLabel;

    @FXML
    private Label companyNameLabel;

    @FXML
    private Label nipLabel;

    @FXML
    private Label companyAddress;

    @FXML
    private Label priceLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(Rent rent){
        rentDateLabel.setText(rent.getStartDate().toString());
        returnDateLabel.setText(rent.getEndDate().toString());
        customerFirstNameLabel.setText(rent.getCustomer().getFirstName());
        customerlastNameLabel.setText(rent.getCustomer().getLastName());
        PESELLabel.setText(rent.getCustomer().getPeselNumber());
        addressLabel.setText(rent.getCustomer().getAddress());
        phoneLabel.setText(String.valueOf(rent.getCustomer().getPhoneNumber()));
        vehicleNameLabel.setText(rent.getVehicle().getName());
        vehicleTypeLabel.setText(rent.getVehicle().getTypeProperty());
        //TODO dodać kolor
        prodYearLabel.setText(String.valueOf(rent.getVehicle().getProductionYear()));
        mileageLabel.setText(rent.getVehicle().getMileageProperty());
        engCapacityLabel.setText(rent.getVehicle().getEngineCapacityProperty());
        fuelTypeLabel.setText(rent.getVehicle().getFuelTypeProperty());
        fuealUsageLabel.setText(rent.getVehicle().getFuelUsageProperty());
        seatsNumberLabel.setText(rent.getVehicle().getNumOfPersonsProperty());
        priceLabel.setText(String.valueOf(rent.getTotalPrice())+" zł");

        String companyName = rent.getCustomer().getCompanyName();
        if(companyName.equals("")){
            companyName = "-";
        }
        companyNameLabel.setText(companyName);

        String companyAddr = rent.getCustomer().getCompanyAddress();
        if(companyAddr.equals("")){
            companyAddr = "-";
        }
        companyAddress.setText(companyAddr);

        String nip = rent.getCustomer().getNipNumber();
        if(nip.equals("")){
            nip = "-";
        }
        nipLabel.setText(nip);
    }
}
