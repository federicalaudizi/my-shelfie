package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.exceptions.TileUnpickableException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {
    @Test
    public void constructorExceptionTest() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> new Board(1));
        assertEquals(exception.getMessage(), "Player number not in range.");
        exception = assertThrows(IllegalArgumentException.class, () -> new Board(42));
        assertEquals(exception.getMessage(), "Player number not in range.");
    }

    @Test
    public void nullCoordinateTest() throws TileUnpickableException {
        Board board = new Board(2);
        try {
            board.pickTile(null, null, null);
        } catch (NullPointerException e) {
            return;
        }
        fail();
    }

    @Test
    public void boardSetupTest() {
        // Two player board layout test
        Board board2 = new Board(2);
        System.out.println(board2);

        // Three player board layout test
        Board board3 = new Board(3);
        System.out.println(board3);

        // Four player board layout test
        Board board4 = new Board(4);
        System.out.println(board4);
    }

    @Test
    public void boardToJSONTest() {
        Board board = new Board(2);
        JSONObject JSONBoard = board.toJSON();
        System.out.println(JSONBoard);
    }

    @Test
    public void repopulateTest() {
        Board board = new Board();
        System.out.println(board);
        String boardCheck = """
        x x x x x x x x x\s
        x x x e e x x x x\s
        x x x e e e x x x\s
        x x e e e g e e x\s
        x e e e e e e e x\s
        x e e e e e e x x\s
        x x x e e e x x x\s
        x x x x e e x x x\s
        x x x x x x x x x\s
        """;
        assertEquals(board.toString(), boardCheck);

        board.checkBoard();

        System.out.println(board);
    }

    @Test
    public void pickTileTest() {
        Board board = new Board();
        board.checkBoard();
        System.out.println(board);
        String oldBoard = board.toString();
        //assertThrows(TileUnpickableException.class, () -> board.pickTile(new Coordinate(4, 5), null, null));

        try {
            board.pickTile(new Coordinate(1, 3), new Coordinate(1,4), null);
        } catch (TileUnpickableException e) {
            throw new RuntimeException(e);
        }

        System.out.println(board);

        assertNotEquals(oldBoard, board.toString());
    }
}
