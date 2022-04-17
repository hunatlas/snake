package model;

public class Player extends GameItem{
    
    public int x;
    public int y;
    public boolean head;
    
    public Player(int x, int y, boolean head){
        super();
        this.x = x;
        this.y = y;
        this.head = head;
    }
    
    public Player movePlayer(Direction d){
        return new Player(x + d.x, y + d.y, true);
    }
    
    public void setHead(boolean head){
        this.head = head;
    }
}
