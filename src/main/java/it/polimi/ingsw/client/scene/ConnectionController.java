package it.polimi.ingsw.client.scene;

import it.polimi.ingsw.client.ViewGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;



public class ConnectionController {

    @FXML
    public Text genericError;
    @FXML
    private TextField ipAddress;
    @FXML
    private Button ok;


    /*
     * if(startGame) ==> changes scene asking nickname
     * */
    @FXML
    public void handleMouseClickForIp() {
        String IPAddress = ipAddress.getCharacters().toString();
        next();
        try {
            //give ip address to manager thread
            ViewGUI.queue.put(IPAddress);

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void next(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Username.fxml"));
        Parent thirdViewParent;
        try {
            thirdViewParent = loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        Scene thirdViewScene = new Scene(thirdViewParent);
        Stage currentStage = (Stage) ok.getScene().getWindow();
        currentStage.setScene(thirdViewScene);
    }

}

