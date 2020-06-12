package Assignment.WebSemantico.mapdata;

import org.semanticweb.owlapi.model.*;

import Assignment.WebSemantico.Main;
import Assignment.WebSemantico.utils.ontology.OWLOntologyWithTools;
import Assignment.WebSemantico.utils.ontology.OntologyUtils;
import Assignment.WebSemantico.utils.ontology.exception.LoadedOntoNotSelected;

public class MyMapDataUtils {	
	public final static IRI indivIntersection = IRI.create(Main.myOntologyIRI + "#indivIntersection");
	
	private final static IRI indivRoadSegmentNorth = IRI.create(Main.myOntologyIRI + "#indivRoadSegmentNorth");
	private final static IRI indivRoadSegmentNorth_lane1 = IRI.create(Main.myOntologyIRI + "#indivRoadSegmentNorth_lane1");
	private final static IRI indivRoadSegmentNorth_lane2 = IRI.create(Main.myOntologyIRI + "#indivRoadSegmentNorth_lane2");
	
	private final static IRI indivRoadSegmentEast = IRI.create(Main.myOntologyIRI + "#indivRoadSegmentEast");
	private final static IRI indivRoadSegmentEast_lane1 = IRI.create(Main.myOntologyIRI + "#indivRoadSegmentEast_lane1");
	private final static IRI indivRoadSegmentEast_lane2 = IRI.create(Main.myOntologyIRI + "#indivRoadSegmentEast_lane2");
	
	private final static IRI indivRoadSegmentSouth = IRI.create(Main.myOntologyIRI + "#indivRoadSegmentSouth");
	private final static IRI indivRoadSegmentSouth_lane1 = IRI.create(Main.myOntologyIRI + "#indivRoadSegmentSouth_lane1");
	private final static IRI indivRoadSegmentSouth_lane2 = IRI.create(Main.myOntologyIRI + "#indivRoadSegmentSouth_lane2");
	
	private final static IRI indivRoadSegmentWest = IRI.create(Main.myOntologyIRI + "#indivRoadSegmentWest");
	private final static IRI indivRoadSegmentWest_lane1 = IRI.create(Main.myOntologyIRI + "#indivRoadSegmentWest_lane1");
	private final static IRI indivRoadSegmentWest_lane2 = IRI.create(Main.myOntologyIRI + "#indivRoadSegmentWest_lane2");

	private final static IRI indivSpeedLimit = IRI.create(Main.myOntologyIRI + "#speedLimit");

	private final static String mapOntoIri= "http://www.toyota-ti.ac.jp/Lab/Denshi/COIN/Map";
	// Classes
	private final static IRI roadSegmentClassIRI = IRI.create(mapOntoIri + "#RoadSegment");
	private final static IRI oneWayLaneClassIRI = IRI.create(mapOntoIri + "#OneWayLane");
	private final static IRI intersectionClassIRI = IRI.create(mapOntoIri + "#Intersection");
	private final static IRI speedLimitClassIRI = IRI.create(mapOntoIri + "#SpeedLimit");
	// Object property: 
	// - RoadSegment
	private final static IRI hasLaneIRI = IRI.create(mapOntoIri + "#hasLane");
	// - Lane
	private final static IRI isLaneOfIRI = IRI.create(mapOntoIri + "#isLaneOf");
	private final static IRI goStraightToIRI = IRI.create(mapOntoIri + "#goStraightTo");
	private final static IRI turnLeftToIRI = IRI.create(mapOntoIri + "#turnLeftTo");
	private final static IRI turnRightToIRI = IRI.create(mapOntoIri + "#turnRightTo");
	// - Intersection and road segment
	private final static IRI isConnectedToIRI = IRI.create(mapOntoIri + "#isConnectedTo");

	// Data property:
	private final static IRI dataPropertySpeedMaxIRI = IRI.create(mapOntoIri + "#speedMax");

	private final static String SPEED_LIMIT = "50";
	
	private static final MyMapData mapData = MyMapData.getInstance();
	
