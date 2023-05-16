package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Coordinate;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import org.json.JSONArray;
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

    abstract void move() throws NullPointerException, UnknownError, IOException;

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
    void update(JSONObject gameData) {
        // Create Game object from game update message
        Game game = new Game(gameData);

        // Create player order list
        LinkedList<String> playerOrder = new LinkedList<>();
        ArrayList<Player> players = game.getPlayers();

        for (Player player : players)
            playerOrder.add(player.getUsername());

        // Move the player associated to this client to the top of the list
        if (playerOrder.remove(username))
            playerOrder.addFirst(username);

        // Send updates to view
        view.update(game, playerOrder);

        // TODO Fix OK response to game update
        // Respond to update message
        // send(new Message(Message.Header.OK));
    }

    abstract void reconnect() throws IOException;

    abstract Message getReply() throws NullPointerException;

    abstract void send(Message message);

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
