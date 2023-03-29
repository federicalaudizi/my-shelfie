package it.polimi.ingsw.server;

import it.polimi.ingsw.server.exceptions.fullColumnException;
import it.polimi.ingsw.server.exceptions.notEnoughTilesException;
import it.polimi.ingsw.server.exceptions.tooManyTilesException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class PersonalObjectiveCardTest {
    @Test
    public void toStringTest() throws tooManyTilesException, notEnoughTilesException, fullColumnException {
        PersonalObjectiveCard card = new PersonalObjectiveCard();

        System.out.println(card.toString());
    }

    @Test
    public void checkObjectectiveTest() throws tooManyTilesException, notEnoughTilesException, fullColumnException {
        PersonalObjectiveCard card = new PersonalObjectiveCard();
        Shelf shelf = new Shelf();

        shelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        shelf.addTiles(1, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        shelf.addTiles(2, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        shelf.addTiles(3, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        shelf.addTiles(4, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});

        System.out.println(card);
        System.out.println(shelf);
        System.out.println(card.checkObjective(shelf));
    }
}