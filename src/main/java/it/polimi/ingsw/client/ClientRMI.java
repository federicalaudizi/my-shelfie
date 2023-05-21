package it.polimi.ingsw.client;

import it.polimi.ingsw.server.controller.network.Message;
import it.polimi.ingsw.server.controller.network.rmi.RMIGameInterface;
import it.polimi.ingsw.server.controller.network.rmi.RMILoginInterface;
import org.json.JSONArray;

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
        boolean exit = false;
        while(!exit) {
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
                    update(reply.getBody());
                else if(headerCode == GAME_OVER.getCode()) {
                    gameOver = true;
                    gameOver(reply.getBody());
                    exit = view.continueScreen();
                } else if(headerCode == PING.getCode()) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
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
        Message reply;
        int headerCode;
        boolean operationCompleted = false, loggedIn = false;

        while(!operationCompleted) {
            // Choose whether to create a game, join a game or reconnect to one
            int choice = getGameChoice();

            // Set the server username when logging in normally
            if(choice <= 2 && !loggedIn) {
                setServerUsername();
                loggedIn = true;
            }

            switch(choice) {
                case 1 -> {
                    // Set the player number and validate it
                    boolean validPlayerNumber = false;
                    int playerNumber = 0;
                    while(!validPlayerNumber) {
                        playerNumber = view.getPlayerNumber();
                        validPlayerNumber = checkPlayerNumber(playerNumber);
                    }

                    reply = loginInterface.createGame(getUsername(), playerNumber);
                    headerCode = reply.getHeaderCode();
                    if(headerCode == OK.getCode()) {
                        // TODO Remove debug statement
                        System.err.println("Correctly created game.");
                        operationCompleted = true;
                    } else showError(reply);
                }
                case 2 -> {
                    // Join a new game option
                    reply = loginInterface.getGameList(getUsername());
                    headerCode = reply.getHeaderCode();

                    if(headerCode == GAME_LIST_RESPONSE.getCode()) {
                        // Get game ID list from reply
                        JSONArray gameListJSON = reply.getBody().getJSONObject(0).getJSONArray("games");
                        ArrayList<String> gameList = new ArrayList<>();
                        for (int i = 0; i < gameListJSON.length(); i++)
                            gameList.add(gameListJSON.getString(i));

                        // Choose and send the game ID to the server
                        reply = loginInterface.joinGame(getUsername(), view.gameIdSelection(gameList));
                        headerCode = reply.getHeaderCode();

                        if(headerCode == OK.getCode()) {
                            // TODO Remove debug statement
                            System.err.println("You correctly joined the game.");
                            operationCompleted = true;
                        } else showError(reply);
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
        showError(gameInterface.submitTiles(getUsername(), tileValidation()));
    }

    @Override
    void getColumn() throws RemoteException {
        showError(gameInterface.submitColumn(getUsername(), columnValidation()));
    }

    @Override
    boolean reconnect() throws RemoteException {
        setUsername(view.getUsername());

        Message reply = loginInterface.reconnect(getUsername());
        int headerCode = reply.getHeaderCode();

        if(headerCode == OK.getCode()) {
            // TODO Remove debug statement
            System.err.println("Correctly reconnected to game.");
            return true;
        } else {
            showError(reply);
            return false;
        }
    }

    private void setServerUsername() throws RemoteException {
        setUsername(view.getUsername());
        boolean loggedIn = false;

        while(!loggedIn) {
            Message reply = loginInterface.login(getUsername());
            int headerCode = reply.getHeaderCode();

            if(headerCode == OK.getCode()) {
                // TODO Remove debug statement
                System.err.println("Correctly logged in as " + getUsername());
                loggedIn = true;
            } else if(headerCode == USERNAME_TAKEN.getCode()) {
                view.showError(reply.getBody().getJSONObject(0).getString("message"));
                setUsername(view.getUsername());
            } else if(headerCode == BAD_HEADER.getCode()) {
                view.showError(reply.getBody().getJSONObject(0).getString("message"));
            } else throw new UnknownError("An unknown error occurred.");
        }
    }
}
