package it.polimi.ingsw.server;

public class PatternOne extends CollectiveObjectiveCard {
    @Override
    public boolean checkObjective(Shelf shelf) {
        int count = 0;

        //check vertically
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                Coordinate tileCoordinate = new Coordinate(i, j);
                Coordinate tileCoordinateIncremented = new Coordinate(i + 1, j);
                if (shelf.getTile(tileCoordinate) == shelf.getTile(tileCoordinateIncremented)) {
                    count++;
                    if (count == 6){
                        return true;
                    }
                }
            }
        }

        //check horizontally
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                Coordinate tileCoordinate = new Coordinate(i, j);
                Coordinate tileCoordinateIncremented = new Coordinate(i, j + 1);
                if (shelf.getTile(tileCoordinate) == shelf.getTile(tileCoordinateIncremented)) {
                    count++;
                    if (count == 6){
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
