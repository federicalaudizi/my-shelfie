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

    public Game(int numOfPlayers) {
        players = new ArrayList<Player>();
        CollectiveObjectiveCards = new CollectiveObjectiveCard[2];
        pointCardDeck1 = new PointDeck(numOfPlayers);
        pointCardDeck2 = new PointDeck(numOfPlayers);
        board = new Board(numOfPlayers);
        lastTurn = false;
        firstTurn = true;
        finalScores = new int[numOfPlayers];
    }

    /**
     * This function chooses randomly the first player in the given range of players
     *
     * @param numOfPlayers represents the number of Players
     * @return first player's index
     */
    private int chooseFirstPlayer(int numOfPlayers) {
        int first;
        Random random = new Random();
        first = random.nextInt(numOfPlayers) + 1;
        return first;
    }

    /**
     * This function decides who's next turn
     *
     * @param gamer represents the player who is playing his turn

     */
    private int nextTurn(int gamer) {
        gamer = (gamer + 1) % players.size();
        return gamer;
    }


    /**
     * This function manages the play.
     * Firstly it decides who is going to start, then at each turn checks if the board needs to be repopulate,
     * let the player make his moves and checks if the player has achieved the collective objective.
     * At the end puts the points of each gamer in an array to decide who is the winner.
     */
    private void gameTurn() {
        int giocatoreDiTurno, chair, numOfPlayers;
        if (firstTurn) {
            numOfPlayers = players.size();
            chair = chooseFirstPlayer(numOfPlayers); //the first player is the one who has the chair
            firstTurn = false;
        }
        giocatoreDiTurno = chair;
        while (!lastTurn) {
            board.checkBoard();
            board.pickTile(coordinate);
            //player gives the column in which he wants to put his tiles and call addTiles
            if (CollectiveObjectiveCards[1].checkObjective(players.get(giocatoreDiTurno).getShelf())) {
                players.get(giocatoreDiTurno).assignPointCard(pointCardDeck1.takePoints());
            }
            if (CollectiveObjectiveCards[2].checkObjective(players.get(giocatoreDiTurno).getShelf())) {
                players.get(giocatoreDiTurno).assignPointCard(pointCardDeck2.takePoints());
            }
            if (players.get(giocatoreDiTurno).getShelf().isFull()) {
                lastTurn = true;
                players.get(giocatoreDiTurno).setEndGameCard();
                giocatoreDiTurno = nextTurn(giocatoreDiTurno);
                break;
            }
            giocatoreDiTurno = nextTurn(giocatoreDiTurno);

        }
        if (lastTurn) {
            while (giocatoreDiTurno <= (chair - 1)) {
                board.checkBoard();
                board.pickTile(coordinate);
                if (CollectiveObjectiveCards[1].checkObjective(players.get(giocatoreDiTurno).getShelf())) {
                    players.get(giocatoreDiTurno).assignPointCard(pointCardDeck1.takePoints());
                }
                if (CollectiveObjectiveCards[2].checkObjective(players.get(giocatoreDiTurno).getShelf())) {
                    players.get(giocatoreDiTurno).assignPointCard(pointCardDeck2.takePoints());
                }
                giocatoreDiTurno = nextTurn(giocatoreDiTurno);
            }
        }
        for (int i = 1; i <= numOfPlayers; i++) {
            finalScores[i] = players.get(i).calculatePoints();
        }

    }
}

