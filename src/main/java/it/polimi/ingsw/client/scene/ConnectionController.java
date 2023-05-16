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
    TextField ipAddress;
    @FXML
    Button ok;

    public void connect() {
        ok.addEventHandler(MouseEvent.MOUSE_PRESSED, this::handleMouseClickForIp);
    }

    /*
     * if(startGame) ==> changes scene asking nickname
     * */
    @FXML
    public void handleMouseClickForIp(Event e) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Nickname.fxml"));
        Parent secondViewParent=null;
        try {
            secondViewParent = loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        Scene secondViewScene = new Scene(secondViewParent);
        Stage currentStage = (javafx.stage.Stage) ok.getScene().getWindow();
        currentStage.setScene(secondViewScene);
        ip= ipAddress.getText();
    }

     public String getIp(){
        return ip;
    }
}
