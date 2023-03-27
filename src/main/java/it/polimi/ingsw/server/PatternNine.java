package it.polimi.ingsw.server;

public class PatternNine extends CollectiveObjectiveCard {
    public boolean checkObjective(Shelf shelf) {
        int count = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 1; j < 6; j++) {
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
