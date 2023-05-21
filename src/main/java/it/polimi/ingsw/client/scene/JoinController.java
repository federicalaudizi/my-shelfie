package it.polimi.ingsw.client.scene;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class JoinController implements Initializable {

    @FXML
    public ListView<String> listView;

    public void addGameIds(ArrayList<String> gameIds) {
        // Sample data
        listView.getItems().addAll(gameIds);
    }

    @FXML
    public void handleItemClick() {
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        System.out.println("Selected item: " + selectedItem);
    }

    /**
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleItemClick());

    }
}
