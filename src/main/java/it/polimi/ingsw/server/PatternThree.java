package it.polimi.ingsw.server;

public class PatternThree extends CollectiveObjectiveCard {
    @Override
    public boolean checkObjective(Shelf shelf) {
        Coordinate upperLeft = new Coordinate(0, 0);
        Coordinate upperRight = new Coordinate(0, 4);
        Coordinate bottomLeft = new Coordinate(5, 0);
        Coordinate bottomRight = new Coordinate(5, 5);

        return shelf.getTile(upperLeft) == shelf.getTile(upperRight) && shelf.getTile(bottomLeft) == shelf.getTile(bottomRight)
                && shelf.getTile(upperLeft) == shelf.getTile(bottomRight) && shelf.getTile(upperRight) == shelf.getTile(bottomRight)
                && shelf.getTile(upperLeft) == shelf.getTile(bottomLeft) && shelf.getTile(bottomLeft) == shelf.getTile(upperRight);

    }
}
