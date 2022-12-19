package at.ddb.teamwork;

    import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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
import javax.swing.Timer;

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
    protected Timer gameElementsTimer;

    
    public Level(Game game, int number, String imagePath, int timeoutSeconds) {
        super("Level " + number);

        Game.logger.info("Initializing level " + number); 
        /** Hier beginnt die Initialisierung von diversen Variablen um Sie im Anschluss verwenden zu können */
        this.game = game;
        this.imagePath = imagePath;
        this.timeoutSeconds = timeoutSeconds;
        /** Variable "elements" wird initialisiert und die später unsere Obstacles übergeben zu können */
        this.elements = new ArrayList<GameElement>();
        /** Variable "game.Started" wird später auf true gesetzt ab dann wird Mauszeiger Position gemessen */
        this.gameStarted = false;

        /* set mouse cursor to CROSSHAIR */
        this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

    }
    /**
     * Hier werden bei jedem Aufruf Obstacles gesammelt und in unserer ArrayList gespeichert 
     * @param e das GameElement welches im Level plaziert werden soll
     */
    public void addElement(GameElement e) {
        this.elements.add(e);
    }

    /**
     * 
     * @throws IOException when background image cannot be loaded
     */
    public void display() throws IOException {
        Level t = this;

        Game.logger.info("Display level "); 
        //Hier ruft die Methode Display intiScreen auf (Zeile 127)/
        this.initScreen();
        this.displayTime();

        /* make jframe visible */
        this.setVisible(true);


        /* start listening to mouse events */
        this.addMouseMotionListener(new MouseMotionAdapter() {

            /**
             * Invoked when a mouse button is pressed on a component and then
             * dragged. 
             */
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                t.mouseMoved(e);
            }

            /**
             * Invoked when the mouse button has been moved on a component
             * (with no buttons pressed).
             */
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                t.mouseMoved(e);
            }
        });
    }

    public void start() {
        this.gameStarted = true;
        this.startCounter();
        this.startGameElements();
        this.game.getAudioController().playMusic("assets/THE_HARA_Fire.mp3");

        Game.logger.info("Game started"); 
    }

    public void stop() {
        this.gameStarted = false;
        this.stopGameElements();
        this.game.getAudioController().stopMusic();

        Game.logger.info("Game stopped"); 
    }

    /**
     * @throws IOException
     */
    protected void  initScreen() throws IOException  {
        /** Hier wird zuerst der Layoutmanager deaktiviert da wir über Koordinaten arbeiten */
        this.setLayout(null);
        /** Hier defineren wir das Programm Fenster mit 1600 x 900 Pixel */
        this.setSize(1600, 900);

        /* fullscreen */
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); 

        /* without frame, buttons, ... */
        this.setUndecorated(true);

        /* Background image */
        JLabel bgImageLabel;
        /** Hier wir der Pfad des Hintergrundbildes festgelegt es eingelesen (ImageIO.read(new File(this.imagePath) 
         * Dann in ein Icon gepackt (new ImageIcon(ImageIO.read(new File(this.imagePath)
         * Dann in ein Icon gepackt (new ImageIcon(ImageIO.read(new File(this.imagePath)
         * Dann in ein Label gepackt  bgImageLabel = new JLabel(new ImageIcon */
        bgImageLabel = new JLabel(new ImageIcon(ImageIO.read(new File(this.imagePath))));
        /** Das Hintergrundbild auf die selben Maße wie unser Spiel Bildschirm gesetzt */
        bgImageLabel.setBounds(0,0,1600,900);
        /** Hier wird dem Frame der Hintergrund zugewiesen */
        this.setContentPane(bgImageLabel);

        /** Hier wird der Username von game abgefragt und in ein Label gesetzt
         *  Kästchen rechts im Spielbildschirm
        */
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
        
        Game.logger.info("Level Screen initialized"); 
        
    }

    private void mouseMoved(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();

        Robot robot;
        try {
            robot = new Robot();
            Color color = robot.getPixelColor(x, y);
            String text = String.format("x: %d, y: %d, color: %s", x, y, color.toString());
            /** Haben wir als Hilfe für uns genutzt um die Obstacles auf unseren Levels zu platzieren */
            System.out.println(text);

            if(this.gameStarted == false) {
                if(color.equals(this.startColor)) {
                    this.start();
                }
                /**Wenn das Spiel begonnen hat beginnt der Mousetracker die definerten Farben zu kontrollieren */
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
            Game.logger.error("Platform does not allow low level input control", e); 
            JOptionPane.showMessageDialog(this, "Platform does not allow low level input control. Exit game.");
            System.exit(2);
        }
    }
//* */
    private void displayTime() {
        long  restTimeMillis = this.getRemainingTime();

        /* set game counter text */
        Date date = new Date(restTimeMillis);
        String formattedTime = new SimpleDateFormat("mm:ss:SSS").format(date).substring(0, 8);   
        this.timerLabel.setText(formattedTime);

        if(restTimeMillis <= 0) {
            this.gameOver(true);
        }
        
    }

    private long getRemainingTime() {
        long passedTimeSinceStartMillis = 0;

        if(gameStarted) {
            /* calculate elapsed time */
            long currentTimeMillis = System.currentTimeMillis();
            passedTimeSinceStartMillis = currentTimeMillis - startTimeMillis;
        } 

        return this.timeoutSeconds * 1000 - passedTimeSinceStartMillis;
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
        this.game.getAudioController().playGameOver();
        System.out.println("Game Over!");

        // hide username, timer and game elements
        this.userNameLabel.setVisible(false);
        this.timerLabel.setVisible(false);

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
        /** after klicking the dialog button... */
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

        /* starts the timer, and thus the countdown of the level */
        countDownTimer.start();

    }

    
    private void startGameElements() {

        /* show and start all obstacles by iterating */
        for (GameElement el : elements) {
            el.setVisible(true); 
            el.start(); 
        }   

        /*
         * call the .draw() method of each game element, 25 times a second (40ms) 
         */
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
        /* hide and stop all obstacles by iterating */
        for (GameElement el : elements) {
            el.setVisible(false); 
            el.stop();
        }   

        // stop timer that frequently calls draw() on each game element
        this.gameElementsTimer.stop();
    }

    
}
