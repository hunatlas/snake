package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.BasicStroke;
import javax.swing.JPanel;
import model.Model;
import model.Player;
import model.Apple;
import model.Rock;

public class Board extends JPanel{
    
    private final Model model;
    private final int TILESIZE = 15;
    
    public Board(Model model){
        this.model = model;
        Dimension dim = new Dimension(model.getGameSize() * TILESIZE, model.getGameSize() * TILESIZE);
        setSize(dim);
        setPreferredSize(dim);
        setMaximumSize(dim);
    }
    
    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        for(int i = 0; i < model.getGameSize(); ++i){
            for(int j = 0; j < model.getGameSize(); ++j){
                if(model.getGameItem(i, j) instanceof Player){
                    Player p = (Player)model.getGameItem(i, j);
                    if(p.head) g2d.setColor(new Color(7, 130, 75));
                    else g2d.setColor(Color.GREEN);
                }
                else if(model.getGameItem(i, j) instanceof Apple)
                    g2d.setColor(Color.RED);
                else if(model.getGameItem(i, j) instanceof Rock)
                    g2d.setColor(Color.LIGHT_GRAY);
                else
                    g2d.setColor(Color.BLACK);
                g2d.fillRect(i * TILESIZE, j * TILESIZE, TILESIZE, TILESIZE);
            }
        }
    }
}
