package it.polimi.ingsw.server;

import java.util.ArrayList;
import java.util.Random;


public class Bag {
    private ArrayList<Tile> tiles;

    public Bag() {
        int remainingTiles = 132;
        tiles = new ArrayList<>(remainingTiles);

        for (int i = 0; i < 22; i++) {
            tiles.add(Tile.PLANTS);
        }
        for (int i = 0; i < 22; i++) {
            tiles.add(Tile.FRAMES);
        }
        for (int i = 0; i < 22; i++) {
            tiles.add(Tile.CATS);
        }
        for (int i = 0; i < 22; i++) {
            tiles.add(Tile.GAMES);
        }
        for (int i = 0; i < 22; i++) {
            tiles.add(Tile.BOOKS);
        }
        for (int i = 0; i < 22; i++) {
            tiles.add(Tile.TROPHIES);
        }
    }

    Tile extract() {
        Random rand = new Random();
        int index = rand.nextInt();
        return tiles.remove(index);
    }


}
