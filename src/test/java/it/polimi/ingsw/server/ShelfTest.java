package it.polimi.ingsw.server;

import junit.framework.TestCase;

public class ShelfTest extends TestCase {

    public void testIsFull() {
    }

    public void testGetTile() {
    }

    public void testAddTiles() {
    }

    public void testGetTileClusterPoints() {
        Shelf testingShelf = new Shelf();

        int zeroPoints = testingShelf.getTileClusterPoints();
        assertEquals(0, zeroPoints);
    }

    public void testTestToString() {
    }
}