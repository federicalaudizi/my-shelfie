package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Coordinate;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class handles the interactions between the user and the server.
 *
 * @author Mario Merlo
 */
public abstract class Client {
    private String username;
    final View view;

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

    public abstract void start() throws Exception;

    abstract void connect() throws IOException;

    abstract void login() throws IOException;

    abstract void getTiles() throws Exception;

    abstract void getColumn() throws Exception;

    abstract boolean reconnect() throws Exception;

    /**
     * Triggers the game over screen on the view, passing the player leaderboard to it
     *
     * @param leaderboard The message containing the leaderboard
     * @author Mario Merlo
     */
    void gameOver(JSONArray leaderboard) {
        view.gameOverScreen(leaderboard);
    }

    /**
     * Sends the game data to the view in order to update it
     *
     * @param gameData The JSONObject containing a representation of the Game object stored in the server
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
        try {
            JSONObject objectiveStatus = gameData.getJSONObject(1);
            view.showAchievement(objectiveStatus.getString("username"), objectiveStatus.getInt("objective"));
        } catch (JSONException e) {
            // No objectives were won, so there is nothing else to do
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
     * Gets the player's username.
     *
     * @return The player's username.
     * @author Mario Merlo
     */
    String getUsername() {
        return username;
    }

    /**
     * Sets the player's username.
     *
     * @param username The username chosen by the user.
     * @author Mario Merlo
     */
    void setUsername(String username) {
        this.username = username;
    }

    // TODO This might change visibility later on

    /**
     * Returns the view associated to this client
     *
     * @return the view associated to this client
     * @author Mario Merlo
     */
    public View getView() {
        return view;
    }
}
