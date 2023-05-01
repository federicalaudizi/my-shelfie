package it.polimi.ingsw.server.controller;


import it.polimi.ingsw.server.controller.network.ClientHandler;
import it.polimi.ingsw.server.exceptions.*;
import it.polimi.ingsw.server.model.Coordinate;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Tile;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Game Controller
 *
 * @author Federica & Sara
 */

public class GameController implements Runnable {

    private final Object lock = new Object();
    private final Game game;
    private final Map<String, ClientHandler> playerToClientHandlerMap;
    private Map<String, Integer> connectedPlayers;
    private ArrayList<String> turnOrder;
    private final String gameId;
    private boolean isOver;
    private int currentTurnIndex;


    GameController(int playerNumber, String gameId) {
        playerToClientHandlerMap = new HashMap<>();
        this.gameId = gameId;
        game = new Game(playerNumber);
        isOver = false;
        turnOrder = new ArrayList<>();
        currentTurnIndex = game.getCurrentPlayerIndex();
        connectedPlayers = new HashMap<>();
    }

    /**
     * called by the ClientHandler to modify the connection status of the player in disconnected
     *
     * @param playerId of which ClientHandler needs to modify the status
     */
    public void notifyDisconnection(String playerId) {
        connectedPlayers.put(playerId, 0);
    }

    /**
     * called by the ClientHandler to modify the connection status of the player in connected
     *
     * @param playerId of which ClientHandler needs to modify the status
     */
    public void notifyConnection(String playerId) {
        connectedPlayers.put(playerId, 1);
    }

    /**
     * Adds the player to the map of connected players and to the map with their connection status
     *
     * @param playerId the nickname of each player
     * @param handler  the ClientHandler connected to that playerId
     * @throws ReachedMaxNumberOfPlayers when the number of players reaches the number chosen at
     *                                   the beginning of the play
     */
    public void addPlayer(String playerId, ClientHandler handler) throws ReachedMaxNumberOfPlayers {
        if (playerToClientHandlerMap.size() >= game.getNumberOfPlayers()) {
            throw new ReachedMaxNumberOfPlayers();
        }
        connectedPlayers.put(playerId, 1);
        playerToClientHandlerMap.put(playerId, handler);
    }

    /**
     * sets the player's username in the game
     * */
    void setUsernames() {
        ArrayList<String> usernames = new ArrayList<>(playerToClientHandlerMap.keySet());
        game.setUsernames(usernames);
    }

    /**
     * @param playerId of the client handler
     * @return Client Handler referred to that playerId
     */
    ClientHandler getClientHandler(String playerId) {
        return playerToClientHandlerMap.get(playerId);
    }

    /**
     * This method decides who's next turn modifying the currentTurnIndex.
     * If it is the last turn the game goes on until it reaches the last player
     */
    void turnHandler() {
        if (currentTurnIndex % (turnOrder.size() - 1) != 0 && !game.isLastTurn()) {
            currentTurnIndex = (currentTurnIndex + 1) % turnOrder.size();
            game.nextTurn();
        } else {
            isOver = true;
            for(String currentPlayer: turnOrder){
            getClientHandler(currentPlayer).gameOver((JSONObject) game.getRankedPlayers());
            }
            game.nextTurn();
        }
    }

    @Override
    public void run() {
        setUsernames();
        turnOrder = game.getPlayerId();
        for (String currentPlayerId : turnOrder) {
            try {
                getClientHandler(currentPlayerId).sendGameState(game.toJson());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        while (!isOver) {
            int column;
            Tile[] tiles = new Tile[0];
            Coordinate[] coordinates;
            String currentPlayerId = turnOrder.get(currentTurnIndex);
            //if the player is not connected and the number of connected players is greater than 0 then
            // the turn passes automatically to the next player
            if (connectedPlayers.get(currentPlayerId) == 0 && connectedPlayers.values().stream().filter(value -> value == 1).count() > 1) {
                turnHandler();
                continue;
                //If there is only one player connected starts a timer.
                //If no player has connected before this timer reaches zero, then the game automatically ends;
                // otherwise, it continues as soon as a second player reconnects.
            } else if (connectedPlayers.values().stream().filter(value -> value == 1).count() == 1) {
                try {
                    // Wait for the condition to be verified, with a timeout of 15 seconds
                    synchronized (lock) {
                        long startTime = System.currentTimeMillis();
                        long elapsedTime = 0;
                        while (connectedPlayers.values().stream().filter(value -> value == 1).count() <= 1 && elapsedTime < 15000) {
                            lock.wait(5000 - elapsedTime);
                            elapsedTime = System.currentTimeMillis() - startTime;
                        }
                        //if there is only one player connected he is the winner
                        if (connectedPlayers.values().stream().filter(value -> value == 1).count() <= 1) {
                            getClientHandler(currentPlayerId).gameOver(currentPlayerId);
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            boolean exceptionThrown = true;
            while (exceptionThrown) {
                coordinates = getClientHandler(currentPlayerId).getTiles();
                try {
                    tiles = game.chooseTiles(coordinates[0], coordinates[1], coordinates[2]);
                    getClientHandler(currentPlayerId).sendOk();
                    exceptionThrown = false;
                } catch (TileUnpickableException e) {
                    getClientHandler(currentPlayerId).badTile();
                    throw new RuntimeException(e);
                }
            }

            exceptionThrown = true;
            while (exceptionThrown) {
                column = getClientHandler(currentPlayerId).getColumn();
                getClientHandler(currentPlayerId).sendOk();

                try {
                    int numCollObj = game.insertInShelf(column, tiles);

                    if (numCollObj == 0) {
                        for (String currentPlayer : turnOrder) {
                            //TODO: sendGameState signature has been changed
                        }
                    } else {
                        for (String currentPlayer : turnOrder) {
                            //TODO: sendGameState signature has been changed
                        }
                    }

                    getClientHandler(currentPlayerId).sendOk();
                    exceptionThrown = false;
                } catch (tooManyTilesException | notEnoughTilesException | fullColumnException e) {
                    getClientHandler(currentPlayerId).badColumn();
                    throw new RuntimeException(e);
                }
            }
            turnHandler();
        }
    }
}
