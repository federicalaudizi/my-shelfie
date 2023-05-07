package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Game;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

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

    private void composeView(Game game, LinkedList<String> playerOrder) {
        // Get correct view based on player number
        String view = ViewPrototypes.getViewByPlayerNum(playerOrder.size());

        // Get board, shelf and objective associated to the client's user and the second player's shelf
        String board = game.getBoard().toString();
        String userShelf = game.getPlayerByUsername(client.getUsername()).getShelf().toString();
        String userObjective = game.getPlayerByUsername(client.getUsername()).getObjective().toString();
        String playerTwoShelf = game.getPlayerByUsername(playerOrder.get(1)).getShelf().toString();

        // Update board on view
        // TODO Make boundaries dynamic
        for (int i = 0; i < 81; i++)
            view = Pattern.compile("\\^").matcher(view).replaceFirst(board.substring(i, i + 1));

        // Update shelves on board
        for (int i = 0; i < 30; i++) {
            // Print user shelf and objective
            view = Pattern.compile("\\*").matcher(view).replaceFirst(userShelf.substring(i, i + 1));
            view = Pattern.compile("\\$").matcher(view).replaceFirst(userObjective.substring(i, i + 1));

            // Print player two's shelf
            view = Pattern.compile("@").matcher(view).replaceFirst(playerTwoShelf.substring(i, i + 1));
            if(playerOrder.size() >= 3) {
                // If applicable, print player three's shelf
                String playerThreeShelf = game.getPlayerByUsername(playerOrder.get(2)).getShelf().toString();
                view = Pattern.compile("%").matcher(view).replaceFirst(playerThreeShelf.substring(i, i + 1));

                if(playerOrder.size() == 4) {
                    // If applicable, print player four's shelf
                    String playerFourShelf = game.getPlayerByUsername(playerOrder.get(3)).getShelf().toString();
                    view = Pattern.compile("#").matcher(view).replaceFirst(playerFourShelf.substring(i, i + 1));
                }
            }
        }

        // Update usernames on board
        for(int i = 0; i < playerOrder.size(); i++) {
            String currentPlayer = playerOrder.get(i);
            view = Pattern.compile(i + "{15}").matcher(view).replaceFirst(usernameFormatter(currentPlayer, game.getPlayerByUsername(currentPlayer).getPointCardStatus()));
        }

        // Substitute objective descriptions and remaining points
        for(int i = 0; i < 2; i++) {
            view = Pattern.compile("&").matcher(view).replaceFirst(String.valueOf(game.getPointsValue()[i]));
            view = Pattern.compile("£").matcher(view).replaceFirst(ObjectiveDescription.getDescriptionFromName(game.getObjectives()[i]));
        }

        // Print composed view
        System.out.println(view);
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
