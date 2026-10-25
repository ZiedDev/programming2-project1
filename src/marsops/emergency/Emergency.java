package marsops.emergency;

import java.util.List;
import java.util.Map;

import marsops.agent.Agent;
import marsops.resource.ResourceType;

public abstract class Emergency {
    protected EmergencyStatus status;
    protected String failureReason;
    
    protected final Map<Class<? extends Agent>, Integer> requiredAgents;
    protected final Map<ResourceType, Integer> requiredResources;
    protected final List<Agent> assignedResponders;
}
