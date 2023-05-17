package it.polimi.ingsw.client;

import it.polimi.ingsw.client.scene.ConnectionController;
import it.polimi.ingsw.client.scene.GameOptionsController;
import it.polimi.ingsw.client.scene.NicknameController;
import it.polimi.ingsw.client.scene.WelcomeController;
import it.polimi.ingsw.server.model.Game;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;


public class ViewGUI extends View {
    Client client;
    private final NicknameController nicknameController;
    private final WelcomeController welcomeController;
    private final ConnectionController connectionController;
    private final GameOptionsController gameOptionsController;
    public static Parent welcomeRoot;
    public static Parent gameOptionsRoot;
    public static Parent connectRoot;
    public static Parent nicknameRoot;

    public ViewGUI(Client client) {
        FXMLLoader welcomeLoader = new FXMLLoader(getClass().getResource("/Welcome.fxml"));
        FXMLLoader connectLoader = new FXMLLoader(getClass().getResource("/Connection.fxml"));
        FXMLLoader nicknameLoader = new FXMLLoader(getClass().getResource("/Username.fxml"));


        try {
            welcomeRoot = welcomeLoader.load();
            connectRoot = connectLoader.load();
            nicknameRoot= nicknameLoader.load();
            gameOptionsRoot = gameOptionsLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.welcomeController = welcomeLoader.getController();
        this.connectionController = connectLoader.getController();
        this.nicknameController = nicknameLoader.getController();
        this.gameOptionsController = gameOptionsLoader.getController();
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
        CompletableFuture<String> future = new CompletableFuture<>();

        Platform.runLater(() -> future.complete(nicknameController.getNickname()));
        try {
            return future.get();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    int getGameOptions() {
        return 0;
    }

    @Override
    int getPlayerNumber() {
        return 0;
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

    }
}
