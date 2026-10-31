# Mars Colony Operations Management System

<!-- TODO: table of contents -->

## 1. System description and main components

This project aims to model a colony on mars and the components that make it up, including: human crew members, robotic units, different types of facilities aswell as missions and emergencies that interact with them

### Main Packages
1. **`marsops.colony`**:
   - `Colony`: Singleton class that allows fro mission submission, emergency handling, and simulation time advancement.
   - `AgentManager`: Manages human crew and robotic units, and tracks their availability and assignment to missions.
   - `FacilityManager`: Manages operational facilities, aggregates colony-wide resource and equipment totals.
   - `PrintManager`: Groups all methods relating to printing colony info including: reports, enviroment, agents, past missions and emergencies.
2. **`marsops.agent`**:
   - `Agent`: Abstract base entity with health, availability, location, and assigned mission tracking.
   - `Human` (`Engineer`, `Scientist`, `MedicalOfficer`): Track fatigue levels and personal identification numbers.
   - `Robot` (`Drone`, `ExplorationRover`, `RepairRobot`): Track battery levels and autonomous capabilities.
3. **`marsops.facility`**:
   - `Location`: Abstract coordinate/place marker. Specialized into `SurfaceLocation` and `Facility`.
   - `Facility` (`Habitat`, `Laboratory`, `PowerStation`, `StorageFacility`): Maintain resource storage, equipment inventories, capacities, and availability flags.
4. **`marsops.resource`**:
   - `ResourceType`: Enum covering `OXYGEN`, `WATER`, `FOOD`, `FUEL`, `BATTERY`, `SPARE_PARTS`, and `MEDICAL`.
   - `ResourceAllocationPolicy`: Defines the priorities the facilities use for each resource type during deductions.
   - `Equipment`: Represents individual resource items that independantly have availability.
5. **`marsops.mission`**:
   - `Mission`: Abstract event with a lifecycle with `duration`, `elapsedTime`, requirements, and status.
   - Subtypes: `ExplorationMission`, `ResearchMission`, `RepairMission`, and `RescueMission`.
   - `MissionStatus`: Enum (`ASSIGNED`, `IN_PROGRESS`, `COMPLETED`, `FAILED`).
6. **`marsops.emergency`**:
   - `Emergency`: Abstract event with requirments.
   - Subtypes: `OxygenLeakEmergency`, `FireEmergency`, `MedicalEmergency`, and `RoverBreakdownEmergency`.
   - `EmergencyStatus`: Enum (`RESOLVED`, `UNRESOLVED`).

---

## 2. Design Decisions & Assumptions

1. Colony Singleton
2. Relocation & Origin Return + health check
3. Immediate Emergency Resolution
4. Equipment are not resources and are individual with availability
5. Agents wear down over time | Human fatigue increases per tick; robot battery drains per tick. Exhausted or depleted agents cannot start new missions.
...

<!-- TODO: complete list and format -->

## 3. Resource Allocation Rules


1. **Colony-Wide Aggregation**:
    <!-- TODO: word better -->
   - When verifying mission or emergency viability, available resources are summed across all currently available facilities.
   - resources are never partially deducted for a failed submission.
2. **Deduction Priority (`ResourceAllocationPolicy`)**:
    - **Oxygen**: `StorageFacility` $\rightarrow$ `Laboratory` $\rightarrow$ `PowerStation` $\rightarrow$ `Habitat` 
    - **Life Support (Water, Food)**: `StorageFacility` $\rightarrow$ `Habitat` $\rightarrow$ `Laboratory` $\rightarrow$ `PowerStation`.
    - **Medical Supplies**: `StorageFacility` $\rightarrow$ `Laboratory` $\rightarrow$ `Habitat` $\rightarrow$ `PowerStation`.
    - **Technical Supplies (Fuel, Battery, Spare Parts, Equipment)**: `StorageFacility` $\rightarrow$ `PowerStation` $\rightarrow$ `Laboratory` $\rightarrow$ `Habitat`.
