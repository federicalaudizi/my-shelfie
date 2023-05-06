package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.Random;


public class Bag {
    private final ArrayList<Tile> tiles;
    private int remainingTiles;

    /**
     * Constructor for bag
     * */
    public Bag() {
        remainingTiles = 132;
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

    /**
     * Copy constructor
     * @param other
     * */
    public Bag(Bag other){
        this.remainingTiles = other.remainingTiles;
        this.tiles = new ArrayList<>(other.tiles);
    }

    Tile extract() {
        Random rand = new Random();
        int index = rand.nextInt(0, remainingTiles);
        remainingTiles--;
        return tiles.remove(index);
    }


}
