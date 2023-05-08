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
        Board board = new Board(2);
        System.out.println("Initial state of board:\n" + board);

        String initialBoard = board.toString();

        // Testing for no tiles passed to pickTile
        assertThrows(NullPointerException.class, () -> board.pickTile(null, null, null));

        // Testing for tiles that cannot be picked
        assertThrows(TileUnpickableException.class, () -> board.pickTile(new Coordinate(4, 5), null, null));
        assertThrows(IllegalArgumentException.class, () -> board.pickTile(new Coordinate(6, 6), new Coordinate(8, 8), null));
        assertThrows(TileUnpickableException.class, () -> board.pickTile(new Coordinate(4, 2), new Coordinate(4, 3), new Coordinate(4, 4)));

        // Testing for tiles that can be picked
        try {
            board.pickTile(new Coordinate(4, 1), new Coordinate(5, 1), null);
            System.out.println("First move:\n" + board);
            board.pickTile(new Coordinate(3, 2), new Coordinate(4,2), new Coordinate(5, 2));
            System.out.println("Second move:\n" + board);
            board.pickTile(new Coordinate(1, 3), null, null);
            System.out.println("Third move:\n" + board);
            // TODO Add testing for limit cases
        } catch (TileUnpickableException e) {
            fail();
        }

        System.out.println("Final state of the board:\n" + board);

        assertNotEquals(initialBoard, board.toString());
    }

    @Test
    public void toStringTest(){
        Board board = new Board();

        System.out.println(board);


    }

    @Test
    public void equalsTest(){
        Board b = new Board();
        Board b1 = new Board();

        assertTrue(b.equals(b1));
    }

    @Test
    public void JsonConstructorTest(){
        Board board = new Board(2);
        JSONObject JSONBoard = board.toJSON();

        Board board1 = new Board(JSONBoard);
        assertTrue(board.equals(board1));
    }

}
