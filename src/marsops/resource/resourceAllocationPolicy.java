package marsops.resource;

import marsops.facility.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class resourceAllocationPolicy {
    public static final Map<ResourceType, List<Class<? extends Facility>>> PRIORITY = new HashMap<>();
    public static final List<Class<? extends Facility>> PRIORITY_EQUIPMENT = new ArrayList<>();
    static {
        List<Class<? extends Facility>> oxygenPrio = new ArrayList<>();
        oxygenPrio.add(StorageFacility.class);
        oxygenPrio.add(Laboratory.class);
        oxygenPrio.add(PowerStation.class);
        oxygenPrio.add(Habitat.class);
        PRIORITY.put(ResourceType.OXYGEN, oxygenPrio);

        List<Class<? extends Facility>> waterPrio = new ArrayList<>();
        waterPrio.add(StorageFacility.class);
        waterPrio.add(Habitat.class);
        waterPrio.add(Laboratory.class);
        waterPrio.add(PowerStation.class);
        PRIORITY.put(ResourceType.WATER, waterPrio);

        List<Class<? extends Facility>> foodPrio = new ArrayList<>();
        foodPrio.add(StorageFacility.class);
        foodPrio.add(Habitat.class);
        foodPrio.add(Laboratory.class);
        foodPrio.add(PowerStation.class);
        PRIORITY.put(ResourceType.FOOD, foodPrio);

        List<Class<? extends Facility>> fuelPrio = new ArrayList<>();
        fuelPrio.add(StorageFacility.class);
        fuelPrio.add(PowerStation.class);
        fuelPrio.add(Laboratory.class);
        fuelPrio.add(Habitat.class);
        PRIORITY.put(ResourceType.FUEL, fuelPrio);

        List<Class<? extends Facility>> batteryPrio = new ArrayList<>();
        batteryPrio.add(StorageFacility.class);
        batteryPrio.add(PowerStation.class);
        batteryPrio.add(Laboratory.class);
        batteryPrio.add(Habitat.class);
        PRIORITY.put(ResourceType.BATTERY, batteryPrio);

        List<Class<? extends Facility>> medicalPrio = new ArrayList<>();
        fuelPrio.add(StorageFacility.class);
        fuelPrio.add(Laboratory.class);
        fuelPrio.add(Habitat.class);
        fuelPrio.add(PowerStation.class);
        PRIORITY.put(ResourceType.MEDICAL, fuelPrio);

        List<Class<? extends Facility>> sparePartsPrio = new ArrayList<>();
        sparePartsPrio.add(StorageFacility.class);
        sparePartsPrio.add(PowerStation.class);
        sparePartsPrio.add(Laboratory.class);
        sparePartsPrio.add(Habitat.class);
        PRIORITY.put(ResourceType.SPARE_PARTS, sparePartsPrio);

        PRIORITY_EQUIPMENT.add(StorageFacility.class);
        PRIORITY_EQUIPMENT.add(PowerStation.class);
        PRIORITY_EQUIPMENT.add(Laboratory.class);
        PRIORITY_EQUIPMENT.add(Habitat.class);

    }
}
