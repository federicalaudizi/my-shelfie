package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.network.ClientHandler;
import it.polimi.ingsw.server.exceptions.NonExsistentGameException;
import it.polimi.ingsw.server.exceptions.PlayerIdTakenException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class manages the creation of games and the association of players to games.
 * It also keeps track of the games that are currently running, the players that are currently playing and the players that are currently waiting for a game to start.
 *
 * @author Federico
 */
public class GameSupervisor{

    private final ArrayList<String> gamesId;
    private final ArrayList<String> playersId;
    private final HashMap<String, GameController<String>> games;
    private final HashMap<String, ClientHandler> players;
    private final HashMap<String, String> playersGames;

    public GameSupervisor(){
        gamesId = new ArrayList<>();
        playersId = new ArrayList<>();
        games = new HashMap<>();
        players = new HashMap<>();
        playersGames = new HashMap<>();
    }

    /**
     * This method adds a new player to the list of players that are logged in
     *
     * @param handler the client handler of the player
     * @author Federico
     */
    public void addUser(String playerId, ClientHandler handler) throws PlayerIdTakenException {
        if(playersId.contains(playerId)) throw new PlayerIdTakenException();
        playersId.add(playerId);
        players.put(playerId, handler);
    }

    /**
     * Allows for a user that previously logged in to be recognized again
     *
     * @param playerId the id of the player
     * @param handler  the client handler of the player
     * @author Federico
     */
    public void userLogin(String playerId, ClientHandler handler) {
        players.put(playerId, handler);
    }

    /**
     * This method creates a new game and adds it to the list of games, after its creation, joinGame should be called
     *
     * @param numberOfPlayers the number of players that will play the game
     * @return the id of the game
     * @author Federico
     */
    public String newGame(int numberOfPlayers) {
        //TODO: implement this method when GameController is completed
        return null;
    }

    /**
     * This method adds a player to a game
     *
     * @param playerId the id of the player
     * @param gameId   the id of the game
     * @return the game controller of the game
     * @author Federico
     */
    public GameController joinGame(String playerId, String gameId) {
        playersGames.put(playerId, gameId);
        return games.get(gameId);
    }

    /**
     * This method lets a player rejoin a game that
     *
     * @param playerId the playerId that wants to join a game
     * @return the game controller of the playing game
     * @throws NonExsistentGameException if there is no game associated to that player
     * @author Federico
     */
    public GameController joinGame(String playerId) throws NonExsistentGameException {
        return null;
    }

    /**
     * This method returns the list of the ids of the games that are currently running
     *
     * @return the list of the ids of the games that are currently running
     * @author Federico
     */
    public ArrayList<String> getGamesId() {
        return new ArrayList<>(gamesId);
    }

    /**
     * this method returns the game controller of a game by its id
     *
     * @param gameId the id of the game
     * @return the game controller of the game
     * @author Federico
     */
    public GameController getGamebyId(String gameId) {
        return games.get(gameId);
    }

    /**
     * This method returns whether a player exists or not
     *
     * @param playerId the id of the player
     * @return true if the player exists, false otherwise
     * @author Federico
     */
    public boolean playerExists(String playerId) {
        return playersId.contains(playerId);
    }

    /**
     * This method returns whether a game exists or not
     *
     * @param gameId the id of the game
     * @return true if the game exists, false otherwise
     * @author Federico
     */
    public boolean gameExists(String gameId) {
        return gamesId.contains(gameId);
    }

    /**
     * This method returns whether a player is in a game or not
     *
     * @param playerId the id of the player
     * @return true if the player is in a game, false otherwise
     * @author Federico
     */
    public boolean playerIsInGame(String playerId) {
        return playersGames.containsKey(playerId);
    }

    /**
     * This method ends a game
     *
     * @param gameId the id of the game
     * @author Federico
     */
    public void gameOver(String gameId) {
        //TODO: what should the supervisor do when a game ends?
    }

    /**
     * This method removes a player from the list of players
     *
     * @param playerId the id of the player
     * @author Federico
     */
    public void removePlayer(String playerId) {
        //TODO: what should the supervisor do when a player disconnects?
    }

    /**
     * This method returns the client handler of a player by its id
     *
     * @param playerId the id of the player
     * @return the client handler of the player
     * @author Federico
     */
    public ClientHandler getClientHandlerById(String playerId){
        return players.get(playerId);
    }

    /**
     * This is a helper method that generates random strings
     *
     * @return a random string
     * @author Federico
     */
    private String randomString(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int index = (int)(characters.length() * Math.random());
            randomString.append(characters.charAt(index));
        }
        return randomString.toString();
    }
}
