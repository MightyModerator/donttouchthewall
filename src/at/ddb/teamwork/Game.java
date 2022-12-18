package at.ddb.teamwork;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main game controller class, here all parts come together.
 */
public class Game extends JFrame {

    private JPanel highscorePanel;
    private JScrollPane highscoreScrollPane;
    private HighScore highscore;
    private JTextField usernameField;
    private JButton startButton;
    private HighScoreEntry currentHighScoreEntry;
    private JLabel highscoreLabel;
    private int levelNumber = 0;
    private AudioController audioController;

    public final static Logger logger = LogManager.getLogger(Game.class);

    public Game() {
        super("Dont't touch the wall");

        /* size of the startscreen in pixels */
        this.setSize(1024, 768);

        /* no layout manager necessary, we work with absolute coordinates. */
        this.setLayout(null);

        /* damit die applikation endet wenn das fenster geschlossen wird */
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        logger.debug("Program started");  
        
        try {
            /* initialisation of the audioController. with this background music and sound clips can be played */
            audioController = new AudioController();

            /* initialisation of the highscore */
            highscore = new HighScore();

            /* load highscore from disk */
            highscore.load();

            /* initialisation of frame with all visual elements */
            this.initHomeScreen();

            /* play background music */
            this.audioController.playMusic("assets/THE_HARA_Fire.mp3");

        } catch (Exception e) {
            Game.logger.error("Unable to start game.", e); 
            JOptionPane.showMessageDialog(this, "Unable to start game." + e.getMessage());
            System.exit(ABORT);
        }

    }

