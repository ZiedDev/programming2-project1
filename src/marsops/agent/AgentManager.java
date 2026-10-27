package marsops.agent;

import marsops.agent.human.Engineer;
import marsops.agent.human.Human;
import marsops.agent.human.MedicalOfficer;
import marsops.agent.human.Scientist;
import marsops.agent.robot.Drone;
import marsops.agent.robot.ExplorationRover;
import marsops.agent.robot.RepairRobot;
import marsops.agent.robot.Robot;
import marsops.facility.Facility;
import marsops.mission.Mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AgentManager {
    private ArrayList<Agent> agents;
    private Map<Class<? extends Agent>, Integer> agentsCount;

    public AgentManager() {
        this.agents = new ArrayList<>();
        this.agentsCount = new HashMap<>();
    }

    public AgentManager(Map<Class<? extends Agent>, Integer> agentsCount) {
        this.agentsCount = agentsCount;
    }

    public AgentManager(ArrayList<Agent> agents) {
        this.agents = agents;
    }

    private <T extends Agent> T register(T agent) {
        if (agent != null) {
            agents.add(agent);
            agentsCount.merge(agent.getClass(), 1, Integer::sum);
        }
        return agent;
    }

    // Creating a Human
    public Human createAgent(AgentTypes type, Facility originLocation, String name, String id) {
        return register(switch (type) {
            case ENGINEER -> new Engineer(originLocation, name, id);
            case MEDICAL_OFFICER -> new MedicalOfficer(originLocation, name, id);
            case SCIENTIST -> new Scientist(originLocation, name, id);
            default -> null;
        });
    }

    // Creating a Robot
    public Robot createAgent(AgentTypes type, Facility originLocation) {
        return register(switch (type) {
            case DRONE -> new Drone(originLocation);
            case EXPLORATION_ROVER -> new ExplorationRover(originLocation);
            case REPAIR_ROBOT -> new RepairRobot(originLocation);
            default -> null;
        });
    }

    public ArrayList<Agent> getAgents() {
        return agents;
    }

    public boolean canProvide(Mission mission) {
        if (agents == null || agents.isEmpty()) {
            return false;
        }

        Map<Class<? extends Agent>, Integer> required = mission.getRequiredAgents();
        for (Class<? extends Agent> agentClass : required.keySet()) {
            int neededCount = required.get(agentClass);
            int totalOwnedCount = agentsCount.getOrDefault(agentClass, 0);

            if (totalOwnedCount < neededCount) {
                return false;
            }

            int capableCount = 0;
            for (Agent agent : agents) {
                if (agent.getClass().equals(agentClass) && agent.canPerform(mission)) {
                    capableCount++;
                }
            }

            if (capableCount < neededCount) {
                return false;
            }
        }

        return true;
    }

    public ArrayList<Agent> provide(Mission mission) {
        if (!this.canProvide(mission)) {
            return null;
        }

        ArrayList<Agent> agentsToProvide = new ArrayList<>();
        Map<Class<? extends Agent>, Integer> required = mission.getRequiredAgents();

        for (Class<? extends Agent> agentClass : required.keySet()) {
            int neededCount = required.get(agentClass);

            for (Agent agent : agents) {
                if (neededCount == 0) {
                    break;
                }

                if (!agent.getClass().equals(agentClass) || !agent.canPerform(mission)) {
                    continue;
                }

                agentsToProvide.add(agent);
                agent.setAvailable(false);
                agent.setAssignedMission(mission);

                neededCount--;
            }
        }

        return agentsToProvide;
    }
}
