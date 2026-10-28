package marsops.emergency;

import marsops.agent.human.Human;
import marsops.agent.human.MedicalOfficer;
import marsops.facility.Facility;
import marsops.resource.ResourceType;

public class MedicalEmergency extends Emergency {
    private final Human patient;
    private final Facility treatmentFacility;

    public MedicalEmergency(
            Human patient,
            Facility treatmentFacility,
            int requiredMedicalSuppliesCount) {
        super(treatmentFacility);
        this.patient = patient;
        this.treatmentFacility = treatmentFacility;

        this.requiredAgents.put(MedicalOfficer.class, 1);
        this.addRequiredResource(ResourceType.MEDICAL, requiredMedicalSuppliesCount);
    }

    @Override
    public boolean validateRequirements() {
        if (this.patient == null) {
            this.failureReason = "No patient specified for medical emergency.";
            return false;
        }
        if (this.treatmentFacility == null || !treatmentFacility.isAvailable()) {
            this.failureReason = "Treatment facility is unavailable or non-operational.";
            return false;
        }
        return true;
    }

    @Override
    public void resolve() {
        super.resolve();
        patient.setHealth(100);
        patient.setFatigue(0);
    }
}
