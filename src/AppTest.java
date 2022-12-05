import static org.junit.Assert.assertEquals;
import java.io.File;  
import java.io.IOException;
import java.util.Random;

import org.testng.annotations.Test;

import at.ddb.teamwork.HighScore;

public class AppTest {

    /**
     * Test creation of the highScore and its getHTML method.
     */
    @Test
    public void testHighscoreCreate() {
        /* create highscore and add some random data */
        HighScore h = new HighScore();
        h.add("testuser1", 333333);
        h.add("testuser2", 4444);
        h.add("testuser3", 55);
        h.add("testuser4", 6);

        String expectedHTML = "<html><h1>Highscore</h1><table width=100% cellpadding='0' cellmargin='0'><tr color='#FFFFFF'><td width='50px'>#1</td><td width='235px'>testuser1</td><td align='right'>333333 credits</td></tr><tr color='#FFFFFF'><td width='50px'>#2</td><td width='235px'>testuser2</td><td align='right'>4444 credits</td></tr><tr color='#FFFFFF'><td width='50px'>#3</td><td width='235px'>testuser3</td><td align='right'>55 credits</td></tr><tr color='#FF0000'><td width='50px'>#4</td><td width='235px'>testuser4</td><td align='right'>6 credits</td></tr></table></html>";
        assertEquals(expectedHTML, h.getHtml());
    }

    /**
     * Test the creating, saving and loading of the Highscore.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Test
    public void testHighscoreSave() throws IOException, ClassNotFoundException {

        /* Rename current highscore file if it exists to a temp filename, and later rename it back */
        File currentFile = new File("highscore");
        File tmpFile = new File("highscore.tmp");
        boolean backupCurrentHighscore = currentFile.exists();
        if(backupCurrentHighscore) {
            currentFile.renameTo(tmpFile);
        }

        try {

            /* create highscore with huge amount of random data */
            HighScore h = new HighScore();
            Random r = new Random();
            for(int i = 0; i <= 10000; i++) {
                h.add("User"+i, i*666);
            }

            /* store html of created highscore to see if ater saving and loading it stayed the same */
            String htmlBefore = h.getHtml();

            /* save the highscore to file */
            h.save();

            /* create a new highscore and laod it from file */
            h = new HighScore();
            h.load();
            
            /* see if highscore has been saved and loaded correctly */
            assertEquals(htmlBefore, h.getHtml());
        } catch(Exception ex) {
            throw ex;
        } finally {
            /* clean up */
            File f = new File("highscore");
            f.delete();

            /* rename the previously renamed file back */
            if(backupCurrentHighscore) {
                tmpFile.renameTo(currentFile);
            }
        }
    }


    
}
