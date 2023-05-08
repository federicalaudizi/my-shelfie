package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

// TODO Implement "User has won objective" method

public abstract class View {
    Client client;

    abstract void update(Game game, LinkedList<String> playerOrder);

    abstract String confirmationPrompt(String message);

    abstract void okPrompt(String message);

    abstract void prompt(String message);

    abstract int choicePrompt(String message, String[] options);

    abstract String gameIdSelection(ArrayList<String> gameIds);

    abstract void gameOverScreen(HashMap<String, Integer> leaderboard);
}