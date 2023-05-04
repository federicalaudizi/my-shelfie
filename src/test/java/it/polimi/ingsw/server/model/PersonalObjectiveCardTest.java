package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.exceptions.fullColumnException;
import it.polimi.ingsw.server.exceptions.notEnoughTilesException;
import it.polimi.ingsw.server.exceptions.tooManyTilesException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PersonalObjectiveCardTest {
    Game g;
    @Test
    public void toStringTest() {
        PersonalObjectiveCard card = new PersonalObjectiveCard(g);
        System.out.println(card);
    }

    @Test
    public void checkObjectectiveTest() throws tooManyTilesException, notEnoughTilesException, fullColumnException {
        for(int i = 0; i < 200; i++) {
            PersonalObjectiveCard card;
            try {
                card = new PersonalObjectiveCard(g);
            } catch (Exception e) {
                assertTrue("All the cards have been tested", true);
                break;
            }
            Shelf shelf = new Shelf();

            shelf.addTiles(0, new Tile[]{Tile.PLANTS});
            shelf.addTiles(1, new Tile[]{Tile.CATS, Tile.GAMES});
            shelf.addTiles(2, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
            shelf.addTiles(2, new Tile[]{Tile.CATS, Tile.CATS, Tile.TROPHIES});
            shelf.addTiles(3, new Tile[]{Tile.CATS, Tile.FRAMES, Tile.CATS});
            shelf.addTiles(3, new Tile[]{Tile.BOOKS});
            shelf.addTiles(4, new Tile[]{Tile.BOOKS, Tile.CATS, Tile.CATS});
            shelf.addTiles(4, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});

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