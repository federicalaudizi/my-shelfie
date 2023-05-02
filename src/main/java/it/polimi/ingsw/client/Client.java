package it.polimi.ingsw.client;

import it.polimi.ingsw.server.controller.network.Message;
import org.json.*;

import java.io.IOException;
import java.util.HashMap;

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

    abstract void connect() throws IOException;
    abstract void login() throws IOException;
    abstract void startGame() throws UnknownError, IOException;
    abstract void move() throws NullPointerException, UnknownError, IOException;
    void gameOver(Message gameOverMessage) {
        JSONArray messageBody = gameOverMessage.getBody();
        // TODO Finish implementation
    }
    abstract void reconnect() throws IOException;
    abstract void viewUpdate(JSONObject gameState);
    abstract Message getReply() throws NullPointerException;
    abstract void send(Message message) throws IOException;

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
