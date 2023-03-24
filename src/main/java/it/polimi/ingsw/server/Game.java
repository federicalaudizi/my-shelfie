package it.polimi.ingsw.server;
import java.util.ArrayList;
import java.util.LinkedList;
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

    public Game (int numOfPlayers){
        players = new ArrayList<Player>();
        players.set(players.size()-1, players.get(0)); // imposto lista circolare
        CollectiveObjectiveCards = new CollectiveObjectiveCard[2];
        pointCardDeck1 = new PointDeck(numOfPlayers);
        pointCardDeck2 = new PointDeck(numOfPlayers);
        lastTurn = false;
        firstTurn= true;
    }

    /**
     * This function chooses randomly the first player in the given range of players
     *
     * @param numOfPlayers represents the number of Players
     */
    private int chooseFirstPlayer(int numOfPlayers){
        int primo;
        Random random = new Random();
        primo = random.nextInt(numOfPlayers + 1)+1;
        return primo;
    }


    //This function chooses two collective objective cards for the game
    private void assignCollectiveObjectiveCard(){

    }


    //Manages each turn
    private void gameTurn(){
        int giocatoreDiTurno, chair, numOfPlayers;
        if(firstTurn==true){
            /* TODO : assegnare obiettivi comuni casualmente*/
            numOfPlayers = players.size();
            chair = chooseFirstPlayer(numOfPlayers);
            firstTurn = false;
        }
        giocatoreDiTurno = chair;
        while(lastTurn== false){
            board.checkBoard();
            board.pickTile(coordinate); //capire sto passaggio di coordinate????
            //giocatore deve passare coordinate e devo consentire addPlayerTiles
            //check obiettivo raggiunto e assegnamento carta punto
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
                //check obiettivo comune raggiunto
                giocatoreDiTurno++;
                if(giocatoreDiTurno > numOfPlayers){
                    giocatoreDiTurno = 1;
                }
            }
        }

    }


}
