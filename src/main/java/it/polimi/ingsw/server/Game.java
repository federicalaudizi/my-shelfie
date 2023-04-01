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
    private final Board board;
    private final CollectiveObjectiveCard collectiveObjectiveCard1;
    private final CollectiveObjectiveCard collectiveObjectiveCard2;
    private final PointDeck pointCardDeck1;
    private final PointDeck pointCardDeck2;
    private boolean lastTurn;
    private int currentPlayerIndex;
    private int lastPlayer;
    private int firstPlayerSeat;

    Game(int numOfPlayers) throws IllegalArgumentException{
        players = new ArrayList<Player>();
        for(int i=0;i<numOfPlayers;i++){
            players.add(new Player(new PersonalObjectiveCard()));
        }
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

    /**
     * This method decides who's next turn modifying the currentPlayerIndex.
     * If it is the last turn the game goes on until it reaches the last player
     */
    void nextTurn() {
        if(lastTurn && currentPlayerIndex!=lastPlayer){
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else if (!lastTurn) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }

    /**
     * Checks if the player in turn has achieved common goals and, if so, assigns them the score
     * taking the upper card in the deck.
     */
    private void checkGoals() {
        int status = players.get(currentPlayerIndex).getPointCardStatus();

        if (status == 0) {
            if (collectiveObjectiveCard1.checkObjective(players.get(currentPlayerIndex).getShelf())) {
                players.get(currentPlayerIndex).assignPointCard(pointCardDeck1.takePoints(), 0);
            }
            if (collectiveObjectiveCard2.checkObjective(players.get(currentPlayerIndex).getShelf())) {
                players.get(currentPlayerIndex).assignPointCard(pointCardDeck2.takePoints(), 1);
            }
        }else if (status == 1) {
                if (collectiveObjectiveCard2.checkObjective(players.get(currentPlayerIndex).getShelf())) {
                    players.get(currentPlayerIndex).assignPointCard(pointCardDeck2.takePoints(), 1);
                }
        } else if (status == 2) {
            if (collectiveObjectiveCard1.checkObjective(players.get(currentPlayerIndex).getShelf())) {
                players.get(currentPlayerIndex).assignPointCard(pointCardDeck1.takePoints(), 0);
            }
        }
    }



    /**
     * This method manages the turn.
     * It makes the player make the move, then it checks if the player has achieved some
     * common objective and if anyone has completed his shelf.
     * Finally, it passes the turn
     * @param c1,c2,c3 are the coordinates of the tiles chosen by the playerInTurn
     */
     void playerTurn(Coordinate c1, Coordinate c2, Coordinate c3) throws TileUnpickableException {
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
     public Player getWinner(){
        int results[] = new int[4];
        for(int i=0;i<players.size();i++){
            results[i]= players.get(i).calculatePoints();
        }
        return players.get(findMax(results));
    }

    /**@return a copy of the current player*/
    public Player getCurrentPlayer(){
         return new Player(players.get(getCurrentPlayerIndex()));
    }


    /**@return the current player index*/
    int getCurrentPlayerIndex(){
         return currentPlayerIndex;
    }

    /**@return the last player*/
    int getLastPlayer(){
         return lastPlayer;
    }

    /**@return a copy of the first player*/
    public Player getFirstPlayerSeat(){
        return new Player(players.get(firstPlayerSeat));
    }


}