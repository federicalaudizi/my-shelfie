package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.exceptions.fullColumnException;
import it.polimi.ingsw.server.exceptions.notEnoughTilesException;
import it.polimi.ingsw.server.exceptions.tooManyTilesException;
import junit.framework.TestCase;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;

public class ShelfTest extends TestCase {

    public void testIsFull() throws Exception{
        Shelf testingShelf = new Shelf();

        assertFalse(testingShelf.isFull());

        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        assertFalse(testingShelf.isFull());

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
        assertTrue(testingShelf.isFull());
    }

    public void testIsEmpty() throws Exception{
        Shelf testingShelf = new Shelf();
        assertTrue(testingShelf.isEmpty());
        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        assertFalse(testingShelf.isEmpty());
    }

    public void testContains() throws Exception {
        Shelf testingShelf = new Shelf();
        testingShelf.addTiles(0, new Tile[]{Tile.TROPHIES});
        assertFalse(testingShelf.contains(Tile.CATS));
        testingShelf.addTiles(0, new Tile[]{Tile.CATS});
        assertTrue(testingShelf.contains(Tile.CATS));
    }

    public void testEquals() throws Exception{
        Shelf testingShelf = new Shelf();
        Shelf comparingShelf = new Shelf();
        assertTrue(testingShelf.equals(comparingShelf));
        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        assertFalse(testingShelf.equals(comparingShelf));
        comparingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        assertTrue(testingShelf.equals(comparingShelf));
    }

    public void testGetTile() throws Exception {
        Shelf testingShelf = new Shelf();
        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        assertEquals(Tile.CATS, testingShelf.getTile(new Coordinate(0, 0)));
        assertEquals(Tile.CATS, testingShelf.getTile(new Coordinate(0, 1)));
        assertEquals(Tile.CATS, testingShelf.getTile(new Coordinate(0, 2)));
    }

    public void testAddTiles() throws Exception {
        Shelf testingShelf = new Shelf();
        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        assertEquals(Tile.CATS, testingShelf.getTile(new Coordinate(0, 0)));
        assertEquals(Tile.CATS, testingShelf.getTile(new Coordinate(0, 1)));
        assertEquals(Tile.CATS, testingShelf.getTile(new Coordinate(0, 2)));
    }

    public void testGetTileClusterPoints() throws Exception {
        Shelf testingShelf = new Shelf();

        int testingPoints = testingShelf.getTileClusterPoints();
        assertEquals(0, testingPoints);

        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        testingPoints = testingShelf.getTileClusterPoints();
        assertEquals(2, testingPoints);

        testingShelf.addTiles(1, new Tile[]{Tile.CATS});
        testingPoints = testingShelf.getTileClusterPoints();
        assertEquals(3, testingPoints);

        testingShelf.addTiles(2, new Tile[]{Tile.PLANTS, Tile.PLANTS, Tile.PLANTS});
        testingPoints = testingShelf.getTileClusterPoints();
        assertEquals(5, testingPoints);

        testingShelf.addTiles(0, new Tile[]{Tile.PLANTS, Tile.PLANTS, Tile.PLANTS});
        testingShelf.addTiles(1, new Tile[]{Tile.PLANTS, Tile.PLANTS, Tile.PLANTS});
        System.out.println(testingShelf.prettyString());
        testingPoints = testingShelf.getTileClusterPoints();
        assertEquals(11, testingPoints);
    }

    public void testTestToString() throws Exception{
        Shelf testingShelf = new Shelf();

        System.out.println(testingShelf);

        testingShelf.addTiles(0, new Tile[]{Tile.CATS});
        testingShelf.addTiles(0, new Tile[]{Tile.PLANTS});

        System.out.println(testingShelf);
    }

    public void testToJson() throws Exception{
        Shelf testingShelf = new Shelf();

        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        testingShelf.addTiles(0, new Tile[]{Tile.CATS});

        System.out.println(testingShelf.toJson());
        JSONArray json = testingShelf.toJson().getJSONArray("contents");
        System.out.println(json);

        for (int i=0; i<json.length(); i++){
            for (int j=0; j<json.getJSONArray(i).length(); j++){
                System.out.print(json.getJSONArray(i).getJSONObject(j).get("color")+", ");
            }
            System.out.println();
        }

        Shelf comparingShelf = new Shelf(testingShelf.toJson());
        assertTrue(testingShelf.equals(comparingShelf));

        comparingShelf = new Shelf(testingShelf);
        assertTrue(testingShelf.equals(comparingShelf));
    }


    public void testAvailableSlots() throws tooManyTilesException, notEnoughTilesException, fullColumnException {
        Shelf testingShelf = new Shelf();
        assertEquals(6, testingShelf.availableSlots());
        testingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        assertEquals(6, testingShelf.availableSlots());
        assertEquals(3, testingShelf.availableSlots(0));
    }

}