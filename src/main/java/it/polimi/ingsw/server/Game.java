package it.polimi.ingsw.server;
import it.polimi.ingsw.server.exceptions.TileUnpickableException;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class manages game's turn
 *
 * @author Sara Massarelli
 */
public class Game {
    private final ArrayList<Player> players;
    private Board board;
    private final CollectiveObjectiveCard collectiveObjectiveCard1;
    private final CollectiveObjectiveCard collectiveObjectiveCard2;
    private PointDeck pointCardDeck1;
    private PointDeck pointCardDeck2;
    private boolean lastTurn;
    private int currentPlayerIndex;

    public Game(int numOfPlayers) {
        players = new ArrayList<Player>();
        for(int i=0;i<numOfPlayers;i++){
            players.add(new Player(new PersonalObjectiveCard()));
        }
        currentPlayerIndex = 0;
        collectiveObjectiveCard1 = CollectiveObjectiveCard.getRandomCard();
        collectiveObjectiveCard2 = CollectiveObjectiveCard.getRandomCard(collectiveObjectiveCard1);
        pointCardDeck1 = new PointDeck(numOfPlayers);
        pointCardDeck2 = new PointDeck(numOfPlayers);
        board = new Board(numOfPlayers);
        lastTurn = false;
    }

    /**
     * This function chooses randomly the first player in the given range of players
     *
     * @param numOfPlayers represents the number of Players
     */
    private void chooseFirstPlayer(int numOfPlayers) {
        Random random = new Random();
        currentPlayerIndex = random.nextInt(numOfPlayers) + 1;
        //dovrò salvare da qualche parte chi scelgo per primo per definire ultimo giro
    }

    /**
     * This function decides who's next turn modifying the currentPlayerIndex.
     */
    private void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    /**
     * Checks if the player in turn has achieved common goals and, if so, assigns them the score.
     */
    private void checkGoals() {
        if (collectiveObjectiveCard1.checkObjective(players.get(currentPlayerIndex).getShelf())) {
            players.get(currentPlayerIndex).assignPointCard(pointCardDeck1.takePoints());
        }
        if (collectiveObjectiveCard2.checkObjective(players.get(currentPlayerIndex).getShelf())) {
            players.get(currentPlayerIndex).assignPointCard(pointCardDeck2.takePoints());
        }
    }


    /**
     * This function manages the turn.
     * It makes the player make the move, then it checks if the player has achieved some
     * common objective and if anyone has completed his shelf.
     *
     * @param c1,c2,c3 are the coordinates of the tiles chosen by the playerInTurn
     */
    private void gameTurn(Coordinate c1, Coordinate c2, Coordinate c3) throws TileUnpickableException {
        board.checkBoard();
        board.pickTile(c1,c2,c3);
        checkGoals();
            if (players.get(currentPlayerIndex).getShelf().isFull()) {
                lastTurn = true;
                players.get(currentPlayerIndex).setEndGameCard();
            }
            nextTurn();
    }

    /**
     *This Function manages the last Turn. Every player plays his last move.
     * @param c1,c2,c3 are the coordinates of the tiles chosen by the playerInTurn
     */
    private void gameFinalTurn(Coordinate c1, Coordinate c2, Coordinate c3) throws TileUnpickableException{
        board.checkBoard();
        board.pickTile(c1,c2,c3);
        checkGoals();
        nextTurn();
    }

    /**
     * Finds the maximum in a given array of int
     * @param array
     */
    private int findMax(int array[]){
        int max = array[0];
        for(int i=0;i< array.length;i++){
            if(array[i]>max){
                max= array[i];
            }
        }
        return max;
    }

    /**
     *
     * @return the winner player
     */
    private Player winner(){
        int results[] = new int[4];
        for(int i=0;i<players.size();i++){
            results[i]= players.get(i).calculatePoints();
        }
        return players.get(findMax(results));
    }
}