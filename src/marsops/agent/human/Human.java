package marsops.agent.human;

import marsops.agent.Agent;
import marsops.facility.Facility;

public class Human extends Agent {
    final String id;
    String name;
    int fatigue;
    public Human(Facility originLocation, String name, String id) {
        super(originLocation);
        this.name = name;
        this.id = id;
        this.fatigue = 0;
    }
}
