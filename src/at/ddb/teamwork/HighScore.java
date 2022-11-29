package at.ddb.teamwork;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;

public class HighScore implements Serializable {

    private ArrayList<HighScoreEntry> entries = new ArrayList<HighScoreEntry>();

    /**
     * Adds an new entry to the highscore
     * @param username the users name for the highscore
     * @param score the users highscore
     * @return returns a new 
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

    public String getHtml() {

        Collections.sort(entries);

        String html = "<html><h1>Highscore</h1><table width=100% cellpadding='0' cellmargin='0'>";

        int ranking = 1;
        String colorHTML;
        for (HighScoreEntry e : this.entries) {
            colorHTML = e.hilight ? "#FF0000" : "#FFFFFF";
            html += "<tr color='" + colorHTML + "'><td width='50px'>#"+ranking+"</td><td width='235px'>"+e.username+"</td><td align='right'>"+e.points+" credits</td></tr>";
            ranking++;
        }

        html += "</table></html>";
        return html;
    }

    public void save() throws IOException {
        FileOutputStream fileStream = new FileOutputStream("highscore");

        /* Serialize highscore from ArrayList */
        ObjectOutputStream os = new ObjectOutputStream(fileStream);
        os.writeObject(this.entries);

        /* close file output stream */
        fileStream.close();
        os.close();
        
    }

    public void load() throws ClassNotFoundException, IOException {
        FileInputStream fileStream = new FileInputStream("highscore");
        ObjectInputStream is = new ObjectInputStream(fileStream);
        this.entries = (ArrayList<HighScoreEntry>) is.readObject();

    }
}

