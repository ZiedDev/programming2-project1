package marsops.facility;

import marsops.resource.ResourceType;

public class Laboratory extends Facility {
    public Laboratory(String location, int capacity) {
        super(location, capacity);
        prioToAllocate[0] = ResourceType.BATTERY;
        prioToAllocate[1] = ResourceType.FUEL;
        prioToAllocate[2] = ResourceType.SPARE_PARTS;
        prioToAllocate[3] = ResourceType.OXYGEN;
        prioToAllocate[4] = ResourceType.MEDICAL;
        prioToAllocate[5] = ResourceType.FOOD;
        prioToAllocate[6] = ResourceType.WATER;

    }
}
