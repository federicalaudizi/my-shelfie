package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Game;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.SynchronousQueue;

public abstract class View {
    Client client;

    public static final SynchronousQueue<Object> queue = new SynchronousQueue<>();

    /**
     * This method is used to update the view with the new game data the client receives at every turn.
     * @param game The updated game data
     * @param playerOrder A list containing the players in the game with the player associated to the active client
     *                    in the first position
     * @author Mario Merlo
     */
    abstract void update(Game game, LinkedList<String> playerOrder);

    /**
     * This method is used to get the server IP input by the user from the view to the client.
     * @return A String representing the server's IP address
     * @author Mario Merlo
     */
    abstract String getIp();

    /**
     * This method is used to get the player's username from the view.
     * @return The username in String form
     * @author Mario Merlo
     */
    abstract String getUsername();

    /**
     * This method is used to ask the player whether they want to create a new game, join one or reconnect to an
     * ongoing game.
     * @return The integer representing the user's choice
     * @author Mario Merlo
     */
    abstract int getGameOptions();

    /**
     * This method is used to ask the player creating a new game the number of players they would like to have in
     * the game.
     * @return The selected number of players
     * @author Mario Merlo
     */
    abstract int getPlayerNumber();

    /**
     * This method is used to ask the user what tiles they would like to get from the board
     * @return A String representation of the selected tiles
     * @author Mario Merlo
     */
    abstract String getTiles();

    /**
     * This method is used to ask the user in what column of their shelf they would like to put their selected tiles.
     * @return The integer representing the selected column of the shelf
     * @author Mario Merlo
     */
    abstract int getColumn();

    /**
     * This method is used to notify the player when another player or themselves won a common objective.
     * @param username The username of the player that won the objective
     * @param objectiveNumber The integer representing what objective was won
     * @author Mario Merlo
     */
    abstract void showAchievement(String username, int objectiveNumber);

    /**
     * This method is used to notify the player when an error occurs. Specifically, this method should show an error
     * when the response received by the server is an error message.
     * @param errorMessage The message explaining the error
     * @author Mario Merlo
     */
    abstract void showError(String errorMessage);

    /**
     * This method enables the user to choose a game ID from a list shown on screen.
     * @param gameIds The list of game IDs currently available on the server
     * @return The String representing one of the game IDs
     * @author Mario Merlo
     */
    abstract String gameIdSelection(ArrayList<String> gameIds);

    /**
     * This method is called when the game is over and shows the user a leaderboard with the points obtained by every
     * player in the game.
     * @param leaderboard The leaderboard for the current game
     * @author Mario Merlo
     */
    abstract void gameOverScreen(JSONArray leaderboard);

    /**
     * This method shows a screen that lets the user choose whether they would like to continue playing or exit the game
     * once a game is over.
     * @return true if the player decides to continue playing, false otherwise
     * @author Mario Merlo
     */
    abstract boolean continueScreen();

    /**
     * This method notifies the player when a user gets disconnected from the game.
     * @param username The username of the disconnected user
     */
    abstract void showDisconnection(String username);

    /**
     * This method notifies the player when they are disconnected due to inactivity.
     */
    abstract void showServerDisconnection();
}