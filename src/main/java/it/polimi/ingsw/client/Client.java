package it.polimi.ingsw.client;

import it.polimi.ingsw.server.controller.network.Message;
import org.json.*;

/**
 * This class handles the interactions between the user and the server.
 *
 * @author Mario Merlo
 */
public abstract class Client {
    String username;
    final View view;

    public Client(boolean cli) {
        if(cli)
            view = new ViewCLI(this);
        else
            view = new ViewGUI(this);
    }

    abstract void connect();
    abstract void login();
    abstract void move();
    void gameOver(Message gameOverMessage) {
        JSONArray messageBody = gameOverMessage.getBody();
        // TODO Finish implementation
    }
    abstract void viewUpdate(JSONObject gameState);
    abstract JSONObject getReply();
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
