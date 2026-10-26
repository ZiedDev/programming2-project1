package marsops.mission;

import java.util.List;
import marsops.agent.Agent;
import marsops.agent.human.Scientist;
import marsops.agent.robot.ExplorationRover;
import marsops.facility.SurfaceLocation;
import marsops.resource.ResourceType;

public class ExplorationMission extends Mission {
    private String region;

    public ExplorationMission(int duration, String region, int requiredRoverCount, int requiredScientistCount, int requiredBatteryCount, int requiredEquipmentCount, int requiredFuel, int requiredOxygen, int requiredWater, int requiredFood) {
        super(duration);
        this.region = region;
        this.requiredEquipment = requiredEquipmentCount;

        this.requiredAgents.put(ExplorationRover.class, Math.max(1,requiredRoverCount));
        this.requiredAgents.put(Scientist.class, Math.max(1,requiredScientistCount));
        this.addRequiredResource(ResourceType.BATTERY, requiredBatteryCount);
        this.addRequiredResource(ResourceType.FUEL, requiredFuel);
        this.addRequiredResource(ResourceType.OXYGEN, requiredOxygen);
        this.addRequiredResource(ResourceType.WATER, requiredWater);
        this.addRequiredResource(ResourceType.FOOD, requiredFood);
    }
    
    @Override
    public boolean validateRequirements(List<Agent> agents) {
        return validateAgentsBasic(agents);
    }
    @Override //override to change their location
    public void assignAgents(List<Agent> agents) {
        super.assignAgents(agents);
        for (Agent agent : this.assignedAgents) {
            agent.setCurrentLocation(new SurfaceLocation(region));
        }
    }
    public String getRegion() {
        return this.region;
    }
}