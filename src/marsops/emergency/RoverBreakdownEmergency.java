package marsops.emergency;

import marsops.agent.human.Engineer;
import marsops.agent.human.Human;
import marsops.agent.human.MedicalOfficer;
import marsops.agent.robot.ExplorationRover;
import marsops.agent.robot.RepairRobot;
import marsops.facility.Facility;
import marsops.resource.ResourceType;

public class RoverBreakdownEmergency  extends Emergency {
    private final ExplorationRover damagedRover;

    public RoverBreakdownEmergency(
            ExplorationRover damagedRover,
            int requiredSparePartsCount) {
        super(damagedRover.getCurrentLocation());
        this.damagedRover = damagedRover;

        this.requiredAgents.put(Engineer.class, 1);
        this.requiredAgents.put(RepairRobot.class, 1);
        this.addRequiredResource(ResourceType.SPARE_PARTS, requiredSparePartsCount);
    }

    @Override
    public void resolve() {
        super.resolve();
        damagedRover.setHealth(100);
        damagedRover.setBatteryLevel(100);
        damagedRover.setAvailable(true);
    }
}
