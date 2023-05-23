package it.polimi.ingsw.client;

import it.polimi.ingsw.server.controller.network.Message;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static it.polimi.ingsw.server.controller.network.Message.Header.*;

/**
 * The client used when communicating to the server via Socket
 * @author Mario Merlo
 */
public class ClientSocket extends Client {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader bufferedReader;

    /**
     * The class constructor calls the abstract parent class Client to build a new instance of the client
     * @param cli Specifies whether the client should start in CLI-mode or GUI-mode
     * @author Mario Merlo
     */
    public ClientSocket(boolean cli) {
        super(cli);
    }

    /**
     * The class constructor calls the abstract parent class Client to build a new instance of the client
     * @param cli Specifies whether the client should start in CLI-mode or GUI-mode
     * @param username Specifies the username associated to this client
     * @author Mario Merlo
     */
    public ClientSocket(boolean cli, String username) { super(cli, username); }

    /**
     * Starts the client by cycling through the game phases such as connection, login and move parsing
     * @author Mario Merlo
     */
    @Override
    public void start() {
        boolean exit = false;
        while(!exit) {
            boolean gameOver = false;
            try {
                // First step: connect to the game server
                connect();

                // Second step: log into the server
                login();

                while(!gameOver) {
                    Message reply = getReply();
                    int headerCode = reply.getHeaderCode();

                    // Third step: when asked for a move, provide it
                    if(headerCode == GET_TILES.getCode())
                        getTiles();
                    if(headerCode == GET_COLUMN.getCode())
                        getColumn();
                    // Fourth step: execute game over operations when Game Over is sent by the server
                    else if(headerCode == GAME_OVER.getCode()) {
                        gameOver = true;
                        gameOver(reply.getBody());
                        cleanUp();
                        exit = view.continueScreen();
                    } else if(headerCode == GAME_UPDATE.getCode())
                        update(reply.getBody());
                }
            } catch (IOException e) {
                view.showError("Network error: you were disconnected from the server. Try selecting the reconnect option in the main menu.");
            }
        }
    }

    /**
     * Connects to the specified IP and port through the client's socket
     * @throws IOException If the connection is compromised and the client disconnects inadvertently, this exception
     *                     is thrown.
     * @author Mario Merlo
     */
    @Override
    void connect() throws IOException {
        boolean isValid = false;
        String ip = null;
        while(!isValid) {
            ip = view.getIp();
            try {
                isValid = validateIp(ip);
            } catch (IllegalArgumentException e) {
                view.showError("You entered a malformed IP:port combo. Retry.");
            }
        }
        String[] hostInfo = ip.split(":");
        try {
            socket = new Socket(hostInfo[0], Integer.parseInt(hostInfo[1]));
            writer = new PrintWriter(socket.getOutputStream());
            InputStreamReader reader = new InputStreamReader(socket.getInputStream());
            bufferedReader = new BufferedReader(reader);
        } catch (UnknownHostException e) {
            view.showError("The host does not exist. Retry.");
            throw new UnknownHostException(e.getMessage());
        } catch (IOException e) {
            view.showError("Something went wrong.");
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Prompts the user for a username and logs into the connected server
     * @throws IOException If the client disconnects inadvertently from the server, this exception is thrown
     * @author Mario Merlo
     */
    @Override
    void login() throws IOException {
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

                    // Send the new game message to the server
                    send(new Message(NEW_GAME_REQUEST, new JSONObject().put("playerNumber", playerNumber)));

                    // Parse the reply
                    reply = getReply();
                    headerCode = reply.getHeaderCode();
                    if(headerCode == OK.getCode()) {
                        // TODO Remove debug statement
                        System.err.println("Correctly created game.");
                        operationCompleted = true;
                    } else showError(reply);
                }
                case 2 -> {
                    // Join a new game option
                    send(new Message(JOIN_GAME_REQUEST));
                    reply = getReply();
                    headerCode = reply.getHeaderCode();

                    if(headerCode == GAME_LIST_RESPONSE.getCode()) {
                        // Get game ID list from reply
                        JSONArray gameListJSON = reply.getBody().getJSONObject(0).getJSONArray("games");
                        ArrayList<String> gameList = new ArrayList<>();
                        for(int i = 0; i < gameListJSON.length(); i++)
                            gameList.add(gameListJSON.getString(i));

                        // Choose and send the game ID to the server
                        send(new Message(JOIN_GAME_RESPONSE, new JSONObject().put("gameId", view.gameIdSelection(gameList))));
                        reply = getReply();
                        headerCode = reply.getHeaderCode();

                        if(headerCode == OK.getCode()) {
                            // TODO Remove debug statement
                            System.err.println("You correctly joined the game.");
                            operationCompleted = true;
                        } else showError(reply);
                    } else showError(reply);
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

    @Override
    void getTiles() throws IOException {
        send(tileValidation());
        showError(getReply());
    }

    @Override
    void getColumn() throws IOException {
        send(columnValidation());
        showError(getReply());
    }

    /**
     * This method is used to reconnect to an ongoing game in case of an accidental client disconnection.
     * @author Mario Merlo
     */
    @Override
    boolean isDisconnected() {
        setUsername(view.getUsername());

        Message reconnectionMessage = new Message(RECONNECT, new JSONObject().put("username", getUsername()));
        Message reply;
        int headerCode;

        send(reconnectionMessage);
        reply = getReply();
        headerCode = reply.getHeaderCode();

        if(headerCode == OK.getCode()) {
            // TODO Remove debug statement
            System.err.println("Correctly reconnected to game.");
            return false;
        }

        showError(reply);
        return true;
    }

    /**
     * Correctly closes the socket components and the socket itself once the client disconnects or the game ends
     * @throws IOException If something goes wrong when closing the components or the socket, this exception is thrown
     * @author Mario Merlo
     */
    void cleanUp() throws IOException {
        writer.close();
        try {
            bufferedReader.close();
            socket.close();
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    void setServerUsername() {
        setUsername(view.getUsername());
        boolean loggedIn = false;

        while(!loggedIn) {
            send(new Message(LOGIN_REQUEST, new JSONObject().put("username", getUsername())));
            loggedIn = checkUsernameValidity(getReply());
        }
    }

    /**
     * Reads the reply sent from the server and packages it as a Message.
     * @return The reply sent from the server as a Message object.
     * @throws NullPointerException If the reply is null, this exception is thrown.
     * @author Mario Merlo
     */
    Message getReply() throws NullPointerException {
        Message reply = null;
        try {
            reply = new Message(bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(reply != null) {
            // TODO Remove debug statement
            System.err.println(reply);
            return reply;
        } else throw new NullPointerException("Reply was empty.");
    }

    /**
     * Sends a message to the server by writing it on the socket's OutputStream.
     * @param message The message to send to the server.
     * @author Mario Merlo
     */
    void send(Message message) {
        writer.print(message.toString() + "\n");
        writer.flush();
    }

    public static void main(String[] args) {
        ClientSocket client = new ClientSocket(true);

        try {
            client.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Exiting...");
        }
    }
}
