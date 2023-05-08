package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.PointCard;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test for PointCard.
 * It checks if the Point Card is correctly initialized and if it has the correct value
 *
 * @author Sara
 */


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

    @Test
    public void testToJson() {
        PointCard card = new PointCard(8);
        System.out.println(card.toJson());
    }

    @Test
    public void JsonConstructor(){
        PointCard p = new PointCard(2);
        JSONObject j = p.toJson();
        PointCard pp = new PointCard(j);

        System.out.println(p);
        System.out.println(p.toJson());
        System.out.println(pp);

        assertTrue(p.equals(pp));

        PointCard po = new PointCard(4);
        JSONObject jj = po.toJson();
        PointCard ppo = new PointCard(jj);

        System.out.println(po);
        System.out.println(po.toJson());
        System.out.println(ppo);

        assertTrue(po.equals(ppo));
    }
}
