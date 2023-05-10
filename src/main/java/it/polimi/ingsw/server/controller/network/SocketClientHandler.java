package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.server.controller.GameSupervisor;
import it.polimi.ingsw.server.exceptions.*;
import it.polimi.ingsw.server.model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import static it.polimi.ingsw.server.controller.network.Message.Header.*;

/**
 * This class handles the exchange of messages with the client and runs as a thread.
 * This class represents che client on the server side, being a thread, after its initialization, it will run on command by the game controller it is associated to.
 * The logic behind it is that it first handshakes with the client, handles its login, then deals with the player the creation or joining of a game.
 * After that, it will be at the disposal of the game controller, which will send the commands that the player has to execute.
 */
public class SocketClientHandler extends ClientHandler{
    private final Socket clientSocket;
    private PrintWriter dataOut;
    private BufferedReader dataIn;
    private final GameSupervisor ongoingGames;
    private String thisPlayerId;

    private boolean pendingGameStateFlag;
    private JSONObject pendingGameState;

    private boolean gameOver;

    public SocketClientHandler(Socket clientSocket, GameSupervisor ongoingGames) {
        this.clientSocket = clientSocket;
        this.ongoingGames = ongoingGames;
        this.pendingGameStateFlag = false;
        this.gameOver = false;

        try {
            // Get the input and output streams of the socket
            dataOut = new PrintWriter(clientSocket.getOutputStream(), true);
            dataIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            loginPhase();
        } catch (PlayerDisconnectedException e) {
            System.out.println(clientSocket.getInetAddress()+": Disconnected at during login phase");
            // If the player logged and then
            if(thisPlayerId != null) ongoingGames.removeUser(thisPlayerId);

            closeSocket();
            return;
        }

        if(!ongoingGames.userIsInGame(thisPlayerId)) {
            try {
                joinGamePhase();
            } catch (PlayerDisconnectedException e) {
                if(ongoingGames.userIsInGame(thisPlayerId)) {
                    System.out.println(clientSocket.getInetAddress()+": Disconnected at during join game phase, warning game");
                    ongoingGames.notifyDisconnection(thisPlayerId);
                }
                else {
                    System.out.println(clientSocket.getInetAddress()+": Disconnected at during join game phase, deleting user");
                    ongoingGames.removeUser(thisPlayerId);
                }
                closeSocket();
                return;
            }
        }

        // Run until game over or disconnection
        while (!gameOver && clientSocket.isConnected()) {

            // Heartbeat
            // TODO: To implement an heartbeat, it must be done in the client too, the server needs to send pings and wait for answars, this means there must be a lock on the communication stream.

            // Check if there is a pending gamestate to send
            if(pendingGameStateFlag){
                // TODO: check why it never enters here
                System.out.println(clientSocket.getInetAddress()+": Sending gamestate");
                send(new Message(GAME_UPDATE, pendingGameState));
                pendingGameStateFlag = false;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }

        System.out.println(clientSocket.getInetAddress()+": Terminating thread");
        closeSocket();
    }

    /**
     * This method sends the first gamestate to the client
     *
     * @param gameState the gamestate to send to the client
     */
    @Override
    public void sendGameState(Game gameState) {
        System.out.println(clientSocket.getInetAddress()+": I have to send the gamestate");

        // TODO: Asynchronous gamestate sending
        /*pendingGameState = gameState.toJson();
        pendingGameStateFlag = true;*/

        send(new Message(GAME_UPDATE, gameState.toJson()));
    }

    /**
     * This method signals the client that a response was accepted
     *
     * @author Federico
     */
    @Override
    public void sendOk() {
        send(new Message(OK));
    }

    /**
     * This method asks the client to select a set of tiles
     *
     * @return an array of tiles
     * @author Federico
     */
    @Override
    public Coordinate[] getTiles() throws PlayerDisconnectedException {
        // Send the request
        send(new Message(GET_TILES));

        // Wait for the response, if it is not valid, catch up by asking again
        Message answer = receive();

        if(answer.getHeaderCode() == SEND_TILES.getCode()){
            JSONArray args = answer.getBody();

            Coordinate[] tiles = new Coordinate[args.length()];

            for(int i = 0; i < args.length(); i++){
                tiles[i] = new Coordinate(args.getJSONObject(i));
            }

            return tiles;
        } else {
            // The response was not valid, ask again
            send(new Message(GENERIC_ERROR));
            return this.getTiles();
        }
    }

    /**
     * This method signals the client that the selected tiles are not valid
     *
     * @author Federico
     */
    @Override
    public void badTile() {
        send(new Message(BAD_TILES));
    }

    /**
     * This method asks the client to select a column
     *
     * @return the column selected by the client
     * @author Federico
     */
    @Override
    public int getColumn() throws PlayerDisconnectedException {
        // Send the request
        send(new Message(GET_COLUMN));

        Message answer = receive();

        if(answer.getHeaderCode() == SEND_COLUMN.getCode()){

            JSONArray args = answer.getBody();
            JSONObject column = (JSONObject) args.get(0);

            return column.getInt("column");
        } else {
            // The response was not valid, ask again
            send(new Message(GENERIC_ERROR));
            return this.getColumn();
        }
    }

    /**
     * This method signals the client that the selected column is not valid
     *
     * @author Federico
     */
    @Override
    public void badColumn() {
        send(new Message(BAD_COLUMN));
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

        send(new Message(GAME_OVER, leaderboardJson));
        gameOver = true;
    }

    /**
     * This method manages the login phase of the client, if an error occurs, signals the client the error and restarts the login phase
     *
     * @author Federico
     */
    private void loginPhase() throws PlayerDisconnectedException {
        Message recievedMessage;
        JSONObject response;
        try {
            // Wait for the login request
            recievedMessage = receive();

            if(recievedMessage.getHeaderCode() == LOGIN_REQUEST.getCode()){
                // This is the case of a new player
                JSONArray body = recievedMessage.getBody();
                thisPlayerId = body.getJSONObject(0).getString("username");

                ongoingGames.newUser(thisPlayerId, this);

                // Send the confirmation
                send(new Message(OK));

            } else if(recievedMessage.getHeaderCode() == RECONNECT.getCode()){
                // This is the case of a reconnecting player
                JSONArray body = recievedMessage.getBody();
                thisPlayerId = body.getJSONObject(0).getString("username");

                ongoingGames.oldUser(thisPlayerId, this);
                // Send the confirmation
                System.out.println(clientSocket.getInetAddress()+": Successfully reconnected");
                send(new Message(OK));

            } else {
                // The response was not valid, ask again
                response = new JSONObject();
                response.put("message", "Wrong request received");
                send(new Message(GENERIC_ERROR, response));
                loginPhase();
            }
        } catch (PlayerIdTakenException e) {
            // The player already exists, send the error and restart the login phase
            response = new JSONObject();
            response.put("message", "Player already exists");
            send(new Message(USERNAME_TAKEN, response));
            loginPhase();
        } catch (PlayerDoesNotExistsException e) {
            // The player does not exist, send the error and restart the login phase
            response = new JSONObject();
            response.put("message", "Player does not exist");
            send(new Message(GENERIC_ERROR, response));
            loginPhase();
        }
    }

    /**
     * This method manages the game creation/joining phase of the client
     *
     * @author Federico
     */
    private void joinGamePhase() throws PlayerDisconnectedException {
        //TODO: Fix recursion
        Message recievedMessage;
        JSONObject response;
        try {
            // Wait for the game request

            recievedMessage = receive();
            System.out.println(clientSocket.getInetAddress()+": "+recievedMessage);

            if(recievedMessage.getHeaderCode() == NEW_GAME_REQUEST.getCode()){
                // This is the case of a new game
                JSONArray body = recievedMessage.getBody();
                int playerNumber = body.getJSONObject(0).getInt("playerNumber");
                String newGameId = ongoingGames.newGame(playerNumber);
                ongoingGames.joinGame(thisPlayerId, newGameId);
                System.out.println(clientSocket.getInetAddress()+": Successfully created a new game");
                send(new Message(OK));
            } else if(recievedMessage.getHeaderCode() == JOIN_GAME_REQUEST.getCode()){
                // This is the case of a joining game
                response = new JSONObject();
                response.put("games", ongoingGames.getGameIds());
                // Send the list of games
                System.out.println(clientSocket.getInetAddress() + ": Wants to join a game");
                send(new Message(GAMES_ID_RESPONSE, response));

                recievedMessage = receive();

                // Wait for the selected gameId
                if(recievedMessage.getHeaderCode() == JOIN_GAME_RESPONSE.getCode()){

                    JSONArray body = recievedMessage.getBody();
                    String gameId = body.getJSONObject(0).getString("gameId");
                    ongoingGames.joinGame(thisPlayerId, gameId);
                    System.out.println(clientSocket.getInetAddress()+": Joined a game");
                    send(new Message(OK));

                } else {
                    // The response was not valid, ask again
                    response = new JSONObject();
                    response.put("message", "Wrong request received");
                    System.out.println(clientSocket.getInetAddress()+": Wrong request received");
                    send(new Message(GENERIC_ERROR, response));
                    joinGamePhase();
                }
            } else {
                // The response was not valid, ask again
                response = new JSONObject();
                response.put("message", "Wrong request received");
                System.out.println(clientSocket.getInetAddress()+": Wrong request received");
                send(new Message(GENERIC_ERROR, response));
                joinGamePhase();
            }
        } catch (NonExsistentGameException e) {
            // An NonExistentGameException occurred, send the error and restart the login phase
            System.out.println(clientSocket.getInetAddress()+": Game does not exist");
            send(new Message(BAD_GAME_ID));
            joinGamePhase();
        } catch (ReachedMaxNumberOfPlayers e) {
            // An FullGameException occurred, send the error and restart the login phase
            System.out.println(clientSocket.getInetAddress()+": Game does not exist");
            send(new Message(BAD_GAME_ID));
            joinGamePhase();
        } catch (NoGamesException e) {
            // There are no games to join
            response = new JSONObject();
            response.put("message", "No games to join");
            System.out.println(clientSocket.getInetAddress()+": No games to join");
            send(new Message(NO_GAMES, response));
            joinGamePhase();
        }
    }

    private void closeSocket(){
        System.out.println(clientSocket.getInetAddress()+": Closing.");

        ongoingGames.notifyDisconnection(thisPlayerId);

        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a message to the client
     *
     * @param message the message to send
     */
    private void send(Message message){
        System.out.println(clientSocket.getInetAddress()+": Sending: "+message);
        dataOut.println(message);
    }

    private Message receive() throws PlayerDisconnectedException{
        try {
            String recievedMessage = dataIn.readLine();
            if(recievedMessage == null) throw new PlayerDisconnectedException();
            else {
                System.out.println(clientSocket.getInetAddress()+": Recieved: "+recievedMessage);
                return new Message(recievedMessage);
            }
        } catch (IOException e) {
            System.out.println(clientSocket.getInetAddress()+": Disconnected!");
            closeSocket();
            throw new PlayerDisconnectedException();
        }

    }
}
