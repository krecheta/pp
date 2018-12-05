package sample;
import Model.CustomExceptions.ErrorMessageException;
import Model.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Model.Logs;

import java.util.List;
import java.util.logging.Logger;

public class Main extends Application {

    protected final Logger log = Logger.getLogger(getClass().getName()); //java.util.logging.Logger

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) throws ErrorMessageException {
        new Logs();
        Model model = new Model();
        launch(args);

    }
}
