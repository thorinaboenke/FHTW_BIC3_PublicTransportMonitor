package at.fhtw.publictransportmonitor.model;

import java.lang.constant.Constable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Departure {
    String timePlanned;
    String timeReal;
    Integer countdown;

    public String getTimePlanned() {
        return timePlanned;
    }

    public void setTimePlanned(String timePlanned) {
        this.timePlanned = timePlanned;
    }

    public String getTimeReal() {
        return timeReal;
    }

    public void setTimeReal(String timeReal) {
        this.timeReal = timeReal;
    }

    public Integer getCountdown() {
        return countdown;
    }

    public void setCountdown(Integer countdown) {
        this.countdown = countdown;
    }
    @Override
    public String toString() {
        try {
            return "Departing at " + timeStringToTime(timeReal) + " in " + countdown + " min \n";
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Extracts the hours and minutes form a time string and return them in the format "hh:mm"
     *
     * @param  timeString time string to be converted
     * @return      a string in the format "hh:mm" representing a departure time
     *
     */
    public String timeStringToTime(String timeString) throws ParseException {
        return timeString.substring(12,17);
    }
}
