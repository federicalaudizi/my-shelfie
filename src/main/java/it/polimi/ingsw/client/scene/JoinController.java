package it.polimi.ingsw.client.scene;

import it.polimi.ingsw.client.ViewGUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


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

    public void displayError(String message) {

        Stage popupStage = new Stage();
        popupStage.initStyle(StageStyle.UNDECORATED);
        Text text = new Text(message);
        Button tryAgainButton = new Button("Try again");
        VBox.setMargin(tryAgainButton, new Insets(40, 0, 0, 182)); // Add margin to the button
        // Create the AnchorPane and add the content nodes
        VBox layout= new VBox(3);
        layout.getChildren().addAll(text, tryAgainButton);

        TitledPane errorPopup = new TitledPane();
        errorPopup.setAnimated(false);
        errorPopup.setLayoutX(197);
        errorPopup.setLayoutY(61);
        errorPopup.setPrefHeight(130);
        errorPopup.setPrefWidth(280);
        errorPopup.setText("Error");
        errorPopup.setContent(layout);

        popupStage.setResizable(false);
        tryAgainButton.setOnAction(event -> popupStage.hide());

        // Set the TitledPane as the content of the popup Stage
        StackPane container = new StackPane(errorPopup);
        Scene popupScene = new Scene(container);
        popupStage.setScene(popupScene);

        // Show the popup Stage
        popupStage.showAndWait();
    }
}
