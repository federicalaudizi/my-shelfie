package it.polimi.ingsw.client;

import it.polimi.ingsw.server.controller.network.Message;
import org.json.*;

import java.io.IOException;
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
    private HashMap<String, JSONArray> gameData;
    private LinkedList<String> playerList;

    public Client(boolean cli) {
        gameData = new HashMap<>();
        playerList = new LinkedList<>();
        if(cli)
            view = new ViewCLI(this);
        else
            view = new ViewGUI(this);
    }

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
        int achievedObjective = -1;
        boolean lastTurn;

        // Save board information
        JSONArray board = gameData.getJSONArray("board"); // Game board
        if(!this.gameData.containsKey("board"))
            this.gameData.put("board", board);
        else this.gameData.replace("board", board);

        // Save player information
        try {
            // If it's the first game update, then gameData will contain the field "players"
            JSONArray players = gameData.getJSONArray("players"); // Username, shelf and objective of the players
            for(int i = 0; i < players.length(); i++) {
                // Get player data from the update message
                JSONObject player = players.getJSONObject(i);
                // Format player data to save it into the gameData HashMap
                JSONArray playerData = new JSONArray();
                playerData.put(player.getJSONArray("shelf"));
                playerData.put(player.getJSONArray("objective"));
                this.gameData.put(player.getString("username"), playerData);
                // Update playerList in order to let the view know in what order the shelves must be rendered
                playerList.add(player.getString("username"));
            }
            // Move the player connected to the client to the top of the list
            if(playerList.remove(username)) {
                playerList.addFirst(username);
            }
        } catch (JSONException e) {
            // Otherwise, it will contain the field "player"
            JSONObject player = gameData.getJSONObject("player"); // Username and shelf only
            // If the entry exists in the HashMap, then replace the shelf contained in gameData with the updated one
            if(this.gameData.containsKey(player.getString("username"))) {
                JSONArray buffer = this.gameData.get(player.getString("username"));
                buffer.put(0, player.getJSONArray("shelf"));
                this.gameData.replace(player.getString("username"), buffer);
            }
        }

        // Save objectives information
        try {
            // If it's the first game update, then gameData will contain the field "objectives"
            JSONArray objectives = gameData.getJSONArray("objectives"); // Collective objective data
            this.gameData.put("objectives", objectives);
        } catch (JSONException e) {
            // Otherwise, the objectives never change, so there's no need to update them again.
        }

        // Save point decks information and trigger objective completion
        if (!this.gameData.containsKey("pointDecks")) {
            JSONArray pointDecks = gameData.getJSONArray("pointDecks"); // Points for the completion of objectives
            this.gameData.put("pointDecks", pointDecks);
        } else {
            JSONArray oldDecks = this.gameData.get("pointDecks"), newDecks = gameData.getJSONArray("pointDecks");
            if(oldDecks.getInt(0) != newDecks.getInt(0))
                achievedObjective = 1;
            if(oldDecks.getInt(1) != newDecks.getInt(1))
                achievedObjective = 2;
        }

        // Check whether it's the last turn and trigger last turn warning
        try {
            lastTurn = gameData.getBoolean("lastTurn");
        } catch (JSONException e) {
            // If it's the first turn, this field won't be available in the update message
            lastTurn = false;
        }

        // Send updates to view
        view.update(this.gameData, playerList, lastTurn, achievedObjective);
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
