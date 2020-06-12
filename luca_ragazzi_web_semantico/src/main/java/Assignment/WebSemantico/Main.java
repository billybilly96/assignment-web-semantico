package Assignment.WebSemantico;

import java.util.ArrayList;
import java.util.List;

import Assignment.WebSemantico.cardata.SpecialVehicleDataUtils;
import Assignment.WebSemantico.cardata.SpeedProfileDataUtils;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import Assignment.WebSemantico.cardata.MyCarData;
import Assignment.WebSemantico.cardata.MyCarDataUtils;
import Assignment.WebSemantico.intersection.uncontrolled.rules.DrivingDirection;
import Assignment.WebSemantico.intersection.uncontrolled.rules.SWRLGenericRules;
import Assignment.WebSemantico.intersection.uncontrolled.rules.SWRLItalianRules;
import Assignment.WebSemantico.mapdata.MyMapData;
import Assignment.WebSemantico.mapdata.MyMapDataUtils;
import Assignment.WebSemantico.pathdata.MyPathDataUtils;
import Assignment.WebSemantico.reasoner.ReasonerUtils;
import Assignment.WebSemantico.utils.logger.Logger;
import Assignment.WebSemantico.utils.logger.MessageType;
import Assignment.WebSemantico.utils.ontology.LoadMainOntology;
import Assignment.WebSemantico.utils.ontology.OWLOntologyWithTools;
import Assignment.WebSemantico.utils.ontology.OntologyLoadUtils;
import Assignment.WebSemantico.utils.ontology.exception.LoadedOntoNotSelected;
import Assignment.WebSemantico.utils.ontology.exception.OntologyCreationOrLoadingFailed;

public class Main {
	private static final String className = "Main";
	
	// My ontology
	public final static String myOntologyFileName = "MyOntologyPath.owl";
	public final static String myOntologyIRI = "http://assignment-ws/my-ontology";
	
	// Car ontology
	private final static String carOntoVersionIri = "https://www.toyota-ti.ac.jp/Lab/Denshi/COIN/Ontology/TTICore-0.1/TTICarOnto.owl";
	// Control ontology
	private final static String controlOntoVersionIri = "https://www.toyota-ti.ac.jp/Lab/Denshi/COIN/Ontology/TTICore-0.1/TTIControlOnto.owl";
	// Map ontology
	private final static String mapOntoVersionIri= "https://www.toyota-ti.ac.jp/Lab/Denshi/COIN/Ontology/TTICore-0.1/TTIMapOnto.owl";
	
	private final static IRI swrlVarXIRI = IRI.create(myOntologyIRI + "#X");
	private final static IRI swrlVarYIRI = IRI.create(myOntologyIRI + "#Y");
	private final static IRI swrlVarIntIRI = IRI.create(myOntologyIRI + "#INT");
	private final static IRI swrlVarLaneIRI = IRI.create(myOntologyIRI + "#LANE");
	private final static IRI swrlVarLane2IRI = IRI.create(myOntologyIRI + "#LANE2");
	private final static IRI swrlVarRoadSegIRI = IRI.create(myOntologyIRI + "#RS");
	private final static IRI swrlVarRoadSeg2IRI = IRI.create(myOntologyIRI + "#RS2");
	private final static IRI swrlVarNextLaneIRI = IRI.create(myOntologyIRI + "#NEXTLANE");
		
