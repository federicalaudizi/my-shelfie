package it.polimi.ingsw.server;

public class PatternTwo extends CollectiveObjectiveCard {

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

