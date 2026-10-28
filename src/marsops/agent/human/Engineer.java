package marsops.agent.human;

import java.util.List;

import marsops.emergency.Emergency;
import marsops.emergency.FireEmergency;
import marsops.emergency.OxygenLeakEmergency;
import marsops.emergency.RoverBreakdownEmergency;
import marsops.facility.Facility;
import marsops.mission.ExplorationMission;
import marsops.mission.Mission;
import marsops.mission.RepairMission;
import marsops.mission.RescueMission;

public class Engineer extends Human {

    private static final List<Class<? extends Mission>> ALLOWED_MISSIONS = List.of(
        RepairMission.class,
        RescueMission.class,
        ExplorationMission.class
    );

    private static final List<Class<? extends Emergency>> ALLOWED_EMERGENCIES = List.of(
        OxygenLeakEmergency.class,
        FireEmergency.class,
        RoverBreakdownEmergency.class
    );

    public Engineer(Facility originLocation, String name, String id) {
        super(originLocation, name, id);
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