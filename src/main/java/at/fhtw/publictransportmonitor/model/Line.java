package at.fhtw.publictransportmonitor.model;

import java.util.ArrayList;
import java.util.List;

public class Line {
    String name;
    String towards;
    List<Departure> departures = new ArrayList<Departure>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTowards() {
        return towards;
    }

    public void setTowards(String towards) {
        this.towards = towards;
    }

    public List<Departure> getDepartures() {
        return departures;
    }

    public void setDepartures(List<Departure> departures) {
        this.departures = departures;
    }

    @Override
    public String toString() {
        return "Line "  + name +
                " towards " + towards + " " + departures.get(0).toString();
    }
}
