package it.polimi.ingsw.server.model;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class PatternTest {
    @Test
    public void patternThree() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternThree pattern = new CollectiveObjectiveCard.PatternThree();


        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS});
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(2, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(2, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(3, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(3, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(4, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(4, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});

        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));

    }

    @Test
    public void patternOne() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternOne pattern = new CollectiveObjectiveCard.PatternOne();

        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.PLANTS, Tile.PLANTS, Tile.TROPHIES});
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.GAMES, Tile.FRAMES});
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.GAMES, Tile.FRAMES, Tile.FRAMES});
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.CATS, Tile.PLANTS, Tile.GAMES});
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(2, new Tile[]{Tile.FRAMES, Tile.BOOKS, Tile.PLANTS});
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(2, new Tile[]{Tile.FRAMES, Tile.TROPHIES, Tile.CATS});
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(3, new Tile[]{Tile.PLANTS, Tile.TROPHIES, Tile.TROPHIES});
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(3, new Tile[]{Tile.FRAMES, Tile.CATS});
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(4, new Tile[]{Tile.CATS, Tile.BOOKS, Tile.FRAMES});
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(4, new Tile[]{Tile.CATS, Tile.BOOKS});

        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternTwo() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternTwo pattern = new CollectiveObjectiveCard.PatternTwo();

        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.FRAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.CATS, Tile.PLANTS, Tile.PLANTS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.TROPHIES, Tile.CATS, Tile.TROPHIES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(2, new Tile[]{Tile.FRAMES, Tile.PLANTS, Tile.PLANTS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(2, new Tile[]{Tile.TROPHIES, Tile.FRAMES, Tile.CATS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(3, new Tile[]{Tile.GAMES, Tile.TROPHIES, Tile.GAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(3, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.PLANTS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(4, new Tile[]{Tile.GAMES, Tile.FRAMES, Tile.FRAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(4, new Tile[]{Tile.FRAMES, Tile.FRAMES, Tile.CATS});

        System.out.println(testingShelf.prettyString());
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternFour() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternFour pattern = new CollectiveObjectiveCard.PatternFour();

        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.TROPHIES, Tile.FRAMES, Tile.FRAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.CATS, Tile.PLANTS, Tile.PLANTS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.TROPHIES, Tile.FRAMES, Tile.FRAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(2, new Tile[]{Tile.FRAMES, Tile.PLANTS, Tile.PLANTS});

        System.out.println(testingShelf.prettyString());
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternFive() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternFive pattern = new CollectiveObjectiveCard.PatternFive();

        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.TROPHIES, Tile.PLANTS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.CATS, Tile.PLANTS, Tile.PLANTS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.TROPHIES, Tile.CATS, Tile.TROPHIES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(2, new Tile[]{Tile.FRAMES, Tile.PLANTS, Tile.PLANTS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(2, new Tile[]{Tile.TROPHIES, Tile.FRAMES, Tile.CATS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(3, new Tile[]{Tile.GAMES, Tile.TROPHIES, Tile.GAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(3, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.PLANTS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(4, new Tile[]{Tile.GAMES, Tile.FRAMES, Tile.FRAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(4, new Tile[]{Tile.FRAMES, Tile.CATS, Tile.CATS});

        System.out.println(testingShelf.prettyString());
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternSix() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternSix pattern = new CollectiveObjectiveCard.PatternSix();

        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.FRAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.CATS, Tile.PLANTS, Tile.PLANTS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.TROPHIES, Tile.CATS, Tile.TROPHIES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(2, new Tile[]{Tile.FRAMES, Tile.PLANTS, Tile.PLANTS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(2, new Tile[]{Tile.TROPHIES, Tile.FRAMES, Tile.CATS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(3, new Tile[]{Tile.GAMES, Tile.TROPHIES, Tile.GAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(3, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.PLANTS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(4, new Tile[]{Tile.GAMES, Tile.FRAMES, Tile.FRAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(4, new Tile[]{Tile.FRAMES, Tile.CATS, Tile.CATS});

        System.out.println(testingShelf.prettyString());
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternSeven() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternSeven pattern = new CollectiveObjectiveCard.PatternSeven();

        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.FRAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.CATS, Tile.CATS, Tile.PLANTS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.TROPHIES, Tile.CATS, Tile.TROPHIES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(2, new Tile[]{Tile.FRAMES, Tile.PLANTS, Tile.CATS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(2, new Tile[]{Tile.TROPHIES, Tile.FRAMES, Tile.CATS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(3, new Tile[]{Tile.GAMES, Tile.TROPHIES, Tile.GAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(3, new Tile[]{Tile.CATS, Tile.PLANTS, Tile.PLANTS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(4, new Tile[]{Tile.GAMES, Tile.CATS, Tile.FRAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(4, new Tile[]{Tile.FRAMES, Tile.CATS});

        System.out.println(testingShelf.prettyString());
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternEight() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternEight pattern = new CollectiveObjectiveCard.PatternEight();

        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.FRAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.GAMES, Tile.PLANTS, Tile.PLANTS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.TROPHIES, Tile.CATS, Tile.TROPHIES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(2, new Tile[]{Tile.FRAMES, Tile.PLANTS, Tile.PLANTS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(2, new Tile[]{Tile.TROPHIES, Tile.FRAMES, Tile.CATS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(3, new Tile[]{Tile.GAMES, Tile.TROPHIES, Tile.GAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(3, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.TROPHIES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(4, new Tile[]{Tile.GAMES, Tile.FRAMES, Tile.FRAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(4, new Tile[]{Tile.FRAMES, Tile.CATS, Tile.CATS});

        System.out.println(testingShelf.prettyString());
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternNine() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternNine pattern = new CollectiveObjectiveCard.PatternNine();

        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.GAMES, Tile.BOOKS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.FRAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.CATS, Tile.PLANTS, Tile.PLANTS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.TROPHIES, Tile.CATS, Tile.TROPHIES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(2, new Tile[]{Tile.FRAMES, Tile.PLANTS, Tile.GAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(2, new Tile[]{Tile.TROPHIES, Tile.BOOKS, Tile.CATS});

        System.out.println(testingShelf.prettyString());
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternTen() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternTen pattern = new CollectiveObjectiveCard.PatternTen();

        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.GAMES, Tile.BOOKS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.FRAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.GAMES, Tile.PLANTS, Tile.PLANTS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.GAMES, Tile.CATS, Tile.TROPHIES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(2, new Tile[]{Tile.FRAMES, Tile.PLANTS, Tile.GAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(2, new Tile[]{Tile.CATS, Tile.BOOKS, Tile.CATS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(3, new Tile[]{Tile.BOOKS, Tile.TROPHIES, Tile.GAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(3, new Tile[]{Tile.BOOKS, Tile.PLANTS, Tile.PLANTS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(4, new Tile[]{Tile.TROPHIES, Tile.FRAMES, Tile.FRAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(4, new Tile[]{Tile.FRAMES, Tile.CATS, Tile.CATS});

        System.out.println(testingShelf.prettyString());
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternEleven() throws Exception{
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternEleven pattern = new CollectiveObjectiveCard.PatternEleven();

        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.GAMES, Tile.CATS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.FRAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.CATS, Tile.CATS, Tile.PLANTS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.TROPHIES, Tile.CATS, Tile.TROPHIES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(2, new Tile[]{Tile.CATS, Tile.PLANTS, Tile.CATS});

        System.out.println(testingShelf.prettyString());
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternTwelve() throws Exception{
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternTwelve pattern = new CollectiveObjectiveCard.PatternTwelve();

        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.GAMES, Tile.BOOKS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.TROPHIES, Tile.PLANTS, Tile.FRAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.CATS, Tile.PLANTS, Tile.PLANTS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(1, new Tile[]{Tile.TROPHIES, Tile.CATS});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(2, new Tile[]{Tile.FRAMES, Tile.PLANTS, Tile.GAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(2, new Tile[]{Tile.TROPHIES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(3, new Tile[]{Tile.GAMES, Tile.TROPHIES, Tile.GAMES});
        System.out.println(testingShelf.prettyString());
        assertFalse(pattern.checkObjective(testingShelf));
        testingShelf.addTiles(4, new Tile[]{Tile.GAMES, Tile.FRAMES});

        System.out.println(testingShelf.prettyString());
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void testGetRandomCard() {
        CollectiveObjectiveCard card = CollectiveObjectiveCard.getRandomCard();
        System.out.println("Random Card: " + card);
    }

    @Test
    public void testGetRandomCard1() {
        CollectiveObjectiveCard card = CollectiveObjectiveCard.getRandomCard();
        System.out.println("Random Card: " + card);

        assert card != null;
        CollectiveObjectiveCard cc = CollectiveObjectiveCard.getRandomCard(card);
        System.out.println("second random card: "+ cc);

        CollectiveObjectiveCard card1 = CollectiveObjectiveCard.getRandomCard();
        System.out.println("Random Card: " + card1);

        assert card1 != null;
        CollectiveObjectiveCard cc1 = CollectiveObjectiveCard.getRandomCard(card1);
        System.out.println("second random card: "+ cc1);

        CollectiveObjectiveCard card2 = CollectiveObjectiveCard.getRandomCard();
        System.out.println("Random Card: " + card2);

        assert card2 != null;
        CollectiveObjectiveCard cc2 = CollectiveObjectiveCard.getRandomCard(card2);
        System.out.println("second random card: "+ cc2);

        CollectiveObjectiveCard card3 = CollectiveObjectiveCard.getRandomCard();
        System.out.println("Random Card: " + card3);

        CollectiveObjectiveCard cc3 = CollectiveObjectiveCard.getRandomCard(card3);
        System.out.println("second random card: "+ cc3);

        CollectiveObjectiveCard card4 = CollectiveObjectiveCard.getRandomCard();
        System.out.println("Random Card: " + card4);

        CollectiveObjectiveCard cc4 = CollectiveObjectiveCard.getRandomCard(card4);
        System.out.println("second random card: "+ cc4);

        CollectiveObjectiveCard card5 = CollectiveObjectiveCard.getRandomCard();
        System.out.println("Random Card: " + card5);

        CollectiveObjectiveCard cc5 = CollectiveObjectiveCard.getRandomCard(card5);
        System.out.println("second random card: "+ cc5);
    }

}