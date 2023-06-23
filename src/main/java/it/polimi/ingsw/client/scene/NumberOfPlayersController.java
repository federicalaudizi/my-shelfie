package it.polimi.ingsw.client.scene;

import it.polimi.ingsw.client.ViewGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;

public class NumberOfPlayersController {
    @FXML
    public ProgressIndicator progress;
    @FXML
    public Text text;
    @FXML
    public Button threePlayers;
    @FXML
    public Button fourPlayers;
    @FXML
    public Button twoPlayers;

    /**
     * Manages the number of player selection
     * */
    public void chooseOption(){
        twoPlayers.setOnAction(event -> {
            progress.setVisible(true);
            text.setVisible(true);
            try {
                ViewGUI.queue.put(2);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });

        threePlayers.setOnAction(event ->{
            progress.setVisible(true);
            text.setVisible(true);
            try {
                ViewGUI.queue.put(3);

            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });

        fourPlayers.setOnAction(event ->{
            progress.setVisible(true);
            text.setVisible(true);
            try {
                ViewGUI.queue.put(4);

            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
    }
}
