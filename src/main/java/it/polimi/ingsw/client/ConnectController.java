package it.polimi.ingsw.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ConnectController {

    @FXML
    TextField ipAddress;
    @FXML
    Button ok;


    public ConnectController() {
    }

    @FXML
    public String connect(){
        return ipAddress.getText();
    }

    @FXML
    private void handleMouseClick(){
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Username.fxml"));
        Parent secondViewParent =loader.load();
        Scene secondViewScene = new Scene(secondViewParent);

        // Get the Stage object from the current view
        Stage currentStage = (javafx.stage.Stage) ok.getScene().getWindow();

        // Set the new scene on the stage
        currentStage.setScene(secondViewScene);
    } catch (
    IOException e) {
        throw new RuntimeException(e);
    }
    }
}
