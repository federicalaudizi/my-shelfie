package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.exceptions.TileUnpickableException;
import it.polimi.ingsw.server.model.Board;
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
        String twoPlayerBoard = """
                x x x x x x x x x\s
                x x x e e x x x x\s
                x x x e e e x x x\s
                x x e e e e e e x\s
                x e e e e e e e x\s
                x e e e e e e x x\s
                x x x e e e x x x\s
                x x x x e e x x x\s
                x x x x x x x x x\s
                """;
        assertEquals(board2.toString(), twoPlayerBoard);

        // Three player board layout test
        Board board3 = new Board(3);
        String threePlayerBoard = """
                x x x e x x x x x\s
                x x x e e x x x x\s
                x x e e e e e x x\s
                x x e e e e e e e\s
                x e e e e e e e x\s
                e e e e e e e x x\s
                x x e e e e e x x\s
                x x x x e e x x x\s
                x x x x x e x x x\s
                """;
        assertEquals(board3.toString(), threePlayerBoard);

        // Four player board layout test
        Board board4 = new Board(4);
        String fourPlayerBoard = """
                x x x e e x x x x\s
                x x x e e e x x x\s
                x x e e e e e x x\s
                x e e e e e e e e\s
                e e e e e e e e e\s
                e e e e e e e e x\s
                x x e e e e e x x\s
                x x x e e e x x x\s
                x x x x e e x x x\s
                """;
        assertEquals(board4.toString(), fourPlayerBoard);
    }

    @Test
    public void boardToJSONTest() {
        Board board = new Board(2);
        JSONObject JSONBoard = board.toJSON();
        System.out.println(JSONBoard);
    }
}
