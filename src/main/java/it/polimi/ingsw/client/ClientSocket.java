package it.polimi.ingsw.client;

import it.polimi.ingsw.server.controller.network.Message;
import it.polimi.ingsw.server.model.Coordinate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientSocket extends Client {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private OutputStreamWriter writer;
    private InputStreamReader reader;
    private BufferedReader bufferedReader;

    public ClientSocket(boolean cli) {
        super(cli);
    }

    void start() {
        boolean gameOver = false;
        // First step: connect to the game server
        connect();

        // Second step: log into the server
        login();

        // Third step: create new game or join a game
        // TODO Implement third step

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
            }
        }
    }

    @Override
    void connect() {
        boolean isValid = false;
        String ip = null;
        while(!isValid) {
            ip = view.confirmationPrompt("Enter the server's IP (syntax: ip:port): ");
            isValid = validateIp(ip);
        }
        String[] hostInfo = ip.split(":");
        try {
            socket = new Socket(hostInfo[0], Integer.parseInt(hostInfo[1]));
            writer = new OutputStreamWriter(socket.getOutputStream());
            reader = new InputStreamReader(socket.getInputStream());
            bufferedReader = new BufferedReader(reader);
        } catch (UnknownHostException e) {
            view.okPrompt("The host does not exist. Retry.");
        } catch (IOException e) {
            view.okPrompt("Something went wrong.");
        }
    }

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

    @Override
    void login() {
        JSONArray body = new JSONArray();
        body.put(new JSONObject().put("username", this.getUsername()));
        Message loginMessage = new Message(Message.Header.LOGIN_REQUEST, body);
        send(loginMessage);

        boolean loggedIn = false;
        String line;
        JSONObject reply;

        while(!loggedIn) {
            try {
                line = bufferedReader.readLine();
                reply = new JSONObject(line);
                int headerCode = Message.Header.valueOf(reply.getString("header")).getCode();

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
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    void move() throws NullPointerException, UnknownError {
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
    void viewUpdate(JSONObject gameState) {
        // TODO Implement this
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
        JSONObject replyJSON;
        try {
            replyJSON = new JSONObject(reader.read());
            reply = new Message(Message.Header.valueOf(replyJSON.getString("header")), replyJSON.getJSONObject("body"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(reply != null)
            return reply;
        else throw new NullPointerException("Reply was empty.");
    }

    @Override
    public void send(Message message) {
        try {
            writer.write(message.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
