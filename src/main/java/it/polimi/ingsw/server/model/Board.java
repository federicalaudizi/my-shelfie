package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.exceptions.TileUnpickableException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.InvalidParameterException;

/**
 * This class creates, initializes and manages the game's board.
 *
 * @author Mario Merlo
 */
public class Board {
    private final int MAX_X = 9, MAX_Y = 9;
    private final Tile[][] board;
    private final Bag bag = new Bag();

    // TODO This constructor must be removed once the test phase is over
    Board() {
        board = new Tile[MAX_X][MAX_Y];
        nonPlayableTileInit(2);
        board[3][5] = Tile.CATS;
    }

    /**
     * Initializes the board for the current game.
     * @param playerNumber The effective dimension of the board depends on the number of players in the game.
     * @throws IllegalArgumentException Thrown when a wrong player number is passed to the function.
     * @author Mario Merlo
     */
    Board(int playerNumber) throws IllegalArgumentException {
        // Check for player number in range
        if(playerNumber >= 2 && playerNumber <= 4) {
            board = new Tile[MAX_X][MAX_Y];

            // Setup non-playable areas for the number of players selected
            nonPlayableTileInit(playerNumber);
        } else throw new IllegalArgumentException("Player number not in range.");

        // Fill board with random tiles from bag
        repopulate();
    }

    /**
     * Initializes a new board from an already instantiated one.
     * @param toCopy The board to copy from
     */
     Board(Board toCopy) {
        this.board = new Tile[MAX_X][MAX_Y];

        for(int i = 0; i < MAX_X; i++){
            System.arraycopy(toCopy.board[i], 0, this.board[i], 0, MAX_Y);
        }
    }

    /**
     * Since the game board is tilted by 45°, this method initializes the tiles that are outside the playing
     * field. This method is only called once per game and assigns to a newly instantiated Board object the special
     * Tile.OUTSIDE_GAME_BOARD tile to specified coordinates.
     *
     * @param playerNumber Different tiles are made available according to the player number for the current game.
     * @author Mario Merlo
     */
    private void nonPlayableTileInit(int playerNumber) {
        // Upper corners initialization
        for(int i = 0; i <= 3; i++) {
            for(int j = 0; j <= 3 - i; j++)
                board[i][j] = Tile.OUTSIDE_GAME_BOARD;
            for(int j = 8; j >= 5 + i; j--)
                board[i][j] = Tile.OUTSIDE_GAME_BOARD;
        }

        // Lower corners initialization
        for(int i = 5; i <= 8; i++) {
            for(int j = 0; j <= i - 5; j++)
                board[i][j] = Tile.OUTSIDE_GAME_BOARD;
            for(int j = 8; j >= 8 - i + 5; j--)
                board[i][j] = Tile.OUTSIDE_GAME_BOARD;
        }

        switch (playerNumber) {
            case 2 -> {
                threePlayerBoardInit(false);
                fourPlayerBoardInit(false);
            }
            case 3 -> {
                threePlayerBoardInit(true);
                fourPlayerBoardInit(false);
            }
            case 4 -> {
                threePlayerBoardInit(true);
                fourPlayerBoardInit(true);
            }
        }

        for(int i = 0; i < MAX_X; i++) {
            for(int j = 0; j < MAX_Y; j++) {
                if(board[i][j] == null)
                    board[i][j] = Tile.EMPTY;
            }
        }
    }

    /**
     * Renders available or unavailable the tiles that are exclusive to games with three or more players.
     *
     * @param atLeastThree This parameter specifies whether there are at least three players in the game. If true, then
     *                     the tiles are made available; if false, they are initialized as Tile.OUTSIDE_GAME_BOARD.
     *
     * @author Mario Merlo
     */
    private void threePlayerBoardInit(boolean atLeastThree) {
        if (!atLeastThree) {
            board[0][3] = Tile.OUTSIDE_GAME_BOARD;
            board[2][2] = Tile.OUTSIDE_GAME_BOARD;
            board[2][6] = Tile.OUTSIDE_GAME_BOARD;
            board[3][8] = Tile.OUTSIDE_GAME_BOARD;
            board[5][0] = Tile.OUTSIDE_GAME_BOARD;
            board[6][2] = Tile.OUTSIDE_GAME_BOARD;
            board[6][6] = Tile.OUTSIDE_GAME_BOARD;
            board[8][5] = Tile.OUTSIDE_GAME_BOARD;
        } else {
            board[0][3] = Tile.EMPTY;
            board[2][2] = Tile.EMPTY;
            board[2][6] = Tile.EMPTY;
            board[3][8] = Tile.EMPTY;
            board[5][0] = Tile.EMPTY;
            board[6][2] = Tile.EMPTY;
            board[6][6] = Tile.EMPTY;
            board[8][5] = Tile.EMPTY;
        }
    }

