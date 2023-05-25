package it.polimi.ingsw.client.scene;

import it.polimi.ingsw.client.ViewGUI;
import it.polimi.ingsw.server.model.Game;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image ;

import java.util.ArrayList;
import java.util.List;

public class BoardController{
    public Button continueBotton;
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
    private ImageView shelfImage2;
    @FXML
    private ImageView shelfImage3;
    @FXML
    private ImageView shelfImage4;
    @FXML
    private ImageView s00;

    @FXML
    private ImageView s01;

    @FXML
    private ImageView s02;

    @FXML
    private ImageView s03;

    @FXML
    private ImageView s04;

    @FXML
    private ImageView s10;

    @FXML
    private ImageView s11;

    @FXML
    private ImageView s12;

    @FXML
    private ImageView s13;

    @FXML
    private ImageView s14;

    @FXML
    private ImageView s20;

    @FXML
    private ImageView s21;

    @FXML
    private ImageView s22;

    @FXML
    private ImageView s23;

    @FXML
    private ImageView s24;

    @FXML
    private ImageView s30;

    @FXML
    private ImageView s31;

    @FXML
    private ImageView s32;

    @FXML
    private ImageView s33;

    @FXML
    private ImageView s34;

    @FXML
    private ImageView s40;

    @FXML
    private ImageView s41;

    @FXML
    private ImageView s42;

    @FXML
    private ImageView s43;

    @FXML
    private ImageView s44;

    @FXML
    private ImageView s50;

    @FXML
    private ImageView s51;

    @FXML
    private ImageView s52;

    @FXML
    private ImageView s53;

    @FXML
    private ImageView s54;

    /**
     * Sets the number of shelves in the view
     * @param game is the model game
     * */
    public void setShelves(Game game){
        if(game.getNumberOfPlayers() == 2){
            shelfImage3.setVisible(false);
            shelfImage4.setVisible(false);
        } else if (game.getNumberOfPlayers() ==3) {
            shelfImage4.setVisible(false);
        }
    }

    /**
     * Initialize the board view
     * @param game is the model game
     * */
    public void initializeBoard(Game game){
        this.tiles = new ArrayList<>();
        continueBotton.setVisible(false);
        for(int i=0; i< game.getBoard().getMAX_X(); i++){
            for(int j=0;j<game.getBoard().getMAX_Y();j++){
               Image image;
               if(game.getBoard().getTile(i,j).getPath()==null){
                   continue;
               }
               image = new Image(game.getBoard().getTile(i,j).getPath());
               getImageViewForPositionBoard(i,j).setImage(image);
            }
        }
    }

    /**
     * Sets the shelves view
     * @param game is the game model
     * */
    /*public void initializeShelves(Game game){
        for(int k=0;k< game.getNumberOfPlayers();k++) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 5; j++) {
                    Image image;
                    if (game.getPlayers().get(k).getShelf().getTile(i, j).getPath() == null) {
                        continue;
                    }
                    image = new Image(game.getPlayers().get(k).getShelf().getTile(i, j).getPath());
                    getImageViewForPositionShelf(i, j).setImage(image);
                }
            }
        }
    }*/

    /**
     * Helper method to get the ImageView of the tile for the shelf
     * @param i is the row
     * @param  j is the column
     * @return the ImageView of the tile in position i,j
     * */
    private ImageView getImageViewForPositionShelf(int i, int j) {
        return getImageViewFromCoordinates(i, j, shelfGrid);
    }

    private ImageView getImageViewFromCoordinates(int rowIndex, int colIndex, GridPane grid) {
        String imageViewId = "#b" + rowIndex + colIndex;
        Node node = grid.lookup(imageViewId);

        if (node instanceof ImageView) {
            return (ImageView) node;
        } else {
            throw new IllegalStateException("ImageView not found for coordinates: (" + rowIndex + ", " + colIndex + ")");
        }
    }

    /**
     * Helper method to get the ImageView of the tile for the board
     *      * @param i is the row
     *      * @param  j is the column
     *      * @return the ImageView of the tile in position i,j*/
    private ImageView getImageViewForPositionBoard(int row, int col) {
        return getImageViewFromCoordinates(row, col, boardPane);
    }

