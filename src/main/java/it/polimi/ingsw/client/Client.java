package it.polimi.ingsw.client;

import it.polimi.ingsw.server.controller.network.Message;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import org.json.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class handles the interactions between the user and the server.
 *
 * @author Mario Merlo
 */
public abstract class Client {
    private String username;
    final View view;

    public Client(boolean cli) {
        if(cli)
            view = new ViewCLI(this);
        else
            view = new ViewGUI(this);
    }

    // TODO This is a test constructor and should be removed
    public Client(boolean cli, String username) {
        if(cli)
            view = new ViewCLI(this);
        else
            view = new ViewGUI(this);
        this.username = username;
    }

    public abstract void start();
    abstract void connect() throws IOException;
    abstract void login() throws IOException;
    abstract void startGame() throws UnknownError, IOException;
    abstract void move() throws NullPointerException, UnknownError, IOException;
    void gameOver(Message gameOverMessage) {
        JSONArray leaderboardJSON = gameOverMessage.getBody().getJSONObject(0).getJSONArray("leaderboard");
        HashMap<String, Integer> leaderboard = new HashMap<>();
        for(int i = 0; i < leaderboardJSON.length(); i++) {
            JSONObject player = leaderboardJSON.getJSONObject(i);
            leaderboard.put(player.getString("playerId"), Integer.parseInt(player.getString("points")));
        }
        view.gameOverScreen(leaderboard);
    }

    void update(JSONObject gameData) {
        // Create Game object from game update message
        Game game = new Game(gameData);

        // Create player order list
        LinkedList<String> playerOrder = new LinkedList<>();
        ArrayList<Player> players = game.getPlayers();

        for(Player player : players)
            playerOrder.add(player.getUsername());

        // Move the player associated to this client to the top of the list
        if(playerOrder.remove(username))
            playerOrder.addFirst(username);

        // Send updates to view
        view.update(game, playerOrder);
    }
    abstract void reconnect() throws IOException;
    abstract Message getReply() throws NullPointerException;
    abstract void send(Message message);

    /**
     * Gets the player's username.
     * @return The player's username.
     */
    String getUsername() {
        return username;
    }

    /**
     * Sets the player's username.
     * @param username The username chosen by the user.
     */
    void setUsername(String username) {
        this.username = username;
    }
}
