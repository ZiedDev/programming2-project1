package marsops.resource;

import marsops.facility.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;

public final class ResourceAllocationPolicy {
    private static final Map<ResourceType, List<Class<? extends Facility>>> PRIORITY;
    private static final List<Class<? extends Facility>> DEFAULT_PRIORITY;
    static {
        DEFAULT_PRIORITY = List.of(
                StorageFacility.class,
                PowerStation.class,
                Laboratory.class,
                Habitat.class);

        List<Class<? extends Facility>> oxygenPrio = List.of(
                StorageFacility.class,
                Laboratory.class,
                PowerStation.class,
                Habitat.class);

        List<Class<? extends Facility>> lifeSupportPrio = List.of(
                StorageFacility.class,
                Habitat.class,
                Laboratory.class,
                PowerStation.class);

        List<Class<? extends Facility>> medicalPrio = List.of(
                StorageFacility.class,
                Laboratory.class,
                Habitat.class,
                PowerStation.class);

        Map<ResourceType, List<Class<? extends Facility>>> map = new HashMap<>();
        map.put(ResourceType.OXYGEN, oxygenPrio);
        map.put(ResourceType.WATER, lifeSupportPrio);
        map.put(ResourceType.FOOD, lifeSupportPrio);
        map.put(ResourceType.FUEL, DEFAULT_PRIORITY);
        map.put(ResourceType.BATTERY, DEFAULT_PRIORITY);
        map.put(ResourceType.SPARE_PARTS, DEFAULT_PRIORITY);
        map.put(ResourceType.MEDICAL, medicalPrio);

        PRIORITY = Collections.unmodifiableMap(map);

    }

    private ResourceAllocationPolicy() {
    }

    public static List<Class<? extends Facility>> getPriority(ResourceType resourceType) {
        return PRIORITY.getOrDefault(resourceType, DEFAULT_PRIORITY);
    }

    public static List<Class<? extends Facility>> getEquipmentPriority() {
        return DEFAULT_PRIORITY;
    }
}