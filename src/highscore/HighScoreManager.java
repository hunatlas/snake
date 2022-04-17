package highscore;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class HighScoreManager {
    
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private static final String URL = "jdbc:derby://localhost:1527/snake_highscore;create=true";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    
    public HighScoreManager(){
        openConnection();
        createTable();
    }
    
    @Override
    protected void finalize() throws Throwable{
        closeConnection();
    }
    
    private void openConnection(){
        try{
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        catch(SQLException | ClassNotFoundException e){
        }
    }
    
    private void createTable(){
        try{
            String createTableSQL = "CREATE TABLE HighScores( "
                    + "id INT NOT NULL GENERATED ALWAYS AS IDENTITY, "
                    + "name VARCHAR(255) NOT NULL, "
                    + "score INT NOT NULL, "
                    + "PRIMARY KEY (id))";
            pst = con.prepareStatement(createTableSQL);
            pst.execute();
        }
        catch(SQLException e){
        }
        finally{
            try{
               pst.close(); 
            }
            catch(SQLException e){
            }
        }
    }
    
    private void closeConnection(){
        try{
            if((con != null) && !con.isClosed())
                con.close();
        }
        catch(SQLException e){
        }
    }
    
    public void putHighScore(String name, int score){
        try{
            pst = con.prepareStatement("INSERT INTO HighScores (name, score) VALUES (?,?)");
            pst.setString(1, name);
            pst.setInt(2, score);
            pst.executeUpdate();
        }
        catch(SQLException e){
        }
        finally{
            try{
                pst.close();
            }
            catch(SQLException e){
            }
        }
    }
    
    public List<HighScore> getSortedHighScores(){
        
        List<HighScore> results = new ArrayList<>();
        
        try{
            String selectSQL = "SELECT name, score FROM HighScores";
            pst = con.prepareStatement(selectSQL);
            rs = pst.executeQuery();
            while(rs.next()){
                String currentName = rs.getString("name");
                int currentScore = rs.getInt("score");
                results.add(new HighScore(currentName, currentScore));
            }
        }
        catch(SQLException e){
        }
        finally{
            try{
                pst.close();
                rs.close();
            }
            catch(SQLException e){
            }
        }
        
        Collections.sort(results);
        if(results.size() > 10)
            results = results.subList(0, 10);
        
        return results;
    }
}
