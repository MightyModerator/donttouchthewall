package at.ddb.teamwork;

import java.util.ArrayList;
import java.util.Collections;

public class HighScore {
    final class Entry implements Comparable {
        public final String username;
        public final int time;
        public final boolean hilight;

        public Entry(String username, int time, boolean hilight) {
            this.username = username;
            this.time = time;
            this.hilight = hilight;
        }

        @Override
        public int compareTo(Object o) {
            return this.time < ((Entry)o).time ? -1 : +1;
        }
    }

    private ArrayList<Entry> entries = new ArrayList<Entry>();

    public void add(String username, int time) {
        this.add(username, time, false);
    }

    public void add(String username, int time, boolean hilight) {
        this.entries.add(new Entry(username, time, hilight));
    }

    public String getHtml() {

        Collections.sort(entries);

        String html = "<html><h1>Highscore</h1><table width=100% cellpadding='0' cellmargin='0'>";

        int ranking = 1;
        String colorHTML;
        for (Entry e : this.entries) {
            colorHTML = e.hilight ? "#FF0000" : "#FFFFFF";
            html += "<tr color='" + colorHTML + "'><td width='50px'>#"+ranking+"</td><td width='235px'>"+e.username+"</td><td align='right'>"+e.time+" Sekunden</td></tr>";
            ranking++;
        }

        


        html += "</table></html>";
        return html;
    }
}

