package it.polimi.ingsw.server;

/**
 * Class that defines a shelf
 *
 * @author Federico
 */
public class Shelf {
    private Tile[][] contents;

    /**
     * Constructor of the class
     *
     * @author Federico
     */
    Shelf(){
        this.contents = new Tile[5][6];
    }

    /**
     * Copy constructor of the class
     *
     * @param toCopy Shelf that has to be copied
     */
    Shelf(Shelf toCopy){
        this.contents = new Tile[5][6];
        //TODO: write copy constructor
    }

    /**
     * Checks if the shelf is full
     *
     * @return Returns true if the shelf is full, false otherwise
     */
    boolean isFull(){
        //TODO: Write isFull() method
        return true;
    }

    /**
     * Method that gives the tile present at a specified coordinate
     *
     * @author Federico
     *
     * @param coordinate the coordinates of the requested tile
     * @return the tile present at the specified coordinates
     */
    Tile getTile(Coordinate coordinate) {
        //TODO: write getTile() method
        return null;
    }

    /**
     * Adds up to three tiles into the shelf in a specified column and in a specified order,
     * the first of the tiles array gets placed in the lowest position of the selected column
     *
     * @author Federico
     *
     * @param column the number of the column where to place the tiles
     * @param tiles array containing the tiles in the intended placement order
     * @throws tooManyTilesException Exception thrown when the array is made of more than 3 tiles
     * @throws notEnoughTilesException Exception thrown when the array is empty
     * @throws fullColumnException Exception thrown when the selected column is full or there are not enough slots available
     */
    void addTiles(int column, Tile[] tiles) throws tooManyTilesException, notEnoughTilesException, fullColumnException{
        if(tiles.length > 3) throw new tooManyTilesException();
        if(tiles.length == 0) throw new notEnoughTilesException();
        if(availableSlots(column) < tiles.length) throw new fullColumnException();

        //TODO: write addTiles() method
    }

    /**
     * Helper method to insert a tile into a column
     *
     * @author Federico
     *
     * @param column number of the column where to insert the tile
     * @param tile tile to be inserted
     */
    private void insertTile(int column, Tile tile){
        //TODO: write insertTile() method
    }

    /**
     * Helper method that returns how many empty slots are left in a column
     *
     * @author Federico
     *
     * @param column number of the column
     * @return number of available slots
     */
    private int availableSlots(int column){
        int takenSlots = 0;

        for (Tile checkedTile : contents[column]) {
            if(checkedTile != null) {
                takenSlots += 1;
            }
        }

        return 6 - takenSlots;
    }

    static class tooManyTilesException extends Exception {
    }

    static class notEnoughTilesException extends Exception{
    }

    static class fullColumnException extends Exception {
    }
}
