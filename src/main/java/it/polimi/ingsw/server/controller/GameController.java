package it.polimi.ingsw.server.controller;


import it.polimi.ingsw.server.controller.network.ClientHandler;
import it.polimi.ingsw.server.exceptions.*;
import it.polimi.ingsw.server.model.Coordinate;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Tile;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Game Controller
 *
 * @author Federica & Sara
 */

public class GameController implements Runnable {

    private final Game game;
    private final GameSupervisor ongoingGames;
    private final HashMap<String, ClientHandler> playerToClientHandlerMap;
    private final HashMap<String, Integer> connectedPlayers;
    private final ArrayList<String> players;
    private final String gameId;
    private boolean isOver;
    private final Object waitLock;


    /**
     * Constructor for the game controller
     *
     * @param playerNumber is the number of players in the game
     * @param  gameId is the identifier of the game
     * @param ongoingGames is the reference to the game supervisor
     *
     * */
    public GameController(int playerNumber, String gameId, GameSupervisor ongoingGames) {
        playerToClientHandlerMap = new HashMap<>();
        this.gameId = gameId;
        game = new Game(playerNumber);
        isOver = false;
        players = new ArrayList<>();
        connectedPlayers = new HashMap<>();
        this.ongoingGames = ongoingGames;
        this.waitLock = new Object();
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
        synchronized (waitLock) {
            waitLock.notifyAll();
        }
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
        System.out.println(gameId+": "+playerId+" joined this game!");

        synchronized (waitLock) {
            waitLock.notifyAll();
        }
    }


    /**
     * this thread manages the player turn.
     * Firstly it waits until all the players are connected.
     * When all the players are connected sends to all the client handlers the game state, then the game can start.
     * */
    @Override
    public void run() {
        //waiting for all the players to be connected
        waitAllPlayers();

        //players added to the map
        players.addAll(playerToClientHandlerMap.keySet());

        game.setUsernames(players);

        // Sends the first game update to all the client handlers
        for (String currentPlayerId : players) {
            System.out.println(gameId+": Sending game state to "+currentPlayerId);
            getClientHandler(currentPlayerId).sendGameState(game);
        }

        System.out.println(gameId+": Let's start playing!");
        //let's start playing
        playGame();

        gameOver();
    }


    /**
     * Helper method to play the game
     *
     * @author Sara, Federica
     */
    private void playGame(){
        while (!isOver) {
            String currentPlayerId = players.get(game.getCurrentPlayerIndex());

            if(connectedPlayers.values().stream().filter(value -> value == 1).count() > 1){
                // Case when there are more than 1 player connected
                if(connectedPlayers.get(currentPlayerId) == 1){
                    // Case when the current player is connected
                    playerMakeMove();
                } else {
                    // Skip turn
                    isOver = game.nextTurn();
                }
            } else {
                // Case 1 or zero players connected, count timer
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    // Someone connected, continue
                    continue;
                }
                if(connectedPlayers.values().stream().filter(value -> value == 1).count() <= 1){
                    // No one connected, game over
                    isOver = true;
                }
            }
        }
    }

    /**
     * Helper method used to make a player play
     *
     * @author Federico
     */
    private void playerMakeMove(){
        try {
            tilesInShelf(getTiles(players.get(game.getCurrentPlayerIndex())), players.get(game.getCurrentPlayerIndex()));
        } catch (PlayerDisconnectedException e) {
            // Skip player's turn
        }
        isOver = game.nextTurn();
    }

    /**
     * Private method used to keep asking the user to pick some tiles until they are in the correct position
     * or the correct amount
     *
     * @param currentPlayerId is the player in turn
     *
     * @return an array with the chosen tiles
     */
    private Tile[] getTiles(String currentPlayerId) throws PlayerDisconnectedException {
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

    /**
     * Private method to insert the tiles chosen by the player in the correct shelf
     *
     * @param tiles an array with the chosen tiles
     * @param  currentPlayerId is the player in turn
     *
     * */
    private void tilesInShelf(Tile[] tiles, String currentPlayerId) throws PlayerDisconnectedException {
        int column;
        column = getClientHandler(currentPlayerId).getColumn();

        try {
            game.insertInShelf(column, tiles);
            for (String currentPlayer : players) {
                getClientHandler(currentPlayer).sendGameState(game.getBoard(), game.getCurrentPlayer(), game.getPointsValue(), game.isLastTurn());
            }
            getClientHandler(currentPlayerId).sendOk();
        } catch (fullColumnException e) {
            getClientHandler(currentPlayerId).badColumn();
            tilesInShelf(tiles, currentPlayerId);
            throw new RuntimeException(e);
        } catch (tooManyTilesException | notEnoughTilesException ignored) {
        }
    }

    /**
     * @param playerId of the client handler
     * @return Client Handler referred to that playerId
     */
    private ClientHandler getClientHandler(String playerId) {
        return playerToClientHandlerMap.get(playerId);
    }

    /**
     * This method loops until all players connect
     *
     * @author Federica, Sara
     */
    private void waitAllPlayers(){
        while (playerToClientHandlerMap.size() < game.getNumberOfPlayers()) {
            synchronized (System.out){
            System.out.println(gameId+": Waiting for players to join, "+playerToClientHandlerMap.size()+" out of "+game.getNumberOfPlayers());}

            try {
                synchronized (waitLock) {
                    waitLock.wait();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread Interrupted");
            }
        }
    }

    /**
     * This method handles the gameOver procedure
     *
     * @author Federica, Sara
     */
    private void gameOver(){
        HashMap<String, Integer> leaderboard = game.getRankedPlayers();

        for (String player : leaderboard.keySet()) {
            if (connectedPlayers.get(player) == 0) {
                leaderboard.put(player, -1);
            }
        }

        for (String player : players) {
            if (connectedPlayers.get(player) == 1) {
                getClientHandler(player).gameOver(leaderboard);
            }
        }

        ongoingGames.gameOver(gameId);
    }

    /**
     * getter for playerToClientHandlerMap implemented for testing
     *
     * @return playerToClientHandlerMap
     *
     * */
    public HashMap<String, ClientHandler> getPlayerToClientHandlerMap(){
        return playerToClientHandlerMap;
    }

    /**
     * getter for connectedPlayers implemented for testing
     *
     * @return connectedPlayer
     *
     * */
    public HashMap<String, Integer> getConnectedPlayers(){
        return connectedPlayers;
    }
}
