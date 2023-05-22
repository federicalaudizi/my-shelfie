package it.polimi.ingsw.client.scene;

import it.polimi.ingsw.server.model.Game;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image ;

public class BoardController {

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

    public void initializeBoard(Game game){
        for(int i=0; i< game.getBoard().getMAX_X(); i++){
            for(int j=0;j<game.getBoard().getMAX_Y();j++){
                Image image = new Image(game.getBoard().getTile(i,j).getPath());
                boardPane.getChildren().add(new ImageView(image));
            }
        }
    }
}





