package it.polimi.ingsw.client;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public abstract class View {
    Client client;

    abstract void update(HashMap<String, JSONArray> gameData, LinkedList<String> playerOrder, boolean lastTurn, int achievement);

    abstract String confirmationPrompt(String message);

    abstract void okPrompt(String message);

    abstract int choicePrompt(String message, String[] options);

    abstract String gameIdSelection(ArrayList<String> gameIds);

    abstract void gameOverScreen(HashMap<String, Integer> leaderboard);
}