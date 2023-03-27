package it.polimi.ingsw.server;

public class PatternSeven extends CollectiveObjectiveCard {
    public boolean checkObjective(Shelf shelf) {
        int j = 0, k = 0;

        for (int i = 0; i < 4; i++) {
            Coordinate tileCoordinate = new Coordinate(i, j);
            Coordinate incrementedCoordinate = new Coordinate(i + 1, j + 1);
            if (shelf.getTile(tileCoordinate) != shelf.getTile(incrementedCoordinate)) {
                break;
            }
            if (i == 3) {
                return true;
            }
            j++;
        }

        for (int i = 4; i > 0 ; i--) {
            Coordinate tileCoordinate = new Coordinate(k, i);
            Coordinate incrementedCoordinate = new Coordinate(k + 1, i - 1);
            if (shelf.getTile(tileCoordinate) != shelf.getTile(incrementedCoordinate)) {
                break;
            }
            if (i == 1) {
                return true;
            }
            k++;
        }

        j = 0;

        for (int i = 1; i < 5; i ++) {
            Coordinate tileCoordinate = new Coordinate(i, j);
            Coordinate incrementedCoordinate = new Coordinate(i + 1, j + 1);
            if (shelf.getTile(tileCoordinate) != shelf.getTile(incrementedCoordinate)) {
                break;
            }
            if (i == 4) {
                return true;
            }
            j++;
        }

        k = 1;

        for (int i = 4; i > 0; i--){
            Coordinate tileCoordinate = new Coordinate(k, i);
            Coordinate incrementedCoordinate = new Coordinate(k + 1, i - 1);
            if (shelf.getTile(tileCoordinate) != shelf.getTile(incrementedCoordinate)) {
                break;
            }
            if (k == 4) {
                return true;
            }
            k++;
        }

        return false;
    }
}
