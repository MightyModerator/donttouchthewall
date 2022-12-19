package at.ddb.teamwork;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Represents the hiscore, has capability to save to and load from file.
 */
public class HighScore implements Serializable {

    private ArrayList<HighScoreEntry> entries = new ArrayList<HighScoreEntry>();

    /**
     * Adds an new entry to the highscore
     * @param username the users name for the highscore
     * @param score the users highscore
     * @return returns a new HighscoreEntry
     */
    public HighScoreEntry add(String username, int score) {
        
        /* only the newest entry should be hilighted, so remove hilight from all other entries */
        for (HighScoreEntry entry : this.entries) {
            entry.hilight = false;
        }

        /* add new entry to arraylist */
        HighScoreEntry e = new HighScoreEntry(username, score, true);
        this.entries.add(e);
        return e;
    }

    /**
     * Creates a html represenation of the highscore.
     * @return a String representing the highscore as html
     */
    public String getHtml() {
        /* sort highscore data decending by score. the sorting logic is defined 
         * in HighScoreEntry.java method compareTo */
        Collections.sort(entries);

        StringBuilder html = new StringBuilder("<html><h1>Highscore</h1><table width='100%' cellpadding='0' cellmargin='0'>");

        int ranking = 1;
        String colorHTML;
        for (HighScoreEntry e : this.entries) {
            colorHTML = e.hilight ? "#FF0000" : "#FFFFFF";
            html.append("<tr color='" + colorHTML + "'><td width='50px'>#"+ranking+"</td><td width='235px'>"+e.username+"</td><td align='right'>"+e.points+" credits</td></tr>");
            ranking++;
        }

        html.append("</table></html>");
        return html.toString();
    }

    /**
     * Saves the current highscore to the file "highscore"
     * @throws HighScoreException When any file IO error happens, e.g. the file is locked
     */
    public void save() throws HighScoreException {
        Game.logger.info("Saving highscore"); 

        try {
            /* fileoutput stream pointing to hihghscore file */
            FileOutputStream fileStream = new FileOutputStream("highscore");

            /* Serialize highscore from ArrayList and pass into fileoutput stream */
            ObjectOutputStream os = new ObjectOutputStream(fileStream);
            os.writeObject(this.entries);

            /* close file output stream */
            fileStream.close();
            os.close();

            Game.logger.info("Highscore saved"); 
        } catch(Exception ex) {
            throw new HighScoreException("Error saving highscore. " + ex.getMessage(), ex);
        }

        
    }

    /**
     * Loads the highscore entries from the file "highscore"
     * @throws HighScoreException
     * @throws ClassNotFoundException When highscore file is broken, or when any file IO error happens, e.g. the file is locked
     */
    public void load() throws HighScoreException {
        Game.logger.info("Load highscore from file"); 

        try{
            /* fileinput stream with the highscore file as source */
            FileInputStream fileStream = new FileInputStream("highscore");

            /* deserialize highscoredata into entries ArrayList */
            ObjectInputStream is = new ObjectInputStream(fileStream);
            this.entries = (ArrayList<HighScoreEntry>) is.readObject();

            /* close file input stream */
            fileStream.close();
            is.close();
        } catch(Exception ex) {
            throw new HighScoreException("Error loading highscore. " + ex.getMessage(), ex);
        }
        

        Game.logger.info("Highscore loaded from file"); 
    }
}

