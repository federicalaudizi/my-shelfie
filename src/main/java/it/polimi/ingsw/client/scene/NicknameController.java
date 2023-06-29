package it.polimi.ingsw.client.scene;

import it.polimi.ingsw.client.ViewGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


public class NicknameController {

    @FXML
    public Text genericError;
    @FXML
    private TextField nicknameText;
    @FXML
    private Text errorNick;
    @FXML
    private Text waitingOther;
    @FXML
    private Button okNick;

    public NicknameController() {
    }

    /**
     * Handles the mouse click event for the nickname.
     * Retrieves the nickname entered in the nicknameText field and puts it into the queue for further processing.
     */
    @FXML
    private void handleMouseClickForNickname() {
        String nickname = nicknameText.getCharacters().toString();

        try {
            //give nick to manager thread
            ViewGUI.queue.put(nickname);

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

    }

}
