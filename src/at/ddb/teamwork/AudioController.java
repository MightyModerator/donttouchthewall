package at.ddb.teamwork;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioController {

    private MediaPlayer mediaPlayer;
    private Clip coinCollectedClip;
    private Clip levelCompleteClip;
    private Clip gameOverClip;

    public AudioController() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.coinCollectedClip = this.prepareAudioClip("sounds/collect.wav");
        this.levelCompleteClip = this.prepareAudioClip("sounds/levelup.wav");
        this.gameOverClip = this.prepareAudioClip("sounds/gameover.wav");
    }


    private Clip prepareAudioClip(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // Create an AudioInputStream from a given sound file
        File audioFile = new File(filePath);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

        // Acquire audio format and create a DataLine.Infoobject
        AudioFormat format = audioStream.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, format);

        // obtain the clip
        Clip audioClip = (Clip) AudioSystem.getLine(info);

        // open audio stream
        audioClip.open(audioStream);

        return audioClip;    }

    public void playMusic(String filePath) {
        if(this.mediaPlayer != null) {
            this.mediaPlayer.stop();
        }
        

        final JFXPanel fxPanel = new JFXPanel();
        Media music = new Media(new File(filePath).toURI().toString());
        this.mediaPlayer = new MediaPlayer(music);
        this.mediaPlayer.setCycleCount(Integer.MAX_VALUE);
        this.mediaPlayer.play();
        this.mediaPlayer.setVolume(0.3);
    }

    public void stopMusic() {
        this.mediaPlayer.stop();
    }

    public void playCoinCollected() {
        this.coinCollectedClip.stop(); // falls der clip schon läuft, stoppen
        this.coinCollectedClip.setFramePosition(0); // falls der clip schon einmal lieft, an den anfang zurücksetzen
        this.coinCollectedClip.start(); // clip abspielen
        
    }

    public void playLevelComplete() {
        this.levelCompleteClip.stop(); // falls der clip schon läuft, stoppen
        this.levelCompleteClip.setFramePosition(0); // falls der clip schon einmal lieft, an den anfang zurücksetzen
        this.levelCompleteClip.start(); // clip abspielen
        
    }

    public void playGameOver() {
        this.gameOverClip.stop(); // falls der clip schon läuft, stoppen
        this.gameOverClip.setFramePosition(0); // falls der clip schon einmal lieft, an den anfang zurücksetzen
        this.gameOverClip.start(); // clip abspielen
        
    }
    
    
}
