package marsops.colony;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import marsops.emergency.Emergency;
import marsops.facility.Facility;
import marsops.mission.Mission;
import marsops.resource.ResourceType;
import marsops.resource.ResourceAllocationPolicy;

public class FacilityManager {
    private final List<Facility> facilityList;

    public FacilityManager() {
        facilityList = new ArrayList<>();
    }

    public void addFacility(Facility x) {
        facilityList.add(x);
    }

    public List<Facility> getFacilities() {
        return this.facilityList;
    }

    public Map<ResourceType, Integer> getTotalResources() {
        Map<ResourceType, Integer> ans = new HashMap<>();
        for (Facility x : facilityList) {
            if (x.isAvailable()) {
                x.getResources().forEach((key, value) -> ans.merge(key, value, Integer::sum));
            }
        }
        return ans;
    }

    public int getTotalEquipment() {
        int ans = 0;
        for (Facility x : facilityList) {
            if (x.isAvailable()) {
                ans = ans + x.getEquipmentCount();
            }
        }
        return ans;
    }

    public boolean canProvide(Mission mission) {
        return canProvide(mission.getRequiredResources(), mission.getEquipmentCount());
    }

    public boolean canProvide(Emergency emergency) {
        return canProvide(emergency.getRequiredResources(), 0);
    }

    private boolean canProvide(Map<ResourceType, Integer> requiredResources, int requiredEquipment) {
        boolean can = true;
        Map<ResourceType, Integer> availableResources = getTotalResources();
        for (Map.Entry<ResourceType, Integer> entry : requiredResources.entrySet()) {
            ResourceType resource = entry.getKey();
            int available = availableResources.getOrDefault(resource, 0);
            int required = entry.getValue();
            if (required > available) {
                can = false;
            }
        }
        if (requiredEquipment > getTotalEquipment()) {
            can = false;
        }
        return can;
    }

    public void provide(Emergency emergency) {
        Map<ResourceType, Integer> requiredResources = emergency.getRequiredResources();
        int requiredEquipment = 0; // does emergency really need no equipment ?
        if (!canProvide(requiredResources, requiredEquipment)) {
            return;
        }
        provide(requiredResources, requiredEquipment);
    }

    public void provide(Mission mission) {
        Map<ResourceType, Integer> requiredResources = mission.getRequiredResources();
        int requiredEquipment = mission.getEquipmentCount();
        if (!canProvide(requiredResources, requiredEquipment)) {
            return;
        }
        provide(requiredResources, requiredEquipment);
    }

    private void provide(Map<ResourceType, Integer> requiredResources, int requiredEquipment) {
        for (Map.Entry<ResourceType, Integer> entry : requiredResources.entrySet()) { // deduct resources
            ResourceType resource = entry.getKey();
            int amount = entry.getValue();
            for (Class<? extends Facility> currClass : ResourceAllocationPolicy.getPriority(resource)) {
                for (Facility x : facilityList) {
                    if (x.getClass() == currClass && x.isAvailable() && amount > 0) {
                        amount = x.deductResource(amount, resource);
                    } else if (amount <= 0) {
                        break;
                    }
                }
            }
        }
        for (Class<? extends Facility> currClass : ResourceAllocationPolicy.getEquipmentPriority()) { // deduct
                                                                                                      // equipment
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
