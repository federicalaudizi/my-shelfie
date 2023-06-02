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
     * Handles the item click event in the ListView.
     * Retrieves the selected item from the ListView and shows the button.
     * When the button is clicked, the selected item is put into the queue for further processing.
     * The progress indicator is also made visible.
     */
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
     * Initializes the controller.
     * Sets up a listener on the ListView's selectedItemProperty to handle item selection events.
     * When an item is selected, the handleItemClick method is called.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleItemClick());

    }
}
