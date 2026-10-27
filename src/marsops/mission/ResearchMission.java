package marsops.mission;

import marsops.agent.human.Scientist;
import marsops.facility.Laboratory;
import marsops.resource.ResourceType;

public class ResearchMission extends Mission {
    private final Laboratory laboratory;

    public ResearchMission(int duration, Laboratory laboratory, int requiredScientistCount, int requiredBatteryCount,
            int requiredSparePartsCount, int requiredEquipmentCount) {
        super(duration);
        this.laboratory = laboratory;
        this.requiredEquipment = requiredEquipmentCount;
        this.requiredAgents.put(Scientist.class, Math.max(1, requiredScientistCount));
        this.addRequiredResource(ResourceType.BATTERY, requiredBatteryCount);
        this.addRequiredResource(ResourceType.SPARE_PARTS, requiredSparePartsCount);
    }

    @Override
    public boolean validateRequirements() {
        if (this.laboratory == null) {
            this.failureReason = "No laboratory found to use for research.";
            return false;
        }
        return true;
    }

    public Laboratory geLaboratory() {
        return this.laboratory;
    }
}
