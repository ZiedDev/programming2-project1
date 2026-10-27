package marsops.facility;

import marsops.resource.ResourceType;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import marsops.mission.Mission;

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

}
