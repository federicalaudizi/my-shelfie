package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Player;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    int choicePrompt(String message, String[] options) {
        while(true) {
            System.out.println(message);
            for(int i = 0; i < options.length; i++)
                System.out.println(i + 1 + ". " + options[i]);
            System.out.println("Your choice: ");
            int choice = scanner.nextInt();
            System.out.print("Are you sure this is ok? (y/n) ");
            String selection = scanner.nextLine();
            if(selection.equals("y"))
                return choice;
        }
    }

    @Override
    String gameIdSelection(ArrayList<String> gameIds) {
        System.out.println("Select one of the following game IDs:");
        for(String gameId : gameIds)
            System.out.println(gameId);
        while(true) {
            System.out.println("Your choice: ");
            String choice = scanner.nextLine();
            if(gameIds.contains(choice)) {
                System.out.print("Are you sure this is ok? (y/n) ");
                String confirmation = scanner.nextLine();
                if(confirmation.equals("y")) {
                    return choice;
                }
            } else {
                System.out.println("You selected an invalid game ID. Please retry.");
            }
        }
    }

    @Override
    void gameOverScreen(HashMap<String, Integer> leaderboard) {
        try {
            Runtime.getRuntime().exec(new String[] { "clear" });
        } catch (IOException e) {
            System.out.println("Unable to clear screen. Printing below game view.");
        } finally {
            Map.Entry<String, Integer> entry = leaderboard.entrySet().iterator().next();
            String winner = entry.getKey();
            if(winner.equals(client.getUsername()))
                System.out.println("                                YOU WON!                                ");
            else
                System.out.println("                             " + winner + " WON!                             ");
            System.out.println("                         | " + winner + " | " + entry.getValue() + " |                         ");
            boolean donePrinting = false;
            for(Map.Entry<String, Integer> player : leaderboard.entrySet()) {
                System.out.println("                         | " + player.getKey() + " | " + player.getValue() + " |                         ");
            }
        }
    }
}
