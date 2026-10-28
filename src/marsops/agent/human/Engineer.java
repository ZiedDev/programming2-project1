package marsops.agent.human;

import marsops.facility.Facility;
import marsops.mission.Mission;

public class Engineer extends Human {

    public Engineer(Facility originLocation, String name, String id) {
        super(originLocation, name, id);
    }

    @Override
    public boolean canPerform(Mission mission) {
        if (!super.canPerform(mission)) return false;
        // TODO: each agent should check what missions it can perform itself
        // not just relying on !agent.getClass().equals(agentType) in the AgentManager checking for it

        // (same for the rest of agents)
        return true;
    }
}