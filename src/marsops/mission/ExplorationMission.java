package marsops.mission;

import java.util.List;
import java.util.Map;

import marsops.resource.ResourceType;

public class ExplorationMission extends Mission {
    private String region;

    public ExplorationMission(int duration, String region, int requiredRoverCount, int requiredScientistCount, int requiredBatteryCount, int requiredEquipmentCount) {
        super(duration);
        this.region = region;
        this.requiredEquipment = requiredEquipmentCount;

        this.requiredAgents.put(ExplorationRover.class, Math.max(1,requiredRoverCount));
        this.requiredAgents.put(Scientist.class, Math.max(1,requiredScientistCount));
        this.addRequiredResource(ResourceType.BATTERY, requiredBatteryCount);
    }
    
    @Override
    public boolean validateRequirements(List<Agent> agents) {
        return validateAgentsBasic(agents);
    }
    public String getRegion() {
        return this.region;
    }
    public int getRequiredEquipment() {
        return this.requiredEquipment;
    }
}