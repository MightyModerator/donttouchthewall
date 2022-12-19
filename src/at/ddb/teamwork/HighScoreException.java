package at.ddb.teamwork;

public class HighScoreException extends Exception {
    public HighScoreException(String message) {
        super(message);
    }

    public HighScoreException(String message, Exception parent) {
        super(message, parent);
    }
    
}
