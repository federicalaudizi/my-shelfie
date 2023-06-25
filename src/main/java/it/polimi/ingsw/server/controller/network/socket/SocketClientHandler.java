package it.polimi.ingsw.server.controller.network.socket;

import it.polimi.ingsw.server.controller.GameSupervisor;
import it.polimi.ingsw.server.controller.network.ClientHandler;
import it.polimi.ingsw.server.controller.network.Message;
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
public class SocketClientHandler extends ClientHandler {
    private final Socket clientSocket;
    private PrintWriter dataOut;
    private BufferedReader dataIn;
    private boolean disconnectedPlayer;

    public SocketClientHandler(Socket clientSocket, GameSupervisor ongoingGames) {
        super(ongoingGames);
        this.clientSocket = clientSocket;
        this.disconnectedPlayer = false;

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
            System.out.println(clientSocket.getInetAddress()+": Disconnected at login phase");
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
                    System.out.println(clientSocket.getInetAddress()+": Disconnected at join game phase, warning game");
                    ongoingGames.notifyDisconnection(thisPlayerId);
                }
                else {
                    System.out.println(clientSocket.getInetAddress()+": Disconnected at join game phase, deleting user");
                    ongoingGames.removeUser(thisPlayerId);
                }
                closeSocket();
                return;
            }
        }

        // Run until game over or disconnection
        while (!gameOver && !disconnectedPlayer) {
            try {
                synchronized (clientSocket) {
                    clientSocket.wait();
                }
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
        send(new Message(GAME_UPDATE, gameState.toJson()));
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
        send(new Message(GAME_UPDATE, body));
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
     * This method signal the client that a player disconnected
     *
     * @param disconnectedPlayer the player that disconnected
     */
    @Override
    public void sendDisconnectedPlayer(String disconnectedPlayer) {
        send(new Message(PLAYER_DISCONNECTED, new JSONObject().put("username", disconnectedPlayer)));
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

        try {
            return parseTiles(answer);
        } catch (ClientHandler.WrongHeaderException e) {
            send(new Message(BAD_HEADER, new JSONObject().put("message", "Wrong header")));
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
        send(new Message(BAD_TILES, new JSONObject().put("message", "The tiles you chose are not valid")));
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

        try {
            return parseColumn(answer);
        } catch (WrongHeaderException e) {
            send(new Message(BAD_HEADER, new JSONObject().put("message", "Wrong header")));
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
        send(new Message(BAD_COLUMN, new JSONObject().put("message", "The column you chose is not valid")));
    }

    /**
     * This method signals the client that the game has ended
     *
     * @param leaderboard a JSON object containing the leaderboard
     * @author Federico
     */
    @Override
    public void gameOver(HashMap<String, Integer> leaderboard) {
        send(new Message(GAME_OVER, parseLeaderboard(leaderboard)));
        gameOver = true;
        synchronized (clientSocket) {
            clientSocket.notifyAll();
        }
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
                System.out.println(thisPlayerId+": Successfully logged in");

                // Send the confirmation
                send(new Message(OK));

            } else if(recievedMessage.getHeaderCode() == RECONNECT.getCode()){
                // This is the case of a reconnecting player
                JSONArray body = recievedMessage.getBody();
                thisPlayerId = body.getJSONObject(0).getString("username");

                ongoingGames.oldUser(thisPlayerId, this);
                // Send the confirmation
                System.out.println(thisPlayerId+": Successfully reconnected");
                send(new Message(OK));
                ongoingGames.notifyConnection(thisPlayerId);

            } else {
                // The response was not valid, ask again
                send(new Message(BAD_HEADER, new JSONObject().put("message", "Wrong header")));
                loginPhase();
            }
        } catch (PlayerIdTakenException e) {
            // The player already exists, send the error and restart the login phase
            response = new JSONObject();
            response.put("message", "Username is already taken");
            send(new Message(USERNAME_TAKEN, response));
            loginPhase();
        } catch (PlayerDoesNotExistsException e) {
            // The player does not exist, send the error and restart the login phase
            response = new JSONObject();
            response.put("message", "Player does not exist");
            send(new Message(PLAYER_NOT_FOUND, response));
            loginPhase();
        }
    }

    /**
     * This method manages the game creation/joining phase of the client
     *
     * @author Federico
     */
    private void joinGamePhase() throws PlayerDisconnectedException {
        Message recievedMessage;
        JSONObject response;
        try {
            // Wait for the game request

            recievedMessage = receive();
            System.out.println(thisPlayerId+": "+recievedMessage);

            if(recievedMessage.getHeaderCode() == NEW_GAME_REQUEST.getCode()){
                // This is the case of a new game
                JSONArray body = recievedMessage.getBody();
                int playerNumber = body.getJSONObject(0).getInt("playerNumber");

                String newGameId = ongoingGames.newGame(playerNumber);
                ongoingGames.joinGame(thisPlayerId, newGameId);

                System.out.println(thisPlayerId+": Successfully created a new game");
                send(new Message(OK));
            } else if(recievedMessage.getHeaderCode() == JOIN_GAME_REQUEST.getCode()){
                // This is the case of a joining game
                response = new JSONObject();
                response.put("games", ongoingGames.getGameIds());
                // Send the list of games
                System.out.println(thisPlayerId + ": Wants to join a game");
                send(new Message(GAME_LIST_RESPONSE, response));

                recievedMessage = receive();

                // Wait for the selected gameId
                if(recievedMessage.getHeaderCode() == JOIN_GAME_RESPONSE.getCode()){

                    JSONArray body = recievedMessage.getBody();
                    String gameId = body.getJSONObject(0).getString("gameId");
                    ongoingGames.joinGame(thisPlayerId, gameId);
                    System.out.println(thisPlayerId+": Joined a game");
                    send(new Message(OK));

                } else {
                    // The response was not valid, ask again
                    System.out.println(thisPlayerId+": Wrong request received");
                    send(new Message(BAD_HEADER, new JSONObject().put("message", "Wrong header")));
                    joinGamePhase();
                }
            } else {
                // The response was not valid, ask again
                System.out.println(thisPlayerId+": Wrong request received");
                send(new Message(BAD_HEADER, new JSONObject().put("message", "Wrong header")));
                joinGamePhase();
            }
        } catch (NonExistentGameException e) {
            // An NonExistentGameException occurred, send the error and restart the login phase
            System.out.println(thisPlayerId+": Game does not exist");
            send(new Message(BAD_GAME_ID, new JSONObject().put("message", "Game does not exists")));
            joinGamePhase();
        } catch (ReachedMaxNumberOfPlayers e) {
            // An FullGameException occurred, send the error and restart the login phase
            System.out.println(thisPlayerId+": Game is full");
            send(new Message(BAD_GAME_ID, new JSONObject().put("message", "Game is full")));
            joinGamePhase();
        } catch (NoGamesException e) {
            // There are no games to join
            response = new JSONObject();
            response.put("message", "No games to join");
            System.out.println(thisPlayerId+": No games to join");
            send(new Message(NO_GAMES, response));
            joinGamePhase();
        }
    }

    private void closeSocket(){
        System.out.println(thisPlayerId+": Closing.");

        if(thisPlayerId != null) ongoingGames.notifyDisconnection(thisPlayerId);

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
    private synchronized void send(Message message){
        System.out.println(clientSocket.getInetAddress()+": Sending: "+message);
        dataOut.println(message);
    }

    private Message receive() throws PlayerDisconnectedException{
        try {
            String recievedMessage = dataIn.readLine();
            if(recievedMessage == null) {
                disconnectedPlayer = true;
                synchronized (clientSocket) {
                    clientSocket.notifyAll();
                }
                throw new PlayerDisconnectedException();
            }
            else {
                System.out.println(clientSocket.getInetAddress()+": Recieved: "+recievedMessage);
                return new Message(recievedMessage);
            }
        } catch (IOException e) {
            disconnectedPlayer = true;
            synchronized (clientSocket) {
                clientSocket.notifyAll();
            }
            throw new PlayerDisconnectedException();
        }

    }
}