    /**
     * All visual elements of the Game frame are created and defined here.
     * @throws IOException Thrown when the background image can not be loaded from disk.
     */
    private void  initHomeScreen() throws IOException {

        Game t = this;

        /* Background image */
        JLabel bgImageLabel = new JLabel(new ImageIcon(ImageIO.read(new File("assets/home-background.png"))));
        //bgImageLabel.setBounds(MAXIMIZED_BOTH, ABORT, WIDTH, HEIGHT);
        this.setContentPane(bgImageLabel);

        /* Username and Start Button Panel */
        JPanel userNamePanel = new JPanel();
        userNamePanel.setLayout(null);
        userNamePanel.setLocation(200, 150);
        userNamePanel.setSize(800, 30);
        userNamePanel.setBackground(new Color(0,0,0,0)); /* transparent background color */
        this.add(userNamePanel);

        // Username Textfield + Label
        usernameField = new JTextField(8);
        usernameField.setLocation(200, 0);
        usernameField.setSize(220, 30);
        userNamePanel.add(usernameField);
        JLabel userNameLabel = new JLabel("Username");
        userNameLabel.setLocation(0, 0);
        userNameLabel.setSize(200, 30);
        userNameLabel.setFont(new Font("Verdana", Font.BOLD, 18)); /* font name and size */
        userNameLabel.setForeground(new Color(255,255,255));
        userNamePanel.add(userNameLabel);

        // Start Button
        startButton = new JButton("Spiel starten");
        startButton.setLocation(502, 0);
        startButton.setSize(120, 30);
        userNamePanel.add(startButton); 
        startButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                /* this is executed when start button is clicked */
                if(usernameField.getText().trim().length() == 0) {
                    JOptionPane.showMessageDialog(t, "Bitte zuerst einen Usernamen eingeben.");   
                    usernameField.grabFocus();
                    return;
                }
                /* start game */
                t.audioController.stopMusic();
                t.currentHighScoreEntry = t.highscore.add(usernameField.getText().trim(), 0);
                t.nextLevel();
            } 
        });

        // Highscore Panel, ScrollPane and Label
        highscorePanel = new JPanel();
        highscorePanel.setLayout(new BorderLayout());
        highscorePanel.setBorder(new EmptyBorder(30, 80, 30, 80));  
        highscoreLabel = new JLabel("");
        highscoreLabel.setForeground(Color.WHITE);
        highscorePanel.add(highscoreLabel, BorderLayout.NORTH);
        highscorePanel.setBackground(Color.BLACK);
        highscoreScrollPane = new JScrollPane(highscorePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        highscoreScrollPane.setBounds(200, 200, 624, 500);
        this.add(highscoreScrollPane);

        this.refreshHighScore();

        Game.logger.info("Home screen initialized"); 

    }

    /**
     * Here the diffent levels get created and defined. This mehtod is called
     * for each level using the levelnumber, the level with the number is then 
     * created and returned. 
     * @param levelNumber the level number that shall be created
     * @return the created level
     * @throws Exception
     */
    private Level createLevel(int levelNumber) throws Exception {
        Level lv = null;

        switch(levelNumber) {
            case 1:
                lv = new Level(this, 1, "assets/Levels/LevelDesert.png", 30);
                lv.addElement(new Obstacle1(1392, 546, true, 2));
                lv.addElement(new Obstacle1(765, 355, false, 3));
                lv.addElement(new Obstacle1(198, 190, true, 4));
                lv.addElement(new Obstacle2(778, 355, 3000));
                lv.addElement(new Obstacle2(766, 443, 3000));
                break;
            case 2:
                lv = new Level(this, 1, "assets/Levels/LevelFire.png", 40);
                lv.addElement(new Obstacle1(152, 142, true, 3));
                lv.addElement(new Obstacle1(768, 279, false, 4));
                lv.addElement(new Obstacle1(1274, 547, true, 5));
                lv.addElement(new Obstacle2(217, 382, 1500));
                lv.addElement(new Obstacle2(553, 382, 1000));
                lv.addElement(new Obstacle2(1078, 382, 2000));
                lv.addElement(new Obstacle2(1434, 382, 1200));
                break;
            case 3:
                lv = new Level(this, 1, "assets/Levels/LevelMaze.png", 50);
                lv.addElement(new Obstacle1(100, 100, true, 8));
                lv.addElement(new Obstacle1(1500, 300, false, 3));
                lv.addElement(new Obstacle1(700, 600, true, 6));
                lv.addElement(new Obstacle1(500, 450, false, 2));
                lv.addElement(new Obstacle1(732, 234, true, 1));
                lv.addElement(new Obstacle1(900, 650, true, 3));
                lv.addElement(new Obstacle2(800, 450, 2800));
                lv.addElement(new Obstacle2(599, 344, 1000));
                break;
            case 4:
                lv = new Level(this, 1, "assets/Levels/LevelEarth.png", 60);
                lv.addElement(new Obstacle1(100, 100, true, 1));
                lv.addElement(new Obstacle1(1500, 300, false, 2));
                lv.addElement(new Obstacle1(700, 600, true, 3));
                lv.addElement(new Obstacle1(500, 450, false, 2));
                lv.addElement(new Obstacle1(900, 650, true, 3));
                lv.addElement(new Obstacle2(800, 450, 3000));
                break;
        }

        if(lv == null) throw new Exception("Level number " + levelNumber + " does not exist");

        return lv;


    }

    /**
     * returns the players name 
     * @return username
     */
    public String getUserName() {
        return usernameField.getText().trim();
    }

    /**
     * This may be called from insed the levels or the gameelements.
     * @return  returns the audio controller.
     */
    public AudioController getAudioController() {
        return this.audioController;
    }

    /**
     * Add highscore points to current user. Normally this is called from Level.java when a level has been completed.
     * @param points the points, the current user receives.
     */
    public void addPoints(long points) {
        this.currentHighScoreEntry.addPoints(points);
    }

    /**
     * Increases the levelNumber and starts the next level.
     */
    public void nextLevel() {
        this.levelNumber++;

        Game.logger.info("Starting level "+levelNumber); 

        Level l;
        try {
            l = this.createLevel(levelNumber);
            l.display();
        } catch(IOException ioex) {
            /* when level image can not be loaded */
            JOptionPane.showMessageDialog(this, "Unable to start level." +ioex.getMessage());
            System.exit(1);
        } catch (Exception e1) {
            /* alle levels durch, spiel zurücksetzen */
            Game.logger.info("All levels completed"); 
            this.gameOver();
        }
        
        this.refreshHighScore();
    }

    /**
     * Called from Level.java to tell the game controller that the game is over.
     * Resets the levelnumber back to 0.
     */
    public void gameOver() {
        Game.logger.info("Game over"); 

        this.levelNumber = 0;
        this.refreshHighScore();
    }

    /**
     * Updates the highscoreLabel text with the current highscore.
     */
    private void refreshHighScore() {
        this.highscoreLabel.setText(highscore.getHtml());
        try {
            this.highscore.save();
        } catch (IOException e) {
            Game.logger.error("Unable to save highscore to file.", e); 
            JOptionPane.showMessageDialog(this, "Unable to save highscore to file. "+e.getMessage());
        }
    }   

}
