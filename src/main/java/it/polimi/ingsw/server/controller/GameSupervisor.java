package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.exceptions.NonExsistentGameException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class manages the creation of games and the association of players to games.
 *
 * @author Federico
 */
public class GameSupervisor {
    private long lastPlayerId;
    private long lastGameId;
    private final ArrayList<Long> players;
    private final ArrayList<Long> gamesId;
    private final HashMap<Long, GameController> games; // Relates the game
    private final HashMap<Long, Long> participants;

    /**
     * This constructor initializes the GameSupervisor.
     */
    GameSupervisor() {
        this.lastPlayerId = 0;
        this.lastGameId = 0;
        this.players = new ArrayList<>();
        this.gamesId = new ArrayList<>();
        this.games = new HashMap<>();
        this.participants = new HashMap<>();
    }

    /**
     * This method adds a new player to the list of players.
     *
     * @author Federico
     *
     * @return the id of the new player
     */
    long addPlayer() {
        long newPlayerId = lastPlayerId;
        players.add(newPlayerId);
        lastPlayerId++;
        return newPlayerId;
    }

    /**
     * This method creates a new game and adds it to the list of games. It does not add the creator to the game as a participant
     *
     * @author Federico
     *
     * @return the id of the new game
     */
    long newGame() {
        long newGameId = lastGameId;
        gamesId.add(newGameId);
        games.put(newGameId, new GameController());
        lastGameId++;
        return newGameId;
    }

    /**
     * This method adds a player to a game
     *
     * @author Federico
     *
     * @param playerId the id of the player
     * @param gameId the id of the game
     * @return true if the game exists, false otherwise
     * @throws NonExsistentGameException if the game that the player wants to join does not exist
     */
    GameController joinGame(long playerId, long gameId) throws NonExsistentGameException {
        if (games.containsKey(gameId)) {
            participants.put(playerId, gameId);
            return games.get(gameId);
        }else throw new NonExsistentGameException();
    }

    /**
     * This method returns the id of all games
     *
     * @author Federico
     *
     * @return the id of all games
     */
    ArrayList<Long> getGamesId() {
        return new ArrayList<>(gamesId);
    }

    /**
     * This method checks if a player has already been created
     *
     * @author Federico
     *
     * @param id the id of the player
     * @return true if the player exists, false otherwise
     */
    boolean playerExists(long id) {
        return players.contains(id);
    }

    /**
     * This method checks if a player is in any game
     *
     * @author Federico
     *
     * @param id the id of the player
     * @return true if the player is in a game, false otherwise
     */
    boolean playerIsInGame(long id) {
        return participants.containsKey(id);
    }

    /**
     * This method removes a player's id from the list of players.
     *
     * @author Federico
     *
     * @param id the id of the player to remove
     */
    void removePlayer(long id) {
        players.remove(id);
    }
}
