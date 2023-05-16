package it.polimi.ingsw.client.scene;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.scene.control.Button;

public class ConnectionController {

    private String ip;

    @FXML
    public Text genericError;
    @FXML
    private TextField ipAddress;
    @FXML
    private Button ok;

    /*
     * if(startGame) ==> changes scene asking nickname
     * */


    public String handleMouseClickForIp() {
        ok.setOnAction(event -> {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Username.fxml"));
            Parent thirdViewParent;
            try {
                thirdViewParent = loader.load();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Scene thirdViewScene = new Scene(thirdViewParent);
            Stage currentStage = (javafx.stage.Stage) ok.getScene().getWindow();
            currentStage.setScene(thirdViewScene);
            System.out.println(ipAddress.getText());
        });
        return ipAddress.getText();
    }



    /*@FXML
    public String handleMouseClickForIp() {
        EventHandler<MouseEvent> eventHandler = new EventHandler<>() {
            @Override
            public void handle(MouseEvent e) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Username.fxml"));
                Parent thirdViewParent;
                try {
                    thirdViewParent = loader.load();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Scene thirdViewScene = new Scene(thirdViewParent);
                Stage currentStage = (javafx.stage.Stage) ok.getScene().getWindow();
                currentStage.setScene(thirdViewScene);
            }
        };

        ok.addEventHandler(MouseEvent.MOUSE_PRESSED, eventHandler);
        System.out.println(ipAddress.getText());
        return ipAddress.getText();
    }**/
}

