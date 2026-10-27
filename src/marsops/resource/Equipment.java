package marsops.resource;

import marsops.facility.Location;

public class Equipment {
    private Boolean isDamaged;
    private static int cnt = 0;
    private int id;
    private Location location;

    public Equipment() {
        this.isDamaged = false;
        id = ++cnt;
    }

    public int getId() {
        return id;
    }

    public boolean isAvailable() {
        return !isDamaged;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void fix() {
        this.isDamaged = false;
    }

    public void damage() {
        this.isDamaged = true;
    }
}