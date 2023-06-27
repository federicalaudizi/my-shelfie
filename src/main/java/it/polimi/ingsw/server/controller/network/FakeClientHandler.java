package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.server.model.Coordinate;
import it.polimi.ingsw.server.model.Game;

import java.util.HashMap;
import java.util.Random;

/**
 * This class is a fake client handler used to test the game controller
 *
 * @author Sara
 * */
public class FakeClientHandler extends ClientHandler {

    public FakeClientHandler() {
        super(null);
    }

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
        System.out.println(gameState.getBoard().toString());
        System.out.println(gameState.getCurrentPlayer().getShelf());
    }

    /**
     * This method sends the first gamestate to the client
     *
     * @param gameState       the gamestate to send to the client
     * @param player          the player that completed the objective
     * @param gainedObjective 1 if the player gained the first objective, 2 if the player gained the second objective, 3 if the player gained both objectives
     */
    @Override
    public void sendGameState(Game gameState, String player, int gainedObjective) {
        System.out.println("Someone won");
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
     * This method signal the client that a player disconnected
     *
     * @param disconnectedPlayer the player that disconnected
     */
    @Override
    public void sendDisconnectedPlayer(String disconnectedPlayer) {
        System.out.println(disconnectedPlayer+" disconnected");
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
        int x = r.nextInt(1,8);
        int y = r.nextInt(1,8);
        chosenCoordinate[0] = new Coordinate(x, y);
        System.out.println("Picked: ("+x+","+y+")");
        return chosenCoordinate;
    }

    /**
     * This method signals the client that the selected tiles are not valid
     *
     * @author Sara
     */
    @Override
    public void badTile(String info) {
        // System.out.println("Wrong tile, try again");
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
        int ret = r.nextInt(0,5);
        System.out.println("Picked: "+ret);
        return ret;
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
