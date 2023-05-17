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

    public void displayErrorNick() {
        errorNick.setVisible(true);
    }
    public void removeErrorNickFromScreen() {
        errorNick.setVisible(false);
    }
    public void displayWaitingOther() {
        waitingOther.setVisible(true);
    }
    public void removeWaitingOtherFromScreen() {
        waitingOther.setVisible(false);
    }
    public void displayGenericError(){genericError.setVisible(true);}
    public void removeGenericError(){genericError.setVisible(false);}
}
