package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.exceptions.fullColumnException;
import it.polimi.ingsw.server.exceptions.notEnoughTilesException;
import it.polimi.ingsw.server.exceptions.tooManyTilesException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Class that defines a shelf
 *
 * @author Federico
 */
public class Shelf {
    private final Tile[][] contents;

    /**
     * Constructor of the class
     *
     * @author Federico
     */
    Shelf(){
        this.contents = new Tile[5][6];
        for(Tile[] row : contents){
            Arrays.fill(row, Tile.EMPTY);
        }
    }

    /**
     * Copy constructor of the class
     *
     * @param toCopy Shelf that has to be copied
     */
    Shelf(Shelf toCopy){
        this.contents = new Tile[5][6];

        //Fills the new shelf contents with the toAdd one
        for (int i = 0; i < 5 ; i++) {
            for (int j = 0; j < 6; j++) {
                Tile toAdd = toCopy.getTile(new Coordinate(i, j));
                contents[i][j] = toAdd;
            }
        }
    }

    /**
     * Constructor of the class that creates a shelf from a JSONObject
     *
     * @param shelf JSONObject containing the shelf
     * @author Federico
     */
    Shelf(JSONObject shelf){
        this.contents = new Tile[5][6];

        JSONArray array = shelf.getJSONArray("contents");

        for (int i = 0; i < 5 ; i++) {
            for (int j = 0; j < 6; j++) {
                contents[i][j] = Tile.valueOf(array.getJSONArray(i).getJSONObject(j).getString("value"));
            }
        }
    }

    /**
     * Checks if the shelf is full
     *
     * @return Returns true if the shelf is full, false otherwise
     */
    boolean isFull(){
        for (int i = 0; i < 5 ; i++) {
            for (int j = 0; j < 6; j++) {
                if(contents[i][j] == Tile.EMPTY){
                    return false;
                }
            }
        }
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
        return contents[coordinate.getRow()][coordinate.getColumn()];
    }

    /**
     * Adds up to three tiles into the shelf in a specified column and in a specified order,
     * the first tile of the array gets placed in the lowest position of the selected column
     *
     * @author Federico
     *
     * @param column the number of the column where to place the tiles
     * @param tiles array containing the tiles in the intended placement order
     * @throws tooManyTilesException Exception thrown when the array is made of more than 3 tiles or there is not a column with enough free spaces
     * @throws notEnoughTilesException Exception thrown when the array is empty
     * @throws fullColumnException Exception thrown when the selected column is full or there are not enough slots available
     */
    void addTiles(int column, Tile[] tiles) throws tooManyTilesException, notEnoughTilesException, fullColumnException {
        boolean freeColumn = false;
        for(int i=0; i<5; i++){
            if (availableSlots(i) >= tiles.length) {
                freeColumn = true;
                break;
            }
        }
        if(tiles.length > 3 || !freeColumn) throw new tooManyTilesException();
        if(tiles.length == 0) throw new notEnoughTilesException();
        if(availableSlots(column) < tiles.length) throw new fullColumnException();

        for (Tile toAdd : tiles) insertTile(column, toAdd);
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
        int row = 0;
        while(contents[column][row] != Tile.EMPTY) row++;

        contents[column][row] = tile;
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
            if(checkedTile != Tile.EMPTY) {
                takenSlots += 1;
            }
        }

