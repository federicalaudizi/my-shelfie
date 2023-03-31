package it.polimi.ingsw.server;

import org.junit.*;

public class PlayerTest {
    Player player;
    PersonalObjectiveCard playerObjectve = new PersonalObjectiveCard(PersonalObjectiveCard.PersonalObjectivePattern.FIRST_PATTERN);
    Shelf comparingShelf;


    @Test
    public void testGetShelf() throws Exception {
        player = new Player(playerObjectve);
        comparingShelf = new Shelf();

        Assert.assertTrue(player.getShelf().isEmpty());
        Assert.assertTrue(comparingShelf.equals(player.getShelf()));

        player.addPlayerTiles(0, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        comparingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        Assert.assertFalse(player.getShelf().isEmpty());
        Assert.assertTrue(comparingShelf.equals(player.getShelf()));
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
        player.addPlayerTiles(0, new Tile[]{Tile.PIANTE, Tile.PIANTE, Tile.PIANTE});
        Assert.assertEquals(8, player.calculatePoints());
        player.addPlayerTiles(1, new Tile[]{Tile.PIANTE});
        Assert.assertEquals(9, player.calculatePoints());
    }

    @Test
    public void testAssignPointCard() {
        playerObjectve = new PersonalObjectiveCard();
        player = new Player(playerObjectve);
        comparingShelf = new Shelf();

        player.assignPointCard(new PointCard(4));
        Assert.assertEquals(4, player.calculatePoints());
    }

    @Test
    public void testAddPlayerTiles() throws Exception{
        player = new Player(playerObjectve);
        comparingShelf = new Shelf();

        player.addPlayerTiles(0, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});
        comparingShelf.addTiles(0, new Tile[]{Tile.GATTI, Tile.GATTI, Tile.GATTI});

        Assert.assertTrue(comparingShelf.equals(player.getShelf()));
    }

    @Test
    public void testSetEndGameCard() {
        playerObjectve = new PersonalObjectiveCard();
        player = new Player(playerObjectve);
        comparingShelf = new Shelf();

        player.setEndGameCard();
        Assert.assertEquals(1, player.calculatePoints());
    }

    @Test
    public void testTestToString() {
        player = new Player(playerObjectve);
        System.out.println(player);
        Assert.assertTrue(true);
    }
}