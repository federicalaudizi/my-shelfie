package it.polimi.ingsw.server.model;

import org.json.JSONObject;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Abstract class for Collective Objective cards
 *
 * @author Federica
 */
public abstract class CollectiveObjectiveCard {
    //TODO: It seems like the same collective objectives are picked for the same game

    public abstract boolean checkObjective(Shelf shelf);

    public static CollectiveObjectiveCard getRandomCard() {
        List<Class<? extends CollectiveObjectiveCard>> subclasses = getAllPossibleCards();

        Random random = new Random();

        int index = random.nextInt(subclasses.size());

        Class<? extends CollectiveObjectiveCard> subclass = subclasses.get(index);

        try {
            return subclass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static CollectiveObjectiveCard getRandomCard(CollectiveObjectiveCard other) {
        List<Class<? extends CollectiveObjectiveCard>> subclasses = getAllPossibleCards();

        Random random = new Random();

        int index = random.nextInt(subclasses.size());

        Class<? extends CollectiveObjectiveCard> subclass = subclasses.get(index);

        while (other.equals(subclass)) {
            index = random.nextInt(subclasses.size());
            subclass = subclasses.get(index);
        }

        try {
            return subclass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<Class<? extends CollectiveObjectiveCard>> getAllPossibleCards() {
        List<Class<? extends CollectiveObjectiveCard>> subclasses = new ArrayList<>();

        Reflections reflections = new Reflections("it.polimi.ingsw.server");

        Set<Class<? extends CollectiveObjectiveCard>> allClasses = reflections.getSubTypesOf(CollectiveObjectiveCard.class);

        for (Class<? extends CollectiveObjectiveCard> clazz : allClasses) {
            if (!Modifier.isAbstract(clazz.getModifiers())) {
                subclasses.add(clazz);
            }
        }
        return subclasses;
    }

    /**
     * Concrete class for pattern eight: Four lines each formed by 5 tiles of maximum three different types.
     *
     * @author Federica
     */
    static class PatternEight extends CollectiveObjectiveCard {
        public boolean checkObjective(Shelf shelf) {
            int count = 0;

            for (int i = 0; i < 6; i++) {
                Set<Tile> squareValues = new HashSet<>();
                for (int j = 0; j < 5; j++) {
                    Coordinate tileCoordinate = new Coordinate(j, i);
                    if (shelf.getTile(tileCoordinate) != Tile.EMPTY) {
                        squareValues.add(shelf.getTile(tileCoordinate));
                        if (squareValues.size() <= 3 & j == 4) {
                            count++;
                            if (count == 4) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
    }

    /**
     * Concrete class for pattern eleven: PatternOne: Five tiles of the same type forming an X.
     *
     * @author Federica
     */
    static class PatternEleven extends CollectiveObjectiveCard {
        public boolean checkObjective(Shelf shelf) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 2; j++) {
                    Coordinate upLeft = new Coordinate(i, j);
                    Coordinate middle = new Coordinate(i + 1, j + 1);
                    Coordinate upRight = new Coordinate(i, j + 2);
                    Coordinate bottomLeft = new Coordinate(i + 2, j);
                    Coordinate bottomRight = new Coordinate(i + 2, j + 2);
                    if (shelf.getTile(upLeft) == shelf.getTile(upRight) & shelf.getTile(upLeft) == shelf.getTile(middle)
                            & shelf.getTile(upLeft) == shelf.getTile(bottomLeft) & shelf.getTile(upLeft) == shelf.getTile(bottomRight))
                        return true;
                }
            }
            return false;
        }
    }

    /**
     * Concrete class for pattern five: Three columns each formed by 6 tiles of maximum 3 different types.
     *
     * @author Federica
     */
    static class PatternFive extends CollectiveObjectiveCard {
        @Override
        public boolean checkObjective(Shelf shelf) {
            int count = 0;
            for (int col = 0; col < 5; col++) {
                Set<Tile> distinctValues = new HashSet<>();
                for (int row = 0; row < 6; row++) {
                    Coordinate tileCoordinate = new Coordinate(col, row);
                    if (shelf.getTile(tileCoordinate) != Tile.EMPTY) {
                        distinctValues.add(shelf.getTile(tileCoordinate));
                    }
                }
                if (distinctValues.size() <= 3) {
                    count++;
                    if (count == 3) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     * Concrete class for pattern four: Two groups each containing 4 tiles of the same type in a 2x2 square.
     *
     * @author Federica
     */
    static class PatternFour extends CollectiveObjectiveCard {
        public boolean checkObjective(Shelf shelf) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j <= 3; j++) {
                    // Check if the 2x2 square starting at (i,j) contains 4 of the same enum value
                    Coordinate tileCoordinate = new Coordinate(i, j);
                    Set<Tile> squareValues = new HashSet<>();
                    if (shelf.getTile(tileCoordinate) != Tile.EMPTY) {
                        squareValues.add(shelf.getTile(tileCoordinate));
                    }

                    Coordinate tileCoordinateBottomLeft = new Coordinate(i + 1, j);
                    if (shelf.getTile(tileCoordinateBottomLeft) != Tile.EMPTY) {
                        squareValues.add(shelf.getTile(tileCoordinateBottomLeft));
                    }

                    Coordinate tileCoordinateTopRight = new Coordinate(i, j + 1);
                    if (shelf.getTile(tileCoordinateTopRight) != Tile.EMPTY) {
                        squareValues.add(shelf.getTile(tileCoordinateTopRight));
                    }

                    Coordinate tileCoordinateBottomRight = new Coordinate(i + 1, j + 1);
                    if (shelf.getTile(tileCoordinateBottomRight) != Tile.EMPTY) {
                        squareValues.add(shelf.getTile(tileCoordinateBottomRight));
                    }

                    if (squareValues.size() == 1) {
                        // Found a group of 4, so iterate over the remaining 2x2 squares to find another group
                        for (int k = i; k < 5; k++) {
                            for (int l = 0; l <= 3; l++) {
                                if (k == i && l < j + 2) {
                                    // Skip the 2x2 square we already found
                                    continue;
                                }
                                // Check if the 2x2 square starting at (k,l) contains 4 of the same enum value
                                Coordinate otherTileCoordinate = new Coordinate(l, k);
                                Set<Tile> otherSquareValues = new HashSet<>();
                                if (shelf.getTile(otherTileCoordinate) != Tile.EMPTY) {
                                    otherSquareValues.add(shelf.getTile(otherTileCoordinate));
                                }

                                Coordinate otherTileCoordinateBottomLeft = new Coordinate(l + 1, k);
                                if (shelf.getTile(otherTileCoordinateBottomLeft) != Tile.EMPTY) {
                                    otherSquareValues.add(shelf.getTile(otherTileCoordinateBottomLeft));
                                }

                                Coordinate otherTileCoordinateTopRight = new Coordinate(l, k + 1);
                                if (shelf.getTile(otherTileCoordinateTopRight) != Tile.EMPTY) {
                                    otherSquareValues.add(shelf.getTile(otherTileCoordinateTopRight));
                                }

                                Coordinate otherTileCoordinateBottomRight = new Coordinate(l + 1, k + 1);
                                if (shelf.getTile(otherTileCoordinateBottomRight) != Tile.EMPTY) {
                                    otherSquareValues.add(shelf.getTile(otherTileCoordinateBottomRight));
                                }
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

    /**
     * Concrete class for pattern nine: Two columns each formed by 6 different types of tiles.
     *
     * @author Federica
     */
    static class PatternNine extends CollectiveObjectiveCard {
        public boolean checkObjective(Shelf shelf) {
            int count = 0;

            for (int i = 0; i < 5; i++) {
                Set<Tile> column = new HashSet<>();
                for (int j = 0; j < 6; j++) {
                    Coordinate tileCoordinate = new Coordinate(i, j);
                    if (shelf.getTile(tileCoordinate) != Tile.EMPTY) {
                        column.add(shelf.getTile(tileCoordinate));
                        if (column.size() == 6) {
                            count++;
                            if (count == 2) {
                                return true;
                            }
                        }
                    }
                }

            }

            return false;
        }
    }

    /**
     * Concrete class for pattern one: Six groups each containing at least two tiles of the same type.
     *
     * @author Federica
     */
    static class PatternOne extends CollectiveObjectiveCard {
        @Override
        public boolean checkObjective(Shelf shelf) {
            int count = 0;

            //check vertically
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 6; j++) {
                    Coordinate tileCoordinate = new Coordinate(i, j);
                    Coordinate tileCoordinateIncremented = new Coordinate(i + 1, j);
                    if (shelf.getTile(tileCoordinate) == shelf.getTile(tileCoordinateIncremented) & shelf.getTile(tileCoordinate) != Tile.EMPTY & shelf.getTile(tileCoordinateIncremented) != Tile.EMPTY) {
                        count++;
                        if (count == 6) {
                            return true;
                        }
                    }
                }
            }

            //check horizontally
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    Coordinate tileCoordinate = new Coordinate(i, j);
                    Coordinate tileCoordinateIncremented = new Coordinate(i, j + 1);
                    if (shelf.getTile(tileCoordinate) == shelf.getTile(tileCoordinateIncremented) & shelf.getTile(tileCoordinate) != Tile.EMPTY & shelf.getTile(tileCoordinateIncremented) != Tile.EMPTY) {
                        count++;
                        if (count == 6) {
                            return true;
                        }
                    }
                }
            }

            return false;
        }
    }

    /**
     * Concrete class for pattern seven: Five tiles of the same type forming a diagonal.
     *
     * @author Federica
     */
    static class PatternSeven extends CollectiveObjectiveCard {
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

            for (int i = 4; i > 0; i--) {
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

            for (int i = 1; i < 5; i++) {
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

            for (int i = 4; i > 0; i--) {
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

    /**
     * Concrete class for pattern six: Eight tiles of the same type.
     *
     * @author Federica
     */
    static class PatternSix extends CollectiveObjectiveCard {
        public boolean checkObjective(Shelf shelf) {
            int count = 0;
            for (Tile type : Tile.values()) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 6; j++) {
                        Coordinate tileCoordinate = new Coordinate(i, j);
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

    /**
     * Concrete class for pattern ten: Two lines each formed by 5 different types of tiles.
     *
     * @author Federica
     */
    static class PatternTen extends CollectiveObjectiveCard {
        public boolean checkObjective(Shelf shelf) {
            int count = 0;
            for (int j = 0; j < 6; j++) {
                Set<Tile> column = new HashSet<>();
                for (int i = 0; i < 5; i++) {
                    Coordinate tileCoordinate = new Coordinate(i, j);
                    if (shelf.getTile(tileCoordinate) != Tile.EMPTY) {
                        column.add(shelf.getTile(tileCoordinate));
                        if (column.size() == 5) {
                            count++;
                            if (count == 2) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
    }

    /**
     * Concrete class for pattern three: Four tiles of the same type in the four corners of the bookshelf.
     *
     * @author Federica
     */
    static class PatternThree extends CollectiveObjectiveCard {
        @Override
        public boolean checkObjective(Shelf shelf) {
            Coordinate upperLeft = new Coordinate(0, 0);
            Coordinate upperRight = new Coordinate(0, 5);
            Coordinate bottomLeft = new Coordinate(4, 0);
            Coordinate bottomRight = new Coordinate(4, 5);

            return shelf.getTile(upperLeft) == shelf.getTile(upperRight) && shelf.getTile(bottomLeft) == shelf.getTile(bottomRight)
                    && shelf.getTile(upperLeft) == shelf.getTile(bottomRight) && shelf.getTile(upperRight) == shelf.getTile(bottomRight)
                    && shelf.getTile(upperLeft) == shelf.getTile(bottomLeft) && shelf.getTile(bottomLeft) == shelf.getTile(upperRight);

        }
    }

    /**
     * Concrete class for pattern twelve: Five columns of increasing or decreasing height.
     *
     * @author Federica
     */
    static class PatternTwelve extends CollectiveObjectiveCard {
        public boolean checkObjective(Shelf shelf) {

            int[] countColumn = {0, 0, 0, 0, 0};
            int k = 0;

            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 6; j++) {
                    Coordinate tileCoordinate = new Coordinate(i, j);
                    if (shelf.getTile(tileCoordinate) != Tile.EMPTY) {
                        countColumn[k]++;
                    }
                }
                k++;
                if ((countColumn[0] == 1 & countColumn[1] == 2 & countColumn[2] == 3 & countColumn[3] == 4 & countColumn[4] == 5) ||
                        (countColumn[0] == 5 & countColumn[1] == 4 & countColumn[2] == 3 & countColumn[3] == 2 & countColumn[4] == 1)
                        || (countColumn[0] == 2 & countColumn[1] == 3 & countColumn[2] == 4 & countColumn[3] == 5 & countColumn[4] == 6)
                        || (countColumn[0] == 6 & countColumn[1] == 5 & countColumn[2] == 4 & countColumn[3] == 3 & countColumn[4] == 2)) {
                    return true;
                }

            }

            return false;
        }
    }

    /**
     * Concrete class for pattern two: Four groups each containing al least 4 tiles of the same type.
     *
     * @author Federica
     */
    static class PatternTwo extends CollectiveObjectiveCard {
        public boolean checkObjective(Shelf shelf) {
            int outerCount = 0;
            int[][] visited = new int[5][6];
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 6; j++) {
                    if (hasFourAdjacentTilesStartingAt(i, j, shelf, visited)) {
                        outerCount++;
                        if (outerCount == 4) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        private boolean hasFourAdjacentTilesStartingAt(int row, int col, Shelf shelf, int[][] visited) {
            Tile tileType = shelf.getTile(new Coordinate(row, col));
            int count = dfs(row, col, tileType, visited, shelf);
            if (count >= 4) {
                count = 0;
                return true;
            }
            count = 0;
            return false;
        }

        private int dfs(int row, int col, Tile tileType, int[][] visited, Shelf shelf) {
            Coordinate tileCoordinate = new Coordinate(row, col);

            if (row < 0 || row >= 5 || col < 0 || col >= 6) {
                return 0;
            }

            if (shelf.getTile(tileCoordinate) != tileType || shelf.getTile(tileCoordinate) == Tile.EMPTY) {
                return 0;
            }

            if (visited[row][col] > 0) {
                return 0; // already visited
            }

            visited[row][col] = 1; // mark as visiting
            int count = 1;
            count += dfs(row - 1, col, tileType, visited, shelf);
            count += dfs(row + 1, col, tileType, visited, shelf);
            count += dfs(row, col - 1, tileType, visited, shelf);
            count += dfs(row, col + 1, tileType, visited, shelf);
            visited[row][col] = 2; // mark as visited
            return count;
        }

    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("cardType", this.getClass().getSimpleName());
        return obj;
    }

    /**
     * Returns a pattern from a collectiveObjective
     *
     * @author Federica
     * @return CollectiveObjectiveCard
     * @param json
     *
     */
    public static CollectiveObjectiveCard fromJson(JSONObject json) {
        String type = json.getString("cardType");

        return switch (type) {
            case "PatternOne" -> new PatternOne();
            case "PatternTwo" -> new PatternTwo();
            case "PatternThree" -> new PatternThree();
            case "PatternFour" -> new PatternFour();
            case "PatternFive" -> new PatternFive();
            case "PatternSix" -> new PatternSix();
            case "PatternSeven" -> new PatternSeven();
            case "PatternEight" -> new PatternEight();
            case "PatternNine" -> new PatternNine();
            case "PatternTen" -> new PatternTen();
            case "PatternEleven" -> new PatternEleven();
            case "PatternTwelve" -> new PatternTwelve();
            default -> throw new IllegalArgumentException("Invalid card type: " + type);
        };
    }
}
