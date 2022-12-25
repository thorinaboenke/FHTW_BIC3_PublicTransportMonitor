package at.fhtw.publictransportmonitor.model;

import java.util.ArrayList;
import java.util.List;

public class LocationStop {
    String title;
    List<Line> lines = new ArrayList<Line>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    @Override
    public String toString() {
        return "LocationStop " + title + lines.get(0).toString();
    }
}
