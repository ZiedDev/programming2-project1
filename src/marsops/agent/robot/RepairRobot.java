package marsops.agent.robot;

import java.util.List;

import marsops.emergency.Emergency;
import marsops.emergency.FireEmergency;
import marsops.emergency.OxygenLeakEmergency;
import marsops.emergency.RoverBreakdownEmergency;
import marsops.facility.Facility;
import marsops.mission.Mission;
import marsops.mission.RepairMission;

public class RepairRobot extends Robot {

    private static final List<Class<? extends Mission>> ALLOWED_MISSIONS = List.of(
        RepairMission.class
    );

    private static final List<Class<? extends Emergency>> ALLOWED_EMERGENCIES = List.of(
        OxygenLeakEmergency.class,
        FireEmergency.class,
        RoverBreakdownEmergency.class
    );

    public RepairRobot(Facility originLocation) {
        super(originLocation);
    }

    @Override
    protected List<Class<? extends Mission>> getAllowedMissions() {
        return ALLOWED_MISSIONS;
    }

    @Override
    protected List<Class<? extends Emergency>> getAllowedEmergencies() {
        return ALLOWED_EMERGENCIES;
    }
}