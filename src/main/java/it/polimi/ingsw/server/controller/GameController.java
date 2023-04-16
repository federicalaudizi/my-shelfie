package it.polimi.ingsw.server.controller;


import it.polimi.ingsw.server.model.Game;

import java.util.ArrayList;

/**
 * This class is the abstract class that represents a game controller.
 * Game controller holds a list client handler that are subscribed to this game so that it can send them messages and control the flow of the game.
 * Game controller also holds the game model and allows its subscribers to see the state of the model so that it can send the state to the players
 * The game controller should run as a thread, after its creation it gets started by its creator, then it will wait for all players to join before starting the game.
 * All players will be subscribed by their id, so that even if a player disconnects, the game can continue and the player can reconnect and continue playing with a different ClientHandler.
 *
 * @param <T> the type of definition of the player, it can be a string, an integer, a class, etc.
 * @author Sara, Federica
 */
public abstract class GameController<T> implements Runnable{

    private ArrayList<T> players;
    private Game game;
    private GameSupervisor<T> ongoingGames;

    /**
     * This method adds a player to the list of subscribed players.
     *
     * @param player the player identifier to be added to the list of subscribed players
     */
    public abstract void addPlayer(T player);
}
