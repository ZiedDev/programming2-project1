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

    public boolean submitMission(Mission mission, List<Agent> agents) {
        agents=mission.getAssignedAgents();
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
        //check agent requirements
        if (!mission.validateRequirements(agents)) {
            // mission.validateRequirements() already set mission.failureReason
            rejectMission(mission, mission.getFailureReason());
            return false;
        }
        //check and consume colony-wide resources (all or nothing)
        if (!consumeResources(mission.getRequiredResources())) {//returns true if consumption successful
            rejectMission(mission, "Insufficient colony resources to start the mission.");
            return false;
        }
        //valid mission yippie
        mission.assignAgents(agents); //sets status = ASSIGNED and marks agents unavailable
        this.activeMission = mission;
        return true;
    }

    private void rejectMission(Mission mission, String reason) {
        mission.fail(reason);
        this.pastMissions.add(mission);
    }
}