package it.polimi.ingsw.client.scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import it.polimi.ingsw.client.ViewGUI;


import java.util.ArrayList;

public class JoinController {

    @FXML
    public static ListView<String> listView;

    public static void addGameIds(ArrayList<String> gameIds) {
        // Sample data
        listView.getItems().addAll(gameIds);
    }

    @FXML
    public void handleItemClick() {
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        System.out.println("Selected item: " + selectedItem);
    }
}
