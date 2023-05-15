package it.polimi.ingsw.client;

import it.polimi.ingsw.client.scene.NicknameController;
import it.polimi.ingsw.client.scene.WelcomeController;
import it.polimi.ingsw.server.model.Game;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;



public class ViewGUI extends View {
    Client client;
    private NicknameController nicknameController;
    private WelcomeController welcomeController;
    protected static Parent welcomeRoot;
    protected static Parent connectRoot;
    protected static Parent nicknameRoot;

    public ViewGUI(Client client) {
        FXMLLoader welcomeLoader = new FXMLLoader(getClass().getResource("/Welcome.fxml"));

        try {
            welcomeRoot = welcomeLoader.load();
        }catch (IOException e){
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
        return welcomeController.connect();
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
        try {
            return (String) queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
