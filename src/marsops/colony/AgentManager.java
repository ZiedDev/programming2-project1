package marsops.colony;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import marsops.agent.Agent;
import marsops.agent.human.Engineer;
import marsops.agent.human.Human;
import marsops.agent.human.MedicalOfficer;
import marsops.agent.human.Scientist;
import marsops.agent.robot.Drone;
import marsops.agent.robot.ExplorationRover;
import marsops.agent.robot.RepairRobot;
import marsops.agent.robot.Robot;
import marsops.emergency.Emergency;
import marsops.facility.Facility;
import marsops.facility.Location;
import marsops.mission.Mission;
import marsops.mission.MissionStatus;
import marsops.mission.RescueMission;

public class AgentManager {
    private final ArrayList<Agent> agents;
    private final Map<Class<? extends Agent>, Integer> agentsCount;

    public AgentManager() {
        this.agents = new ArrayList<>();
        this.agentsCount = new HashMap<>();
    }

    private <T extends Agent> T register(T agent) {
        if (agent != null) {
            agents.add(agent);
            agentsCount.merge(agent.getClass(), 1, Integer::sum);
        }
        return agent;
    }

    // Creating a Human
    public Human createAgent(Class<? extends Human> type, Facility originLocation, String name, String id) {
        Human newAgent;
        if (type == Engineer.class) {
            newAgent = new Engineer(originLocation, name, id);
        } else if (type == MedicalOfficer.class) {
            newAgent = new MedicalOfficer(originLocation, name, id);
        } else if (type == Scientist.class) {
            newAgent = new Scientist(originLocation, name, id);
        } else {
            throw new IllegalArgumentException("Invalid agent: " + type);
        }
        return register(newAgent);
    }

    // Creating a Robot
    public Robot createAgent(Class<? extends Robot> type, Facility originLocation) {
        Robot newAgent;
        if (type == Drone.class) {
            newAgent = new Drone(originLocation);
        } else if (type == ExplorationRover.class) {
            newAgent = new ExplorationRover(originLocation);
        } else if (type == RepairRobot.class) {
            newAgent = new RepairRobot(originLocation);
        } else {
            throw new IllegalArgumentException("Invalid agent: " + type);
        }
        return register(newAgent);
    }

    public ArrayList<Agent> getAgents() {
        return agents;
    }

    public boolean canProvide(Mission mission) {
        if (agents.isEmpty()) {
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

    public boolean canProvide(Emergency emergency) {
        if (agents.isEmpty()) {
            return false;
        }

        Map<Class<? extends Agent>, Integer> required = emergency.getRequiredAgents();
        for (Class<? extends Agent> agentClass : required.keySet()) {
            int neededCount = required.get(agentClass);
            int totalOwnedCount = agentsCount.getOrDefault(agentClass, 0);

            if (totalOwnedCount < neededCount) {
                return false;
            }

            int capableCount = 0;
            for (Agent agent : agents) {
                if (agent.getClass().equals(agentClass) && agent.canHandle(emergency)) {
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
        Map<Class<? extends Agent>, Integer> requiredAgentType = mission.getRequiredAgents();

        for (Class<? extends Agent> agentType : requiredAgentType.keySet()) {
            int neededCount = requiredAgentType.get(agentType);

            for (Agent agent : agents) {
                if (neededCount == 0) {
                    break;
                }

                if (!agent.getClass().equals(agentType) || !agent.canPerform(mission)) {
                    continue;
                }

                agentsToProvide.add(agent);
                agent.setAvailable(false);
                agent.setAssignedMission(mission);
                if (mission.getTargetLocation() != null) {
                    relocate(agent, mission.getTargetLocation());
                }

                neededCount--;
            }
        }

        return agentsToProvide;
    }

    public void release(Mission mission) {
        ArrayList<Agent> assignedAgents = mission.getAssignedAgents();

        for (Agent agent : assignedAgents) {
            agent.setAvailable(true);
            if (agent.getHealth() > 0) {
                agent.setAvailable(true);
                relocate(agent, agent.getOriginLocation());
            }
        }
        if (mission instanceof RescueMission) {
            rescue(mission);
        }
    }

    public void rescue(Mission mission) {
        Agent rescued = mission.getTargetAgent();
        if (mission.getStatus() == MissionStatus.COMPLETED && rescued != null) {
            rescued.setHealth(100);
            rescued.setAvailable(true);
            rescued.setAssignedMission(null);
            relocate(rescued, rescued.getOriginLocation());
        }
    }

    public void relocate(Agent agent, Location location) {
        agent.setCurrentLocation(location);
    }

    public void relocate(ArrayList<Agent> agents, Location location) {
        for (Agent agent : agents) {
            agent.setCurrentLocation(location);
        }
    }

    public void advanceTime(int time_unit) {
        for (Agent agent : agents) {
            agent.advanceTime(time_unit);
        }
    }
}
