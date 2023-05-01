package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.network.ClientHandler;
import it.polimi.ingsw.server.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class manages the creation of games and the association of players to games.
 * It also keeps track of the games that are currently running, the players that are currently playing and the players that are currently waiting for a game to start.
 *
 * @author Federico
 */
public class GameSupervisor{
    private final HashMap<String, GameController> games; //Associates a game to its id
    private final HashMap<String, ClientHandler> players; //Associates a player to his client handler
    private final HashMap<String, String> playersGames; //Associates a player to the game he is playing

    public GameSupervisor(){
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
    public synchronized void newUser(String playerId, ClientHandler handler) throws PlayerIdTakenException {
        if(players.containsKey(playerId)) throw new PlayerIdTakenException();
        players.put(playerId, handler);
    }

    /**
     * Allows for a user that previously logged in to be recognized again
     *
     * @param playerId the id of the player
     * @param handler  the client handler of the player
     * @author Federico
     */
    public synchronized GameController oldUser(String playerId, ClientHandler handler) throws PlayerDoesNotExistsException {
        if(!players.containsKey(playerId)) throw new PlayerDoesNotExistsException();
        players.put(playerId, handler);
        return games.get(playersGames.get(playerId));
    }

    /**
     * This method creates a new game and adds it to the list of games, after its creation, joinGame should be called
     *
     * @param numberOfPlayers the number of players that will play the game
     * @return the id of the game
     * @author Federico
     */
    public synchronized String newGame(int numberOfPlayers) {
        String newGameId = randomString();
        GameController game = new GameController(numberOfPlayers, newGameId);
        games.put(newGameId, game);

        return newGameId;
    }

    /**
     * This method adds a player to a game
     *
     * @param playerId the id of the player
     * @param gameId   the id of the game
     * @return the game controller of the game
     * @author Federico
     */
    public synchronized GameController joinGame(String playerId, String gameId) throws NonExsistentGameException, ReachedMaxNumberOfPlayers {
        if(!games.containsKey(gameId)) throw new NonExsistentGameException();
        GameController game = games.get(gameId);
        game.addPlayer(playerId, players.get(playerId));
        playersGames.put(playerId, gameId);
        return game;
    }

    /**
     * This method returns the list of the ids of the games that are currently running
     *
     * @return the list of the ids of the games that are currently running
     * @author Federico
     */
    public ArrayList<String> getGamesId() {
        return new ArrayList<>(games.keySet());
    }

    /**
     * this method returns the game controller of a game by its id
     *
     * @param gameId the id of the game
     * @return the game controller of the game
     * @author Federico
     */
    public GameController getGameById(String gameId) {
        return games.get(gameId);
    }

    /**
     * This method returns whether a player exists or not
     *
     * @param playerId the id of the player
     * @return true if the player exists, false otherwise
     * @author Federico
     */
    public boolean userExists(String playerId) {
        return players.containsKey(playerId);
    }

    /**
     * This method returns whether a game exists or not
     *
     * @param gameId the id of the game
     * @return true if the game exists, false otherwise
     * @author Federico
     */
    public boolean gameExists(String gameId) {
        return games.containsKey(gameId);
    }

    /**
     * This method returns whether a player is in a game or not
     *
     * @param playerId the id of the player
     * @return true if the player is in a game, false otherwise
     * @author Federico
     */
    public boolean userIsInGame(String playerId) {
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
