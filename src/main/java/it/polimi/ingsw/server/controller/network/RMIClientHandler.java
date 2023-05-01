package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.server.model.Coordinate;
import org.json.JSONObject;

import java.io.IOException;

public class RMIClientHandler extends ClientHandler {
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
     * @throws IOException if an error occurs with the data stream
     */
    @Override
    public void sendGameState(JSONObject gameState) throws IOException {

    }

    /**
     * This method sends the updates of the gamestate to the client at the end of each player's turn
     *
     * @param board           the board of the game
     * @param player          the player who just played
     * @param pointDeckValues the values of the point decks
     * @throws IOException if an error occurs with the data stream
     */
    @Override
    public void sendGameState(JSONObject board, JSONObject player, int[] pointDeckValues) throws IOException {

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
    public void gameOver(JSONObject leaderboard) {

    }

    /**
     * This method signals the client that the game has ended
     *
     * @param winner the playerId of the winner
     * @author Federico
     */
    @Override
    public void gameOver(String winner) {

    }
}
