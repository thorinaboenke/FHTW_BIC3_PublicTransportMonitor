package at.fhtw.publictransportmonitor.model;

public class Disruption {
    String title;
    String description;
    String reason;

    public Disruption(String title, String description, String reason) {
        this.title = title;
        this.description = description;
        this.reason = reason;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return  title + '\n' +
                description + "\n" + reason + "\n \n" ;
    }
}
