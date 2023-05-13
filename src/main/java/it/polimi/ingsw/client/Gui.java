package it.polimi.ingsw.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Gui extends Application {

    private static Stage stage;

    /* Code for JavaFX application. (Stage, scene, scene graph) */
    @Override
    public void start(Stage stage) throws Exception {
        Gui.stage = stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Welcome.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setTitle("MyShelfie");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    //Launches the GUI
    public static void main(String[] args) {
        launch();
    }
}
