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
    private boolean isCommunicationDependent;
    protected MissionStatus status;
    protected String failureReason;
    protected Map<ResourceType, Integer> requiredResources;
    protected Map<Class<? extends Agent>, Integer> requiredAgents;
    protected List<Agent> assignedAgents;

    public Mission(int duration) {
        this.duration = duration;
        this.elapsed = 0;
        this.isCommunicationDependent = false;
        this.status = null;
        this.failureReason = "";
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
            if (!agent.canPerform(this)) {
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
                this.assignedAgents.add(agent);
            }
        }
        this.status = MissionStatus.ASSIGNED;
    }

    //time advancement according to state graph (i hope we make the graph </3)
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
                }
            }
        }
    }

    //setters and getters

    public int getDuration() {
        return this.duration;
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
