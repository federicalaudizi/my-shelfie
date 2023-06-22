package it.polimi.ingsw.client.scene;

import it.polimi.ingsw.client.ViewGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;




public class ConnectionController {

    @FXML
    public AnchorPane wrongIp;
    @FXML
    public TitledPane errorPopup;
    @FXML
    private TextField ipAddress;
    @FXML
    private Button ok;

    /**
     * Handles the mouse click event for the IP address input field.
     * Retrieves the IP address entered by the user and adds it to the queue for further processing.
     *
     * @throws InterruptedException If the thread is interrupted while adding the IP address to the queue.
     */
    @FXML
    public void handleMouseClickForIp() {
        String IPAddress = ipAddress.getCharacters().toString();
        try {
            //give ip address to manager thread
            ViewGUI.queue.put(IPAddress);

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}

