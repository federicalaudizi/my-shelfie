package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.exceptions.TileUnpickableException;
import it.polimi.ingsw.server.exceptions.fullColumnException;
import it.polimi.ingsw.server.exceptions.notEnoughTilesException;
import it.polimi.ingsw.server.exceptions.tooManyTilesException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static junit.framework.TestCase.*;

/**Test for Game
 * @author Sara
 * */
public class GameTest {

    @Test
    public void testChooseFirstPlayer(){
        Game g2 = new Game(2);
        Game g3 = new Game(3);
        Game g4 = new Game(4);

        //Verify that when the game start the current player and the first player are the same
        assertEquals(g2.getFirst(), g2.getCurrentPlayerIndex());
        assertEquals(g3.getFirst(), g3.getCurrentPlayerIndex());
        assertEquals(g4.getFirst(), g4.getCurrentPlayerIndex());

        //Checks if the range of the chosen player is correct
        assertTrue(g2.getFirst()>=0 && g2.getFirst()<=1);
        assertTrue(g3.getFirst()>=0 && g3.getFirst()<=2);
        assertTrue(g4.getFirst()>=0 && g4.getFirst()<=3);
    }

    @Test
    public void getRankedPlayersTest() throws tooManyTilesException, notEnoughTilesException, fullColumnException {
        Game g2 = new Game(2);
        Game g3 = new Game(3);
        Game g4 = new Game(4);

        g2.getPlayers().get(0).setEndGameCard();
        g2.getPlayers().get(0).assignPointCard(new PointCard(8), 0);
        g2.getPlayers().get(0).assignPointCard(new PointCard(4), 1);

        g2.getPlayers().get(1).assignPointCard(new PointCard(4), 0);
        g2.getPlayers().get(1).assignPointCard(new PointCard(8), 1);

        HashMap<String, Integer> h = new HashMap<>();
        h.put(g2.getPlayers().get(0).getUsername(), 13);
        h.put(g2.getPlayers().get(1).getUsername(), 12);

        assertEquals(h,g2.getRankedPlayers());

        g3.getPlayers().get(0).assignPointCard(new PointCard(4),0);
        g3.getPlayers().get(0).setEndGameCard();
        g3.getPlayers().get(0).assignPointCard(new PointCard(6),1);
        g3.getPlayers().get(0).addPlayerTiles(0,new Tile[]{Tile.CATS,Tile.CATS,Tile.CATS}); //cluster 5 = 5 punti
        g3.getPlayers().get(0).addPlayerTiles(0, new Tile[]{Tile.CATS,Tile.CATS});//tot 16

        g3.getPlayers().get(1).assignPointCard(new PointCard(8),0);
        g3.getPlayers().get(1).assignPointCard(new PointCard(8),1);
        g3.getPlayers().get(1).addPlayerTiles(1, new Tile[]{Tile.GAMES,Tile.GAMES});
        g3.getPlayers().get(1).addPlayerTiles(2, new Tile[]{Tile.GAMES,Tile.GAMES});
        g3.getPlayers().get(1).addPlayerTiles(3, new Tile[]{Tile.GAMES,Tile.GAMES}); //8punti--> tot 24

        g3.getPlayers().get(2).assignPointCard(new PointCard(6),0);
        g3.getPlayers().get(2).assignPointCard(new PointCard(4),1);
        g3.getPlayers().get(2).addPlayerTiles(2, new Tile[]{Tile.PLANTS,Tile.PLANTS});
        g3.getPlayers().get(2).addPlayerTiles(1, new Tile[]{Tile.PLANTS});//2punti -->12pt

        HashMap<String, Integer> h1 = new HashMap<>();
        h1.put(g3.getPlayers().get(1).getUsername(), 24);
        h1.put(g3.getPlayers().get(0).getUsername(), 16);
        h1.put(g3.getPlayers().get(2).getUsername(),12);

        assertEquals(h1,g3.getRankedPlayers());

        g4.getPlayers().get(0).assignPointCard(new PointCard(4),0);
        g4.getPlayers().get(0).setEndGameCard();
        g4.getPlayers().get(0).assignPointCard(new PointCard(6),1);
        g4.getPlayers().get(0).addPlayerTiles(0,new Tile[]{Tile.CATS,Tile.CATS,Tile.CATS}); //cluster 5 = 5 punti
        g4.getPlayers().get(0).addPlayerTiles(0, new Tile[]{Tile.CATS,Tile.CATS});//tot 16

        g4.getPlayers().get(1).assignPointCard(new PointCard(8),0);
        g4.getPlayers().get(1).assignPointCard(new PointCard(8),1);
        g4.getPlayers().get(1).addPlayerTiles(1, new Tile[]{Tile.GAMES,Tile.GAMES});
        g4.getPlayers().get(1).addPlayerTiles(2, new Tile[]{Tile.GAMES,Tile.GAMES});
        g4.getPlayers().get(1).addPlayerTiles(3, new Tile[]{Tile.GAMES,Tile.GAMES}); //8punti--> tot 24

        g4.getPlayers().get(2).assignPointCard(new PointCard(6),0);
        g4.getPlayers().get(2).assignPointCard(new PointCard(4),1);
        g4.getPlayers().get(2).addPlayerTiles(2, new Tile[]{Tile.PLANTS,Tile.PLANTS});
        g4.getPlayers().get(2).addPlayerTiles(1, new Tile[]{Tile.PLANTS});//2punti -->12pt

        g4.getPlayers().get(3).assignPointCard(new PointCard(2),0);
        g4.getPlayers().get(3).addPlayerTiles(0, new Tile[]{Tile.FRAMES, Tile.FRAMES});
        g4.getPlayers().get(3).addPlayerTiles(1, new Tile[]{Tile.FRAMES, Tile.FRAMES});
        g4.getPlayers().get(3).addPlayerTiles(2, new Tile[]{Tile.FRAMES, Tile.FRAMES});//tot10

        HashMap<String, Integer> h2 = new HashMap<>();
        h2.put(g4.getPlayers().get(1).getUsername(), 24);
        h2.put(g4.getPlayers().get(0).getUsername(), 16);
        h2.put(g4.getPlayers().get(2).getUsername(),12);
        h2.put(g4.getPlayers().get(3).getUsername(),10);

        assertEquals(h2,g4.getRankedPlayers());
    }

