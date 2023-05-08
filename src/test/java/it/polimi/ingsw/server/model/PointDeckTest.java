package it.polimi.ingsw.server.model;


import org.json.JSONObject;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/** This test checks that the constructor creates the correct number of  Point cards based on the
 * number of players and that the method takePoints() returns the correct card and reduce the stack size.
 *
 * @author Sara
 * */

public class PointDeckTest {

        @Test
        public void testTakePoints() {
            // Create a deck for 2 players
            PointDeck deck1 = new PointDeck(2);
            assertEquals(2, deck1.cards.size());

            // Check if takePoints() returns the correct card for a 2-player game
            PointCard card1 = deck1.takePoints();
            assertEquals(8, card1.getValue());
            assertEquals(1, deck1.cards.size());

            PointCard card2 = deck1.takePoints();
            assertEquals(4, card2.getValue());
            assertEquals(0, deck1.cards.size());

            // Create a deck for 3 players
            PointDeck deck2 = new PointDeck(3);
            assertEquals(3, deck2.cards.size());

            // Check if takePoints() returns the correct card for a 3-player game
            PointCard card3 = deck2.takePoints();
            assertEquals(8, card3.getValue());
            assertEquals(2, deck2.cards.size());

            PointCard card4 = deck2.takePoints();
            assertEquals(6, card4.getValue());
            assertEquals(1, deck2.cards.size());

            PointCard card5 = deck2.takePoints();
            assertEquals(4, card5.getValue());
            assertEquals(0, deck2.cards.size());

            // Create a deck for 4 players
            PointDeck deck3 = new PointDeck(4);
            assertEquals(4, deck3.cards.size());

            // Check if takePoints() returns the correct card for a 4-player game
            PointCard card6 = deck3.takePoints();
            assertEquals(8, card6.getValue());
            assertEquals(3, deck3.cards.size());

            PointCard card7 = deck3.takePoints();
            assertEquals(6, card7.getValue());
            assertEquals(2, deck3.cards.size());

            PointCard card8 = deck3.takePoints();
            assertEquals(4, card8.getValue());
            assertEquals(1, deck3.cards.size());

            PointCard card9 = deck3.takePoints();
            assertEquals(2, card9.getValue());
            assertEquals(0, deck3.cards.size());
        }

        @Test
        public void toJsonTest(){
            PointDeck p = new PointDeck(2);
            System.out.println(p.toJson());

            PointDeck pp = new PointDeck(3);
            System.out.println(pp.toJson());

            PointDeck ppp = new PointDeck(4);
            System.out.println(ppp.toJson());
        }

        @Test
        public void JsonConstructorTest(){
            PointDeck p = new PointDeck(4);
            JSONObject j = p.toJson();
            PointDeck pp = new PointDeck(j);

            System.out.println(p);
            System.out.println(p.toJson());
            System.out.println(pp);


            assertTrue(p.isEqualTo(pp));
        }

        @Test
        public void isEqualToTest(){
            PointDeck p = new PointDeck(2);
            PointDeck pp = new PointDeck(2);
            assertTrue(p.isEqualTo(pp));

            p.takePoints();
            assertTrue(!p.isEqualTo(pp));
            pp.takePoints();
            assertTrue(p.isEqualTo(pp));
        }
    }


