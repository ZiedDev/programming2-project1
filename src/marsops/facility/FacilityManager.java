package marsops.facility;

import marsops.resource.ResourceType;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import marsops.mission.Mission;
import marsops.resource.resourceAllocationPolicy;

public class FacilityManager {
    private List<Facility> facilityList;
    private Map<ResourceType, Integer> totalResourceAmount;
    private int functionalEquipmentCount;

    public FacilityManager() {
        facilityList = new ArrayList<>();
        totalResourceAmount = new HashMap<>();
        functionalEquipmentCount = 0;
    }

    public boolean canProvide(Mission mission) {
        boolean can = true;
        for (Map.Entry<ResourceType, Integer> entry : totalResourceAmount.entrySet()) {
            ResourceType resource = entry.getKey();
            int available = entry.getValue();
            if (mission.getRequiredResources().get(resource) > available) {
                can = false;
            }
        }
        if (mission.getEquipmentCount() > functionalEquipmentCount) {
            can = false;
        }
        return can;
    }

    public void provide(Mission mission) {
        Map<ResourceType, Integer> requiredResources = mission.getRequiredResources();
        int requiredEquipment = mission.getEquipmentCount();
        for (Map.Entry<ResourceType, Integer> entry : requiredResources.entrySet()) { // deduct resources
            ResourceType resource = entry.getKey();
            int amount = entry.getValue();
            for (Class<? extends Facility> currClass : resourceAllocationPolicy.PRIORITY.get(resource)) {
                for (Facility x : facilityList) {
                    if (x.getClass() == currClass && x.isAvailable() && amount > 0) {
                        amount = x.deductResource(amount, resource);
                    } else if (amount <= 0) {
                        break;
                    }
                }
            }
        }
        for (Class<? extends Facility> currClass : resourceAllocationPolicy.PRIORITY_EQUIPMENT) { // deduct equipment
            for (Facility x : facilityList) {
                if (x.getClass() == currClass && x.isAvailable() && requiredEquipment > 0) {
                    requiredEquipment = x.deductEquipment(requiredEquipment);
                } else if (requiredEquipment <= 0) {
                    break;
                }
            }
        }
    }

}
