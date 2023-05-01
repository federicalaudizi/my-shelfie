package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Player;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Scanner;

public class ViewCLI extends View {
    Scanner scanner = new Scanner(System.in);

    public ViewCLI(Client client) {
        this.client = client;
    }

    private String composeView(JSONObject gameState) {
        // TODO Implement this method
        return null;
    }

    @Override
    void update(JSONObject gameState) {
        // If there is no data about the game stored in the view, then this is the
        // first turn in the game and the view can save some immutable data in order
        // to reprint it when needed.

        // TODO Refactor to make more efficient

        // gameData.put(gameState.getJSONArray("players").getJSONObject(0).getString("username"), gameState.get)

        if(gameData.isEmpty()) {
            gameData.put("board", gameState.getJSONObject("board"));
            gameData.put("players", gameState.getJSONObject("players"));
            gameData.put("objective1", gameState.getJSONObject("collectiveObjectiveCard1"));
            gameData.put("objective2", gameState.getJSONObject("collectiveObjectiveCard2"));
            gameData.put("pointDeck1", gameState.getJSONObject("pointCardDeck1"));
            gameData.put("pointDeck2", gameState.getJSONObject("pointCardDeck2"));
        } else { // Otherwise, only the updated objects are actually updated in the HashMap

        }
    }

    @Override
    String confirmationPrompt(String message) {
        while(true) {
            System.out.print(message);
            String input = scanner.nextLine();
            System.out.print("Are you sure this is ok? (y/n) ");
            String selection = scanner.nextLine();
            if(selection.equals("y"))
                return input;
        }
    }

    @Override
    void okPrompt(String message) {
        System.out.println(message);
        System.out.println();
        System.out.println("Press any key to continue");
        scanner.nextLine();
    }

    @Override
    void gameOverScreen(ArrayList<Player> leaderboard) {
        // TODO Finish implementation
    }

    @Override
    void gameOverScreen(String winnerUsername) {
        // TODO Finish implementation
    }
}
