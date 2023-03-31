package it.polimi.ingsw.server;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class GameTest {
    Game g2 = new Game(2);
    Game g3 = new Game(3);
    Game g4 = new Game(4);
        @Test
    public void testNextTurn(){

            //Check if nextTurn() works with two players twice
            int previousIndex = g2.getCurrentPlayerIndex();
            g2.nextTurn();
            assertEquals((previousIndex +1)%2, g2.getCurrentPlayerIndex());
            previousIndex = g2.getCurrentPlayerIndex();
            g2.nextTurn();
            assertEquals((previousIndex +1)%2, g2.getCurrentPlayerIndex());

            //Check if nextTurn() works with three players twice
             previousIndex = g3.getCurrentPlayerIndex();
            g3.nextTurn();
            assertEquals((previousIndex +1)%3, g3.getCurrentPlayerIndex() );
            previousIndex = g3.getCurrentPlayerIndex();
            g3.nextTurn();
            assertEquals((previousIndex +1)%3, g3.getCurrentPlayerIndex());

            //Check if nextTurn() works with four players twice
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
            // assigns correctly the first player seat
             g2.chooseFirstPlayer(2);
            System.out.println(g2.getCurrentPlayerIndex());
            assertTrue(g2.getCurrentPlayerIndex() >=1 && g2.getCurrentPlayerIndex() <=2);
            assertEquals(g2.getFirstPlayerSeat(), g2.getCurrentPlayerIndex());


            //Checks if chooseFirstPlayer chooses correctly the first player in a game with 3 players and if
            // assigns correctly the first player seat
            g3.chooseFirstPlayer(3);
            System.out.println(g3.getCurrentPlayerIndex());
            assertTrue(g3.getCurrentPlayerIndex() >=1 && g3.getCurrentPlayerIndex() <=3);
            assertEquals(g3.getFirstPlayerSeat(), g3.getCurrentPlayerIndex());

            //Checks if chooseFirstPlayer chooses correctly the first player in a game with 4 players and if
            // assigns correctly the first player seat
              g4.chooseFirstPlayer(4);
            System.out.println(g4.getCurrentPlayerIndex());
            assertTrue(g4.getCurrentPlayerIndex() >=1 && g4.getCurrentPlayerIndex() <=4);
            assertEquals(g4.getFirstPlayerSeat(), g4.getCurrentPlayerIndex());
        }


}
