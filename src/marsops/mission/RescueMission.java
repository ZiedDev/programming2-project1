package marsops.mission;

import marsops.agent.Agent;
import marsops.agent.human.MedicalOfficer;
import marsops.agent.robot.ExplorationRover;
import marsops.facility.SurfaceLocation;
import marsops.resource.ResourceType;

public class RescueMission extends Mission {
    private final Agent strandedAgent;

    // constructor for agent
    public RescueMission(int duration, Agent strandedAgent, int requiredRoverCount, int requiredMedicalOfficerCount,
            int requiredMedicalSuppliesCount, int requiredFuelCount) {
        super(duration);
        this.strandedAgent = strandedAgent;
        this.targetLocation = (strandedAgent != null) ? strandedAgent.getCurrentLocation() : null;
        this.setCommunicationDependent(true);
        this.targetAgent = strandedAgent;

        this.requiredAgents.put(ExplorationRover.class, Math.max(1, requiredRoverCount));
        this.requiredAgents.put(MedicalOfficer.class, Math.max(1, requiredMedicalOfficerCount));
        this.addRequiredResource(ResourceType.MEDICAL, requiredMedicalSuppliesCount);
        this.addRequiredResource(ResourceType.FUEL, requiredFuelCount);
    }

    // constructor for region
    public RescueMission(
            int duration,
            String targetRegion,
            int requiredRoverCount,
            int requiredMedicalOfficerCount,
            int requiredMedicalSuppliesCount,
            int requiredFuelCount) {
        super(duration);
        this.strandedAgent = null;
        this.targetLocation = (targetRegion != null) ? new SurfaceLocation(targetRegion) : null;
        this.setCommunicationDependent(true);

        this.requiredAgents.put(ExplorationRover.class, Math.max(1, requiredRoverCount));
        this.requiredAgents.put(MedicalOfficer.class, Math.max(1, requiredMedicalOfficerCount));
        this.addRequiredResource(ResourceType.MEDICAL, requiredMedicalSuppliesCount);
        this.addRequiredResource(ResourceType.FUEL, requiredFuelCount);
    }

    @Override
    public boolean validateRequirements() {
        if (this.targetLocation == null) {
            this.failureReason = "No valid target location or stranded entity specified for rescue.";
            return false;
        }
        return true;
    }

    public Agent getStrandedAgent() {
        return this.strandedAgent;
    }
}