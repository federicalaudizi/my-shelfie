package it.polimi.ingsw.server.controller.network.rmi;

import it.polimi.ingsw.server.controller.GameSupervisor;
import it.polimi.ingsw.server.controller.network.ClientHandler;
import it.polimi.ingsw.server.controller.network.Message;
import it.polimi.ingsw.server.exceptions.PlayerDisconnectedException;
import it.polimi.ingsw.server.model.Coordinate;
import it.polimi.ingsw.server.model.Game;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static it.polimi.ingsw.server.controller.network.Message.Header.*;

public class RMIClientHandler extends ClientHandler {
    private final GameSupervisor ongoingGames;
    private final String thisPlayerId;

    private final Object heartbeatLock;
    private boolean isAlive;

    private boolean gameOver;

    private final Object pingLock;
    private boolean pingFlag;
    private Message pingMessage;

    RMIClientHandler(String username, GameSupervisor ongoingGames) {
        this.ongoingGames = ongoingGames;
        this.thisPlayerId = username;
        this.pingLock = new Object();
        this.heartbeatLock = new Object();
        this.isAlive = true;
        this.gameOver = false;

        this.pingFlag = false;
    }

    /**
     * This method starts the thread and executes all the logic of the client handler
     *
     * @author Federico
     */
    @Override
    public void run() {

    }

    /**
     * This method sends the first gamestate to the client
     *
     * @param gameState the gamestate to send to the client
     */
    @Override
    public void sendGameState(Game gameState) {

    }

    /**
     * This method signals the client that a response was accepted
     *
     * @author Federico
     */
    @Override
    public void sendOk() {

    }

    /**
     * This method asks the client to select a set of tiles
     *
     * @return an array of tiles
     * @author Federico
     */
    @Override
    public Coordinate[] getTiles() {
        return new Coordinate[0];
    }

    /**
     * This method signals the client that the selected tiles are not valid
     *
     * @author Federico
     */
    @Override
    public void badTile() {

    }

    /**
     * This method asks the client to select a column
     *
     * @return the column selected by the client
     * @author Federico
     */
    @Override
    public int getColumn() {
        return 0;
    }

    /**
     * This method signals the client that the selected column is not valid
     *
     * @author Federico
     */
    @Override
    public void badColumn() {

    }

    /**
     * This method signals the client that the game has ended
     *
     * @param leaderboard a JSON object containing the leaderboard
     * @author Federico
     */
    @Override
    public void gameOver(HashMap<String, Integer> leaderboard) {

    }
}
