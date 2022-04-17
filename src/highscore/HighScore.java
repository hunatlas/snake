package highscore;

public class HighScore implements Comparable<HighScore>{
    
    private final String name;
    private final int score;
    
    public HighScore(String name, int score){
        this.name = name;
        this.score = score;
    }
    
    public String getName(){
        return name;
    }
    
    public int getScore(){
        return score;
    }
    
    @Override
    public int compareTo(HighScore other){
        if(this.score >= other.getScore()) return -1;
        else return 1;
    }
}
