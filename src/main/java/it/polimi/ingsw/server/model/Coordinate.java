package it.polimi.ingsw.server.model;

import org.json.JSONObject;

public class Coordinate {
    private final int row;
    private final int column;

    public Coordinate (int row, int column){
        this.row = row;
        this.column = column;
    }

    public Coordinate(Coordinate other){
        this.row = other.row;
        this.column = other.column;
    }

    public Coordinate(JSONObject jsonObject) {
        row = jsonObject.getInt("row");
        column = jsonObject.getInt("column");
    }

    public int getRow(){
       return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "row: " + row + ", column: " + column;
    }

    public JSONObject toJSON() {
        return new JSONObject().put("row", row).put("column", column);
    }
}
