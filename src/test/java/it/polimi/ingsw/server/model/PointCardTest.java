package it.polimi.ingsw.server.model;
import it.polimi.ingsw.server.model.PointCard;
import org.junit.Test;
import static junit.framework.TestCase.assertEquals;

/**Test for PointCard.
 * It checks if the Point Card is correctly initialized and if it has the correct value
 *
 * @author Sara
 * */


public class PointCardTest {


        @Test
        public void testGetValue() {
            // Create a point card with value 4
            PointCard card = new PointCard(4);

            // Check if getValue() returns the correct value
            assertEquals(4, card.getValue());

            // Create a point card with value 2
            PointCard card2 = new PointCard(2);

            // Check if getValue() returns the correct value
            assertEquals(2, card2.getValue());

            // Create a point card with negative value
            PointCard card3 = new PointCard(-2);

            // Check if getValue() returns the correct value
            assertEquals(-2, card3.getValue());
        }
    }
