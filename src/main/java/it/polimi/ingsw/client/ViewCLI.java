package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Tile;
import org.json.JSONArray;

import java.io.IOException;
import java.util.*;

public class ViewCLI extends View {
    private final String twoPlayerView = """
            You (?)         | Board             | Your objective:
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | $ $ $ $ $
            --------------- | ^ ^ ^ ^ ^ ^ ^ ^ ^ | ---------------
            ¥2       (?)    | ^ ^ ^ ^ ^ ^ ^ ^ ^ |
            @ @ @ @ @       | ^ ^ ^ ^ ^ ^ ^ ^ ^ |
            @ @ @ @ @       | ----------------- |
            @ @ @ @ @       |                   |
            @ @ @ @ @       |                   |
            @ @ @ @ @       |                   |
            @ @ @ @ @       |                   |
            -----------------------------------------------------
            Objective I (&1): £1
            Objective II (&2): £2
            """;
    private final String threePlayerView = """
            You (?)         | Board             | ¥3       (?)   
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %
            --------------- | ^ ^ ^ ^ ^ ^ ^ ^ ^ | ---------------
            ¥2       (?)    | ^ ^ ^ ^ ^ ^ ^ ^ ^ | Your objective:
            @ @ @ @ @       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | $ $ $ $ $
            @ @ @ @ @       | ----------------- | $ $ $ $ $
            @ @ @ @ @       |                   | $ $ $ $ $
            @ @ @ @ @       |                   | $ $ $ $ $
            @ @ @ @ @       |                   | $ $ $ $ $
            @ @ @ @ @       |                   | $ $ $ $ $
            -----------------------------------------------------
            Objective I (&1): £1
            Objective II (&2): £2
            """;
    private final String fourPlayerView = """
            You (?)         | Board             | ¥3       (?)    | Your objective:
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %       | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %       | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %       | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %       | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %       | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %       | $ $ $ $ $
            --------------- | ^ ^ ^ ^ ^ ^ ^ ^ ^ | --------------- | ---------------
            ¥2       (?)    | ^ ^ ^ ^ ^ ^ ^ ^ ^ | ¥4       (?)    |
            @ @ @ @ @       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | # # # # #       |
            @ @ @ @ @       | ----------------- | # # # # #       |
            @ @ @ @ @       |                   | # # # # #       |
            @ @ @ @ @       |                   | # # # # #       |
            @ @ @ @ @       |                   | # # # # #       |
            @ @ @ @ @       |                   | # # # # #       |
            -----------------------------------------------------------------------
            Objective I (&): £
            Objective II (&): £
            """;
    Scanner scanner = new Scanner(System.in);
    private final int MAX_USERNAME_CHARS = 15;
    public ViewCLI(Client client) {
        this.client = client;
    }

    private String composeView(HashMap<String, JSONArray> gameData, HashMap<String, JSONArray> playerData, LinkedList<String> playerOrder, int playerNumber) {
        /*// Convert board to string
        String board = jsonMatrixToString(gameData.get("board"));

        // Convert shelves to strings
        String[] shelves = new String[playerNumber];
        for(int i = 0; i < playerNumber; i++)
            shelves[i] = jsonMatrixToString(playerData.get(playerOrder.get(i)).getJSONArray(0));

        // Get personal objective of the player associated to this client
        String personalObjective = jsonMatrixToString(playerData.get(playerOrder.get(i)).getJSONArray(1));

        // Get descriptions for the collective objectives in the game
        String[] objectiveDescriptions = new String[2];
        JSONArray objectives = gameData.get("objectives");
        objectiveDescriptions[0] = ObjectiveDescription.init().get(objectives.get(0));
        objectiveDescriptions[1] = ObjectiveDescription.init().get(objectives.get(1));

        // Compose view with obtained strings
        StringBuilder composedView = new StringBuilder();
        composedView.append(usernamePadding("You")).append(" | ")
                .append(usernamePadding("Board")).append(" | ");
        if(playerNumber > 2)
            composedView.append(usernamePadding(playerOrder.get(2))).append(" | ");

        // Add vertical padding to board splits
        String[] boardSplits = new String[15];
        String[] boardSplits = board.split("\n");
        boardSplits[9] = "-----------------";
        for(int i = boardSplits.length; i < 15; i++)
            boardSplits[i] = "                 ";

        String[] shelfSplits;
        for(int i = 0; i < playerNumber; i++) {
            shelfSplits = shelves[i].split("\n");
            int j = 0, k = 0;
            while(j < shelfSplits.length) {
                composedView.append(shelfSplits[j]).append("| ");
                composedView.append(boardSplits[j]).append("| ")
            }
        }*/
    }

    @Override
    void update(HashMap<String, JSONArray> gameData, LinkedList<String> playerOrder, boolean lastTurn, int achievement, int playerNumber) {

    }

    private String usernamePadding(String username) {
        if(username.length() < MAX_USERNAME_CHARS) {
            StringBuilder usernamePadder = new StringBuilder(username);
            while(usernamePadder.length() < MAX_USERNAME_CHARS)
                usernamePadder.append(" ");
            return usernamePadder.toString();
        } else return username;
    }

    private String jsonMatrixToString(JSONArray matrix) {
        StringBuilder board = new StringBuilder();

        for(int i = 0; i < matrix.length(); i++) {
            // Get every row of the board into a JSONArray
            JSONArray row = new JSONArray();
            row.put(matrix.get(i));

            for(int j = 0; j < row.length(); j++) {
                // Get every tile of that row
                Tile currentTile = row.getEnum(Tile.class, j);
                // Convert that tile to a character
                switch(currentTile) {
                    case OUTSIDE_GAME_BOARD -> board.append("x ");
                    case EMPTY -> board.append("e ");
                    case CATS -> board.append("g ");
                    case PLANTS -> board.append("m ");
                    case FRAMES -> board.append("b ");
                    case TROPHIES -> board.append("a ");
                    case GAMES -> board.append("o ");
                    case BOOKS -> board.append("y ");
                }
            }
            // Append a newline at the end of the row
            board.append("\n");
        }

        return board.toString();
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
