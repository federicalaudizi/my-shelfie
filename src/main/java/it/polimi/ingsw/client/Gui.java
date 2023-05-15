package it.polimi.ingsw.client;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class Gui extends Application {


    static Stage stage;

    /* Code for JavaFX application. (Stage, scene, scene graph) */
    @Override
    public void start(Stage stage) throws Exception {
        Gui.stage = stage;

        // Load the FXML file with the FXMLLoader
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Welcome.fxml"));
        Parent root = fxmlLoader.load();

        // Construct the Scene using the loaded Parent and set it on the Stage
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    //Launches the GUI
    public static void main(String[] args) {
        launch();
    }
}
