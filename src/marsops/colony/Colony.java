package marsops.colony;

import java.util.List;
import marsops.agent.Agent;
import marsops.agent.AgentManager;
import marsops.facility.FacilityManager;
import marsops.mission.ExplorationMission;
import marsops.mission.Mission;
import marsops.mission.MissionStatus;

/**
 * Colony
 */
public class Colony {

    private Mission activeMission;
    private Environment environment;
    private List<Mission> pastMissions;
    private List<Agent> agents;
    public AgentManager agentManager;
    public FacilityManager facilityManager;

    public boolean submitMission(Mission mission) {
        if(mission == null){
            return false;
        }
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
        // check agents
        if(!this.agentManager.canProvide(mission)){
            rejectMission(mission, "Agents unavailable.");
            return false;
        }
        // check resources
        if(!this.facilityManager.canProvide(mission)){
            rejectMission(mission, "Resources unavailable.");
            return false;
        }
        // check mission internal validity
        if(!mission.validateRequirements()){
            rejectMission(mission, mission.getFailureReason());
            return false;
        }
        //valid mission yippie
        mission.assignAgents(this.agentManager.provide(mission)); //sets status = ASSIGNED and marks agents unavailable
        this.activeMission = mission;
        return true;
    }

    private void rejectMission(Mission mission, String reason) {
        mission.fail(reason);
        addCompletedMission(mission);
    }

    public void addCompletedMission(Mission mission){
        this.pastMissions.add(mission);
    }

    public void advanceTime(int time_unit){
        this.agentManager.tick(time_unit);
        if(this.activeMission!=null){
            this.activeMission.tick(time_unit);
            if(this.activeMission.getStatus()==MissionStatus.COMPLETED||this.activeMission.getStatus()==MissionStatus.FAILED){
                addCompletedMission(this.activeMission);
                this.agentManager.releaseAgents(this.activeMission.getAssignedAgents());
                this.activeMission=null;
            }
        }
    }
}