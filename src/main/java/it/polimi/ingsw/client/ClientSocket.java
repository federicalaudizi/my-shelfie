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
        // TODO Implement this method
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
        body.put(new JSONObject().put("username", username));
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
                    // TODO Send login confirmed message to View
                    loggedIn = true;
                } else if(headerCode == 411) {
                    // TODO Send username taken message to View
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
    void getMove() {
        // Ask for tiles to user
    }

    /**
     * This method transforms the coordinates gathered by the user into a JSON object that will then be parsed
     * by the server in order to validate them.
     * @param coords The ArrayList containing the coordinates chosen by the user.
     * @return The JSONObject containing a JSONArray with the aforementioned coordinates.
     */
    private JSONObject coordsToJson(ArrayList<Coordinate> coords) {
        JSONArray JSONCoords = new JSONArray();
        for(Coordinate item : coords)
            JSONCoords.put(item);

        JSONObject JSONMessage = new JSONObject();
        JSONMessage.put("coordinates", JSONCoords);

        return JSONMessage;
    }

    /**
     * This method parses the coordinate input from the user with a regex matching and returns an ArrayList of
     * coordinates with all the matches it found.
     * @param input The input string gathered from the user.
     * @return An ArrayList of Coordinate objects that contain the coordinates gathered from the user.
     */
    private ArrayList<Coordinate> parseMoveInput(String input) {
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

        return inputCoords;
    }

    @Override
    JSONObject getReply() throws NullPointerException {
        JSONObject reply = null;
        try {
            reply = new JSONObject(reader.read());
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
