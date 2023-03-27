package it.polimi.ingsw.server;

public class PatternTwelve extends CollectiveObjectiveCard {
    public boolean checkObjective(Shelf shelf) {

        boolean increasing = false;

        for (int i = 0; i < 5; i++) {
            boolean isIncreasing = true;
            for (int j = 1; j < 6; j++) {
                Coordinate tileCoordinate = new Coordinate(i, j);
                Coordinate tileCoordinateDecremented = new Coordinate(i , j - i);
                if (shelf.getTile(tileCoordinate).ordinal() != shelf.getTile(tileCoordinateDecremented).ordinal() - 1) {
                    isIncreasing = false;
                }
            }
            if (isIncreasing) {
                increasing = true;
            }

        }

        return increasing ;
    }
}
