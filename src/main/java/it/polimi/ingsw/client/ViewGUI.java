package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Player;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewGUI extends View {
    Client client;
    public ViewGUI(Client client) {
        this.client = client;
    }

    @Override
    void update(JSONObject gameState) {
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
    void gameOverScreen(ArrayList<Player> leaderboard) {
        // TODO Implement
    }

    @Override
    void gameOverScreen(String winnerUsername) {
        // TODO Implement
    }
}
