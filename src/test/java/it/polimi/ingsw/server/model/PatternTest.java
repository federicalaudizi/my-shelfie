package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.CollectiveObjectiveCard;
import it.polimi.ingsw.server.model.Shelf;
import it.polimi.ingsw.server.model.Tile;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class PatternTest {
    @Test
    public void patternThree() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternThree pattern = new CollectiveObjectiveCard.PatternThree();


        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS});
        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        testingShelf.addTiles(1, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        testingShelf.addTiles(1, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        testingShelf.addTiles(2, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        testingShelf.addTiles(2, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        testingShelf.addTiles(3, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        testingShelf.addTiles(3, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        testingShelf.addTiles(4, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        testingShelf.addTiles(4, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});

        System.out.println(testingShelf);
        assertFalse(pattern.checkObjective(testingShelf));

    }

    @Test
    public void patternOne() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternOne pattern = new CollectiveObjectiveCard.PatternOne();

        testingShelf.addTiles(0, new Tile[]{Tile.PLANTS, Tile.PLANTS, Tile.TROPHIES});
        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.GAMES, Tile.FRAMES});
        testingShelf.addTiles(1, new Tile[]{Tile.GAMES, Tile.FRAMES, Tile.FRAMES});
        testingShelf.addTiles(1, new Tile[]{Tile.CATS, Tile.PLANTS, Tile.GAMES});
        testingShelf.addTiles(2, new Tile[]{Tile.FRAMES, Tile.BOOKS, Tile.PLANTS});
        testingShelf.addTiles(2, new Tile[]{Tile.FRAMES, Tile.TROPHIES, Tile.CATS});
        testingShelf.addTiles(3, new Tile[]{Tile.PLANTS, Tile.TROPHIES, Tile.TROPHIES});
        testingShelf.addTiles(3, new Tile[]{Tile.FRAMES, Tile.CATS});
        testingShelf.addTiles(4, new Tile[]{Tile.CATS, Tile.BOOKS, Tile.FRAMES});
        testingShelf.addTiles(4, new Tile[]{Tile.CATS, Tile.BOOKS});

        System.out.println(testingShelf);
        assertFalse(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternTwo() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternTwo pattern = new CollectiveObjectiveCard.PatternTwo();

        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        testingShelf.addTiles(0, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.FRAMES});
        testingShelf.addTiles(1, new Tile[]{Tile.CATS, Tile.PLANTS, Tile.PLANTS});
        testingShelf.addTiles(1, new Tile[]{Tile.TROPHIES, Tile.CATS, Tile.TROPHIES});
        testingShelf.addTiles(2, new Tile[]{Tile.FRAMES, Tile.PLANTS, Tile.PLANTS});
        testingShelf.addTiles(2, new Tile[]{Tile.TROPHIES, Tile.FRAMES, Tile.CATS});
        testingShelf.addTiles(3, new Tile[]{Tile.GAMES, Tile.TROPHIES, Tile.GAMES});
        testingShelf.addTiles(3, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.PLANTS});
        testingShelf.addTiles(4, new Tile[]{Tile.GAMES, Tile.FRAMES, Tile.FRAMES});
        testingShelf.addTiles(4, new Tile[]{Tile.FRAMES, Tile.FRAMES, Tile.CATS});

        System.out.println(testingShelf);
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternFour() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternFour pattern = new CollectiveObjectiveCard.PatternFour();

        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        testingShelf.addTiles(0, new Tile[]{Tile.TROPHIES, Tile.FRAMES, Tile.FRAMES});
        testingShelf.addTiles(1, new Tile[]{Tile.CATS, Tile.PLANTS, Tile.PLANTS});
        testingShelf.addTiles(1, new Tile[]{Tile.TROPHIES, Tile.FRAMES, Tile.FRAMES});
        testingShelf.addTiles(2, new Tile[]{Tile.FRAMES, Tile.PLANTS, Tile.PLANTS});
        testingShelf.addTiles(2, new Tile[]{Tile.TROPHIES, Tile.FRAMES, Tile.CATS});
        testingShelf.addTiles(3, new Tile[]{Tile.GAMES, Tile.TROPHIES, Tile.GAMES});
        testingShelf.addTiles(3, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.PLANTS});
        testingShelf.addTiles(4, new Tile[]{Tile.GAMES, Tile.FRAMES, Tile.FRAMES});
        testingShelf.addTiles(4, new Tile[]{Tile.FRAMES, Tile.CATS, Tile.CATS});

        System.out.println(testingShelf);
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternFive() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternFive pattern = new CollectiveObjectiveCard.PatternFive();

        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        testingShelf.addTiles(0, new Tile[]{Tile.TROPHIES, Tile.PLANTS});
        testingShelf.addTiles(1, new Tile[]{Tile.CATS, Tile.PLANTS, Tile.PLANTS});
        testingShelf.addTiles(1, new Tile[]{Tile.TROPHIES, Tile.CATS, Tile.TROPHIES});
        testingShelf.addTiles(2, new Tile[]{Tile.FRAMES, Tile.PLANTS, Tile.PLANTS});
        testingShelf.addTiles(2, new Tile[]{Tile.TROPHIES, Tile.FRAMES, Tile.CATS});
        testingShelf.addTiles(3, new Tile[]{Tile.GAMES, Tile.TROPHIES, Tile.GAMES});
        testingShelf.addTiles(3, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.PLANTS});
        testingShelf.addTiles(4, new Tile[]{Tile.GAMES, Tile.FRAMES, Tile.FRAMES});
        testingShelf.addTiles(4, new Tile[]{Tile.FRAMES, Tile.CATS, Tile.CATS});

        System.out.println(testingShelf);
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternSix() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternSix pattern = new CollectiveObjectiveCard.PatternSix();

        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        testingShelf.addTiles(0, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.FRAMES});
        testingShelf.addTiles(1, new Tile[]{Tile.CATS, Tile.PLANTS, Tile.PLANTS});
        testingShelf.addTiles(1, new Tile[]{Tile.TROPHIES, Tile.CATS, Tile.TROPHIES});
        testingShelf.addTiles(2, new Tile[]{Tile.FRAMES, Tile.PLANTS, Tile.PLANTS});
        testingShelf.addTiles(2, new Tile[]{Tile.TROPHIES, Tile.FRAMES, Tile.CATS});
        testingShelf.addTiles(3, new Tile[]{Tile.GAMES, Tile.TROPHIES, Tile.GAMES});
        testingShelf.addTiles(3, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.PLANTS});
        testingShelf.addTiles(4, new Tile[]{Tile.GAMES, Tile.FRAMES, Tile.FRAMES});
        testingShelf.addTiles(4, new Tile[]{Tile.FRAMES, Tile.CATS, Tile.CATS});

        System.out.println(testingShelf);
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternSeven() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternSeven pattern = new CollectiveObjectiveCard.PatternSeven();

        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS});
        testingShelf.addTiles(0, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.FRAMES});
        testingShelf.addTiles(1, new Tile[]{Tile.CATS, Tile.CATS, Tile.PLANTS});
        testingShelf.addTiles(1, new Tile[]{Tile.TROPHIES, Tile.CATS, Tile.TROPHIES});
        testingShelf.addTiles(2, new Tile[]{Tile.FRAMES, Tile.PLANTS, Tile.CATS});
        testingShelf.addTiles(2, new Tile[]{Tile.TROPHIES, Tile.FRAMES, Tile.CATS});
        testingShelf.addTiles(3, new Tile[]{Tile.GAMES, Tile.TROPHIES, Tile.GAMES});
        testingShelf.addTiles(3, new Tile[]{Tile.CATS, Tile.PLANTS, Tile.PLANTS});
        testingShelf.addTiles(4, new Tile[]{Tile.GAMES, Tile.CATS, Tile.FRAMES});
        testingShelf.addTiles(4, new Tile[]{Tile.FRAMES, Tile.CATS});

        System.out.println(testingShelf);
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternEight() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternEight pattern = new CollectiveObjectiveCard.PatternEight();

        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        testingShelf.addTiles(0, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.FRAMES});
        testingShelf.addTiles(1, new Tile[]{Tile.GAMES, Tile.PLANTS, Tile.PLANTS});
        testingShelf.addTiles(1, new Tile[]{Tile.TROPHIES, Tile.CATS, Tile.TROPHIES});
        testingShelf.addTiles(2, new Tile[]{Tile.FRAMES, Tile.PLANTS, Tile.PLANTS});
        testingShelf.addTiles(2, new Tile[]{Tile.TROPHIES, Tile.FRAMES, Tile.CATS});
        testingShelf.addTiles(3, new Tile[]{Tile.GAMES, Tile.TROPHIES, Tile.GAMES});
        testingShelf.addTiles(3, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.TROPHIES});
        testingShelf.addTiles(4, new Tile[]{Tile.GAMES, Tile.FRAMES, Tile.FRAMES});
        testingShelf.addTiles(4, new Tile[]{Tile.FRAMES, Tile.CATS, Tile.CATS});

        System.out.println(testingShelf);
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternNine() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternNine pattern = new CollectiveObjectiveCard.PatternNine();

        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.GAMES, Tile.BOOKS});
        testingShelf.addTiles(0, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.FRAMES});
        testingShelf.addTiles(1, new Tile[]{Tile.CATS, Tile.PLANTS, Tile.PLANTS});
        testingShelf.addTiles(1, new Tile[]{Tile.TROPHIES, Tile.CATS, Tile.TROPHIES});
        testingShelf.addTiles(2, new Tile[]{Tile.FRAMES, Tile.PLANTS, Tile.GAMES});
        testingShelf.addTiles(2, new Tile[]{Tile.TROPHIES, Tile.BOOKS, Tile.CATS});
        testingShelf.addTiles(3, new Tile[]{Tile.GAMES, Tile.TROPHIES, Tile.GAMES});
        testingShelf.addTiles(3, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.PLANTS});
        testingShelf.addTiles(4, new Tile[]{Tile.GAMES, Tile.FRAMES, Tile.FRAMES});
        testingShelf.addTiles(4, new Tile[]{Tile.FRAMES, Tile.CATS, Tile.CATS});

        System.out.println(testingShelf);
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternTen() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternTen pattern = new CollectiveObjectiveCard.PatternTen();

        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.GAMES, Tile.BOOKS});
        testingShelf.addTiles(0, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.FRAMES});
        testingShelf.addTiles(1, new Tile[]{Tile.GAMES, Tile.PLANTS, Tile.PLANTS});
        testingShelf.addTiles(1, new Tile[]{Tile.GAMES, Tile.CATS, Tile.TROPHIES});
        testingShelf.addTiles(2, new Tile[]{Tile.FRAMES, Tile.PLANTS, Tile.GAMES});
        testingShelf.addTiles(2, new Tile[]{Tile.CATS, Tile.BOOKS, Tile.CATS});
        testingShelf.addTiles(3, new Tile[]{Tile.BOOKS, Tile.TROPHIES, Tile.GAMES});
        testingShelf.addTiles(3, new Tile[]{Tile.BOOKS, Tile.PLANTS, Tile.PLANTS});
        testingShelf.addTiles(4, new Tile[]{Tile.TROPHIES, Tile.FRAMES, Tile.FRAMES});
        testingShelf.addTiles(4, new Tile[]{Tile.FRAMES, Tile.CATS, Tile.CATS});

        System.out.println(testingShelf);
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternEleven() throws Exception{
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternEleven pattern = new CollectiveObjectiveCard.PatternEleven();

        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.GAMES, Tile.CATS});
        testingShelf.addTiles(0, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.FRAMES});
        testingShelf.addTiles(1, new Tile[]{Tile.CATS, Tile.CATS, Tile.PLANTS});
        testingShelf.addTiles(1, new Tile[]{Tile.TROPHIES, Tile.CATS, Tile.TROPHIES});
        testingShelf.addTiles(2, new Tile[]{Tile.CATS, Tile.PLANTS, Tile.CATS});
        testingShelf.addTiles(2, new Tile[]{Tile.TROPHIES, Tile.BOOKS, Tile.CATS});
        testingShelf.addTiles(3, new Tile[]{Tile.GAMES, Tile.TROPHIES, Tile.GAMES});
        testingShelf.addTiles(3, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.PLANTS});
        testingShelf.addTiles(4, new Tile[]{Tile.GAMES, Tile.FRAMES, Tile.FRAMES});
        testingShelf.addTiles(4, new Tile[]{Tile.FRAMES, Tile.CATS, Tile.CATS});

        System.out.println(testingShelf);
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternTwelve() throws Exception{
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternTwelve pattern = new CollectiveObjectiveCard.PatternTwelve();

        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.GAMES, Tile.BOOKS});
        testingShelf.addTiles(0, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.FRAMES});
        testingShelf.addTiles(1, new Tile[]{Tile.CATS, Tile.PLANTS, Tile.PLANTS});
        testingShelf.addTiles(1, new Tile[]{Tile.TROPHIES, Tile.CATS});
        testingShelf.addTiles(2, new Tile[]{Tile.FRAMES, Tile.PLANTS, Tile.GAMES});
        testingShelf.addTiles(2, new Tile[]{Tile.TROPHIES});
        testingShelf.addTiles(3, new Tile[]{Tile.GAMES, Tile.TROPHIES, Tile.GAMES});
        testingShelf.addTiles(4, new Tile[]{Tile.GAMES, Tile.FRAMES});

        System.out.println(testingShelf);
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void testGetRandomCard() {
        CollectiveObjectiveCard card = CollectiveObjectiveCard.getRandomCard();
        System.out.println("Random Card: " + card);
    }

}