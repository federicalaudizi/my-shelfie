package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.server.exceptions.PlayerDisconnectedException;
import it.polimi.ingsw.server.model.Coordinate;
import it.polimi.ingsw.server.model.*;

import java.util.HashMap;

/**
 * This class handles the exchange of messages with the client and runs as a thread.
 * This class represents che client on the server side, being a thread, after its initialization, it will run on command by the game controller it is associated to.
 * The logic behind it is that it first handshakes with the client, handles its login, then deals with the player the creation or joining of a game.
 * After that, it will be at the service of the game controller, which will send the commands that the player has to execute.
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
     * This method sends the first gamestate to the client
     *
     * @param gameState the gamestate to send to the client
     */
    public abstract void sendGameState(Game gameState);

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
    public abstract Coordinate[] getTiles() throws PlayerDisconnectedException;

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
    public abstract int getColumn() throws PlayerDisconnectedException;

    /**
     * This method signals the client that the selected column is not valid
     *
     * @author Federico
     */
    public abstract void badColumn();

    /**
     * This method signals the client that the game has ended
     *
     * @param leaderboard a JSON object containing the leaderboard
     * @author Federico
     */
    public abstract void gameOver(HashMap<String, Integer> leaderboard);
}
