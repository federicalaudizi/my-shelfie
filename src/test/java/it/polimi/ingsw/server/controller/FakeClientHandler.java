package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.network.ClientHandler;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Coordinate;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;

import java.util.HashMap;
import java.util.Random;

/**
 * This class is a fake client handler used to test the game controller
 *
 * @author Sara
 * */
public class FakeClientHandler extends ClientHandler {
    /**
     * This method starts the thread and executes all the logic of the client handler
     *
     * @author Sara
     */
    @Override
    public void run() {
    }

    /**
     * This method sends the first gamestate to the client
     *
     * @param gameState the gamestate to send to the client
     */
    @Override
    public void sendGameState(Game gameState) {
        System.out.println(gameState.toJson());
    }

    /**
     * This method sends the updates of the gamestate to the client at the end of each player's turn
     *
     * @param board           the board of the game
     * @param player          the player who just played
     * @param pointDeckValues the values of the point decks
     * @param lastTurnFlag    true if we are in the last turn, false otherwise
     */
    @Override
    public void sendGameState(Board board, Player player, int[] pointDeckValues, boolean lastTurnFlag) {
        System.out.println(player.getUsername()+"'s turn:");
        System.out.println(board);
        System.out.println(player);
    }

    /**
     * This method signals the client that a response was accepted
     *
     * @author Sara
     */
    @Override
    public void sendOk() {
        System.out.println("ok");
    }

    /**
     * This method asks the client to select a set of tiles
     *
     * @return an array of tiles
     * @author Sara
     */
    @Override
    public Coordinate[] getTiles() {
        Random r = new Random();
        Coordinate[] chosenCoordinate = new Coordinate[3];
        chosenCoordinate[0] = new Coordinate(r.nextInt(1,8), r.nextInt(1,8));
        //chosenCoordinate[1] = new Coordinate(r.nextInt(8),r.nextInt(8));
        return chosenCoordinate;
    }

    /**
     * This method signals the client that the selected tiles are not valid
     *
     * @author Sara
     */
    @Override
    public void badTile() {
        System.out.println("Wrong tile, try again");
    }

    /**
     * This method asks the client to select a column
     *
     * @return the column selected by the client
     * @author Sara
     */
    @Override
    public int getColumn() {
        Random r = new Random();
        return r.nextInt(0,5);
    }

    /**
     * This method signals the client that the selected column is not valid
     *
     * @author Sara
     */
    @Override
    public void badColumn() {
        System.out.println("Wrong column, try again");
    }

    /**
     * This method signals the client that the game has ended
     *
     * @param leaderboard a JSON object containing the leaderboard
     * @author Sara
     */
    @Override
    public void gameOver(HashMap<String, Integer> leaderboard) {
        System.out.println("Game is over, here's the leaderboard:");
        System.out.println(leaderboard);
    }
}
