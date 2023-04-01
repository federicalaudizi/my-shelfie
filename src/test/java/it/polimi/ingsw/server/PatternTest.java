package it.polimi.ingsw.server;

import junit.framework.TestCase;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class PatternTest {
    @Test
    public void patternThree() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternThree pattern = new CollectiveObjectiveCard.PatternThree();


        testingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GATTI});
        testingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        testingShelf.addTiles(1, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        testingShelf.addTiles(1, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        testingShelf.addTiles(2, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        testingShelf.addTiles(2, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        testingShelf.addTiles(3, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        testingShelf.addTiles(3, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        testingShelf.addTiles(4, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        testingShelf.addTiles(4, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});

        System.out.println(testingShelf);
        assertFalse(pattern.checkObjective(testingShelf));

    }

    @Test
    public void patternOne() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternOne pattern = new CollectiveObjectiveCard.PatternOne();

        testingShelf.addTiles(0, new Tile[]{Tile.PIANTE, Tile.PIANTE, Tile.TROFEI});
        testingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GIOCHI, Tile.CORNICI});
        testingShelf.addTiles(1, new Tile[]{Tile.GIOCHI, Tile.CORNICI, Tile.CORNICI});
        testingShelf.addTiles(1, new Tile[]{Tile.GATTI, Tile.PIANTE, Tile.GIOCHI});
        testingShelf.addTiles(2, new Tile[]{Tile.CORNICI, Tile.LIBRI, Tile.PIANTE});
        testingShelf.addTiles(2, new Tile[]{Tile.CORNICI, Tile.TROFEI, Tile.GATTI});
        testingShelf.addTiles(3, new Tile[]{Tile.PIANTE, Tile.TROFEI, Tile.TROFEI});
        testingShelf.addTiles(3, new Tile[]{Tile.CORNICI, Tile.GATTI});
        testingShelf.addTiles(4, new Tile[]{Tile.GATTI, Tile.LIBRI, Tile.CORNICI});
        testingShelf.addTiles(4, new Tile[]{Tile.GATTI, Tile.LIBRI});

        System.out.println(testingShelf);
        assertFalse(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternTwo() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternTwo pattern = new CollectiveObjectiveCard.PatternTwo();

        testingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        testingShelf.addTiles(0, new Tile[]{Tile.TROFEI, Tile.PIANTE, Tile.CORNICI});
        testingShelf.addTiles(1, new Tile[]{Tile.GATTI, Tile.PIANTE, Tile.PIANTE});
        testingShelf.addTiles(1, new Tile[]{Tile.TROFEI, Tile.GATTI, Tile.TROFEI});
        testingShelf.addTiles(2, new Tile[]{Tile.CORNICI, Tile.PIANTE, Tile.PIANTE});
        testingShelf.addTiles(2, new Tile[]{Tile.TROFEI, Tile.CORNICI, Tile.GATTI});
        testingShelf.addTiles(3, new Tile[]{Tile.GIOCHI, Tile.TROFEI, Tile.GIOCHI});
        testingShelf.addTiles(3, new Tile[]{Tile.TROFEI, Tile.PIANTE, Tile.PIANTE});
        testingShelf.addTiles(4, new Tile[]{Tile.GIOCHI, Tile.CORNICI, Tile.CORNICI});
        testingShelf.addTiles(4, new Tile[]{Tile.CORNICI, Tile.CORNICI, Tile.GATTI});

        System.out.println(testingShelf);
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternFour() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternFour pattern = new CollectiveObjectiveCard.PatternFour();

        testingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        testingShelf.addTiles(0, new Tile[]{Tile.TROFEI, Tile.CORNICI, Tile.CORNICI});
        testingShelf.addTiles(1, new Tile[]{Tile.GATTI, Tile.PIANTE, Tile.PIANTE});
        testingShelf.addTiles(1, new Tile[]{Tile.TROFEI, Tile.CORNICI, Tile.CORNICI});
        testingShelf.addTiles(2, new Tile[]{Tile.CORNICI, Tile.PIANTE, Tile.PIANTE});
        testingShelf.addTiles(2, new Tile[]{Tile.TROFEI, Tile.CORNICI, Tile.GATTI});
        testingShelf.addTiles(3, new Tile[]{Tile.GIOCHI, Tile.TROFEI, Tile.GIOCHI});
        testingShelf.addTiles(3, new Tile[]{Tile.TROFEI, Tile.PIANTE, Tile.PIANTE});
        testingShelf.addTiles(4, new Tile[]{Tile.GIOCHI, Tile.CORNICI, Tile.CORNICI});
        testingShelf.addTiles(4, new Tile[]{Tile.CORNICI, Tile.GATTI, Tile.GATTI});

        System.out.println(testingShelf);
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternFive() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternFive pattern = new CollectiveObjectiveCard.PatternFive();

        testingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        testingShelf.addTiles(0, new Tile[]{Tile.TROFEI, Tile.PIANTE});
        testingShelf.addTiles(1, new Tile[]{Tile.GATTI, Tile.PIANTE, Tile.PIANTE});
        testingShelf.addTiles(1, new Tile[]{Tile.TROFEI, Tile.GATTI, Tile.TROFEI});
        testingShelf.addTiles(2, new Tile[]{Tile.CORNICI, Tile.PIANTE, Tile.PIANTE});
        testingShelf.addTiles(2, new Tile[]{Tile.TROFEI, Tile.CORNICI, Tile.GATTI});
        testingShelf.addTiles(3, new Tile[]{Tile.GIOCHI, Tile.TROFEI, Tile.GIOCHI});
        testingShelf.addTiles(3, new Tile[]{Tile.TROFEI, Tile.PIANTE, Tile.PIANTE});
        testingShelf.addTiles(4, new Tile[]{Tile.GIOCHI, Tile.CORNICI, Tile.CORNICI});
        testingShelf.addTiles(4, new Tile[]{Tile.CORNICI, Tile.GATTI, Tile.GATTI});

        System.out.println(testingShelf);
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternSix() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternSix pattern = new CollectiveObjectiveCard.PatternSix();

        testingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        testingShelf.addTiles(0, new Tile[]{Tile.TROFEI, Tile.PIANTE, Tile.CORNICI});
        testingShelf.addTiles(1, new Tile[]{Tile.GATTI, Tile.PIANTE, Tile.PIANTE});
        testingShelf.addTiles(1, new Tile[]{Tile.TROFEI, Tile.GATTI, Tile.TROFEI});
        testingShelf.addTiles(2, new Tile[]{Tile.CORNICI, Tile.PIANTE, Tile.PIANTE});
        testingShelf.addTiles(2, new Tile[]{Tile.TROFEI, Tile.CORNICI, Tile.GATTI});
        testingShelf.addTiles(3, new Tile[]{Tile.GIOCHI, Tile.TROFEI, Tile.GIOCHI});
        testingShelf.addTiles(3, new Tile[]{Tile.TROFEI, Tile.PIANTE, Tile.PIANTE});
        testingShelf.addTiles(4, new Tile[]{Tile.GIOCHI, Tile.CORNICI, Tile.CORNICI});
        testingShelf.addTiles(4, new Tile[]{Tile.CORNICI, Tile.GATTI, Tile.GATTI});

        System.out.println(testingShelf);
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternSeven() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternSeven pattern = new CollectiveObjectiveCard.PatternSeven();

        testingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GATTI});
        testingShelf.addTiles(0, new Tile[]{Tile.TROFEI, Tile.PIANTE, Tile.CORNICI});
        testingShelf.addTiles(1, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.PIANTE});
        testingShelf.addTiles(1, new Tile[]{Tile.TROFEI, Tile.GATTI, Tile.TROFEI});
        testingShelf.addTiles(2, new Tile[]{Tile.CORNICI, Tile.PIANTE, Tile.GATTI});
        testingShelf.addTiles(2, new Tile[]{Tile.TROFEI, Tile.CORNICI, Tile.GATTI});
        testingShelf.addTiles(3, new Tile[]{Tile.GIOCHI, Tile.TROFEI, Tile.GIOCHI});
        testingShelf.addTiles(3, new Tile[]{Tile.GATTI, Tile.PIANTE, Tile.PIANTE});
        testingShelf.addTiles(4, new Tile[]{Tile.GIOCHI, Tile.GATTI, Tile.CORNICI});
        testingShelf.addTiles(4, new Tile[]{Tile.CORNICI, Tile.GATTI});

        System.out.println(testingShelf);
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternEight() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternEight pattern = new CollectiveObjectiveCard.PatternEight();

        testingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        testingShelf.addTiles(0, new Tile[]{Tile.TROFEI, Tile.PIANTE, Tile.CORNICI});
        testingShelf.addTiles(1, new Tile[]{Tile.GIOCHI, Tile.PIANTE, Tile.PIANTE});
        testingShelf.addTiles(1, new Tile[]{Tile.TROFEI, Tile.GATTI, Tile.TROFEI});
        testingShelf.addTiles(2, new Tile[]{Tile.CORNICI, Tile.PIANTE, Tile.PIANTE});
        testingShelf.addTiles(2, new Tile[]{Tile.TROFEI, Tile.CORNICI, Tile.GATTI});
        testingShelf.addTiles(3, new Tile[]{Tile.GIOCHI, Tile.TROFEI, Tile.GIOCHI});
        testingShelf.addTiles(3, new Tile[]{Tile.TROFEI, Tile.PIANTE, Tile.TROFEI});
        testingShelf.addTiles(4, new Tile[]{Tile.GIOCHI, Tile.CORNICI, Tile.CORNICI});
        testingShelf.addTiles(4, new Tile[]{Tile.CORNICI, Tile.GATTI, Tile.GATTI});

        System.out.println(testingShelf);
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternNine() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternNine pattern = new CollectiveObjectiveCard.PatternNine();

        testingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GIOCHI, Tile.LIBRI});
        testingShelf.addTiles(0, new Tile[]{Tile.TROFEI, Tile.PIANTE, Tile.CORNICI});
        testingShelf.addTiles(1, new Tile[]{Tile.GATTI, Tile.PIANTE, Tile.PIANTE});
        testingShelf.addTiles(1, new Tile[]{Tile.TROFEI, Tile.GATTI, Tile.TROFEI});
        testingShelf.addTiles(2, new Tile[]{Tile.CORNICI, Tile.PIANTE, Tile.GIOCHI});
        testingShelf.addTiles(2, new Tile[]{Tile.TROFEI, Tile.LIBRI, Tile.GATTI});
        testingShelf.addTiles(3, new Tile[]{Tile.GIOCHI, Tile.TROFEI, Tile.GIOCHI});
        testingShelf.addTiles(3, new Tile[]{Tile.TROFEI, Tile.PIANTE, Tile.PIANTE});
        testingShelf.addTiles(4, new Tile[]{Tile.GIOCHI, Tile.CORNICI, Tile.CORNICI});
        testingShelf.addTiles(4, new Tile[]{Tile.CORNICI, Tile.GATTI, Tile.GATTI});

        System.out.println(testingShelf);
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternTen() throws Exception {
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternTen pattern = new CollectiveObjectiveCard.PatternTen();

        testingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GIOCHI, Tile.LIBRI});
        testingShelf.addTiles(0, new Tile[]{Tile.TROFEI, Tile.PIANTE, Tile.CORNICI});
        testingShelf.addTiles(1, new Tile[]{Tile.GIOCHI, Tile.PIANTE, Tile.PIANTE});
        testingShelf.addTiles(1, new Tile[]{Tile.GIOCHI, Tile.GATTI, Tile.TROFEI});
        testingShelf.addTiles(2, new Tile[]{Tile.CORNICI, Tile.PIANTE, Tile.GIOCHI});
        testingShelf.addTiles(2, new Tile[]{Tile.GATTI, Tile.LIBRI, Tile.GATTI});
        testingShelf.addTiles(3, new Tile[]{Tile.LIBRI, Tile.TROFEI, Tile.GIOCHI});
        testingShelf.addTiles(3, new Tile[]{Tile.LIBRI, Tile.PIANTE, Tile.PIANTE});
        testingShelf.addTiles(4, new Tile[]{Tile.TROFEI, Tile.CORNICI, Tile.CORNICI});
        testingShelf.addTiles(4, new Tile[]{Tile.CORNICI, Tile.GATTI, Tile.GATTI});

        System.out.println(testingShelf);
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternEleven() throws Exception{
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternEleven pattern = new CollectiveObjectiveCard.PatternEleven();

        testingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GIOCHI, Tile.GATTI});
        testingShelf.addTiles(0, new Tile[]{Tile.TROFEI, Tile.PIANTE, Tile.CORNICI});
        testingShelf.addTiles(1, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.PIANTE});
        testingShelf.addTiles(1, new Tile[]{Tile.TROFEI, Tile.GATTI, Tile.TROFEI});
        testingShelf.addTiles(2, new Tile[]{Tile.GATTI, Tile.PIANTE, Tile.GATTI});
        testingShelf.addTiles(2, new Tile[]{Tile.TROFEI, Tile.LIBRI, Tile.GATTI});
        testingShelf.addTiles(3, new Tile[]{Tile.GIOCHI, Tile.TROFEI, Tile.GIOCHI});
        testingShelf.addTiles(3, new Tile[]{Tile.TROFEI, Tile.PIANTE, Tile.PIANTE});
        testingShelf.addTiles(4, new Tile[]{Tile.GIOCHI, Tile.CORNICI, Tile.CORNICI});
        testingShelf.addTiles(4, new Tile[]{Tile.CORNICI, Tile.GATTI, Tile.GATTI});

        System.out.println(testingShelf);
        assertTrue(pattern.checkObjective(testingShelf));
    }

    @Test
    public void patternTwelve() throws Exception{
        Shelf testingShelf = new Shelf();
        CollectiveObjectiveCard.PatternTwelve pattern = new CollectiveObjectiveCard.PatternTwelve();

        testingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GIOCHI, Tile.LIBRI});
        testingShelf.addTiles(0, new Tile[]{Tile.TROFEI, Tile.PIANTE, Tile.CORNICI});
        testingShelf.addTiles(1, new Tile[]{Tile.GATTI, Tile.PIANTE, Tile.PIANTE});
        testingShelf.addTiles(1, new Tile[]{Tile.TROFEI, Tile.GATTI});
        testingShelf.addTiles(2, new Tile[]{Tile.CORNICI, Tile.PIANTE, Tile.GIOCHI});
        testingShelf.addTiles(2, new Tile[]{Tile.TROFEI});
        testingShelf.addTiles(3, new Tile[]{Tile.GIOCHI, Tile.TROFEI, Tile.GIOCHI});
        testingShelf.addTiles(4, new Tile[]{Tile.GIOCHI, Tile.CORNICI});

        System.out.println(testingShelf);
        assertTrue(pattern.checkObjective(testingShelf));
    }
}