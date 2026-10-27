package marsops.agent.robot;

import marsops.agent.Agent;
import marsops.facility.Facility;
import marsops.mission.Mission;

public class Robot extends Agent {
    static final int BATTERY_LEVEL_MAX = 100;
    static final int BATTERY_LEVEL_MIN = 0;

    int batteryLevel;

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

    public void reduceBatteryLevel(int time_unit) {
        this.batteryLevel = Math.max(this.batteryLevel - time_unit, BATTERY_LEVEL_MIN);
    }

    public void increaseBatteryLevel(int time_unit) {
        this.batteryLevel = Math.min(this.batteryLevel + time_unit, BATTERY_LEVEL_MAX);
    }

    public void updateBatteryLevel(int time_unit) {
        if (this.getAssignedMission() != null) {
            int time_left = this.getAssignedMission().getDuration() - this.getAssignedMission().getElapsed();
            reduceBatteryLevel(Math.min(time_left, time_unit));

            if (time_unit > time_left) {
                increaseBatteryLevel(time_unit - time_left);
            }
        } else {
            increaseBatteryLevel(time_unit);
        }
    }

    @Override
    public boolean canPerform(Mission mission) {
        if (!super.isAvailable())
            return false;
        return (batteryLevel - mission.getDuration()) >= BATTERY_LEVEL_MIN;
    }

    @Override
    public void advanceTime(int time_unit) {
        updateBatteryLevel(time_unit);
    }
}
