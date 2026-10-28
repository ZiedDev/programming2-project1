package marsops.colony;

import java.util.Map;
import java.util.Objects;

import marsops.agent.Agent;
import marsops.emergency.Emergency;
import marsops.emergency.EmergencyStatus;
import marsops.mission.Mission;
import marsops.mission.MissionStatus;
import marsops.resource.ResourceType;

public class PrintManager {
    private Colony colony;

    public PrintManager(Colony colony) {
        this.colony = Objects.requireNonNull(colony, "Colony must not be null");;
    }

    // Print Total Resources
    public void printTotalResources() {
        System.out.println("[Total Resources]");
        Map<ResourceType, Integer> totals = colony.getTotalResources();
        for (ResourceType type : ResourceType.values()) {
            System.out.println(type + ": " + totals.getOrDefault(type, 0));
        }
        System.out.println("Equipment: " + colony.getTotalEquipment());
        System.out.println();
    }

    // Print Reports
    public void printReports() {
        System.out.println("[Colony Status Report]");
        printEnvironment();
        printTotalResources();

        Mission active = colony.getActiveMission();
        if (active != null) {
            System.out.println("Active Mission: " + active.getClass().getSimpleName() + " (" + active.getStatus() + ")");
        } else {
            System.out.println("Active Mission: None");
        }

        System.out.println();


        System.out.println("[Agents]");
        for (Agent agent : colony.getAgents()) {
            System.out.println(agent.getClass().getSimpleName() + " - Health: " + agent.getHealth()
                    + " - Available: " + agent.isAvailable());
        }
        System.out.println();
    }

    // Print Environment
    public void printEnvironment() {
        System.out.println("[Environment]");
        System.out.println("Current Environment: " + colony.getEnvironment());
        if (colony.getEnvironment() == Environment.DUST_STORM) {
            System.out.println("Warning: Surface operations are not allowed during a dust storm.");
        } else if (colony.getEnvironment() == Environment.COMM_DISRUPTION) {
            System.out.println("Warning: Communication disruption in effect. Remote operations disabled.");
        } else {
            System.out.println("Conditions: Normal.");
        }
        System.out.println();
    }

    // Print Mission and Emergency outcomes
    public void printMissionOutcome(Mission mission) {
        if (mission == null) {
            return;
        }
        System.out.println("[Mission Outcome]");
        System.out.println("Mission: " + mission.getClass().getSimpleName());
        System.out.println("Status: " + mission.getStatus());
        if (mission.getStatus() == MissionStatus.FAILED) {
            System.out.println("Reason: " + mission.getFailureReason());
        }
        System.out.println();
    }

    public void printEmergencyOutcome(Emergency emergency) {
        if (emergency == null) {
            return;
        }
        System.out.println("[Emergency Outcome]");
        System.out.println("Emergency: " + emergency.getClass().getSimpleName());
        System.out.println("Status: " + emergency.getStatus());
        if (emergency.getStatus() == EmergencyStatus.UNRESOLVED) {
            System.out.println("Reason: " + emergency.getFailureReason());
        }
        System.out.println();
    }

    public void printMissionAndEmergencyOutcomes() {
        System.out.println("[Mission Outcomes]");
        for (Mission mission : colony.getPastMissions()) {
            printMissionOutcome(mission);
        }
        System.out.println("[Emergency Outcomes]");
        for (Emergency emergency : colony.getPastEmergencies()) {
            printEmergencyOutcome(emergency);
        }
    }
}
