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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * Starts the client by cycling through the game phases such as connection, login and move parsing
     * @author Mario Merlo
     */
    void start() {
        boolean gameOver = false;
        try {
            // First step: connect to the game server
            connect();

            // Second step: log into the server
            login();

            // Third step: create new game or join a game
            startGame();

            while(!gameOver) {
                Message reply = getReply();
                int headerCode = reply.getHeaderCode();

                // Fourth step: when asked for a move, provide it
                if(headerCode == 321)
                    move();
                // Fifth step: execute game over operations when Game Over is sent by the server
                else if(headerCode == 121) {
                    gameOver = true;
                    gameOver(reply);
                } else if(headerCode == 122)
                    update(reply.getBody().getJSONObject(0));
            }
        } catch (IOException e) {
            try {
                reconnect();
            } catch (IOException ex) {
                // TODO Close socket on exit
                view.okPrompt("Unable to reconnect. Exiting.");
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
            ip = view.confirmationPrompt("Enter the server's IP (syntax: ip:port): ");
            isValid = validateIp(ip);
        }
        String[] hostInfo = ip.split(":");
        try {
            socket = new Socket(hostInfo[0], Integer.parseInt(hostInfo[1]));
            writer = new PrintWriter(socket.getOutputStream());
            InputStreamReader reader = new InputStreamReader(socket.getInputStream());
            bufferedReader = new BufferedReader(reader);
        } catch (UnknownHostException e) {
            view.okPrompt("The host does not exist. Retry.");
            throw new UnknownHostException(e.getMessage());
        } catch (IOException e) {
            view.okPrompt("Something went wrong.");
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Checks whether the passed IP and port combo is valid
     * @param ip The string formatted as ip:port
     * @return true if the passed IP is valid, false otherwise
     */
    private boolean validateIp(String ip) {
        // Split IP and Port
        String[] portSplit = ip.split(":");
        // Convert port String to int to perform the comparison
        int port = Integer.parseInt(portSplit[1]);
        // If the port is not a valid number, return false
        if(port <= 0 || port > 65535)
            return false;
        // Split IP into the four integers that compose it
        String[] ipSplit = portSplit[0].split("\\.");
        for(String item : ipSplit) {
            // Convert IP Segment to integer for the comparison
            int ipSegment = Integer.parseInt(item);
            // If the segment is not a valid number, return false
            if(ipSegment <= 0 || ipSegment > 255)
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
        setUsername(view.confirmationPrompt("Enter a username: "));
        boolean loggedIn = false;
        String line;
        Message reply;
        String[] options = { "Create or join a new game", "Reconnect to an ongoing game" };

        int choice = view.choicePrompt("What do you want to do?", options);
        switch(choice) {
            case 1 -> {
                while(!loggedIn) {
                    try {
                        JSONArray body = new JSONArray();
                        body.put(new JSONObject().put("username", this.getUsername()));
                        Message loginMessage = new Message(Message.Header.LOGIN_REQUEST, body);
                        send(loginMessage);

                        line = bufferedReader.readLine();
                        reply = new Message(line);
                        int headerCode = reply.getHeaderCode();

                        if(headerCode == 200) {
                            view.okPrompt(this.getUsername() + " correctly logged in. Welcome.");
                            loggedIn = true;
                        } else if(headerCode == 411) {
                            setUsername(view.confirmationPrompt("Username taken. Enter a new username: "));
                        } else if(headerCode == 400) {
                            // A generic error occurred. The client throws an exception.
                            throw new RuntimeException("Unknown error");
                        }
                    } catch (IOException e) {
                        throw new IOException(e.getMessage());
                    }
                }
            }
            case 2 -> {
                reconnect();
                /*while(!loggedIn) {
                    try {
                        JSONArray body = new JSONArray();
                        body.put(new JSONObject().put("username", getUsername()));
                        Message reconnectMessage = new Message(Message.Header.RECONNECT, body);
                        send(reconnectMessage);

                        line = bufferedReader.readLine();
                        reply = new Message(line);
                        int headerCode = reply.getHeaderCode();

                        if(headerCode == 200) {
                            view.okPrompt("You successfully reconnected to your ongoing game.");
                            loggedIn = true;
                        } else if(headerCode == 400)
                            view.okPrompt("Something went wrong during the reconnection. Please retry.");
                    } catch (IOException e) {
                        throw new IOException(e.getMessage());
                    }
                }*/
            }
        }
    }

    /**
     * This method asks the user whether they want to create a new game or join an existing game and then proceeds to
     * communicate the choice to the server.
     * @throws UnknownError If an unknown response is sent by the server, this exception is thrown
     * @throws IOException If the client disconnects inadvertently from the server, this exception is thrown
     * @author Mario Merlo
     */
    @Override
    void startGame() throws UnknownError, IOException {
        String[] options = { "Create new game", "Join existing game" };
        int choice = view.choicePrompt("What do you want to do?", options);
        boolean operationCompleted = false;
        while(!operationCompleted) {
            switch (choice) {
                case 1 -> {
                    boolean gameCreated = false;
                    while (!gameCreated) {
                        boolean playerNumberValid = false;
                        int playerNumber = 0;
                        while (!playerNumberValid) {
                            playerNumber = Integer.parseInt(view.confirmationPrompt("Enter the number of players (between 2 and 4): "));
                            if (playerNumber < 2 || playerNumber > 4)
                                view.okPrompt("You entered an invalid number of players. Please retry.");
                            else playerNumberValid = true;
                        }
                        Message newGameMessage = new Message(Message.Header.NEW_GAME_REQUEST, new JSONObject().put("playerNumber", playerNumber));
                        send(newGameMessage);

                        int headerCode = getReply().getHeaderCode();

                        if (headerCode == 200) {
                            view.okPrompt("The game was correctly created.");
                            gameCreated = true;
                        } else if (headerCode == 400) {
                            view.okPrompt("An error occurred. Please retry.");
                        } else throw new UnknownError("An unknown error occurred.");
                    }
                    operationCompleted = true;
                }
                case 2 -> {
                    boolean gameJoined = false, noGames = true;
                    while (!gameJoined || noGames) {
                        send(new Message(Message.Header.JOIN_GAME_REQUEST));
                        Message gameListMessage = getReply();
                        if (gameListMessage.getHeaderCode() == 211) {
                            JSONArray gameListJSON = gameListMessage.getBody().getJSONObject(0).getJSONArray("games");
                            ArrayList<String> gameList = new ArrayList<>();
                            if(gameListJSON.length() != 0) {
                                for (int i = 0; i < gameListJSON.length(); i++)
                                    gameList.add(gameListJSON.getString(i));
                                String selectedGame = view.gameIdSelection(gameList);
                                Message joinGameMessage = new Message(Message.Header.JOIN_GAME_RESPONSE, new JSONObject().put("gameId", selectedGame));
                                send(joinGameMessage);
                                int gameJoinHeaderCode = getReply().getHeaderCode();
                                if (gameJoinHeaderCode == 200) {
                                    view.okPrompt("You correctly joined the game.");
                                    gameJoined = true;
                                    operationCompleted = true;
                                } else if (gameJoinHeaderCode == 412) {
                                    view.okPrompt("This game does not exist on the server. Please retry.");
                                } else if (gameJoinHeaderCode == 400) {
                                    view.okPrompt("An error occurred. Please retry.");
                                } else throw new UnknownError("An unknown error occurred.");
                            } else {
                                view.okPrompt("There are no ongoing games on this server. Creating a new game.");
                                noGames = false;
                                choice = 1;
                            }
                        }
                    }
                }
                default -> view.okPrompt("You entered an invalid option. Please retry.");
            }
        }
    }

    /**
     * This method prompts the user to enter a move, validates it and sends it to the server.
     * @throws NullPointerException
     * @throws UnknownError
     * @throws IOException
     */
    @Override
    void move() throws NullPointerException, UnknownError, IOException {
        boolean inputValidation, moveValidation = false, columnValidation = false;
        ArrayList<Coordinate> coordinates = null;
        JSONObject body = null;
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
                String input = view.confirmationPrompt("Enter up to three coordinates.\nSyntax: (x, y)[, (x, y), (x, y)]\n Your choice: ");
                try {
                    coordinates = parseMoveInput(input);
                    inputValidation = true;
                } catch (IllegalStateException e) {
                    view.okPrompt("You entered an invalid number of coordinates. Retry.");
                }
            }
            try {
                body = coordsToJson(coordinates);
            } catch (NullPointerException e) {
                view.okPrompt("Something went wrong.");
                e.printStackTrace();
            }

            // --- Server-side validation ---
            // The client packages the tiles selected by the user and then sends them to the server in order to be
            // validated according to the game's rules. This while loop does not break until the server has validated
            // the player's move.
            Message tileMessage;
            if(body != null) {
                tileMessage = new Message(Message.Header.SEND_TILES, body);
            } else throw new NullPointerException("The message body was empty.");

            send(tileMessage);

            reply = getReply();
            headerCode = reply.getHeaderCode();

            // Check reply to either resend coordinates or continue with the move
            if(headerCode == 200) {
                moveValidation = true;
            } else if(headerCode == 421) {
                view.okPrompt("The tiles you chose are not valid. Please retry.");
            } else if(headerCode == 400) {
                view.okPrompt("A generic error occurred.");
            } else throw new UnknownError("An unknown error occurred.");
        }

        // Phase 2: ask for column to put tiles in
        while(!columnValidation) {
            inputValidation = false;
            int column;
            JSONObject columnBody = null;
            Message columnMessage;

            while(!inputValidation) {
                String input = view.confirmationPrompt("Enter the column you want to put the tiles in.\nPossible values: 1 to 5 (including 1 and 5).\nYour choice: ");
                column = Integer.parseInt(input);
                if(column >= 0 && column <= 4) {
                    inputValidation = true;
                    columnBody = new JSONObject().put("column", column);
                } else {
                    view.okPrompt("The column you input is invalid. Retry.");
                }
            }

            if(columnBody != null) {
                columnMessage = new Message(Message.Header.SEND_COLUMN, columnBody);
            } else throw new NullPointerException("Column message body was empty.");

            send(columnMessage);

            reply = getReply();
            headerCode = reply.getHeaderCode();

            if(headerCode == 200) {
                columnValidation = true;
            } else if(headerCode == 422) {
                view.okPrompt("The column you chose is not valid. Please retry.");
            } else if(headerCode == 400) {
                view.okPrompt("A generic error occurred.");
            } else throw new UnknownError("An unknown error occurred.");
        }
        view.okPrompt("Your move was correctly sent to the server.");
    }

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
                view.okPrompt("Successfully reconnected to server.");
                reconnected = true;
            } else if (replyHeaderCode == 400) {
                attempts++;
                // TODO Change type of prompt: do not require any key to continue
                System.out.println("Something went wrong during the reconnection. Retrying... (Attempt " + attempts + "/3)");
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
     * This method transforms the coordinates gathered by the user into a JSON object that will then be parsed
     * by the server in order to validate them.
     * @param coords The ArrayList containing the coordinates chosen by the user.
     * @return The JSONObject containing a JSONArray with the aforementioned coordinates.
     */
    private JSONObject coordsToJson(ArrayList<Coordinate> coords) throws NullPointerException {
        if(coords == null)
            throw new NullPointerException("Coordinate array was empty.");
        JSONArray JSONCoords = new JSONArray();
        for(Coordinate item : coords)
            JSONCoords.put(item);

        JSONObject tileBody = new JSONObject();
        tileBody.put("tiles", JSONCoords);

        return tileBody;
    }

    /**
     * This method parses the coordinate input from the user with a regex matching and returns an ArrayList of
     * coordinates with all the matches it found.
     * @param input The input string gathered from the user.
     * @return An ArrayList of Coordinate objects that contain the coordinates gathered from the user.
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
            System.out.println(reply);
            return reply;
        }
        else throw new NullPointerException("Reply was empty.");
    }

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
