package marsops.emergency;

import marsops.agent.human.Engineer;
import marsops.agent.robot.RepairRobot;
import marsops.facility.Facility;
import marsops.facility.Habitat;
import marsops.facility.Laboratory;
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

        // TODO: implement randomised
        this.affectedLaboratory.damageEquipment();
    }

    @Override
    public void fail(String reason) {
        super.fail(reason);
        affectedLaboratory.setAvailability(false);
    }
}
