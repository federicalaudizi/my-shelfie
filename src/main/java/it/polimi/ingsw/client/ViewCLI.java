package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Game;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class ViewCLI extends View {
    Scanner scanner = new Scanner(System.in);

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

    private String usernameFormatter(String username, int completedObjectives) {
        // Create StringBuilder starting from the passed username
        StringBuilder formattedUsername = new StringBuilder(username);

        // Append the completed objective badge
        switch (completedObjectives) {
            case 1 -> formattedUsername.append(" (I)");
            case 2 -> formattedUsername.append(" (II)");
            case 3 -> formattedUsername.append(" (I/II)");
        }

        // Pad to maintain correct formatting
        final int MAX_USERNAME_CHARS = 15;
        if(formattedUsername.length() < MAX_USERNAME_CHARS) {
            while(formattedUsername.length() < MAX_USERNAME_CHARS)
                formattedUsername.append(" ");
        }

        // Return formatted username
        return formattedUsername.toString();
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

enum ViewPrototypes {
    TWO_PLAYERS(2, """
            111111111111111 | Board             | Your objective:
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | $ $ $ $ $
            --------------- | ^ ^ ^ ^ ^ ^ ^ ^ ^ | ---------------
            222222222222222 | ^ ^ ^ ^ ^ ^ ^ ^ ^ |
            @ @ @ @ @       | ^ ^ ^ ^ ^ ^ ^ ^ ^ |
            @ @ @ @ @       | ----------------- |
            @ @ @ @ @       |                   |
            @ @ @ @ @       |                   |
            @ @ @ @ @       |                   |
            @ @ @ @ @       |                   |
            -----------------------------------------------------
            Objective I (&): £
            Objective II (&): £
            """),
    THREE_PLAYERS(3, """
            111111111111111 | Board             | 333333333333333
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %
            --------------- | ^ ^ ^ ^ ^ ^ ^ ^ ^ | ---------------
            222222222222222 | ^ ^ ^ ^ ^ ^ ^ ^ ^ | Your objective:
            @ @ @ @ @       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | $ $ $ $ $
            @ @ @ @ @       | ----------------- | $ $ $ $ $
            @ @ @ @ @       |                   | $ $ $ $ $
            @ @ @ @ @       |                   | $ $ $ $ $
            @ @ @ @ @       |                   | $ $ $ $ $
            @ @ @ @ @       |                   | $ $ $ $ $
            -----------------------------------------------------
            Objective I (&): £
            Objective II (&): £
            """),
    FOUR_PLAYERS(4, """
            111111111111111 | Board             | 333333333333333 | Your objective:
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %       | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %       | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %       | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %       | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %       | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | % % % % %       | $ $ $ $ $
            --------------- | ^ ^ ^ ^ ^ ^ ^ ^ ^ | --------------- | ---------------
            222222222222222 | ^ ^ ^ ^ ^ ^ ^ ^ ^ | 444444444444444 |
            @ @ @ @ @       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | # # # # #       |
            @ @ @ @ @       | ----------------- | # # # # #       |
            @ @ @ @ @       |                   | # # # # #       |
            @ @ @ @ @       |                   | # # # # #       |
            @ @ @ @ @       |                   | # # # # #       |
            @ @ @ @ @       |                   | # # # # #       |
            -----------------------------------------------------------------------
            Objective I (&): £
            Objective II (&): £
            """);

    final int playerNumber;
    final String viewPrototype;

    ViewPrototypes(int playerNumber, String viewPrototype) {
        this.playerNumber = playerNumber;
        this.viewPrototype = viewPrototype;
    }

    public static String getViewByPlayerNum(int playerNumber) {
        for(ViewPrototypes viewPrototype : ViewPrototypes.values()) {
            if(viewPrototype.playerNumber == playerNumber)
                return viewPrototype.viewPrototype;
        }
        throw new IllegalArgumentException("No such game with " + playerNumber + " players.");
    }
}

enum ObjectiveDescription {
    PATTERNONE("Six groups each containing at least two tiles of the same type."),
    PATTERNTWO("Four groups each containing al least 4 tiles of the same type."),
    PATTERNTHREE("Four tiles of the same type in the four corners of the bookshelf."),
    PATTERNFOUR("Two groups each containing 4 tiles of the same type in a 2x2 square."),
    PATTERNFIVE("Three columns each formed by 6 tiles of maximum 3 different types."),
    PATTERNSIX("Eight tiles of the same type."),
    PATTERNSEVEN("Five tiles of the same type forming a diagonal."),
    PATTERNEIGHT("Four lines each formed by 5 tiles of maximum three different types."),
    PATTERNNINE("Two columns each formed by 6 different types of tiles."),
    PATTERNTEN("Two lines each formed by 5 different types of tiles."),
    PATTERNELEVEN("Five tiles of the same type forming an X."),
    PATTERNTWELVE("Five columns of increasing or decreasing height.");

    private final String description;
    ObjectiveDescription(String description) {
        this.description = description;
    }

    private String getDescription() {
        return this.description;
    }

    public static String getDescriptionFromName(String name) {
        for(ObjectiveDescription desc : ObjectiveDescription.values()) {
            if(desc.name().equalsIgnoreCase(name))
                return desc.getDescription();
        }
        throw new IllegalArgumentException("No such pattern name.");
    }
}