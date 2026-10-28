package marsops.colony;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import marsops.agent.Agent;
import marsops.agent.human.Human;
import marsops.agent.robot.Robot;
import marsops.emergency.Emergency;
import marsops.facility.Facility;
import marsops.mission.ExplorationMission;
import marsops.mission.Mission;
import marsops.mission.MissionStatus;
import marsops.resource.ResourceType;

/**
 * Colony
 */
public class Colony {

    private static Colony instance;

    private Mission activeMission;
    private Environment environment;
    private final List<Mission> pastMissions;
    private final List<Emergency> pastEmergencies;

    private final AgentManager agentManager;
    private final FacilityManager facilityManager;
    private final PrintManager printManager;

    private Colony() {
        this.environment = Environment.NORMAL;
        this.activeMission = null;
        this.pastMissions = new ArrayList<>();
        this.pastEmergencies = new ArrayList<>();
        this.agentManager = new AgentManager();
        this.facilityManager = new FacilityManager();
        this.printManager = new PrintManager(this);
    }

    public static Colony getInstance() {
        if (instance == null) {
            instance = new Colony();
        }
        return instance;
    }

    public static void resetInstance() {
        Human.resetIds();
        instance = new Colony();
    }

    // no methods to hide + easier to call that delegation
    public PrintManager getPrintManager() {
        return this.printManager;
    }

    // facilityManager delegates
    public void addFacility(Facility x) {
        this.facilityManager.addFacility(x);
    }

    public List<Facility> getFacilities() {
        return this.facilityManager.getFacilities();
    }

    public Map<ResourceType, Integer> getTotalResources() {
        return this.facilityManager.getTotalResources();
    }

    public int getTotalEquipment() {
        return this.facilityManager.getTotalEquipment();
    }

    // agentManager delegates
    public Human createAgent(Class<? extends Human> type, Facility originLocation, String name, String id) {
        return this.agentManager.createAgent(type, originLocation, name, id);
    }

    // Creating a Robot
    public Robot createAgent(Class<? extends Robot> type, Facility originLocation) {
        return this.agentManager.createAgent(type, originLocation);
    }

    public void addAgent(Agent agent) {
        this.agentManager.addAgent(agent);
    }

    public ArrayList<Agent> getAgents() {
        return this.agentManager.getAgents();
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
        // check if mission is com dependent and there's com disruption
        if (this.environment == Environment.COMM_DISRUPTION && mission.isCommunicationDependent()) {
            rejectMission(mission,
                    "Communication-dependent operations are not allowed during communication disruption.");
            return false;
        }
        // check mission internal validity
        if (!mission.validateRequirements()) {
            rejectMission(mission, mission.getFailureReason());
            return false;
        }
        // check agents
        if (!this.agentManager.canProvide(mission)) {
            rejectMission(mission, "Required agents are unavailable or do not possess the required capability.");
            return false;
        }
        // check resources
        if (!this.facilityManager.canProvide(mission)) {
            rejectMission(mission, "Resources unavailable.");
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
        if (!this.agentManager.canProvide(emergency)) {
            rejectEmergency(emergency, "Agents unavailable.");
            return false;
        }
        // check resources
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

    private void addCompletedMission(Mission mission) {
        this.pastMissions.add(mission);
    }

    public void advanceTime(int timeUnit) {
        this.agentManager.advanceTime(timeUnit);
        if (this.activeMission != null) {
            if (this.environment == Environment.DUST_STORM && this.activeMission instanceof ExplorationMission) {
                this.activeMission.fail("Mission failed due to a dust storm.");
            } else if (this.environment == Environment.COMM_DISRUPTION
                    && this.activeMission.isCommunicationDependent()) {
                this.activeMission.fail("Mission failed due to communication disruption.");
            } else {
                this.activeMission.tick(timeUnit);
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

    public AgentManager getAgentManager() {
        return agentManager;
    }

    public FacilityManager getFacilityManager() {
        return facilityManager;
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