    /**
     * Renders available or unavailable the tiles that are exclusive to four player games.
     *
     * @param fourPlayers This parameter specifies whether the game has exactly four players. If true, then the four
     *                    player tiles are made available; if false, they are initialized as Tile.OUTSIDE_GAME_BOARD.
     *
     * @author Mario Merlo
     */
    private void fourPlayerBoardInit(boolean fourPlayers) {
        if (!fourPlayers) {
            board[0][4] = Tile.OUTSIDE_GAME_BOARD;
            board[1][5] = Tile.OUTSIDE_GAME_BOARD;
            board[3][1] = Tile.OUTSIDE_GAME_BOARD;
            board[4][0] = Tile.OUTSIDE_GAME_BOARD;
            board[4][8] = Tile.OUTSIDE_GAME_BOARD;
            board[5][7] = Tile.OUTSIDE_GAME_BOARD;
            board[7][3] = Tile.OUTSIDE_GAME_BOARD;
            board[8][4] = Tile.OUTSIDE_GAME_BOARD;
        } else {
            board[0][4] = Tile.EMPTY;
            board[1][5] = Tile.EMPTY;
            board[3][1] = Tile.EMPTY;
            board[4][0] = Tile.EMPTY;
            board[4][8] = Tile.EMPTY;
            board[5][7] = Tile.EMPTY;
            board[7][3] = Tile.EMPTY;
            board[8][4] = Tile.EMPTY;
        }
    }

    /**
     * Returns whether a tile has a free side and is thus pickable from the board.
     * @param coord Specifies the coordinate of the tile on the board.
     * @return true if the tile has at least a side with no other tiles attached, false if not.
     * @throws IllegalArgumentException If the coordinate that is passed to the method identifies a tile that is either
     *                                  empty or outside the game board, this exception is thrown.
     * @author Mario Merlo
     */
    private boolean isPickable(Coordinate coord) throws IllegalArgumentException {
        // Saving coordinate to make code more readable and avoid subsequent method calls.
        int row = coord.getRow();
        int column = coord.getColumn();

        // The check only makes sense when called upon a playable tile.
        // If this condition is not met, then the method throws an exception.
        if (board[row][column] != Tile.EMPTY && board[row][column] != Tile.OUTSIDE_GAME_BOARD) {
            // Checking for specific limit cases
            if (row == 0) {
                if (column == 3)
                    return board[1][3] == Tile.EMPTY || board[0][4] == Tile.EMPTY || board[0][4] == Tile.OUTSIDE_GAME_BOARD;
                if (column == 4)
                    return board[0][3] == Tile.EMPTY || board[1][4] == Tile.EMPTY;
            } else if (row == 3) {
                if (column == 8)
                    return board[3][7] == Tile.EMPTY || board[4][8] == Tile.EMPTY || board[4][8] == Tile.OUTSIDE_GAME_BOARD;
            } else if (row == 4) {
                if (column == 0)
                    return board[5][0] == Tile.EMPTY || board[4][1] == Tile.EMPTY;
                if (column == 8)
                    return board[3][8] == Tile.EMPTY || board[4][7] == Tile.EMPTY;
            } else if (row == 5) {
                if (column == 0)
                    return board[4][0] == Tile.EMPTY || board[4][0] == Tile.OUTSIDE_GAME_BOARD || board[5][1] == Tile.EMPTY;
            } else if (row == 8) {
                if (column == 4)
                    return board[7][4] == Tile.EMPTY || board[8][5] == Tile.EMPTY;
                if (column == 5)
                    return board[7][5] == Tile.EMPTY || board[8][4] == Tile.EMPTY || board[8][4] == Tile.OUTSIDE_GAME_BOARD;
            }
        } else throw new IllegalArgumentException("Selected tile is empty or outside of the board.");

        // Default checking case
        return board[row + 1][column] == Tile.EMPTY ||
                board[row - 1][column] == Tile.EMPTY ||
                board[row][column + 1] == Tile.EMPTY ||
                board[row][column - 1] == Tile.EMPTY ||
                board[row + 1][column] == Tile.OUTSIDE_GAME_BOARD ||
                board[row - 1][column] == Tile.OUTSIDE_GAME_BOARD ||
                board[row][column + 1] == Tile.OUTSIDE_GAME_BOARD ||
                board[row][column - 1] == Tile.OUTSIDE_GAME_BOARD;
    }

