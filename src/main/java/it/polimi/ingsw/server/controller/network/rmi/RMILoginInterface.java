package it.polimi.ingsw.server.controller.network.rmi;

import it.polimi.ingsw.server.controller.network.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMILoginInterface extends Remote {
    /**
     * This method is used to log in a player, it expects a username and returns a message with an error or confirmation
     *
     * @param username the username of the player
     * @return a message with an error or confirmation, confirmation consist in OK, errors can be USERNAME_TAKEN or GENERIC_ERROR
     * @throws RemoteException when a connection error occurs
     * @author Federico
     */
    Message login(String username) throws RemoteException;

    /**
     * This method is used to reconnect a player, it expects a username and returns a message with an error or confirmation
     *
     * @param username the username of the player
     * @return a message with an error or confirmation, confirmation consist in OK, error could be GENERIC_ERROR
     * @throws RemoteException when a connection error occurs
     * @author Federico
     */
    Message reconnect(String username) throws RemoteException;

    /**
     * This method is used to create a game, it expects a username and the number of players, it returns a message with an error or confirmation
     *
     * @param me the username of the player
     * @param numberOfPlayers the number of players
     * @return a message with an error or confirmation, confirmation consist in OK, error can be GENERIC_ERROR
     * @throws RemoteException when a connection error occurs
     * @author Federico
     */
    Message createGame(String me, int numberOfPlayers) throws RemoteException;

    /**
     * This method is used to get the list of games, it expects a username and returns a message with an error or confirmation
     *
     * @param me the username of the player
     * @return a message with the list of all games
     * @throws RemoteException when a connection error occurs
     * @author Federico
     */
    Message getGameList(String me) throws RemoteException;

    /**
     * This method is used to join a game, it expects a username and a gameId and returns a message with an error or confirmation
     *
     * @param me the username of the player
     * @param gameID the id of the game
     * @return a message with an error or confirmation, confirmation consist in OK, error can be GENERIC_ERROR
     * @throws RemoteException when a connection error occurs
     */
    Message joinGame(String me, String gameID) throws RemoteException;
}
