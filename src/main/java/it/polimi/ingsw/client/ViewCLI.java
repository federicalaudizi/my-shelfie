package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Game;
import org.json.JSONArray;

import java.util.*;
import java.util.regex.Pattern;

public class ViewCLI extends View {
    public static final int MAX_USERNAME_CHARS = 15;
    Scanner scanner = new Scanner(System.in);

    /**
     * The constructor takes the passed client and associates it to the view
     * @param client The client associated to the newly created view
     * @author Mario Merlo
     */
    public ViewCLI(Client client) {
        this.client = client;
    }

    /**
     * Constructs the CLI view starting from a Game object representing the ongoing game and a list of players that
     * specifies the player order. The views are gathered by the ViewPrototypes enum and the placeholders are
     * substituted with the player data.
     * @param game The Game object defining the ongoing game
     * @param playerOrder a LinkedList containing the player associated to the client in the first position and the
     *                    other players in the other positions
     * @author Mario Merlo
     */
    private void composeView(Game game, LinkedList<String> playerOrder) {
        // Get correct view based on player number
        String view = ViewPrototypes.getViewByPlayerNum(playerOrder.size());

        // Get board, shelf and objective associated to the client's user and the second player's shelf
        String board = game.getBoard().toString();
        String userShelf = game.getPlayerByUsername(client.getUsername()).getShelf().toString();
        String userObjective = game.getPlayerByUsername(client.getUsername()).getObjective().toString();
        String playerTwoShelf = game.getPlayerByUsername(playerOrder.get(1)).getShelf().toString();

        // Update board on view
        int boardSize = game.getBoard().getMAX_X() * game.getBoard().getMAX_Y();
        for (int i = 0; i < boardSize; i++)
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
            view = Pattern.compile(i + 1 + "{15}").matcher(view).replaceFirst(usernameFormatter(currentPlayer, game.getPlayerByUsername(currentPlayer).getPointCardStatus()));
        }

        // Substitute objective descriptions and remaining points
        for(int i = 0; i < 2; i++) {
            view = Pattern.compile("&").matcher(view).replaceFirst(String.valueOf(game.getPointsValue()[i]));
            view = Pattern.compile("£").matcher(view).replaceFirst(ObjectiveDescription.getDescriptionFromName(game.getObjectives()[i]));
        }

