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

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Game extends JFrame {

    private JPanel highscorePanel;
    private JScrollPane highscoreScrollPane;
    private HighScore highscore;
    private JTextField usernameField;
    private JButton startButton;
    private MediaPlayer mediaPlayer;
    private HighScoreEntry currentHighScoreEntry;
    private JLabel highscoreLabel;
    private int levelNumber = 0;


    public Game() {
        super("Dont't touch the wall");

        this.setSize(1024, 768);

        this.setLayout(null);



/* 
        
        highscore.add("User 1", 15);
        highscore.add("XYZ", 28);
        highscore.add("ABC User", 11);
        highscore.add("Test User", 44);
        highscore.add("John Doe", 63, true);
        highscore.add("User 1", 115);
        highscore.add("XYZ", 128);
        highscore.add("ABC User", 111);
        highscore.add("Test User", 144);
        highscore.add("John Doe", 163);

 */
        /* damit die applikation endet wenn das fenster geschlossen wird */
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        try {
            highscore = new HighScore();
            highscore.load();

            this.initHomeScreen();
            this.playMusic();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //this.usernameField.setText("Mario");
        //this.levels.get(0).display();
        //this.setEnabled(false); /* no user interaction with game frame during play */
    }

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
                if(usernameField.getText().trim().length() == 0) {
                    JOptionPane.showMessageDialog(t, "Bitte zuerst einen Usernamen eingeben.");   
                    usernameField.grabFocus();
                    return;
                }
                /* start game */
                mediaPlayer.stop();
                t.currentHighScoreEntry = t.highscore.add(usernameField.getText().trim(), 0);
                t.nextLevel();
            } 
          } );

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


    }

    private Level createLevel(int levelNumber) throws Exception {
        Level lv = null;

        switch(levelNumber) {
            case 1:
                lv = new Level(this, 1, "assets/Levels/level1.png", 60);
                lv.addElement(new Obstacle1(100, 100, true, 1));
                lv.addElement(new Obstacle1(1500, 300, false, 2));
                lv.addElement(new Obstacle1(700, 600, true, 3));
                lv.addElement(new Obstacle1(500, 450, false, 2));
                lv.addElement(new Obstacle1(900, 650, true, 3));
                lv.addElement(new Obstacle2(800, 450, 38, 52));
                break;
            case 2:
                System.out.println("Level 2 wird starten.");
                break;
        }

        if(lv == null) throw new Exception("Level number " + levelNumber + " does not exist");

        return lv;


    }

    private void playMusic() {
        final JFXPanel fxPanel = new JFXPanel();
        Media music = new Media(new File("assets/THE_HARA_Fire.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(music);
        mediaPlayer.setCycleCount(Integer.MAX_VALUE);
        //mediaPlayer.play();
        //mediaPlayer.setVolume(0.3);
    }

    public String getUserName() {
        return usernameField.getText().trim();
    }

    public void addPoints(long points) {
        this.currentHighScoreEntry.addPoints(points);
    }

    public void nextLevel() {
        this.levelNumber++;
        Level l;
        try {
            l = this.createLevel(levelNumber);
            l.display();
        } catch (Exception e1) {
            /* alle levels durch */
            this.gameOver();
        }

        this.refreshHighScore();
    }

    public void gameOver() {
        this.levelNumber = 0;
        this.refreshHighScore();
    }

    private void refreshHighScore() {
        this.highscoreLabel.setText(highscore.getHtml());
        try {
            this.highscore.save();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    

    /**
     * @return JPanel return the highscorePanel
     */
    public JPanel getHighscorePanel() {
        return highscorePanel;
    }

    /**
     * @param highscorePanel the highscorePanel to set
     */
    public void setHighscorePanel(JPanel highscorePanel) {
        this.highscorePanel = highscorePanel;
    }

    /**
     * @return JScrollPane return the highscoreScrollPane
     */
    public JScrollPane getHighscoreScrollPane() {
        return highscoreScrollPane;
    }

    /**
     * @param highscoreScrollPane the highscoreScrollPane to set
     */
    public void setHighscoreScrollPane(JScrollPane highscoreScrollPane) {
        this.highscoreScrollPane = highscoreScrollPane;
    }

    /**
     * @return HighScore return the highscore
     */
    public HighScore getHighscore() {
        return highscore;
    }

    /**
     * @param highscore the highscore to set
     */
    public void setHighscore(HighScore highscore) {
        this.highscore = highscore;
    }

    /**
     * @return JTextField return the usernameField
     */
    public JTextField getUsernameField() {
        return usernameField;
    }

    /**
     * @param usernameField the usernameField to set
     */
    public void setUsernameField(JTextField usernameField) {
        this.usernameField = usernameField;
    }

    /**
     * @return JButton return the startButton
     */
    public JButton getStartButton() {
        return startButton;
    }

    /**
     * @param startButton the startButton to set
     */
    public void setStartButton(JButton startButton) {
        this.startButton = startButton;
    }

    /**
     * @return MediaPlayer return the mediaPlayer
     */
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /**
     * @param mediaPlayer the mediaPlayer to set
     */
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

}
