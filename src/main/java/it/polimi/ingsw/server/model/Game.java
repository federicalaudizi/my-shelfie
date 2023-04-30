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
    public final ArrayList<Player> players;
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

    public Game(int numOfPlayers) throws IllegalArgumentException{
        players = new ArrayList<>(numOfPlayers);
        chooseFirstPlayer(numOfPlayers);
        collectiveObjectiveCard1 = CollectiveObjectiveCard.getRandomCard();
        assert collectiveObjectiveCard1 != null;
        collectiveObjectiveCard2 = CollectiveObjectiveCard.getRandomCard(collectiveObjectiveCard1);
        pointCardDeck1 = new PointDeck(numOfPlayers);
        pointCardDeck2 = new PointDeck(numOfPlayers);
        try{
            board = new Board(numOfPlayers);
        }catch(IllegalArgumentException e){
            throw new IllegalArgumentException();
        }
        lastTurn = false;
        isOver = false;
    }

    /**
     * This method chooses randomly the first player in the given range of players, sets the first and
     * the last player
     * @param numOfPlayers represents the number of Players
     */
     private void chooseFirstPlayer(int numOfPlayers) {
        Random random = new Random();
        currentPlayerIndex = random.nextInt(numOfPlayers) + 1;
        firstPlayerSeat = currentPlayerIndex;
        lastPlayer = firstPlayerSeat - 1;
        if(lastPlayer == 0){
            lastPlayer=numOfPlayers;
        }
    }

    public void setUsernames(ArrayList<String> usernames){
         for(int i=0; i< players.size();i++){
            players.get(i).setPlayerName(usernames.get(i));
         }
    }


    /**
     * This method manages the turn modifying the current player index.
     * */
    public void nextTurn(){
        if(lastTurn && currentPlayerIndex!=lastPlayer){
            currentPlayerIndex = ((currentPlayerIndex+1)% players.size());
        } else if (!lastTurn) {
            currentPlayerIndex = ((currentPlayerIndex+1)% players.size());
        }
        else{
            isOver= true;
        }
    }


    /**
     * Checks if the player in turn has achieved common goals and, if so, assigns them the score
     * taking the upper card in the deck.
     *
     * @return the deck's number of the achieved Objective
     */
    private int checkGoals() {
        int status = players.get(currentPlayerIndex).getPointCardStatus();

        if (status == 0) {
            if (collectiveObjectiveCard1.checkObjective(players.get(currentPlayerIndex).getShelf())) {
                players.get(currentPlayerIndex).assignPointCard(pointCardDeck1.takePoints(), 0);
                return 1;
            }
            if (collectiveObjectiveCard2.checkObjective(players.get(currentPlayerIndex).getShelf())) {
                players.get(currentPlayerIndex).assignPointCard(pointCardDeck2.takePoints(), 1);
                return 2;
            }
        }else if (status == 1) {
                if (collectiveObjectiveCard2.checkObjective(players.get(currentPlayerIndex).getShelf())) {
                    players.get(currentPlayerIndex).assignPointCard(pointCardDeck2.takePoints(), 1);
                    return 2;
                }
        } else if (status == 2) {
            if (collectiveObjectiveCard1.checkObjective(players.get(currentPlayerIndex).getShelf())) {
                players.get(currentPlayerIndex).assignPointCard(pointCardDeck1.takePoints(), 0);
                return 1;
            }
        }
            return 0;
    }


    /**
     * This method checks if the board needs to be repopulate and removes the chosen tiles
     * from the board
     *
     * @return an array with the chosen tiles
     * @param c1,c2,c3 are the coordinates of the tiles chosen by the playerInTurn
     */
     public Tile[] chooseTiles(Coordinate c1, Coordinate c2, Coordinate c3) throws TileUnpickableException {
        board.checkBoard();
        return board.pickTile(c1,c2,c3);
    }


    /**
     * This method handles the insertion of tiles into the shelf.
     * The player is allowed to insert their tiles into a selected column of the shelf.
     * Subsequently, the method checks if this insertion enables the player to achieve some shared objectives.
     * Additionally, the method verifies if the player's shelf has become full, and if so, it sets
     * the player's turn as the last one.
     * @param column of the shelf where to place the tiles
     * @param tiles to place in the shelf
     * @return 0 if none collective objective has been achieved with the move, 1 if has been achieved
     * the first collective objective, 2 if has been achieved the second collective objective
     * */
    public int insertInShelf(int column, Tile[] tiles) throws tooManyTilesException, notEnoughTilesException, fullColumnException {
        players.get(currentPlayerIndex).addPlayerTiles(column, tiles);
        if (players.get(currentPlayerIndex).getShelf().isFull()) {
            lastTurn = true;
            players.get(currentPlayerIndex).setEndGameCard();
        }
        return checkGoals();
    }

    public ArrayList<String> getPlayerId (){
        ArrayList<String> playersId = new ArrayList<>();
        for (Player player : players){
            playersId.add(player.getUsername());
        }
        return playersId;
    }
    /**
     * @return a HashMap with the player as key and the player's score
     * as value, sorted in descending order based on the player's score.*/
    public Map<Player, Integer> getRankedPlayers() {
        Map<Player, Integer> playerScoreMap = new HashMap<>();
        for (Player player : players) {
            int score = player.calculatePoints();
            playerScoreMap.put(player, score);
        }
        Map<Player, Integer> sortedPlayerScoreMap = new LinkedHashMap<>();
        playerScoreMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(entry -> sortedPlayerScoreMap.put(entry.getKey(), entry.getValue()));
        return sortedPlayerScoreMap;
    }



    /**@return a copy of the current player*/
    public Player getCurrentPlayer(){
         return new Player(players.get(getCurrentPlayerIndex()));
    }


    /**@return the current player index*/
    public int getCurrentPlayerIndex(){
         return currentPlayerIndex;
    }


    /**
     * setter of player index*/
    public void setCurrentPlayerIndex(int newIndex){
        currentPlayerIndex = newIndex;
    }

    /**@return the last player*/
    public int getLastPlayer(){
         return lastPlayer;
    }

    /**@return a copy of the first player*/
    public Player getFirstPlayerSeat(){
        return new Player(players.get(firstPlayerSeat));
    }

    /**
     * getter for the last Turn
     * @return if the turn is the last one*/
    public boolean isLastTurn(){
        return lastTurn;
    }

    /**
     * @return the number of players in the game*/
    public int getNumberOfPlayers(){
        return players.size();
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray playersJson = new JSONArray();
        for (Player player : players) {
            playersJson.put(player.toJson());
        }
        json.put("players", playersJson);
        json.put("board", board.toJSON());
        json.put("collectiveObjectiveCard1", collectiveObjectiveCard1.toJson());
        json.put("collectiveObjectiveCard2", collectiveObjectiveCard2.toJson());
        json.put("pointCardDeck1", pointCardDeck1.toJson());
        json.put("pointCardDeck2", pointCardDeck2.toJson());
        json.put("lastTurn", lastTurn);
        json.put("currentPlayerIndex", currentPlayerIndex);
        json.put("lastPlayer", lastPlayer);
        json.put("firstPlayerSeat", firstPlayerSeat);
        json.put("isOver", isOver);
        return json;
    }

}
