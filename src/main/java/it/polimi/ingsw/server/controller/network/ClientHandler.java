package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.server.model.Coordinate;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class handles the exchange of messages with the client and runs as a thread.
 * This class represents che client on the server side, being a thread, after its initialization, it will run on command by the game controller it is associated to.
 * The logic behind it is that it first handshakes with the client, handles its login, then deals with the player the creation or joining of a game.
 * After that, it will be at the disposal of the game controller, which will send the commands that the player has to execute.
 *
 * @author Federico
 */
public abstract class ClientHandler implements Runnable{
    /**
     * This method starts the thread and executes all the logic of the client handler
     *
     * @author Federico
     */
    public abstract void run();

    /**
     * This method signals the client handler to send the game state to the client
     *
     * @param gameState the game state to be sent packetized as a JSON object
     * @throws IOException if an error occurs when sending the message to the client
     * @author Federico
     */
    public abstract void sendGameState(JSONObject gameState) throws IOException;

    /**
     * This method signals the client handler to send the game state to the client and that an objective has been completed
     *
     * @param gameState the game state to be sent packetized as a JSON object
     * @param collectiveObjectiveNumber the number of the collective objective that has been completed
     * @throws IOException if an error occurs when sending the message to the client
     * @author Federico
     */
    public abstract void sendGameState(JSONObject gameState, int collectiveObjectiveNumber) throws IOException;

    /**
     * This method signals the client handler to update the board and the players
     *
     * @param board the board to be sent packetized as a JSON object
     * @param players an array of players to be sent packetized as a JSON object
     * @throws IOException if an error occurs when sending the message to the client
     *
     * @author Federico
     */
    public abstract void sendGameState(JSONObject board, ArrayList<JSONObject> players) throws IOException;

    /**
     * This method signals the client that a response was accepted
     *
     * @author Federico
     */
    public abstract void sendOk();

    /**
     * This method asks the client to select a set of tiles
     *
     * @return an array of tiles
     * @author Federico
     */
    public abstract Coordinate[] getTiles();

    /**
     * This method signals the client that the selected tiles are not valid
     *
     * @author Federico
     */
    public abstract void badTile();

    /**
     * This method asks the client to select a column
     *
     * @return the column selected by the client
     * @author Federico
     */
    public abstract int getColumn();

    /**
     * This method signals the client that the selected column is not valid
     *
     * @author Federico
     */
    public abstract void badColumn();

    /**
     * This method asks the client to select a row
     *
     * @param leaderboard a JSON object containing the leaderboard
     * @author Federico
     */
    public abstract void gameOver(JSONObject leaderboard);
}
