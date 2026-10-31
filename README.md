# Mars Colony Operations Management System

## Table of Contents
1. [System Description and Main Components](#1-system-description-and-main-components)
2. [Design Decisions and Assumptions](#2-design-decisions--assumptions)
3. [Resource Allocation Rules](#3-resource-allocation-rules)
4. [Emergency Rules](#4-emergency-rules)
5. [Environmental Effects](#5-environmental-effects)
6. [Mission States](#6-mission-states)
7. [Open Specification Decisions](#7-open-specification-decisions)
8. [Compilation and Execution](#8-compilation-and-execution)
9. [Demonstration Scenarios](#9-demonstration-scenarios-mainjava)

---

## 1. System description and main components

This project aims to model a colony on mars and the components that make it up, including: human crew members, robotic units, different types of facilities aswell as missions and emergencies that interact with them.

### Main Packages
1. **`marsops.colony`**:
   - `Colony`: Singleton class that allows for mission submission, emergency handling, and simulation time advancement.
   - `AgentManager`: Manages human crew and robotic units, and tracks their availability, capabilities, and assignment to missions.
   - `FacilityManager`: Manages operational facilities, aggregates colony-wide resource and equipment totals, and handles deductions.
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
   - `Equipment`: Represents individual resource items that independently have availability.
5. **`marsops.mission`**:
   - `Mission`: Abstract event with a lifecycle with `duration`, `elapsedTime`, requirements, and status.
   - Subtypes: `ExplorationMission`, `ResearchMission`, `RepairMission`, and `RescueMission`.
   - `MissionStatus`: Enum (`ASSIGNED`, `IN_PROGRESS`, `COMPLETED`, `FAILED`).
6. **`marsops.emergency`**:
   - `Emergency`: Abstract crisis event with requirements and validation.
   - Subtypes: `OxygenLeakEmergency`, `FireEmergency`, `MedicalEmergency`, and `RoverBreakdownEmergency`.
   - `EmergencyStatus`: Enum (`RESOLVED`, `UNRESOLVED`).

---

## 2. Design Decisions and Assumptions

1. **Colony Singleton**: Kept as a single instance so there is one central access point for managers and colony state (`activeMission`, `environment`). We also have `resetInstance()` to easily clear state and reset IDs between test runs.
2. **Single Active Mission**: Only one mission can be active at a time. Trying to submit another while one is running gets rejected right away.
3. **All-or-Nothing Checks**: Before any mission or emergency is accepted, the system checks that agents, resources, and environmental conditions are valid. If anything fails, nothing is deducted and no agents are locked.
4. **Relocation and Origin Return**: When agents go on a mission, their location updates to the target location. Once it finishes (or fails), agents that are still alive (`health > 0`) get returned back to their `originLocation` and become available again.
5. **Immediate Emergency Handling**: Emergencies are treated as immediate crisis events. `submitEmergency()` validates responders and resources, deducts what is needed, and resolves it on the spot.
6. **Equipment as Distinct Objects**: Equipment items are treated as separate objects with their own availability and health, not simple numeric resources. They still take up facility capacity.
7. **Agent Fatigue and Battery**: Agents wear down over time. Human fatigue goes up per tick, and robot battery drains per tick. If a human is exhausted (fatigue $\ge 100$) or a robot is out of battery ($\le 0$), they cannot be assigned to new tasks.
8. **Manager Separation**: Delegated specific jobs to dedicated managers (`AgentManager` handles crew/robots, `FacilityManager` handles facilities/resources, and `PrintManager` handles all console output).

---

## 3. Resource Allocation Rules

1. **Colony-Wide Aggregation**:
   - When verifying mission or emergency viability, available resources and equipment are summed across all currently operational facilities (`isAvailable() == true`).
   - If any required resource or equipment count falls short, the operation is rejected and resources are never partially deducted for a failed submission.
2. **Deduction Priority (`ResourceAllocationPolicy`)**:
   When resources are consumed, operational facilities are depleted in a specific order based on resource criticality:
   - **Oxygen**: `StorageFacility` $\rightarrow$ `Laboratory` $\rightarrow$ `PowerStation` $\rightarrow$ `Habitat`.
   - **Life Support (Water, Food)**: `StorageFacility` $\rightarrow$ `Habitat` $\rightarrow$ `Laboratory` $\rightarrow$ `PowerStation`.
   - **Medical Supplies**: `StorageFacility` $\rightarrow$ `Laboratory` $\rightarrow$ `Habitat` $\rightarrow$ `PowerStation`.
   - **Technical Supplies (Fuel, Battery, Spare Parts, Equipment)**: `StorageFacility` $\rightarrow$ `PowerStation` $\rightarrow$ `Laboratory` $\rightarrow$ `Habitat`.
3. **Allocation Priority (Facility Storage)**:
   - Each facility has a strict maximum capacity: $\text{Total Taken} = \sum \text{Resources} + \text{Equipment Count} \le \text{Capacity}$.
   - When resources are added via `addResources()`, excess amounts beyond capacity are unallocated and returned.
   - Facilities fill available space following their internal allocation priority:
     - **`Habitat`**: `OXYGEN` $\rightarrow$ `WATER` $\rightarrow$ `FOOD` $\rightarrow$ `MEDICAL` $\rightarrow$ `BATTERY` $\rightarrow$ `FUEL` $\rightarrow$ `SPARE_PARTS`
     - **`Laboratory`**: `BATTERY` $\rightarrow$ `FUEL` $\rightarrow$ `SPARE_PARTS` $\rightarrow$ `OXYGEN` $\rightarrow$ `MEDICAL` $\rightarrow$ `FOOD` $\rightarrow$ `WATER`
     - **`PowerStation`**: `FUEL` $\rightarrow$ `BATTERY` $\rightarrow$ `SPARE_PARTS` $\rightarrow$ `OXYGEN` $\rightarrow$ `FOOD` $\rightarrow$ `WATER` $\rightarrow$ `MEDICAL`
     - **`StorageFacility`**: `SPARE_PARTS` $\rightarrow$ `BATTERY` $\rightarrow$ `FUEL` $\rightarrow$ `FOOD` $\rightarrow$ `WATER` $\rightarrow$ `OXYGEN` $\rightarrow$ `MEDICAL`

---

## 4. Emergency Rules

- Successfully mitigated emergencies immediately transition to `EmergencyStatus.RESOLVED`.
- Insufficient responders or spare parts cause immediate rejection to `EmergencyStatus.UNRESOLVED` with a logged cause.
- All occurrences are archived in `pastEmergencies`.
- Besides main requirements of the emergency, extra required resources can be added per instance with `addRequiredResource()`.

### Emergency Types and Requirements
1. **`OxygenLeakEmergency`**:
   - **Target**: An operational `Habitat`.
   - **Immediate Impact**: Immediately deducts `oxygenLoss` from the affected habitat upon creation (clamped at 0).
   - **Requirements**: Requires at least 1 `Engineer`, 1 `RepairRobot`, and specified `SPARE_PARTS`.
   - **Failure Effect**: If unresolved, secondary oxygen loss occurs and habitat status remains compromised (`isAvailable = false`).
2. **`FireEmergency`**:
   - **Target**: An operational `Laboratory`.
   - **Immediate Impact**: All available equipment in the affected laboratory are damaged upon creation.
   - **Requirements**: Requires 1 `Engineer`, specified `WATER`, and `MEDICAL` supplies.
   - **Failure Effect**: If unresolved, the laboratory is shut down (`isAvailable = false`).
3. **`MedicalEmergency`**:
   - **Target**: Injured `Human` patient at an operational treatment `Facility`.
   - **Requirements**: Requires 1 `MedicalOfficer` and specified `MEDICAL` supplies.
   - **Resolution Effect**: Patient health is restored to 100 and fatigue reset to 0 upon resolution.
4. **`RoverBreakdownEmergency`**:
   - **Target**: A damaged `ExplorationRover`.
   - **Immediate Impact**: Rover health set to 0 and marked unavailable.
   - **Requirements**: Requires 1 `Engineer`, 1 `RepairRobot`, and specified `SPARE_PARTS`.
   - **Resolution Effect**: Rover health restored to 100, battery restored to 100, and marked available again.

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
        -> [FAILED] if invalid or environment hazard or missing agents/resources
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

The project spec left a few areas open to interpretation, so here is how we chose to handle them:

1. **Colony Facade vs Direct Access**:
   - `Colony` acts as the main facade delegating agent and facility work. But for printing, we expose `colony.getPrintManager()` directly since print methods do not change state and are cleaner to call this way.
2. **Facility Storage vs Deduction Priorities**:
   - We used separate priority orders for adding resources (`prioToAllocate` per facility) vs deducting resources (`ResourceAllocationPolicy`). This protects critical life support in habitats during missions, while letting storage facilities handle the bulk of operational deductions.
3. **Emergency Handling Model**:
   - Emergencies are resolved right on submission. In a real base crisis you want immediate response, so `submitEmergency()` checks responders, deducts parts, and resolves the issue immediately instead of waiting across multiple time ticks.
4. **Agent Return After Missions**:
   - Every agent keeps track of an `originLocation` (like their home habitat). Once a mission ends or fails, agents return to their base rather than being left stranded at the mission site.

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

`marsops.Main` automates and validates the five required scenarios. In code, a shared helper method `setupColony()` initializes common baseline facilities (a Habitat and Storage Facility), resources, crew members, and robots to keep test setups concise and avoid boilerplate:

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
   - Simulates an oxygen leak in a Habitat.
   - Locates habitat, identifies Engineer and RepairRobot responders, consumes spare parts, and marks status `RESOLVED`.
5. **Scenario 5: Environmental Effect (Dust Storm)**
   - Sets environment to `DUST_STORM`.
   - Submits an `ExplorationMission`.
   - Verifies immediate rejection: `"Surface operations are not allowed during a dust storm."`
