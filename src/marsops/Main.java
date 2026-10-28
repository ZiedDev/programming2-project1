package marsops;

import java.util.HashMap;
import java.util.Map;

import marsops.agent.AgentTypes;
import marsops.colony.Colony;
import marsops.colony.Environment;
import marsops.emergency.OxygenLeakEmergency;
import marsops.facility.Habitat;
import marsops.facility.StorageFacility;
import marsops.mission.ExplorationMission;
import marsops.resource.ResourceType;

/**
 * Main
 */
public class Main {

    private static void setupColony(Colony colony) {
        Habitat habitat = new Habitat("Alpha Base", 100);
        StorageFacility storage = new StorageFacility("Main Depot", 500);
        Map<ResourceType, Integer> habRes = new HashMap<>();
        habRes.put(ResourceType.OXYGEN, 300);
        habRes.put(ResourceType.WATER, 100);
        habRes.put(ResourceType.FOOD, 100);
        habitat.addResources(habRes);
        Map<ResourceType, Integer> storageResources = new HashMap<>();
        storageResources.put(ResourceType.BATTERY, 200);
        storageResources.put(ResourceType.FUEL, 200);
        storageResources.put(ResourceType.SPARE_PARTS, 50);
        storage.addResources(storageResources);
        colony.getFacilityManager().addFacility(habitat);
        colony.getFacilityManager().addFacility(storage);
        colony.getAgentManager().createAgent(AgentTypes.SCIENTIST, habitat, "Dr. Alice", "S-01");
        colony.getAgentManager().createAgent(AgentTypes.ENGINEER, habitat, "Bob", "E-01");
        colony.getAgentManager().createAgent(AgentTypes.EXPLORATION_ROVER, habitat);
        colony.getAgentManager().createAgent(AgentTypes.REPAIR_ROBOT, storage);
    }

    // Test 1: Successful Exploration
    private static void runTest1() {
        System.out.println("\n___Test 1: SUCCESSFUL EXPLORATION___");
        Colony.resetInstance();
        Colony colony = Colony.getInstance();
        setupColony(colony);
        colony.getPrintManager().printReports();
        ExplorationMission mission = new ExplorationMission(2, "Gale Crater", 1, 1, 10, 0, 10, 10, 5, 5);
        boolean accepted = colony.submitMission(mission);
        System.out.println("Mission submitted: " + accepted);
        colony.advanceTime(1);
        colony.advanceTime(1);
        colony.getPrintManager().printMissionOutcome(mission);
        colony.getPrintManager().printReports();
    }

    // Test 2: Mission Rejection (Unsuitable Agent)
    private static void runTest2() {
        System.out.println("\n___Test 2: MISSION REJECTION___");
        Colony.resetInstance();
        Colony colony = Colony.getInstance();
        setupColony(colony);
        // Submit mission requiring agents that do not exist
        ExplorationMission mission = new ExplorationMission(2, "Chryse Planitia", 5, 5, 10, 0, 10, 10, 5, 5);
        colony.submitMission(mission);
        colony.getPrintManager().printMissionOutcome(mission);
    }

    // Test 3: Resource Shortage
    private static void runTest3() {
        System.out.println("\n___Test 3: RESOURCE SHORTAGE___");
        Colony.resetInstance();
        Colony colony = Colony.getInstance();
        setupColony(colony);
        // Request impossible quantity of fuel
        ExplorationMission mission = new ExplorationMission(2, "Valles Marineris", 1, 1, 10, 0, 99999, 10, 5, 5);
        colony.submitMission(mission);
        colony.getPrintManager().printMissionOutcome(mission);
        colony.getPrintManager().printTotalResources();
    }

    // Test 4: Emergency Handling (Oxygen Leak)
    private static void runTest4() {
        System.out.println("\n___Test 4: EMERGENCY HANDLING___");
        Colony.resetInstance();
        Colony colony = Colony.getInstance();
        setupColony(colony);
        Habitat hab = (Habitat) colony.getFacilityManager().getFacilities().getFirst();
        OxygenLeakEmergency leak = new OxygenLeakEmergency(hab, 50, 1, 1, 2);
        colony.submitEmergency(leak);
        colony.getPrintManager().printEmergencyOutcome(leak);
        colony.getPrintManager().printTotalResources();
    }

    // Test 5: Environmental Effect (Dust Storm)
    private static void runTest5() {
        System.out.println("\n___Test 5: ENVIRONMENTAL EFFECT___");
        Colony.resetInstance();
        Colony colony = Colony.getInstance();
        setupColony(colony);
        colony.setEnvironment(Environment.DUST_STORM);
        colony.getPrintManager().printEnvironment();
        ExplorationMission mission = new ExplorationMission(2, "Acidalia Planitia", 1, 1, 10, 0, 10, 10, 5, 5);
        colony.submitMission(mission);
        colony.getPrintManager().printMissionOutcome(mission);
    }

    public static void main(String[] args) {
        System.out.println("___MARS-OPS TESTS START___");
        runTest1();
        runTest2();
        runTest3();
        runTest4();
        runTest5();
    }
}