        return 6 - takenSlots;
    }

    /**
     * Method that calculates how many points are worth the various tile clusters in the shelf by scanning whole the shelf grid
     *
     * @author Federico
     *
     * @return amount of points that the clusters are worth
     */
    int getTileClusterPoints(){
        //TODO: its possible that this method has errors
        int points = 0;
        boolean[][] exploredSlots = new boolean[5][6];

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                if(!exploredSlots[i][j] && contents[i][j] != Tile.EMPTY){
                    int count = 1;
                    int curColumn = i;
                    int curRow = j;

                    //Explore upwards
                    if(curRow<4) {
                        while (contents[curColumn][curRow + 1] == contents[curColumn][curRow] && !exploredSlots[curColumn][curRow + 1] && curRow < 5) {
                            count++;
                            curRow++;
                            exploredSlots[curColumn][curRow] = true;

                            //Explore to the right
                            if(curColumn < 3) {
                                while (contents[curColumn + 1][curRow] == contents[curColumn][curRow] && !exploredSlots[curColumn + 1][curRow] && curColumn < 3) {
                                    count++;
                                    curColumn++;
                                    exploredSlots[curColumn][curRow] = true;
                                }
                            }
                            curColumn = i;
                        }
                    }

                    curRow = j;

                    //Explore to the right
                    if(curColumn < 3) {
                        while (contents[curColumn + 1][curRow] == contents[curColumn][curRow] && !exploredSlots[curColumn + 1][curRow] && curColumn < 3) {
                            count++;
                            curColumn++;
                            exploredSlots[curColumn][curRow] = true;

                            //Explore upwards
                            if (curRow < 4) {
                                while (contents[curColumn][curRow + 1] == contents[curColumn][curRow] && !exploredSlots[curColumn][curRow + 1] && curRow < 5) {
                                    count++;
                                    curRow++;
                                    exploredSlots[curColumn][curRow] = true;
                                }
                            }
                            curRow = j;
                        }
                    }

                    //Assign points
                    if(count == 3) points += 2;
                    else if(count == 4) points += 3;
                    else if(count == 5) points += 5;
                    else if(count >= 6) points += 8;
                }
            }

        }

        return points;
    }

    /**
     * Method that checks if two shelves are equal
     *
     * @author Federico
     *
     * @param other the shelf to compare to
     * @return true if the shelves are equal, false otherwise
     */
    boolean equals(Shelf other) {
        for(int i= 0; i < 5; i++){
            for(int j = 0; j < 6; j++){
                if(contents[i][j] != other.contents[i][j]) return false;
            }
        }
        return true;
    }

    /**
     * Method that checks if a shelf contains a specific tile
     *
     * @author Federico
     *
     * @param tile the tile to check
     * @return true if the shelf contains the tile, false otherwise
     */
    boolean contains(Tile tile){
        for(Tile[] columns : contents){
            for(Tile checkedTile : columns){
                if(checkedTile == tile) return true;
            }
        }
        return false;
    }

    /**
     * Method that checks if a shelf is empty
     *
     * @author Federico
     *
     * @return true if the shelf is empty, false otherwise
     */
    boolean isEmpty() {
        for (Tile[] columns : contents) {
            for (Tile checkedTile : columns) {
                if (checkedTile != Tile.EMPTY) return false;
            }
        }
        return true;
    }

    /**
     * Method that generates a string that represents the shelf
     *
     * @author Federico
     *
     * @return the shelf formatted as a viewable string
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (int j = 5; j >= 0; j--) {
            for (int i = 0; i < 5; i++) {
                if(contents[i][j] == Tile.EMPTY)
                    result.append("e ");
                if(contents[i][j] == Tile.OUTSIDE_GAME_BOARD)
                    result.append("x ");
                if(contents[i][j] == Tile.CATS)
                    result.append("g ");
                if(contents[i][j] == Tile.FRAMES)
                    result.append("b ");
                if(contents[i][j] == Tile.GAMES)
                    result.append("y ");
                if(contents[i][j] == Tile.BOOKS)
                    result.append("b ");
                if(contents[i][j] == Tile.PLANTS)
                    result.append("m ");
                if(contents[i][j] == Tile.TROPHIES)
                    result.append("a ");
            }
            result.append(" |\n");
        }

        return result.toString();
    }

    /**
     * This method returns a representation of the shelf
     * Example: {"contents":[[{"color":"GREEN"},{"color":"N/A"}...],...]}
     *
     * @return a JSON representing the shelf
     * @author Federica, Federico
     */
    JSONObject toJson() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        jsonBuilder.append("\"contents\":[");

        // Iterate through the contents array and construct JSON objects for each Tile
        for (int i = 0; i < contents.length; i++) {
            jsonBuilder.append("[");
            for (int j = 0; j < contents[i].length; j++) {
                jsonBuilder.append("{");
                jsonBuilder.append("\"color\":\"").append(contents[i][j].getColour()).append("\",");
                jsonBuilder.append("\"value\":\"").append(Tile.valueOf(contents[i][j].name())).append("\",");
                jsonBuilder.append("}");
                if (j < contents[i].length - 1) {
                    jsonBuilder.append(",");
                }
            }
            jsonBuilder.append("]");
            if (i < contents.length - 1) {
                jsonBuilder.append(",");
            }
        }

        jsonBuilder.append("]");
        jsonBuilder.append("}");
        return new JSONObject(jsonBuilder.toString());
    }
}

