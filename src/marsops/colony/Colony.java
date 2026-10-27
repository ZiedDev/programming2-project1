package marsops.colony;

import java.util.ArrayList;
import java.util.List;
import marsops.agent.Agent;
import marsops.agent.AgentManager;
import marsops.emergency.Emergency;
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
    private List<Emergency> pastEmergencies;

    // TODO: public -> private + delegates
    public AgentManager agentManager;
    public FacilityManager facilityManager;

    // TODO: singleton?
    public Colony() {
        this.environment = Environment.NORMAL;
        this.activeMission = null;
        this.pastMissions = new ArrayList<>();
        this.pastEmergencies = new ArrayList<>();
        this.agentManager = new AgentManager();
        this.facilityManager = new FacilityManager();
    }

    public boolean submitMission(Mission mission) {
        if (mission == null) {
            return false;
        }
        // check if another mission is active
        if (this.activeMission != null) {
            rejectMission(mission, "Another mission is already active.");
            return false;
        }
        // check if dust storm for surface mission
        if (this.environment == Environment.DUST_STORM && mission instanceof ExplorationMission) {
            rejectMission(mission, "Surface operations are not allowed during a dust storm.");
            return false;
        }
        // check if mission is com dependent and theres com disruption
        if (this.environment == Environment.COMM_DISRUPTION && mission.isCommunicationDependent()) {
            rejectMission(mission,
                    "Communication-dependent operations are not allowed during communication disruption.");
            return false;
        }
        // check agents
        if (!this.agentManager.canProvide(mission)) {
            rejectMission(mission, "Agents unavailable.");
            return false;
        }
        // check resources
        if (!this.facilityManager.canProvide(mission)) {
            rejectMission(mission, "Resources unavailable.");
            return false;
        }
        // check mission internal validity
        if (!mission.validateRequirements()) {
            rejectMission(mission, mission.getFailureReason());
            return false;
        }
        // valid mission yippie
        mission.assignAgents(this.agentManager.provide(mission)); // sets status = ASSIGNED and marks agents unavailable
        this.facilityManager.provide(mission);
        this.activeMission = mission;
        return true;
    }

    public boolean submitEmergency(Emergency emergency) {
        if (emergency == null) {
            return false;
        }
        // check agents
        // TODO: overload to accept emergency too
        if (!this.agentManager.canProvide(emergency)) {
            rejectEmergency(emergency, "Agents unavailable.");
            return false;
        }
        // check resources
        // TODO: overload to accept emergency too
        if (!this.facilityManager.canProvide(emergency)) {
            rejectEmergency(emergency, "Resources unavailable.");
            return false;
        }
        // check emergency internal validity
        if (!emergency.validateRequirements()) {
            rejectEmergency(emergency, emergency.getFailureReason());
            return false;
        }

        // valid emergency
        this.facilityManager.provide(emergency);
        emergency.resolve();
        this.pastEmergencies.add(emergency);
        return true;
    }

    private void rejectMission(Mission mission, String reason) {
        mission.fail(reason);
        addCompletedMission(mission);
    }

    private void rejectEmergency(Emergency emergency, String reason) {
        emergency.fail(reason);
        this.pastEmergencies.add(emergency);
    }

    // TODO: why public method?
    public void addCompletedMission(Mission mission) {
        this.pastMissions.add(mission);
    }

    public void advanceTime(int time_unit) {
        this.agentManager.advanceTime(time_unit);
        if (this.activeMission != null) {
            if (this.environment == Environment.DUST_STORM && this.activeMission instanceof ExplorationMission) {
                this.activeMission.fail("Mission failed due to a dust storm.");
            } else if (this.environment == Environment.COMM_DISRUPTION
                    && this.activeMission.isCommunicationDependent()) {
                this.activeMission.fail("Mission failed due to communication disruption.");
            } else {
                this.activeMission.tick(time_unit);
            }
            if (this.activeMission.getStatus() == MissionStatus.COMPLETED
                    || this.activeMission.getStatus() == MissionStatus.FAILED) {
                addCompletedMission(this.activeMission);
                this.agentManager.release(this.activeMission);
                this.activeMission = null;
            }
        }
    }

    public List<Mission> getPastMissions() {
        return this.pastMissions;
    }

    public List<Emergency> getPastEmergencies() {
        return this.pastEmergencies;
    }

    public Environment getEnvironment() {
        return this.environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Mission getActiveMission() {
        return this.activeMission;
    }
}
