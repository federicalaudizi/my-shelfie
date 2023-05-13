package it.polimi.ingsw.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.stage.Stage;


import java.io.IOException;

import static it.polimi.ingsw.client.Gui.stage;

public class WelcomeController {
    @FXML
    private Button mainPane;

    public WelcomeController() {
    }

    @FXML
    private void handleStartGameButtonClick() {
        try {
            // Load the second view FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Username.fxml"));
            Parent secondViewParent =loader.load();
            Scene secondViewScene = new Scene(secondViewParent);

            // Get the Stage object from the current view
            Stage currentStage = (javafx.stage.Stage) mainPane.getScene().getWindow();

            // Set the new scene on the stage
            currentStage.setScene(secondViewScene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
