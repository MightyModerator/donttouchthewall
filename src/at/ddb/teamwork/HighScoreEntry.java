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

    public void addPoints(long toAdd) {
        this.points = this.points + toAdd;
    }

    @Override
    public int compareTo(Object o) {
        return this.points < ((HighScoreEntry)o).points ? +1 : -1;
    }
}