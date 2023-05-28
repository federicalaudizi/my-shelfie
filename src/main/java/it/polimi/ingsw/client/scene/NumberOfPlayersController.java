package it.polimi.ingsw.client.scene;

import it.polimi.ingsw.client.ViewGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;

public class NumberOfPlayersController {
    @FXML
    public ChoiceBox<Integer> choiceSelector;
    @FXML
    public Button button;
    @FXML
    public ProgressIndicator progress;
    @FXML
    public Text text;

    /**
     * Manages the number of player selection
     * */
    public void chooseOption(){
        Integer selectedOption = choiceSelector.getValue();
        button.setVisible(true);

        button.setOnAction(event -> {

            switch (selectedOption) {

                case 2 -> {
                    progress.setVisible(true);
                    text.setVisible(true);
                    try {
                        ViewGUI.queue.put(2);

                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                case 3 -> {
                    progress.setVisible(true);
                    text.setVisible(true);
                    try {
                        ViewGUI.queue.put(3);

                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                case 4 -> {
                    progress.setVisible(true);
                    text.setVisible(true);
                    try {
                        ViewGUI.queue.put(4);

                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
}
