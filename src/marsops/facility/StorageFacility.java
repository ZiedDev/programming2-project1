package marsops.facility;

import marsops.resource.ResourceType;

public class StorageFacility extends Facility {
    public StorageFacility(String location, int capacity) {
        super(location, capacity);
        prioToAllocate[0] = ResourceType.SPARE_PARTS;
        prioToAllocate[1] = ResourceType.BATTERY;
        prioToAllocate[2] = ResourceType.FUEL;
        prioToAllocate[3] = ResourceType.FOOD;
        prioToAllocate[4] = ResourceType.WATER;
        prioToAllocate[5] = ResourceType.OXYGEN;
        prioToAllocate[6] = ResourceType.MEDICAL;
            
    }
}