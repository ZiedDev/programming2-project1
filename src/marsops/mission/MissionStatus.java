package marsops.mission;

public enum MissionStatus {
    ASSIGNED("Assigned"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    FAILED("Failed");

    private final String value;

    MissionStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}