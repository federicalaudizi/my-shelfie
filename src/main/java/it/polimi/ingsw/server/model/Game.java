package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.exceptions.TileUnpickableException;
import it.polimi.ingsw.server.exceptions.fullColumnException;
import it.polimi.ingsw.server.exceptions.notEnoughTilesException;
import it.polimi.ingsw.server.exceptions.tooManyTilesException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * This class manages game's initialization.
 *
 * @author Sara Massarelli
 */
public class Game {
    // TODO: there might be an error when sending/recreating to/from json of the point decks
    private final ArrayList<Player> players;
    private final Board board;
    private final CollectiveObjectiveCard collectiveObjectiveCard1;
    private final CollectiveObjectiveCard collectiveObjectiveCard2;
    private final PointDeck pointCardDeck1;
    private final PointDeck pointCardDeck2;
    private boolean lastTurn;
    private int currentPlayerIndex;
    private int lastPlayer;
    private int firstPlayerSeat;
    private boolean isOver;

    public Game(int numOfPlayers) throws IllegalArgumentException {
        this.players = new ArrayList<>();

        for (int i = 0; i < numOfPlayers; i++) {
            players.add(new Player(new PersonalObjectiveCard(this)));
        }

        chooseFirstPlayer(numOfPlayers);

        this.collectiveObjectiveCard1 = CollectiveObjectiveCard.getRandomCard();
        assert collectiveObjectiveCard1 != null;
        this.collectiveObjectiveCard2 = CollectiveObjectiveCard.getRandomCard(collectiveObjectiveCard1);

        this.pointCardDeck1 = new PointDeck(numOfPlayers);
        this.pointCardDeck2 = new PointDeck(numOfPlayers);

        this.board = new Board(numOfPlayers);

        this.lastTurn = false;
        this.isOver = false;
    }

    /**
     * Json constructor for game
     * @param jsonObject is the json Object containing the game
     * */
    public Game(JSONObject jsonObject) {
        this.board = new Board(jsonObject.getJSONObject("board"));

        this.players = new ArrayList<>();
        JSONArray playersArray = jsonObject.getJSONArray("players");
        for (int i = 0; i < playersArray.length(); i++) {
            JSONObject playerJson = playersArray.getJSONObject(i);
            Player player = new Player(playerJson);
            this.players.add(player);
        }

        JSONArray decksArray = jsonObject.getJSONArray("pointDecks");
        this.pointCardDeck1 = new PointDeck(decksArray.getJSONObject(0));
        this.pointCardDeck2 = new PointDeck(decksArray.getJSONObject(1));


        JSONArray objectivesArray = jsonObject.getJSONArray("objectives");
        this.collectiveObjectiveCard1 = CollectiveObjectiveCard.fromJson(objectivesArray.getJSONObject(0));
        this.collectiveObjectiveCard2 = CollectiveObjectiveCard.fromJson(objectivesArray.getJSONObject(1));

        this.lastTurn = jsonObject.getBoolean("lastTurn");

    }

    /**
     * Copy constructor
     */
    public Game(Game other, CollectiveObjectiveCard collectiveObjectiveCard1, CollectiveObjectiveCard collectiveObjectiveCard2) {
        this.collectiveObjectiveCard1 = collectiveObjectiveCard1;
        this.collectiveObjectiveCard2 = collectiveObjectiveCard2;
        this.players = new ArrayList<>();

        for (Player player : other.players) {
            this.players.add(new Player(player));
        }

        this.board = new Board(other.board);
        this.pointCardDeck1 = new PointDeck(other.pointCardDeck1);
        this.pointCardDeck2 = new PointDeck(other.pointCardDeck2);

        this.lastTurn = other.lastTurn;
        this.currentPlayerIndex = other.currentPlayerIndex;
        this.lastPlayer = other.lastPlayer;
        this.firstPlayerSeat = other.firstPlayerSeat;
        this.isOver = other.isOver;
    }

    /**
     * This method chooses randomly the first player in the given range of players, sets the first and
     * the last player
     *
     * @param numOfPlayers represents the number of Players
     */
    private void chooseFirstPlayer(int numOfPlayers) {
        Random random = new Random();
        currentPlayerIndex = random.nextInt(numOfPlayers);
        firstPlayerSeat = currentPlayerIndex;
        lastPlayer = firstPlayerSeat - 1;
        if (lastPlayer == -1) {
            lastPlayer = numOfPlayers - 1;
        }
    }

