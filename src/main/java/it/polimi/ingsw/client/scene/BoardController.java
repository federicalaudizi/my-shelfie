package it.polimi.ingsw.client.scene;

import it.polimi.ingsw.server.model.Game;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image ;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardController{

    @FXML
    GridPane boardPane;
    @FXML
    GridPane shelf1;
    @FXML
    GridPane shelf2;
    @FXML
    GridPane shelf3;
    @FXML
    GridPane shelf4;
    @FXML
    public
    ImageView commonCard1;
    @FXML
    public
    ImageView commonCard2;


    public void initializeBoard(Game game){
        for(int i=0; i< game.getBoard().getMAX_X(); i++){
            for(int j=0;j<game.getBoard().getMAX_Y();j++){
                Image image = new Image(game.getBoard().getTile(i,j).getPath());
                boardPane.getChildren().add(new ImageView(image));
            }
        }
    }


}





