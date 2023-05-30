package it.polimi.ingsw.client.scene;

import it.polimi.ingsw.client.Gui;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import java.io.IOException;

public class CommonObjController {

    @FXML
    public Button continueGame;

    public void displayAchievement() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/commonObj.fxml"));
        AnchorPane root2 = loader.load();
        Scene scene2 = new Scene(root2);
        scene2.setFill(Color.TRANSPARENT);

        Gui.getStage().getScene().getRoot().setEffect(new GaussianBlur());
        Platform.runLater(() -> Gui.getStage().setScene(scene2));
    }

}
