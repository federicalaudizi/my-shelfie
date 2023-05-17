package it.polimi.ingsw.client;

import it.polimi.ingsw.client.scene.*;
import it.polimi.ingsw.server.model.Game;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;


public class ViewGUI extends View {
    Client client;
    private final NicknameController nicknameController;
    private final WelcomeController welcomeController;
    private final ConnectionController connectionController;
    private final GameOptionsController gameOptionsController;
    private final NumberOfPlayersController numberOfPlayersController;
    public static Parent welcomeRoot;
    public static Parent gameOptionsRoot;
    public static Parent connectRoot;
    public static Parent nicknameRoot;
    public static Parent numberOfPlayersRoot;

    public ViewGUI(Client client) {
        FXMLLoader welcomeLoader = new FXMLLoader(getClass().getResource("/Welcome.fxml"));
        FXMLLoader connectLoader = new FXMLLoader(getClass().getResource("/Connection.fxml"));
        FXMLLoader nicknameLoader = new FXMLLoader(getClass().getResource("/Username.fxml"));
        FXMLLoader gameOptionsLoader = new FXMLLoader(getClass().getResource("/GameOptions.fxml"));
        FXMLLoader numberOfPlayersLoader = new FXMLLoader(getClass().getResource("/NumberOfPlayers.fxml"));


        try {
            welcomeRoot = welcomeLoader.load();
            connectRoot = connectLoader.load();
            nicknameRoot= nicknameLoader.load();
            gameOptionsRoot = gameOptionsLoader.load();
            numberOfPlayersRoot = numberOfPlayersLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.welcomeController = welcomeLoader.getController();
        this.connectionController = connectLoader.getController();
        this.nicknameController = nicknameLoader.getController();
        this.gameOptionsController = gameOptionsLoader.getController();
        this.numberOfPlayersController = numberOfPlayersLoader.getController();
        this.client = client;
    }


    @Override
    void update(Game game, LinkedList<String> playerOrder) {

    }

    @Override
    String getIp() {
        String IP = null;
        try {
            IP = (String) queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> Gui.getStage().setScene(new Scene(nicknameRoot)));
        return IP;
    }

    @Override
    String gameIdSelection(ArrayList<String> gameIds) {
        // TODO Implement
        return null;
    }

    @Override
    void gameOverScreen(JSONArray leaderboard) {
        // TODO Implement
    }

    @Override
    public String getUsername() {
        String username = null;
        try {
            username = (String) queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> Gui.getStage().setScene(new Scene(gameOptionsRoot)));
        return username;
    }

    @Override
    int getGameOptions() {
        int choice;
        try{
            choice = (int) queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (choice == 1){
            Platform.runLater(() -> Gui.getStage().setScene(new Scene(numberOfPlayersRoot)));
        } else if (choice == 2) {
            //TODO cosa succede quando scegli "Join game"
        }else{
            //Todo cosa succede quando "connect to an ongoing game"
        }
        return choice;
    }

    @Override
    int getPlayerNumber() {
        int numOfPlayers;
        try {
            numOfPlayers = (int) queue.take();
        }catch (InterruptedException e){
            throw new RuntimeException();
        }
        return numOfPlayers;
    }

    @Override
    String getTiles() {
        return null;
    }

    @Override
    int getColumn() {
        return 0;
    }

    @Override
    void showAchievement() {

    }

    @Override
    void showError(String errorMessage) {
        //TODO: distinguere tipo di errore con display error diversi
        if(errorMessage.equals("You entered a malformed IP:port combo. Retry.") || errorMessage.equals("The host does not exist. Retry.") || errorMessage.equals("Something went wrong.")){
            connectionController.displayError();
        }
    }
}
