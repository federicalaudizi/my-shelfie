package it.polimi.ingsw.client;

import it.polimi.ingsw.server.controller.network.Message;
import it.polimi.ingsw.server.model.Coordinate;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static it.polimi.ingsw.server.controller.network.Message.Header.*;

/**
 * This class handles the interactions between the user and the server.
 * @author Mario Merlo
 */
public abstract class Client {
    private String username;
    final View view;

    /**
     * This constructor creates a client and the corresponding view.
     * @param cli Specifies whether the client should be CLI-only or not
     * @author Mario Merlo
     */
    public Client(boolean cli) {
        if (cli)
            view = new ViewCLI(this);
        else {
            new Thread(Gui::main).start();
            view = new ViewGUI(this);
        }
    }

    // TODO This is a test constructor and should be removed
    public Client(boolean cli, String username) {
        if (cli)
            view = new ViewCLI(this);
        else
            view = new ViewGUI(this);
        this.username = username;
    }

    /**
     * This method starts the main loop of the client.
     * @throws Exception This method can throw a subclass of Exception when the connection with the server malfunctions.
     * @author Mario Merlo
     */
    public abstract void start() throws Exception;

    /**
     * This method is used to connect the client to the server.
     * @throws Exception This method can throw a subclass of Exception when the connection with the server malfunctions.
     * @author Mario Merlo
     */
    abstract void connect() throws Exception;

    /**
     * This method is used to let the player log in with a username and choose whether to create a new game, join one or
     * reconnect to a game they were previously playing in.
     * @throws Exception This method can throw a subclass of Exception when the connection with the server malfunctions.
     * @author Mario Merlo
     */
    abstract void login() throws Exception;

    /**
     * This method is used to set the user's username in the server and handles errors when a username is already taken.
     * @throws Exception This method can throw a subclass of Exception when the connection with the server malfunctions.
     * @author Mario Merlo
     */
    abstract void setServerUsername() throws Exception;

    /**
     * This method is used to send the user selected tiles to the server for validation and registration.
     * @throws Exception This method can throw a subclass of Exception when the connection with the server malfunctions.
     * @author Mario Merlo
     */
    abstract void getTiles() throws Exception;

    /**
     * This method is used to send the user selected column to the server for validation and registration.
     * @throws Exception This method can throw a subclass of Exception when the connection with the server malfunctions.
     * @author Mario Merlo
     */
    abstract void getColumn() throws Exception;

    /**
     * This method checks whether the client disconnected from the server and returns to the main menu if it did.
     * @return true if the client disconnected from the server, false otherwise
     * @throws Exception This method can throw a subclass of Exception when the connection with the server malfunctions.
     * @author Mario Merlo
     */
    abstract boolean isDisconnected() throws Exception;

    /**
     * This method triggers the game over screen on the view, passing the player leaderboard to it.
     * @param leaderboard A JSONArray containing the ordered players and their corresponding points
     * @author Mario Merlo
     */
    void gameOver(JSONArray leaderboard) {
        view.gameOverScreen(leaderboard);
    }

    /**
     * This method sends the game data to the view in order to update it.
     * @param gameData The JSONArray containing a representation of the Game object stored in the server and, when
     *                 applicable, a JSONObject specifying which player won what objective.
     * @author Mario Merlo
     */
    void update(JSONArray gameData) {
        // Create Game object from game update message
        Game game = new Game(gameData.getJSONObject(0));

        // Create player order list
        LinkedList<String> playerOrder = new LinkedList<>();
        ArrayList<Player> players = game.getPlayers();

        for (Player player : players)
            playerOrder.add(player.getUsername());

        // Move the player associated to this client to the top of the list
        if (playerOrder.remove(username))
            playerOrder.addFirst(username);

        // Check for won objective JSONObject
        if(gameData.length() == 2){
            JSONObject objectiveStatus = gameData.getJSONObject(1);
            view.showAchievement(objectiveStatus.getString("username"), objectiveStatus.getInt("objective"));
        }

        // Send updates to view
        view.update(game, playerOrder);

        // TODO Fix OK response to game update
        // Respond to update message
        // send(new Message(Message.Header.OK));
    }

