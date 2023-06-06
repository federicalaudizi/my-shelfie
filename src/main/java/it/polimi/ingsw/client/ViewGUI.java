package it.polimi.ingsw.client;

import it.polimi.ingsw.client.scene.*;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.PersonalObjectiveCard;
import it.polimi.ingsw.server.model.PointCard;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import org.json.JSONArray;

import java.io.IOException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;


public class ViewGUI extends View {
    Client client;
    private final JoinController joinController;
    private final NicknameController nicknameController;
    private final WelcomeController welcomeController;
    private final ConnectionController connectionController;
    private final GameOptionsController gameOptionsController;
    private final NumberOfPlayersController numberOfPlayersController;
    private final BoardController gameController;
    public static Parent welcomeRoot;
    public static Parent networkErrorRoot;
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
        this.welcomeController = welcomeLoader.getController();
        this.connectionController = connectLoader.getController();
        this.nicknameController = nicknameLoader.getController();
        this.gameOptionsController = gameOptionsLoader.getController();
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
            Platform.runLater(() -> gameController.instruction.setText("It's the last turn! Make your last move."));
        }
        Platform.runLater(() -> gameController.instruction.setText(""));
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
                Scene newScene = new Scene(gameRoot);
                Gui.getStage().setScene(newScene);
            }
        });
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
        PointCard[] pointcards = game.getPlayerByUsername(playerOrder.get(1)).getPointCards();
        PointCard card = pointcards[0];
        PointCard card2 = pointcards[1];

        String stream;
        Image image;
        if (card.getValue() == 0) {
            return;
        } else if (card.getValue() == 8) {
            stream = "file:src/main/resources/Images/scoring_8.jpg";
            image = new Image(stream);
            gameController.scoreFirst1.setImage(image);
        } else if (card.getValue() == 6) {
            stream = "file:src/main/resources/Images/scoring_6.jpg";
            image = new Image(stream);
            gameController.scoreFirst1.setImage(image);
        } else if (card.getValue() == 4) {
            stream = "file:src/main/resources/Images/scoring_4.jpg";
            image = new Image(stream);
            gameController.scoreFirst1.setImage(image);
        } else {
            stream = "file:src/main/resources/Images/scoring_2.jpg";
            image = new Image(stream);
            gameController.scoreFirst1.setImage(image);
        }


        if (card2.getValue() == 0) {
            return;
        } else if (card2.getValue() == 8) {
            stream = "file:src/main/resources/Images/scoring_8.jpg";
            image = new Image(stream);
            gameController.scoreSecond1.setImage(image);
        } else if (card2.getValue() == 6) {
            stream = "file:src/main/resources/Images/scoring_6.jpg";
            image = new Image(stream);
            gameController.scoreSecond1.setImage(image);
        } else if (card2.getValue() == 4) {
            stream = "file:src/main/resources/Images/scoring_4.jpg";
            image = new Image(stream);
            gameController.scoreSecond1.setImage(image);
        } else {
            stream = "file:src/main/resources/Images/scoring_2.jpg";
            image = new Image(stream);
            gameController.scoreSecond1.setImage(image);
        }

        if (game.getNumberOfPlayers() == 3) {
            pointcards = game.getPlayerByUsername(playerOrder.get(2)).getPointCards();
            card = pointcards[0];
            card2 = pointcards[1];

            if (card.getValue() == 0) {
                return;
            } else if (card.getValue() == 8) {
                stream = "file:src/main/resources/Images/scoring_8.jpg";
                image = new Image(stream);
                gameController.scoreFirst2.setImage(image);
            } else if (card.getValue() == 6) {
                stream = "file:src/main/resources/Images/scoring_6.jpg";
                image = new Image(stream);
                gameController.scoreFirst2.setImage(image);
            } else if (card.getValue() == 4) {
                stream = "file:src/main/resources/Images/scoring_4.jpg";
                image = new Image(stream);
                gameController.scoreFirst2.setImage(image);
            } else {
                stream = "file:src/main/resources/Images/scoring_2.jpg";
                image = new Image(stream);
                gameController.scoreFirst2.setImage(image);
            }


            if (card2.getValue() == 0) {
                return;
            } else if (card2.getValue() == 8) {
                stream = "file:src/main/resources/Images/scoring_8.jpg";
                image = new Image(stream);
                gameController.scoreSecond2.setImage(image);
            } else if (card2.getValue() == 6) {
                stream = "file:src/main/resources/Images/scoring_6.jpg";
                image = new Image(stream);
                gameController.scoreSecond2.setImage(image);
            } else if (card2.getValue() == 4) {
                stream = "file:src/main/resources/Images/scoring_4.jpg";
                image = new Image(stream);
                gameController.scoreSecond2.setImage(image);
            } else {
                stream = "file:src/main/resources/Images/scoring_2.jpg";
                image = new Image(stream);
                gameController.scoreSecond2.setImage(image);
            }
        } else if (game.getNumberOfPlayers() == 3) {
            pointcards = game.getPlayerByUsername(playerOrder.get(3)).getPointCards();
            card = pointcards[0];
            card2 = pointcards[1];

            if (card.getValue() == 0) {
                return;
            } else if (card.getValue() == 8) {
                stream = "file:src/main/resources/Images/scoring_8.jpg";
                image = new Image(stream);
                gameController.scoreFirst3.setImage(image);
            } else if (card.getValue() == 6) {
                stream = "file:src/main/resources/Images/scoring_6.jpg";
                image = new Image(stream);
                gameController.scoreFirst3.setImage(image);
            } else if (card.getValue() == 4) {
                stream = "file:src/main/resources/Images/scoring_4.jpg";
                image = new Image(stream);
                gameController.scoreFirst3.setImage(image);
            } else {
                stream = "file:src/main/resources/Images/scoring_2.jpg";
                image = new Image(stream);
                gameController.scoreFirst3.setImage(image);
            }


            if (card2.getValue() == 0) {
                return;
            } else if (card2.getValue() == 8) {
                stream = "file:src/main/resources/Images/scoring_8.jpg";
                image = new Image(stream);
                gameController.scoreSecond3.setImage(image);
            } else if (card2.getValue() == 6) {
                stream = "file:src/main/resources/Images/scoring_6.jpg";
                image = new Image(stream);
                gameController.scoreSecond3.setImage(image);
            } else if (card2.getValue() == 4) {
                stream = "file:src/main/resources/Images/scoring_4.jpg";
                image = new Image(stream);
                gameController.scoreSecond3.setImage(image);
            } else {
                stream = "file:src/main/resources/Images/scoring_2.jpg";
                image = new Image(stream);
                gameController.scoreSecond3.setImage(image);
            }
        }
    }

    private void setPointCard(PointCard card, int i) {
        String stream;
        Image image;
        if (i == 1) {
            if (card.getValue() == 0) {
                return;
            } else if (card.getValue() == 8) {
                stream = "file:src/main/resources/Images/scoring_8.jpg";
                image = new Image(stream);
                gameController.scoreFirst.setImage(image);
            } else if (card.getValue() == 6) {
                stream = "file:src/main/resources/Images/scoring_6.jpg";
                image = new Image(stream);
                gameController.scoreFirst.setImage(image);
            } else if (card.getValue() == 4) {
                stream = "file:src/main/resources/Images/scoring_4.jpg";
                image = new Image(stream);
                gameController.scoreFirst.setImage(image);
            } else {
                stream = "file:src/main/resources/Images/scoring_2.jpg";
                image = new Image(stream);
                gameController.scoreFirst.setImage(image);
            }
        } else {
            if (card.getValue() == 0) {
                return;
            } else if (card.getValue() == 8) {
                stream = "file:src/main/resources/Images/scoring_8.jpg";
                image = new Image(stream);
                gameController.scoreSecond.setImage(image);
            } else if (card.getValue() == 6) {
                stream = "file:src/main/resources/Images/scoring_6.jpg";
                image = new Image(stream);
                gameController.scoreSecond.setImage(image);
            } else if (card.getValue() == 4) {
                stream = "file:src/main/resources/Images/scoring_4.jpg";
                image = new Image(stream);
                gameController.scoreSecond.setImage(image);
            } else {
                stream = "file:src/main/resources/Images/scoring_2.jpg";
                image = new Image(stream);
                gameController.scoreSecond.setImage(image);
            }
        }
    }

    /**
     * Sets the personal objective card image for the current player in the game controller.
     *
     * @param game The current game instance.
     */
    public void setPersonalObjectiveCard(Game game) {
        String stream;
        Image image;
        PersonalObjectiveCard card = game.getPlayerByUsername(client.getUsername()).getObjective();
        if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.FIRST_PATTERN) {
            stream = "file:src/main/resources/Images/one_po.png";
            image = new Image(stream);
            gameController.personalCard.setImage(image);
        } else if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.SECOND_PATTERN) {
            stream = "file:src/main/resources/Images/two_po.png";
            image = new Image(stream);
            gameController.personalCard.setImage(image);
        } else if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.THIRD_PATTERN) {
            stream = "file:src/main/resources/Images/three_po.png";
            image = new Image(stream);
            gameController.personalCard.setImage(image);
        } else if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.FOURTH_PATTERN) {
            stream = "file:src/main/resources/Images/four_po.png";
            image = new Image(stream);
            gameController.personalCard.setImage(image);
        } else if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.FIFTH_PATTERN) {
            stream = "file:src/main/resources/Images/five_po.png";
            image = new Image(stream);
            gameController.personalCard.setImage(image);
        } else if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.SIXTH_PATTERN) {
            stream = "file:src/main/resources/Images/six_po.png";
            image = new Image(stream);
            gameController.personalCard.setImage(image);
        } else if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.SEVENTH_PATTERN) {
            stream = "file:src/main/resources/Images/seven_po.png";
            image = new Image(stream);
            gameController.personalCard.setImage(image);
        } else if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.EIGHTH_PATTERN) {
            stream = "file:src/main/resources/Images/eight_po.png";
            image = new Image(stream);
            gameController.personalCard.setImage(image);
        } else if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.NINTH_PATTERN) {
            stream = "file:src/main/resources/Images/nine_po.png";
            image = new Image(stream);
            gameController.personalCard.setImage(image);
        } else if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.TENTH_PATTERN) {
            stream = "file:src/main/resources/Images/ten_po.png";
            image = new Image(stream);
            gameController.personalCard.setImage(image);
        } else if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.ELEVENTH_PATTERN) {
            stream = "file:src/main/resources/Images/eleven_po.png";
            image = new Image(stream);
            gameController.personalCard.setImage(image);
        } else if (card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.TWELFTH_PATTERN) {
            stream = "file:src/main/resources/Images/twelve_po.png";
            image = new Image(stream);
            gameController.personalCard.setImage(image);
        }
    }

    /**
     * Sets the images for common objective cards in the game.
     *
     * @param game The Game object representing the current game.
     */
    public void setCommonObjectivesCards(Game game) {
        String stream;
        Image image;

        String[] string = game.getObjectives();
        if (Objects.equals(string[0], "PatternOne")) {
            stream = "file:src/main/resources/Images/one.jpg";
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        } else if (Objects.equals(string[0], "PatternTwo")) {
            stream = "file:src/main/resources/Images/two.jpg";
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        } else if (Objects.equals(string[0], "PatternThree")) {
            stream = "file:src/main/resources/Images/three.jpg";
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        } else if (Objects.equals(string[0], "PatternFour")) {
            stream = "file:src/main/resources/Images/four.jpg";
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        } else if (Objects.equals(string[0], "PatternFive")) {
            stream = "file:src/main/resources/Images/five.jpg";
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        } else if (Objects.equals(string[0], "PatternSix")) {
            stream = "file:src/main/resources/Images/six.jpg";
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        } else if (Objects.equals(string[0], "PatternSeven")) {
            stream = "file:src/main/resources/Images/seven.jpg";
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        } else if (Objects.equals(string[0], "PatternEight")) {
            stream = "file:src/main/resources/Images/eight.jpg";
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        } else if (Objects.equals(string[0], "PatternNine")) {
            stream = "file:src/main/resources/Images/nine.jpg";
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        } else if (Objects.equals(string[0], "PatternTen")) {
            stream = "file:src/main/resources/Images/ten.jpg";
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        } else if (Objects.equals(string[0], "PatternEleven")) {
            stream = "file:src/main/resources/Images/eleven.jpg";
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        } else if (Objects.equals(string[0], "PatternTwelve")) {
            stream = "file:src/main/resources/Images/twelve.jpg";
            image = new Image(stream);
            gameController.commonCard1.setImage(image);
        }

        if (Objects.equals(string[1], "PatternOne")) {
            stream = "file:src/main/resources/Images/one.jpg";
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        } else if (Objects.equals(string[1], "PatternTwo")) {
            stream = "file:src/main/resources/Images/two.jpg";
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        } else if (Objects.equals(string[1], "PatternThree")) {
            stream = "file:src/main/resources/Images/three.jpg";
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        } else if (Objects.equals(string[1], "PatternFour")) {
            stream = "file:src/main/resources/Images/four.jpg";
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        } else if (Objects.equals(string[1], "PatternFive")) {
            stream = "file:src/main/resources/Images/five.jpg";
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        } else if (Objects.equals(string[1], "PatternSix")) {
            stream = "file:src/main/resources/Images/six.jpg";
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        } else if (Objects.equals(string[1], "PatternSeven")) {
            stream = "file:src/main/resources/Images/seven.jpg";
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        } else if (Objects.equals(string[1], "PatternEight")) {
            stream = "file:src/main/resources/Images/eight.jpg";
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        } else if (Objects.equals(string[1], "PatternNine")) {
            stream = "file:src/main/resources/Images/nine.jpg";
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        } else if (Objects.equals(string[1], "PatternTen")) {
            stream = "file:src/main/resources/Images/ten.jpg";
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        } else if (Objects.equals(string[1], "PatternEleven")) {
            stream = "file:src/main/resources/Images/eleven.jpg";
            image = new Image(stream);
            gameController.commonCard2.setImage(image);
        } else if (Objects.equals(string[1], "PatternTwelve")) {
            stream = "file:src/main/resources/Images/twelve.jpg";
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
        String selectedGame = null;
        Platform.runLater(() -> Gui.getStage().setScene(new Scene(joinRoot)));
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
        Platform.runLater(() -> Gui.getStage().setScene(new Scene(nicknameRoot)));
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
        Platform.runLater(() -> Gui.getStage().setScene(new Scene(gameOptionsRoot)));
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
        Platform.runLater(() -> Gui.getStage().setScene(new Scene(numberOfPlayersRoot)));
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
        gameController.boardPane.setDisable(false);
        gameController.continueButton.setDisable(false);
        Platform.runLater(() -> gameController.instruction.setText("Choose the tiles, then press continue."));
        gameController.boardPane.setDisable(false);
        String r = null;
        try {
            r = (String) queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameController.disableView();
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
        Platform.runLater(() -> {
            gameController.instruction.setText("Good choice, now choose the column.");
        });
        gameController.ableView();
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
        switch (errorMessage) {
            case "You entered a malformed IP:port combo. Retry.", "The host does not exist. Retry.", "Something went wrong.", "Network error: you were disconnected from the server. Try selecting the reconnect option in the main menu." ->
                    Platform.runLater(() -> connectionController.displayError(errorMessage));
            case "The column you chose is not valid", "You entered an invalid number of coordinates. Retry.", "The tiles you chose are not valid" ->
                    Platform.runLater(() -> gameController.displayError(errorMessage));
            case "Username is already taken" ->
                    Platform.runLater(() -> nicknameController.displayErrorNick(errorMessage));
        }
    }
}
