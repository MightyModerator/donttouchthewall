package at.ddb.teamwork;

import java.io.Serializable;

final class HighScoreEntry implements Comparable , Serializable {
    public final String username;
    public boolean hilight;
    public long points;

    public HighScoreEntry(String username, long points, boolean hilight) {
        this.username = username;
        this.points = points;
        this.hilight = hilight;
    }

    
    /** 
     * 
     * @param toAdd
     */
    public void addPoints(long toAdd) {
        this.points = this.points + toAdd;
    }

    
    /** 
     * 
     * @param o
     * @return int
     */
    @Override
    public int compareTo(Object o) {
        if(((HighScoreEntry)o).points > this.points) {
            return +1;
        } else {
            return -1;
        }
    }
}