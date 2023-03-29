package it.polimi.ingsw.server;

import it.polimi.ingsw.server.exceptions.fullColumnException;
import it.polimi.ingsw.server.exceptions.notEnoughTilesException;
import it.polimi.ingsw.server.exceptions.tooManyTilesException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PersonalObjectiveCardTest {
    @Test
    public void toStringTest() {
        PersonalObjectiveCard card = new PersonalObjectiveCard();

        System.out.println(card);
    }

    @Test
    public void checkObjectectiveTest() throws tooManyTilesException, notEnoughTilesException, fullColumnException {
        for(int i = 0; i < 200; i++) {
            PersonalObjectiveCard card;
            try {
                card = new PersonalObjectiveCard();
            } catch (Exception e) {
                assertTrue("All the cards have been tested", true);
                break;
            }
            Shelf shelf = new Shelf();

            shelf.addTiles(0, new Tile[]{Tile.PIANTE});
            shelf.addTiles(1, new Tile[]{Tile.GATTI, Tile.GIOCHI});
            shelf.addTiles(2, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
            shelf.addTiles(2, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.TROFEI});
            shelf.addTiles(3, new Tile[]{Tile.GATTI, Tile.CORNICI, Tile.GATTI});
            shelf.addTiles(3, new Tile[]{Tile.LIBRI});
            shelf.addTiles(4, new Tile[]{Tile.LIBRI, Tile.GATTI, Tile.GATTI});
            shelf.addTiles(4, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});

            System.out.println("Test " + i);
            System.out.println(card);
            System.out.println(shelf);
            System.out.println("Earned points: " + card.checkObjective(shelf));
            System.out.println();

            assertTrue(!(card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.FIRST_PATTERN) || 12 == card.checkObjective(shelf));
            assertTrue(!(card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.SECOND_PATTERN) || 1 == card.checkObjective(shelf));
            assertTrue(!(card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.THIRD_PATTERN) || 0 == card.checkObjective(shelf));
            assertTrue(!(card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.FOURTH_PATTERN) || 1 == card.checkObjective(shelf));
            assertTrue(!(card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.FIFTH_PATTERN) || 1 == card.checkObjective(shelf));
            assertTrue(!(card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.SIXTH_PATTERN) || 1 == card.checkObjective(shelf));
            assertTrue(!(card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.SEVENTH_PATTERN) || 2 == card.checkObjective(shelf));
            assertTrue(!(card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.EIGHTH_PATTERN) || 0 == card.checkObjective(shelf));
            assertTrue(!(card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.NINTH_PATTERN) || 0 == card.checkObjective(shelf));
            assertTrue(!(card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.TENTH_PATTERN) || 1 == card.checkObjective(shelf));
            assertTrue(!(card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.ELEVENTH_PATTERN) || 0 == card.checkObjective(shelf));
            assertTrue(!(card.getPattern() == PersonalObjectiveCard.PersonalObjectivePattern.TWELFTH_PATTERN) || 0 == card.checkObjective(shelf));
        }

        assertTrue("All tests passed", true);
    }
}