package marsops.facility;

import marsops.resource.ResourceType;

public class Habitat extends Facility {
    public Habitat(String location, int capacity) {
        super(location, capacity);
        prioToAllocate[0] = ResourceType.OXYGEN;
        prioToAllocate[1] = ResourceType.WATER;
        prioToAllocate[2] = ResourceType.FOOD;
        prioToAllocate[3] = ResourceType.MEDICAL;
        prioToAllocate[4] = ResourceType.BATTERY;
        prioToAllocate[5] = ResourceType.FUEL;
        prioToAllocate[6] = ResourceType.SPARE_PARTS;
    }

}