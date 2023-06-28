package it.polimi.ingsw.client.scene;

import com.sun.glass.ui.Cursor;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.Gui;
import it.polimi.ingsw.client.ViewGUI;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Tile;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BoardController{
    public AnchorPane board;
    public AnchorPane commonObj;
    public Text who;
    public AnchorPane leaderboard;
    public Text firstPlace;
    public Text secondPlace;
    public Text thirdPlace;
    public Text lastPlace;
    public Text firstPoints;
    public Text secondPoints;
    public Text thirdPoints;
    public Text fourthPoints;
    public Button exit;
    public Button newGame;
    public ImageView tile1;
    public ImageView tile2;
    public ImageView tile3;
    public ImageView scoreFirst;
    public ImageView scoreSecond;
    public ImageView scoreFirst1;
    public ImageView scoreSecond1;
    public ImageView scoreFirst2;
    public ImageView scoreSecond2;
    public ImageView scoreSecond3;
    public ImageView scoreFirst3;
    public Text ClientName;
    public ImageView winner_first;
    public ImageView winner_second;
    public ImageView winner_third;
    public ImageView winner;
    public ImageView living_room_image;
    public AnchorPane serverError;
    private boolean[][] clickedCells;
    private ImageView[][] imageViews;
    public Label client2;
    public Label client3;
    public Label client4;
    public Label instruction;
    public Button continueButton;
    public ImageView column0;
    public ImageView column1;
    public ImageView column2;
    public ImageView column3;
    public ImageView column4;
    private List<String> tiles;
    private List<Tile> temp;

    @FXML
    public GridPane boardPane;

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
     * Sets the view of the correct number of shelves in the board
     * @param game The Game object representing the current game.
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
     * Initializes the game board by updating the ImageView nodes in the boardPane with the corresponding
     * tiles from the game's board. Also sets up event handling for mouse clicks on the ImageView nodes.
     *
     * @param game The Game object representing the current game.
     */
    public void initializeBoard(Game game){
        //initialize local variables
        this.temp = new ArrayList<>();
        tile1.setImage(null);
        tile2.setImage(null);
        tile3.setImage(null);
        this.tiles = new ArrayList<>();
        continueButton.setVisible(false);
        imageViews = new ImageView[game.getBoard().getMAX_X()][game.getBoard().getMAX_Y()];
        clickedCells = new boolean[9][9];

        for (int i = 0; i < 9; i++) {
            Arrays.fill(clickedCells[i], false);
        }


        for (Node node : boardPane.getChildren()) {
            // Check if the node is an instance of ImageView
            if (node instanceof ImageView) {
                // Get the row and column indices of the node in the GridPane
                int row = GridPane.getRowIndex(node);
                int column = GridPane.getColumnIndex(node);
                // Assign the ImageView node to the corresponding position in the imageViews array
                imageViews[row][column] = (ImageView) node;
            }
        }

        for(int i=0; i< game.getBoard().getMAX_X(); i++){
            for(int j=0;j<game.getBoard().getMAX_Y();j++){
                ImageView imageView = imageViews[i][j];
                Image image;
                //if the image is empty it sets the imageview to null
               if(game.getBoard().getTile(i,j).getType().equals("Empty")){
                   imageView.setImage(null);
               }
               else {
                   //if there is no imageview continue
                   if (game.getBoard().getTile(i, j).getPath() == null) {
                       continue;
                   }
                   //sets the right imageView
                   image = new Image(game.getBoard().getTile(i, j).getPath());
                   imageView.setImage(image);
                   imageView.setOpacity(1);
                   int finalI = i;
                   int finalJ = j;
                   imageView.setOnMouseClicked(event -> handleCellClick(finalI, finalJ, game));
               }
            }
        }
    }

    /**
     * Resets the clickedCells array to false and restores the opacity of all ImageViews in the boardPane.
     * This method is typically used to reset the state of the game board.
     */
    public void resetFalse(){
        for (int i = 0; i < 9; i++) {
            Arrays.fill(clickedCells[i], false);
        }
        for (Node node : boardPane.getChildren()) {
            if (node instanceof ImageView imageView) {
                imageView.setOpacity(1);
            }
        }
    }

    /**
     * Initializes the client's shelf in the game by updating the ImageView nodes in the shelfGrid
     * with the corresponding tiles from the client's shelf.
     *
     * @param game    The Game object representing the current game.
     * @param client  The Client object representing the client whose shelf is being initialized.
     */
    public void initializeClientShelf(Game game, Client client){
        ImageView[][] imageMatrix = new ImageView[6][5];
        ClientName.setText(client.getUsername());

        // Check if the node is an instance of ImageView
        for (Node node : shelfGrid.getChildren()) {
            if (node instanceof ImageView) {
                // Get the row and column indices of the node in the GridPane
                int row = GridPane.getRowIndex(node);
                int column = GridPane.getColumnIndex(node);
                imageMatrix[row][column] = (ImageView) node;
            }
        }
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                ImageView imageView = imageMatrix[5-i][j];
                Image image;
                if (game.getPlayerByUsername(client.getUsername()).getShelf().getTile(i, j).getPath() == null) {
                    continue;
                }
                image = new Image(game.getPlayerByUsername(client.getUsername()).getShelf().getTile(i, j).getPath());
                imageView.setImage(image);
            }
        }
        }

    /**
     * Initializes the shelves of the other players in the game by updating the ImageView nodes in the
     * respective shelfGrids with the corresponding tiles from each player's shelf. It also sets the names
     * of the other players on UI elements.
     *
     * @param game         The Game object representing the current game.
     * @param playerOrder  A LinkedList containing the players in the game.
     */
        public void initializeOtherShelves(Game game, LinkedList<String> playerOrder){
        client2.setText(playerOrder.get(1));
        int players = game.getNumberOfPlayers();
            ImageView[][] imageMatrix = new ImageView[6][5];

            for (Node node : shelfGrid2.getChildren()) {
                if (node instanceof ImageView) {
                    int row = GridPane.getRowIndex(node);
                    int column = GridPane.getColumnIndex(node);
                    imageMatrix[row][column] = (ImageView) node;
                }
            }
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 5; j++) {
                    ImageView imageView = imageMatrix[5-i][j];
                    Image image;
                    if (game.getPlayerByUsername(playerOrder.get(1)).getShelf().getTile(i, j).getPath() == null) {
                        continue;
                    }
                    image = new Image(game.getPlayerByUsername(playerOrder.get(1)).getShelf().getTile(i, j).getPath());
                    imageView.setImage(image);
                }
            }
            if(players==3){
                initializeShelf3(game, playerOrder);
            } else if(players == 4) {
                initializeShelf3(game, playerOrder);
                client4.setText(playerOrder.get(3));
                ImageView[][] imageMatrix4 = new ImageView[6][5];

                for (Node node : shelfGrid4.getChildren()) {
                    if (node instanceof ImageView) {
                        int row = GridPane.getRowIndex(node);
                        int column = GridPane.getColumnIndex(node);
                        imageMatrix4[row][column] = (ImageView) node;
                    }
                }
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 5; j++) {
                        ImageView imageView = imageMatrix4[5-i][j];
                        Image image;
                        if (game.getPlayerByUsername(playerOrder.get(3)).getShelf().getTile(i, j).getPath() == null) {
                            continue;
                        }
                        image = new Image(game.getPlayerByUsername(playerOrder.get(3)).getShelf().getTile(i, j).getPath());
                        imageView.setImage(image);
                        GridPane.setHalignment(imageView, HPos.CENTER);
                        GridPane.setValignment(imageView, VPos.CENTER);
                    }
                }
            }
        }

    /**
     * Initializes the shelf for the third player in the game by updating the ImageView nodes in the
     * shelfGrid3 with the corresponding tiles from the player's shelf. It also sets the name of the
     * third player on the client3 UI element.
     *
     * @param game         The Game object representing the current game.
     * @param playerOrder  A LinkedList containing the order of the players in the game.
     */
    private void initializeShelf3(Game game, LinkedList<String> playerOrder) {
        client3.setText(playerOrder.get(2));
        ImageView[][] imageMatrix = new ImageView[6][5];

        for (Node node : shelfGrid3.getChildren()) {
            if (node instanceof ImageView) {
                int row = GridPane.getRowIndex(node);
                int column = GridPane.getColumnIndex(node);
                imageMatrix[row][column] = (ImageView) node;
            }
        }
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                ImageView imageView = imageMatrix[5-i][j];
                Image image;
                if (game.getPlayerByUsername(playerOrder.get(2)).getShelf().getTile(i, j).getPath() == null) {
                    continue;
                }
                image = new Image(game.getPlayerByUsername(playerOrder.get(2)).getShelf().getTile(i, j).getPath());
                imageView.setImage(image);
                GridPane.setHalignment(imageView, HPos.CENTER);
                GridPane.setValignment(imageView, VPos.CENTER);
            }
        }
    }

    /**
     * Handles the click event on a cell in the game board. Updates the visual state of the clicked cell
     * and manages the selection of tiles. Also updates the visibility of the continueButton based on
     * the current selection state.
     *
     * @param row   The row index of the clicked cell.
     * @param col   The column index of the clicked cell.
     * @param game  The Game object representing the current game.
     */
    private void handleCellClick(int row, int col, Game game) {
        ImageView imageView = imageViews[row][col];
        boolean isCellClicked = clickedCells[row][col];

        if (!isCellClicked && imageView.getImage() != null) {
            int firstEmptyTileIndex = -1;
            if (tile3.getImage() == null) {
                firstEmptyTileIndex = 1;
            } else if (tile2.getImage() == null) {
                firstEmptyTileIndex = 2;
            } else if (tile1.getImage() == null) {
                firstEmptyTileIndex = 3;
            }

            if (firstEmptyTileIndex != -1) {
                InputStream imagePath = game.getBoard().getTile(row, col).getPath();
                switch (firstEmptyTileIndex) {
                    case 1 -> tile3.setImage(new Image(imagePath));
                    case 2 -> tile2.setImage(new Image(imagePath));
                    case 3 -> tile1.setImage(new Image(imagePath));
                }
                if(!clickedCells[row][col]){
                    imageView.setOpacity(0.5);
                    clickedCells[row][col] = true;
                    tiles.add("(" + row + "," + col + ")");
                    temp.add(game.getBoard().getTile(row,col));
                }
            }
        } else {
            if(clickedCells[row][col]){
                imageView.setOpacity(1);
                clickedCells[row][col] = false;
                tiles.remove("(" + row + "," + col + ")");
                temp.remove(game.getBoard().getTile(row,col));
            }

            int s = temp.size();
            if(s >= 3){
                tile3.setImage(new Image(temp.get(0).getPath()));
                tile2.setImage(new Image(temp.get(1).getPath()));
                tile1.setImage(new Image(temp.get(2).getPath()));
            } else if (s == 2) {
                tile3.setImage(new Image(temp.get(0).getPath()));
                tile2.setImage(new Image(temp.get(1).getPath()));
                tile1.setImage(null);
            } else if (s==1) {
                tile3.setImage(new Image(temp.get(0).getPath()));
                tile2.setImage(null);
                tile1.setImage(null);
            }else {
                tile3.setImage(null);
                tile2.setImage(null);
                tile1.setImage(null);
            }
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
        temp.clear();
        tile1.setImage(null);
        tile2.setImage(null);
        tile3.setImage(null);
    }

    /**
     * Handles the confirmation Button when the player has chosen the tiles
     * */
    public void handleConfirmation() {
        getTile();
    }

    /**
     * When more than a cell is selected this method sets the visibility of the
     * confirmation button as true
     * */
    private void updateContinueButtonVisibility(){
        continueButton.setVisible(tiles.size() > 0);
    }

    /**
     * Disables and hides the columns in the view.
     * This method sets disable and visible properties of each column in the view to false.
     */
    public void disableView(){
        column0.setDisable(true);
        column1.setDisable(true);
        column2.setDisable(true);
        column3.setDisable(true);
        column4.setDisable(true);
        column0.setVisible(false);
        column1.setVisible(false);
        column2.setVisible(false);
        column3.setVisible(false);
        column4.setVisible(false);
    }

    /**
     * Enables and shows the columns in the view.
     * This method sets disable and visible properties of each column in the view to true.
     */
    public void ableView(){
        column0.setDisable(false);
        column1.setDisable(false);
        column2.setDisable(false);
        column3.setDisable(false);
        column4.setDisable(false);
        column0.setVisible(true);
        column1.setVisible(true);
        column2.setVisible(true);
        column3.setVisible(true);
        column4.setVisible(true);
    }

    /**
     * Inserts a value into column 0 of the view.
     * This method puts the value 0 into the queue for further processing.
     * It also disables and hides the columns in the view.
     */
    public void insertInColumn0() {
        try {
            ViewGUI.queue.put(0);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        disableView();
    }

    /**
     * Inserts a value into column 1 of the view.
     * This method puts the value 1 into the queue for further processing.
     * It also disables and hides the columns in the view.
     */
    public void insertInColumn1() {
        try {
            ViewGUI.queue.put(1);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        disableView();
    }

    /**
     * Inserts a value into column 2 of the view.
     * This method puts the value 2 into the queue for further processing.
     * It also disables and hides the columns in the view.
     */
    public void insertInColumn2() {
        try {
            ViewGUI.queue.put(2);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        disableView();
    }

    /**
     * Inserts a value into column 3 of the view.
     * This method puts the value 3 into the queue for further processing.
     * It also disables and hides the columns in the view.
     */
    public void insertInColumn3() {
        try {
            ViewGUI.queue.put(3);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        disableView();
    }

    /**
     * Inserts a value into column 4 of the view.
     * This method puts the value 4 into the queue for further processing.
     * It also disables and hides the columns in the view.
     */
    public void insertInColumn4() {
        try {
            ViewGUI.queue.put(4);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        disableView();
    }


    /**
     * Displays an achievement message for a specific user and objective number.
     *
     * @param nick The nickname of the current user.
     * @param username The username of the user who achieved the objective.
     * @param objectiveNumber The number of the objective achieved.
     */
    public void displayAchievement(String nick,String username, int objectiveNumber) {
        if (nick.equals(username)){
            if (objectiveNumber == 1){
                who.setText("Congratulations! You achieved the first common goal");
            }else {
                who.setText("Congratulations! You achieved the second common goal");
            }
        }else {
            if (objectiveNumber == 1){
                who.setText(username + " achieved the first common goal");
            }else{
                who.setText(username + " achieved the second common goal");
            }
        }
        commonObj.setVisible(true);
        board.setEffect(new GaussianBlur());
    }

    /**
     * Closes the current view or dialog, hiding the common object and removing any visual effects.
     */
    public void close() {
        commonObj.setVisible(false);
        board.setEffect(null);
    }

    /**
     * Displays the leaderboard with the given rank data.
     *
     * @param rank The JSONArray containing the rank data.
     */
    public void showLeaderboard(JSONArray rank){
        JSONObject rank_first, rank_second, rank_third, rank_fourth;
        String username_first, username_second, username_third, username_fourth;
        int points_first, points_second, points_third, points_fourth;
        if(rank.length() == 1){
            rank_first = rank.getJSONObject(0);
            username_first = rank_first.getString("username");
            points_first = rank_first.getInt("points");
            firstPlace.setText(username_first);
            firstPoints.setText(Integer.toString(points_first));
        } else if (rank.length() == 2) {
            rank_first = rank.getJSONObject(0);
            username_first = rank_first.getString("username");
            points_first = rank_first.getInt("points");
            rank_second = rank.getJSONObject(1);
            username_second = rank_second.getString("username");
            points_second = rank_second.getInt("points");
            firstPlace.setText(username_first);
            firstPoints.setText(Integer.toString(points_first));
            secondPlace.setText(username_second);
            secondPoints.setText(Integer.toString(points_second));
        } else if (rank.length() == 3){
            rank_first = rank.getJSONObject(0);
            username_first = rank_first.getString("username");
            points_first = rank_first.getInt("points");
            rank_second = rank.getJSONObject(1);
            username_second = rank_second.getString("username");
            points_second = rank_second.getInt("points");
            rank_third = rank.getJSONObject(2);
            username_third = rank_third.getString("username");
            points_third = rank_third.getInt("points");
            firstPlace.setText(username_first);
            firstPoints.setText(Integer.toString(points_first));
            secondPlace.setText(username_second);
            secondPoints.setText(Integer.toString(points_second));
            thirdPlace.setText(username_third);
            thirdPoints.setText(Integer.toString(points_third));
        }else {
            rank_first = rank.getJSONObject(0);
            username_first = rank_first.getString("username");
            points_first = rank_first.getInt("points");
            rank_second = rank.getJSONObject(1);
            username_second = rank_second.getString("username");
            points_second = rank_second.getInt("points");
            rank_third = rank.getJSONObject(2);
            username_third = rank_third.getString("username");
            points_third = rank_third.getInt("points");
            rank_fourth = rank.getJSONObject(3);
            username_fourth = rank_fourth.getString("username");
            points_fourth = rank_fourth.getInt("points");
            firstPlace.setText(username_first);
            firstPoints.setText(Integer.toString(points_first));
            secondPlace.setText(username_second);
            secondPoints.setText(Integer.toString(points_second));
            thirdPlace.setText(username_third);
            thirdPoints.setText(Integer.toString(points_third));
            lastPlace.setText(username_fourth);
            fourthPoints.setText(Integer.toString(points_fourth));
        }
        board.setEffect(new GaussianBlur());
        leaderboard.setVisible(true);
    }

    /**
     * Sets up the exit button functionality.
     * When the exit button is clicked or the window is closed, the application will be terminated.
     */
    public void exitButton (){
        Gui.getStage().setOnCloseRequest((WindowEvent t) -> {
            Platform.exit();
            System.exit(0);
        });
        try {
            ViewGUI.queue.put(true);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Starts a new game by adding a "true" value to the queue.
     *
     * @throws RuntimeException If the thread is interrupted while adding the value to the queue.
     */
    public void startNewGame(){
        try {
            ViewGUI.queue.put(false);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the translation transition for the columns.
     * Each column will be animated using a translate transition.
     * Call this method to start the animation for the columns.
     */
    public void setTransition() {
        animateColumn(column0);
        animateColumn(column1);
        animateColumn(column2);
        animateColumn(column3);
        animateColumn(column4);
    }

    /**
     * Animates the specified column using a translate transition.
     * The column will move vertically in a continuous animation loop.
     *
     * @param column The ImageView representing the column to animate.
     */
    private void animateColumn(ImageView column) {
        double initialY = column.getY();
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.75), column);
        translateTransition.setFromY(initialY);
        translateTransition.setToY(initialY - 10);
        translateTransition.setAutoReverse(true);
        translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransition.play();
    }
}

