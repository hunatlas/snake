package model;

public enum Direction {
    SOUTH(0, 1), WEST(-1, 0), NORTH(0, -1), EAST(1, 0);
    
    Direction(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public final int x;
    public final int y;
}
