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

    public void testGetTile() {
    }

    public void testAddTiles() {
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