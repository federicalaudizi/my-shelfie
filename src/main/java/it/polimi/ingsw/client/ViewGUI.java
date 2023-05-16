package it.polimi.ingsw.client;

import it.polimi.ingsw.client.scene.ConnectionController;
import it.polimi.ingsw.client.scene.NicknameController;
import it.polimi.ingsw.client.scene.WelcomeController;
import it.polimi.ingsw.server.model.Game;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;


public class ViewGUI extends View {
    Client client;
    private NicknameController nicknameController;
    private final WelcomeController welcomeController;
    private final ConnectionController controller;
    protected static Parent welcomeRoot;
    protected  Parent connectRoot;
    protected static Parent nicknameRoot;

    public ViewGUI(Client client) {
        FXMLLoader welcomeLoader = new FXMLLoader(getClass().getResource("/Welcome.fxml"));
        controller = new ConnectionController();

        try {
            welcomeRoot = welcomeLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.welcomeController = welcomeLoader.getController();
        this.client = client;
    }


    @Override
    void update(Game game, LinkedList<String> playerOrder) {

    }

    @Override
    String getIp() {
        //To handle the result of the asynchronous operation

        CompletableFuture<String> future = new CompletableFuture<>();
        controller.handleMouseClickForIp();

        Platform.runLater(() -> future.complete(controller.handleMouseClickForIp()));
        try {
            return future.get();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
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
