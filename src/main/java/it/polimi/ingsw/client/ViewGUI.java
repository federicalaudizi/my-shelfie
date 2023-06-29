package it.polimi.ingsw.client;

import it.polimi.ingsw.client.scene.*;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.PersonalObjectiveCard;
import it.polimi.ingsw.server.model.PointCard;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;


public class ViewGUI extends View {
    Client client;
    private final JoinController joinController;
    private final NumberOfPlayersController numberOfPlayersController;
    private final BoardController gameController;
    public static Parent welcomeRoot;
    public static Parent joinRoot;
    public static Parent gameOptionsRoot;
    public static Parent connectRoot;
    public static Parent nicknameRoot;
    public static Parent numberOfPlayersRoot;
    public static Parent gameRoot;

    /**
     * Constructs a new instance of the ViewGUI class.
     *
     * @param client the client object associated with the GUI
     */
    public ViewGUI(Client client) {
        // Load the FXML files for different GUI components
        FXMLLoader welcomeLoader = new FXMLLoader(getClass().getResource("/Welcome.fxml"));
        FXMLLoader connectLoader = new FXMLLoader(getClass().getResource("/Connection.fxml"));
        FXMLLoader nicknameLoader = new FXMLLoader(getClass().getResource("/Username.fxml"));
        FXMLLoader gameOptionsLoader = new FXMLLoader(getClass().getResource("/GameOptions.fxml"));
        FXMLLoader numberOfPlayersLoader = new FXMLLoader(getClass().getResource("/NumberOfPlayers.fxml"));
        FXMLLoader joinLoader = new FXMLLoader(getClass().getResource("/JoinGame.fxml"));
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/Board.fxml"));

        try {
            // Load the root elements of the FXML files
            welcomeRoot = welcomeLoader.load();
            connectRoot = connectLoader.load();
            nicknameRoot = nicknameLoader.load();
            gameOptionsRoot = gameOptionsLoader.load();
            numberOfPlayersRoot = numberOfPlayersLoader.load();
            joinRoot = joinLoader.load();
            gameRoot = gameLoader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get the controllers for each GUI component
        WelcomeController welcomeController = welcomeLoader.getController();
        ConnectionController connectionController = connectLoader.getController();
        NicknameController nicknameController = nicknameLoader.getController();
        GameOptionsController gameOptionsController = gameOptionsLoader.getController();
        this.numberOfPlayersController = numberOfPlayersLoader.getController();
        this.joinController = joinLoader.getController();
        this.gameController = gameLoader.getController();
        this.client = client;
    }


    /**
     * Updates the game state and UI based on the provided game object and player order.
     *
     * @param game        The game object representing the current state of the game.
     * @param playerOrder The order of players in the game.
     */
    @Override
    void update(Game game, LinkedList<String> playerOrder) {
        if (game.isLastTurn()) {
            Platform.runLater(() -> {
                gameController.instruction.setText("It's the last turn!");
                String stream = "/Images/livingroom.png";
                try (InputStream inputStream = getClass().getResourceAsStream(stream)) {
                    if (inputStream != null) {
                        Image image = new Image(inputStream);
                        gameController.living_room_image.setImage(image);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            Platform.runLater(() -> {
                gameController.instruction.setText("");
                String stream = "/Images/new_livingroom.png";
                try (InputStream inputStream = getClass().getResourceAsStream(stream)) {
                    if (inputStream != null) {
                        Image image = new Image(inputStream);
                        gameController.living_room_image.setImage(image);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        gameController.boardPane.setDisable(true);
        setCommonObjectivesCards(game);
        setPersonalObjectiveCard(game);
        gameController.disableView();
        gameController.setShelves(game);
        gameController.initializeBoard(game);
        gameController.initializeClientShelf(game, this.client);
        gameController.initializeOtherShelves(game, playerOrder);
        setCommonObjectivesScores(game);
        setCommonObjectiveScoresOtherPlayer(game, playerOrder);
        Platform.runLater(() -> {
            Gui.getStage().setTitle("My Shelfie");
            Scene currentScene = Gui.getStage().getScene();
            if (currentScene == null || currentScene.getRoot() != gameRoot) {
                numberOfPlayersController.progress.setVisible(false);
                numberOfPlayersController.text.setVisible(false);
                joinController.progress.setVisible(false);
                joinController.progressText.setVisible(false);
                Scene newScene = new Scene(gameRoot);
                Gui.getStage().setScene(newScene);
            }
        });

        if (game.isLastTurn()) {
            setWinnerCard(game);
            setWinnerCardOtherPlayers(game, playerOrder);
        }
    }

    /**
     * Sets the winner card image for the current player in the game.
     *
     * @param game The current game object.
     */
    public void setWinnerCard(Game game) {
        InputStream stream;
        Image image;

        // Retrieve the input stream for the winner card image
        stream = getClass().getResourceAsStream("/Images/end_game_card.png");
        assert stream != null; // Ensure that the input stream is not null

        // Create a new Image object from the input stream
        image = new Image(stream);

        // Check if the current player's shelf is full
        if (game.getPlayerByUsername(client.getUsername()).getShelf().isFull()) {
            // Set the winner card image in the game controller
            gameController.winner.setImage(image);
        }
    }

    /**
     * Sets the winner card image for other players in the game.
     *
     * @param game        The current game object.
     * @param playerOrder The ordered list of player usernames.
     */
    public void setWinnerCardOtherPlayers(Game game, LinkedList<String> playerOrder) {
        int numPlayers = game.getNumberOfPlayers();

        // Iterate through all players starting from the second player (index 1)
        for (int i = 1; i < numPlayers; i++) {
            String stream;
            Image image;
            // Check if the player's shelf is full
            if (game.getPlayerByUsername(playerOrder.get(i)).getShelf().isFull()) {
                stream = "/Images/end_game_card.png";

                try (InputStream inputStream = getClass().getResourceAsStream(stream)) {
                    if (inputStream != null) {
                        // Create a new Image object from the input stream
                        image = new Image(inputStream);

                        // Set the winner card image based on the player's index
                        switch (i) {
                            case 1 -> gameController.winner_first.setImage(image);
                            case 2 -> gameController.winner_second.setImage(image);
                            case 3 -> gameController.winner_third.setImage(image);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     Shows a disconnection notification for a specific user.
     @param username The username of the disconnected user.
     */
    public void showDisconnection(String username) {
        Platform.runLater(() -> displayDisconnection(username));
    }

    /**
     * Sets the common objectives scores in the UI based on the provided game object.
     *
     * @param game The game object representing the current state of the game.
     */
    private void setCommonObjectivesScores(Game game) {
        PointCard[] pointcards = game.getPlayerByUsername(client.getUsername()).getPointCards();
        setPointCard(pointcards[0], 1);
        setPointCard(pointcards[1], 2);
    }

    /**
     * Sets the common objective scores for other players in the UI based on the provided
     * game and player order.
     *
     * @param game        The game object representing the current state of the game.
     * @param playerOrder The order of players in the game.
     */
    private void setCommonObjectiveScoresOtherPlayer(Game game, LinkedList<String> playerOrder) {
        ImageView[] scoreFirstArray = {
                null,
                gameController.scoreFirst1,
                gameController.scoreFirst2,
                gameController.scoreFirst3
        };
        ImageView[] scoreSecondArray = {
                null,
                gameController.scoreSecond1,
                gameController.scoreSecond2,
                gameController.scoreSecond3
        };

        int numPlayers = game.getNumberOfPlayers();
        for (int i = 1; i < numPlayers; i++) {
            PointCard[] pointCards = game.getPlayerByUsername(playerOrder.get(i)).getPointCards();
            PointCard card = pointCards[0];
            PointCard card2 = pointCards[1];

            ImageView scoreFirst = scoreFirstArray[i];
            ImageView scoreSecond = scoreSecondArray[i];

            String stream;
            Image image;

            if (card.getValue() == 0) {
                if (scoreFirst != null) {
                    scoreFirst.setImage(null);
                }
            } else {
                stream = "/Images/scoring_" + card.getValue() + ".jpg";
                try (InputStream inputStream = getClass().getResourceAsStream(stream)) {
                    if (inputStream != null) {
                        image = new Image(inputStream);
                        if (scoreFirst != null) {
                            scoreFirst.setImage(image);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (card2.getValue() != 0) {
                stream = "/Images/scoring_" + card2.getValue() + ".jpg";
                try (InputStream inputStream = getClass().getResourceAsStream(stream)) {
                    if (inputStream != null) {
                        image = new Image(inputStream);
                        if (scoreSecond != null) {
                            scoreSecond.setImage(image);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Sets the point card image for a specific position for the client.
     *
     * @param card The PointCard object representing the card.
     * @param i    The position index (1 for the first position, 2 for the second position).
     */
    private void setPointCard(PointCard card, int i) {
        String stream;
        Image image;
        if (i == 1) {
            if (card.getValue() == 0) {
            } else if (card.getValue() == 8) {
                stream = "/Images/scoring_8.jpg";
                try (InputStream inputStream = getClass().getResourceAsStream(stream)) {
                    if (inputStream != null) {
                        image = new Image(inputStream);
                        gameController.scoreFirst.setImage(image);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (card.getValue() == 6) {
                stream = "/Images/scoring_6.jpg";
                try (InputStream inputStream = getClass().getResourceAsStream(stream)) {
                    if (inputStream != null) {
                        image = new Image(inputStream);
                        gameController.scoreFirst.setImage(image);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (card.getValue() == 4) {
                stream = "/Images/scoring_4.jpg";
                try (InputStream inputStream = getClass().getResourceAsStream(stream)) {
                    if (inputStream != null) {
                        image = new Image(inputStream);
                        gameController.scoreFirst.setImage(image);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                stream = "/Images/scoring_2.jpg";
                try (InputStream inputStream = getClass().getResourceAsStream(stream)) {
                    if (inputStream != null) {
                        image = new Image(inputStream);
                        gameController.scoreFirst.setImage(image);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (card.getValue() == 0) {
            } else if (card.getValue() == 8) {
                stream = "/Images/scoring_8.jpg";
                try (InputStream inputStream = getClass().getResourceAsStream(stream)) {
                    if (inputStream != null) {
                        image = new Image(inputStream);
                        gameController.scoreSecond.setImage(image);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (card.getValue() == 6) {
                stream = "/Images/scoring_6.jpg";
                try (InputStream inputStream = getClass().getResourceAsStream(stream)) {
                    if (inputStream != null) {
                        image = new Image(inputStream);
                        gameController.scoreSecond.setImage(image);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (card.getValue() == 4) {
                stream = "/Images/scoring_4.jpg";
                try (InputStream inputStream = getClass().getResourceAsStream(stream)) {
                    if (inputStream != null) {
                        image = new Image(inputStream);
                        gameController.scoreSecond.setImage(image);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                stream = "/Images/scoring_2.jpg";
                try (InputStream inputStream = getClass().getResourceAsStream(stream)) {
                    if (inputStream != null) {
                        image = new Image(inputStream);
                        gameController.scoreSecond.setImage(image);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Sets the personal objective card image for the current player in the game controller.
     *
     * @param game The current game instance.
     */
    public void setPersonalObjectiveCard(Game game) {
        String stream = null;
        Image image;
        PersonalObjectiveCard card = game.getPlayerByUsername(client.getUsername()).getObjective();
        if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.FIRST_PATTERN) {
            stream = "/Images/one_po.png";
        } else if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.SECOND_PATTERN) {
            stream = "/Images/two_po.png";
        } else if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.THIRD_PATTERN) {
            stream = "/Images/three_po.png";
        } else if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.FOURTH_PATTERN) {
            stream = "/Images/four_po.png";
        } else if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.FIFTH_PATTERN) {
            stream = "/Images/five_po.png";
        } else if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.SIXTH_PATTERN) {
            stream = "/Images/six_po.png";
        } else if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.SEVENTH_PATTERN) {
            stream = "/Images/seven_po.png";
        } else if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.EIGHTH_PATTERN) {
            stream = "/Images/eight_po.png";
        } else if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.NINTH_PATTERN) {
            stream = "/Images/nine_po.png";
        } else if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.TENTH_PATTERN) {
            stream = "/Images/ten_po.png";
        } else if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.ELEVENTH_PATTERN) {
            stream = "/Images/eleven_po.png";
        } else if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.TWELFTH_PATTERN) {
            stream = "/Images/twelve_po.png";
        }

        if (stream != null) {
            try (InputStream inputStream = getClass().getResourceAsStream(stream)) {
                if (inputStream != null) {
                    image = new Image(inputStream);
                    gameController.personalCard.setImage(image);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Sets the images for common objective cards in the game.
     *
     * @param game The Game object representing the current game.
     */
    public void setCommonObjectivesCards(Game game) {
        InputStream stream;
        Image image;

        String[] string = game.getObjectives();
        if (Objects.equals(string[0], "PatternOne")) {
            stream = getClass().getResourceAsStream("/Images/one.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        } else if (Objects.equals(string[0], "PatternTwo")) {
            stream = getClass().getResourceAsStream("/Images/two.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        } else if (Objects.equals(string[0], "PatternThree")) {
            stream = getClass().getResourceAsStream("/Images/three.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        } else if (Objects.equals(string[0], "PatternFour")) {
            stream = getClass().getResourceAsStream("/Images/four.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        } else if (Objects.equals(string[0], "PatternFive")) {
            stream = getClass().getResourceAsStream("/Images/five.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        } else if (Objects.equals(string[0], "PatternSix")) {
            stream = getClass().getResourceAsStream("/Images/six.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        } else if (Objects.equals(string[0], "PatternSeven")) {
            stream = getClass().getResourceAsStream("/Images/seven.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        } else if (Objects.equals(string[0], "PatternEight")) {
            stream = getClass().getResourceAsStream("/Images/eight.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        } else if (Objects.equals(string[0], "PatternNine")) {
            stream = getClass().getResourceAsStream("/Images/nine.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        } else if (Objects.equals(string[0], "PatternTen")) {
            stream = getClass().getResourceAsStream("/Images/ten.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        } else if (Objects.equals(string[0], "PatternEleven")) {
            stream = getClass().getResourceAsStream("/Images/eleven.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        } else if (Objects.equals(string[0], "PatternTwelve")) {
            stream = getClass().getResourceAsStream("/Images/twelve.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        }

        if (Objects.equals(string[1], "PatternOne")) {
            stream = getClass().getResourceAsStream("/Images/one.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        } else if (Objects.equals(string[1], "PatternTwo")) {
            stream = getClass().getResourceAsStream("/Images/two.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        } else if (Objects.equals(string[1], "PatternThree")) {
            stream = getClass().getResourceAsStream("/Images/three.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        } else if (Objects.equals(string[1], "PatternFour")) {
            stream = getClass().getResourceAsStream("/Images/four.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        } else if (Objects.equals(string[1], "PatternFive")) {
            stream = getClass().getResourceAsStream("/Images/five.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        } else if (Objects.equals(string[1], "PatternSix")) {
            stream = getClass().getResourceAsStream("/Images/six.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        } else if (Objects.equals(string[1], "PatternSeven")) {
            stream = getClass().getResourceAsStream("/Images/seven.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        } else if (Objects.equals(string[1], "PatternEight")) {
            stream = getClass().getResourceAsStream("/Images/eight.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        } else if (Objects.equals(string[1], "PatternNine")) {
            stream = getClass().getResourceAsStream("/Images/nine.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        } else if (Objects.equals(string[1], "PatternTen")) {
            stream = getClass().getResourceAsStream("/Images/ten.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        } else if (Objects.equals(string[1], "PatternEleven")) {
            stream = getClass().getResourceAsStream("/Images/eleven.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        } else if (Objects.equals(string[1], "PatternTwelve")) {
            stream = getClass().getResourceAsStream("/Images/twelve.jpg");
            assert stream != null;
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        }
    }

    /**
     * Retrieves the IP address from the blocking queue.
     *
     * @return The IP address as a String, or null if no IP address is available.
     */
    @Override
    String getIp() {
        String IP = null;
        try {
            IP = (String) queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return IP;
    }

    /**
     * Allows the user to select a game ID from a list of available game IDs.
     * Displays a GUI scene for game ID selection.
     *
     * @param gameIds The list of available game IDs as ArrayList of Strings.
     * @return The selected game ID as a String, or null if no game ID is selected.
     */
    @Override
    String gameIdSelection(ArrayList<String> gameIds) {
        Scene currentScene = joinRoot.getScene();
        if (currentScene == null) {
            Scene scene = new Scene(joinRoot);
            Platform.runLater(() -> Gui.getStage().setScene(scene));
        } else {
            Platform.runLater(() -> Gui.getStage().setScene(new Scene(joinRoot)));
        }
        String selectedGame = null;
        joinController.addGameIds(gameIds);
        try {
            selectedGame = (String) queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return selectedGame;
    }

    /**
     * Displays the game over screen with the provided leaderboard data.
     *
     * @param leaderboard The leaderboard data as a JSONArray.
     */
    @Override
    void gameOverScreen(JSONArray leaderboard) {
        gameController.showLeaderboard(leaderboard);
    }

    /**
     * Displays the continue screen and waits for user input regarding starting a new game.
     *
     * @return A boolean value indicating whether the user wants to start a new game.
     * - `true` if the user wants to start a new game.
     * - `false` if the user does not want to start a new game.
     * @throws RuntimeException if the thread is interrupted while waiting for user input.
     */
    @Override
    boolean continueScreen() {
        boolean wantsNewGame;
        try {
            wantsNewGame = (boolean) queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (wantsNewGame) {
            Platform.runLater(() -> Gui.getStage().close());
        } else {
            Platform.runLater(() -> Gui.getStage().setScene(new Scene(connectRoot)));
        }
        return wantsNewGame;
    }

    /**
     * Displays the nickname screen and waits for the user to enter their username.
     *
     * @return The username entered by the user.
     * @throws RuntimeException if the thread is interrupted while waiting for user input.
     */
    @Override
    public String getUsername() {
        Scene currentScene = nicknameRoot.getScene();
        if (currentScene == null) {
            Scene scene = new Scene(nicknameRoot);
            Platform.runLater(() -> Gui.getStage().setScene(scene));
        } else {
            Platform.runLater(() -> Gui.getStage().setScene(currentScene));
        }
        String username = null;
        try {
            username = (String) queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return username;
    }

    /**
     * Displays the game options screen and waits for the user to select an option.
     *
     * @return The selected game option as an integer.
     * @throws RuntimeException if the thread is interrupted while waiting for user input.
     */
    @Override
    int getGameOptions() {
        Scene currentScene = gameOptionsRoot.getScene();
        if (currentScene == null) {
            Scene scene = new Scene(gameOptionsRoot);
            Platform.runLater(() -> Gui.getStage().setScene(scene));
        } else {
            Platform.runLater(() -> Gui.getStage().setScene(currentScene));
        }
        int choice;
        try {
            choice = (int) queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return choice;
    }

    /**
     * Displays the number of players screen and returns the selected number of players.
     *
     * @return The selected number of players.
     */
    @Override
    int getPlayerNumber() {
        Scene currentScene = numberOfPlayersRoot.getScene();
        if (currentScene == null) {
            Scene scene = new Scene(numberOfPlayersRoot);
            Platform.runLater(() -> Gui.getStage().setScene(scene));
        } else {
            Platform.runLater(() -> Gui.getStage().setScene(currentScene));
        }
        int numOfPlayers;
        numberOfPlayersController.chooseOption();
        try {
            numOfPlayers = (int) queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        return numOfPlayers;
    }

    /**
     * Enables the board and continue button, displays instructions, waits for the player to choose tiles, and returns the selected tiles.
     *
     * @return The selected tiles.
     */
    @Override
    String getTiles() {
        gameController.resetFalse();
        gameController.boardPane.setDisable(false);
        gameController.continueButton.setDisable(false);
        Platform.runLater(() -> gameController.instruction.setText("Choose the tiles, then press continue."));
        String r = null;
        try {
            r = (String) queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameController.disableView();
        gameController.continueButton.setVisible(false);
        gameController.boardPane.setDisable(true);
        return r;
    }

    /**
     * Disables the continue button, updates the instruction text, enables the board for column selection, waits for the player to choose a column, and returns the selected column.
     *
     * @return The selected column.
     */
    @Override
    int getColumn() {
        gameController.continueButton.setDisable(true);
        Platform.runLater(() -> gameController.instruction.setText("Good choice, now choose the column."));
        gameController.ableView();
        gameController.setTransition();
        int column = 0;
        try {
            column = (int) queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameController.disableView();
        return column;
    }


    /**
     * Displays an achievement for a player.
     *
     * @param username        The username of the player who achieved the objective.
     * @param objectiveNumber The number of the objective achieved.
     */
    @Override
    void showAchievement(String username, int objectiveNumber) {
        String nick = client.getUsername();
        gameController.displayAchievement(nick, username, objectiveNumber);
    }

    /**
     * Displays an error message in the game interface based on the given error message.
     *
     * @param errorMessage The error message to be displayed.
     */
    @Override
    void showError(String errorMessage) {
        Platform.runLater(() -> displayError(errorMessage));
    }


    /**
     * Displays a popup notification to the client indicating that they have been disconnected by the server.
     * When the "Okay" button is pressed, the client application is closed.
     */
    public void showServerDisconnection() {
        gameController.serverError.setVisible(true);
    }

    /**
     * Displays a disconnection message in a popup window.
     *
     * @param message The message to be displayed indicating the disconnected player.
     */
    public void displayDisconnection(String message) {
        Stage popupStage = new Stage();
        popupStage.initStyle(StageStyle.UNDECORATED);
        Text text = new Text(message + " disconnected from the game");
        Button tryAgainButton = new Button("Okay");
        VBox.setMargin(tryAgainButton, new Insets(40, 0, 0, 182)); // Add margin to the button
        // Create the AnchorPane and add the content nodes
        VBox layout = new VBox(3);
        layout.getChildren().addAll(text, tryAgainButton);

        TitledPane errorPopup = new TitledPane();
        errorPopup.setAnimated(false);
        errorPopup.setLayoutX(197);
        errorPopup.setLayoutY(61);
        errorPopup.setPrefHeight(130);
        errorPopup.setPrefWidth(280);
        errorPopup.setText("Warning");

        errorPopup.setContent(layout);

        popupStage.setResizable(false);
        tryAgainButton.setOnAction(event -> popupStage.hide());

        // Set the TitledPane as the content of the popup Stage
        StackPane container = new StackPane(errorPopup);
        Scene popupScene = new Scene(container);
        popupStage.setScene(popupScene);

        // Show the popup Stage
        popupStage.showAndWait();
    }


    /**
     * Displays an error popup with the given message.
     * The popup contains a text message and a "Try again" button.
     * Clicking the "Try again" button hides the popup.
     *
     * @param message The error message to be displayed.
     */
    public void displayError(String message) {
        Stage popupStage = new Stage();
        popupStage.initStyle(StageStyle.UNDECORATED);
        Text text = new Text(message);
        Button tryAgainButton = new Button("Try again");
        VBox.setMargin(tryAgainButton, new Insets(40, 0, 0, 182)); // Add margin to the button
        // Create the AnchorPane and add the content nodes
        VBox layout = new VBox(3);
        layout.getChildren().addAll(text, tryAgainButton);

        TitledPane errorPopup = new TitledPane();
        errorPopup.setAnimated(false);
        errorPopup.setLayoutX(197);
        errorPopup.setLayoutY(61);
        errorPopup.setPrefHeight(130);
        errorPopup.setPrefWidth(280);
        errorPopup.setText("Error");
        errorPopup.setContent(layout);

        popupStage.setResizable(false);
        tryAgainButton.setOnAction(event -> popupStage.hide());

        // Set the TitledPane as the content of the popup Stage
        StackPane container = new StackPane(errorPopup);
        Scene popupScene = new Scene(container);
        popupStage.setScene(popupScene);

        // Show the popup Stage
        popupStage.showAndWait();
    }
}
