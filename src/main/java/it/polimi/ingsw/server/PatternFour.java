package it.polimi.ingsw.server;

import java.util.HashSet;
import java.util.Set;

public class PatternFour extends CollectiveObjectiveCard {
    public boolean checkObjective(Shelf shelf) {
        for (int i = 0; i <= 4; i++) {
            for (int j = 0; j <= 3; j++) {
                // Check if the 2x2 square starting at (i,j) contains 4 of the same enum value
                Coordinate tileCoordinate = new Coordinate(i, j);
                Set<Tile> squareValues = new HashSet<>();
                squareValues.add(shelf.getTile(tileCoordinate));

                Coordinate tileCoordinateBottomLeft = new Coordinate(i+1, j);
                squareValues.add(shelf.getTile(tileCoordinateBottomLeft));

                Coordinate tileCoordinateTopRight = new Coordinate(i, j+1);
                squareValues.add(shelf.getTile(tileCoordinateTopRight));

                Coordinate tileCoordinateBottomRight = new Coordinate(i+1, j+1);
                squareValues.add(shelf.getTile(tileCoordinateBottomRight));

                if (squareValues.size() == 1) {
                    // Found a group of 4, so iterate over the remaining 2x2 squares to find another group
                    for (int k = i; k <= 4; k++) {
                        for (int l = 0; l <= 3; l++) {
                            if (k == i && l < j + 2) {
                                // Skip the 2x2 square we already found
                                continue;
                            }
                            // Check if the 2x2 square starting at (k,l) contains 4 of the same enum value
                            Coordinate otherTileCoordinate = new Coordinate(k, l);
                            Set<Tile> otherSquareValues = new HashSet<>();
                            otherSquareValues.add(shelf.getTile(otherTileCoordinate));

                            Coordinate otherTileCoordinateBottomLeft = new Coordinate(k+1, l);
                            otherSquareValues.add(shelf.getTile(otherTileCoordinateBottomLeft));

                            Coordinate otherTileCoordinateTopRight = new Coordinate(k, l+1);
                            otherSquareValues.add(shelf.getTile(otherTileCoordinateTopRight));

                            Coordinate otherTileCoordinateBottomRight = new Coordinate(k+1, l+1);
                            otherSquareValues.add(shelf.getTile(otherTileCoordinateBottomRight));
                            if (otherSquareValues.size() == 1) {
                                // Found another group of 4, so we're done
                                return true;
                            }
                        }
                    }
                }
            }
        }
        // Didn't find two groups of 4
        return false;
    }
}