    /**
     * Returns an array of tiles selected from the board through their coordinates. The method also checks if a tile
     * is pickable through the method isPickable(). After all tiles are checked, the method removes those tiles from
     * the board by calling removeTile() on the tile's coordinate.
     * @param coord1 The coordinate for the first tile.
     * @param coord2 The coordinate for the second tile.
     * @param coord3 The coordinate for the third tile.
     * @return Tile array with the selected tiles.
     * @throws TileUnpickableException If a tile is deemed unpickable by isPickable(), this exception is thrown.
     * @throws NullPointerException If coord1 is null, this exception is thrown, because at least one coordinate must
     * be passed to the method.
     * @throws InvalidParameterException If removeTile() throws an InvalidParameterException, this is escalated outside
     * Board.
     * @author Mario Merlo
     */
    Tile[] pickTile(Coordinate coord1, Coordinate coord2, Coordinate coord3) throws TileUnpickableException, NullPointerException, InvalidParameterException {
        Tile[] pickableTiles = new Tile[3];

        try {
            if(isPickable(coord1)) {
                pickableTiles[0] = board[coord1.getRow()][coord1.getColumn()];
            } else throw new TileUnpickableException();
        } catch (NullPointerException e) {
            throw new NullPointerException("At least one coordinate must not be null.");
        }

        if(coord2 != null) {
            if (isPickable(coord2) && (coord1.getRow() == coord2.getRow() || coord1.getColumn() == coord2.getColumn())) {
                pickableTiles[1] = board[coord2.getRow()][coord2.getColumn()];
            } else throw new TileUnpickableException();
        }

        if(coord3 != null) {
            if (isPickable(coord3) && (coord1.getRow() == coord3.getRow() || coord1.getColumn() == coord3.getColumn())) {
                pickableTiles[2] = board[coord3.getRow()][coord3.getColumn()];
            } else throw new TileUnpickableException();
        }

        try {
            removeTile(coord1);
            if(coord2 != null) removeTile(coord2);
            if(coord3 != null) removeTile(coord3);
        } catch (InvalidParameterException e) {
            throw new InvalidParameterException();
        }

        return pickableTiles;
    }

    /**
     * Removes a tile from the board by assigning it the null value.
     * @param coord The coordinate of the tile to be removed.
     * @throws InvalidParameterException If the coordinates passed identify a non-playing tile, this exception
     * is thrown.
     * @author Mario Merlo
     */
    private void removeTile(Coordinate coord) throws InvalidParameterException {
        if(board[coord.getRow()][coord.getColumn()] != Tile.OUTSIDE_GAME_BOARD)
            board[coord.getRow()][coord.getColumn()] = Tile.EMPTY;
        else throw new InvalidParameterException();
    }

