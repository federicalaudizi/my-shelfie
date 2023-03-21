package it.polimi.ingsw.client;

/**
 * This class creates, initializes and manages the game's board.
 *
 * @author Mario Merlo
 */
public class Board {
    int MAX_X = 9, MAX_Y = 9;

    private Tile[][] board;

    /**
     * Initializes the board for the current game.
     * @author Mario Merlo
     *
     * @param playerNumber The effective dimension of the board depends on the number of players in the game.
     */
    Board(int playerNumber) {
        board = new Tile[MAX_X][MAX_Y];
        nonPlayableTileInit(playerNumber);
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
            board[0][3] = null;
            board[2][2] = null;
            board[2][6] = null;
            board[3][8] = null;
            board[5][0] = null;
            board[6][2] = null;
            board[6][6] = null;
            board[8][5] = null;
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
            board[0][4] = null;
            board[1][5] = null;
            board[3][1] = null;
            board[4][0] = null;
            board[4][8] = null;
            board[5][7] = null;
            board[7][3] = null;
            board[8][4] = null;
        }
    }
}
