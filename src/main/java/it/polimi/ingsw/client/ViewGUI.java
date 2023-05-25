package it.polimi.ingsw.client;

import it.polimi.ingsw.client.scene.*;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.PersonalObjectiveCard;
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

    public ViewGUI(Client client) {
        FXMLLoader welcomeLoader = new FXMLLoader(getClass().getResource("/Welcome.fxml"));
        FXMLLoader connectLoader = new FXMLLoader(getClass().getResource("/Connection.fxml"));
        FXMLLoader nicknameLoader = new FXMLLoader(getClass().getResource("/Username.fxml"));
        FXMLLoader gameOptionsLoader = new FXMLLoader(getClass().getResource("/GameOptions.fxml"));
        FXMLLoader numberOfPlayersLoader = new FXMLLoader(getClass().getResource("/NumberOfPlayers.fxml"));
        FXMLLoader joinLoader = new FXMLLoader(getClass().getResource("/JoinGame.fxml"));
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/Board.fxml"));
        FXMLLoader networkErrorLoader = new FXMLLoader(getClass().getResource("/Network_error.fxml"));

        try {
            welcomeRoot = welcomeLoader.load();
            connectRoot = connectLoader.load();
            nicknameRoot = nicknameLoader.load();
            gameOptionsRoot = gameOptionsLoader.load();
            numberOfPlayersRoot = numberOfPlayersLoader.load();
            joinRoot = joinLoader.load();
            gameRoot = gameLoader.load();
            networkErrorRoot = networkErrorLoader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.welcomeController = welcomeLoader.getController();
        this.connectionController = connectLoader.getController();
        this.nicknameController = nicknameLoader.getController();
        this.gameOptionsController = gameOptionsLoader.getController();
        this.numberOfPlayersController = numberOfPlayersLoader.getController();
        this.joinController = joinLoader.getController();
        this.gameController = gameLoader.getController();
        this.client = client;
    }


    @Override
    void update(Game game, LinkedList<String> playerOrder) {
        if (game.isLastTurn()) {
        }
        setCommonObjectivesCards(game);
        setPersonalObjectiveCard(game);
        gameController.setShelves(game);
        gameController.initializeBoard(game);
        //gameController.initializeShelves(game);
        Platform.runLater(() -> {
            Gui.getStage().setTitle(client.getUsername());
            Gui.getStage().setScene(new Scene(gameRoot));
        });
    }


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

    @Override
    void gameOverScreen(JSONArray leaderboard) {
        // TODO Implement
    }

    @Override
    boolean continueScreen() {
        // TODO Implement
        return false;
    }

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

    @Override
    int getPlayerNumber() {
        Platform.runLater(() -> Gui.getStage().setScene(new Scene(numberOfPlayersRoot)));
        int numOfPlayers;
        try {
            numOfPlayers = (int) queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        return numOfPlayers;
    }

    @Override
    String getTiles() {
        gameController.ableView();
        String r = null;
        try {
            r = (String) queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameController.disableView();
        return r;
    }

    @Override
    int getColumn() {
        return 0;
    }

    @Override
    void showAchievement(String username, int objectiveNumber) {

    }

    @Override
    void showError(String errorMessage) {
        //TODO: distinguere tipo di errore con display error diversi
        if (errorMessage.equals("You entered a malformed IP:port combo. Retry.") || errorMessage.equals("The host does not exist. Retry.") || errorMessage.equals("Something went wrong.")) {
            Platform.runLater(connectionController::displayError);
        } else if (errorMessage.equals("Network error: you were disconnected from the server. Try selecting the reconnect option in the main menu.")) {
            Platform.runLater(() -> Gui.getStage().setScene(new Scene(networkErrorRoot)));
        }
    }
}
