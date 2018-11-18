package DataBase;

import Model.Client;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.AfterEach;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class DataBaseTest {
    static DataBase database;

    @org.junit.jupiter.api.BeforeEach
    void setupDatabase(){
        File file = new File("biblioteka.db");
        if(file.delete())
            System.out.println("File deleted");
        else
            System.out.println("Can't find file");

        database = new DataBase();
    }

    @org.junit.jupiter.api.Test
    void addOneClient() {

        database.addClient("actual", "95062910555", "ajsdli", "oiasjdoij", 15, 669669669, "Adress");
        List<Client> clients = database.getAllClients();
        String Response = "Client{pesel='95062910555', firstName='ajsdli', lastName='oiasjdoij', age=15, phoneNumber=669669669, address='Adress', type=0, sumPaidForAllRents=0}";

        assertEquals(1, clients.size());
        assertEquals(Response, clients.get(0).toString());
    }


    @org.junit.jupiter.api.Test
    void addThreeClients() {
        database.addClient("actual", "95062910555", "Dominik", "X", 15, 669669669, "Adress0");
        database.addClient("actual", "95062910553", "Lukasz", "Y", 16, 669669660, "Adress1");
        database.addClient("actual", "95062910552", "Grzegorz", "Z", 17, 669669661, "Adress2");

        List<Client> clients = database.getAllClients();
        String Response0 = "Client{pesel='95062910555', firstName='Dominik', lastName='X', age=15, phoneNumber=669669669, address='Adress0', type=0, sumPaidForAllRents=0}";
        String Response1 = "Client{pesel='95062910553', firstName='Lukasz', lastName='Y', age=16, phoneNumber=669669660, address='Adress1', type=0, sumPaidForAllRents=0}";
        String Response2 = "Client{pesel='95062910552', firstName='Grzegorz', lastName='Z', age=17, phoneNumber=669669661, address='Adress2', type=0, sumPaidForAllRents=0}";

        assertEquals(3, clients.size());
        assertEquals(Response0, clients.get(0).toString());
        assertEquals(Response1, clients.get(1).toString());
        assertEquals(Response2, clients.get(2).toString());
    }

    @AfterEach
    void closeDatabaseConnection() {
        database.closeConnection();
    }
}