	/**
	 * Creates the intersection and assign its type.
	 * @param ontology OWLOntologyWithTools
	 * @throws LoadedOntoNotSelected 
	 */
	public static void addIntersection(OWLOntologyWithTools ontology) throws LoadedOntoNotSelected {
		// Create the intersection and set its type
		OWLClass classIntersection = OntologyUtils.createClass(intersectionClassIRI);
		mapData.setClassIntersection(classIntersection);
		mapData.setIntersection(OntologyUtils.createIndividualAndSetHisType(indivIntersection, classIntersection));
	}

	public static void addRoadSpeedLimit() throws LoadedOntoNotSelected {
		// Create the speed limit
		OWLClass classSpeedLimit = OntologyUtils.createClass(speedLimitClassIRI);
		mapData.setClassSpeedLimit(classSpeedLimit);
		mapData.setSpeedLimit(OntologyUtils.createIndividualAndSetHisType(indivSpeedLimit, classSpeedLimit));
	}

	public static void addDataPropertySpeedMaxToSpeedLimit(OWLOntologyWithTools ontology) throws LoadedOntoNotSelected {
		OWLDataProperty dpSpeedMax = OntologyUtils.createDataProperty(dataPropertySpeedMaxIRI);
		mapData.setSpeedMax(dpSpeedMax);
		ontology.addAxiom(OntologyUtils.createDataPropertyAxiom(dpSpeedMax, mapData.getSpeedLimit(), SPEED_LIMIT));
	}
	
	/**
	 * Creates the four road segments.
	 * @param ontology OWLOntologyWithTools
	 * @throws LoadedOntoNotSelected 
	 */
	public static void addRoadSegments(OWLOntologyWithTools ontology) throws LoadedOntoNotSelected {
		OWLClass classRoadSegment = OntologyUtils.createClass(roadSegmentClassIRI);
		OWLClass classOneWayLane = OntologyUtils.createClass(oneWayLaneClassIRI);
		mapData.setClassRoadSegment(classRoadSegment);
		mapData.setClassOneWayLane(classOneWayLane);
		// North
		RoadSegmentWithTwoLane northRoadSegment = new RoadSegmentWithTwoLane(OntologyUtils.createIndividualAndSetHisType(indivRoadSegmentNorth, classRoadSegment),
																			 OntologyUtils.createIndividualAndSetHisType(indivRoadSegmentNorth_lane1, classOneWayLane),
																			 OntologyUtils.createIndividualAndSetHisType(indivRoadSegmentNorth_lane2, classOneWayLane));		
		// East
		RoadSegmentWithTwoLane eastRoadSegment = new RoadSegmentWithTwoLane(OntologyUtils.createIndividualAndSetHisType(indivRoadSegmentEast, classRoadSegment),
																		    OntologyUtils.createIndividualAndSetHisType(indivRoadSegmentEast_lane1, classOneWayLane),
																		    OntologyUtils.createIndividualAndSetHisType(indivRoadSegmentEast_lane2, classOneWayLane));
		// South
		RoadSegmentWithTwoLane southRoadSegment = new RoadSegmentWithTwoLane(OntologyUtils.createIndividualAndSetHisType(indivRoadSegmentSouth, classRoadSegment),
																			 OntologyUtils.createIndividualAndSetHisType(indivRoadSegmentSouth_lane1, classOneWayLane),
																			 OntologyUtils.createIndividualAndSetHisType(indivRoadSegmentSouth_lane2, classOneWayLane));
		// West
		RoadSegmentWithTwoLane westRoadSegment = new RoadSegmentWithTwoLane(OntologyUtils.createIndividualAndSetHisType(indivRoadSegmentWest, classRoadSegment),
																			OntologyUtils.createIndividualAndSetHisType(indivRoadSegmentWest_lane1, classOneWayLane),
																			OntologyUtils.createIndividualAndSetHisType(indivRoadSegmentWest_lane2, classOneWayLane));
		mapData.setNorthRoadSegment(northRoadSegment);
		mapData.setEastRoadSegment(eastRoadSegment);
		mapData.setSouthRoadSegment(southRoadSegment);
		mapData.setWestRoadSegment(westRoadSegment);
	}
	