    /**
     * Checks whether the board needs to be refilled with tiles from the bag. This happens when there are no more
     * clusters of tiles on the board at least two tiles big.
     *
     * @author Mario Merlo
     */
    void checkBoard() {
        boolean toRepopulate = true;

        // Inner board check: only the first 8 rows and columns are checked, to avoid breaking boundaries.
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(board[i][j] != Tile.EMPTY && board[i][j] != Tile.OUTSIDE_GAME_BOARD)
                    toRepopulate = (board[i + 1][j] == Tile.EMPTY && board[i][j + 1] == Tile.EMPTY) ||
                                   (board[i + 1][j] == Tile.OUTSIDE_GAME_BOARD && board[i][j + 1] == Tile.OUTSIDE_GAME_BOARD);
                if(!toRepopulate) break;
            }
        }

        // Outliers check: in three and four player modes, these tiles must be checked as well, but it is not necessary
        // to check them in the same way as for all the other tiles.
        if(toRepopulate) {
            if(board[3][8] != Tile.OUTSIDE_GAME_BOARD)
                toRepopulate = (board[4][8] == Tile.EMPTY && board[3][7] == Tile.EMPTY) ||
                               (board[4][8] == Tile.OUTSIDE_GAME_BOARD && board[3][7] == Tile.EMPTY);
            if(board[4][8] != Tile.OUTSIDE_GAME_BOARD)
                toRepopulate = board[4][7] == Tile.EMPTY;
            if(board[8][4] != Tile.OUTSIDE_GAME_BOARD)
                toRepopulate = board[8][5] == Tile.EMPTY && board[7][4] == Tile.EMPTY;
            if(board[8][5] != Tile.OUTSIDE_GAME_BOARD)
                toRepopulate = board[7][5] == Tile.EMPTY;
        }

        // If toRepopulate remains true up to this point, then there are no more tiles in groups of at least two, thus
        // the board must be repopulated through the repopulate() method.
        if(toRepopulate) repopulate();
    }

    /**
     * Refills the board with tiles extracted from the bag. Each tile assigned to a new coordinate is a copy of the tile
     * passed by Bag's extract() method.
     *
     * @author Mario Merlo
     */
    private void repopulate() {
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                if(board[i][j] == Tile.EMPTY) {
                    board[i][j] = bag.extract();
                }
            }
        }
    }

    /**
     * Returns a String representation of the board in its current state.
     * @return a string that represents the current state of the board.
     *
     * @author Mario Merlo
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        for(int i = 0; i < MAX_X; i++) {
            for(int j = 0; j < MAX_Y; j++) {
                output.append(board[i][j].getSymbol());
            }
        }

        return output.toString();
    }

    /**
     * Returns a JSONObject representation of the board
     * @return a JSONObject containing a "board" field, which in turn contains an array of arrays representing the
     *         whole board
     */
    public JSONObject toJSON() {
        JSONArray contents = new JSONArray();
        for(int i = 0; i < MAX_X; i++) {
            JSONArray row = new JSONArray();
            for(int j = 0; j < MAX_Y; j++) {
                // Add tile to row
                row.put(board[i][j]);
            }
            // Add row to board contents
            contents.put(row);
            // Clear row to restart the process
        }
        // Return correctly-formatted JSONObject
        return new JSONObject().put("board", contents);
    }

    /**
     * Json constructor for Board
     * @param jsonObject is the json Object containing the board
     * */
    public Board(JSONObject jsonObject){
        this.board = new Tile[MAX_X][MAX_Y];
        JSONArray contents = jsonObject.getJSONArray("board");
        for(int i = 0; i < MAX_X; i++) {
            JSONArray row = contents.getJSONArray(i);
            for(int j = 0; j < MAX_Y; j++) {
                this.board[i][j] = (Tile) row.get(j);
            }
        }

    }

    /**
     * Returns a copy of the passed board
     *
     * @return A new board identical to the one passed as a parameter
     */
    Board copy() {
        return new Board(this);
    }

    public int getMAX_X() {
        return MAX_X;
    }

    public int getMAX_Y() {
        return MAX_Y;
    }


    /**
     * Method that checks if two boards are equal
     *
     * @param other the board to compare to
     * @return true if the boards are equals, false if not
     * */
    public boolean equals(Board other){
        for(int i = 0;i<MAX_X;i++){
            for(int j =0;j<MAX_Y;j++){
                if(this.board[i][j]!=other.board[i][j])
                    return false;
            }
        }
        return true;
    }
}
