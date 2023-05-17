package it.polimi.ingsw.client.scene;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;


public class WelcomeController {
    @FXML
    public Text genericError;
    @FXML
    private Button mainPane;

    public WelcomeController() {
    }

    /*
     * if(startGame) ==> changes scene asking ip
     * */
    @FXML
    private void handleStartGameButtonClick() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Connection.fxml"));
        Parent secondViewParent;
        try {
            secondViewParent = loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        Scene secondViewScene = new Scene(secondViewParent);
        Stage currentStage = (javafx.stage.Stage) mainPane.getScene().getWindow();
        currentStage.setScene(secondViewScene);
    }



    //public void displayGenericError(){genericError.setVisible(true);}
    //public void removeGenericError(){genericError.setVisible(false);}

}
