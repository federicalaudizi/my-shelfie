package it.polimi.ingsw.server.controller.network.rmi;

import it.polimi.ingsw.server.controller.GameSupervisor;
import it.polimi.ingsw.server.controller.network.ClientHandler;
import it.polimi.ingsw.server.controller.network.Message;
import it.polimi.ingsw.server.exceptions.PlayerDisconnectedException;
import it.polimi.ingsw.server.model.Coordinate;
import it.polimi.ingsw.server.model.Game;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static it.polimi.ingsw.server.controller.network.Message.Header.*;

public class RMIClientHandler extends ClientHandler {
    private final GameSupervisor ongoingGames;
    private final String thisPlayerId;

    private final Object heartbeatLock;
    private boolean isAlive;

    private boolean gameOver;

    private final Object pingLock;
    private boolean pingFlag;
    private Message pingMessage;

    private final Object moveLock;
    private boolean moveFlag;
    private Message moveMessage;

    private final Object responseLock;
    private boolean responseFlag;
    private Message responseMessage;

    RMIClientHandler(String username, GameSupervisor ongoingGames) {
        this.ongoingGames = ongoingGames;
        this.thisPlayerId = username;
        this.pingLock = new Object();
        this.heartbeatLock = new Object();
        this.isAlive = true;
        this.gameOver = false;

        this.pingFlag = false;

        this.moveLock = new Object();
        this.moveFlag = false;
        this.responseLock = new Object();
        this.responseFlag = false;
    }

    /**
     * This method starts the thread and executes all the logic of the client handler
     *
     * @author Federico
     */
    @Override
    public void run() {
        boolean temp;
        // Heartbeat
        synchronized (heartbeatLock) {
            temp = isAlive;
        }
        while(temp && !gameOver){
            isAlive = false;
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if(!isAlive) {
            ongoingGames.notifyDisconnection(thisPlayerId);
        }

        // TODO: Gameover
    }

    /**
     * This method sends the first gamestate to the client
     *
     * @param gameState the gamestate to send to the client
     */
    @Override
    public void sendGameState(Game gameState) {
        synchronized (pingLock) {
            pingFlag = true;
            pingMessage = new Message(GAME_UPDATE, gameState.toJson());
        }
    }

    /**
     * This method signals the client that a response was accepted
     *
     * @author Federico
     */
    @Override
    public void sendOk() {
        synchronized (responseLock) {
            responseFlag = true;
            responseMessage = new Message(OK);
            responseLock.notifyAll();
        }
    }

    /**
     * This method asks the client to select a set of tiles
     *
     * @return an array of tiles
     * @author Federico
     */
    @Override
    public Coordinate[] getTiles() throws PlayerDisconnectedException {
        if(!isAlive) throw new PlayerDisconnectedException();
        synchronized (pingLock) {
            pingFlag = true;
            pingMessage = new Message(GET_TILES);
        }

        synchronized (moveLock) {
            while (!moveFlag) {
                try {
                    moveLock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // GameController now knows that the player has selected the tiles
        moveFlag = false;

        // TODO: extract this logic in a method in the superclass
        JSONArray args = moveMessage.getBody();

        Coordinate[] tiles = new Coordinate[args.length()];

        for(int i = 0; i < args.length(); i++){
            tiles[i] = new Coordinate(args.getJSONObject(i));
        }

        return tiles;
    }

    /**
     * This method signals the client that the selected tiles are not valid
     *
     * @author Federico
     */
    @Override
    public void badTile() {
        synchronized (responseLock) {
            responseFlag = true;
            responseMessage = new Message(BAD_TILES, new JSONObject().put("message", "The selected tiles are not valid"));
            responseLock.notifyAll();
        }
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
        //TODO: Modify so that leaderboard is ordered
        JSONArray leaderboardJson = new JSONArray();

        for(String player : leaderboard.keySet()){
            JSONObject playerScore = new JSONObject();
            playerScore.put("username", player);
            playerScore.put("points", leaderboard.get(player));
            leaderboardJson.put(playerScore);
        }

        synchronized (pingLock) {
            pingFlag = true;

            pingMessage = new Message(GAME_OVER, leaderboardJson);
        }

        gameOver = true;
    }

    Message ping(){
        synchronized (heartbeatLock) {
            isAlive = true;
        }
        synchronized (pingLock) {
            if(!pingFlag) return new Message(PING);
            else {
                pingFlag = false;
                return pingMessage;
            }
        }
    }

    // This is crazy, but it's the only way I can think of to make the client wait for the response
    Message submitTiles(Message tiles){
        // Notifying the gameController that the tiles arrived
        synchronized (moveLock) {
            moveFlag = true;
            moveMessage = tiles;
            moveLock.notifyAll();
        }

        while(!responseFlag){
            try {
                synchronized (responseLock) {
                    responseLock.wait();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        responseFlag = false;

        return responseMessage;
    }
}
