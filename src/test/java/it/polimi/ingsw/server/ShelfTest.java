package it.polimi.ingsw.server;

import junit.framework.TestCase;

public class ShelfTest extends TestCase {

    public void testIsFull() throws Exception{
        Shelf testingShelf = new Shelf();

        assertFalse(testingShelf.isFull());

        testingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        assertFalse(testingShelf.isFull());

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
        assertTrue(testingShelf.isFull());
    }

    public void testIsEmpty() throws Exception{
        Shelf testingShelf = new Shelf();
        assertTrue(testingShelf.isEmpty());
        testingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        assertFalse(testingShelf.isEmpty());
    }

    public void testContains() throws Exception {
        Shelf testingShelf = new Shelf();
        testingShelf.addTiles(0, new Tile[]{Tile.TROFEI});
        assertFalse(testingShelf.contains(Tile.GATTI));
        testingShelf.addTiles(0, new Tile[]{Tile.GATTI});
        assertTrue(testingShelf.contains(Tile.GATTI));
    }

    public void testEquals() throws Exception{
        Shelf testingShelf = new Shelf();
        Shelf comparingShelf = new Shelf();
        assertTrue(testingShelf.equals(comparingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        assertFalse(testingShelf.equals(comparingShelf));
        comparingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        assertTrue(testingShelf.equals(comparingShelf));
    }

    public void testGetTile() throws Exception {
        Shelf testingShelf = new Shelf();
        testingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        assertEquals(Tile.GATTI, testingShelf.getTile(new Coordinate(0, 0)));
        assertEquals(Tile.GATTI, testingShelf.getTile(new Coordinate(0, 1)));
        assertEquals(Tile.GATTI, testingShelf.getTile(new Coordinate(0, 2)));
    }

    public void testAddTiles() throws Exception {
        Shelf testingShelf = new Shelf();
        testingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        assertEquals(Tile.GATTI, testingShelf.getTile(new Coordinate(0, 0)));
        assertEquals(Tile.GATTI, testingShelf.getTile(new Coordinate(0, 1)));
        assertEquals(Tile.GATTI, testingShelf.getTile(new Coordinate(0, 2)));
    }

    public void testGetTileClusterPoints() throws Exception {
        Shelf testingShelf = new Shelf();

        int testingPoints = testingShelf.getTileClusterPoints();
        assertEquals(0, testingPoints);

        testingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        testingPoints = testingShelf.getTileClusterPoints();
        assertEquals(2, testingPoints);

        testingShelf.addTiles(2, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        testingPoints = testingShelf.getTileClusterPoints();
        assertEquals(4, testingPoints);
    }

    public void testTestToString() throws Exception{
        Shelf testingShelf = new Shelf();

        System.out.println(testingShelf);

        testingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        testingShelf.addTiles(0, new Tile[]{Tile.GATTI});

        System.out.println(testingShelf);
    }
}