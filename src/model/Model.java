package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Model{
    
    private final GameItem[][] board;
    private final List<Player> player;
    private Player head;
    private int score;
    private final Random random;
    private boolean running = true;
    
    private final int GAMESIZE = 20;
    private final int NUMBEROFROCKS = 10;
    private int DELAY = 200;
    
    public Model(){
        player = new ArrayList<>();
        score = 0;
        random = new Random();
        board = new GameItem[GAMESIZE][GAMESIZE];
        
        head = new Player(GAMESIZE/2-1, GAMESIZE/2-1, true);
        player.add(head);
        player.add(new Player(head.x-1, head.y, false));
        
        placePlayer();
        generateRocks();
        generateApple();
    }
    
    public boolean movePlayer(Direction d){
        head = head.movePlayer(d);
        if(head.x < 0 || head.x == GAMESIZE || head.y < 0 || head.y == GAMESIZE
           || isRockHit() || isSelfHit()){
            running = false;
            return false;
        }
        else
        {
            if(isAppleEaten())
            {
               ++score;
               player.get(0).setHead(false);
               player.add(0, head);
               deletePlayer();
               placePlayer();
               generateApple();
            }
            else
            {
                player.remove(player.size()-1);
                player.get(0).setHead(false);
                player.add(0, head);
                deletePlayer();
                placePlayer();
            }
            
            return true;
        }
    }
    
    private boolean isAppleEaten(){
        return board[head.x][head.y] instanceof Apple;
    }
    
    private boolean isRockHit(){
        return board[head.x][head.y] instanceof Rock;
    }
    
    private boolean isSelfHit(){
        return board[head.x][head.y] instanceof Player;
    }
    
    private void generateRocks(){
        for(int i = 0; i < NUMBEROFROCKS; ++i)
        {
            int x = random.nextInt(GAMESIZE);
            int y = random.nextInt(GAMESIZE);
            while(x == 0 || y == 0 || x == GAMESIZE-1 || y == GAMESIZE-1 ||
                  (x > GAMESIZE/2-4 && x < GAMESIZE/2+2) ||
                  (y > GAMESIZE/2-4 && y < GAMESIZE/2+2) ||
                  areRocksAround(x, y))
            {
                x = random.nextInt(GAMESIZE);
                y = random.nextInt(GAMESIZE);
            }
            board[x][y] = new Rock();
        }
    }
    
    private boolean areRocksAround(int x, int y){
        return (y-1 >= 0 && board[x][y-1] instanceof Rock) ||
               (y-2 >= 0 && board[x][y-2] instanceof Rock) ||
               (y+1 < GAMESIZE && board[x][y+1] instanceof Rock) ||
               (y+2 < GAMESIZE && board[x][y+2] instanceof Rock) ||
               (x-1 >= 0 && board[x-1][y] instanceof Rock) ||
               (x-2 >= 0 && board[x-2][y] instanceof Rock) ||
               (x+1 < GAMESIZE && board[x+1][y] instanceof Rock) ||
               (x+2 < GAMESIZE && board[x+2][y] instanceof Rock) ||
               (y-1 >= 0 && x-1 >= 0 && board[x-1][y-1] instanceof Rock) ||
               (y-1 >= 0 && x+1 < GAMESIZE && board[x+1][y-1] instanceof Rock) ||
               (y+1 < GAMESIZE && x-1 >= 0 && board[x-1][y+1] instanceof Rock) ||
               (y+1 < GAMESIZE && x+1 < GAMESIZE && board[x+1][y+1] instanceof Rock);
    }
    
    private void generateApple(){
        int x = random.nextInt(GAMESIZE);
        int y = random.nextInt(GAMESIZE);
        while(board[x][y] != null)
        {
            x = random.nextInt(GAMESIZE);
            y = random.nextInt(GAMESIZE);
        }
        board[x][y] = new Apple();
    }
    
    private void deletePlayer(){
        for(int i = 0; i < GAMESIZE; ++i)
            for(int j = 0; j < GAMESIZE; ++j)
                if(board[i][j] instanceof Player)
                    board[i][j] = null;
    }
    
    private void placePlayer(){
        for(Player p : player)
            board[p.x][p.y] = p;
    }
    
    public boolean isRunning(){
        return running;
    }
    
    public int getGameSize(){
        return GAMESIZE;
    }
    
    public int getScore(){
        return score;
    }
    
    public int getDelay(){
        return DELAY;
    }
    
    public GameItem getGameItem(int x, int y){
        return board[x][y];
    }
}
