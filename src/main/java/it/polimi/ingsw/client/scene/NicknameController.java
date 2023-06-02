package it.polimi.ingsw.client.scene;

import it.polimi.ingsw.client.ViewGUI;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class NicknameController {

    @FXML
    public Text genericError;
    @FXML
    private TextField nicknameText;
    @FXML
    private Text errorNick;
    @FXML
    private Text waitingOther;
    @FXML
    private Button okNick;

    public NicknameController() {
    }

    /**
     * Handles the mouse click event for the nickname.
     * Retrieves the nickname entered in the nicknameText field and puts it into the queue for further processing.
     */
    @FXML
    private void handleMouseClickForNickname() {
        String nickname = nicknameText.getCharacters().toString();

        try {
            //give nick to manager thread
            ViewGUI.queue.put(nickname);

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Displays an error popup with the given message.
     * The popup contains a text message and a "Try again" button.
     * Clicking the "Try again" button hides the popup.
     *
     * @param message The error message to be displayed.
     */
    public void displayErrorNick(String message) {

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
    public void displayWaitingOther() {
        waitingOther.setVisible(true);
    }
    public void removeWaitingOtherFromScreen() {
        waitingOther.setVisible(false);
    }
}
