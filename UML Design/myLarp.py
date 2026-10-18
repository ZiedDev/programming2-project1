from enum import Enum, auto
from types import Literal

################################ enums

type surfaceLocation = str
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

class EmergencyType(Enum):
    OXYGEN_LEAK = "Oxygen Leak"
    FIRE = "Fire"
    ROVER_BREAKDOWN = "Rover Breakdown"
    MEDICAL_EMERGENCY = "Medical Emergency"

class Equipment:
    def __init__(self):
        self.isDamaged:bool=True

################################ Colony singletpn

class Colony:
    def __init__(self,facilities:list[Facility],agents:list[Agent]) -> None:
        self.facilities:list[Facility]=facilities
        self.agents:list[Agent]=agents
        self.environment:Environment=Environment.NORMAL
        self.activeMission:Mission|None=None

        self.pastMissions:list[Mission]=[]
        self.pastEmergency:list[Emergency]=[]

    # Manage Agents
    # Manage Facilities

    # Print Total Resources
    # Print Reports
    # Print Environment
    # Print Mission and Emergency outcomes

    def _getResource(self, resourceType:ResourceType,amount:int):
        pass

    def advanceTime(self, time_units:int) -> None:
        pass

    def submitMission(self, mission:Mission)-> None:
        pass

    def handleEmergency(self, emergency:Emergency) -> None:
        pass


################################ agents

class Agent: # Human / Robot
    def __init__(self,originLocation:Facility):
        self.originLocation:Facility=originLocation
        self.currentLocation:Facility | Literal["Mars Surface"]=originLocation
        self.isAvailable:bool=True
        self.Health:int=100

    def canPerform(self, missionType:MissionType):
        pass

    def canHandle(self, emergencyType:EmergencyType):
        pass

class Human(Agent):
    def __init__(self, originLocation:Facility, name:str,ID:str):
        super().__init__(originLocation)

        self.name:str=name
        self.ID:str=ID
        self.fatigue:int=0

# Humans
class Scientist(Human): # Performs scientific research missions
    pass
class Engineer(Human): # Repairs damaged equipment
    pass
class MedicalOfficer(Human): # Responds to medical emergencies
    pass

class RobotUnit(Agent):
    def __init__(self, originLocation:Facility):
        super().__init__(originLocation)

        self.batteryLevel:int=100

class ExplorationRover(RobotUnit): # Travels across the Martian surface, carries equipment, and performs exploration.
    pass
class RepairRobot(RobotUnit): # Repairs damaged equipment and operates inside facilities.
    pass
class Drone (RobotUnit): # Performs aerial reconnaissance and operates in areas inaccessible to ground vehicles.
    pass

################################ facilites

class Facility:
    def __init__(self, 
                 location:surfaceLocation,
                 capacity:int,
                 resources:dict[ResourceType,int],
                 equipment:list[Equipment]):
        self.location:surfaceLocation=location
        self.capacity:int=capacity
        self.isAvailable:bool=True
        self.resources:dict[ResourceType,int]=resources
        self.equipment:list[Equipment]=equipment

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

################################ Structs
class Mission:
    def __init__(self, duration:int) -> None:
        self.duration:int=duration
        self.isCommunicationDependant:bool=False
        
        self.requiredAgents:dict[type[Agent],int]
        self.requiredResources:dict[ResourceType,int]
        self.requiredEquipment:int

        self.status:MissionStatus
        self.failureReason:str=""

        self.agents:list[Agent]

class ExplorationMission(Mission):
    def __init__(self, 
                 duration:int,
                 region:str,
                 requiredRoverCount:int,
                 requiredScientistCount:int,
                 requiredBatteryCount:int,
                 requiredEquipmentCount:int):
        super().__init__(duration)
        self.region:str=region

        self.requiredAgents[ExplorationRover]=requiredRoverCount
        self.requiredAgents[Scientist]=requiredScientistCount
        self.requiredEquipment[ResourceType.BATTERY]=requiredBatteryCount
        self.requiredEquipment=requiredEquipmentCount
class RepairMission(Mission):
    def __init__(self,
                 duration: int,
                 damagedObject:Facility|Equipment,
                 requiredEngineerCount:int,
                 requiredRepairBotCount:int,
                 requiredSparePartsCount:int):
        super().__init__(duration)
        self.damagedObject=damagedObject

        self.requiredAgents[Engineer]=requiredEngineerCount
        self.requiredAgents[RepairRobot]=requiredRepairBotCount
        self.requiredEquipment[ResourceType.SPARE_PARTS]=requiredSparePartsCount
        self.requiredEquipment=0
