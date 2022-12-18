package at.ddb.teamwork;

import javax.swing.Timer;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Obstacle2 extends GameElement {

    private boolean isVisible = true;
    private Timer hideTimer;
    private final int blinkIntervalMilliseconds;

    public Obstacle2(int x, int y, int blinkIntervalMilliseconds) {
        super("assets/poison_obstacle2.gif", x, y, 38, 52);
        
        this.blinkIntervalMilliseconds = blinkIntervalMilliseconds;

    }

    @Override
    public void draw() {
        this.setVisible(this.isVisible); // set current visibility from member var
    }

    @Override
    public void start() {
        Obstacle2 t = this;

        this.hideTimer = new Timer(blinkIntervalMilliseconds, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                t.isVisible = !t.isVisible; // toggle visibiliy status in member var
            }
        });

        this.hideTimer.setRepeats(true);
        this.hideTimer.start();
        
    }

    @Override
    public void stop() {
        this.hideTimer.stop();
    }
    
}