    /**
     * sets the player's usernames
     */
    public void setUsernames(ArrayList<String> usernames) {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setPlayerName(usernames.get(i));
        }
    }

    /**
     * This method manages the turn modifying the current player index.
     */
    public boolean nextTurn() {
        board.checkBoard();
        if (lastTurn && currentPlayerIndex != lastPlayer) {
            currentPlayerIndex = ((currentPlayerIndex + 1) % players.size());
        } else if (!lastTurn) {
            currentPlayerIndex = ((currentPlayerIndex + 1) % players.size());
        } else {
            isOver = true;
        }
        return isOver;
    }

    /**
     * Checks if the player in turn has achieved common goals and, if so, assigns them the score
     * taking the upper card in the deck.
     *
     * @return 0 if the player has not achieved any goal, 1 if the player has achieved the first goal, 2 if the player has achieved the second goal, 3 if the player has achieved both goals
     */
    public int checkGoals() {
        int status = players.get(currentPlayerIndex).getPointCardStatus();
        int returnStatus = 0;

        if (status == 0) {
            // Player has no point cards
            if (collectiveObjectiveCard1.checkObjective(players.get(currentPlayerIndex).getShelf())) {
                // Player has achieved the first collective objective
                players.get(currentPlayerIndex).assignPointCard(pointCardDeck1.takePoints(), 0);
                returnStatus += 1;
            }
            if (collectiveObjectiveCard2.checkObjective(players.get(currentPlayerIndex).getShelf())) {
                // Player has achieved the second collective objective
                players.get(currentPlayerIndex).assignPointCard(pointCardDeck2.takePoints(), 1);
                returnStatus += 2;
            }
        } else if (status == 1) {
            // Player has a point card from the first deck
            if (collectiveObjectiveCard2.checkObjective(players.get(currentPlayerIndex).getShelf())) {
                // Player has achieved the second collective objective
                players.get(currentPlayerIndex).assignPointCard(pointCardDeck2.takePoints(), 1);
                returnStatus += 2;
            }
        } else if (status == 2) {
            // Player has a point card from the second deck
            if (collectiveObjectiveCard1.checkObjective(players.get(currentPlayerIndex).getShelf())) {
                // Player has achieved the first collective objective
                players.get(currentPlayerIndex).assignPointCard(pointCardDeck1.takePoints(), 0);
                returnStatus += 1;
            }
        }

        return returnStatus;
    }

    /**
     * Ensures that a tile is a pickable
     * @param c the coordinate of the tile
     * @return true if pickable, false otherwise
     * @author Federico
     */
    public boolean isPickable(Coordinate c){
        try{
            return board.isPickable(c);
        } catch (IllegalArgumentException e){
            return false;
        }
    }

    /**
     * This method handles the insertion of tiles into the shelf.
     * The player is allowed to insert their tiles into a selected column of the shelf.
     * Subsequently, the method checks if this insertion enables the player to achieve some shared objectives.
     * Additionally, the method verifies if the player's shelf has become full, and if so, it sets
     * the player's turn as the last one.
     * All tiles are expected to be checked for pickability first.
     *
     * @param column of the shelf where to place the tiles
     * @param c1 first coordinate of the tile, must be pickable
     * @param c2 second coordinate of the tile, must be pickable
     * @param c3 third coordinate of the tile, must be pickable
     */
    public void makeMove(int column, Coordinate c1, Coordinate c2, Coordinate c3) throws tooManyTilesException, notEnoughTilesException, fullColumnException {
        // Executing all checks to be sure that the move is legal
        int selectedTiles = 0;
        if(c1 != null) selectedTiles++;
        if(c2 != null) selectedTiles++;
        if(c3 != null) selectedTiles++;
        if(selectedTiles == 0) throw new notEnoughTilesException();
        if(players.get(currentPlayerIndex).getShelf().availableSlots(column) == 0) throw new fullColumnException();
        else if(players.get(currentPlayerIndex).getShelf().availableSlots(column) < selectedTiles) throw new tooManyTilesException();

        // This instruction is now atomic, all checks are executed before taking the tiles out of the board
        try {
            Tile[] tiles = board.pickTile(c1, c2, c3);
            players.get(currentPlayerIndex).addPlayerTiles(column, tiles);
        } catch (TileUnpickableException ignored) {
        }

        if (players.get(currentPlayerIndex).getShelf().isFull()) {
            lastTurn = true;
            players.get(currentPlayerIndex).setEndGameCard();
        }
    }

    /**
     * This method checks if the board needs to be repopulated and removes the chosen tiles
     * from the board
     *
     * @param c1,c2,c3 are the coordinates of the tiles chosen by the playerInTurn
     * @return an array with the chosen tiles
     */
    @Deprecated
    public Tile[] chooseTiles(Coordinate c1, Coordinate c2, Coordinate c3) throws TileUnpickableException {
        return board.pickTile(c1, c2, c3);
    }

    /**
     * @return a HashMap with the player as key and the player's score
     * as value, sorted in descending order based on the player's score.
     */
    public HashMap<String, Integer> getRankedPlayers() {
        HashMap<String, Integer> playerScoreMap = new HashMap<>();
        for (Player player : players) {
            int score = player.calculatePoints();
            playerScoreMap.put(player.getUsername(), score);
        }
        HashMap<String, Integer> sortedPlayerScoreMap = new LinkedHashMap<>();
        playerScoreMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(entry -> sortedPlayerScoreMap.put(entry.getKey(), entry.getValue()));
        return sortedPlayerScoreMap;
    }

    /**
     * @return the current player index
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * getter for the last Turn
     *
     * @return if the turn is the last one
     */
    public boolean isLastTurn() {
        return lastTurn;
    }

    /**
     * @return the number of players in the game
     */
    public int getNumberOfPlayers() {
        return players.size();
    }

    /**
     * @return the board of the game
     */
    public Board getBoard() {
        return board.copy();
    }

    /**
     * @return array with the maximum disposable points in the decks
     */
    public int[] getPointsValue() {
        return new int[]{pointCardDeck1.topValue(), pointCardDeck2.topValue()};
    }

    /**
     * @return the index of the first player
     */
    public int getFirst() {
        return firstPlayerSeat;
    }

    /**
     * Getter for the players in game
     *
     * @return an array list of players in game
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Returns the player whose username corresponds to the one passed to the method
     *
     * @param username The username of the player to return
     * @return The specified player
     * @throws IllegalArgumentException If there is no player with the passed username, this exception is thrown
     * @author Mario Merlo
     */
    public Player getPlayerByUsername(String username) throws IllegalArgumentException {
        for (Player player : players) {
            if (player.getUsername().equals(username))
                return player;
        }
        throw new IllegalArgumentException("No such player exists.");
    }

    /**
     * Returns an array with the names of the objectives associated to the current game.
     *
     * @return A String array containing the names of the objective in order
     * @author Mario Merlo
     */
    public String[] getObjectives() {
        return new String[]{collectiveObjectiveCard1.getClass().getSimpleName(), collectiveObjectiveCard2.getClass().getSimpleName()};
    }

    public Player getCurrentPlayer() {
        return new Player(players.get(getCurrentPlayerIndex()));
    }

    /**
     * @return game to json object
     * @author Federica, Federico
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        //Inserting the board
        json.put("board", board.toJSON());

        //Inserting the players
        JSONArray JSONArrayPlayers = new JSONArray();
        for (Player player : players) {
            JSONArrayPlayers.put(player.toJson());
        }
        json.put("players", JSONArrayPlayers);

        //Inserting the objectives
        JSONArray JSONArrayObjectives = new JSONArray();
        JSONArrayObjectives.put(collectiveObjectiveCard1.toJson());
        JSONArrayObjectives.put(collectiveObjectiveCard2.toJson());
        json.put("objectives", JSONArrayObjectives);

        //Inserting the decks
        JSONArray JSONArrayDecks = new JSONArray();
        JSONArrayDecks.put(pointCardDeck1.toJson());
        JSONArrayDecks.put(pointCardDeck2.toJson());
        json.put("pointDecks", JSONArrayDecks);

        //Inserting the "isOver" flag
        json.put("lastTurn", lastTurn);

        return json;
    }
}
