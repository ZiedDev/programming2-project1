package marsops.agent.human;

import java.util.List;

import marsops.emergency.Emergency;
import marsops.emergency.FireEmergency;
import marsops.emergency.MedicalEmergency;
import marsops.facility.Facility;
import marsops.mission.Mission;
import marsops.mission.RescueMission;
import marsops.mission.ResearchMission;

public class MedicalOfficer extends Human {

    private static final List<Class<? extends Mission>> ALLOWED_MISSIONS = List.of(
        ResearchMission.class,
        RescueMission.class
    );

    private static final List<Class<? extends Emergency>> ALLOWED_EMERGENCIES = List.of(
        FireEmergency.class,
        MedicalEmergency.class
    );

    public MedicalOfficer(Facility originLocation, String name, String id) {
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