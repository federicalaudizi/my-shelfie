package it.polimi.ingsw.client.scene;

import it.polimi.ingsw.client.ViewGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class GameOptionsController {
    @FXML
    public ChoiceBox<String> choiceSelector;
    @FXML
    public Button button;

    /**
     * The player can decide if create a new game, join an existing game or to reconnect to an old one
     * This method lets the player chose and sends the choice to the manager thread as a number
     * */
    public void chooseOption() {
        String selectedOption = choiceSelector.getValue();
        button.setVisible(true);

        button.setOnAction(event -> {

            switch (selectedOption) {
                case "Create a new game" -> {
                    try {
                        //give nick to manager thread
                        ViewGUI.queue.put(1);

                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                case "Join an existing game" -> {
                    try {
                        //give nick to manager thread
                        ViewGUI.queue.put(2);

                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                case "Reconnect to an ongoing game" -> {
                    try {
                        //give nick to manager thread
                        ViewGUI.queue.put(3);

                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
}
