package it.polimi.ingsw.server;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class manages game's turn
 *
 * @author Sara Massarelli
 */
public class Game {
    private ArrayList<Player> players;
    private Board board;
    private CollectiveObjectiveCard[] CollectiveObjectiveCards;
    private PointDeck pointCardDeck1;
    private PointDeck pointCardDeck2;
    private boolean lastTurn;
    private boolean firstTurn;
    private int[] finalScores;

    public Game (int numOfPlayers){
        players = new ArrayList<Player>();
        CollectiveObjectiveCards = new CollectiveObjectiveCard[2];
        pointCardDeck1 = new PointDeck(numOfPlayers);
        pointCardDeck2 = new PointDeck(numOfPlayers);
        board =new Board(numOfPlayers);
        lastTurn = false;
        firstTurn= true;
        finalScores = new int[numOfPlayers];
    }

    /**
     * This function chooses randomly the first player in the given range of players
     *
     * @param numOfPlayers represents the number of Players
     * @return first player's index
     */
    private int chooseFirstPlayer(int numOfPlayers){
        int first;
        Random random = new Random();
        first = random.nextInt(numOfPlayers + 1)+1;
        return first;
    }


    /**
     * This function chooses randomly the collective Objective Card for the game
     *
     */
    private void assignCollectiveObjectiveCard(){

    }


    /**
     * This function manages the play
     */
    private void gameTurn(){
        int giocatoreDiTurno, chair, numOfPlayers;
        if(firstTurn==true){
            assignCollectiveObjectiveCard();
            numOfPlayers = players.size();
            chair = chooseFirstPlayer(numOfPlayers);
            firstTurn = false;
        }
        giocatoreDiTurno = chair;
        while(lastTurn== false){
            board.checkBoard();
            board.pickTile(coordinate); //capire sto passaggio di coordinate????
            //giocatore deve passare coordinate e devo consentire addPlayerTiles
            if(CollectiveObjectiveCards[0].checkObjective(players.get(giocatoreDiTurno).getShelf())){
                players.get(giocatoreDiTurno).assignPointCard(pointCardDeck1.takePoints());
            }
            if(CollectiveObjectiveCards[1].checkObjective(players.get(giocatoreDiTurno).getShelf())){
                players.get(giocatoreDiTurno).assignPointCard(pointCardDeck2.takePoints());
            }
            if(players.get(giocatoreDiTurno).getShelf().isFull() == true){
                lastTurn=true;
                players.get(giocatoreDiTurno).endGameCard == true; //da mettere anche qua friendly
                break;
            }
            giocatoreDiTurno++;
            if(giocatoreDiTurno > numOfPlayers)
                giocatoreDiTurno = 1;

        }
        if(lastTurn== true){
            while(giocatoreDiTurno <= (chair-1)){
                board.checkBoard();
                board.pickTile(coordinate); // capire sto passaggio pure qua
                if(CollectiveObjectiveCards[1].checkObjective(players.get(giocatoreDiTurno).getShelf())){
                    players.get(giocatoreDiTurno).assignPointCard(pointCardDeck1.takePoints());
                }
                if(CollectiveObjectiveCards[2].checkObjective(players.get(giocatoreDiTurno).getShelf())){
                    players.get(giocatoreDiTurno).assignPointCard(pointCardDeck2.takePoints());
                }
                giocatoreDiTurno++;
                if(giocatoreDiTurno > numOfPlayers){
                    giocatoreDiTurno = 1;
                }
            }
        }
        for(int i=0;i<numOfPlayers;i++){
            finalScores[i]= players.get(i).calculatePoints();
        }

    }
}
