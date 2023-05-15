package it.polimi.ingsw.client.scene;
import it.polimi.ingsw.client.Client;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

//import static it.polimi.ingsw.client.View.queue;

public class NicknameController {

    @FXML
    public Text genericError;
    @FXML
    private TextField nicknameText;
    @FXML
    private Text errorNick;
    @FXML
    private Text waitingOther;

    public NicknameController() {
    }


    @FXML
    private void login(KeyEvent ke) throws InterruptedException {

        if (ke.getCode().equals(KeyCode.ENTER)) {
            String nickname = nicknameText.getCharacters().toString();
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
