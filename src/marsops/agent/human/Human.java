package marsops.agent.human;

import marsops.agent.Agent;
import marsops.emergency.Emergency;
import marsops.facility.Facility;
import marsops.mission.Mission;

public class Human extends Agent {
    static final int FATIGUE_MAX = 100;
    static final int FATIGUE_MIN = 0;

    private final String id;
    private String name;
    private int fatigue;

    public Human(Facility originLocation, String name, String id) {
        super(originLocation);
        this.name = name;
        this.id = id;
        this.fatigue = 0;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getFatigue() {
        return fatigue;
    }

    public void setFatigue(int fatigue) {
        this.fatigue = fatigue;
    }

    public void reduceFatigue(int time_unit) {
        this.fatigue = Math.max(this.fatigue - time_unit, FATIGUE_MIN);
    }

    public void increaseFatigue(int time_unit) {
        this.fatigue = Math.min(this.fatigue + time_unit, FATIGUE_MAX);
    }

    public void updateFatigue(int time_unit) {
        if (this.getAssignedMission() != null) {
            int time_left = this.getAssignedMission().getDuration() - this.getAssignedMission().getElapsed();
            increaseFatigue(Math.min(time_left, time_unit));

            if (time_unit > time_left) {
                reduceFatigue(time_unit - time_left);
            }
        } else {
            reduceFatigue(time_unit);
        }
    }

    @Override
    public boolean canPerform(Mission mission) {
        if (!super.isAvailable())
            return false;
        return fatigue + mission.getDuration() < FATIGUE_MAX;
    }

    @Override
    public boolean canHandle(Emergency emergency) {
        if (!super.isAvailable())
            return false;
        return fatigue < FATIGUE_MAX;
    }

    @Override
    public void advanceTime(int time_unit) {
        updateFatigue(time_unit);
    }
}
