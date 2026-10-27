package marsops.facility;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import marsops.resource.Equipment;
import marsops.resource.ResourceType;

abstract public class Facility extends Location {
    private int capacity;
    private Map<ResourceType, Integer> resourceAmount;
    private List<Equipment> equipmentList;
    private final int id;
    private static int cnt = 0;
    private boolean isAvailable;
    private int totalTakenCapacity = 0;
    protected ResourceType[] prioToAllocate = new ResourceType[7];

    public Facility(String location, int capacity) {
        super(location);

        this.capacity = capacity;
        this.id = ++cnt;
        this.isAvailable = true;
        resourceAmount = new HashMap<>();
        equipmentList = new ArrayList<>();
        for (ResourceType i : ResourceType.values()) {
            this.resourceAmount.put(i, 0);
        }
        // default prio
        prioToAllocate[0] = ResourceType.OXYGEN;
        prioToAllocate[1] = ResourceType.WATER;
        prioToAllocate[2] = ResourceType.FOOD;
        prioToAllocate[3] = ResourceType.MEDICAL;
        prioToAllocate[4] = ResourceType.BATTERY;
        prioToAllocate[5] = ResourceType.FUEL;
        prioToAllocate[6] = ResourceType.SPARE_PARTS;
    }

    public void setAvailability(boolean state) {
        this.isAvailable = state;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public boolean isAvailable() {
        return this.isAvailable;
    }

    public Map<ResourceType, Integer> getResources() {
        return this.resourceAmount;
    }

    public List<Equipment> getEquipment() {
        return this.equipmentList;
    }

    public int getEquipmentCount() {
        int ans = 0;
        for (Equipment i : equipmentList) {
            if (i.isAvailable()) {
                ans++;
            }
        }
        return ans;
    }

    // public int getTotalTakenCapacity(){
    // int cnt=0;
    // for(int value : this.resourceAmount.values()){
    // cnt+=value;
    // }
    // cnt+=equipmentList.size();
    // return cnt;
    // }
    public int getTotalTakenCapacity() {
        return totalTakenCapacity;
    }

    public boolean addEquipment() { // return true if successfully allocated
        if (totalTakenCapacity < capacity) {
            totalTakenCapacity++;
            Equipment x = new Equipment();
            equipmentList.add(x);
            return true;
        }
        return false;
    }

    public int deductEquipment(int amount) { // returns number of equipment that still need to be deducted
        List<Equipment> toRemove = new ArrayList<>();
        for (Equipment i : equipmentList) {
            if (amount == 0) {
                break;
            }
            if (i.isAvailable()) {
                toRemove.add(i);
                amount--;
            }
        }
        equipmentList.removeAll(toRemove);
        return amount;
    }

    public Map<ResourceType, Integer> addResources(Map<ResourceType, Integer> resourcesToAllocate) { // returns
                                                                                                     // un-allocated
                                                                                                     // resources
        for (ResourceType i : prioToAllocate) {
            if (totalTakenCapacity < capacity) {
                int left = capacity - totalTakenCapacity;
                int toPut = Math.min(left, resourcesToAllocate.get(i));
                this.resourceAmount.put(i, resourceAmount.get(i) + toPut);
                totalTakenCapacity += toPut;
                resourcesToAllocate.put(i, resourcesToAllocate.get(i) - toPut);
            }
        }
        return resourcesToAllocate;
    }

    int deductResource(int amount, ResourceType resource) { // returns undeducted amount
        int toSubt = Math.min(this.resourceAmount.get(resource), amount);
        this.resourceAmount.put(resource, this.resourceAmount.get(resource) - toSubt);
        return amount - toSubt;
    }

}
