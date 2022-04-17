package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import java.net.URL;
import java.util.Random;
import java.util.List;
import model.Model;
import model.Direction;
import highscore.HighScoreManager;
import highscore.HighScore;

public class MainWindow extends JFrame implements ActionListener {
    
    public static Model model;
    private static Board board;
    private static JLabel label;
    private static JMenuItem menuItemNewGame;
    private static Timer timer;
    private static Direction currentDirection;
    private static long startTime;
    private boolean started;
    private HighScoreManager highScoreManager;
    
    public MainWindow(){

        setTitle("Snake");
        setSize(800, 800);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                showExitConfirmation();
            }
        });
        URL url = MainWindow.class.getClassLoader().getResource("view/snake.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menuGame = new JMenu("Game");
        
        menuItemNewGame = new JMenuItem(new AbstractAction("New Game"){
            @Override
            public void actionPerformed(ActionEvent ae){
                dispose();
                new MainWindow();
            }
        });
        menuItemNewGame.setMnemonic(KeyEvent.VK_N);
        menuItemNewGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        
        JMenuItem menuItemHighScores = new JMenuItem(new AbstractAction("HighScores"){
           @Override
           public void actionPerformed(ActionEvent ae){
               showHighScores();
           }
        });
        menuItemHighScores.setMnemonic(KeyEvent.VK_S);
        menuItemHighScores.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        
        JMenuItem menuItemHelp = new JMenuItem(new AbstractAction("Help"){
            @Override
            public void actionPerformed(ActionEvent ae){
                showHelp();
            }
        });
        menuItemHelp.setMnemonic(KeyEvent.VK_H);
        menuItemHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        
        JMenuItem menuItemExit = new JMenuItem(new AbstractAction("Exit"){
            @Override
            public void actionPerformed(ActionEvent ae){
                showExitConfirmation();
            }
        });
        menuItemExit.setMnemonic(KeyEvent.VK_E);
        menuItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        
        menuGame.add(menuItemNewGame);
        menuGame.add(menuItemHelp);
        menuGame.add(menuItemHighScores);
        menuGame.addSeparator();
        menuGame.add(menuItemExit);
        menuBar.add(menuGame);
        setJMenuBar(menuBar);
        
        addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent ke){
                super.keyPressed(ke);
                int kk = ke.getKeyCode();
                switch(kk){
                    case KeyEvent.VK_W: moveNorth(); break;
                    case KeyEvent.VK_UP: moveNorth(); break;
                    case KeyEvent.VK_S: moveSouth(); break;
                    case KeyEvent.VK_DOWN: moveSouth(); break;
                    case KeyEvent.VK_A: moveWest(); break;
                    case KeyEvent.VK_LEFT: moveWest(); break;
                    case KeyEvent.VK_D: moveEast(); break;
                    case KeyEvent.VK_RIGHT: moveEast(); break;
                    case KeyEvent.VK_SPACE:{
                        if(!started){
                            menuItemNewGame.setEnabled(false);
                            startTime = System.currentTimeMillis() / 1000;
                            timer.start();
                            started = true;
                        }
                        break;
                    }
                }
            }  
        });
        
        highScoreManager = new HighScoreManager();

        initGame();
        
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
    
    private void initGame(){
        model = new Model();
        board = new Board(model);
        timer = new Timer(model.getDelay(), this);
        currentDirection = startingDirection();
        started = false;
        
        label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setText("Press Space to start the game");
        
        add(label, BorderLayout.NORTH);
        add(board, BorderLayout.CENTER);
    }
    
    private Direction startingDirection(){
        Random r = new Random();
        Direction d;
        int i = r.nextInt(3);
        switch(i)
        {
            case 0: d = Direction.NORTH; break;
            case 1: d = Direction.SOUTH; break;
            case 2: d = Direction.EAST; break;
            default: d = Direction.EAST;
        }
        return d;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae){
        if(model.isRunning())
            movePlayerAutomatically();
        else
            timer.stop();
    }
    
    private void setLabelText(){
        label.setText("Time: " + elapsedTime() + " Score: " + model.getScore());
    }
    
    private void showHighScores(){
        List<HighScore> highScores = highScoreManager.getSortedHighScores();
        new Table(this, highScores);
    }
    
    private void showHelp(){
        String help = "The goal is to eat as many apples as possible.\n";
        help += "Control the snake with WASD or the arrow keys.\n";
        help += "Try to avoid the border of the level and the stones.\n";
        JOptionPane.showMessageDialog(this, help, "Help", JOptionPane.OK_OPTION);
    }
    
    private void showExitConfirmation()
    {
        int n = JOptionPane.showConfirmDialog(this, "Sure you want to exit?",
                "Exit Confirmation", JOptionPane.YES_NO_OPTION);
        if(n == JOptionPane.YES_OPTION)
            System.exit(0);
    }
    
    private void showEndDialog(){
        String name = JOptionPane.showInputDialog(MainWindow.this, 
                        "The snake is dead, game over!\nInsert your name", "Game Over", 
                        JOptionPane.INFORMATION_MESSAGE);
        int score = model.getScore();
        highScoreManager.putHighScore(name, score);
    }
    
    private void movePlayerAutomatically(){
        boolean b = model.movePlayer(currentDirection);
        if(b)
        {
            board.repaint();
            setLabelText();
        }
        else
        {
            showEndDialog();
            menuItemNewGame.setEnabled(true);
        }
    }
    
    private long elapsedTime(){
        return System.currentTimeMillis() / 1000 - startTime;
    }
    
    private void moveNorth(){
        if(currentDirection != Direction.SOUTH)
            currentDirection = Direction.NORTH;
    }
    
    private void moveSouth(){
        if(currentDirection != Direction.NORTH)
            currentDirection = Direction.SOUTH;
    }
    
    private void moveWest(){
        if(currentDirection != Direction.EAST)
            currentDirection = Direction.WEST;
    }
    
    private void moveEast(){
        if(currentDirection != Direction.WEST)
            currentDirection = Direction.EAST;
    }
    
    private class Table extends JDialog{
        
        private JTable table;
        private JPanel panel;
        private JButton buttonOK;
        private List<HighScore> scores;
        
        public Table(JFrame frame, List<HighScore> scores){
            super(frame, "HighScores", true);
            setSize(400, 400);
            setResizable(true);
            setLayout(new BorderLayout());
            table = new JTable(tableModel);
            panel = new JPanel(new FlowLayout());
            buttonOK = new JButton(new AbstractAction("OK"){
                @Override
                public void actionPerformed(ActionEvent ae){
                    Table.this.dispose();
                }
            });
            panel.add(buttonOK);
            add("Center", new JScrollPane(table));
            add("South", panel);
            this.scores = scores;
            setVisible(true);
        }
        
        private AbstractTableModel tableModel = new AbstractTableModel()
        {
            private final String[] names = {"Name", "Score"};
            
            @Override
            public int getRowCount() {return scores.size();}
            
            @Override
            public int getColumnCount() {return names.length;}
            
            @Override
            public Object getValueAt(int rowIndex, int columnIndex){
                switch(columnIndex)
                {
                    case 0: return scores.get(rowIndex).getName();
                    case 1: return scores.get(rowIndex).getScore();
                }
                return null;
            }
            
            @Override
            public String getColumnName(int column){
                return names[column];
            }
        };
    }
    
    public static void main(String[] args)
    {
       new MainWindow();
    }
}
