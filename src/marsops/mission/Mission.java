package marsops.mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import marsops.agent.Agent;
import marsops.facility.Location;
import marsops.resource.ResourceType;

public abstract class Mission {
    private final int duration;
    private int elapsed;
    protected int requiredEquipment;
    private boolean isCommunicationDependent;
    private MissionStatus status;
    protected String failureReason;
    private final Map<ResourceType, Integer> requiredResources;
    protected final Map<Class<? extends Agent>, Integer> requiredAgents;
    private ArrayList<Agent> assignedAgents;
    protected Location targetLocation;
    protected Agent targetAgent;

    public Map<Class<? extends Agent>, Integer> getRequiredAgents() {
        return requiredAgents;
    }

    public Mission(int duration) {
        this.duration = Math.max(1, duration);
        this.elapsed = 0;
        this.isCommunicationDependent = false;
        this.status = null;
        this.failureReason = "";
        this.requiredEquipment = 0;
        this.requiredResources = new HashMap<>();
        this.requiredAgents = new HashMap<>();
        this.assignedAgents = new ArrayList<>();
    }

    public abstract boolean validateRequirements();

    public void assignAgents(ArrayList<Agent> agents) {
        this.assignedAgents = (agents != null) ? new ArrayList<>(agents) : new ArrayList<>();
        this.status = MissionStatus.ASSIGNED;
    }

    // time advancement according to state graph (I hope we make the graph </3)
    public void tick(int timeUnits) {
        if (this.status == MissionStatus.COMPLETED || this.status == MissionStatus.FAILED || this.status == null) {
            return;
        }

        if (this.status == MissionStatus.ASSIGNED) {
            this.status = MissionStatus.IN_PROGRESS;
        }

        this.elapsed += timeUnits;

        if (this.elapsed >= this.duration) {
            this.elapsed = this.duration;
            complete();
        }
    }

    // complete and release agents
    public void complete() {
        this.status = MissionStatus.COMPLETED;
    }

    // fail mission with message
    public void fail(String reason) {
        this.status = MissionStatus.FAILED;
        this.failureReason = (reason != null) ? reason : "Unknown failure reason.";
    }

    // setters and getters

    public int getDuration() {
        return this.duration;
    }

    public Agent getTargetAgent() {
        return this.targetAgent;
    }

    public Location getTargetLocation() {
        return this.targetLocation;
    }

    public int getEquipmentCount() {
        return this.requiredEquipment;
    }

    public int getElapsed() {
        return this.elapsed;
    }

    public boolean isCommunicationDependent() {
        return this.isCommunicationDependent;
    }

    public void setCommunicationDependent(boolean isCommunicationDependent) {
        this.isCommunicationDependent = isCommunicationDependent;
    }

    public MissionStatus getStatus() {
        return this.status;
    }

    public void setStatus(MissionStatus status) {
        this.status = status;
    }

    public String getFailureReason() {
        return this.failureReason;
    }

    public Map<ResourceType, Integer> getRequiredResources() {
        return this.requiredResources;
    }

    public ArrayList<Agent> getAssignedAgents() {
        return this.assignedAgents;
    }

    public void addRequiredResource(ResourceType type, int amount) {
        if (amount > 0) {
            this.requiredResources.put(type, this.requiredResources.getOrDefault(type, 0) + amount);
        }
    }

}
