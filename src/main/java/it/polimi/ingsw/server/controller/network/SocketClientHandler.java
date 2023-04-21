package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.controller.GameSupervisor;
import it.polimi.ingsw.server.exceptions.FullGameException;
import it.polimi.ingsw.server.exceptions.NonExsistentGameException;
import it.polimi.ingsw.server.exceptions.PlayerIdTakenException;
import it.polimi.ingsw.server.model.Coordinate;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static it.polimi.ingsw.server.controller.network.Message.CommandCode.*;

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
        loginPhase();

        //TODO: Check joinGamePhase() and createGamePhase() for errors
        joinGamePhase();

        // clientSocket heartbeat
        while (true) {
            if(!clientSocket.isConnected()){
                //TODO: Implement disconnection when GameController is implemented
            }
        }
    }

    @Override
    public void sendGameState(JSONObject gameState) throws IOException {
        dataOut.println(new Message(GAME_UPDATE, gameState));
        if(dataIn.readLine().equals(new Message(OK).toString())){
            this.sendGameState(gameState);
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
    public Coordinate[] getTiles() {
        // Send the request
        dataOut.println(new Message(GET_TILES));

        // Wait for the response, if it is not valid, catch up by asking again
        try {
            JSONObject answer = new JSONObject(dataIn.readLine());

            if(answer.getString("code").equals(SEND_TILES.toString())){
                JSONObject[] args = (JSONObject[]) answer.get("args");
                Coordinate[] tiles = new Coordinate[args.length];

                for(int i = 0; i < args.length; i++){
                    tiles[i] = new Coordinate(args[i].getInt("x"), args[i].getInt("y"));
                }

                // Send the confirmation
                dataOut.println(new Message(OK));

                return tiles;
            } else {
                // The response was not valid, ask again
                return this.getTiles();
            }
        } catch (IOException e) {
            dataOut.println(new Message(BAD_TILES));
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
        dataOut.println(new Message(BAD_TILES));
    }

    /**
     * This method asks the client to select a column
     *
     * @return the column selected by the client
     * @author Federico
     */
    @Override
    public int getColumn() {
        // Send the request
        dataOut.println(new Message(GET_COLUMN));

        try {
            JSONObject answer = new JSONObject(dataIn.readLine());

            if(answer.getString("header").equals(SEND_COLUMN.toString())){
                int column = answer.getInt("args");


                return column;
            } else {
                // The response was not valid, ask again
                return this.getColumn();
            }
        } catch (IOException e) {
            dataOut.println(new Message(BAD_COLUMN));
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
        dataOut.println(new Message(BAD_COLUMN));
    }

    /**
     * This method asks the client to select a row
     *
     * @param leaderboard a JSON object containing the leaderboard
     * @author Federico
     */
    @Override
    public void gameOver(JSONObject leaderboard) {
        dataOut.println(new Message(GAME_OVER, leaderboard));
    }

    /**
     * This method manages the login phase of the client, if an error occurs, signals the client the error and restarts the login phase
     *
     * @author Federico
     */
    private void loginPhase() {
        try {
            // Wait for the login request
            JSONObject packet = new JSONObject(dataIn.readLine());
            String header = packet.getString("header");

            if(header.equals(LOGIN_REQUEST.toString())){
                // This is the case of a new player
                JSONObject body = packet.getJSONObject("body");
                String playerId = body.getString("playerId");

                ongoingGames.addUser(playerId, this);

                // Send the confirmation
                dataOut.println(new Message(OK));

            } else if(header.equals(RECONNECT.toString())){
                // This is the case of a reconnecting player
                JSONObject body = packet.getJSONObject("body");
                String playerId = body.getString("playerId");

                if(ongoingGames.userExists(playerId)){
                    ongoingGames.userLogin(playerId, this);
                    // Send the confirmation
                    dataOut.println(new Message(OK));
                } else {
                    // The player does not exist, send the error and restart the login phase
                    JSONObject response = new JSONObject();
                    response.put("message", "Player does not exist");
                    dataOut.println(new Message(GENERIC_ERROR, response));
                    loginPhase();
                }
            } else {
                // The response was not valid, ask again
                JSONObject response = new JSONObject();
                response.put("message", "Wrong request received");
                dataOut.println(new Message(GENERIC_ERROR, response));
                loginPhase();
            }
        } catch (IOException e) {
            // An IOException occurred, send the error and restart the login phase
            JSONObject response = new JSONObject();
            response.put("message", "IOException");
            dataOut.println(new Message(GENERIC_ERROR, response));
            loginPhase();
        } catch (PlayerIdTakenException e) {
            // The player already exists, send the error and restart the login phase
            JSONObject response = new JSONObject();
            response.put("message", "Player already exists");
            dataOut.println(new Message(USERNAME_TAKEN, response));
            loginPhase();
        }
    }

    /**
     * This method manages the game creation/joining phase of the client
     *
     * @author Federico
     */
    private void joinGamePhase(){
        try {
            JSONObject packet = new JSONObject(dataIn.readLine());
            String header = packet.getString("header");

            if(header.equals(NEW_GAME_REQUEST.toString())){
                // This is the case of a new game
                JSONObject body = packet.getJSONObject("body");
                int playerNumber = body.getInt("playerNumber");
                String newGameId = ongoingGames.newGame(playerNumber);
                game = ongoingGames.joinGame(thisPlayerId, newGameId);
                dataOut.println(new Message(OK));
            } else if(header.equals(JOIN_GAME_REQUEST.toString())){
                // This is the case of a joining game
                JSONObject gamesList = new JSONObject();
                gamesList.put("games", ongoingGames.getGamesId());
                // Send the list of games
                dataOut.println(new Message(GAMES_ID_RESPONSE, gamesList));

                packet = new JSONObject(dataIn.readLine());
                header = packet.getString("header");

                // Wait for the selected gameId
                if(header.equals(JOIN_GAME_RESPONSE.toString())){
                    JSONObject body = packet.getJSONObject("body");
                    String gameId = body.getString("gameId");
                    game = ongoingGames.joinGame(thisPlayerId, gameId);
                    dataOut.println(new Message(OK));
                } else {
                    // The response was not valid, ask again
                    JSONObject response = new JSONObject();
                    response.put("message", "Wrong request received");
                    dataOut.println(new Message(GENERIC_ERROR, response));
                    joinGamePhase();
                }
            } else {
                // The response was not valid, ask again
                JSONObject response = new JSONObject();
                response.put("message", "Wrong request received");
                dataOut.println(new Message(GENERIC_ERROR, response));
                joinGamePhase();
            }
        } catch (IOException e) {
            // An IOException occurred, send the error and restart the login phase
            JSONObject response = new JSONObject();
            response.put("message", "IOException");
            dataOut.println(new Message(GENERIC_ERROR, response));
            joinGamePhase();
        } catch (NonExsistentGameException e) {
            // An NonExistentGameException occurred, send the error and restart the login phase
            dataOut.println(new Message(BAD_GAME_ID));
            joinGamePhase();
        } catch (FullGameException e) {
            // An FullGameException occurred, send the error and restart the login phase
            dataOut.println(new Message(BAD_GAME_ID));
            joinGamePhase();
        }
    }
}
