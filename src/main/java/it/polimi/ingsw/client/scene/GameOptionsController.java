package it.polimi.ingsw.client.scene;

import it.polimi.ingsw.client.ViewGUI;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class GameOptionsController {
    @FXML
    public ChoiceBox choiceSelector;

    public void chooseOption(){
        choiceSelector.setOnAction(event -> {
            String selectedOption = (String) choiceSelector.getValue();

            if (selectedOption.equals("Create a new game")) {
                try {
                    //give nick to manager thread
                    ViewGUI.queue.put(1);

                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } else if (selectedOption.equals("Join a new game")) {
                try {
                    //give nick to manager thread
                    ViewGUI.queue.put(2);

                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } else if (selectedOption.equals("Reconnect to an ongoing game")) {
                try {
                    //give nick to manager thread
                    ViewGUI.queue.put(3);

                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }
}
