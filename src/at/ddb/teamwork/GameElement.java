package at.ddb.teamwork;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.awt.Image;

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

    public void setImage(String imagePath) {
        ImageIcon im = new ImageIcon(imagePath);
        Image scaledIm = im.getImage().getScaledInstance(this.width, this.height, Image.SCALE_DEFAULT);
        this.setIcon(new ImageIcon(scaledIm));
        this.repaint();
        
    }
    
    public abstract void draw();

    
}
