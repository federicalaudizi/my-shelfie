package it.polimi.ingsw.server.controller;


import it.polimi.ingsw.server.controller.network.ClientHandler;
import it.polimi.ingsw.server.exceptions.*;
import it.polimi.ingsw.server.model.Coordinate;
import it.polimi.ingsw.server.model.Game;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Game Controller
 *
 * @author Federica & Sara
 */
public class GameController implements Runnable {
    private final boolean resumed;
    private boolean started;
    private final Game game;
    private final GameSupervisor ongoingGames;
    private final ConcurrentHashMap<String, ClientHandler> playerToClientHandlerMap;
    private final ConcurrentHashMap<String, Integer> connectedPlayers;
    private final ArrayList<String> players;
    private final String gameId;
    private boolean isOver;
    private final Object waitLock;
    private final Object stateLock;

    /**
     * Constructor for the game controller
     *
     * @param playerNumber is the number of players in the game
     * @param  gameId is the identifier of the game
     * @param ongoingGames is the reference to the game supervisor
     *
     * */
    public GameController(int playerNumber, String gameId, GameSupervisor ongoingGames) {
        this.playerToClientHandlerMap = new ConcurrentHashMap<>();
        this.gameId = gameId;
        this.game = new Game(playerNumber);
        this.isOver = false;
        this.players = new ArrayList<>();
        this.connectedPlayers = new ConcurrentHashMap<>();
        this.ongoingGames = ongoingGames;
        this.waitLock = new Object();
        this.stateLock = new Object();
        this.resumed = false;
        this.started = false;
    }

