package it.polimi.ingsw.client;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class ViewGUI extends View {
    Client client;
    public ViewGUI(Client client) {
        this.client = client;
    }

    @Override
    void update(HashMap<String, JSONArray> gameData, LinkedList<String> playerOrder, boolean lastTurn, int achievement) {
        // TODO Implement
    }

    @Override
    String confirmationPrompt(String message) {
        // TODO Implement
        return null;
    }

    @Override
    void okPrompt(String message) {
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
}
