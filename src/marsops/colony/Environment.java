package marsops.colony;

/**
 * Environment
 */
public enum Environment {
    NORMAL("Normal"),
    DUST_STORM("Dust Storm"),
    COMM_DISRUPTION("Communication Disruption");

    private final String value;

    Environment(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}