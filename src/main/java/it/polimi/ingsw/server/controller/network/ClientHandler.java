package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.server.exceptions.PlayerDisconnectedException;
import it.polimi.ingsw.server.model.Coordinate;
import it.polimi.ingsw.server.model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static it.polimi.ingsw.server.controller.network.Message.Header.*;

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
     * This method sends the first gamestate to the client
     *
     * @param gameState the gamestate to send to the client
     * @param player the player that completed the objective
     * @param gainedObjective 1 if the player gained the first objective, 2 if the player gained the second objective, 3 if the player gained both objectives
     */
    public abstract void sendGameState(Game gameState, String player, int gainedObjective);

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

    /**
     * Helper method to parse the tiles from a message
     *
     * @param tilesMessage the message containing the tiles
     * @return an array of coordinates
     * @throws WrongHeaderException if the message is not valid
     */
    protected Coordinate[] parseTiles(Message tilesMessage) throws WrongHeaderException{
        if(tilesMessage.getHeaderCode() == SEND_TILES.getCode()){
            JSONArray args = tilesMessage.getBody();

            Coordinate[] tiles = new Coordinate[args.length()];

            for(int i = 0; i < args.length(); i++){
                tiles[i] = new Coordinate(args.getJSONObject(i));
            }

            return tiles;
        } else {
            // The response was not valid, ask again
            throw new WrongHeaderException();
        }
    }

    /**
     * Helper method to parse the column from a message
     *
     * @param columnMessage the message containing the column
     * @return the column
     * @throws WrongHeaderException if the message is not valid
     */
    protected int parseColumn(Message columnMessage) throws WrongHeaderException{
        if(columnMessage.getHeaderCode() == SEND_COLUMN.getCode()){

            JSONArray args = columnMessage.getBody();
            JSONObject column = args.getJSONObject(0);

            return column.getInt("column");
        } else {
            throw new WrongHeaderException();
        }
    }

    /**
     * Helper method to parse the leaderboard from a message
     *
     * @param leaderboard the hashmap containing the leaderboard
     * @return a JSON array containing the leaderboard
     */
    protected JSONArray parseLeaderboard(HashMap<String, Integer> leaderboard){
        // TODO: Leaderboard has to be sent ordered
        JSONArray leaderboardJson = new JSONArray();

        for(String player : leaderboard.keySet()){
            JSONObject playerScore = new JSONObject();
            playerScore.put("username", player);
            playerScore.put("points", leaderboard.get(player));
            leaderboardJson.put(playerScore);
        }

        return leaderboardJson;
    }

    protected static class WrongHeaderException extends Exception {
    }
}
