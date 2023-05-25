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

    public void displayError() {
        /*Popup pp = new Popup();

        Label label1= new Label("You entered a malformed IP:port combo. \n                                    Retry.");
        label1.setMinWidth(100);
        label1.setMinHeight(150);
        label1.setStyle("-fx-text-fill: RED; -fx-font-weight: bold; -fx-alignment: center; -fx-font-family: Marker Felt");

        Button button1= new Button("Try again");
        button1.setOnAction(e -> pp.hide());
        VBox layout= new VBox(3);
        layout.setStyle("-fx-background-image: url(Images/base_pagina2.jpg); -fx-border-width: 1px; -fx-border-color: #25171c; -fx-background-radius: 2px"); // Set the background color and transparency
        layout.setPadding(new Insets(10, 20, 10, 20));


        layout.getChildren().addAll(label1, button1);

        layout.setAlignment(Pos.CENTER);
        pp.getContent().addAll(layout);
        pp.show(Gui.getStage(), Gui.getStage().getX() +170.00, Gui.getStage().getY() + 150.00);*/

        Stage popupStage = new Stage();
        //popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UNDECORATED);
        Text text = new Text("You entered a malformed ip:port combo. Retry.");
        Button tryAgainButton = new Button("Try again");
        VBox.setMargin(tryAgainButton, new Insets(40, 0, 0, 182)); // Add margin to the button
        // Create the AnchorPane and add the content nodes
        VBox layout= new VBox(3);
        layout.getChildren().addAll(text, tryAgainButton);

        // Create the TitledPane and set its properties
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
            // Perform the action here
            popupStage.hide();
            // You can add your desired code to handle the button click event
        });

        // Set the TitledPane as the content of the popup Stage
        StackPane container = new StackPane(errorPopup);
        Scene popupScene = new Scene(container);
        popupStage.setScene(popupScene);

        // Show the popup Stage
        popupStage.showAndWait();
    }

}

