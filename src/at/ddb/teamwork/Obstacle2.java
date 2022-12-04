package at.ddb.teamwork;

import javax.swing.Timer;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Obstacle2 extends GameElement {

    private boolean isVisible = true;

    public Obstacle2(int x, int y, int width, int height) {
        super("assets/poison_obstacle2.gif", x, y, width, height);
        Obstacle2 t = this;


        Timer hideTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(t.isVisible == true) {
                    t.setVisible(false);
                    t.isVisible = false;
                } else {
                    t.setVisible(true);
                    t.isVisible = true;
                }

            }
        });



        hideTimer.setRepeats(true);
        hideTimer.start();
    }

    @Override
    public void draw() {
        /* do nothing, object should not move */
    }
    
}
