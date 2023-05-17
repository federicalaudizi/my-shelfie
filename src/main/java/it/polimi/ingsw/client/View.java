package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Game;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.SynchronousQueue;


// TODO Implement "User has won objective" method

public abstract class View {
    Client client;

    public static final SynchronousQueue<Object> queue = new SynchronousQueue<>();

    abstract void update(Game game, LinkedList<String> playerOrder);

    abstract String getIp();
    abstract String getUsername();
    abstract int getGameOptions();
    abstract int getPlayerNumber();
    abstract String getTiles();
    abstract int getColumn();
    abstract void showAchievement();
    abstract void showError(String errorMessage);

    abstract String gameIdSelection(ArrayList<String> gameIds);

    abstract void gameOverScreen(JSONArray leaderboard);
}