	public static OWLOntologyWithTools createMyPathDataOntology() throws OntologyCreationOrLoadingFailed, LoadedOntoNotSelected {
		// Create empty ontology.
		OWLOntologyWithTools ontology = OntologyLoadUtils.createEmptyOntology(IRI.create(myOntologyIRI))
				                                         .orElseThrow(OntologyCreationOrLoadingFailed::new);
		// Set the current loaded ontology.
		LoadMainOntology.selectOntology(ontology);
		
		Logger.log(MessageType.INFO, className, "Empty ontology created!");
				
		// Import ontologies
		ontology.importAnOntology(IRI.create(mapOntoVersionIri));
		ontology.importAnOntology(IRI.create(controlOntoVersionIri));
		ontology.importAnOntology(IRI.create(carOntoVersionIri));
		Logger.log(MessageType.INFO, className, "Number of imported ontologies: " + ontology.getNumberOfOntologiesImported());
		
		// Add the intersection to the ontology
		MyMapDataUtils.addIntersection(ontology);
		// Add the four road segments to the ontology
		MyMapDataUtils.addRoadSegments(ontology);
		// Connects the lanes to the roadsegments
		MyMapDataUtils.addHasLaneObjectProperties(ontology);
		// Add axioms for object property turnLeftTo, turnRightTo and goStraightTo 
		MyMapDataUtils.addLanesConnections(ontology);
		// Connects the road segments to the intersection
		MyMapDataUtils.addRoadSegmentAndIntersectionConnection(ontology);
		// Add the class and the object property for the swrl rules
		MyPathDataUtils.addClassAndObjectPropertyForSwrlRules(ontology);
		// Add the ego vehicle
		MyCarDataUtils.addEgoVehicle();
		MyCarDataUtils.addDataPropertyCarIdToEgoVehicle(ontology);
		SpeedProfileDataUtils.addSpeedProfileClasses();
		// Add the tram vehicle (special vehicle)
		SpecialVehicleDataUtils.addTramVehicle();
		SpecialVehicleDataUtils.addDataPropertyCarIdToTramVehicle(ontology);
		SpecialVehicleDataUtils.addTramVehiclePosition();
		// Add the road's speed limit
		MyMapDataUtils.addRoadSpeedLimit();
		MyMapDataUtils.addDataPropertySpeedMaxToSpeedLimit(ontology);
		return ontology;
	}
	
	public static void setEgoVehiclePathFromSouthToNorth(OWLOntologyWithTools ontology) throws LoadedOntoNotSelected {
		// Add the paths segments sequence to reach the endNode.
		List<OWLNamedIndividual> pathSequence = new ArrayList<>();
		pathSequence.add(MyMapData.getInstance().getSouthRoadSegment().getLane1()); // StartNode
		pathSequence.add(MyMapData.getInstance().getIntersection());
		pathSequence.add(MyMapData.getInstance().getNorthRoadSegment().getLane2()); // EndNode
    	MyPathDataUtils.addSequenceOfPathSegments(ontology, pathSequence);
	}

	public static void setTramVehiclePathFromWestToEast(OWLOntologyWithTools ontology) throws LoadedOntoNotSelected {
		// Add the paths segments sequence to reach the endNode.
		List<OWLNamedIndividual> pathSequence = new ArrayList<>();
		pathSequence.add(MyMapData.getInstance().getWestRoadSegment().getLane1()); // StartNode
		pathSequence.add(MyMapData.getInstance().getIntersection());
		pathSequence.add(MyMapData.getInstance().getEastRoadSegment().getLane2()); // EndNode
		MyPathDataUtils.addSequenceOfPathSegmentsForTramVehicle(ontology, pathSequence);
	}
	
	public static void setEgoVehiclePathFromSouthToWest(OWLOntologyWithTools ontology) throws LoadedOntoNotSelected {
		// Add the paths segments sequence to reach the endNode.
		List<OWLNamedIndividual> pathSequence = new ArrayList<>();
		pathSequence.add(MyMapData.getInstance().getSouthRoadSegment().getLane1()); // StartNode
		pathSequence.add(MyMapData.getInstance().getIntersection());
		pathSequence.add(MyMapData.getInstance().getWestRoadSegment().getLane2()); // EndNode
    	MyPathDataUtils.addSequenceOfPathSegments(ontology, pathSequence);
	}
	