    /**
     * Checks whether the passed IP and port combo is valid
     * @param ip The string formatted as ip:port
     * @throws IllegalArgumentException If the IP string is malformed, this exception is thrown
     * @return true if the passed IP is valid, false otherwise
     * @author Mario Merlo
     */
    boolean validateIp(String ip) throws IllegalArgumentException {
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
     * This method transforms the coordinates gathered by the user into a JSON object that will then be parsed
     * by the server in order to validate them.
     * @param coords The ArrayList containing the coordinates chosen by the user.
     * @throws NullPointerException If the ArrayList of coordinates passed is empty, this exception is thrown
     * @return The JSONObject containing a JSONArray with the aforementioned coordinates.
     * @author Mario Merlo
     */
    JSONArray coordsToJson(ArrayList<Coordinate> coords) throws NullPointerException {
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
    ArrayList<Coordinate> parseMoveInput(String input) throws IllegalStateException {
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
     * This method is used to validate and package the tiles in a Message to be sent to the server.
     * @return a Message object with SEND_TILES as a header and the JSON representation of the selected
     *         tiles as the body.
     * @author Mario Merlo
     */
    Message tileValidation() {
        boolean inputValidation = false;
        ArrayList<Coordinate> coordinates = null;

        // This loop does not break until the input from the user is validated by the client.
        // The validation checks whether the user input the correct number of coordinates -- between 1 and 3.
        // The user is also prompted to confirm his own input with the confirmationPrompt method of ViewCLI.
        while(!inputValidation) {
            String input = view.getTiles();
            try {
                coordinates = parseMoveInput(input);
                if(coordinates != null)
                    inputValidation = true;
            } catch (IllegalStateException e) {
                view.showError("You entered an invalid number of coordinates. Retry.");
            }
        }

        return new Message(Message.Header.SEND_TILES, coordsToJson(coordinates));
    }

    /**
     * This method is used to validate and package the column in a Message to be sent to the server.
     * @return a Message object with SEND_COLUMN as a header and the JSON representation of the selected
     *          column as the body.
     * @author Mario Merlo
     */
    Message columnValidation() {
        boolean inputValidation = false;
        int column;
        JSONObject body = null;

        while(!inputValidation) {
            column = view.getColumn();
            if(column >= 0 && column <= 4) {
                inputValidation = true;
                body = new JSONObject().put("column", column);
            } else {
                view.showError("The column you input is invalid. Retry.");
            }
        }

        return new Message(Message.Header.SEND_COLUMN, body);
    }

    /**
     * This method is used to show the error message contained in replies sent by the server on the View.
     * @param reply The Message object sent as a response by the server
     * @author Mario Merlo
     */
    void showError(Message reply) {
        if(reply.getHeaderCode() / 100 == 4)
            view.showError(reply.getBody().getJSONObject(0).getString("message"));
    }

    /**
     * This method checks whether the response sent by the server when validating a username is positive or negative.
     * @param message The response sent by the server
     * @return true if the username is valid, false otherwise
     * @author Mario Merlo
     */
    boolean checkUsernameValidity(Message message) {
        if(message.getHeaderCode() == OK.getCode())
            return true;
        showError(message);
        setUsername(view.getUsername());
        return false;
    }

    /**
     * This method validates the number of players input by the user when creating a new game.
     * @param playerNumber The number of players input by the user
     * @return true if the number is valid, false otherwise
     * @author Mario Merlo
     */
    boolean checkPlayerNumber(int playerNumber) {
        return playerNumber >= 2 && playerNumber <= 4;
    }

    /**
     * This method asks the user to create a new game, join one or reconnect to a game they were previously playing in.
     * The method also validates the number sent by the View.
     * @return the integer input by the user
     * @author Mario Merlo
     */
    int getGameChoice() {
        int choice = 0;
        boolean validGameOption = false;

        while(!validGameOption) {
            choice = view.getGameOptions();
            if(choice >= 1 && choice <= 3)
                validGameOption = true;
            else view.showError("You entered an invalid choice. Retry.");
        }

        return choice;
    }

    /**
     * This method gets the player's username.
     * @return The player's username.
     * @author Mario Merlo
     */
    String getUsername() {
        return username;
    }

    /**
     * This method sets the player's username.
     * @param username The username chosen by the user.
     * @author Mario Merlo
     */
    void setUsername(String username) {
        this.username = username;
    }

    // TODO This might change visibility later on
    /**
     * This method returns the view associated to this client.
     * @return the view associated to this client
     * @author Mario Merlo
     */
    public View getView() {
        return view;
    }
}
