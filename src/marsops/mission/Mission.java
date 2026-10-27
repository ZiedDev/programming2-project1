package marsops.mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import marsops.agent.Agent;
import marsops.resource.ResourceType;

public abstract class Mission {
    protected int duration;
    protected int elapsed;
    protected int requiredEquipment;
    private boolean isCommunicationDependent;
    protected MissionStatus status;
    protected String failureReason;
    protected Map<ResourceType, Integer> requiredResources;
    protected Map<Class<? extends Agent>, Integer> requiredAgents;
    protected List<Agent> assignedAgents;

    public Map<Class<? extends Agent>, Integer> getRequiredAgents() {
        return requiredAgents;
    }

    public Mission(int duration) {
        this.duration = duration;
        this.elapsed = 0;
        this.isCommunicationDependent = false;
        this.status = null;
        this.failureReason = "";
        this.requiredEquipment = 0;
        this.requiredResources = new HashMap<>();
        this.requiredAgents = new HashMap<>();
        this.assignedAgents = new ArrayList<>();
    }

    public abstract boolean validateRequirements(List<Agent> agents);
    
    protected boolean validateAgentsBasic(List<Agent> agents) {
        if (agents == null || agents.isEmpty()) {
            this.failureReason = "No agents were provided for the mission.";
            return false;
        }
        for (Agent agent : agents) {
            if (!agent.canPerform(this)) {//assuming that it handles availability as well
                this.failureReason = "Agent not available for mission.";
                return false;
            }
        }
        return true;
    }

    public void assignAgents(List<Agent> agents) {
        this.assignedAgents.clear();
        if (agents != null) {
            for (Agent agent : agents) {
                agent.setAvailable(false);
                agent.setAssignedMission(this);
                this.assignedAgents.add(agent);
            }
        }
        this.status = MissionStatus.ASSIGNED;
    }

    //time advancement according to state graph (I hope we make the graph </3)
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

    //complete and release agents
    public void complete() {
        this.status = MissionStatus.COMPLETED;
        releaseAgents();
    }

    //fail mission with message
    public void fail(String reason) {
        this.status = MissionStatus.FAILED;
        this.failureReason = (reason != null) ? reason : "Unknown failure reason.";
        releaseAgents();
    }

    //release agents duh (become free my slaves)
    protected void releaseAgents() {
        if (this.assignedAgents != null) {
            for (Agent agent : this.assignedAgents) {
                if (agent != null) {
                    agent.setAvailable(true);
                    agent.setAssignedMission(null);
                }
            }
        }
    }

    // TODO (This check should not be here please refer to AgentManger.canPerformMission)
    public List<Agent> findValidAgents(List<Agent> pool) {
        List<Agent> toBeAssigned = new ArrayList<>();
        if (pool == null || pool.isEmpty()) {
            this.failureReason = "No agents available in the colony.";
            return null;
        }
        // For each required agent type & count (e.g., Scientist -> 1, ExplorationRover -> 1)
        for (Map.Entry<Class<? extends Agent>, Integer> entry : this.requiredAgents.entrySet()) {
            Class<? extends Agent> requiredType = entry.getKey();
            int neededCount = entry.getValue();
            int foundCount = 0;
            for (Agent agent : pool) {
                if (requiredType.isInstance(agent) && agent.isAvailable() && agent.canPerform(this) && !toBeAssigned.contains(agent)) {
                    toBeAssigned.add(agent);
                    foundCount++;
                    if (foundCount == neededCount) {
                        break;
                    }
                }
            }
            //agents not fit >:[
            if (foundCount < neededCount) {
                this.failureReason = "Agent does not possess the required capability or is unavailable.";
                return null;
            }
        }
        return toBeAssigned;
    }

    //setters and getters

    public int getDuration() {
        return this.duration;
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

    public List<Agent> getAssignedAgents() {
        return this.assignedAgents;
    }

    public void addRequiredResource(ResourceType type, int amount) {
        if (amount > 0) {
            this.requiredResources.put(type, this.requiredResources.getOrDefault(type, 0) + amount);
        }
    }


}
