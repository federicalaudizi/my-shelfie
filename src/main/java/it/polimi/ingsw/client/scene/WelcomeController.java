package it.polimi.ingsw.client.scene;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.io.IOException;


public class WelcomeController {
    @FXML
    private Button mainPane;
    @FXML
    TextField ipAddress;
    @FXML
    Button ok;

    public WelcomeController() {
    }

    /*
    * if(startGame) ==> changes scene asking ip and nickname
    * */

    @FXML
    private void handleStartGameButtonClick() {
        try {
            // Load the second view FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Connection.fxml"));
            Parent secondViewParent =loader.load();
            Scene secondViewScene = new Scene(secondViewParent);

            // Get the Stage object from the current view
            mainPane.setOnAction(event -> {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.setScene(secondViewScene);

            });

            // Set the new scene on the stage
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String connect(){
        ipAddress.addEventHandler(MouseEvent.MOUSE_PRESSED, this::handleMouseClick);
        return ipAddress.getText();
    }
    @FXML
    private void handleMouseClick(Event event){
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

    //public void displayGenericError(){genericError.setVisible(true);}
    //public void removeGenericError(){genericError.setVisible(false);}

}
