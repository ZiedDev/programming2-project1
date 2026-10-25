package marsops.resource;

/**
 * ResourceType
 */
public enum ResourceType {
    OXYGEN("Oxygen"),
    WATER("Water"),
    FOOD("Food"),
    FUEL("Fuel"),
    BATTERY("Battery"),
    MEDICAL("Medical"),
    SPARE_PARTS("Spare Parts");

    private final String value;

    ResourceType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
