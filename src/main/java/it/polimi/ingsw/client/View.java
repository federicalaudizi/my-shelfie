package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Player;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class View {
    HashMap<String, JSONObject> gameData;
    Client client;

    abstract void update(JSONObject gameState);

    abstract String confirmationPrompt(String message);

    abstract void okPrompt(String message);

    abstract void gameOverScreen(ArrayList<Player> leaderboard);

    abstract void gameOverScreen(String winnerUsername);
}