3. **Allocation Priority**
    <!-- TODO: word better -->
    - facilities have maximum capacity (sum of equipment + resource count)
    - overwhelming resources than capacity are returned when adding and resources are added according to following priority
    <!-- TODO: add priorities in same way -->

## 4. Emergency Rules

- Successfully mitigated emergencies immediately transition to `EmergencyStatus.RESOLVED`.

- Insufficient responders or spare parts cause immediate rejection to `EmergencyStatus.UNRESOLVED` with a logged cause.

- All occurrences are archived in `pastEmergencies`.

- besides main requirments of the emergency extra required resources can be added per instance with addrequiredresource

<!-- TODO: List each emergency and its main requirments and event behaviour  -->


---

## 5. Environmental Effects

Colony conditions track three environmental states (`Environment` enum):

1. **`NORMAL`**: All surface operations, laboratory research, and communication channels proceed without restriction.
2. **`DUST_STORM`**:
   - Blocks submission of any surface operations (`ExplorationMission`).
   - If a dust storm begins during an ongoing surface mission, the active mission fails on the next simulation tick.
3. **`COMM_DISRUPTION`**:
   - Blocks submission and execution of missions requiring remote command communication (`isCommunicationDependent() == true`).

---

## 6. Mission States

Missions transition through states defined by `MissionStatus`:

```
   Created -> Checked 
        -> [FAILED] if invalid or enviroment hazard or missing agents/resources
        -> [ASSIGNED] if passed
            -> [FAILED] Environmental hazard / internal failure
            -> [IN_PROGRESS] advanceTime() under safe conditions
                -> [COMPLETED] elapsedTime >= duration
```

- **Unsubmitted / Created**: Initial state with zero elapsed time.
- **`ASSIGNED`**: Successfully passed pre-conditions. Resources deducted, agents locked and relocated.
- **`IN_PROGRESS`**: Active execution. Each `advanceTime(timeUnit)` updates agent fatigue/battery and increments elapsed time.
- **`COMPLETED`**: Reached target duration. Assigned agents released to origins, targets repaired/rescued, moved to `pastMissions`.
- **`FAILED`**: Terminal failure state with descriptive `failureReason`. Agents released immediately.

---

## 7. Open Specification Decisions

    <!-- TODO:  is this even neccesary isnt htis literally Design Decisions & Assumptions???-->

---


## 8. Compilation and Execution

### Prerequisites
- Java Development Kit (JDK) 21 or higher.

### Compile
Run the following command from the project root:
```powershell
cd src
javac marsops/Main.java
```

### Run Demonstration Scenarios
Execute the main test runner:
```powershell
java marsops.Main
```

---

## 9. Demonstration Scenarios (`Main.java`)

`marsops.Main` automates and validates the five required scenarios:

    <!-- TODO:  almost perfect but remove instance names like "shibuya" + add here that in code we use setup colony to not redeclare alot of stuff-->

1. **Scenario 1: Successful Exploration**
   - Configures a colony with a Scientist, Exploration Rover, Habitat, and Storage Facility.
   - Submits and executes a 2-hour exploration mission.
   - Demonstrates before and after states showing resource consumption, agent fatigue, battery drain, and successful completion.
2. **Scenario 2: Mission Rejection (Unsuitable Agent)**
   - Attempts to launch a mission requiring five Scientists and five Rovers when only one exists.
   - Verifies immediate rejection with reason: `"Required agents are unavailable or do not possess the required capability."`
3. **Scenario 3: Resource Shortage**
   - Submits a mission requesting 99,999 units of fuel.
   - Verifies rejection without partial consumption or negative balances.
4. **Scenario 4: Emergency Handling (Oxygen Leak)**
   - Simulates an oxygen leak in Habitat `"Shibuya"`.
   - Locates habitat, identifies Engineer and RepairRobot responders, consumes spare parts, and marks status `RESOLVED`.
5. **Scenario 5: Environmental Effect (Dust Storm)**
   - Sets environment to `DUST_STORM`.
   - Submits an `ExplorationMission`.
   - Verifies immediate rejection: `"Surface operations are not allowed during a dust storm."`
