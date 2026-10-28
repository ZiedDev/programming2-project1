package marsops.emergency;

import marsops.agent.human.Engineer;
import marsops.agent.robot.RepairRobot;
import marsops.facility.Habitat;
import marsops.resource.ResourceType;

public class OxygenLeakEmergency extends Emergency {
    private final Habitat affectedHabitat;
    private final int oxygenLoss;

    public OxygenLeakEmergency(
            Habitat affectedHabitat,
            int oxygenLoss,
            int requiredEngineerCount,
            int requiredRepairBotCount,
            int requiredSparePartsCount) {
        super(affectedHabitat);
        this.affectedHabitat = affectedHabitat;
        this.oxygenLoss = oxygenLoss;

        this.requiredAgents.put(Engineer.class, Math.max(1, requiredEngineerCount));
        this.requiredAgents.put(RepairRobot.class, Math.max(1, requiredRepairBotCount));
        this.addRequiredResource(ResourceType.SPARE_PARTS, requiredSparePartsCount);

        applyOxygenLoss();
    }

    private void applyOxygenLoss() {
        affectedHabitat.deductResource(oxygenLoss, ResourceType.OXYGEN);
    }

    @Override
    public boolean validateRequirements() {
        if (this.affectedHabitat == null || !this.affectedHabitat.isAvailable()) {
            this.failureReason = "Affected habitat is invalid or non-operational.";
            return false;
        }
        if (this.oxygenLoss < 0) {
            this.failureReason = "Oxygen loss amount cannot be negative.";
            return false;
        }
        return true;
    }

    @Override
    public void fail(String reason) {
        super.fail(reason);
        applyOxygenLoss();
    }
}