class ResearchMission(Mission):
    def __init__(self, 
                 duration:int,
                 researchedLab:Laboratory,
                 requiredScientistCount:int,
                 requiredEquipmentCount:int,
                 requiredBatteryCount:int,
                 requiredSparePartsCount:int):
        super().__init__(duration)
        self.researchedLab=researchedLab

        self.requiredAgents[Scientist]=requiredScientistCount
        self.requiredResources[ResourceType.BATTERY]=requiredBatteryCount
        self.requiredResources[ResourceType.SPARE_PARTS]=requiredSparePartsCount
        self.requiredEquipment=requiredEquipmentCount
class RescueMission(Mission):
    def __init__(self, 
                 duration:int,
                 requiredRoverCount:int,
                 requiredMedicalOfficerCount:int,
                 requiredMedicalSuppliesCount:int,
                 requiredFuelCount:int):
        super().__init__(duration)

        self.requiredAgents[ExplorationRover]=requiredRoverCount
        self.requiredAgents[MedicalOfficer]=requiredMedicalOfficerCount
        self.requiredResources[ResourceType.MEDICAL]=requiredMedicalSuppliesCount
        self.requiredResources[ResourceType.FUEL]=requiredFuelCount
        self.requiredEquipment=0

        self.isCommunicationDependant=True

class Emergency:
    def __init__(self, emergencyType:EmergencyType, location:Facility | Literal["Mars Surface"]) -> None:
        self.emergencyType:EmergencyType = emergencyType
        self.location:Facility | Literal["Mars Surface"] = location
        self.status:EmergencyStatus = EmergencyStatus.UNRESOLVED
        self.failureReason:str=""

        self.requiredAgents:dict[type[Agent],int]
        self.requiredResources:dict[ResourceType,int]
        self.assignedResponders:list[Agent]

class OxygenLeakEmergency(Emergency):
    def __init__(self, 
                 affectedHabitat:Habitat, 
                 oxygenLoss:int,
                 requiredEngineerCount:int=1,
                 requiredRepairBotCount:int=1,
                 requiredSparePartsCount:int=2):
        super().__init__(EmergencyType.OXYGEN_LEAK, affectedHabitat)
        self.affectedHabitat:Habitat = affectedHabitat
        self.oxygenLoss:int = oxygenLoss

        self.requiredAgents[Engineer] = requiredEngineerCount
        self.requiredAgents[RepairRobot] = requiredRepairBotCount
        self.requiredResources[ResourceType.SPARE_PARTS] = requiredSparePartsCount

class FireEmergency(Emergency):
    def __init__(self, 
                 affectedFacility:Facility,
                 requiredEngineerCount:int=1,
                 requiredMedicalOfficerCount:int=1,
                 requiredWaterCount:int=50,
                 requiredMedicalSuppliesCount:int=10):
        super().__init__(EmergencyType.FIRE, affectedFacility)
        self.affectedFacility:Facility = affectedFacility

        self.requiredAgents[Engineer] = requiredEngineerCount
        self.requiredAgents[MedicalOfficer] = requiredMedicalOfficerCount
        self.requiredResources[ResourceType.WATER] = requiredWaterCount
        self.requiredResources[ResourceType.MEDICAL] = requiredMedicalSuppliesCount

class RoverBreakdownEmergency(Emergency):
    def __init__(self, 
                 damagedRover:ExplorationRover,
                 requiredEngineerCount:int=1,
                 requiredRepairBotCount:int=1,
                 requiredSparePartsCount:int=3):
        super().__init__(EmergencyType.ROVER_BREAKDOWN, damagedRover.currentLocation)
        self.damagedRover:ExplorationRover = damagedRover

        self.requiredAgents[Engineer] = requiredEngineerCount
        self.requiredAgents[RepairRobot] = requiredRepairBotCount
        self.requiredResources[ResourceType.SPARE_PARTS] = requiredSparePartsCount

class MedicalEmergency(Emergency):
    def __init__(self, 
                 patient:Human,
                 treatmentFacility:Facility,
                 requiredMedicalOfficerCount:int=1,
                 requiredMedicalSuppliesCount:int=15):
        super().__init__(EmergencyType.MEDICAL_EMERGENCY, treatmentFacility)
        self.patient:Human = patient
        self.treatmentFacility:Facility = treatmentFacility

        self.requiredAgents[MedicalOfficer] = requiredMedicalOfficerCount
        self.requiredResources[ResourceType.MEDICAL] = requiredMedicalSuppliesCount