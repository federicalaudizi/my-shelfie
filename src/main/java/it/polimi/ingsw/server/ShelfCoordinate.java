package it.polimi.ingsw.server;

public class ShelfCoordinate {
    private int x;
    private int y;
    private int MAX_X = 4;
    private int MAX_Y = 5;

    public ShelfCoordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
       return y;
    }
}
