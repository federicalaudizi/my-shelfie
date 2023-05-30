package it.polimi.ingsw.client.scene;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ViewGUI;
import it.polimi.ingsw.server.model.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image ;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BoardController{
    private boolean b03click = false;
    private boolean b13click = false;
    private boolean b04click = false;
    private boolean b14click = false;
    private boolean b15click = false;
    private boolean b22click = false;
    private boolean b23click = false;
    private boolean b24click = false;
    private boolean b25click = false;
    private boolean b26click = false;
    private boolean b31click = false;
    private boolean b32click = false;
    private boolean b33click = false;
    private boolean b34click = false;
    private boolean b35click = false;
    private boolean b36click = false;
    private boolean b37click = false;
    private boolean b38click = false;
    private boolean b40click = false;
    private boolean b41click = false;
    private boolean b42click = false;
    private boolean b43click = false;
    private boolean b44click = false;
    private boolean b45click = false;
    private boolean b46click = false;
    private boolean b47click = false;
    private boolean b48click = false;
    private boolean b50click = false;
    private boolean b51click = false;
    private boolean b52click = false;
    private boolean b53click = false;
    private boolean b54click = false;
    private boolean b55click = false;
    private boolean b56click = false;
    private boolean b57click = false;
    private boolean b62click = false;
    private boolean b63click = false;
    private boolean b64click = false;
    private boolean b65click = false;
    private boolean b66click = false;
    private boolean b73click = false;
    private boolean b74click = false;
    private boolean b75click = false;
    private boolean b84click = false;
    private boolean b85click = false;

    public Button continueButton;
    public Button column0;
    public Button column1;
    public Button column2;
    public Button column3;
    public Button column4;
    private List<String> tiles;
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
    public ImageView personalCard;

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
    @FXML
    private ImageView shelfImage3;
    @FXML
    private ImageView shelfImage4;

    /**
     * Sets the number of shelves in the view
     * @param game is the model game
     * */
    public void setShelves(Game game){
        if(game.getNumberOfPlayers() == 2){
            shelfImage3.setVisible(false);
            shelfGrid3.setVisible(false);
            shelfImage4.setVisible(false);
            shelfGrid4.setVisible(false);
        } else if (game.getNumberOfPlayers() ==3) {
            shelfImage4.setVisible(false);
            shelfGrid4.setVisible(false);
        }
    }

    /**
     * Initialize the board view
     * @param game is the model game
     * */
    public void initializeBoard(Game game){
        this.tiles = new ArrayList<>();
        continueButton.setVisible(false);
        for(int i=0; i< game.getBoard().getMAX_X(); i++){
            for(int j=0;j<game.getBoard().getMAX_Y();j++){
               Image image;
               if(game.getBoard().getTile(i,j).getType().equals("Empty")){
                   getImageViewFromCoordinatesBoard(i,j,boardPane).setImage(null);
               }
               else {
                   if (game.getBoard().getTile(i, j).getPath() == null) {
                       continue;
                   }
                   image = new Image(game.getBoard().getTile(i, j).getPath());
                   getImageViewFromCoordinatesBoard(i, j, boardPane).setImage(image);
               }
            }
        }
    }

    /**
     * Sets the client shelf view
     *
     * @param game  is the game model
     * @param client
     */
    public void initializeClientShelf(Game game, Client client){
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                Image image;
                if (game.getPlayerByUsername(client.getUsername()).getShelf().getTile(i, j).getPath() == null) {
                    continue;
                }
                image = new Image(game.getPlayerByUsername(client.getUsername()).getShelf().getTile(i, j).getPath());
                getImageViewForPositionShelf(5-i, j, shelfGrid).setImage(image);
            }
        }
        }

        public void initializeOtherShelves(Game game, LinkedList<String> playerOrder){
            int players = game.getNumberOfPlayers();
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 5; j++) {
                        Image image;
                        if (game.getPlayerByUsername(playerOrder.get(1)).getShelf().getTile(i,j).getPath()==null) {
                            continue;
                        }
                        image = new Image(game.getPlayerByUsername(playerOrder.get(1)).getShelf().getTile(i,j).getPath());
                        getImageViewForPositionShelf2(5-i, j, shelfGrid2).setImage(image);
                    }
                }
            if(players==3){
                initializeShelf3(game, playerOrder);
            } else if(players == 4) {
                initializeShelf3(game, playerOrder);
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 5; j++) {
                        Image image;
                        if (game.getPlayerByUsername(playerOrder.get(3)).getShelf().getTile(i,j).getPath()==null) {
                            continue;
                        }
                        image = new Image(game.getPlayerByUsername(playerOrder.get(3)).getShelf().getTile(i,j).getPath());
                        getImageViewForPositionShelf4(5-i, j, shelfGrid4).setImage(image);
                    }
                }
            }
        }

    private void initializeShelf3(Game game, LinkedList<String> playerOrder) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                Image image;
                if (game.getPlayerByUsername(playerOrder.get(2)).getShelf().getTile(i,j).getPath()==null) {
                    continue;
                }
                image = new Image(game.getPlayerByUsername(playerOrder.get(2)).getShelf().getTile(i,j).getPath());
                getImageViewForPositionShelf3(5-i, j, shelfGrid3).setImage(image);
            }
        }
    }

    private ImageView getImageViewForPositionShelf2(int rowIndex, int colIndex, GridPane grid) {
        String imageViewId = "#s2" + rowIndex + colIndex;
        Node node = grid.lookup(imageViewId);

        if (node instanceof ImageView) {
            return (ImageView) node;
        } else {
            throw new IllegalStateException("ImageView not found for coordinates: (" + rowIndex + ", " + colIndex + ")");
        }
    }

    private ImageView getImageViewForPositionShelf3(int rowIndex, int colIndex, GridPane grid) {
        String imageViewId = "#s3" + rowIndex + colIndex;
        Node node = grid.lookup(imageViewId);

        if (node instanceof ImageView) {
            return (ImageView) node;
        } else {
            throw new IllegalStateException("ImageView not found for coordinates: (" + rowIndex + ", " + colIndex + ")");
        }
    }

    private ImageView getImageViewForPositionShelf4(int rowIndex, int colIndex, GridPane grid) {
        String imageViewId = "#s4" + rowIndex + colIndex;
        Node node = grid.lookup(imageViewId);

        if (node instanceof ImageView) {
            return (ImageView) node;
        } else {
            throw new IllegalStateException("ImageView not found for coordinates: (" + rowIndex + ", " + colIndex + ")");
        }
    }
    private ImageView getImageViewForPositionShelf(int rowIndex, int colIndex, GridPane grid) {
        String imageViewId = "#s" + rowIndex + colIndex;
        Node node = grid.lookup(imageViewId);

        if (node instanceof ImageView) {
            return (ImageView) node;
        } else {
            throw new IllegalStateException("ImageView not found for coordinates: (" + rowIndex + ", " + colIndex + ")");
        }
    }

    private ImageView getImageViewFromCoordinatesBoard(int rowIndex, int colIndex, GridPane grid) {
        String imageViewId = "#b" + rowIndex + colIndex;
        Node node = grid.lookup(imageViewId);

        if (node instanceof ImageView) {
            return (ImageView) node;
        } else {
            throw new IllegalStateException("ImageView not found for coordinates: (" + rowIndex + ", " + colIndex + ")");
        }
    }


    /**
     * Method that decides what happens when cell (0,3) is clicked on the board
     */
    public void b03clicked(MouseEvent mouseEvent) {
        if(!b03click){
            b03.setOpacity(0.5);
            tiles.add("(0,3)");
            b03click = true;
        }else {
            b03.setOpacity(1);
            tiles.remove("(0,3)");
            b03click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (0,4) is clicked on the board
     */
    public void b04clicked(MouseEvent mouseEvent) {
        if(!b04click){
            b04.setOpacity(0.5);
            tiles.add("(0,4)");
            b04click = true;
        }else {
            b04.setOpacity(1);
            tiles.remove("(0,4)");
            b04click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (1,3) is clicked on the board
     */
    public void b13clicked(MouseEvent mouseEvent) {
        if(!b13click){
            b13.setOpacity(0.5);
            tiles.add("(1,3)");
            b13click = true;
        }else {
            b13.setOpacity(1);
            tiles.remove("(1,3)");
            b13click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (1,4) is clicked on the board
     */
    public void b14clicked(MouseEvent mouseEvent) {
        if(!b14click){
            b14.setOpacity(0.5);
            tiles.add("(1,4)");
            b14click = true;
        }else {
            b14.setOpacity(1);
            tiles.remove("(1,4)");
            b14click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (1,5) is clicked on the board
     */
    public void b15clicked(MouseEvent mouseEvent) {
        if(!b15click){
            b15.setOpacity(0.5);
            tiles.add("(1,5)");
            b15click = true;
        }else {
            b15.setOpacity(1);
            tiles.remove("(1,5)");
            b15click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (2,2) is clicked on the board
     */
    public void b22clicked(MouseEvent mouseEvent) {
        if(!b22click){
            b22.setOpacity(0.5);
            tiles.add("(2,2)");
            b22click = true;
        }else {
            b22.setOpacity(1);
            tiles.remove("(2,2)");
            b22click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (2,3) is clicked on the board
     */
    public void b23clicked(MouseEvent mouseEvent) {
        if(!b23click){
            b23.setOpacity(0.5);
            tiles.add("(2,3)");
            b23click = true;
        }else {
            b23.setOpacity(1);
            tiles.remove("(2,3)");
            b23click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (2,4) is clicked on the board
     */
    public void b24clicked(MouseEvent mouseEvent) {
        if(!b24click){
            b24.setOpacity(0.5);
            tiles.add("(2,4)");
            b24click = true;
        }else {
            b24.setOpacity(1);
            tiles.remove("(2,4)");
            b24click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (2,5) is clicked on the board
     */
    public void b25clicked(MouseEvent mouseEvent) {
        if(!b25click){
            b25.setOpacity(0.5);
            tiles.add("(2,5)");
            b25click = true;
        }else {
            b25.setOpacity(1);
            tiles.remove("(2,5)");
            b25click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (2,6) is clicked on the board
     */
    public void b26clicked(MouseEvent mouseEvent) {
        if(!b26click){
            b26.setOpacity(0.5);
            tiles.add("(2,6)");
            b26click = true;
        }else {
            b26.setOpacity(1);
            tiles.remove("(2,6)");
            b26click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (3,1) is clicked on the board
     */
    public void b31clicked(MouseEvent mouseEvent) {
        if(!b31click){
            b31.setOpacity(0.5);
            tiles.add("(3,1)");
            b31click = true;
        }else {
            b31.setOpacity(1);
            tiles.remove("(3,1)");
            b31click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (3,2) is clicked on the board
     */
    public void b32clicked(MouseEvent mouseEvent) {
        if(!b32click){
            b32.setOpacity(0.5);
            tiles.add("(3,2)");
            b32click = true;
        }else {
            b32.setOpacity(1);
            tiles.remove("(3,2)");
            b32click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (3,3) is clicked on the board
     */
    public void b33clicked(MouseEvent mouseEvent) {
        if(!b33click){
            b33.setOpacity(0.5);
            tiles.add("(3,3)");
            b33click = true;
        }else {
            b33.setOpacity(1);
            tiles.remove("(3,3)");
            b33click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (3,4) is clicked on the board
     */
    public void b34clicked(MouseEvent mouseEvent) {
        if(!b34click){
            b34.setOpacity(0.5);
            tiles.add("(3,4)");
            b34click = true;
        }else {
            b34.setOpacity(1);
            tiles.remove("(3,4)");
            b34click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (3,5) is clicked on the board
     */
    public void b35clicked(MouseEvent mouseEvent) {
        if(!b35click){
            b35.setOpacity(0.5);
            tiles.add("(3,5)");
            b35click = true;
        }else {
            b35.setOpacity(1);
            tiles.remove("(3,5)");
            b35click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (3,6) is clicked on the board
     */
    public void b36clicked(MouseEvent mouseEvent) {
        if(!b36click){
            b36.setOpacity(0.5);
            tiles.add("(3,6)");
            b36click = true;
        }else {
            b36.setOpacity(1);
            tiles.remove("(3,6)");
            b36click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (3,7) is clicked on the board
     */
    public void b37clicked(MouseEvent mouseEvent) {
        if(!b37click){
            b37.setOpacity(0.5);
            tiles.add("(3,7)");
            b37click = true;
        }else {
            b37.setOpacity(1);
            tiles.remove("(3,7)");
            b37click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (3,8) is clicked on the board
     */
    public void b38clicked(MouseEvent mouseEvent) {
        if(!b38click){
            b38.setOpacity(0.5);
            tiles.add("(3,8)");
            b38click = true;
        }else {
            b38.setOpacity(1);
            tiles.remove("(3,8)");
            b38click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (4,0) is clicked on the board
     */
    public void b40clicked(MouseEvent mouseEvent) {
        if(!b40click){
            b40.setOpacity(0.5);
            tiles.add("(4,0)");
            b40click = true;
        }else {
            b40.setOpacity(1);
            tiles.remove("(4,0)");
            b40click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (4,1) is clicked on the board
     */
    public void b41clicked(MouseEvent mouseEvent) {
        if(!b41click){
            b41.setOpacity(0.5);
            tiles.add("(4,1)");
            b41click = true;
        }else {
            b41.setOpacity(1);
            tiles.remove("(4,1)");
            b41click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (4,2) is clicked on the board
     */
    public void b42clicked(MouseEvent mouseEvent) {
        if(!b42click){
            b42.setOpacity(0.5);
            tiles.add("(4,2)");
            b42click = true;
        }else {
            b42.setOpacity(1);
            tiles.remove("(4,2)");
            b42click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (4,3) is clicked on the board
     */
    public void b43clicked(MouseEvent mouseEvent) {
        if(!b43click){
            b43.setOpacity(0.5);
            tiles.add("(4,3)");
            b43click = true;
        }else {
            b43.setOpacity(1);
            tiles.remove("(4,3)");
            b43click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (4,4) is clicked on the board
     */
    public void b44clicked(MouseEvent mouseEvent) {
        if(!b44click){
            b44.setOpacity(0.5);
            tiles.add("(4,4)");
            b44click = true;
        }else {
            b44.setOpacity(1);
            tiles.remove("(4,4)");
            b44click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (4,5) is clicked on the board
     */
    public void b45clicked(MouseEvent mouseEvent) {
        if(!b45click){
            b45.setOpacity(0.5);
            tiles.add("(4,5)");
            b45click = true;
        }else {
            b45.setOpacity(1);
            tiles.remove("(4,5)");
            b45click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (4,6) is clicked on the board
     */
    public void b46clicked(MouseEvent mouseEvent) {
        if(!b46click){
            b46.setOpacity(0.5);
            tiles.add("(4,6)");
            b46click = true;
        }else {
            b46.setOpacity(1);
            tiles.remove("(4,6)");
            b46click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (4,7) is clicked on the board
     */
    public void b47clicked(MouseEvent mouseEvent) {
        if(!b47click){
            b47.setOpacity(0.5);
            tiles.add("(4,7)");
            b47click = true;
        }else {
            b47.setOpacity(1);
            tiles.remove("(4,7)");
            b47click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (4,8) is clicked on the board
     */
    public void b48clicked(MouseEvent mouseEvent) {
        if(!b48click){
            b48.setOpacity(0.5);
            tiles.add("(4,8)");
            b48click = true;
        }else {
            b48.setOpacity(1);
            tiles.remove("(4,8)");
            b48click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (5,0) is clicked on the board
     */
    public void b50clicked(MouseEvent mouseEvent) {
        if(!b50click){
            b50.setOpacity(0.5);
            tiles.add("(5,0)");
            b50click = true;
        }else {
            b50.setOpacity(1);
            tiles.remove("(5,0)");
            b50click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (6,2) is clicked on the board
     */
    public void b62clicked(MouseEvent mouseEvent) {
        if(!b62click){
            b62.setOpacity(0.5);
            tiles.add("(6,2)");
            b62click = true;
        }else {
            b62.setOpacity(1);
            tiles.remove("(6,2)");
            b62click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (5,7) is clicked on the board
     */
    public void b57clicked(MouseEvent mouseEvent) {
        if(!b57click){
            b57.setOpacity(0.5);
            tiles.add("(5,7)");
            b57click = true;
        }else {
            b57.setOpacity(1);
            tiles.remove("(5,7)");
            b57click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (5,6) is clicked on the board
     */
    public void b56clicked(MouseEvent mouseEvent) {
        if(!b56click){
            b56.setOpacity(0.5);
            tiles.add("(5,6)");
            b56click = true;
        }else {
            b56.setOpacity(1);
            tiles.remove("(5,6)");
            b56click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (5,5) is clicked on the board
     */
    public void b55clicked(MouseEvent mouseEvent) {
        if(!b55click){
            b55.setOpacity(0.5);
            tiles.add("(5,5)");
            b55click = true;
        }else {
            b55.setOpacity(1);
            tiles.remove("(5,5)");
            b55click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (5,4) is clicked on the board
     */
    public void b54clicked(MouseEvent mouseEvent) {
        if(!b54click){
            b54.setOpacity(0.5);
            tiles.add("(5,4)");
            b54click = true;
        }else {
            b54.setOpacity(1);
            tiles.remove("(5,4)");
            b54click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (5,3) is clicked on the board
     */
    public void b53clicked(MouseEvent mouseEvent) {
        if(!b53click){
            b53.setOpacity(0.5);
            tiles.add("(5,3)");
            b53click = true;
        }else {
            b53.setOpacity(1);
            tiles.remove("(5,3)");
            b53click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (5,2) is clicked on the board
     */
    public void b52clicked(MouseEvent mouseEvent) {
        if(!b52click){
            b52.setOpacity(0.5);
            tiles.add("(5,2)");
            b52click = true;
        }else {
            b52.setOpacity(1);
            tiles.remove("(5,2)");
            b52click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (5,1) is clicked on the board
     */
    public void b51clicked(MouseEvent mouseEvent) {
        if(!b51click){
            b51.setOpacity(0.5);
            tiles.add("(5,1)");
            b51click = true;
        }else {
            b51.setOpacity(1);
            tiles.remove("(5,1)");
            b51click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (6,3) is clicked on the board
     */
    public void b63clicked(MouseEvent mouseEvent) {
        if(!b63click){
            b63.setOpacity(0.5);
            tiles.add("(6,3)");
            b63click = true;
        }else {
            b63.setOpacity(1);
            tiles.remove("(6,3)");
            b63click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (6,4) is clicked on the board
     */
    public void b64clicked(MouseEvent mouseEvent) {
        if(!b64click){
            b64.setOpacity(0.5);
            tiles.add("(6,4)");
            b64click = true;
        }else {
            b64.setOpacity(1);
            tiles.remove("(6,4)");
            b64click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (6,5) is clicked on the board
     */
    public void b65clicked(MouseEvent mouseEvent) {
        if(!b65click){
            b65.setOpacity(0.5);
            tiles.add("(6,5)");
            b65click = true;
        }else {
            b65.setOpacity(1);
            tiles.remove("(6,5)");
            b65click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (6,6) is clicked on the board
     */
    public void b66clicked(MouseEvent mouseEvent) {
        if(!b66click){
            b66.setOpacity(0.5);
            tiles.add("(6,6)");
            b66click = true;
        }else {
            b66.setOpacity(1);
            tiles.remove("(6,6)");
            b66click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (7,3) is clicked on the board
     */
    public void b73clicked(MouseEvent mouseEvent) {
        if(!b73click){
            b73.setOpacity(0.5);
            tiles.add("(7,3)");
            b73click = true;
        }else {
            b73.setOpacity(1);
            tiles.remove("(7,3)");
            b73click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (7,4) is clicked on the board
     */
    public void b74clicked(MouseEvent mouseEvent) {
        if(!b74click){
            b74.setOpacity(0.5);
            tiles.add("(7,4)");
            b74click = true;
        }else {
            b74.setOpacity(1);
            tiles.remove("(7,4)");
            b74click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (7,5) is clicked on the board
     */
    public void b75clicked(MouseEvent mouseEvent) {
        if(!b75click){
            b75.setOpacity(0.5);
            tiles.add("(7,5)");
            b75click = true;
        }else {
            b75.setOpacity(1);
            tiles.remove("(7,5)");
            b75click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (8,4) is clicked on the board
     */
    public void b84clicked(MouseEvent mouseEvent) {
        if(!b84click){
            b84.setOpacity(0.5);
            tiles.add("(8,4)");
            b84click = true;
        }else {
            b84.setOpacity(1);
            tiles.remove("(8,4)");
            b84click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (8,5) is clicked on the board
     */
    public void b85clicked(MouseEvent mouseEvent) {
        if(!b85click){
            b85.setOpacity(0.5);
            tiles.add("(8,5)");
            b85click = true;
        }else {
            b85.setOpacity(1);
            tiles.remove("(8,5)");
            b85click = false;
        }
        updateContinueButtonVisibility();
    }

    /**
     * Method used to set the tiles in the correct format and give them to the manager thread
     */
    public void getTile(){
        String r =  String.join(",",tiles);
        try {
            ViewGUI.queue.put(r);

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        tiles.clear();
    }

    /**
     * Handles the confirmation Button when the player has chosen the tiles
     * */
    public void handleConfirmation(ActionEvent actionEvent) {
        getTile();
    }

    /**
     * When more than a cell is selected this method sets the visibility of the
     * confirmation button as true
     * */
    private void updateContinueButtonVisibility(){
        continueButton.setVisible(tiles.size() > 0);
    }

    public void disableView(){
        boardPane.setDisable(true);
        column0.setDisable(true);
        column1.setDisable(true);
        column2.setDisable(true);
        column3.setDisable(true);
        column4.setDisable(true);
    }

    public void ableView(){
        boardPane.setDisable(false);
        column0.setDisable(false);
        column1.setDisable(false);
        column2.setDisable(false);
        column3.setDisable(false);
        column4.setDisable(false);
    }

    public void insertInColumn0(ActionEvent actionEvent) {
        try {
            ViewGUI.queue.put(0);

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        disableView();
    }

    public void insertInColumn1(ActionEvent actionEvent) {
        try {
            ViewGUI.queue.put(1);

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        disableView();
    }

    public void insertInColumn2(ActionEvent actionEvent) {
        try {
            ViewGUI.queue.put(2);

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        disableView();
    }

    public void insertInColumn3(ActionEvent actionEvent) {
        try {
            ViewGUI.queue.put(3);

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        disableView();
    }

    public void insertInColumn4(ActionEvent actionEvent) {
        try {
            ViewGUI.queue.put(4);

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        disableView();
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

