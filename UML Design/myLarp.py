from enum import Enum, auto

################################

class Environment(Enum):
    NORMAL = "Normal"
    DUST_STORM = "Dust Storm"
    COMM_DISRUPTION = "Communication Disruption"

class MissionStatus(Enum):
    ASSIGNED = "ASSIGNED"
    IN_PROGRESS = "IN_PROGRESS"
    COMPLETED = "COMPLETED"
    FAILED = "FAILED"

class EmergencyStatus(Enum):
    RESOLVED = "RESOLVED"
    UNRESOLVED = "UNRESOLVED"

class ResourceType(Enum):
    OXYGEN = "Oxygen"
    WATER = "Water"
    FOOD = "Food"
    FUEL = "Fuel"
    BATTERY = "Battery Power"
    MEDICAL = "Medical Supplies"
    SPARE_PARTS = "Spare Parts"

class MissionType(Enum):
    EXPLORATION = "Exploration" # Suitable vehicle, required scientific skill, sufficient battery, and required equipment.
    REPAIR = "Repair" # Engineer, repair robot, and spare parts.
    RESEARCH = "Research" # Scientists with a particular skill,laboratory, scientific equipment, and sufficient resources.
    RESCUE = "Rescue" # Appropriate vehicle, available personnel, and communication capability.

class EmergencyType(Enum):
    OXYGEN_LEAK = "Oxygen Leak"
    FIRE = "Fire"
    ROVER_BREAKDOWN = "Rover Breakdown"
    MEDICAL_EMERGENCY = "Medical Emergency"

################################

class Colony:
    def __init__(self) -> None:
        self.facilities:list[Facility]
        self.agents:list[Agent]
        self.enviroment:Environment
        self.activeMission:Mission

        self.pastMissions:list[Mission]
        self.pastEmergency:list[Emergency]
        pass

    # Manage Agents
    # Manage Facilities
    # Monitor Total Resources
    # Print Reports
    # Print Enviroment
    
    # submit Mission
    # print Mission and Emergency outcomes

    def advanceTime(self, time_units:int) -> None:
        pass

    def handleEmergency(self, emergencyType:EmergencyType) -> Emergency:
        pass


################################

class Agent: # Human / Robot
    # • Current location
    # • Availability
    # • Health status
    pass

    def canPerform(self, missionType:MissionType):
        pass

    def canHandle(self, emergencyType:EmergencyType):
        pass

class Human(Agent):
    # • Name
    # • Identification number
    # • Skills
    # • Fatigue level
    pass

# Humans
class Scientist(Human): # Performs scientific research missions
    pass
class Engineer(Human): # Repairs damaged equipment
    pass
class MedicalOfficer(Human): # Responds to medical emergencies
    pass

class RobotUnit(Agent):
    # • Battery level
    pass

class ExplorationRover(RobotUnit): # Travels across the Martian surface, carries equipment, and performs exploration.
    pass
class RepairRobot(RobotUnit): # Repairs damaged equipment and operates inside facilities.
    pass
class Drone (RobotUnit): # Performs aerial reconnaissance and operates in areas inaccessible to ground vehicles.
    pass

################################

class Facility:
    # • A location
    # • Capacity
    # • Operational status
    # • Resources
    # • Equipment
    pass

class Habitat(Facility):
    pass
class Laboratory(Facility):
    pass
class StorageFacility(Facility):
    pass
class PowerStation(Facility):
    pass
class CommunicationStation(Facility):
    pass

################################

class Mission:
    # - status: ASSIGNED, IN_PROGRESS, COMPLETED, FAILED
    # - duration
    # - failure reason
    # - Communication-dependent
    # - type
    pass

class Emergency:
    # - type
    # - status: UNRESOLVED
    # - reason
    pass