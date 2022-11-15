package at.ddb.teamwork;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.awt.Font;
import java.awt.event.*;


public class Level extends JFrame {

    protected final Color startColor = new Color(43,255,0);
    protected final Color trackColor = new Color(255,255,255);
    protected final Color goalColor = new Color(255,0,21);

    protected Game game;
    protected String imagePath;
    protected boolean gameStarted;  
    protected int timeoutSeconds;  
    protected JLabel timerLabel;
    protected List<GameElement> elements;
    

    
    public Level(Game game, int number, String imagePath, int timeoutSeconds) {
        super("Level " + number);

        this.game = game;
        this.imagePath = imagePath;
        this.timeoutSeconds = timeoutSeconds;

        this.elements = new ArrayList<GameElement>();

        this.gameStarted = false;
        
    }

    public void addElement(GameElement e) {
        this.elements.add(e);
    }

    public void display() {
        Level t = this;

        this.initScreen();
        this.displayTime();

        this.setVisible(true);

        this.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                int x = e.getX();
                int y = e.getY();
                t.mouseMoved(x, y);
            }
        });
    }


    private void  initScreen()  {

        this.setLayout(null);
        this.setSize(1600, 900);

        /* fullscreen */
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); 

        /* withut frame, buttons, ... */
        this.setUndecorated(true);

        /* Background image */
        JLabel bgImageLabel;
        try {
            bgImageLabel = new JLabel(new ImageIcon(ImageIO.read(new File(this.imagePath))));
            bgImageLabel.setBounds(0,0,1600,900);
            this.setContentPane(bgImageLabel);
        } catch (IOException e) {
            // falls Bild nicht geladen werden kann
            e.printStackTrace();
        }

        /* Username and timer panel */
        JPanel userPanel = new JPanel();
        userPanel.setLayout(null);
        userPanel.setLocation(1200, 50);
        userPanel.setSize(400, 150);
        userPanel.setBackground(new Color(0,0,0,0)); /* transparent background color */
        this.add(userPanel);

        // Username Label
        JLabel userNameLabel = new JLabel(this.game.getUserName());
        userNameLabel.setLocation(0, 0);
        userNameLabel.setSize(200, 40);
        userNameLabel.setFont(new Font("Verdana", Font.BOLD, 22)); /* font name and size */
        userNameLabel.setForeground(new Color(255,255,255));
        userPanel.add(userNameLabel);

        // Timer Label
        timerLabel = new JLabel("");
        timerLabel.setLocation(0, 40);
        timerLabel.setSize(200, 60);
        timerLabel.setFont(new Font("Verdana", Font.BOLD, 36)); /* font name and size */
        timerLabel.setForeground(new Color(255,255,255));
        userPanel.add(timerLabel);

        // add game elements
        for (GameElement e : elements) {
            this.add(e);
        }        
        
    }

    private void mouseMoved(int x, int y) {
        Robot robot;
        try {
            robot = new Robot();
            Color color = robot.getPixelColor(x, y);
            //String text = String.format("x: %d, y: %d, color: %s", x, y, color.toString());
            //System.out.println(text);

            if(!this.gameStarted) {
                if(color.equals(this.startColor)) {
                    this.start();
                }
            } else {
                if(color.equals(this.trackColor) || color.equals(this.startColor)) {
                    /* do nothing, mouse is on track */
                }
                else if(color.equals(this.goalColor)) {
                    this.finish();
                } else {
                    this.gameOver();
                }
            }


        } catch (AWTException e1) {
            e1.printStackTrace();
        }
    }

    protected void displayTime() {

        Date date = new Date((long)(this.timeoutSeconds*1000));
        String formattedTime = new SimpleDateFormat("mm:ss:SS").format(date);

        System.out.println(formattedTime);

        this.timerLabel.setText(formattedTime);
    }

    public void start() {
        this.gameStarted = true;
        System.out.println("Game started.");
        this.startCounter();
        this.startGameElements();
    }

    public void stop() {
        this.gameStarted = false;
        this.stopGameElements();
    }


    private void finish() {
        this.stop();
        System.out.println("Finish!");
        JOptionPane.showMessageDialog(this, "You did it!!!");
        this.setVisible(false); // hide level frame
        this.dispose(); //Destroy Level, will be created again
    }

    private void gameOver() {
        this.stop();
        System.out.println("Game Over!");
        JOptionPane.showMessageDialog(this, "GAME OVER");
        this.setVisible(false); // hide level frame
        this.dispose(); //Destroy Level, will be created again
    }

    private void startCounter() {

    }

    Timer gameElementsTimer;
    private void startGameElements() {
        gameElementsTimer = new Timer(40, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for (GameElement el : elements) {
                    el.draw();
                }   
            }}
        
        );

        gameElementsTimer.start();
    }

    private void stopGameElements() {
        this.gameElementsTimer.stop();
    }

    
}
