package it.polimi.ingsw.server.controller.network;

import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.controller.GameSupervisorString;
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
    private final GameSupervisorString ongoingGames;
    private GameController<String> game; //Is this the right way to do it?
    private long thisPlayerId;

    public SocketClientHandler(Socket clientSocket, GameSupervisorString ongoingGames) {
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
        //TODO: Implement login

        //TODO: Implement game creation/joining

        //TODO: Implement communication logic

        // clientSocket heartbeat
        while (true) {
            if(!clientSocket.isConnected()){
                game.
            }
        }
    }

    @Override
    public void sendGameState(JSONObject gameState) throws IOException {
        dataOut.println(new Message(VIEW_UPDATE_REQUEST, gameState));
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
        dataOut.println(new Message(TILES_REQUEST));

        // Wait for the response, if it is not valid, catch up by asking again
        try {
            JSONObject answer = new JSONObject(dataIn.readLine());

            if(answer.getString("code").equals(TILES_RESPONSE.toString())){
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
            dataOut.println(new Message(BAD_TILES_ERROR));
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
        dataOut.println(new Message(BAD_TILES_ERROR));
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
        dataOut.println(new Message(COLUMN_REQUEST));

        try {
            JSONObject answer = new JSONObject(dataIn.readLine());

            if(answer.getString("code").equals(COLUMN_RESPONSE.toString())){
                int column = answer.getInt("args");

                // Send the confirmation
                dataOut.println(new Message(OK));

                return column;
            } else {
                // The response was not valid, ask again
                return this.getColumn();
            }
        } catch (IOException e) {
            dataOut.println(new Message(BAD_COLUMN_ERROR));
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
        dataOut.println(new Message(BAD_COLUMN_ERROR));
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
}
