package it.polimi.ingsw.server;

import java.util.HashSet;
import java.util.Set;

public class PatternFive extends CollectiveObjectiveCard {
    @Override
    public boolean checkObjective(Shelf shelf) {
        int count = 0;
        for (int col = 0; col < 5; col++) {
            Set<Tile> distinctValues = new HashSet<>();
            for (int row = 0; row < 6; row++) {
                Coordinate tileCoordinate = new Coordinate(row, col);
                distinctValues.add(shelf.getTile(tileCoordinate));
            }
            if (distinctValues.size() <= 3) {
                count++;
                if(count == 3){
                    return true;
                }
            }
        }
        return false;
    }
}
