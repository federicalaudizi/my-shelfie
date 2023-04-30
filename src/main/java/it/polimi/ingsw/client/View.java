package it.polimi.ingsw.client;

import org.json.JSONObject;

import java.util.HashMap;

public abstract class View {
    HashMap<String, JSONObject> gameData;
    Client client;

    abstract void update(JSONObject gameState);

    abstract String confirmationPrompt(String message);

    abstract void okPrompt(String message);
}