package it.polimi.ingsw.server;

import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Abstract class for Collective Objective cards
 *
 * @author Federica
 */
public abstract class CollectiveObjectiveCard {
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
     * Concrete class for pattern eight
     *
     * @author Federica
     */
    static class PatternEight extends CollectiveObjectiveCard {
        public boolean checkObjective(Shelf shelf) {
            Set<Tile> squareValues = new HashSet<>();
            int count = 0;
            for (int i = 0; i < 6; i++){
                for (int j = 0; j < 5; j++){
                    Coordinate tileCoordinate = new Coordinate(i, j);
                    // devo fare check che sia diverso da unknown
                    squareValues.add(shelf.getTile(tileCoordinate));
                    if (squareValues.size() <= 3){
                        count++;
                        if (count == 4){
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    /**
     * Concrete class for pattern eleven
     *
     * @author Federica
     */
    static class PatternEleven extends CollectiveObjectiveCard {
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

    /**
     * Concrete class for pattern five
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
                    Coordinate tileCoordinate = new Coordinate(row, col);
                    distinctValues.add(shelf.getTile(tileCoordinate));
                }
                if (distinctValues.size() <= 3) {
                    count++;
                    if(count == 3){
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     * Concrete class for pattern four
     *
     * @author Federica
     */
    static class PatternFour extends CollectiveObjectiveCard {
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

    /**
     * Concrete class for pattern nine
     *
     * @author Federica
     */
    static class PatternNine extends CollectiveObjectiveCard {
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

    /**
     * Concrete class for pattern one
     *
     * @author Federica
     */
    static class PatternOne extends CollectiveObjectiveCard {
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

    /**
     * Concrete class for pattern seven
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

    /**
     * Concrete class for pattern six
     *
     * @author Federica
     */
    static class PatternSix extends CollectiveObjectiveCard {
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

    /**
     * Concrete class for pattern ten
     *
     * @author Federica
     */
    static class PatternTen extends CollectiveObjectiveCard {
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

    /**
     * Concrete class for pattern three
     *
     * @author Federica
     */
    static class PatternThree extends CollectiveObjectiveCard {
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

    /**
     * Concrete class for pattern twelve
     *
     * @author Federica
     */
    static class PatternTwelve extends CollectiveObjectiveCard {
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

    /**
     * Concrete class for pattern two
     *
     * @author Federica
     */
    static class PatternTwo extends CollectiveObjectiveCard {

        public boolean checkObjective(Shelf shelf) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 5; j++) {
                    if (hasFourGroupsOfFourStartingAt(i, j, shelf)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean hasFourGroupsOfFourStartingAt(int row, int col, Shelf shelf) {

            Coordinate tileCoordinate = new Coordinate(row, col);
            Tile tileType = shelf.getTile(tileCoordinate);
            int[][] visited = new int[6][5];
            int count = dfs(row, col, tileType, visited, shelf);
            if (count < 4) {
                return false;
            }
            visited[row][col] = 2; // mark as visited
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 5; j++) {
                    if (visited[i][j] == 1 && dfs(i, j, tileType, visited, shelf) < 4) {
                        return false;
                    }
                }
            }
            return true;
        }


        private int dfs(int row, int col, Tile tileType, int[][] visited, Shelf shelf) {
            if (row < 0 || row >= 6 || col < 0 || col >= 5) {
                return 0;
            }
            Coordinate tileCoordinate = new Coordinate(row, col);

            if (shelf.getTile(tileCoordinate) != tileType) {
                return 0;
            }
            if (visited[row][col] > 0) {
                return visited[row][col] - 1; // already visited
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
}
