package it.polimi.ingsw.client.scene;

import it.polimi.ingsw.client.ViewGUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class JoinController implements Initializable {

    @FXML
    public ListView<String> listView;
    public Button button;
    public ProgressIndicator progress;

    /***
     * ListView initializer
     * @param gameIds list of the existing games
     */
    public void addGameIds(ArrayList<String> gameIds) {
        // Sample data
        listView.getItems().addAll(gameIds);
    }

    /**
     * When the player choses the game to join it sends it to the manager thread
     * */
    @FXML
    public void handleItemClick() {
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        button.setVisible(true);
        button.setOnAction(event -> {
            try {
                ViewGUI.queue.put(selectedItem);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            progress.setVisible(true);
        });
    }

    /**
     * Manages the selection of the GameID
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleItemClick());

    }
}
