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

/**
 * Central controller class for all audio operations in the game.
 */
public class AudioController {

    private MediaPlayer mediaPlayer;
    private Clip coinCollectedClip;
    private Clip levelCompleteClip;
    private Clip gameOverClip;

    public AudioController() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        /* necessary for initialization of javafx, 
         * see: https://stackoverflow.com/questions/14025718/javafx-toolkit-not-initialized-when-trying-to-play-an-mp3-file-through-mediap  */
        new JFXPanel();
        
        this.coinCollectedClip = this.prepareAudioClip("sounds/collect.wav");
        this.levelCompleteClip = this.prepareAudioClip("sounds/levelup.wav");
        this.gameOverClip = this.prepareAudioClip("sounds/gameover.wav");

        Game.logger.info("Audiocontroller initialized"); 
    }


    
    /** 
     * Internally used to preload an prepare an audioclip given by a filepath.
     * @param filePath the path to a .wav file
     * @return Clip
     * @throws UnsupportedAudioFileException
     * @throws IOException
     * @throws LineUnavailableException
     */
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

        return audioClip;    
    }

        
    
    /** 
     * Loads an audiofile and immediatly starts playing. If another audiofile is already 
     * beeing played, it is stopped and the new one will start.
     * @param filePath The filesystem path to the music file
     */
    public void playMusic(String filePath) {

        Game.logger.info("Playing music: " + filePath); 

        if(this.mediaPlayer != null) {
            this.mediaPlayer.stop();
        }
        
        Media music = new Media(new File(filePath).toURI().toString());
        this.mediaPlayer = new MediaPlayer(music);
        this.mediaPlayer.setCycleCount(Integer.MAX_VALUE);
        this.mediaPlayer.play();
        this.mediaPlayer.setVolume(0.1);
    }

    /**
     * Stops the currently playing music if any.
     */
    public void stopMusic() {
        Game.logger.info("Stopping music"); 
        this.mediaPlayer.stop();
    }

    /**
     * Immediately plays a soudclip describing a coin that is collected.
     */
    public void playCoinCollected() {
        this.coinCollectedClip.stop(); // falls der clip schon läuft, stoppen
        this.coinCollectedClip.setFramePosition(0); // falls der clip schon einmal lieft, an den anfang zurücksetzen
        this.coinCollectedClip.start(); // clip abspielen
        
    }

    /**
     * Immediately plays a soudclip describing that a level has successfully completed.
     */
    public void playLevelComplete() {
        this.levelCompleteClip.stop(); // falls der clip schon läuft, stoppen
        this.levelCompleteClip.setFramePosition(0); // falls der clip schon einmal lieft, an den anfang zurücksetzen
        this.levelCompleteClip.start(); // clip abspielen
        
    }

    /**
     * Immediately plays a soudclip describing that the game is over.
     */
    public void playGameOver() {
        this.gameOverClip.stop(); // falls der clip schon läuft, stoppen
        this.gameOverClip.setFramePosition(0); // falls der clip schon einmal lieft, an den anfang zurücksetzen
        this.gameOverClip.start(); // clip abspielen
        
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

    /**
     * @return Clip return the coinCollectedClip
     */
    public Clip getCoinCollectedClip() {
        return coinCollectedClip;
    }

    /**
     * @param coinCollectedClip the coinCollectedClip to set
     */
    public void setCoinCollectedClip(Clip coinCollectedClip) {
        this.coinCollectedClip = coinCollectedClip;
    }

    /**
     * @return Clip return the levelCompleteClip
     */
    public Clip getLevelCompleteClip() {
        return levelCompleteClip;
    }

    /**
     * @param levelCompleteClip the levelCompleteClip to set
     */
    public void setLevelCompleteClip(Clip levelCompleteClip) {
        this.levelCompleteClip = levelCompleteClip;
    }

    /**
     * @return Clip return the gameOverClip
     */
    public Clip getGameOverClip() {
        return gameOverClip;
    }

    /**
     * @param gameOverClip the gameOverClip to set
     */
    public void setGameOverClip(Clip gameOverClip) {
        this.gameOverClip = gameOverClip;
    }

}
