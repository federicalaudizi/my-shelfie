package it.polimi.ingsw.client.scene;

import it.polimi.ingsw.client.ViewGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class NumberOfPlayersController {
    @FXML
    public ChoiceBox<Integer> choiceSelector;
    @FXML
    public Button button;

    public void chooseOption(){
        Integer selectedOption = choiceSelector.getValue();
        button.setVisible(true);

        button.setOnAction(event -> {

            switch (selectedOption) {
                case 2 -> {
                    try {
                        ViewGUI.queue.put(2);

                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                case 3 -> {
                    try {
                        //give nick to manager thread
                        ViewGUI.queue.put(3);

                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                case 4 -> {
                    try {
                        //give nick to manager thread
                        ViewGUI.queue.put(4);

                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
}
