package it.polimi.ingsw.server.controller.network.rmi;

import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.controller.GameSupervisor;
import it.polimi.ingsw.server.controller.network.Message;
import static it.polimi.ingsw.server.controller.network.Message.Header.*;

import it.polimi.ingsw.server.exceptions.*;
import org.json.JSONObject;

import java.rmi.RemoteException;

public class RMILogin implements RMILoginInterface{

    private final GameSupervisor ongoingGames;

    public RMILogin(GameSupervisor ongoingGames) {
        this.ongoingGames = ongoingGames;
    }

    /**
     * This method is used to log in a player, it expects a username and returns a message with an error or confirmation
     *
     * @param username the username of the player
     * @return a message with an error or confirmation, confirmation consist in OK, errors can be USERNAME_TAKEN or GENERIC_ERROR
     * @throws RemoteException if the connection is lost
     * @author Federico
     */
    @Override
    public Message login(String username) throws RemoteException {
        System.out.println(username + ": Wants to log in");
        RMIClientHandler thisUser = new RMIClientHandler(username, ongoingGames);
        try {
            ongoingGames.newUser(username, thisUser);
            System.out.println(username + ": Successfully logged in");
            new Thread(thisUser).start();
            return new Message(OK);
        } catch (PlayerIdTakenException e) {
            return new Message(USERNAME_TAKEN, new JSONObject().put("message", "Username is already taken"));
        }
    }

    /**
     * This method is used to reconnect a player, it expects a username and returns a message with an error or confirmation
     *
     * @param username the username of the player
     * @return a message with an error or confirmation, confirmation consist in OK, error could be GENERIC_ERROR
     * @throws RemoteException if the connection is lost
     * @author Federico
     */
    @Override
    public Message reconnect(String username) throws RemoteException {
        System.out.println(username + ": Wants to reconnect");
        RMIClientHandler thisUser = new RMIClientHandler(username, ongoingGames);
        try {
            GameController game = ongoingGames.oldUser(username, thisUser);
            System.out.println(username + ": Successfully reconnected");
            new Thread(thisUser).start();
            game.notifyConnection(username);
            return new Message(OK);
        } catch (PlayerDoesNotExistsException e) {
            return new Message(BAD_HEADER, new JSONObject().put("message", "Player does not exists"));
        }
    }

    /**
     * This method is used to create a game, it expects a username and the number of players, it returns a message with an error or confirmation
     *
     * @param me              the username of the player
     * @param numberOfPlayers the number of players
     * @return a message with an error or confirmation, confirmation consist in OK, error can be GENERIC_ERROR
     * @throws RemoteException if the connection is lost
     * @author Federico
     */
    @Override
    public Message createGame(String me, int numberOfPlayers) throws RemoteException {
        System.out.println(me + ": Wants to create a new game");
        String gameId = ongoingGames.newGame(numberOfPlayers);
        try {
            ongoingGames.joinGame(me, gameId);
            System.out.println(me + ": Successfully created a new game");
            return new Message(OK);
        } catch (NonExistentGameException | ReachedMaxNumberOfPlayers ignored) {
            return new Message(BAD_HEADER, new JSONObject().put("message", "An error occurred while creating the game"));
        }
    }

    /**
     * This method is used to get the list of games, it expects a username and returns a message with an error or confirmation
     *
     * @param me the username of the player
     * @return a message with the list of all games
     * @throws RemoteException if the connection is lost
     * @author Federico
     */
    @Override
    public Message getGameList(String me) throws RemoteException {
        System.out.println(me + ": Wants to join a game");
        JSONObject response = new JSONObject();
        try {
            response.put("games", ongoingGames.getGameIds());
        } catch (NoGamesException e) {
            return new Message(NO_GAMES, new JSONObject().put("message", "No games to join"));
        }

        return new Message(GAME_LIST_RESPONSE, response);
    }

    /**
     * This method is used to join a game, it expects a username and a gameId and returns a message with an error or confirmation
     *
     * @param me     the username of the player
     * @param gameID the id of the game
     * @return a message with an error or confirmation, confirmation consist in OK, error can be GENERIC_ERROR or BAD_GAME_ID
     * @throws RemoteException if the connection is lost
     */
    @Override
    public Message joinGame(String me, String gameID) throws RemoteException {
        try {
            ongoingGames.joinGame(me, gameID);
            System.out.println(me + ": Joined a game");
        } catch (NonExistentGameException e) {
            return new Message(BAD_GAME_ID, new JSONObject().put("message", "Game does not exists"));
        } catch (ReachedMaxNumberOfPlayers e) {
            return new Message(BAD_GAME_ID, new JSONObject().put("message", "Game is full"));
        }
        return new Message(OK);
    }
}
