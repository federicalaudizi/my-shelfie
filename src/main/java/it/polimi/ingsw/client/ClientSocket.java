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
            try {
                boolean gameOver = false;

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
                    else if(headerCode == GAME_UPDATE.getCode())
                        update(reply.getBody());
                    // Fourth step: execute game over operations when Game Over is sent by the server
                    else if(headerCode == GAME_OVER.getCode()) {
                        gameOver = true;
                        gameOver(reply.getBody());
                        cleanUp();
                        exit = view.continueScreen();
                    }
                }
            } catch (IOException e) {
                if (isDisconnected()) {
                    view.showError("Network error: you were disconnected from the server. Try selecting the reconnect option in the main menu.");
                    cleanUp();
                }
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
     * Prompts the user for a username, logs into the connected server and asks the user if they want to create a new
     * game, join one or reconnect to a game they were disconnected from.
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
                    if(headerCode == OK.getCode())
                        operationCompleted = true;
                    else showError(reply);
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

                        if(headerCode == OK.getCode())
                            operationCompleted = true;
                        else showError(reply);
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

    /**
     * Asks the user for the tiles they want to pick from the board.
     * @throws IOException If the connection to the server fails, this exception is thrown
     * @author Mario Merlo
     */
    @Override
    void getTiles() throws IOException {
        send(tileValidation());
        showError(getReply());
    }

    /**
     * Asks the user for the column they want to put their selected tiles in.
     * @throws IOException If the connection to the server fails, this exception is thrown
     * @author Mario Merlo
     */
    @Override
    void getColumn() throws IOException {
        send(columnValidation());
        showError(getReply());
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

            Message reconnectionMessage = new Message(RECONNECT, new JSONObject().put("username", getUsername()));
            int headerCode;

            send(reconnectionMessage);
            reply = getReply();
            headerCode = reply.getHeaderCode();

            if(headerCode == OK.getCode())
                return false;

            showError(reply);
        } catch (IOException e) {
            view.showError(e.getMessage());
        }

        return true;
    }

    /**
     * Correctly closes the socket components and the socket itself once the client disconnects or the game ends
     * @author Mario Merlo
     */
    void cleanUp() {
        writer.close();
        try {
            bufferedReader.close();
            socket.close();
        } catch (IOException e) {
            view.showError(e.getMessage());
        }
    }

    /**
     * This method handles the username setting on the server
     * @throws IOException If the connection to the server fails, this exception is thrown
     * @author Mario Merlo
     */
    @Override
    void setServerUsername() throws IOException {
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
     * @throws IOException If something goes wrong when reading the socket's input stream, this exception is thrown
     * @author Mario Merlo
     */
    Message getReply() throws IOException {
        Message reply = new Message(bufferedReader.readLine());
        System.err.println(reply);
        return reply;
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
