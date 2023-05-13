package it.polimi.ingsw.client;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Gui extends Application {

    @FXML
    private Button mainPane;
    static Stage stage;

    /* Code for JavaFX application. (Stage, scene, scene graph) */
    @Override
    public void start(Stage stage) throws Exception {
        Gui.stage = stage;

        // Load the FXML file with the FXMLLoader
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Welcome.fxml"));
        Parent root = fxmlLoader.load();

        // Retrieve the WelcomeController instance from the FXMLLoader
        WelcomeController welcomeController = fxmlLoader.getController();

        // Perform any necessary initialization or setup on the WelcomeController

        // Construct the Scene using the loaded Parent and set it on the Stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    //Launches the GUI
    public static void main(String[] args) {
        launch();
    }
}
