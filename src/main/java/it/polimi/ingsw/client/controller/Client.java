package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.server.model.Coordinate;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.*;

/**
 * This class handles the interactions between the user and the server.
 *
 * @author Mario Merlo
 */
public class Client {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private String username;

    /**
     * Creates a new client specifying the hostname and port of the server to connect to.
     * @param hostname The IP address of the game server
     * @param port The port of the game server
     */
    public Client(String hostname, int port) {
        try {
            socket = new Socket(hostname, port);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            System.out.println("Something went wrong while creating the client's socket.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gathers input from the user regarding its own move and sends it to the server while validating the input.
     * @throws NullPointerException If the server replies with empty messages, this exception is thrown.
     */
    void sendMove() throws NullPointerException {
        boolean areTilesValid = false;
        boolean isColumnValid = false;
        JSONObject reply;

        // Information about the tiles is gathered here.
        // The loop continues until the server replies with a confirmation that the chosen tiles are valid.
        while(!areTilesValid) {
            ArrayList<Coordinate> inputCoords = getCoordinates();

            sendMessage(coordsToJson(inputCoords));

            try {
                reply = getReply();
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
                throw new NullPointerException();
            }

            if(reply.getBoolean("areTilesValid")) {
                areTilesValid = true;
            } else {
                System.out.println("The tiles you chose are not available. Please try again.");
            }
        }

        // Information about the column is gathered here.
        // The loop continues until the server replies with a confirmation that the chosen column is available.
        while(!isColumnValid) {
            int column = getColumn();

            sendMessage(columnToJson(column));

            try {
                reply = getReply();
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
                throw new NullPointerException();
            }

            if(reply.getBoolean("isColumnValid")) {
                isColumnValid = true;
            } else {
                System.out.println("The column you chose is not available. Please try again.");
            }
        }
    }

    /**
     * This method gets input from System.in in order to get the coordinates of the tiles that the user wants to take
     * from the board. The method asks for coordinates in a specific syntax and then asks for confirmation of the
     * coordinates it gathered from the user.
     * @return An ArrayList containing all the coordinates gathered from the user.
     */
    private ArrayList<Coordinate> getCoordinates() {
        Scanner inputScanner = new Scanner(System.in);
        boolean isConfirmed = false;
        ArrayList<Coordinate> inputCoords = new ArrayList<>();

        System.out.println("Select the tiles you want to get (up to three).");
        System.out.println("Syntax: (x, y) [, (x, y), (x, y)]");

        while(!isConfirmed) {
            System.out.print("Your choice: ");
            inputCoords = parseMoveInput(inputScanner.nextLine());
            System.out.println("Is this ok? (y/n)");
            for(Coordinate item : inputCoords)
                System.out.println(item);
            if(inputScanner.nextLine().equals("y")) {
                isConfirmed = true;
            } else {
                System.out.println("Input your coordinates again.");
            }
        }

        return inputCoords;
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

    /**
     * Writes the JSONObject passed as parameter to the output stream of the client's socket in order to send
     * a message to the server.
     * @param message The JSONObject that will be sent to the server.
     */
    private void sendMessage(JSONObject message) {
        try(OutputStreamWriter out = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            out.write(message.toString());
        } catch (IOException e) {
            System.out.println("Something went wrong while creating the client's socket.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * This method reads from the input stream of the client's socket and creates a new JSONObject containing the
     * server's reply to a message sent by the client.
     * @return The JSON-formatted reply from the server.
     * @throws NullPointerException If the reply is null, this exception is thrown.
     */
    private JSONObject getReply() throws NullPointerException {
        JSONObject reply = null;

        try(InputStreamReader in = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            reply = new JSONObject(in.read());
        } catch (IOException e) {
            System.out.println("Something went wrong while creating the client's socket.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        if(reply != null)
            return reply;
        else throw new NullPointerException("Reply was empty.");
    }

    /**
     * Gets the column where the user would like to place his selected tiles from the user's input.
     * @return The integer representing the chosen column.
     */
    private int getColumn() {
        boolean isConfirmed = false;
        int column;

        while(!isConfirmed) {
            System.out.print("Enter the column of the shelf you want to put the tiles in: ");
            Scanner inputScanner = new Scanner(System.in);
            String columnString = inputScanner.nextLine();
            column = Integer.parseInt(columnString);
            System.out.print("Is column " + column + " ok? (y/n) ");
            if(inputScanner.nextLine().equals("y"))
                isConfirmed = true;
            else System.out.println("Input your column of choice again.");
        }

        return column;
    }

    /**
     * Converts the column chosen by the user to a JSONObject that can be parsed by the server.
     * @param column The column chosen by the user.
     * @return The JSONObject containing information about the column.
     */
    private JSONObject columnToJson(int column) {
        JSONObject JSONColumn = new JSONObject();
        JSONColumn.put("column", column);
        return JSONColumn;
    }

    /**
     * Gets the player's username.
     * @return The player's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the player's username.
     * @param username The username chosen by the user.
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
