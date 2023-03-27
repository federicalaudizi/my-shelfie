package it.polimi.ingsw.server;

import java.util.HashSet;
import java.util.Set;

public class PatternEight extends CollectiveObjectiveCard {
    public boolean checkObjective(Shelf shelf) {
        Set<Tile> squareValues = new HashSet<>();
        int count = 0;
        for (int i = 0; i < 6; i++){
            for (int j = 0; j < 5; j++){
                Coordinate tileCoordinate = new Coordinate(i, j);
                // devo fare check che sia diverso da unknown
                squareValues.add(shelf.getTile(tileCoordinate));
                if (squareValues.size() <= 3){
                    count++;
                    if (count == 4){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}