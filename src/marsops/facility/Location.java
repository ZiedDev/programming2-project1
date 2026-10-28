package marsops.facility;

abstract public class Location {
    protected final String location;

    public Location(String location) {
        this.location = location;
    }


    public String getLocation() {
        return location;
    }
}