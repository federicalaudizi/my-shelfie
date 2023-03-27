package it.polimi.ingsw.server;

public class PatternTen extends CollectiveObjectiveCard {
    public boolean checkObjective(Shelf shelf) {
        int count = 0;
        for (int j = 0; j < 6; j++) {
            for (int i = 1; i < 5; i++) {
                Coordinate tileCoordinate = new Coordinate(i, j);
                Coordinate decrementedCoordinate = new Coordinate(i, j - 1);
                if (shelf.getTile(tileCoordinate) == shelf.getTile(decrementedCoordinate)) {
                    break;
                } else if (j == 5) {
                    count++;
                    if (count == 2) {
                        return true;
                    }
                }

            }
        }
        return false;
    }
}