        // Print composed view
        System.out.println(view);
    }

    /**
     * Formats the username to account for the correct amount of padding and adds the completed objective badges to
     * the username of the player who reached them.
     * @param username The username to be formatted
     * @param completedObjectives An integer representing which objectives were reached: 1 for objective I, 2 for
     *                            objective II and 3 for both
     * @return The formatted username with a badge (where applicable)
     * @author Mario Merlo
     */
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
        if(formattedUsername.length() < MAX_USERNAME_CHARS) {
            while(formattedUsername.length() < MAX_USERNAME_CHARS)
                formattedUsername.append(" ");
        }

        // Return formatted username
        return formattedUsername.toString();
    }

    /**
     * Triggers the last turn warning and calls compose view to print out the CLI view
     * @param game The Game object defining the ongoing game
     * @param playerOrder The list of players. The first spot of the list always contains the player associated to
     *                    the current client.
     * @author Mario Merlo
     */
    @Override
    void update(Game game, LinkedList<String> playerOrder) {
        if(game.isLastTurn())
            okPrompt("Warning: this is the last turn!");
        composeView(game, playerOrder);
    }

    @Override
    String getIp() {
        return confirmationPrompt("Enter the server's IP (ip:port): ");
    }

    @Override
    String getUsername() {
        System.out.print("Enter a username: ");
        return scanner.nextLine();
    }

    @Override
    int getGameOptions() {
        String[] options = { "Create a new game", "Join a new game", "Reconnect to an ongoing game" };
        return choicePrompt("What do you want to do?", options);
    }

    @Override
    int getPlayerNumber() {
        return Integer.parseInt(confirmationPrompt("Enter the number of players (between 2 and 4): "));
    }

    @Override
    String getTiles() {
        return confirmationPrompt("Enter up to three coordinates.\nSyntax: (x, y)[, (x, y), (x, y)]\n Your choice: ");
    }

    @Override
    int getColumn() {
        return Integer.parseInt(confirmationPrompt("Enter the column you want to put the tiles in.\nPossible values: 0 to 4.\nYour choice: "));
    }

    @Override
    void showAchievement(String username, int objectiveNumber) {
        StringBuilder achievementMessage = new StringBuilder();

        if(username.equals(client.getUsername()))
            achievementMessage.append(client.getUsername());
        else
            achievementMessage.append(username);

        achievementMessage.append(" has won");

        switch(objectiveNumber) {
            case 1 -> achievementMessage.append(" objective I!");
            case 2 -> achievementMessage.append(" objective II!");
            case 3 -> achievementMessage.append(" both objectives!");
        }

        System.out.println(achievementMessage);
    }

    @Override
    void showError(String errorMessage) {
        System.out.println(errorMessage);
    }

    /**
     * Shows a prompt that asks for input confirmation through a standard "y/n" prompt
     * @param message The message to be printed on screen
     * @return The user input once it has been confirmed
     * @author Mario Merlo
     */
    private String confirmationPrompt(String message) {
        while(true) {
            System.out.print(message);
            String input = scanner.nextLine();
            System.out.print("Are you sure this is ok? (y/n) ");
            String selection = scanner.nextLine();
            if(selection.equals("y"))
                return input;
        }
    }

    /**
     * Shows a prompt that can be discarded by pressing enter.
     * @param message The message to be printed on the screen
     * @author Mario Merlo
     */
    private void okPrompt(String message) {
        System.out.println(message);
        System.out.println("Press enter to continue");
        scanner.nextLine();
    }

    /**
     * Shows an indexed list of choices. The user can select one of the options through its index and then confirm it
     * through a standard "y/n" prompt.
     * @param message The message to be printed on the screen
     * @param options The options to be indexed and then shown to the user
     * @return The index number chosen by the user
     * @author Mario Merlo
     */
    private int choicePrompt(String message, String[] options) {
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

    /**
     * Shows the currently ongoing games to the user and asks them to select one. The user's choice is then confirmed
     * through a standard "y/n" prompt.
     * @param gameIds An ArrayList containing the game IDs of the currently ongoing games.
     * @return The game ID selected by the user
     * @author Mario Merlo
     */
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

    /**
     * Shows the game over message and the game leaderboard, thus announcing the winner.
     * @param leaderboard The leaderboard of the game
     * @author Mario Merlo
     */

    @Override
    void gameOverScreen(JSONArray leaderboard) {
        String leaderboardPrototype = LeaderboardPrototypes.getLeaderboardByPlayerNum(leaderboard.length());


        for(int i = 0; i < MAX_USERNAME_CHARS; i++) {
            leaderboardPrototype = Pattern.compile("\\+").matcher(leaderboardPrototype).replaceFirst(usernameFormatter(leaderboard.getJSONObject(0).getString("username"), 0).substring(i, i + 1));
            leaderboardPrototype = Pattern.compile("\\?").matcher(leaderboardPrototype).replaceFirst(usernameFormatter(leaderboard.getJSONObject(0).getString("username"), 0).substring(i, i + 1));
            leaderboardPrototype = Pattern.compile("#").matcher(leaderboardPrototype).replaceFirst(usernameFormatter(leaderboard.getJSONObject(1).getString("username"), 0).substring(i, i + 1));
            if(leaderboard.length() >= 3) {
                leaderboardPrototype = Pattern.compile("-").matcher(leaderboardPrototype).replaceFirst(usernameFormatter(leaderboard.getJSONObject(2).getString("username"), 0).substring(i, i + 1));
                if(leaderboard.length() == 4)
                    leaderboardPrototype = Pattern.compile("_").matcher(leaderboardPrototype).replaceFirst(usernameFormatter(leaderboard.getJSONObject(3).getString("username"), 0).substring(i, i + 1));
            }
        }

        for(int i = 0; i < 3; i++) {
            leaderboardPrototype = Pattern.compile("\\*").matcher(leaderboardPrototype).replaceFirst(pointFormatter(leaderboard.getJSONObject(0).getInt("points")).substring(i, i + 1));
            leaderboardPrototype = Pattern.compile("@").matcher(leaderboardPrototype).replaceFirst(pointFormatter(leaderboard.getJSONObject(1).getInt("points")).substring(i, i + 1));
            if(leaderboard.length() >= 3) {
                leaderboardPrototype = Pattern.compile("%").matcher(leaderboardPrototype).replaceFirst(pointFormatter(leaderboard.getJSONObject(2).getInt("points")).substring(i, i + 1));
                if(leaderboard.length() == 4)
                    leaderboardPrototype = Pattern.compile("\\$").matcher(leaderboardPrototype).replaceFirst(pointFormatter(leaderboard.getJSONObject(3).getInt("points")).substring(i, i + 1));
            }
        }

        System.out.println(leaderboardPrototype);
    }

    private String pointFormatter(int points) {
        if(points >= 100)
            return String.valueOf(points);
        else if(points >= 10)
            return "0" + points;
        else return "00" + points;
    }

    @Override
    boolean continueScreen() {
        System.out.print("Would you like to continue playing? (y/n) ");
        String selection = scanner.nextLine();
        return selection.equals("y");
    }

    /**
     * @param username
     */
    @Override
    void showDisconnection(String username) {
        System.out.println(username + " disconnected from the game.");
    }

    /**
     *
     */
    @Override
    void showServerDisconnection() {
        System.out.println("The server disconnected.");
    }
}

