package at.ddb.teamwork;

import java.awt.Point;

public class Obstacle1 extends GameElement {

    boolean direction;
    long speed;

    public Obstacle1(int x, int y, boolean direction, long speed) {
        super(direction ? "assets/shark-right.gif" : "assets/shark-left.gif", x, y, 100, 100);

        this.direction = direction;
        this.speed = speed;
    }

    @Override
    public void draw() {
        Point p = this.getLocation();

        if(direction) {
            p.x += this.speed;
        } else {
            p.x -= this.speed;
        }

        if(p.x > 1400) {
            this.direction = false;
            this.setImage("assets/shark-left.gif");
        } 
        else if(p.x < 100) {
            this.direction = true;
            this.setImage("assets/shark-right.gif");
       }
        
        this.setLocation(p);
        
    }

    @Override
    public boolean hitTest(int x, int y) {
        // TODO Auto-generated method stub
        return false;
    }
    
}
