package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.exceptions.fullColumnException;
import it.polimi.ingsw.server.exceptions.notEnoughTilesException;
import it.polimi.ingsw.server.exceptions.tooManyTilesException;
import org.json.JSONObject;
import org.junit.*;

import java.util.Objects;

import static org.junit.Assert.assertTrue;

public class PlayerTest {
    Player player;
    PersonalObjectiveCard playerObjectve = new PersonalObjectiveCard(PersonalObjectiveCard.PersonalObjectivePattern.FIRST_PATTERN);
    Shelf comparingShelf;
    Game g;

    @Test
    public void testGetShelf() throws Exception {
        player = new Player(playerObjectve);
        comparingShelf = new Shelf();

        assertTrue(player.getShelf().isEmpty());
        assertTrue(comparingShelf.equals(player.getShelf()));

        player.addPlayerTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        comparingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        Assert.assertFalse(player.getShelf().isEmpty());
        assertTrue(comparingShelf.equals(player.getShelf()));
    }

    @Test
    public void testCalculatePoints() throws Exception{
        player = new Player(playerObjectve);
        comparingShelf = new Shelf();

        Assert.assertEquals(0, player.calculatePoints());
        player.setEndGameCard();
        Assert.assertEquals(1, player.calculatePoints());
        player.assignPointCard(new PointCard(4));
        Assert.assertEquals(5, player.calculatePoints());
        player.addPlayerTiles(0, new Tile[]{Tile.PLANTS, Tile.PLANTS, Tile.PLANTS});
        Assert.assertEquals(8, player.calculatePoints());
        player.addPlayerTiles(1, new Tile[]{Tile.PLANTS});
        Assert.assertEquals(9, player.calculatePoints());
    }

    @Test
    public void testAssignPointCard() {
        playerObjectve = new PersonalObjectiveCard(g);
        player = new Player(playerObjectve);
        comparingShelf = new Shelf();

        player.assignPointCard(new PointCard(4));
        Assert.assertEquals(4, player.calculatePoints());
    }

    @Test
    public void testAddPlayerTiles() throws Exception{
        player = new Player(playerObjectve);
        comparingShelf = new Shelf();

        player.addPlayerTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        comparingShelf.addTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});

        assertTrue(comparingShelf.equals(player.getShelf()));
    }

    @Test
    public void testSetEndGameCard() {
        playerObjectve = new PersonalObjectiveCard(g);
        player = new Player(playerObjectve);
        comparingShelf = new Shelf();

        player.setEndGameCard();
        Assert.assertEquals(1, player.calculatePoints());
    }

    @Test
    public void testTestToString() throws tooManyTilesException, notEnoughTilesException, fullColumnException {
        player = new Player(playerObjectve);
        System.out.println(player);
        player.addPlayerTiles(0, new Tile[]{Tile.CATS, Tile.CATS, Tile.CATS});
        player.addPlayerTiles(2, new Tile[]{Tile.BOOKS, Tile.CATS, Tile.TROPHIES});
        System.out.println(player);
    }

    @Test
    public void testToJson(){
        player = new Player(playerObjectve);

        player.assignPointCard(new PointCard(4), 0);

        Player player1 = new Player(player.toJson());

        System.out.println(player.toJson());
        System.out.println(player1.toJson());

        assert(Objects.equals(player1.toJson().toString(), player1.toJson().toString()));
    }

    @Test
    public void  testEquals(){
        player = new Player(playerObjectve);
        Player player1 = new Player(player);

        assert(player1.equals(player));
    }

    @Test
    public void JsonConstructorTest(){
        Player p = new Player(new PersonalObjectiveCard(g));

        JSONObject j = p.toJson();

        Player pp = new Player(j);

        assertTrue(p.equals(pp));
    }
}