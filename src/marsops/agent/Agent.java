package marsops.agent;

import marsops.emergency.Emergency;
import marsops.facility.Facility;
import marsops.facility.Location;
import marsops.mission.Mission;

public class Agent {
    private Facility originLocation;
    private Location currentLocation;
    private boolean isAvailable;
    private int health;

    public Agent(Facility originLocation) {
        this.originLocation = originLocation;
        this.currentLocation = originLocation;
        this.isAvailable = true;
        this.health = 100;
    }

    // getters
    public Facility getOriginLocation() {
        return this.originLocation;
    }
    public Location getCurrentLocation() {
        return this.currentLocation;
    }
    public boolean isAvailable() {
        return this.isAvailable;
    }
    public int getHealth() {
        return this.health;
    }

    // setters
    public void setOriginLocation(Facility originLocation) {
        this.originLocation = originLocation;
    }
    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
    public void setHealth(int health) {
        this.health = health;
    }

    // funcs
    public boolean canPerform(Mission mission) {
        return false;
    }

    public void canHandle(Emergency emergency) {

    }
}
