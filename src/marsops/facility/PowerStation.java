package marsops.facility;

import marsops.resource.ResourceType;

public class PowerStation extends Facility {
    public PowerStation(String location, int capacity) {
        super(location, capacity);
        prioToAllocate[0] = ResourceType.FUEL;
        prioToAllocate[1] = ResourceType.BATTERY;
        prioToAllocate[2] = ResourceType.SPARE_PARTS;
        prioToAllocate[3] = ResourceType.OXYGEN;
        prioToAllocate[4] = ResourceType.FOOD;
        prioToAllocate[5] = ResourceType.WATER;
        prioToAllocate[6] = ResourceType.MEDICAL;
        //
    }
}
