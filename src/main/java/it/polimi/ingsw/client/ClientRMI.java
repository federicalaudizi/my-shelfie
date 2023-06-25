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
 * This class implements a client that connects to a server through RMI.
 * @author Mario Merlo
 */
public class ClientRMI extends Client {
    private RMILoginInterface loginInterface;
    private RMIGameInterface gameInterface;

    public ClientRMI(boolean cli) {
        super(cli);
    }

    /**
     * Starts the client by cycling through the game phases such as connection, login and move parsing
     * @throws RemoteException If the RMI connection fails, this exception is thrown
     * @author Mario Merlo
     */
    @Override
    public void start() throws RemoteException {
        boolean exit = false;
        while(!exit) {
            try {
                boolean gameOver = false;

                connect();

                login();

                while(!gameOver) {
                    Message reply;
                    int headerCode;

                    reply = gameInterface.ping(getUsername());
                    System.err.println(reply);
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
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else if(headerCode == PLAYER_TERMINATED.getCode()) {
                        view.showError(reply.getBody().getJSONObject(0).getString("message"));
                        exit = view.continueScreen();
                        //TODO: here i should exit the game
                    } else if(headerCode == PLAYER_DISCONNECTED.getCode()) {
                        // TODO: Maybe handle this differently
                        view.showError(reply.getBody().getJSONObject(0).getString("username") + " disconnected from the game.");
                    }
                }
            } catch (RemoteException e) {
                if(isDisconnected())
                    view.showError("Network error: you were disconnected from the server. Try selecting the reconnect option in the main menu.");
            }
        }
    }

    /**
     * Locates the RMI registry containing login and game methods.
     * @author Mario Merlo
     */
    @Override
    void connect(){
        boolean isValid = false;
        String ip;
        Registry registry = null;

        while(!isValid) {
            ip = view.getIp();
            try {
                isValid = validateIp(ip);
            } catch (IllegalArgumentException e) {
                view.showError("You entered a malformed IP:port combo. Retry.");
                continue;
            }

            if(!isValid) {
                view.showError("You entered an invalid IP:port combo. Retry.");
                continue;
            }

            String[] hostInfo = ip.split(":");

            try {
                registry = LocateRegistry.getRegistry(hostInfo[0], Integer.parseInt(hostInfo[1]));
            } catch (RemoteException e) {
                view.showError("Unable to connect to the server. Retry.");
                isValid = false;
            }

            // TODO: if the ip goes to nowhere the client still asks for the name

            if (registry == null) {
                view.showError("Unable to connect to the server. Retry.");
                isValid = false;
                continue;
            }

            try {
                loginInterface = (RMILoginInterface) registry.lookup("RMILoginInterface");
                gameInterface = (RMIGameInterface) registry.lookup("RMIGameInterface");
            } catch (NotBoundException | RemoteException e) {
                view.showError("Unable to connect to the server. Retry.");
                isValid = false;
            }
        }
    }

    /**
     * Prompts the user for a username, logs into the connected server and asks the user if they want to create a new
     * game, join one or reconnect to a game they were disconnected from.
     * @throws RemoteException If the RMI connection fails, this exception is thrown
     * @author Mario Merlo
     */
    @Override
    void login() throws RemoteException {
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
                    System.err.println(reply);
                    headerCode = reply.getHeaderCode();
                    if(headerCode == OK.getCode())
                        operationCompleted = true;
                    else showError(reply);
                }
                case 2 -> {
                    // Join a new game option
                    reply = loginInterface.getGameList(getUsername());
                    System.err.println(reply);
                    headerCode = reply.getHeaderCode();

                    if(headerCode == GAME_LIST_RESPONSE.getCode()) {
                        // Get game ID list from reply
                        JSONArray gameListJSON = reply.getBody().getJSONObject(0).getJSONArray("games");
                        ArrayList<String> gameList = new ArrayList<>();
                        for (int i = 0; i < gameListJSON.length(); i++)
                            gameList.add(gameListJSON.getString(i));

                        // Choose and send the game ID to the server
                        reply = loginInterface.joinGame(getUsername(), view.gameIdSelection(gameList));
                        System.err.println(reply);
                        headerCode = reply.getHeaderCode();

                        if(headerCode == OK.getCode())
                            operationCompleted = true;
                        else showError(reply);
                    }
                }
                case 3 -> {
                    // Reconnect option
                    if(isDisconnected())
                        view.showError("Unable to reconnect. Try creating a new game or joining one.");
                    else
                        operationCompleted = true;
                }
            }
        }


    }

    /**
     * Asks the user for the tiles they want to pick from the board.
     * @throws RemoteException If the RMI connection fails, this exception is thrown
     * @author Mario Merlo
     */
    @Override
    void getTiles() throws RemoteException {
        showError(gameInterface.submitTiles(getUsername(), tileValidation()));
    }

    /**
     * Asks the user for the column they want to put their selected tiles in.
     * @throws RemoteException If the RMI connection fails, this exception is thrown.
     * @author Mario Merlo
     */
    @Override
    void getColumn() throws RemoteException {
        showError(gameInterface.submitColumn(getUsername(), columnValidation()));
    }

    /**
     * This method is used to stop the ongoing game if the client disconnects from the server.
     * @author Mario Merlo
     */
    @Override
    boolean isDisconnected() {
        Message reply;
        try {
            setUsername(view.getUsername());

            reply = loginInterface.reconnect(getUsername());
            System.err.println(reply);
            int headerCode = reply.getHeaderCode();

            if(headerCode == OK.getCode())
                return false;

            showError(reply);
        } catch (RemoteException e) {
            view.showError(e.getMessage());
        }

        return true;
    }

    /**
     * This method handles the username setting on the server
     * @throws RemoteException If the RMI connection fails, this exception is thrown
     * @author Mario Merlo
     */
    @Override
    void setServerUsername() throws RemoteException {
        setUsername(view.getUsername());
        boolean loggedIn = false;

        while(!loggedIn)
            loggedIn = checkUsernameValidity(loginInterface.login(getUsername()));
    }
}
