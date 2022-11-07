package at.ddb.teamwork;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
import javax.swing.border.EmptyBorder;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;



import java.awt.BorderLayout;
import java.awt.Color;

public class Game extends JFrame{

    private JPanel highscorePanel;
    private JScrollPane highscoreScrollPane;
    private HighScore highscore;
    private JTextField usernameField;
    private JButton startButton;
    private MediaPlayer mediaPlayer;

    public Game() {
        super("Dont't touch the wall");

        this.setSize(1024, 768);

        this.setLayout(null);


        highscore = new HighScore();
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

        try {
            this.initHomeScreen();
            this.playMusic();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
                    JOptionPane.showMessageDialog(t, "Bitte zuerst enen Usernamen eingeben.");   
                    usernameField.grabFocus();
                    return;
                }
                /* start game */
            } 
          } );

        // Highscore Panel, ScrollPane and Label
        highscorePanel = new JPanel();
        highscorePanel.setLayout(new BorderLayout());
        highscorePanel.setBorder(new EmptyBorder(30, 80, 30, 80));  
        JLabel hsLabel = new JLabel(highscore.getHtml());
        hsLabel.setForeground(Color.WHITE);
        highscorePanel.add(hsLabel, BorderLayout.NORTH);
        highscorePanel.setBackground(Color.BLACK);
        highscoreScrollPane = new JScrollPane(highscorePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        highscoreScrollPane.setBounds(200, 200, 624, 500);
        this.add(highscoreScrollPane);




            
    }

    private void playMusic() {
        final JFXPanel fxPanel = new JFXPanel();
        Media music = new Media(new File("assets/THE_HARA_Fire.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(music);
        mediaPlayer.setCycleCount(Integer.MAX_VALUE);
        mediaPlayer.play();
        mediaPlayer.setVolume(0.3);
    }


    
}