	/**
	 * Connects the lanes to the road segments. 
	 * Create even its inverse property: isLaneOf.
	 * @param ontology OWLOntologyWithTools
	 * @throws LoadedOntoNotSelected 
	 */
	public static void addHasLaneObjectProperties(OWLOntologyWithTools ontology) throws LoadedOntoNotSelected {
		OWLObjectProperty hasLane = OntologyUtils.createObjectProperty(hasLaneIRI);
		OWLObjectProperty isLaneOf = OntologyUtils.createObjectProperty(isLaneOfIRI);
		mapData.setHasLane(hasLane);
		mapData.setIsLaneOf(isLaneOf);
		// Set hasLane inverse of isLaneOf
		ontology.addAxiom(OntologyUtils.createInverseObjectProperty(hasLane, isLaneOf));
		
		// North Road Segment
		createObjectPropertyAxiom(ontology, mapData.getNorthRoadSegment().getRoadSegment(), hasLane, mapData.getNorthRoadSegment().getLane1());
		createObjectPropertyAxiom(ontology, mapData.getNorthRoadSegment().getRoadSegment(), hasLane, mapData.getNorthRoadSegment().getLane2());
		// East Road Segment
		createObjectPropertyAxiom(ontology, mapData.getEastRoadSegment().getRoadSegment(), hasLane, mapData.getEastRoadSegment().getLane1());
		createObjectPropertyAxiom(ontology, mapData.getEastRoadSegment().getRoadSegment(), hasLane, mapData.getEastRoadSegment().getLane2());
		// South Road Segment
		createObjectPropertyAxiom(ontology, mapData.getSouthRoadSegment().getRoadSegment(), hasLane, mapData.getSouthRoadSegment().getLane1());
		createObjectPropertyAxiom(ontology, mapData.getSouthRoadSegment().getRoadSegment(), hasLane, mapData.getSouthRoadSegment().getLane2());
		// West Road Segment
		createObjectPropertyAxiom(ontology, mapData.getWestRoadSegment().getRoadSegment(), hasLane, mapData.getWestRoadSegment().getLane1());
		createObjectPropertyAxiom(ontology, mapData.getWestRoadSegment().getRoadSegment(), hasLane, mapData.getWestRoadSegment().getLane2());
	}
	
