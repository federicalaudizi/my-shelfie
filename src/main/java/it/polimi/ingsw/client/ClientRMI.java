package it.polimi.ingsw.client;

import it.polimi.ingsw.server.controller.network.Message;
import it.polimi.ingsw.server.controller.network.rmi.RMIGameInterface;
import it.polimi.ingsw.server.controller.network.rmi.RMILoginInterface;
import it.polimi.ingsw.server.model.Coordinate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import static it.polimi.ingsw.server.controller.network.Message.Header.*;

/**
 * @author Mario Merlo
 */
public class ClientRMI extends Client {
    private RMILoginInterface loginInterface;
    private RMIGameInterface gameInterface;

    public ClientRMI(boolean cli) {
        super(cli);
    }

    @Override
    public void start() throws RemoteException {
        connect();

        login();

        boolean gameOver = false;
        Message reply;
        int headerCode;

        while(!gameOver) {
            reply = gameInterface.ping(getUsername());
            headerCode = reply.getHeaderCode();

            if(headerCode == GET_TILES.getCode())
                getTiles();
            else if(headerCode == GET_COLUMN.getCode())
                getColumn();
            else if(headerCode == GAME_UPDATE.getCode())
                update(reply.getBody().getJSONObject(0));
            else if(headerCode == GAME_OVER.getCode()) {
                gameOver = true;
                gameOver(reply.getBody());
            } else if(headerCode == PING.getCode()) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    void connect() throws RemoteException {
        Registry registry = LocateRegistry.getRegistry();

        try {
            loginInterface = (RMILoginInterface) registry.lookup("RMILoginInterface");
        } catch (NotBoundException e) {
            // TODO Remove debug statement
            System.err.println("Login RMI registry not bound.");
            throw new RuntimeException(e.getMessage());
        }

        try {
            gameInterface = (RMIGameInterface) registry.lookup("RMIGameInterface");
        } catch (NotBoundException e) {
            // TODO Remove debug statement
            System.err.println("Game RMI registry not bound.");
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    void login() throws RemoteException, UnknownError {
        int choice = view.getGameOptions();
        Message reply;
        int headerCode;
        boolean operationCompleted = false;
        while(!operationCompleted) {
            switch(choice) {
                case 1 -> {
                    // Create new game option
                    boolean gameCreated = false;
                    while(!gameCreated) {
                        setServerUsername();
                        reply = loginInterface.createGame(getUsername(), view.getPlayerNumber());
                        headerCode = reply.getHeaderCode();
                        if(headerCode == OK.getCode()) {
                            // TODO Remove debug statement
                            System.err.println("Correctly created game.");
                            gameCreated = true;
                            operationCompleted = true;
                        } else if(headerCode == GENERIC_ERROR.getCode()) {
                            view.showError(reply.getBody().getJSONObject(0).getString("message"));
                        } else throw new UnknownError("An unknown error occurred.");
                    }
                }
                case 2 -> {
                    // Join a new game option
                    boolean gameJoined = false, noGames = false;
                    while(!gameJoined && !noGames) {
                        setServerUsername();
                        reply = loginInterface.getGameList(getUsername());
                        headerCode = reply.getHeaderCode();
                        if (headerCode == GAMES_ID_RESPONSE.getCode()) {
                            // Get game ID list from reply
                            JSONArray gameListJSON = reply.getBody().getJSONObject(0).getJSONArray("games");
                            ArrayList<String> gameList = new ArrayList<>();
                            if (gameListJSON.length() != 0) {
                                for (int i = 0; i < gameListJSON.length(); i++)
                                    gameList.add(gameListJSON.getString(i));
                                // Show ID list to the player
                                reply = loginInterface.joinGame(getUsername(), view.gameIdSelection(gameList));
                                headerCode = reply.getHeaderCode();

                                if(headerCode == OK.getCode()) {
                                    // TODO Remove debug statement
                                    System.err.println("You correctly joined the game.");
                                    gameJoined = true;
                                    operationCompleted = true;
                                } else if (headerCode == BAD_GAME_ID.getCode() || headerCode == GENERIC_ERROR.getCode()) {
                                    view.showError(reply.getBody().getJSONObject(0).getString("message"));
                                }  else throw new UnknownError("An unknown error occurred.");
                            } else {
                                view.showError("There are no ongoing games on this server. Creating a new game.");
                                noGames = true;
                                choice = 1;
                            }
                        }
                    }
                }
                case 3 -> {
                    // Reconnect option
                    if(!reconnect())
                        view.showError("Unable to reconnect. Try creating a new game or joining one.");
                    else
                        operationCompleted = true;
                }
            }
        }


    }

    @Override
    void getTiles() throws RemoteException {
        boolean tileValidation = false, inputValidation;
        ArrayList<Coordinate> coordinates = null;
        JSONArray body = null;
        Message reply;
        int headerCode;

        while(!tileValidation) {
            // --- Client-side validation ---
            // This loop does not break until the input from the user is validated by the client.
            // The validation checks whether the user input the correct number of coordinates -- between 1 and 3.
            // The user is also prompted to confirm his own input with the confirmationPrompt method of ViewCLI.
            inputValidation = false;

            while(!inputValidation) {
                String input = view.getTiles();
                try {
                    coordinates = parseMoveInput(input);
                    inputValidation = true;
                } catch (IllegalStateException e) {
                    view.showError("You entered an invalid number of coordinates. Retry.");
                }
            }
            try {
                body = coordsToJson(coordinates);
            } catch (NullPointerException e) {
                // TODO Remove debug statement
                System.err.println(e.getMessage());
            }

            // --- Server-side validation ---
            // The client packages the tiles selected by the user and then sends them to the server in order to be
            // validated according to the game's rules. This while loop does not break until the server has validated
            // the player's move.
            reply = gameInterface.submitTiles(getUsername(), new Message(SEND_TILES, body));
            headerCode = reply.getHeaderCode();

            // Check reply to either resend coordinates or continue with the move
            if(headerCode == OK.getCode()) {
                tileValidation = true;
            } else if(headerCode == BAD_TILES.getCode() || headerCode == GENERIC_ERROR.getCode()) {
                view.showError("The tiles you chose are not valid. Please retry.");
            } else throw new UnknownError("An unknown error occurred.");
        }
    }

    @Override
    void reconnect() throws RemoteException {

    }

    @Override
    Message getReply() throws NullPointerException {
        return null;
    }

    @Override
    void send(Message message) {

    }
}
