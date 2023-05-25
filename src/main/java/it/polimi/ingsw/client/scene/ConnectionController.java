package it.polimi.ingsw.client.scene;

import it.polimi.ingsw.client.Gui;
import it.polimi.ingsw.client.ViewGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;



public class ConnectionController {

    @FXML
    public AnchorPane wrongIp;
    @FXML
    public TitledPane errorPopup;
    @FXML
    private TextField ipAddress;
    @FXML
    private Button ok;

    /*
     * if(startGame) ==> changes scene asking nickname
     * */
    @FXML
    public void handleMouseClickForIp() {
        String IPAddress = ipAddress.getCharacters().toString();
        //next();
        try {
            //give ip address to manager thread
            ViewGUI.queue.put(IPAddress);

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void next(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Username.fxml"));
        Parent thirdViewParent;
        try {
            thirdViewParent = loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        Scene thirdViewScene = new Scene(thirdViewParent);
        Stage currentStage = (Stage) ok.getScene().getWindow();
        currentStage.setScene(thirdViewScene);
    }

    public void displayError(String errorMessage) {
        Stage popupStage = new Stage();
        popupStage.initStyle(StageStyle.UNDECORATED);
        Text text = new Text(errorMessage);
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
        errorPopup.setPrefWidth(213);
        errorPopup.setText("Error");
        errorPopup.setContent(layout);

        popupStage.setResizable(false);
        tryAgainButton.setOnAction(event -> {
            popupStage.hide();
        });

        // Set the TitledPane as the content of the popup Stage
        StackPane container = new StackPane(errorPopup);
        Scene popupScene = new Scene(container);
        popupStage.setScene(popupScene);

        // Show the popup Stage
        popupStage.showAndWait();
    }
}

