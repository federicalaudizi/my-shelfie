package it.polimi.ingsw.server.controller.network.rmi;

import it.polimi.ingsw.server.controller.GameSupervisor;
import it.polimi.ingsw.server.controller.network.ClientHandler;
import it.polimi.ingsw.server.model.Coordinate;
import it.polimi.ingsw.server.model.Game;

import java.util.HashMap;

public class RMIClientHandler extends ClientHandler {
    private final GameSupervisor ongoingGames;

    RMIClientHandler(GameSupervisor ongoingGames) {
        this.ongoingGames = ongoingGames;
    }

    /**
     * This method starts the thread and executes all the logic of the client handler
     *
     * @author Federico
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

    }

    /**
     * This method signals the client that a response was accepted
     *
     * @author Federico
     */
    @Override
    public void sendOk() {

    }

    /**
     * This method asks the client to select a set of tiles
     *
     * @return an array of tiles
     * @author Federico
     */
    @Override
    public Coordinate[] getTiles() {
        return new Coordinate[0];
    }

    /**
     * This method signals the client that the selected tiles are not valid
     *
     * @author Federico
     */
    @Override
    public void badTile() {

    }

    /**
     * This method asks the client to select a column
     *
     * @return the column selected by the client
     * @author Federico
     */
    @Override
    public int getColumn() {
        return 0;
    }

    /**
     * This method signals the client that the selected column is not valid
     *
     * @author Federico
     */
    @Override
    public void badColumn() {

    }

    /**
     * This method signals the client that the game has ended
     *
     * @param leaderboard a JSON object containing the leaderboard
     * @author Federico
     */
    @Override
    public void gameOver(HashMap<String, Integer> leaderboard) {

    }
}
