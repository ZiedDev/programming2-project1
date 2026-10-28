package marsops.agent;

import java.util.List;

import marsops.emergency.Emergency;
import marsops.facility.Facility;
import marsops.facility.Location;
import marsops.mission.Mission;

public abstract class Agent {
    private final Facility originLocation;
    private Location currentLocation;
    private boolean isAvailable;
    private int health;
    private Mission assignedMission;

    public Agent(Facility originLocation) {
        this.originLocation = originLocation;
        this.currentLocation = originLocation;
        this.isAvailable = true;
        this.health = 100;
        this.assignedMission = null;
    }

    protected abstract List<Class<? extends Mission>> getAllowedMissions();

    protected abstract List<Class<? extends Emergency>> getAllowedEmergencies();

    // setters
    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setAssignedMission(Mission assignedMission) {
        this.assignedMission = assignedMission;
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

    public Mission getAssignedMission() {
        return assignedMission;
    }

    public boolean canPerform(Mission mission) {
        if (!isAvailable || mission == null) {
            return false;
        }
        return getAllowedMissions().contains(mission.getClass());
    }

    public boolean canHandle(Emergency emergency) {
        if (!isAvailable || emergency == null) {
            return false;
        }
        return getAllowedEmergencies().contains(emergency.getClass());
    }

    public abstract void advanceTime(int timeUnit);
}
