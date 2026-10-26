package marsops.agent.human;

import marsops.agent.Agent;
import marsops.facility.Facility;
import marsops.mission.Mission;

public class Human extends Agent {
    static final int FATIGUE_MAX = 100;
    static final int FATIGUE_MIN = 0;

    final String id;
    String name;
    int fatigue;

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

    public void reduce_fatigue(int time_unit) {
        this.fatigue = Math.max(this.fatigue - time_unit, FATIGUE_MIN);
    }

    public void increase_fatigue(int time_unit) {
        this.fatigue = Math.min(this.fatigue + time_unit, FATIGUE_MAX);
    }

    public void update_fatigue(int time_unit) {
        if (this.getAssignedMission() != null) {
            int time_left = this.getAssignedMission().getDuration() - this.getAssignedMission().getElapsed();
            increase_fatigue(Math.min(time_left, time_unit));

            if (time_unit > time_left) {
                reduce_fatigue(time_unit - time_left);
            }
        } else {
            reduce_fatigue(time_unit);
        }
    }

    @Override
    public boolean canPerform(Mission mission) {
        if (!super.isAvailable()) return false;
        return fatigue + mission.getDuration() < FATIGUE_MAX;
    }
}
