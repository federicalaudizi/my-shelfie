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
    private final Object heartbeatLock;
    private boolean terminated;
    private boolean isAlive;
    private boolean suspendHeartbeat;

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

    private boolean notificationFlag;
    private Message notificationMessage;

    private boolean stateFlag;
    private Message stateMessage;

    RMIClientHandler(String username, GameSupervisor ongoingGames) {
        super(ongoingGames);
        this.thisPlayerId = username;
        this.pingLock = new Object();
        this.heartbeatLock = new Object();
        this.suspendHeartbeat = false;
        this.terminated = false;
        this.isAlive = true;

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
        // Heartbeat
        while(!gameOver && !terminated){
            try {
                synchronized (heartbeatLock) {
                    // If the client is picking tiles or column, i suspend the heartbeat
                    if(suspendHeartbeat) heartbeatLock.wait();
                    // Else i wait for 30 seconds
                    else {
                        heartbeatLock.wait(30000);
                        if(!isAlive) terminated = true;
                        isAlive = false;
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Exited while before game is over, i must notify the disconnection;
        if (!gameOver) ongoingGames.notifyDisconnection(thisPlayerId);
    }

    /**
     * This method sends the first gamestate to the client
     *
     * @param gameState the gamestate to send to the client
     */
    @Override
    public void sendGameState(Game gameState) {
        stateFlag = true;
        stateMessage = new Message(GAME_UPDATE, gameState.toJson());
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
        JSONObject objectiveWinner = new JSONObject();

        objectiveWinner.put("username", player);
        objectiveWinner.put("objective", gainedObjective);

        JSONArray body = new JSONArray();
        body.put(gameState.toJson());
        body.put(objectiveWinner);

        stateFlag = true;
        stateMessage = new Message(GAME_UPDATE, body);
    }

    /**
     * This method signals the client that a response was accepted
     *
     * @author Federico
     */
    @Override
    public void sendOk() {
        sendResponse(new Message(OK));
    }

    /**
     * This method signal the client that a player disconnected
     *
     * @param disconnectedPlayer the player that disconnected
     */
    @Override
    public void sendDisconnectedPlayer(String disconnectedPlayer) {
        notificationFlag = true;
        notificationMessage = new Message(PLAYER_DISCONNECTED, new JSONObject().put("username", disconnectedPlayer));
    }

    /**
     * This method asks the client to select a set of tiles
     *
     * @return an array of tiles
     * @author Federico
     */
    @Override
    public Coordinate[] getTiles() throws PlayerDisconnectedException {
        if(terminated) throw new PlayerDisconnectedException();
        else suspendHeartbeat = true;

        // Sending the tiles request
        sendPing(new Message(GET_TILES));

        synchronized (tilesLock) {
            try {
                // Wait for 120 seconds the answer
                tilesLock.wait(10000);
                // If the answer is not received, the player disconnected
                if(!tilesFlag) terminate();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // GameController now knows that the player has selected the tiles
        tilesFlag = false;
        // Resume the heartbeat
        suspendHeartbeat = false;
        synchronized (heartbeatLock) {
            heartbeatLock.notifyAll();
        }
        try {
            return parseTiles(tilesMessage);
        } catch (ClientHandler.WrongHeaderException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method signals the client that the selected tiles are not valid
     *
     * @author Federico
     */
    @Override
    public void badTile(String info) {
        sendResponse(new Message(BAD_TILES, new JSONObject().put("message", info)));
    }

    /**
     * This method asks the client to select a column
     *
     * @return the column selected by the client
     * @author Federico
     */
    @Override
    public int getColumn() throws PlayerDisconnectedException {
        if(terminated) throw new PlayerDisconnectedException();
        else suspendHeartbeat = true;

        // Sending the column request
        sendPing(new Message(GET_COLUMN));

        synchronized (columnLock) {
            try {
                // Wait for 120 seconds the answer
                columnLock.wait(120000);
                // If the answer is not received, the player disconnected
                if(!columnFlag) terminate();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // GameController now knows that the player has selected the tiles
        columnFlag = false;
        // Resume the heartbeat
        suspendHeartbeat = false;
        synchronized (heartbeatLock){
            heartbeatLock.notifyAll();
        }
        try {
            return parseColumn(columnMessage);
        } catch (WrongHeaderException ignored) {
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
        sendResponse(new Message(BAD_COLUMN, new JSONObject().put("message", "The selected column is not valid")));
    }

    /**
     * This method signals the client that the game has ended
     *
     * @param leaderboard a JSON object containing the leaderboard
     * @author Federico
     */
    @Override
    public void gameOver(HashMap<String, Integer> leaderboard) {
        try {
            sendPing(new Message(GAME_OVER, parseLeaderboard(leaderboard)));
        } catch (PlayerDisconnectedException ignored) {
        }

        gameOver = true;
    }

    /**
     * Helper method to send a ping message to the client
     *
     * @param message the message to send
     * @throws PlayerDisconnectedException if the player disconnected
     */
    private void sendPing(Message message) throws PlayerDisconnectedException {
        synchronized (pingLock) {
            if(pingFlag){
                // If there still is a pending message, wait for 30 seconds
                try {
                    System.err.println(thisPlayerId + ": waiting for ping to be received");
                    pingLock.wait(300000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // If the message is still pending, the player disconnected
                if(pingFlag) {
                    System.err.println(thisPlayerId + ": ping time out");
                    terminate();
                }
            }
            System.out.println(thisPlayerId + ": sending ping "+message.toString());
            pingFlag = true;
            pingMessage = message;
        }
    }

    /**
     * Helper method to send a response to the client
     *
     * @param message the message to send
     */
    private void sendResponse(Message message){
        synchronized (responseLock) {
            System.out.println(thisPlayerId + ": sending response "+message.toString());
            responseFlag = true;
            responseMessage = message;
            responseLock.notifyAll();
        }
    }

    /**
     * Helper method to terminate the connection
     *
     * @throws PlayerDisconnectedException to signal the disconnection
     */
    private void terminate() throws PlayerDisconnectedException{
        System.out.println(thisPlayerId + ": terminating");
        terminated = true;
        synchronized (heartbeatLock) {
            heartbeatLock.notifyAll();
        }
        throw new PlayerDisconnectedException();
    }

    /**
     * This method retrieves the ping message
     *
     * @return the ping message
     */
    Message ping(){
        //System.out.println(thisPlayerId + ": retrieved ping message");
        synchronized (heartbeatLock) {
            isAlive = true;
            heartbeatLock.notifyAll();
        }
        if(terminated) {
            // If the server terminates this client, warn him
            HashMap<String, Integer> fakeLeaderboard = new HashMap<>();
            fakeLeaderboard.put("Server Timeout", 808);
            fakeLeaderboard.put("You Got Disconnected", -1);
            return new Message(PLAYER_TERMINATED, parseLeaderboard(fakeLeaderboard));
        }
        synchronized (pingLock) {
            if(notificationFlag){
                notificationFlag = false;
                return notificationMessage;
            }
            if(stateFlag){
                stateFlag = false;
                return stateMessage;
            }
            if(!pingFlag) return new Message(PING);
            else {
                pingFlag = false;
                pingLock.notify();
                return pingMessage;
            }
        }
    }

    /**
     * This method submits the tiles selected by the client
     *
     * @param tiles the tiles selected by the client
     * @return a message containing the result of the operation
     */
    Message submitTiles(Message tiles){
        if(terminated) return new Message(PLAYER_TERMINATED, new JSONObject().put("message", "Connection timed out"));
        System.out.println(thisPlayerId + ": posted tiles message");
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
        System.out.println(thisPlayerId + ": retrieved tiles message");
        return responseMessage;
    }

    /**
     * This method submits the column selected by the client
     *
     * @param column the column selected by the client
     * @return a message containing the result of the operation
     */
    Message submitColumn(Message column){
        if(terminated) return new Message(PLAYER_TERMINATED, new JSONObject().put("message", "Connection timed out"));
        // Notifying the gameController that the column arrived
        System.out.println(thisPlayerId + ": posted column message");
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
        System.out.println(thisPlayerId + ": retrieved column message");
        return responseMessage;
    }
}
