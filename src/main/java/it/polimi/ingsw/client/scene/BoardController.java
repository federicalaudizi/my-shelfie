package it.polimi.ingsw.client.scene;

import it.polimi.ingsw.server.model.Game;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image ;

public class BoardController{
    @FXML
    private ImageView b03;

    @FXML
    private ImageView b04;

    @FXML
    private ImageView b13;

    @FXML
    private ImageView b14;

    @FXML
    private ImageView b15;

    @FXML
    private ImageView b22;

    @FXML
    private ImageView b23;

    @FXML
    private ImageView b24;

    @FXML
    private ImageView b25;

    @FXML
    private ImageView b26;

    @FXML
    private ImageView b31;

    @FXML
    private ImageView b32;

    @FXML
    private ImageView b33;

    @FXML
    private ImageView b34;

    @FXML
    private ImageView b35;

    @FXML
    private ImageView b36;

    @FXML
    private ImageView b37;

    @FXML
    private ImageView b38;

    @FXML
    private ImageView b40;

    @FXML
    private ImageView b41;

    @FXML
    private ImageView b42;

    @FXML
    private ImageView b43;

    @FXML
    private ImageView b44;

    @FXML
    private ImageView b45;

    @FXML
    private ImageView b46;

    @FXML
    private ImageView b47;

    @FXML
    private ImageView b48;

    @FXML
    private ImageView b50;

    @FXML
    private ImageView b51;

    @FXML
    private ImageView b52;

    @FXML
    private ImageView b53;

    @FXML
    private ImageView b54;

    @FXML
    private ImageView b55;

    @FXML
    private ImageView b56;

    @FXML
    private ImageView b57;

    @FXML
    private ImageView b62;

    @FXML
    private ImageView b63;

    @FXML
    private ImageView b64;

    @FXML
    private ImageView b65;

    @FXML
    private ImageView b66;

    @FXML
    private ImageView b73;

    @FXML
    private ImageView b74;

    @FXML
    private ImageView b75;

    @FXML
    private ImageView b84;

    @FXML
    private ImageView b85;

    @FXML
    private GridPane boardPane;

    @FXML
    private ImageView personalCard;

    @FXML
    private GridPane shelfGrid;

    @FXML
    private GridPane shelfGrid2;

    @FXML
    private GridPane shelfGrid3;

    @FXML
    private GridPane shelfGrid4;
    @FXML
    public ImageView commonCard1;
    @FXML
    public ImageView commonCard2;


    public void initializeBoard(Game game){
        for(int i=0; i< game.getBoard().getMAX_X(); i++){
            for(int j=0;j<game.getBoard().getMAX_Y();j++){
               Image image;
               if(game.getBoard().getTile(i,j).getPath()==null){
                   continue;
               }
               image = new Image(game.getBoard().getTile(i,j).getPath());
               getImageViewForPosition(i,j).setImage(image);
            }
        }
    }

    private ImageView getImageViewForPosition(int row, int col) {
        ObservableList<Node> children = boardPane.getChildren();

        for (Node node : children) {
            Integer rowIndex = GridPane.getRowIndex(node);
            Integer colIndex = GridPane.getColumnIndex(node);

            if (rowIndex != null && colIndex != null && rowIndex == row && colIndex == col && node instanceof ImageView) {
                return (ImageView) node;
            }
        }

        throw new IllegalStateException("ImageView not found for position: (" + row + ", " + col + ")");
    }


}





