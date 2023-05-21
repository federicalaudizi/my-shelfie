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
                } else if(headerCode == GAME_UPDATE.getCode())
                    update(reply.getBody());
            }
        } catch (IOException e) {
            try {
                reconnect();
            } catch (IOException ex) {
                try {
                    cleanUp();
                } catch (IOException exc) {
                    view.showError("Something went wrong during the disconnection.");
                }
                view.showError("Unable to reconnect. Exiting.");
                throw new RuntimeException(ex);
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
        setUsername(view.getUsername());
        boolean loggedIn = false, operationCompleted = false, done = false;

        while(!done) {
            int choice = view.getGameOptions();
            if(choice <= 0 || choice > 3)
                view.showError("You entered an invalid option. Retry.");
            else if(choice == 3) {
                reconnect();
                done = true;
            } else {
                while(!loggedIn) {
                    JSONArray body = new JSONArray();
                    body.put(new JSONObject().put("username", this.getUsername()));
                    Message loginMessage = new Message(LOGIN_REQUEST, body);
                    send(loginMessage);

                    int headerCode = getReply().getHeaderCode();

                    if (headerCode == OK.getCode()) {
                        // TODO Remove debug statement
                        System.err.println(this.getUsername() + " correctly logged in. Welcome.");
                        loggedIn = true;
                    } else if (headerCode == USERNAME_TAKEN.getCode()) {
                        view.showError("Username taken.");
                        setUsername(view.getUsername());
                    } else if (headerCode == BAD_HEADER.getCode()) {
                        // A generic error occurred. The client throws an exception.
                        throw new RuntimeException("Unknown error");
                    }
                    while(!operationCompleted) {
                        switch (choice) {
                            case 1 -> {
                                boolean gameCreated = false;
                                while (!gameCreated) {
                                    boolean playerNumberValid = false;
                                    int playerNumber = 0;
                                    while (!playerNumberValid) {
                                        playerNumber = view.getPlayerNumber();
                                        if (playerNumber < 2 || playerNumber > 4)
                                            view.showError("You entered an invalid number of players. Please retry.");
                                        else playerNumberValid = true;
                                    }
                                    Message newGameMessage = new Message(NEW_GAME_REQUEST, new JSONObject().put("playerNumber", playerNumber));
                                    send(newGameMessage);

                                    headerCode = getReply().getHeaderCode();

                                    if (headerCode == OK.getCode()) {
                                        // TODO Remove debug statement
                                        System.err.println("The game was correctly created.");
                                        gameCreated = true;
                                    } else if (headerCode == BAD_HEADER.getCode()) {
                                        view.showError("An error occurred. Please retry.");
                                    } else throw new UnknownError("An unknown error occurred.");
                                }
                                operationCompleted = true;
                            }
                            case 2 -> {
                                boolean gameJoined = false, noGames = true;
                                while (!gameJoined && noGames) {
                                    send(new Message(JOIN_GAME_REQUEST));
                                    Message gameListMessage = getReply();
                                    if (gameListMessage.getHeaderCode() == GAME_LIST_RESPONSE.getCode()) {
                                        JSONArray gameListJSON = gameListMessage.getBody().getJSONObject(0).getJSONArray("games");
                                        ArrayList<String> gameList = new ArrayList<>();
                                        if(gameListJSON.length() != 0) {
                                            for (int i = 0; i < gameListJSON.length(); i++)
                                                gameList.add(gameListJSON.getString(i));
                                            String selectedGame = view.gameIdSelection(gameList);
                                            Message joinGameMessage = new Message(JOIN_GAME_RESPONSE, new JSONObject().put("gameId", selectedGame));
                                            send(joinGameMessage);
                                            int gameJoinHeaderCode = getReply().getHeaderCode();
                                            if (gameJoinHeaderCode == OK.getCode()) {
                                                System.err.println("You correctly joined the game.");
                                                gameJoined = true;
                                                operationCompleted = true;
                                            } else if (gameJoinHeaderCode == BAD_GAME_ID.getCode()) {
                                                view.showError("This game does not exist on the server. Please retry.");
                                            } else if (gameJoinHeaderCode == BAD_HEADER.getCode()) {
                                                view.showError("An error occurred. Please retry.");
                                            } else throw new UnknownError("An unknown error occurred.");
                                        } else {
                                            view.showError("There are no ongoing games on this server. Creating a new game.");
                                            noGames = false;
                                            choice = 1;
                                        }
                                    }
                                }
                            }
                            default -> view.showError("You entered an invalid option. Please retry.");
                        }
                    }
                    done = true;
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
     * @throws IOException If the client disconnects inadvertently from the server, this exception is thrown
     * @author Mario Merlo
     */
    @Override
    boolean reconnect() throws IOException {
        // TODO Change this according to the new signature
        int attempts = 0;
        boolean reconnected = false;
        while(!reconnected && attempts < 3) {
            if(!socket.isConnected())
                connect();

            Message reconnectionMessage = new Message(Message.Header.RECONNECT, new JSONObject().put("username", getUsername()));
            send(reconnectionMessage);

            int replyHeaderCode = getReply().getHeaderCode();
            if(replyHeaderCode == OK.getCode()) {
                System.err.println("Successfully reconnected to server.");
                return true;
            } else if (replyHeaderCode == BAD_HEADER.getCode()) {
                attempts++;
                view.showError("Something went wrong during the reconnection. Retrying... (Attempt " + attempts + "/3)");
                switch(attempts) {
                    case 1 -> {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case 2 -> {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case 3 -> {
                        try {
                            Thread.sleep(30000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        if(!reconnected) {
            throw new IOException("Unable to reconnect.");
        }
        return false;
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
