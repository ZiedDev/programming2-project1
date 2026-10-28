package marsops.agent.robot;

import java.util.List;

import marsops.emergency.Emergency;
import marsops.facility.Facility;
import marsops.mission.ExplorationMission;
import marsops.mission.Mission;
import marsops.mission.RepairMission;
import marsops.mission.RescueMission;

public class ExplorationRover extends Robot {
private static final List<Class<? extends Mission>> ALLOWED_MISSIONS = List.of(
        RepairMission.class,
        RescueMission.class,
        ExplorationMission.class
    );

    private static final List<Class<? extends Emergency>> ALLOWED_EMERGENCIES = List.of(
    );

    public ExplorationRover(Facility originLocation) {
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