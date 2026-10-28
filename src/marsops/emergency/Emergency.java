package marsops.emergency;

import java.util.HashMap;
import java.util.Map;
import marsops.agent.Agent;
import marsops.facility.Location;
import marsops.resource.ResourceType;

public abstract class Emergency {
    protected EmergencyStatus status;
    protected String failureReason;
    protected Location location;

    protected final Map<Class<? extends Agent>, Integer> requiredAgents;
    protected final Map<ResourceType, Integer> requiredResources;

    public Emergency(Location location) {
        this.status = null;
        this.failureReason = "";
        this.location = location;

        this.requiredAgents = new HashMap<>();
        this.requiredResources = new HashMap<>();
    }

    public abstract boolean validateRequirements();

    public String getFailureReason() {
        return this.failureReason;
    }

    public void resolve() {
        this.status = EmergencyStatus.RESOLVED;
    }

    public void fail(String reason) {
        this.status = EmergencyStatus.UNRESOLVED;
        this.failureReason = (reason != null) ? reason : "Unknown failure reason.";
    }

    public Map<Class<? extends Agent>, Integer> getRequiredAgents() {
        return requiredAgents;
    }

    public Map<ResourceType, Integer> getRequiredResources() {
        return this.requiredResources;
    }

    public void addRequiredResource(ResourceType type, int amount) {
        if (amount > 0) {
            this.requiredResources.put(type, this.requiredResources.getOrDefault(type, 0) + amount);
        }
    }

    public EmergencyStatus getStatus() {
        return this.status;
    }

    public Location getLocation() {
        return location;
    }
}
