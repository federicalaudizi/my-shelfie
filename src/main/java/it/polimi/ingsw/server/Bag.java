package it.polimi.ingsw.server;

import it.polimi.ingsw.client.Tile;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;


public class Bag {
    private int remainingTiles = 132;
    private ArrayList<Tile> tiles;

    public Bag() {
        tiles = new ArrayList<>(remainingTiles);

        for (int i = 0; i < 22; i++) {
            tiles.add(Tile.PIANTE);
        }
        for (int i = 0; i < 22; i++) {
            tiles.add(Tile.CORNICI);
        }
        for (int i = 0; i < 22; i++) {
            tiles.add(Tile.GATTI);
        }
        for (int i = 0; i < 22; i++) {
            tiles.add(Tile.GIOCHI);
        }
        for (int i = 0; i < 22; i++) {
            tiles.add(Tile.LIBRI);
        }
        for (int i = 0; i < 22; i++) {
            tiles.add(Tile.TROFEI);
        }
    }

    Tile extract() {
        Random rand = new Random();
        int index = rand.nextInt();
        return tiles.remove(index);
    }


}
