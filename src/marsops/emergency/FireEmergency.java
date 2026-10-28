package marsops.emergency;

import marsops.agent.human.Engineer;
import marsops.facility.Laboratory;
import marsops.resource.Equipment;
import marsops.resource.ResourceType;

public class FireEmergency extends Emergency {
    private final Laboratory affectedLaboratory;

    public FireEmergency(
            Laboratory affectedLaboratory,
            int requiredWaterCount,
            int requiredMedicalSuppliesCount) {
        super(affectedLaboratory);
        this.affectedLaboratory = affectedLaboratory;

        this.requiredAgents.put(Engineer.class, 1);
        this.addRequiredResource(ResourceType.WATER, requiredWaterCount);
        this.addRequiredResource(ResourceType.MEDICAL, requiredMedicalSuppliesCount);

        for (Equipment i : this.affectedLaboratory.getEquipment()) {
            if (i.isAvailable()) {
                i.damage();
            }
        }
    }

    @Override
    public boolean validateRequirements() {
        if (this.affectedLaboratory == null || !this.affectedLaboratory.isAvailable()) {
            this.failureReason = "Affected laboratory is invalid or already non-operational.";
            return false;
        }
        return true;
    }

    @Override
    public void fail(String reason) {
        super.fail(reason);
        affectedLaboratory.setAvailability(false);
    }
}