    @Test
    public void toJSONTest() {
        Game game = new Game(2);

        ArrayList<String> usernames = new ArrayList<>();
        usernames.add("Mario");
        usernames.add("Federico");

        game.setUsernames(usernames);

        JSONObject gameJSON = game.toJson();

        Game gameFromJSON = new Game(gameJSON);

        System.out.println(gameJSON);
    }

    @Test
    public void JsonConstructor(){
        Game g = new Game(2);
        ArrayList<String> usernames = new ArrayList<>();
        usernames.add("Mario");
        usernames.add("Federico");

        g.setUsernames(usernames);
        JSONObject j = g.toJson();
        Game gg = new Game(j);

        System.out.println(j);
        System.out.println(gg.toJson());
        System.out.println(g.equals(gg));
    }

    @Test
    public void repopulate() throws TileUnpickableException {
        Game game = new Game(2);

        game.chooseTiles(new Coordinate(4, 1), new Coordinate(5, 1), null);
        //System.out.println("First move:\n" + board);
        game.chooseTiles(new Coordinate(3, 2), new Coordinate(4,2), new Coordinate(5, 2));
        //System.out.println("Second move:\n" + board);
        game.chooseTiles(new Coordinate(1, 3),new Coordinate(1,4), null);
        //System.out.println("Third move:\n" + board);
        game.chooseTiles(new Coordinate(2, 3),new Coordinate(2,4), new Coordinate(2,5));
        //System.out.println("Third move:\n" + board);
        game.chooseTiles(new Coordinate(3, 3),new Coordinate(3,4), new Coordinate(3,5));
        //System.out.println("Third move:\n" + board);
        game.chooseTiles(new Coordinate(3, 6),new Coordinate(3,7), null);
        //System.out.println("Third move:\n" + board);
        game.chooseTiles(new Coordinate(4, 3),new Coordinate(4,4), new Coordinate(4,5));
        //System.out.println("Third move:\n" + board);
        game.chooseTiles(new Coordinate(4, 6),new Coordinate(4,7), null);
        //System.out.println("Third move:\n" + board);
        game.chooseTiles(new Coordinate(5, 3),new Coordinate(5,4), new Coordinate(5,5));
        //System.out.println("Third move:\n" + board);
        game.chooseTiles(new Coordinate(5, 6),null, null);
        //System.out.println("Third move:\n" + board);
        game.chooseTiles(new Coordinate(6, 4),new Coordinate(6,5), null);
        //System.out.println("Third move:\n" + board);
        game.chooseTiles(new Coordinate(7, 4), null, null);
        game.chooseTiles(new Coordinate(7, 5), null, null);
        //System.out.println("qua:\n" + board);

        //System.out.println("\n\n\n FINAL BOARD: \n" + board);
    }
}