	public static void addGeneralSwrlRulesToOntology(OWLOntologyWithTools ontology) throws LoadedOntoNotSelected {
		SWRLGenericRules.collisionWarningRule(ontology, swrlVarXIRI, swrlVarYIRI);
		SWRLGenericRules.drivingDirectionRule(ontology, swrlVarIntIRI, swrlVarXIRI, swrlVarLaneIRI, swrlVarNextLaneIRI, DrivingDirection.RIGHT);
		SWRLGenericRules.drivingDirectionRule(ontology, swrlVarIntIRI, swrlVarXIRI, swrlVarLaneIRI, swrlVarNextLaneIRI, DrivingDirection.LEFT);
		SWRLGenericRules.drivingDirectionRule(ontology, swrlVarIntIRI, swrlVarXIRI, swrlVarLaneIRI, swrlVarNextLaneIRI, DrivingDirection.STRAIGHT);
		SWRLGenericRules.respectRoadSpeedLimitRule(ontology, swrlVarIntIRI, swrlVarXIRI, swrlVarLaneIRI, false);
	}
	
	public static void main(String[] args) throws OntologyCreationOrLoadingFailed {
		try {
			// Create the ontology
			OWLOntologyWithTools ontology = createMyPathDataOntology();
			// Set the ego vehicle path
			setEgoVehiclePathFromSouthToNorth(ontology);
			// Set the tram vehicle path
			setTramVehiclePathFromWestToEast(ontology);
			// Add the swrl rules
			addGeneralSwrlRulesToOntology(ontology);
			SWRLItalianRules.giveWayToVehicleThatIsRunningOnIntersection(ontology, swrlVarXIRI, swrlVarYIRI, swrlVarIntIRI, swrlVarLaneIRI);
			SWRLItalianRules.rightOfWayRule(ontology, swrlVarXIRI, swrlVarYIRI, swrlVarLaneIRI, swrlVarLane2IRI, swrlVarRoadSegIRI, swrlVarRoadSeg2IRI);
			SWRLItalianRules.giveWaySpecialVehicles(ontology, swrlVarXIRI, swrlVarYIRI, swrlVarLaneIRI, swrlVarLane2IRI, swrlVarRoadSegIRI, swrlVarRoadSeg2IRI);
			// Save the ontology
			OntologyLoadUtils.saveOntologyIntoFileUsingTurtleSyntax(myOntologyFileName, ontology);
			// Create the reasoner
	        OWLReasoner reasoner = ReasonerUtils.createReasoner(ontology.getOntology());
	        Logger.log(MessageType.INFO, className, "Ontology is consistent? " + reasoner.isConsistent());
	        Logger.log(MessageType.INFO, className, "Start node:");
	        Logger.log(MessageType.INFO, className, " - " + ReasonerUtils.getStartNode(reasoner).toString());
	        Logger.log(MessageType.INFO, className, "End node:");
	        Logger.log(MessageType.INFO, className, " - " + ReasonerUtils.getEndNode(reasoner).toString());
	        Logger.log(MessageType.INFO, className, "Current ego vehicle position:");
	        Logger.log(MessageType.INFO, className, " - " + ReasonerUtils.getEgoVehiclePosition(reasoner).toString());
			Logger.log(MessageType.INFO, className, "Current tram vehicle position:");
			Logger.log(MessageType.INFO, className, " - " + ReasonerUtils.getTramVehiclePosition(reasoner).toString());
	        
	        Logger.log(MessageType.INFO, className, "Test swrl rule inference:");
	        Logger.log(MessageType.INFO, className, " - " + ReasonerUtils.getIndividualsByClass(reasoner, MyCarData.getInstance().getClassMyCar()).toString());
	        
	        reasoner.dispose();
		} catch (LoadedOntoNotSelected e) {
			Logger.log(MessageType.ERROR, className, "Set the loaded ontology into the SWRLUtils!");
			if (!Logger.disableStackTrace) e.printStackTrace();
		} catch (OntologyCreationOrLoadingFailed e) {
			Logger.log(MessageType.ERROR, className, "Error during ontology creation!");
			if (!Logger.disableStackTrace) e.printStackTrace();
		}
   }
}
