package at.ddb.teamwork;

import java.io.File;
import java.io.IOException;
import java.net.PortUnreachableException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Cursor;

public class Level extends JFrame {

    private Timer countDownTimer;
    private long startTimeMillis;

    protected final Color startColor = new Color(43,255,0);
    protected final Color trackColor = new Color(255,255,255);
    protected final Color goalColor = new Color(255,0,21);

    protected Game game;
    protected String imagePath;
    protected boolean gameStarted;  
    protected int timeoutSeconds;  
    protected JLabel timerLabel;
    protected JLabel userNameLabel;
    protected List<GameElement> elements;
    

    
    public Level(Game game, int number, String imagePath, int timeoutSeconds) {
        super("Level " + number);

        this.game = game;
        this.imagePath = imagePath;
        this.timeoutSeconds = timeoutSeconds;

        this.elements = new ArrayList<GameElement>();

        this.gameStarted = false;


        this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        /*
        this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
            new ImageIcon("assets/cursor.png").getImage(),
            new Point(0,0),"custom cursor"));
        */
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
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                t.mouseMoved(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                t.mouseMoved(e);
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

        // Username Label
        userNameLabel = new JLabel(this.game.getUserName());
        userNameLabel.setLocation(1200, 50);
        userNameLabel.setSize(200, 40);
        userNameLabel.setFont(new Font("Verdana", Font.BOLD, 22)); /* font name and size */
        userNameLabel.setForeground(new Color(255,255,255));
        this.add(userNameLabel);

        // Timer Label
        timerLabel = new JLabel("");
        timerLabel.setLocation(1200, 90);
        timerLabel.setSize(200, 60);
        timerLabel.setFont(new Font("Verdana", Font.BOLD, 36)); /* font name and size */
        timerLabel.setForeground(new Color(255,255,255));
        this.add(timerLabel);

        // add game elements
        for (GameElement e : elements) {
            this.add(e);
        }        
        
    }

    private void mouseMoved(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();

        Robot robot;
        try {
            robot = new Robot();
            Color color = robot.getPixelColor(x, y);
            String text = String.format("x: %d, y: %d, color: %s", x, y, color.toString());
            System.out.println(text);

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
                    this.gameOver(false);
                }
            }


        } catch (AWTException e1) {
            e1.printStackTrace();
        }
    }

    protected void displayTime() {
        long  restTimeMillis = this.getRemainingTime();

        /* set game counter text */
        Date date = new Date(restTimeMillis);
        String formattedTime = new SimpleDateFormat("mm:ss:SSS").format(date).substring(0, 8);   
        this.timerLabel.setText(formattedTime);

        if(restTimeMillis <= 0) {
            this.gameOver(true);
        }
        
    }

    protected long getRemainingTime() {
        long passedTimeSinceStartMillis = 0;

        if(gameStarted) {
            /* calculate elapsed time */
            long currentTimeMillis = System.currentTimeMillis();
            passedTimeSinceStartMillis = currentTimeMillis - startTimeMillis;
        } 

        return this.timeoutSeconds * 1000 - passedTimeSinceStartMillis;
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
        // tell gamecontroller the received points for this level (i.e. the remaining time in milliseconds)
        this.game.addPoints(this.getRemainingTime()); 
        

        this.stop();
        System.out.println("Finish!");

        

        JOptionPane.showMessageDialog(this, "You did it!!!");
        this.setVisible(false); // hide level frame
        this.dispose(); //Destroy Level, will be created again
        this.game.nextLevel();
    }

    private void gameOver(boolean becauseOfTimeout) {
        this.stop();
        System.out.println("Game Over!");

        // hide username, timer and game elements
        this.userNameLabel.setVisible(false);
        this.timerLabel.setVisible(false);
        this.userNameLabel.setVisible(false);
        for (GameElement e : elements) {
            e.setVisible(false);
        }  

        /* load game over animation */
        ImageIcon im = new ImageIcon("assets/burn.gif");
        Image scaledIm = im.getImage().getScaledInstance(1600, 900, Image.SCALE_DEFAULT);
        JLabel gameoverLabel = new JLabel(new ImageIcon(scaledIm));
        gameoverLabel.setBounds(0,0,1600,900);
        this.add(gameoverLabel);

        if(becauseOfTimeout) {
            JOptionPane.showMessageDialog(this, "Time is up! GAME OVER", "Oh no!", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "You hit a wall! GAME OVER", "Oh no!", JOptionPane.ERROR_MESSAGE);
        }
        
        this.setVisible(false); // hide level frame
        this.dispose(); //Destroy Level, will be created again

        this.game.gameOver();
    }

    /**
     * Starts the Timeout Counter of the Level
     */
    private void startCounter() {
        Level t = this;

        /* recall moment the game counter starts, to later calculate the time difference */
        startTimeMillis = System.currentTimeMillis();

        countDownTimer = new Timer(80, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                t.displayTime();
            }
        });

        countDownTimer.start();

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
