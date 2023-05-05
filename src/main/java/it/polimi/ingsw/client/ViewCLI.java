package it.polimi.ingsw.client;

import org.json.JSONArray;

import java.io.IOException;
import java.util.*;

public class ViewCLI extends View {
    Scanner scanner = new Scanner(System.in);

    public ViewCLI(Client client) {
        this.client = client;
    }

    private String composeView() {
        // TODO Implement this method
        return null;
    }

    @Override
    void update(HashMap<String, JSONArray> gameData, LinkedList<String> playerOrder, boolean lastTurn, int achievement) {
        System.out.println("ViewCLI.update() was called!");
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
        System.out.println("Press any key to continue");
        scanner.nextLine();
    }

    @Override
    int choicePrompt(String message, String[] options) {
        while(true) {
            System.out.println(message);
            for(int i = 0; i < options.length; i++)
                System.out.println(i + 1 + ". " + options[i]);
            System.out.print("Your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());
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
            System.out.print("Your choice: ");
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
            for(Map.Entry<String, Integer> player : leaderboard.entrySet()) {
                System.out.println("                         | " + player.getKey() + " | " + player.getValue() + " |                         ");
            }
        }
    }
}
