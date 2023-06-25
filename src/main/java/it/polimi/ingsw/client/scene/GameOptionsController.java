package it.polimi.ingsw.client.scene;

import it.polimi.ingsw.client.ViewGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class GameOptionsController {

    @FXML
    public Button button;
    public ImageView button3;


    /**
     * Method to handle creation of the game.
     * Sends the code 1 for the creation of the game to the manager thread
     */
    public void handleCreate() {
        try {
            ViewGUI.queue.put(1);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method to handle joining of a game.
     * Sends the code 2 to receive the list of disposable games to the manager thread
     */
    public void handleJoin() {
        try {
            ViewGUI.queue.put(2);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method to handle reconnection.
     * Sends the code 3 to reconnect on a game to the manager thread
     */
    public void handleReconnect() {
        try {
            ViewGUI.queue.put(3);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

}
