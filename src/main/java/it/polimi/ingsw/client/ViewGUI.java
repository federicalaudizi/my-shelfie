package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;



public class ViewGUI extends View {
    Client client;
    private NicknameController nicknameController;
    private ConnectController connectController;
    private WelcomeController welcomeController;
    protected static Parent connectRoot;
    protected static Parent nicknameRoot;

    public ViewGUI(Client client) {
        this.client = client;
        createControllers();
    }

    private void createControllers() {
        FXMLLoader connectLoader = new FXMLLoader(getClass().getResource("Connect.fxml"));
        FXMLLoader nicknameLoader = new FXMLLoader(getClass().getResource("Username.fxml"));

        try {
            connectRoot = connectLoader.load();
            nicknameRoot = nicknameLoader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        connectController = connectLoader.getController();
        nicknameController = nicknameLoader.getController();

    }



    @Override
    void update(Game game, LinkedList<String> playerOrder) {

    }

    @Override
    String confirmationPrompt(String message) {
        return null;
    }

    @Override
    void okPrompt(String message) {
        // TODO Implement
    }

    @Override
    void prompt(String message) {
        // TODO Implement
    }

    @Override
    int choicePrompt(String message, String[] options) {
        // TODO Implement
        return 0;
    }

    @Override
    String gameIdSelection(ArrayList<String> gameIds) {
        // TODO Implement
        return null;
    }

    @Override
    void gameOverScreen(HashMap<String, Integer> leaderboard) {
        // TODO Implement
    }

    public String getServerIp(){
        return connectController.connect();
    }

    public String getUsername(){
        return null;
    }
}
