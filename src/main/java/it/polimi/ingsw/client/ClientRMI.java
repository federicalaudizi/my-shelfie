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
                update(reply.getBody());
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
                        } else if(headerCode == BAD_HEADER.getCode()) {
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
                        if (headerCode == GAME_LIST_RESPONSE.getCode()) {
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
                                } else if (headerCode == BAD_GAME_ID.getCode() || headerCode == BAD_HEADER.getCode()) {
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
        showError(gameInterface.submitTiles(getUsername(), tileValidation()));
    }

    @Override
    void getColumn() throws RemoteException {
        boolean columnValidation = false, inputValidation;
        int column, headerCode;
        JSONObject body = null;
        Message reply;

        while(!columnValidation) {
            inputValidation = false;

            while(!inputValidation) {
                column = view.getColumn();
                if(column >= 0 && column <= 4) {
                    inputValidation = true;
                    body = new JSONObject().put("column", column);
                } else {
                    view.showError("The column you input is invalid. Retry.");
                }
            }

            if(body != null) {
                reply = gameInterface.submitColumn(getUsername(), new Message(SEND_COLUMN, body));
            } else throw new NullPointerException("Column message body was empty.");

            headerCode = reply.getHeaderCode();

            if(headerCode == OK.getCode()) {
                columnValidation = true;
            } else if(headerCode == BAD_COLUMN.getCode() || headerCode == BAD_HEADER.getCode()) {
                view.showError(reply.getBody().getJSONObject(0).getString("message"));
            } else throw new UnknownError("An unknown error occurred.");
        }
    }

    @Override
    boolean reconnect() throws RemoteException {
        int attempts = 0;
        setUsername(view.getUsername());

        while(attempts < 3) {
            Message reply = loginInterface.reconnect(getUsername());
            int headerCode = reply.getHeaderCode();

            if(headerCode == OK.getCode()) {
                // TODO Remove debug statement
                System.err.println("Correctly reconnected to game.");
                return true;
            } else if(headerCode == GAME_UNAVAILABLE.getCode() || headerCode == PLAYER_NOT_FOUND.getCode()) {
                view.showError(reply.getBody().getJSONObject(0).getString("message"));
                return false;
            } else if(headerCode == BAD_HEADER.getCode()) {
                attempts++;
                view.showError(reply.getBody().getJSONObject(0).getString("message"));
                try {
                    Thread.sleep(attempts * 5000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else throw new UnknownError("An unknown error occurred.");
        }
        return false;
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
