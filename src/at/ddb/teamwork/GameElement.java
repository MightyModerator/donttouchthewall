package at.ddb.teamwork;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.awt.Image;

/**
 * To create a new Obstacle or other sprite for the game, extend his Class.
 */
public abstract class GameElement extends JLabel {

    private int width;
    private int height;

    public GameElement(String imagePath, int x, int y, int width, int height) {
        super(new ImageIcon());

        this.width = width;
        this.height = height;

        this.setImage(imagePath);

        this.setLocation(x, y);
        this.setSize(width, height);
        this.setLayout(null);
    }


    
    /** 
     * Initialize and paint the game elements image or animated gif form a given image file.
     * @param imagePath The image filepath to load
     */
    public void setImage(String imagePath) {
        ImageIcon im = new ImageIcon(imagePath);
        Image scaledIm = im.getImage().getScaledInstance(this.width, this.height, Image.SCALE_DEFAULT);
        this.setIcon(new ImageIcon(scaledIm));
        this.repaint();
    }
    
    /**
     * This method is called 25 times per second during game play.
     * It can be used for animation of the game element.
     */
    public abstract void draw();

    /**
     * This method is called when the gameplay is started.
     */
    public abstract void start();

    /** 
     * This mehtod is called when the gameplay is stopped. 
     */
    public abstract void stop();

    
}
