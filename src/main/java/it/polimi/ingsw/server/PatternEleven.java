package it.polimi.ingsw.server;

public class PatternEleven extends CollectiveObjectiveCard {
    public boolean checkObjective(Shelf shelf) {
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 2; j++){
                Coordinate upLeft = new Coordinate(i, j);
                Coordinate middle = new Coordinate(i+1, j+1);
                Coordinate upRight = new Coordinate(i, j + 2);
                Coordinate bottomLeft = new Coordinate(i+2, j);
                Coordinate bottomRight = new Coordinate(i+2, j+2);
                if (shelf.getTile(upLeft) == shelf.getTile(upRight) & shelf.getTile(upLeft) == shelf.getTile(middle)
                   & shelf.getTile(upLeft) == shelf.getTile(bottomLeft) & shelf.getTile(upLeft) == shelf.getTile(bottomRight))
                    return true;
            }
        }
        return false;
    }
}
