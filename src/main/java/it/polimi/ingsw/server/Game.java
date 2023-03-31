package it.polimi.ingsw.server;
import it.polimi.ingsw.server.exceptions.TileUnpickableException;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class manages game's initialization.
 *
 * @author Sara Massarelli
 */
class Game {
    private final ArrayList<Player> players;
    private Board board;
    private final CollectiveObjectiveCard collectiveObjectiveCard1;
    private final CollectiveObjectiveCard collectiveObjectiveCard2;
    private PointDeck pointCardDeck1;
    private PointDeck pointCardDeck2;
    private boolean lastTurn;
    private int currentPlayerIndex;
    private int firstPlayerSeat;

    Game(int numOfPlayers) throws IllegalArgumentException{
        players = new ArrayList<Player>();
        for(int i=0;i<numOfPlayers;i++){
            players.add(new Player(new PersonalObjectiveCard()));
        }
        currentPlayerIndex = 0;
        collectiveObjectiveCard1 = CollectiveObjectiveCard.getRandomCard();
        collectiveObjectiveCard2 = CollectiveObjectiveCard.getRandomCard(collectiveObjectiveCard1);
        pointCardDeck1 = new PointDeck(numOfPlayers);
        pointCardDeck2 = new PointDeck(numOfPlayers);
        try{
            board = new Board(numOfPlayers);
        }catch(IllegalArgumentException e){
            throw new IllegalArgumentException();
        }
        lastTurn = false;
        firstPlayerSeat = 0;
    }

    /**
     * This method chooses randomly the first player in the given range of players
     *
     * @param numOfPlayers represents the number of Players
     */
    void chooseFirstPlayer(int numOfPlayers) {
        Random random = new Random();
        currentPlayerIndex = random.nextInt(numOfPlayers) + 1;
        firstPlayerSeat = currentPlayerIndex; //salvo il primo giocatore per decidere chi gioca nell'ultimo turno
    }

    /**
     * This method decides who's next turn modifying the currentPlayerIndex.
     */
    void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    /**
     * Checks if the player in turn has achieved common goals and, if so, assigns them the score
     * taking the upper card in the deck.
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
     * This method manages the turn.
     * It makes the player make the move, then it checks if the player has achieved some
     * common objective and if anyone has completed his shelf.
     *
     * @param c1,c2,c3 are the coordinates of the tiles chosen by the playerInTurn
     */
     void gameTurn(Coordinate c1, Coordinate c2, Coordinate c3) throws TileUnpickableException {
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
     *This method manages the last Turn. Every player plays his last move.
     * @param c1,c2,c3 are the coordinates of the tiles chosen by the playerInTurn
     */
     void gameFinalTurn(Coordinate c1, Coordinate c2, Coordinate c3) throws TileUnpickableException{
        board.checkBoard();
        board.pickTile(c1,c2,c3);
        checkGoals();
        nextTurn();
    }

    /**
     * @return the maximum value in a given array of int.
     * @param array is the given array
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

    /**@return the winner player*/
     Player winner(){
        int results[] = new int[4];
        for(int i=0;i<players.size();i++){
            results[i]= players.get(i).calculatePoints();
        }
        return players.get(findMax(results));
    }

    int getCurrentPlayerIndex(){
         return currentPlayerIndex;
    }

    int getFirstPlayerSeat(){
         return firstPlayerSeat;
    }


}