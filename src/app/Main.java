package app;
import com.sun.javafx.css.StyleManager;
import database.DatabaseManager;
import model.exceptions.ErrorMessageException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Logs;

import java.io.File;
import java.util.logging.Logger;

public class Main extends Application {

    protected final Logger log = Logger.getLogger(getClass().getName()); //java.util.logging.Logger

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/MainView.fxml"));
        primaryStage.setTitle("Wypożyczalnia pojazdów");
        Scene scene = new Scene(root);

        scene.getStylesheets().add(this.getClass().getResource("../view/css/modena_dark.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) throws ErrorMessageException {
        new Logs();
        DatabaseManager.connect();
        launch(args);
    }
}
