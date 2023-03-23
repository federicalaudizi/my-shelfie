package it.polimi.ingsw.server;


public class PatternSix extends CollectiveObjectiveCard {
    public boolean checkObjective(Shelf shelf) {
        int count = 0;
        for (Tile type : Tile.values()) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 4; j++) {
                    Coordinate tileCoordinate = new Coordinate(i, j);
                    Coordinate tileCoordinateIncremented = new Coordinate(i, j + 1);
                    if (shelf.getTile(tileCoordinate) == type) {
                        count++;
                        if (count == 8) {
                            return true;
                        }
                    }
                }
            }
            count = 0;
        }
       return false;
    }
}
