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

    abstract int choicePrompt(String message, String[] options);

    abstract String gameIdSelection(ArrayList<String> gameIds);

    abstract void gameOverScreen(HashMap<String, Integer> leaderboard);
}