	/**
	 * Create the axioms thas involves the property hasLane. 
	 * Create even its inverse property: isLaneOf.
	 * @param ontology OWLOntologyWithTools
	 * @throws LoadedOntoNotSelected 
	 */
	public static void addLanesConnections(OWLOntologyWithTools ontology) throws LoadedOntoNotSelected {
		OWLObjectProperty turnRightTo = OntologyUtils.createObjectProperty(turnRightToIRI);
		OWLObjectProperty turnLeftTo = OntologyUtils.createObjectProperty(turnLeftToIRI);
		OWLObjectProperty goStraightTo = OntologyUtils.createObjectProperty(goStraightToIRI);
		
		mapData.setTurnLeftTo(turnLeftTo);
		mapData.setTurnRightTo(turnRightTo);
		mapData.setGoStraightTo(goStraightTo);
		
		// North Road Lane
		createObjectPropertyAxiom(ontology, mapData.getNorthRoadSegment().getLane1(), turnRightTo, mapData.getWestRoadSegment().getLane2());
		createObjectPropertyAxiom(ontology, mapData.getNorthRoadSegment().getLane1(), goStraightTo, mapData.getSouthRoadSegment().getLane2());
		createObjectPropertyAxiom(ontology, mapData.getNorthRoadSegment().getLane1(), turnLeftTo, mapData.getEastRoadSegment().getLane2());
		// East Road Lane
		createObjectPropertyAxiom(ontology, mapData.getEastRoadSegment().getLane1(), turnRightTo, mapData.getNorthRoadSegment().getLane2());
		createObjectPropertyAxiom(ontology, mapData.getEastRoadSegment().getLane1(), goStraightTo, mapData.getWestRoadSegment().getLane2());
		createObjectPropertyAxiom(ontology, mapData.getEastRoadSegment().getLane1(), turnLeftTo, mapData.getSouthRoadSegment().getLane2());
		// South Road Lane
		createObjectPropertyAxiom(ontology, mapData.getSouthRoadSegment().getLane1(), turnRightTo, mapData.getEastRoadSegment().getLane2());
		createObjectPropertyAxiom(ontology, mapData.getSouthRoadSegment().getLane1(), goStraightTo, mapData.getNorthRoadSegment().getLane2());
		createObjectPropertyAxiom(ontology, mapData.getSouthRoadSegment().getLane1(), turnLeftTo, mapData.getWestRoadSegment().getLane2());
		// West Road Lane
		createObjectPropertyAxiom(ontology, mapData.getWestRoadSegment().getLane1(), turnRightTo, mapData.getSouthRoadSegment().getLane2());
		createObjectPropertyAxiom(ontology, mapData.getWestRoadSegment().getLane1(), goStraightTo, mapData.getEastRoadSegment().getLane2());
		createObjectPropertyAxiom(ontology, mapData.getWestRoadSegment().getLane1(), turnLeftTo, mapData.getNorthRoadSegment().getLane2());
	}
	
	/** 
	 * Connects the road segments with the intesection.
	 * @param ontology OWLOntologyWithTools
	 * @throws LoadedOntoNotSelected 
	 */
	public static void addRoadSegmentAndIntersectionConnection(OWLOntologyWithTools ontology) throws LoadedOntoNotSelected {
		OWLObjectProperty isConnectedTo = OntologyUtils.createObjectProperty(isConnectedToIRI);
		// Set as symmetric
		ontology.addAxiom(OntologyUtils.createSymmetricObjectProperty(isConnectedTo));
		mapData.setIsConnectedTo(isConnectedTo);
		// North Road Segment
		createObjectPropertyAxiom(ontology, mapData.getNorthRoadSegment().getRoadSegment(), isConnectedTo, mapData.getIntersection());
		createObjectPropertyAxiom(ontology, mapData.getNorthRoadSegment().getRoadSegment(), mapData.getTurnRightTo(), mapData.getWestRoadSegment().getRoadSegment());
		// East Road Segment
		createObjectPropertyAxiom(ontology, mapData.getEastRoadSegment().getRoadSegment(), isConnectedTo, mapData.getIntersection());
		createObjectPropertyAxiom(ontology, mapData.getEastRoadSegment().getRoadSegment(), mapData.getTurnRightTo(), mapData.getNorthRoadSegment().getRoadSegment());
		// South Road Segment
		createObjectPropertyAxiom(ontology, mapData.getSouthRoadSegment().getRoadSegment(), isConnectedTo, mapData.getIntersection());
		createObjectPropertyAxiom(ontology, mapData.getSouthRoadSegment().getRoadSegment(), mapData.getTurnRightTo(), mapData.getEastRoadSegment().getRoadSegment());
		// West Road Segment
		createObjectPropertyAxiom(ontology, mapData.getWestRoadSegment().getRoadSegment(), isConnectedTo, mapData.getIntersection());
		createObjectPropertyAxiom(ontology, mapData.getWestRoadSegment().getRoadSegment(), mapData.getTurnRightTo(), mapData.getSouthRoadSegment().getRoadSegment());
		
	}
	
	private static void createObjectPropertyAxiom(OWLOntologyWithTools ontology, OWLIndividual subject, OWLObjectProperty property, OWLIndividual object) throws LoadedOntoNotSelected {
		ontology.addAxiom(OntologyUtils.createObjectPropertyAssertionAxiom(subject, property, object));
	}
}
