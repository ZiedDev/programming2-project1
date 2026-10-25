package marsops.agent.robot;

import marsops.agent.Agent;
import marsops.facility.Facility;

public class Robot extends Agent {
    int batteryLevel;
    public Robot(Facility originLocation) {
        super(originLocation);
        this.batteryLevel = 100;
    }
}
