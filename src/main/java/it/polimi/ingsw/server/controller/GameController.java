package it.polimi.ingsw.server.controller;


import it.polimi.ingsw.server.controller.network.ClientHandler;
import it.polimi.ingsw.server.exceptions.*;
import it.polimi.ingsw.server.model.Coordinate;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Tile;

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
    private final GameSupervisor ongoingGames;
    private final Map<String, ClientHandler> playerToClientHandlerMap;
    private final Map<String, Integer> connectedPlayers;
    private final ArrayList<String> players;
    private final String gameId;
    private boolean isOver;


    GameController(int playerNumber, String gameId, GameSupervisor ongoingGames) {
        playerToClientHandlerMap = new HashMap<>();
        this.gameId = gameId;
        game = new Game(playerNumber);
        isOver = false;
        players = new ArrayList<>();
        connectedPlayers = new HashMap<>();
        this.ongoingGames = ongoingGames;
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
     * @param playerId of the client handler
     * @return Client Handler referred to that playerId
     */
    private ClientHandler getClientHandler(String playerId) {
        return playerToClientHandlerMap.get(playerId);
    }

    @Override
    public void run() {
        //aspetto che si connettano tutti
        while(playerToClientHandlerMap.size() < game.getNumberOfPlayers()){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        //mettere in palyers i giocatori nell'ordine che voglio imporre
        players.addAll(playerToClientHandlerMap.keySet());

        game.setUsernames(players);

        // Mandiamo il primo gamestate a tutti
        for (String currentPlayerId : players) {
            getClientHandler(currentPlayerId).sendGameState(game);
        }

        //cominciamo a giocare
        while (!isOver) {
            Tile[] tiles;
            String currentPlayerId = players.get(game.getCurrentPlayerIndex());
            //if the player is not connected and the number of connected players is greater than 0 then
            // the turn passes automatically to the next player
            if (connectedPlayers.get(currentPlayerId) == 0 && connectedPlayers.values().stream().filter(value -> value == 1).count() > 1) {
                isOver = game.nextTurn();
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
                            isOver = true;
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }  else if (connectedPlayers.values().stream().filter(value -> value == 1).count() == 0){
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }   else {
                tiles = getTiles(currentPlayerId);
                tilesInShelf(tiles, currentPlayerId);
                isOver = game.nextTurn();
            }

            ongoingGames.gameOver(gameId);
        }

        HashMap<String, Integer> leaderboard = game.getRankedPlayers();
        for(String player: leaderboard.keySet()){
            if(connectedPlayers.get(player) == 0){
                leaderboard.put(player, -1);
            }
        }
        for(String player: players){
            if(connectedPlayers.get(player) == 1) {
                getClientHandler(player).gameOver(leaderboard);
            }
        }


    }

    /**
     * Private method used to keep asking the user to pick some tiles until they are in the correct position
     * or the correct amount
     */
    private Tile[] getTiles(String currentPlayerId) {
        Coordinate[] coordinates = getClientHandler(currentPlayerId).getTiles();
        Tile[] tiles;
        try {
            tiles = game.chooseTiles(coordinates[0], coordinates[1], coordinates[2]);
            getClientHandler(currentPlayerId).sendOk();
        } catch (TileUnpickableException | NullPointerException e) {
            getClientHandler(currentPlayerId).badTile();
            getTiles(currentPlayerId);
            throw new RuntimeException(e);
        }
        return tiles;
    }

    private void tilesInShelf(Tile[] tiles, String currentPlayerId) {
        int column;
        column = getClientHandler(currentPlayerId).getColumn();

        try {
            game.insertInShelf(column, tiles);
            for (String currentPlayer : players) {
                getClientHandler(currentPlayer).sendGameState(game.getBoard(), game.getCurrentPlayer(), game.getPointsValue());
            }
            getClientHandler(currentPlayerId).sendOk();
        } catch (fullColumnException e) {
            getClientHandler(currentPlayerId).badColumn();
            tilesInShelf(tiles, currentPlayerId);
            throw new RuntimeException(e);
        } catch (tooManyTilesException | notEnoughTilesException ignored) {
        }
    }
}
