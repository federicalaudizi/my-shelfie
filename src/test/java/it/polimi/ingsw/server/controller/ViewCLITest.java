package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.ViewCLI;
import it.polimi.ingsw.server.model.Game;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.Assert.fail;

public class ViewCLITest {
    View view;
    Game twoPlayerGame, threePlayerGame, fourPlayerGame;
    LinkedList<String> playerOrder = new LinkedList<>();
    ArrayList<String> usernames = new ArrayList<>();

    @Before
    public void init() {
        // Initialize games
        twoPlayerGame = new Game(2);
        threePlayerGame = new Game(3);
        fourPlayerGame = new Game(4);

        // Add the first two players
        playerOrder.add("Mario");
        usernames.add("Mario");
        playerOrder.add("Martina");
        usernames.add("Martina");

        // Set the player usernames in twoPlayerGame
        twoPlayerGame.setUsernames(usernames);

        // Initialize a client with the username of the first player and get its view
        Client client = new ClientSocket(true, "Mario");
        view = client.getView();
    }

    @Test
    public void composeViewTest() {
        try {
            // Initialize private composeView method
            Method composeViewMethod = ViewCLI.class.getDeclaredMethod("composeView", Game.class, LinkedList.class);
            composeViewMethod.setAccessible(true);

            // Call composeView on twoPlayerGame
            composeViewMethod.invoke(view, twoPlayerGame, playerOrder);

            // Add the third player and set usernames in threePlayerGame
            playerOrder.add("Margherita");
            usernames.add("Margherita");
            threePlayerGame.setUsernames(usernames);

            // Call composeView on threePlayerGame
            composeViewMethod.invoke(view, threePlayerGame, playerOrder);

            // Add the fourth player and set usernames in fourPlayerGame
            playerOrder.add("Camilla");
            usernames.add("Camilla");
            fourPlayerGame.setUsernames(usernames);

            // Call composeView on fourPlayerGame
            composeViewMethod.invoke(view, fourPlayerGame, playerOrder);

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void gameOverScreenTest() {
        JSONArray leaderboard = new JSONArray().put(new JSONObject().put("username", "Mario").put("points", 120))
                .put(new JSONObject().put("username", "Martina").put("points", 69))
                .put(new JSONObject().put("username", "Margherita").put("points", 8))
                .put(new JSONObject().put("username", "Camilla").put("points", 0));

        try {
            Method gameOverScreen = ViewCLI.class.getDeclaredMethod("gameOverScreen", JSONArray.class);
            gameOverScreen.setAccessible(true);
            gameOverScreen.invoke(view, leaderboard);
        } catch (Exception e) {
            fail();
        }
    }
}
