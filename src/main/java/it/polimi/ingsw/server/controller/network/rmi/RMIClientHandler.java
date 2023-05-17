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

    private final Object tilesLock;
    private boolean tilesFlag;
    private Message tilesMessage;

    private final Object columnLock;
    private boolean columnFlag;
    private Message columnMessage;

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

        this.tilesLock = new Object();
        this.tilesFlag = false;
        this.responseLock = new Object();
        this.responseFlag = false;
        this.columnLock = new Object();
        this.columnFlag = false;
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

        // Sending the tiles request
        synchronized (pingLock) {
            pingFlag = true;
            pingMessage = new Message(GET_TILES);
        }

        synchronized (tilesLock) {
            try {
                // Wait for 10 seconds the answer
                tilesLock.wait(10000);
                // If the answer is not received, the player disconnected
                if(!tilesFlag) throw new PlayerDisconnectedException();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // GameController now knows that the player has selected the tiles
        tilesFlag = false;

        try {
            return extractTiles(tilesMessage);
        } catch (ClientHandler.WrongHeaderException ignored) {
            // TODO: This exception should never be thrown
            throw new PlayerDisconnectedException();
        }
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
    public int getColumn() throws PlayerDisconnectedException {
        if(!isAlive) throw new PlayerDisconnectedException();
        // Sending the column request
        synchronized (pingLock) {
            pingFlag = true;
            pingMessage = new Message(GET_COLUMN);
        }

        synchronized (columnLock) {
            try {
                // Wait for 10 seconds the answer
                columnLock.wait(10000);
                // If the answer is not received, the player disconnected
                if(!columnFlag) throw new PlayerDisconnectedException();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // GameController now knows that the player has selected the tiles
        columnFlag = false;
        try {
            return extractColumn(columnMessage);
        } catch (WrongHeaderException ignored) {
            // TODO: This exception should never be thrown
            return 0;
        }
    }

    /**
     * This method signals the client that the selected column is not valid
     *
     * @author Federico
     */
    @Override
    public void badColumn() {
        synchronized (responseLock) {
            responseFlag = true;
            responseMessage = new Message(BAD_COLUMN, new JSONObject().put("message", "The selected column is not valid"));
            responseLock.notifyAll();
        }
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
        synchronized (tilesLock) {
            tilesFlag = true;
            tilesMessage = tiles;
            tilesLock.notifyAll();
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

    Message submitColumn(Message column){
        // Notifying the gameController that the column arrived
        synchronized (columnLock) {
            columnFlag = true;
            columnMessage = column;
            columnLock.notifyAll();
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
