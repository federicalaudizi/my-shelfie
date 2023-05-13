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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Welcome.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setTitle("MyShelfie");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    @FXML
    private void handleStartGameButtonClick() {
        try {
            // Load the second view FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Username.fxml"));
            Parent secondViewParent =loader.load();
            Scene secondViewScene = new Scene(secondViewParent);

            // Get the Stage object from the current view
            javafx.stage.Stage currentStage = (javafx.stage.Stage) mainPane.getScene().getWindow();

            // Set the new scene on the stage
            currentStage.setScene(secondViewScene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Launches the GUI
    public static void main(String[] args) {
        launch();
    }
}
