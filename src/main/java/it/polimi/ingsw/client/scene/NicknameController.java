package it.polimi.ingsw.client.scene;
import it.polimi.ingsw.client.Client;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

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

    public String getNickname(){
        nicknameText.addEventHandler(MouseEvent.MOUSE_PRESSED, this::handleMouseClickForNickname);
        return nicknameText.getText();
    }

    @FXML
    private void handleMouseClickForNickname(Event event) {
        EventHandler<MouseEvent> nicknameHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Nickname.fxml"));
                Parent secondViewParent;
                try {
                    secondViewParent = loader.load();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Scene secondViewScene = new Scene(secondViewParent);
                Stage currentStage = (javafx.stage.Stage) nicknameText.getScene().getWindow();
                currentStage.setScene(secondViewScene);
            }
        };
        nicknameText.addEventHandler(MouseEvent.MOUSE_PRESSED, nicknameHandler);

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
