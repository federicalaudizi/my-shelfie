package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.server.controller.GameController;
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
    private GameController game;
    private String thisPlayerId;

    private boolean gameOver;

    public SocketClientHandler(Socket clientSocket, GameSupervisor ongoingGames) {
        this.clientSocket = clientSocket;
        this.ongoingGames = ongoingGames;

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
        try{
            loginPhase();
        } catch (Exception e){
            System.out.println(clientSocket.getInetAddress()+": Login exception: "+e.getMessage());
            closeSocket();
            throw new RuntimeException(e);
        }

        // clientSocket heartbeat
        while (!gameOver && clientSocket.isConnected()) {
            if(!clientSocket.isConnected()){
                game.notifyDisconnection(thisPlayerId);
                System.out.println(clientSocket.getInetAddress()+": Disconnected!");
                break;
            }
        }

        closeSocket();
    }

    /**
     * This method sends the first gamestate to the client
     *
     * @param gameState the gamestate to send to the client
     */
    @Override
    public void sendGameState(Game gameState) {
        System.out.println(clientSocket.getInetAddress()+": Sending gamestate: "+gameState.toJson());
        dataOut.println(new Message(GAME_UPDATE, gameState.toJson()));

        try {
            JSONObject answer = new JSONObject(dataIn.readLine());
            // If the client does not acknowledge the message, send it again
            if(answer.getInt("header") != OK.getCode()) sendGameState(gameState);
        } catch (IOException e) {
            game.notifyDisconnection(thisPlayerId);
            closeSocket();
        }
    }

    /**
     * This method sends the updates of the gamestate to the client at the end of each player's turn
     *
     * @param board           the board of the game
     * @param player          the player who just played
     * @param pointDeckValues the values of the point decks
     */
    @Override
    public void sendGameState(Board board, Player player, int[] pointDeckValues, boolean lastTurnFlag){
        JSONObject body = new JSONObject();
        body.put("board", board.toJSON());
        body.put("player", player.toJson());
        body.put("pointDeckValues", new JSONArray(pointDeckValues));
        body.put("lastTurn", true);

        dataOut.println(new Message(GAME_UPDATE, body));

        try {
            JSONObject answer = new JSONObject(dataIn.readLine());

            // If the client does not acknowledge the message, send it again
            if(answer.getInt("header") != OK.getCode()) sendGameState(board, player, pointDeckValues, lastTurnFlag);
        } catch (IOException e) {
            game.notifyDisconnection(thisPlayerId);
            closeSocket();
        }
    }

    /**
     * This method signals the client that a response was accepted
     *
     * @author Federico
     */
    @Override
    public void sendOk() {
        dataOut.println(new Message(OK));
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
        dataOut.println(new Message(GET_TILES));

        // Wait for the response, if it is not valid, catch up by asking again
        try {
            JSONObject answer = new JSONObject(dataIn.readLine());

            if(answer.getString("header").equals(SEND_TILES.toString())){
                JSONArray args = (JSONArray) answer.get("args");

                //TODO: Check if this is the correct way to do it

                Coordinate[] tiles = new Coordinate[args.length()];

                for(int i = 0; i < args.length(); i++){
                    //tiles[i] = new Coordinate(((JSONObject) args.get(i)).getInt("x"), ((JSONObject) args.get(i)).getInt("y"));

                    //TODO: Check if this is the correct way to do it
                    tiles[i] = (Coordinate) args.get(i);
                }

                // Send the confirmation
                dataOut.println(new Message(OK));

                return tiles;
            } else {
                // The response was not valid, ask again
                dataOut.println(new Message(GENERIC_ERROR));
                return this.getTiles();
            }
        } catch (IOException e) {
            dataOut.println(new Message(GENERIC_ERROR));
            game.notifyDisconnection(thisPlayerId);
            closeSocket();

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
        dataOut.println(new Message(BAD_TILES));
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
        dataOut.println(new Message(GET_COLUMN));

        try {
            JSONObject answer = new JSONObject(dataIn.readLine());

            if(answer.getString("header").equals(SEND_COLUMN.toString())){
                // Send the confirmation
                dataOut.println(new Message(OK));

                JSONArray args = (JSONArray) answer.get("args");
                JSONObject column = (JSONObject) args.get(0);

                return column.getInt("column");
            } else {
                // The response was not valid, ask again
                dataOut.println(new Message(GENERIC_ERROR));
                return this.getColumn();
            }
        } catch (IOException e) {
            dataOut.println(new Message(GENERIC_ERROR));
            game.notifyDisconnection(thisPlayerId);
            closeSocket();

            throw new PlayerDisconnectedException();
        }
    }

    /**
     * This method signals the client that the selected column is not valid
     *
     * @author Federico
     */
    @Override
    public void badColumn() {
        dataOut.println(new Message(BAD_COLUMN));
    }

    /**
     * This method signals the client that the game has ended
     *
     * @param leaderboard a JSON object containing the leaderboard
     * @author Federico
     */
    @Override
    public void gameOver(HashMap<String, Integer> leaderboard) {
        //TODO: Write the gameOver method
        // dataOut.println(new Message(GAME_OVER, leaderboard));
        gameOver = true;
    }

    /**
     * This method manages the login phase of the client, if an error occurs, signals the client the error and restarts the login phase
     *
     * @author Federico
     */
    private void loginPhase() {
        Message recievedMessage;
        JSONObject response;
        try {
            // Wait for the login request
            recievedMessage = new Message(dataIn.readLine());
            System.out.println(clientSocket.getInetAddress()+": "+recievedMessage);

            if(recievedMessage.getHeaderCode() == LOGIN_REQUEST.getCode()){
                // This is the case of a new player
                JSONArray body = recievedMessage.getBody();
                thisPlayerId = body.getJSONObject(0).getString("username");

                ongoingGames.newUser(thisPlayerId, this);

                // Send the confirmation
                System.out.println(clientSocket.getInetAddress()+": Successfully logged in");
                dataOut.println(new Message(OK));

                joinGamePhase();

            } else if(recievedMessage.getHeaderCode() == RECONNECT.getCode()){
                // This is the case of a reconnecting player
                JSONArray body = recievedMessage.getBody();
                thisPlayerId = body.getJSONObject(0).getString("username");

                game = ongoingGames.oldUser(thisPlayerId, this);
                game.notifyConnection(thisPlayerId);
                // Send the confirmation
                System.out.println(clientSocket.getInetAddress()+": Successfully reconnected");
                dataOut.println(new Message(OK));

            } else {
                // The response was not valid, ask again
                response = new JSONObject();
                response.put("message", "Wrong request received");
                dataOut.println(new Message(GENERIC_ERROR, response));
                loginPhase();
            }
        } catch (IOException e) {
            // An IOException occurred, send the error and restart the login phase
            response = new JSONObject();
            response.put("message", "IOException");
            dataOut.println(new Message(GENERIC_ERROR, response));
            closeSocket();
        } catch (PlayerIdTakenException e) {
            // The player already exists, send the error and restart the login phase
            response = new JSONObject();
            response.put("message", "Player already exists");
            dataOut.println(new Message(USERNAME_TAKEN, response));
            loginPhase();
        } catch (PlayerDoesNotExistsException e) {
            // The player does not exist, send the error and restart the login phase
            response = new JSONObject();
            response.put("message", "Player does not exist");
            dataOut.println(new Message(GENERIC_ERROR, response));
            loginPhase();
        }
    }

    /**
     * This method manages the game creation/joining phase of the client
     *
     * @author Federico
     */
    private void joinGamePhase(){
        Message recievedMessage;
        JSONObject response;
        try {
            // Wait for the game request

            recievedMessage = new Message(dataIn.readLine());
            System.out.println(clientSocket.getInetAddress()+": "+recievedMessage);

            if(recievedMessage.getHeaderCode() == NEW_GAME_REQUEST.getCode()){
                // This is the case of a new game
                JSONArray body = recievedMessage.getBody();
                int playerNumber = body.getJSONObject(0).getInt("playerNumber");
                String newGameId = ongoingGames.newGame(playerNumber);
                game = ongoingGames.joinGame(thisPlayerId, newGameId);
                System.out.println(clientSocket.getInetAddress()+": Successfully created a new game");
                dataOut.println(new Message(OK));
            } else if(recievedMessage.getHeaderCode() == JOIN_GAME_REQUEST.getCode()){
                // This is the case of a joining game
                response = new JSONObject();
                response.put("games", ongoingGames.getGameIds());
                // Send the list of games
                System.out.println(clientSocket.getInetAddress() + ": Wants to join a game");
                dataOut.println(new Message(GAMES_ID_RESPONSE, response));

                recievedMessage = new Message(dataIn.readLine());
                System.out.println(clientSocket.getInetAddress()+": "+recievedMessage);

                // Wait for the selected gameId
                if(recievedMessage.getHeaderCode() == JOIN_GAME_RESPONSE.getCode()){

                    JSONArray body = recievedMessage.getBody();
                    String gameId = body.getJSONObject(0).getString("gameId");
                    game = ongoingGames.joinGame(thisPlayerId, gameId);
                    System.out.println(clientSocket.getInetAddress()+": Joined a game");
                    dataOut.println(new Message(OK));

                } else {
                    // The response was not valid, ask again
                    response = new JSONObject();
                    response.put("message", "Wrong request received");
                    System.out.println(clientSocket.getInetAddress()+": Wrong request received");
                    dataOut.println(new Message(GENERIC_ERROR, response));
                    joinGamePhase();
                }
            } else {
                // The response was not valid, ask again
                response = new JSONObject();
                response.put("message", "Wrong request received");
                System.out.println(clientSocket.getInetAddress()+": Wrong request received");
                dataOut.println(new Message(GENERIC_ERROR, response));
                joinGamePhase();
            }
        } catch (IOException e) {
            // An IOException occurred, send the error and restart the login phase
            response = new JSONObject();
            response.put("message", "IOException");
            System.out.println(clientSocket.getInetAddress()+": IOException");
            dataOut.println(new Message(GENERIC_ERROR, response));
            closeSocket();
        } catch (NonExsistentGameException e) {
            // An NonExistentGameException occurred, send the error and restart the login phase
            System.out.println(clientSocket.getInetAddress()+": Game does not exist");
            dataOut.println(new Message(BAD_GAME_ID));
            joinGamePhase();
        } catch (ReachedMaxNumberOfPlayers e) {
            // An FullGameException occurred, send the error and restart the login phase
            System.out.println(clientSocket.getInetAddress()+": Game does not exist");
            dataOut.println(new Message(BAD_GAME_ID));
            joinGamePhase();
        } catch (NoGamesException e) {
            // There are no games to join
            response = new JSONObject();
            response.put("message", "No games to join");
            System.out.println(clientSocket.getInetAddress()+": No games to join");
            dataOut.println(new Message(NO_GAMES, response));
            joinGamePhase();
        }
    }

    private void closeSocket(){
        System.out.println(clientSocket.getInetAddress()+": Closing.");
        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