    /**
     * Method that decides what happens when cell (0,3) is clicked on the board
     */
    public void b03clicked(MouseEvent mouseEvent) {
        b03.setOpacity(0.5);
        tiles.add("(0,3)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (0,4) is clicked on the board
     */
    public void b04clicked(MouseEvent mouseEvent) {
        b04.setOpacity(0.5);
        tiles.add("(0,4)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (1,3) is clicked on the board
     */
    public void b13clicked(MouseEvent mouseEvent) {
        b13.setOpacity(0.5);
        tiles.add("(1,3)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (1,4) is clicked on the board
     */
    public void b14clicked(MouseEvent mouseEvent) {
        b14.setOpacity(0.5);
        tiles.add("(1,4)");
        updateContinueButtonVisibility();

    }

    /**
     * Method that decides what happens when cell (1,5) is clicked on the board
     */
    public void b15clicked(MouseEvent mouseEvent) {
        b15.setOpacity(0.5);
        tiles.add("(1,5)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (2,2) is clicked on the board
     */
    public void b22clicked(MouseEvent mouseEvent) {
        b22.setOpacity(0.5);
        tiles.add("(2,2)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (2,3) is clicked on the board
     */
    public void b23clicked(MouseEvent mouseEvent) {
        b23.setOpacity(0.5);
        tiles.add("(2,3)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (2,4) is clicked on the board
     */
    public void b24clicked(MouseEvent mouseEvent) {
        b24.setOpacity(0.5);
        tiles.add("(2,4)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (2,5) is clicked on the board
     */
    public void b25clicked(MouseEvent mouseEvent) {
        b25.setOpacity(0.5);
        tiles.add("(2,5)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (2,6) is clicked on the board
     */
    public void b26clicked(MouseEvent mouseEvent) {
        b26.setOpacity(0.5);
        tiles.add("(2,6)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (3,1) is clicked on the board
     */
    public void b31clicked(MouseEvent mouseEvent) {
        b31.setOpacity(0.5);
        tiles.add("(3,1)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (3,2) is clicked on the board
     */
    public void b32clicked(MouseEvent mouseEvent) {
        b32.setOpacity(0.5);
        tiles.add("(3,2)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (3,3) is clicked on the board
     */
    public void b33clicked(MouseEvent mouseEvent) {
        b33.setOpacity(0.5);
        tiles.add("(3,3)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (3,4) is clicked on the board
     */
    public void b34clicked(MouseEvent mouseEvent) {
        b34.setOpacity(0.5);
        tiles.add("(3,4)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (3,5) is clicked on the board
     */
    public void b35clicked(MouseEvent mouseEvent) {
        b35.setOpacity(0.5);
        tiles.add("(3,5)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (3,6) is clicked on the board
     */
    public void b36clicked(MouseEvent mouseEvent) {
        b36.setOpacity(0.5);
        tiles.add("(3,6)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (3,7) is clicked on the board
     */
    public void b37clicked(MouseEvent mouseEvent) {
        b37.setOpacity(0.5);
        tiles.add("(3,7)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (3,8) is clicked on the board
     */
    public void b38clicked(MouseEvent mouseEvent) {
        b38.setOpacity(0.5);
        tiles.add("(3,8)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (4,0) is clicked on the board
     */
    public void b40clicked(MouseEvent mouseEvent) {
        b40.setOpacity(0.5);
        tiles.add("(4,0)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (4,1) is clicked on the board
     */
    public void b41clicked(MouseEvent mouseEvent) {
        b41.setOpacity(0.5);
        tiles.add("(4,1)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (4,2) is clicked on the board
     */
    public void b42clicked(MouseEvent mouseEvent) {
        b42.setOpacity(0.5);
        tiles.add("(4,2)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (4,3) is clicked on the board
     */
    public void b43clicked(MouseEvent mouseEvent) {
        b43.setOpacity(0.5);
        tiles.add("(4,3)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (4,4) is clicked on the board
     */
    public void b44clicked(MouseEvent mouseEvent) {
        b44.setOpacity(0.5);
        tiles.add("(4,4)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (4,5) is clicked on the board
     */
    public void b45clicked(MouseEvent mouseEvent) {
        b45.setOpacity(0.5);
        tiles.add("(4,5)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (4,6) is clicked on the board
     */
    public void b46clicked(MouseEvent mouseEvent) {
        b46.setOpacity(0.5);
        tiles.add("(4,6)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (4,7) is clicked on the board
     */
    public void b47clicked(MouseEvent mouseEvent) {
        b47.setOpacity(0.5);
        tiles.add("(4,7)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (4,8) is clicked on the board
     */
    public void b48clicked(MouseEvent mouseEvent) {
        b48.setOpacity(0.5);
        tiles.add("(4,8)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (5,0) is clicked on the board
     */
    public void b50clicked(MouseEvent mouseEvent) {
        b50.setOpacity(0.5);
        tiles.add("(5,0)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (6,2) is clicked on the board
     */
    public void b62clicked(MouseEvent mouseEvent) {
        b62.setOpacity(0.5);
        tiles.add("(6,2)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (5,7) is clicked on the board
     */
    public void b57clicked(MouseEvent mouseEvent) {
        b57.setOpacity(0.5);
        tiles.add("(5,7)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (5,6) is clicked on the board
     */
    public void b56clicked(MouseEvent mouseEvent) {
        b56.setOpacity(0.5);
        tiles.add("(5,6)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (5,5) is clicked on the board
     */
    public void b55clicked(MouseEvent mouseEvent) {
        b55.setOpacity(0.5);
        tiles.add("(5,5)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (5,4) is clicked on the board
     */
    public void b54clicked(MouseEvent mouseEvent) {
        b54.setOpacity(0.5);
        tiles.add("(5,4)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (5,3) is clicked on the board
     */
    public void b53clicked(MouseEvent mouseEvent) {
        b53.setOpacity(0.5);
        tiles.add("(5,3)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (5,2) is clicked on the board
     */
    public void b52clicked(MouseEvent mouseEvent) {
        b52.setOpacity(0.5);
        tiles.add("(5,2)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (5,1) is clicked on the board
     */
    public void b51clicked(MouseEvent mouseEvent) {
        b51.setOpacity(0.5);
        tiles.add("(5,1)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (6,3) is clicked on the board
     */
    public void b63clicked(MouseEvent mouseEvent) {
        b63.setOpacity(0.5);
        tiles.add("(6,3)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (6,4) is clicked on the board
     */
    public void b64clicked(MouseEvent mouseEvent) {
        b64.setOpacity(0.5);
        tiles.add("(6,4)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (6,5) is clicked on the board
     */
    public void b65clicked(MouseEvent mouseEvent) {
        b65.setOpacity(0.5);
        tiles.add("(6,5)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (6,6) is clicked on the board
     */
    public void b66clicked(MouseEvent mouseEvent) {
        b66.setOpacity(0.5);
        tiles.add("(6,6)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (7,3) is clicked on the board
     */
    public void b73clicked(MouseEvent mouseEvent) {
        b73.setOpacity(0.5);
        tiles.add("(7,3)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (7,4) is clicked on the board
     */
    public void b74clicked(MouseEvent mouseEvent) {
        b74.setOpacity(0.5);
        tiles.add("(7,4)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (7,5) is clicked on the board
     */
    public void b75clicked(MouseEvent mouseEvent) {
        b75.setOpacity(0.5);
        tiles.add("(7,5)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (8,4) is clicked on the board
     */
    public void b84clicked(MouseEvent mouseEvent) {
        b84.setOpacity(0.5);
        tiles.add("(8,4)");
        updateContinueButtonVisibility();
    }

    /**
     * Method that decides what happens when cell (8,5) is clicked on the board
     */
    public void b85clicked(MouseEvent mouseEvent) {
        b85.setOpacity(0.5);
        tiles.add("(8,5)");
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
        if(tiles.size()>0){
            continueBotton.setVisible(true);
        }
    }

    public void disableView(){
        boardPane.setDisable(true);
        shelfGrid.setDisable(true);
    }

    public void ableView(){
        boardPane.setDisable(false);
        shelfGrid.setDisable(false);
    }
}

