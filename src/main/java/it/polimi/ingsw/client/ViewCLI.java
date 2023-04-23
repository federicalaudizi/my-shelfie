package it.polimi.ingsw.client;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Scanner;

public class ViewCLI extends View {
    private Client client;

    Scanner scanner = new Scanner(System.in);

    public ViewCLI(Client client) {
        this.client = client;
    }

    public String getInput() {

    }

    private String composeView(JSONObject gameStatus) {
        JSONObject board = (JSONObject) gameStatus.get("board");
        JSONArray shelves = gameStatus.getJSONArray("shelves");
        String[] objectives = new String[2];
        // objectives[0] = gameStatus.get()

        // TODO Get a map with both the shelf and the username of the player with that shelf
        String[] shelves = client.getShelves();
        StringBuilder shelfFormatter = new StringBuilder();
        StringBuilder boardFormatter = new StringBuilder();

        // Add three newlines at the end of every shelf
        for(String shelf : shelves)
            shelfFormatter.append(shelf).append("\n\n\n");

        // Add title to board
        boardFormatter.append("- Board -\n").append(board);

        // Combine board and shelves horizontally
        String[] boardSplits;
        boardSplits = boardFormatter.toString().split("\n");

    }

    @Override
    void update() {

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
}
