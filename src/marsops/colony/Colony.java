package marsops.colony;

import java.util.List;

import marsops.mission.ExplorationMission;
import marsops.mission.Mission;

/**
 * Colony
 */
public class Colony {

    private Mission activeMission;
    private Environment environment;
    private List<Mission> pastMissions;
    private List<Agent> agents;

    public boolean submitMission(Mission mission) {
        //check if another mission is active
        if (this.activeMission != null) {
            rejectMission(mission, "Another mission is already active.");
            return false;
        }
        //check if dust storm for surface mission
        if (this.environment == Environment.DUST_STORM && mission instanceof ExplorationMission) {
            rejectMission(mission, "Surface operations are not allowed during a dust storm.");
            return false;
        }
        //check if mission is com dependent and theres com disruption
        if (this.environment == Environment.COMM_DISRUPTION && mission.isCommunicationDependent()) {
            rejectMission(mission, "Communication-dependent operations are not allowed during communication disruption.");
            return false;
        }
        // pick valid agents
        List<Agent> toBeAssigned = mission.findValidAgents(this.agents);// returns null if no valid agents found
        if (toBeAssigned == null || !mission.validateRequirements(toBeAssigned)) {
            rejectMission(mission, mission.getFailureReason());
            return false;
        }
        if (!consumeResources(mission.getRequiredResources(), mission.getEquipmentCount())) {
            rejectMission(mission, "Insufficient colony resources to start the mission.");
            return false;
        }
        //valid mission yippie
        mission.assignAgents(toBeAssigned); //sets status = ASSIGNED and marks agents unavailable
        this.activeMission = mission;
        return true;
    }

    private void rejectMission(Mission mission, String reason) {
        mission.fail(reason);
        this.pastMissions.add(mission);
    }
}