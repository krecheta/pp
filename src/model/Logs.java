package model;

import java.io.IOException;
import java.util.logging.*;


public  class Logs {
    public static final Logger logger = Logger.getLogger("Logs");

    /**
     *  This class is needed for log errors to file named 'LOGS'
     *  in constructor is setup of this logger file
     */
    public Logs(){
        FileHandler fileHandler;

        try {
            fileHandler = new FileHandler("LOGS.log");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
