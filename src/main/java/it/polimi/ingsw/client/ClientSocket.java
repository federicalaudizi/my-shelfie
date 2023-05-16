package it.polimi.ingsw.client;

import it.polimi.ingsw.server.controller.network.Message;
import it.polimi.ingsw.server.model.Coordinate;
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
                    update(reply.getBody().getJSONObject(0));
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
     * Checks whether the passed IP and port combo is valid
     * @param ip The string formatted as ip:port
     * @throws IllegalArgumentException If the IP string is malformed, this exception is thrown
     * @return true if the passed IP is valid, false otherwise
     * @author Mario Merlo
     */
    private boolean validateIp(String ip) throws IllegalArgumentException {
        // Split IP and Port
        String[] portSplit = ip.split(":");
        // Check for malformed IP string
        if(portSplit.length != 2) throw new IllegalArgumentException("Malformed IP string");

        // Convert port String to int to perform the comparison
        int port;
        try {
            port = Integer.parseInt(portSplit[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Malformed IP port");
        }
        // If the port is not a valid number, return false
        if(port <= 0 || port > 65535)
            return false;

        // Split IP into the four integers that compose it
        String[] ipSplit = portSplit[0].split("\\.");
        // Check for malformed IP address
        if(ipSplit.length != 4) throw new IllegalArgumentException("Malformed IP address");

        for(String item : ipSplit) {
            // Convert IP Segment to integer for the comparison
            int ipSegment;
            try {
                ipSegment = Integer.parseInt(item);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Malformed IP address segment");
            }
            // If the segment is not a valid number, return false
            if(ipSegment < 0 || ipSegment > 255)
                return false;
        }
        // If none of the checks are triggered, the IP is valid
        return true;
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
                    } else if (headerCode == GENERIC_ERROR.getCode()) {
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
                                    } else if (headerCode == GENERIC_ERROR.getCode()) {
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
                                    if (gameListMessage.getHeaderCode() == 211) {
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
                                            } else if (gameJoinHeaderCode == GENERIC_ERROR.getCode()) {
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

    /**
     * This method prompts the user to enter a move, validates it and sends it to the server.
     * @throws NullPointerException If the body of the messages is null before they are sent, this exception is thrown.
     * @throws UnknownError If something unexpected is sent by the server as a response, this exception is thrown.
     * @throws IOException If the client disconnects inadvertently from the server, this exception is thrown
     * @author Mario Merlo
     */
    @Override
    void move() throws NullPointerException, UnknownError, IOException {
        boolean inputValidation, moveValidation = false, columnValidation = false;
        ArrayList<Coordinate> coordinates = null;
        JSONArray body = null;
        Message reply;
        int headerCode;

        // Phase 1: ask for tiles to pick
        while(!moveValidation) {
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
            Message tileMessage;
            if(body != null) {
                tileMessage = new Message(Message.Header.SEND_TILES, body);
            } else throw new NullPointerException("The message body was empty.");

            // Send the requested information to the server
            send(tileMessage);

            // Get the server's response and its header code
            reply = getReply();
            headerCode = reply.getHeaderCode();

            // Check reply to either resend coordinates or continue with the move
            if(headerCode == Message.Header.OK.getCode()) {
                reply = getReply();
                headerCode = reply.getHeaderCode();
                if(headerCode == GET_COLUMN.getCode())
                    moveValidation = true;
            } else if(headerCode == Message.Header.BAD_TILES.getCode()) {
                view.showError("The tiles you chose are not valid. Please retry.");
            } else if(headerCode == Message.Header.GENERIC_ERROR.getCode()) {
                view.showError("A generic error occurred.");
                // TODO: there is a bug, if i chose the wrong tile, it makes me choose again, but then it crashes
            } else throw new UnknownError("An unknown error occurred.");
        }

        // Phase 2: ask for column to put tiles in
        while(!columnValidation) {
            inputValidation = false;
            int column;
            JSONObject columnBody = null;
            Message columnMessage;

            while(!inputValidation) {
                column = view.getColumn();
                if(column >= 0 && column <= 4) {
                    inputValidation = true;
                    columnBody = new JSONObject().put("column", column);
                } else {
                    view.showError("The column you input is invalid. Retry.");
                }
            }

            if(columnBody != null) {
                columnMessage = new Message(Message.Header.SEND_COLUMN, columnBody);
            } else throw new NullPointerException("Column message body was empty.");

            send(columnMessage);

            reply = getReply();
            headerCode = reply.getHeaderCode();

            if(headerCode == Message.Header.OK.getCode()) {
                columnValidation = true;
            } else if(headerCode == Message.Header.BAD_COLUMN.getCode()) {
                view.showError("The column you chose is not valid. Please retry.");
            } else if(headerCode == Message.Header.GENERIC_ERROR.getCode()) {
                view.showError("A generic error occurred.");
            } else throw new UnknownError("An unknown error occurred.");
        }
        System.err.println("Your move was correctly sent to the server.");
    }

    /**
     * This method is used to reconnect to an ongoing game in case of an accidental client disconnection.
     * @throws IOException If the client disconnects inadvertently from the server, this exception is thrown
     * @author Mario Merlo
     */
    @Override
    void reconnect() throws IOException {
        int attempts = 0;
        boolean reconnected = false;
        while(!reconnected && attempts < 3) {
            if(!socket.isConnected())
                connect();

            Message reconnectionMessage = new Message(Message.Header.RECONNECT, new JSONObject().put("username", getUsername()));
            send(reconnectionMessage);

            int replyHeaderCode = getReply().getHeaderCode();
            if(replyHeaderCode == 200) {
                System.err.println("Successfully reconnected to server.");
                reconnected = true;
            } else if (replyHeaderCode == GENERIC_ERROR.getCode()) {
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
     * This method transforms the coordinates gathered by the user into a JSON object that will then be parsed
     * by the server in order to validate them.
     * @param coords The ArrayList containing the coordinates chosen by the user.
     * @throws NullPointerException If the ArrayList of coordinates passed is empty, this exception is thrown
     * @return The JSONObject containing a JSONArray with the aforementioned coordinates.
     * @author Mario Merlo
     */
    private JSONArray coordsToJson(ArrayList<Coordinate> coords) throws NullPointerException {
        if(coords == null)
            throw new NullPointerException("Coordinate array was empty.");
        JSONArray JSONCoords = new JSONArray();
        for(Coordinate item : coords)
            JSONCoords.put(item.toJSON());

        return JSONCoords;
    }

    /**
     * This method parses the coordinate input from the user with a regex matching and returns an ArrayList of
     * coordinates with all the matches it found.
     * @param input The input string gathered from the user.
     * @throws IllegalStateException If the input string is empty or contains more than three coordinates, this
     *                               exception is thrown
     * @return An ArrayList of Coordinate objects that contain the coordinates gathered from the user.
     * @author Mario Merlo
     */
    private ArrayList<Coordinate> parseMoveInput(String input) throws IllegalStateException {
        final Pattern coordinatePattern = Pattern.compile("(\\([0-9],\\s?[0-9]\\))+");
        final Matcher coordinateMatcher = coordinatePattern.matcher(input);
        ArrayList<Coordinate> inputCoords = new ArrayList<>();

        while(coordinateMatcher.find()) {
            String matchingSubstring = coordinateMatcher.group();
            matchingSubstring = matchingSubstring.substring(1, matchingSubstring.length() - 1);
            matchingSubstring = matchingSubstring.replaceAll("\\s*", "");
            String[] coordinates = matchingSubstring.split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            inputCoords.add(new Coordinate(x, y));
        }

        if(inputCoords.size() == 0 || inputCoords.size() > 3)
            throw new IllegalStateException("Wrong number of coordinates input.");

        return inputCoords;
    }

    /**
     * Reads the reply sent from the server and packages it as a Message.
     * @return The reply sent from the server as a Message object.
     * @throws NullPointerException If the reply is null, this exception is thrown.
     * @author Mario Merlo
     */
    @Override
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
    @Override
    public void send(Message message) {
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