/**
 * Contains the three possible CLI views that can be printed on screen, with the elements of the game substituted by
 * placeholder characters. Once the game starts, the view selects the correct prototype based on the number of players
 * and then substitutes the placeholder characters with the game data.
 * @author Mario Merlo
 */
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

    /**
     * The enum constructor
     * @param playerNumber The number of players associated to the view prototype
     * @param viewPrototype The view prototype itself
     * @author Mario Merlo
     */
    ViewPrototypes(int playerNumber, String viewPrototype) {
        this.playerNumber = playerNumber;
        this.viewPrototype = viewPrototype;
    }

    /**
     * This method returns the correct view prototype based on the number of players passed to it
     * @param playerNumber The number of players in the ongoing game
     * @return The view prototype associated to playerNumber
     * @author Mario Merlo
     */
    public static String getViewByPlayerNum(int playerNumber) {
        for(ViewPrototypes viewPrototype : ViewPrototypes.values()) {
            if(viewPrototype.playerNumber == playerNumber)
                return viewPrototype.viewPrototype;
        }
        throw new IllegalArgumentException("No such game with " + playerNumber + " players.");
    }
}

enum LeaderboardPrototypes {
    TWO_PLAYERS(2, """
            Game over! The winner is +++++++++++++++
            [1] ???????????????: ***
            [2] ###############: @@@
            """),
    THREE_PLAYERS(3, """
            Game over! The winner is +++++++++++++++
            [1] ???????????????: ***
            [2] ###############: @@@
            [3] ---------------: %%%
            """),
    FOUR_PLAYERS(4, """
            Game over! The winner is +++++++++++++++
            [1] ???????????????: ***
            [2] ###############: @@@
            [3] ---------------: %%%
            [4] _______________: $$$
            """);

    final int playerNumber;
    final String leaderboardPrototype;

    LeaderboardPrototypes(int playerNumber, String leaderboardPrototype) {
        this.playerNumber = playerNumber;
        this.leaderboardPrototype = leaderboardPrototype;
    }

    public static String getLeaderboardByPlayerNum(int playerNumber) {
        for(LeaderboardPrototypes leaderboardPrototypes : LeaderboardPrototypes.values()) {
            if(leaderboardPrototypes.playerNumber == playerNumber)
                return leaderboardPrototypes.leaderboardPrototype;
        }
        throw new IllegalArgumentException("No such game with " + playerNumber + " players.");
    }
}

/**
 * Contains short descriptions that define collective objectives. Once the game starts, the view checks what objectives
 * are currently reachable in the game and substitutes the placeholders on the view prototype with the correct
 * short descriptions.
 * @author Mario Merlo
 */
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

    /**
     * The enum constructor
     * @param description The description of a collective objective card
     * @author Mario Merlo
     */
    ObjectiveDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the description of a collective objective card
     * @return The description of the collective objective
     * @author Mario Merlo
     */
    private String getDescription() {
        return this.description;
    }

    /**
     * Compares the name of the objective passed as an argument to the name of the enum objects case insensitively and
     * returns the description of the passed objective name, if found
     * @param name The name of the objective to return the description of
     * @return The description of the passed objective
     * @author Mario Merlo
     */
    public static String getDescriptionFromName(String name) {
        for(ObjectiveDescription desc : ObjectiveDescription.values()) {
            if(desc.name().equalsIgnoreCase(name))
                return desc.getDescription();
        }
        throw new IllegalArgumentException("No such pattern name.");
    }
}