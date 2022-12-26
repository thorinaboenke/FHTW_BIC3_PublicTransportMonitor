package at.fhtw.publictransportmonitor.model;

public class Location {
    String Name;
    String id;

    public Location(String name, String id) {
        Name = name;
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return Name +
                " , id " + id
                ;
    }
}
