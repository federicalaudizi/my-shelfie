package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.network.ClientHandler;
import it.polimi.ingsw.server.exceptions.NonExsistentGameException;
import it.polimi.ingsw.server.exceptions.PlayerIdTakenException;

import java.util.ArrayList;

/**
 * This class is used to manage the games and the players
 *
 * @author Federico
 *
 * @param <T> is the type of the id of the players and the games
 */
public abstract class GameSupervisor<T> {

    /**
     * This method adds a new player to the list of players that are logged in
     *
     * @author Federico
     *
     * @param handler the client handler of the player
     * @throws PlayerIdTakenException if the id of the player is already taken
     */
    public abstract void  addUser(T playerId, ClientHandler handler) throws PlayerIdTakenException;

    /**
     * Allows for a user that previously logged in to be recognized again
     *
     * @author Federico
     *
     * @param playerId the id of the player
     * @param handler the client handler of the player
     */
    public abstract void userLogin(T playerId, ClientHandler handler);

    /**
     * This method creates a new game and adds it to the list of games
     *
     * @author Federico
     *
     * @param numberOfPlayers the number of players that will play the game
     * @return the id of the game
     */
    public abstract T newGame(int numberOfPlayers);

    /**
     * This method adds a player to a game
     *
     * @author Federico
     *
     * @param playerId the id of the player
     * @param gameId the id of the game
     * @throws NonExsistentGameException if the game does not exsits
     * @return the game controller of the game
     */
    public abstract GameController<T> joinGame(T playerId, T gameId) throws NonExsistentGameException;

    /**
     * This method lets a player rejoin a game that
     *
     * @author Federico
     *
     * @param playerId the playerId that wants to join a game
     * @return the game controller of the playing game
     * @throws NonExsistentGameException if there is no game associated to that player
     */
    public abstract GameController<T> joinGame(T playerId) throws NonExsistentGameException;

    /**
     * This method returns the list of the ids of the games that are currently running
     *
     * @author Federico
     *
     * @return the list of the ids of the games that are currently running
     */
    public abstract ArrayList<T> getGamesId();

    /**
     * this method returns the game controller of a game by its id
     *
     * @author Federico
     *
     * @param gameId the id of the game
     * @return the game controller of the game
     */
    public abstract GameController<T> getGamebyId(T gameId);

    /**
     * This method returns whether a player exists or not
     *
     * @author Federico
     *
     * @param playerId the id of the player
     * @return true if the player exists, false otherwise
     */
    public abstract boolean playerExists(T playerId);

    /**
     * This method returns whether a game exists or not
     *
     * @author Federico
     *
     * @param gameId the id of the game
     * @return true if the game exists, false otherwise
     */
    public abstract boolean gameExists(T gameId);

    /**
     * This method returns whether a player is in a game or not
     *
     * @author Federico
     *
     * @param playerId the id of the player
     * @return true if the player is in a game, false otherwise
     */
    public abstract boolean playerIsInGame(T playerId);

    /**
     * This method ends a game
     *
     * @author Federico
     *
     * @param gameId the id of the game
     */
    public abstract void gameOver(T gameId);

    /**
     * This method removes a player from the list of players
     *
     * @author Federico
     *
     * @param playerId the id of the player
     */
    public abstract void removePlayer(T playerId);
}
