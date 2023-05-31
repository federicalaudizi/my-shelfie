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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image ;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
    private boolean[][] clickedCells;
    private ImageView[][] imageViews;
    public Label client2;
    public Label client3;
    public Label client4;
    public Label instruction;
    public Button continueButton;
    public Button column0;
    public Button column1;
    public Button column2;
    public Button column3;
    public Button column4;
    private List<String> tiles;

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
        imageViews = new ImageView[game.getBoard().getMAX_X()][game.getBoard().getMAX_Y()];
        clickedCells = new boolean[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                clickedCells[i][j] = false;
            }
        }
        for (Node node : boardPane.getChildren()) {
            if (node instanceof ImageView) {
                int row = GridPane.getRowIndex(node);
                int column = GridPane.getColumnIndex(node);
                imageViews[row][column] = (ImageView) node;
            }
        }
        for(int i=0; i< game.getBoard().getMAX_X(); i++){
            for(int j=0;j<game.getBoard().getMAX_Y();j++){
                ImageView imageView = imageViews[i][j];
                Image image;
               if(game.getBoard().getTile(i,j).getType().equals("Empty")){
                   imageView.setImage(null);
               }
               else {
                   if (game.getBoard().getTile(i, j).getPath() == null) {
                       continue;
                   }
                   image = new Image(game.getBoard().getTile(i, j).getPath());
                   imageView.setImage(image);
                   imageView.setOpacity(1);
                   int finalI = i;
                   int finalJ = j;
                   imageView.setOnMouseClicked(event -> handleCellClick(finalI, finalJ));
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
        ImageView[][] imageMatrix = new ImageView[6][5];

        for (Node node : shelfGrid.getChildren()) {
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
                if (game.getPlayerByUsername(client.getUsername()).getShelf().getTile(i, j).getPath() == null) {
                    continue;
                }
                image = new Image(game.getPlayerByUsername(client.getUsername()).getShelf().getTile(i, j).getPath());
                imageView.setImage(image);
            }
        }
        }

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
                    }
                }
            }
        }

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
            }
        }
    }

    private void handleCellClick(int row, int col) {
        ImageView imageView = imageViews[row][col];
        boolean isCellClicked = clickedCells[row][col];

        if (!isCellClicked) {
            imageView.setOpacity(0.5);
            tiles.add("(" + row + "," + col + ")");
            clickedCells[row][col] = true;
        } else {
            imageView.setOpacity(1);
            tiles.remove("(" + row + "," + col + ")");
            clickedCells[row][col] = false;
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

    public void disableView(){
        boardPane.setDisable(true);
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

    public void ableView(){
        boardPane.setDisable(false);
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

    public void insertInColumn0() {
        try {
            ViewGUI.queue.put(0);

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        disableView();
    }

    public void insertInColumn1() {
        try {
            ViewGUI.queue.put(1);

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        disableView();
    }

    public void insertInColumn2() {
        try {
            ViewGUI.queue.put(2);

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        disableView();
    }

    public void insertInColumn3() {
        try {
            ViewGUI.queue.put(3);

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        disableView();
    }

    public void insertInColumn4() {
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

    public void displayAchievement(String nick,String username, int objectiveNumber) {
        if (nick.equals(username)){
            if (objectiveNumber == 1){
                who.setText("Congratulations! You achieved the first common goal");
            }else {
                who.setText("Congratulations! You achieved second the common goal");
            }
        }else {
            if (objectiveNumber == 1){
                who.setText("Congratulations! " + username + " achieved the first common goal");
            }else{
                who.setText("Congratulations! " + username + " achieved the second common goal");
            }
        }
        commonObj.setVisible(true);
        board.setEffect(new GaussianBlur());
    }

    public void close() {
        commonObj.setVisible(false);
        board.setEffect(null);
    }

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
}

