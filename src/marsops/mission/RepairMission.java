package marsops.mission;

import java.util.List;
import marsops.agent.Agent;
import marsops.agent.human.Engineer;
import marsops.agent.robot.RepairRobot;
import marsops.facility.Facility;
import marsops.resource.Equipment;
import marsops.resource.ResourceType;

public class RepairMission extends Mission {
    private Facility damagedFacility;
    private Equipment damagedEquipment;

    //gonna overload repair mission due to unknown repair target type: facility/equipment
    public RepairMission(int duration, Facility damagedFacility, int requiredEngineerCount, int requiredRepairBotCount, int requiredBatteryCount, int requiredSparePartsCount) {
        super(duration);
        this.damagedFacility = damagedFacility;
        this.damagedEquipment = null;
        this.requiredAgents.put(RepairRobot.class, Math.max(1,requiredRepairBotCount));
        this.requiredAgents.put(Engineer.class, Math.max(1,requiredEngineerCount));
        this.addRequiredResource(ResourceType.BATTERY, requiredBatteryCount);
        this.addRequiredResource(ResourceType.SPARE_PARTS, requiredSparePartsCount);
    }
    public RepairMission(int duration, Equipment damagedEquipment, int requiredEngineerCount, int requiredRepairBotCount, int requiredBatteryCount, int requiredSparePartsCount) {
        super(duration);
        this.damagedEquipment = damagedEquipment;
        this.damagedFacility = null;
        this.requiredAgents.put(RepairRobot.class, Math.max(1,requiredRepairBotCount));
        this.requiredAgents.put(Engineer.class, Math.max(1,requiredEngineerCount));
        this.addRequiredResource(ResourceType.BATTERY, requiredBatteryCount);
        this.addRequiredResource(ResourceType.SPARE_PARTS, requiredSparePartsCount);
    }
    
    @Override
    public boolean validateRequirements(List<Agent> agents) {
        if (this.damagedFacility == null && this.damagedEquipment == null) {
            this.failureReason = "No damaged facility or equipment specified for repair.";
            return false;
        }
        return validateAgentsBasic(agents);
    }
    @Override
    public void complete() {
        super.complete();
        if (this.damagedFacility != null) {
            this.damagedFacility.setAvailability(true);//assuming availability is the flag for fixed or not
        }
        if (this.damagedEquipment != null) {
            this.damagedEquipment.fix();
        }
    }
    @Override //override to change their location (only if they went to fix facility)
    public void assignAgents(List<Agent> agents) {
        super.assignAgents(agents);
        if(this.damagedFacility!=null){
            for (Agent agent : this.assignedAgents) {
                agent.setCurrentLocation(this.damagedFacility);
            }
        }
        else if(this.damagedEquipment!=null){
            for (Agent agent : this.assignedAgents) {
                //agent.setCurrentLocation(this.damagedFacility);
                //TODO: set location to same location as equipment (need to add location attribute to equipment)
            }
        }
    }
    public Facility getDamagedFacility(){
        return this.damagedFacility;
    }
    public Equipment getDamagedEquipment(){
        return this.damagedEquipment;
    }
}