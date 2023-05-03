package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.exceptions.fullColumnException;
import it.polimi.ingsw.server.exceptions.notEnoughTilesException;
import it.polimi.ingsw.server.exceptions.tooManyTilesException;
import it.polimi.ingsw.server.model.Game;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
/**Test for Game
 * @author Sara
 * */
public class GameTest {

        @Test
        public void testChooseFirstPlayer(){
            Game g2 = new Game(2);
            Game g3 = new Game(3);
            Game g4 = new Game(4);

            //Checks if chooseFirstPlayer chooses correctly the first player in a game with 2 players and if
            // assigns correctly the first and the last player
            System.out.println("First Player:" +g2.getFirstPlayerSeat());
            assertTrue(g2.getCurrentPlayerIndex() >=1 && g2.getCurrentPlayerIndex() <=2);
            System.out.println("Last Player:" + g2.getLastPlayer());
            assertTrue(g2.getLastPlayer() >=1 && g2.getLastPlayer() <=2);


            //Checks if chooseFirstPlayer chooses correctly the first player in a game with 3 players and if
            // assigns correctly the first and the last player
            System.out.println("First Player:" + g3.getFirstPlayerSeat());
            assertTrue(g3.getCurrentPlayerIndex() >=1 && g3.getCurrentPlayerIndex() <=3);
            System.out.println("Last Player:" + g3.getLastPlayer());
            assertTrue(g3.getLastPlayer() >=1 && g3.getLastPlayer() <=3);

            //Checks if chooseFirstPlayer chooses correctly the first player in a game with 4 players and if
            // assigns correctly the first and the last player
            System.out.println("First Player:" +g4.getFirstPlayerSeat());
            assertTrue(g4.getCurrentPlayerIndex() >=1 && g4.getCurrentPlayerIndex() <=4);
            System.out.println("Last Player:" + g4.getLastPlayer());
            assertTrue(g4.getLastPlayer() >=1 && g4.getLastPlayer() <=4);
        }

    @Test
    public void getRankedPlayersTest() throws tooManyTilesException, notEnoughTilesException, fullColumnException {
            Game g2 = new Game(2);
            Game g3 = new Game(3);
            Game g4 = new Game(4);

            g2.players.get(0).setEndGameCard();
            g2.players.get(0).assignPointCard(new PointCard(8), 0);
            g2.players.get(0).assignPointCard(new PointCard(4), 1);

            g2.players.get(1).assignPointCard(new PointCard(4), 0);
            g2.players.get(1).assignPointCard(new PointCard(8), 1);

            HashMap<String, Integer> h = new HashMap<>();
            h.put(g2.players.get(0).getUsername(), 13);
            h.put(g2.players.get(1).getUsername(), 12);

            assertEquals(h,g2.getRankedPlayers());

            g3.players.get(0).assignPointCard(new PointCard(4),0);
            g3.players.get(0).setEndGameCard();
            g3.players.get(0).assignPointCard(new PointCard(6),1);
            g3.players.get(0).addPlayerTiles(0,new Tile[]{Tile.CATS,Tile.CATS,Tile.CATS}); //cluster 5 = 5 punti
            g3.players.get(0).addPlayerTiles(0, new Tile[]{Tile.CATS,Tile.CATS});//tot 16

            g3.players.get(1).assignPointCard(new PointCard(8),0);
            g3.players.get(1).assignPointCard(new PointCard(8),1);
            g3.players.get(1).addPlayerTiles(1, new Tile[]{Tile.GAMES,Tile.GAMES});
            g3.players.get(1).addPlayerTiles(2, new Tile[]{Tile.GAMES,Tile.GAMES});
            g3.players.get(1).addPlayerTiles(3, new Tile[]{Tile.GAMES,Tile.GAMES}); //8punti--> tot 24

            g3.players.get(2).assignPointCard(new PointCard(6),0);
            g3.players.get(2).assignPointCard(new PointCard(4),1);
            g3.players.get(2).addPlayerTiles(2, new Tile[]{Tile.PLANTS,Tile.PLANTS});
            g3.players.get(2).addPlayerTiles(1, new Tile[]{Tile.PLANTS});//2punti -->12pt

        HashMap<String, Integer> h1 = new HashMap<>();
        h1.put(g3.players.get(1).getUsername(), 24);
        h1.put(g3.players.get(0).getUsername(), 16);
        h1.put(g3.players.get(2).getUsername(),12);

        assertEquals(h1,g3.getRankedPlayers());

        g4.players.get(0).assignPointCard(new PointCard(4),0);
        g4.players.get(0).setEndGameCard();
        g4.players.get(0).assignPointCard(new PointCard(6),1);
        g4.players.get(0).addPlayerTiles(0,new Tile[]{Tile.CATS,Tile.CATS,Tile.CATS}); //cluster 5 = 5 punti
        g4.players.get(0).addPlayerTiles(0, new Tile[]{Tile.CATS,Tile.CATS});//tot 16

        g4.players.get(1).assignPointCard(new PointCard(8),0);
        g4.players.get(1).assignPointCard(new PointCard(8),1);
        g4.players.get(1).addPlayerTiles(1, new Tile[]{Tile.GAMES,Tile.GAMES});
        g4.players.get(1).addPlayerTiles(2, new Tile[]{Tile.GAMES,Tile.GAMES});
        g4.players.get(1).addPlayerTiles(3, new Tile[]{Tile.GAMES,Tile.GAMES}); //8punti--> tot 24

        g4.players.get(2).assignPointCard(new PointCard(6),0);
        g4.players.get(2).assignPointCard(new PointCard(4),1);
        g4.players.get(2).addPlayerTiles(2, new Tile[]{Tile.PLANTS,Tile.PLANTS});
        g4.players.get(2).addPlayerTiles(1, new Tile[]{Tile.PLANTS});//2punti -->12pt

        g4.players.get(3).assignPointCard(new PointCard(2),0);
        g4.players.get(3).addPlayerTiles(0, new Tile[]{Tile.FRAMES, Tile.FRAMES});
        g4.players.get(3).addPlayerTiles(1, new Tile[]{Tile.FRAMES, Tile.FRAMES});
        g4.players.get(3).addPlayerTiles(2, new Tile[]{Tile.FRAMES, Tile.FRAMES});//tot10

        HashMap<String, Integer> h2 = new HashMap<>();
        h2.put(g4.players.get(1).getUsername(), 24);
        h2.put(g4.players.get(0).getUsername(), 16);
        h2.put(g4.players.get(2).getUsername(),12);
        h2.put(g4.players.get(3).getUsername(),10);

        assertEquals(h2,g4.getRankedPlayers());
    }
}
