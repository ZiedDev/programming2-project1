package marsops.agent.robot;

import marsops.agent.Agent;
import marsops.emergency.Emergency;
import marsops.facility.Facility;
import marsops.mission.Mission;

public abstract class Robot extends Agent {
    static final int BATTERY_LEVEL_MAX = 100;
    static final int BATTERY_LEVEL_MIN = 0;

    private int batteryLevel;

    public Robot(Facility originLocation) {
        super(originLocation);
        this.batteryLevel = BATTERY_LEVEL_MAX;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public void reduceBatteryLevel(int timeUnit) {
        this.batteryLevel = Math.max(this.batteryLevel - timeUnit, BATTERY_LEVEL_MIN);
    }

    public void increaseBatteryLevel(int timeUnit) {
        this.batteryLevel = Math.min(this.batteryLevel + timeUnit, BATTERY_LEVEL_MAX);
    }

    public void updateBatteryLevel(int timeUnit) {
        if (this.getAssignedMission() != null) {
            int time_left = this.getAssignedMission().getDuration() - this.getAssignedMission().getElapsed();
            reduceBatteryLevel(Math.min(time_left, timeUnit));

            if (timeUnit > time_left) {
                increaseBatteryLevel(timeUnit - time_left);
            }
        } else {
            increaseBatteryLevel(timeUnit);
        }
    }

    @Override
    public boolean canPerform(Mission mission) {
        if (!super.isAvailable())
            return false;
        return (batteryLevel - mission.getDuration()) >= BATTERY_LEVEL_MIN;
    }

    @Override
    public boolean canHandle(Emergency emergency) {
        if (!super.isAvailable())
            return false;
        return batteryLevel > BATTERY_LEVEL_MIN;
    }

    @Override
    public void advanceTime(int timeUnit) {
        updateBatteryLevel(timeUnit);
    }
}
