package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.Game;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
/**Test for Game
 * @author Sara
 * */
public class GameTest {
    Game g2 = new Game(2);
    Game g3 = new Game(3);
    Game g4 = new Game(4);
        @Test
    public void testNextTurn(){

            //Check if nextTurn() works with two players
            int previousIndex = g2.getCurrentPlayerIndex();
            g2.nextTurn();
            assertEquals((previousIndex +1)%2, g2.getCurrentPlayerIndex());
            previousIndex = g2.getCurrentPlayerIndex();
            g2.nextTurn();
            assertEquals((previousIndex +1)%2, g2.getCurrentPlayerIndex());
            previousIndex = g2.getCurrentPlayerIndex();
            g2.nextTurn();
            assertEquals((previousIndex +1)%2, g2.getCurrentPlayerIndex());
            previousIndex = g2.getCurrentPlayerIndex();
            g2.nextTurn();
            assertEquals((previousIndex +1)%2, g2.getCurrentPlayerIndex());

            //Check if nextTurn() works with three players
             previousIndex = g3.getCurrentPlayerIndex();
            g3.nextTurn();
            assertEquals((previousIndex +1)%3, g3.getCurrentPlayerIndex() );
            previousIndex = g3.getCurrentPlayerIndex();
            g3.nextTurn();
            assertEquals((previousIndex +1)%3, g3.getCurrentPlayerIndex());
            previousIndex = g3.getCurrentPlayerIndex();
            g3.nextTurn();
            assertEquals((previousIndex +1)%3, g3.getCurrentPlayerIndex());
            previousIndex = g3.getCurrentPlayerIndex();
            g3.nextTurn();
            assertEquals((previousIndex +1)%3, g3.getCurrentPlayerIndex());

            //Check if nextTurn() works with four players
            previousIndex = g4.getCurrentPlayerIndex();
            g4.nextTurn();
            assertEquals((previousIndex +1)%4, g4.getCurrentPlayerIndex() );
            previousIndex = g4.getCurrentPlayerIndex();
            g4.nextTurn();
            assertEquals((previousIndex +1)%4, g4.getCurrentPlayerIndex() );
            previousIndex = g4.getCurrentPlayerIndex();
            g4.nextTurn();
            assertEquals((previousIndex +1)%4, g4.getCurrentPlayerIndex() );
            previousIndex = g4.getCurrentPlayerIndex();
            g4.nextTurn();
            assertEquals((previousIndex +1)%4, g4.getCurrentPlayerIndex() );
            previousIndex = g4.getCurrentPlayerIndex();
            g4.nextTurn();
            assertEquals((previousIndex +1)%4, g4.getCurrentPlayerIndex() );
        }

        @Test
        public void testChooseFirstPlayer(){

            //Checks if chooseFirstPlayer chooses correctly the first player in a game with 2 players and if
            // assigns correctly the first and the last player
            System.out.println("First Player:" +g2.getFirstPlayerSeat());
            assertTrue(g2.getCurrentPlayerIndex() >=1 && g2.getCurrentPlayerIndex() <=2);
            System.out.println("Last Player:" + g2.getLastPlayer());
            assertTrue(g2.getLastPlayer() >=1 && g2.getLastPlayer() <=2);


            //Checks if chooseFirstPlayer chooses correctly the first player in a game with 3 players and if
            // assigns correctly the first and the last player
            System.out.println("First Player:" + g3.getFirstPlayerSeat());
            assertTrue(g3.getCurrentPlayerIndex() >=1 && g3.getCurrentPlayerIndex() <=3);
            System.out.println("Last Player:" + g3.getLastPlayer());
            assertTrue(g3.getLastPlayer() >=1 && g3.getLastPlayer() <=3);

            //Checks if chooseFirstPlayer chooses correctly the first player in a game with 4 players and if
            // assigns correctly the first and the last player
            System.out.println("First Player:" +g4.getFirstPlayerSeat());
            assertTrue(g4.getCurrentPlayerIndex() >=1 && g4.getCurrentPlayerIndex() <=4);
            System.out.println("Last Player:" + g4.getLastPlayer());
            assertTrue(g4.getLastPlayer() >=1 && g4.getLastPlayer() <=4);
        }


}