    /**
     * Creates a copy of this object from its JSON representation
     * @param toCopy the JSON representation of the object to copy
     */
    public GameController(JSONObject toCopy, GameSupervisor ongoingGames){
        this.waitLock = new Object();
        this.stateLock = new Object();
        this.ongoingGames = ongoingGames;
        this.gameId = toCopy.getString("gameId");
        this.game = new Game(toCopy.getJSONObject("game"));
        this.isOver = toCopy.getBoolean("isOver");

        this.players = new ArrayList<>();
        this.playerToClientHandlerMap = new ConcurrentHashMap<>();
        this.connectedPlayers = new ConcurrentHashMap<>();

        JSONArray jsonPlayers = toCopy.getJSONArray("players");
        for (int i = 0; i < jsonPlayers.length(); i++) {
            String playerId = jsonPlayers.getString(i);
            this.players.add(playerId);
        }
        this.resumed = true;
        this.started = false;
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

        if(!resumed) game.setUsernames(players);

        // Sends the first game update to all the client handlers
        updateAllPlayers();

        if(resumed) System.out.println(gameId+": Let's resume the game!");
        else System.out.println(gameId+": Let's start playing!");

        started = true;

        //let's start playing
        while (!isOver) {
            synchronized (stateLock) {
                playTurn();
                try {
                    stateLock.wait(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        gameOver();
    }

    /**
     * Changes the connection status of the player when disconnected
     *
     * @param playerId the disconnected player
     */
    void notifyDisconnection(String playerId) {
        System.out.println(gameId+": "+playerId+" disconnected from this game");
        connectedPlayers.put(playerId, 0);
        for(String player : players){
            if(connectedPlayers.get(player) == 1){
                getClientHandler(player).sendDisconnectedPlayer(playerId);
            }
        }
    }

    /**
     * called by the ClientHandler to modify the connection status of the player in connected
     *
     * @param playerId of which ClientHandler needs to modify the status
     */
    void notifyConnection(String playerId) {
        System.out.println(gameId+": "+playerId+" reconnected to this game");
        connectedPlayers.put(playerId, 1);
        playerToClientHandlerMap.put(playerId, ongoingGames.getClientHandlerById(playerId));

        synchronized (waitLock){
            waitLock.notifyAll();
        }
        if(started) getClientHandler(playerId).sendGameState(game);
    }

    /**
     * Adds the player to the map of connected players and to the map with their connection status
     *
     * @param playerId the nickname of each player
     * @param handler  the ClientHandler connected to that playerId
     * @throws ReachedMaxNumberOfPlayersException when the number of players reaches the number chosen at
     *                                   the beginning of the play
     */
    public void addPlayer(String playerId, ClientHandler handler) throws ReachedMaxNumberOfPlayersException {
        if (playerToClientHandlerMap.size() >= game.getNumberOfPlayers()) {
            throw new ReachedMaxNumberOfPlayersException();
        }
        connectedPlayers.put(playerId, 1);
        playerToClientHandlerMap.put(playerId, handler);
        System.out.println(gameId+": "+playerId+" joined this game!");

        synchronized (waitLock){
            waitLock.notifyAll();
        }
    }

    /**
     * Helper method to play the game
     *
     * @author Sara, Federica
     */
    private void playTurn(){
        String currentPlayerId = players.get(game.getCurrentPlayerIndex());
        System.out.println(gameId+": It's "+currentPlayerId+"'s turn!");

        if(connectedPlayers.values().stream().filter(value -> value == 1).count() > 1){
            // Case when there are more than 1 player connected
            if(connectedPlayers.get(currentPlayerId) == 1){
                // Case when the current player is connected
                try {
                    boolean ret = playerMakeMove(currentPlayerId);
                    if(ret) {
                        // Konami Code was activated
                        isOver = true;
                        return;
                    }
                } catch (PlayerDisconnectedException e) {
                    // Player disconnected, game over
                    // notifyDisconnection(currentPlayerId);
                }

            }

            int result = game.checkGoals();

            isOver = game.nextTurn();

            updateAllPlayers(currentPlayerId, result);
        } else {
            System.out.println(gameId+": Not enough players to continue, waiting for more players to connect");
            // Case 1 or zero players connected
            try {
                synchronized (waitLock){
                    // Wait for a minute
                    waitLock.wait(60000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(connectedPlayers.values().stream().filter(value -> value == 1).count() <= 1){
                // No one connected, game over
                System.out.println(gameId+": No one connected, game over");
                isOver = true;
            } else {
                System.out.println(gameId+" resuming game");
            }
        }
    }

    /**
     * Helper method to update the game state of all players
     */
    private void updateAllPlayers(){
        // Send game state to all players after the move
        for (String player : players) {
            if(connectedPlayers.get(player) == 1) {
                System.out.println(gameId + ": Sending game state to " + player);
                getClientHandler(player).sendGameState(game);
            }
        }
    }

    /**
     * Helper method to update the game state of all players
     *
     * @param playerWinner the player that won the objective
     * @param winStatus the status of the objective
     * @author Federico, Sara
     */
    private void updateAllPlayers(String playerWinner, int winStatus){
        // Send game state to all players after the move
        if(winStatus != 0){
            System.out.println(gameId + ": "+playerWinner+" won an objective with status "+winStatus);
            for (String player : players) {
                if (connectedPlayers.get(player) == 1) {
                    System.out.println(gameId + ": Sending game state to " + player);
                    getClientHandler(player).sendGameState(game, playerWinner, winStatus);
                }
            }
        } else updateAllPlayers();
    }

    /**
     * Helper method used to make a player play
     *
     * @param currentPlayerId the player that has to play
     * @return true if the Konami Code was activated, false otherwise
     * @author Federico
     */
    private boolean playerMakeMove(String currentPlayerId) throws PlayerDisconnectedException {
        boolean tilesPicked = false;
        boolean columnPicked = false;
        Coordinate[] coordinates = null;
        int column;

        while(!tilesPicked){
            System.out.println(gameId+": "+currentPlayerId+" has to choose the tile");
            boolean pickables;
            String info = "Selected tiles can't be picked";
            coordinates = getClientHandler(currentPlayerId).getTiles();

            // Konami Code
            if(currentPlayerId.equals("Joshua") && coordinates[0].getColumn() == 1 && coordinates[0].getRow() == 5 && coordinates[1].getColumn() == 2 && coordinates[1].getRow() == 4) {
                getClientHandler(currentPlayerId).sendOk();
                return true;
            }

            // Check if all coordinates are pickable
            try {
                if (coordinates.length == 1) pickables = game.arePickable(coordinates[0], null, null);
                else if (coordinates.length == 2) pickables = game.arePickable(coordinates[0], coordinates[1], null);
                else if (coordinates.length == 3) pickables = game.arePickable(coordinates[0], coordinates[1], coordinates[2]);
                else if (coordinates.length == 0) {
                    pickables = false;
                    info = "Not enough tiles selected";
                } else {
                    pickables = false;
                    info = "Too many tiles selected";
                }
                tilesPicked = pickables;
            } catch (tooManyTilesException e) {
                info = "There are not enough spaces to place all the tiles";
            }
            // Notify the player if the tiles are not pickable
            if(!tilesPicked) getClientHandler(currentPlayerId).badTile(info);
        }
        System.out.println(gameId+": "+currentPlayerId+" got the tiles right");
        getClientHandler(currentPlayerId).sendOk();

        while(!columnPicked){
            System.out.println(gameId+": "+currentPlayerId+" has to choose the column");
            column = getClientHandler(currentPlayerId).getColumn();

            try {
                switch (coordinates.length) {
                    case 1 -> game.makeMove(column, coordinates[0], null, null);
                    case 2 -> game.makeMove(column, coordinates[0], coordinates[1], null);
                    default -> game.makeMove(column, coordinates[0], coordinates[1], coordinates[2]);
                }
                columnPicked = true;
            } catch (tooManyTilesException | notEnoughTilesException | fullColumnException e){
                getClientHandler(currentPlayerId).badColumn();
            }
        }
        System.out.println(gameId+": "+currentPlayerId+" has got the column right");
        getClientHandler(currentPlayerId).sendOk();

        return false;
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
        synchronized (waitLock) {
            while (connectedPlayers.values().stream().filter(value -> value == 1).count() < game.getNumberOfPlayers()) {
                System.out.println(gameId + ": Waiting for players to join, " + playerToClientHandlerMap.size() + " out of " + game.getNumberOfPlayers());

                try {
                    waitLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread Interrupted");
                }
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
        // Prepare leaderboard
        for (String player : leaderboard.keySet()) {
            if (connectedPlayers.get(player) == 0) {
                leaderboard.put(player, -1);
            }
        }

        // Send Leaderboard
        for (String player : players) {
            if (connectedPlayers.get(player) == 1) {
                getClientHandler(player).gameOver(leaderboard);
            }
        }

        // Remove game and all players
        ongoingGames.gameOver(gameId);
    }

    /**
     * Checks if this GameController is equal to another one
     * @param other the other GameController
     * @return true if the two GameController are equal, false otherwise
     * @author Federico
     */
    public boolean equals(GameController other) {
        return this.gameId.equals(other.gameId) &&
                this.game.toJson().toString().equals(other.game.toJson().toString()) &&
                this.players.containsAll(other.players) &&
                this.isOver == other.isOver;
    }

    /**
     * Creates a JSON representation of the GameController
     *
     * @return the JSONObject representation of the GameController
     * @throws NonExistentGameException if the game hasn't been started yet or nobody is connected
     * @author Federico
     */
    public JSONObject toJson() throws NonExistentGameException {
        JSONObject ret = new JSONObject();

        synchronized (stateLock) {
            // Check if the game has started
            if(!started) throw new NonExistentGameException("Game hasn't started yet");

            ret.put("gameId", gameId);
            ret.put("game", game.toJson());
            ret.put("players", new JSONArray(players));
            ret.put("isOver", isOver);
            stateLock.notifyAll();
        }

        return ret;
    }

    /**
     * getter for playerToClientHandlerMap implemented for testing
     *
     * @return playerToClientHandlerMap
     *
     * */
    public HashMap<String, ClientHandler> getPlayerToClientHandlerMap(){
        return new HashMap<>(playerToClientHandlerMap);
    }

    /**
     * getter for connectedPlayers implemented for testing
     *
     * @return connectedPlayer
     *
     * */
    public HashMap<String, Integer> getConnectedPlayers(){
        return new HashMap<>(connectedPlayers);
    }

    /**
     * @return if the game started (all players joined) or not
     * @author Federico
     */
    public boolean isStarted() {return started;}
}
