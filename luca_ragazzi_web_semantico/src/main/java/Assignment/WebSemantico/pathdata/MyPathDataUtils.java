package Assignment.WebSemantico.pathdata;

import java.util.List;

import Assignment.WebSemantico.cardata.SpecialVehicleDataUtils;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import Assignment.WebSemantico.Main;
import Assignment.WebSemantico.cardata.MyCarDataUtils;
import Assignment.WebSemantico.utils.ontology.OWLOntologyWithTools;
import Assignment.WebSemantico.utils.ontology.OntologyUtils;
import Assignment.WebSemantico.utils.ontology.exception.LoadedOntoNotSelected;

public class MyPathDataUtils {
	public final static String controlOntoIri = "http://www.toyota-ti.ac.jp/Lab/Denshi/COIN/Control";
	// Classes
	private final static IRI startNodeClassIRI = IRI.create(controlOntoIri + "#StartNode");
	private final static IRI endNodeClassIRI = IRI.create(controlOntoIri + "#EndNode");
	private final static IRI startNodeTramClassIRI = IRI.create(controlOntoIri + "#TramStartNode");
	private final static IRI endNodeTramClassIRI = IRI.create(controlOntoIri + "#TramEndNode");
	private final static IRI classCollisionWarningIRI = IRI.create(Main.myOntologyIRI + "#CollisionWarning");
	private final static IRI classOverSpeedWarningIRI = IRI.create(Main.myOntologyIRI + "#OverSpeedWarning");
	private final static IRI turnLeftClassIRI = IRI.create(controlOntoIri + "#TurnLeft");
	private final static IRI turnRightClassIRI = IRI.create(controlOntoIri + "#TurnRight");
	private final static IRI goForwardClassIRI = IRI.create(controlOntoIri + "#GoForward");
	private final static IRI goClassIRI = IRI.create(controlOntoIri + "#Go");
	private final static IRI stopClassIRI = IRI.create(controlOntoIri + "#Stop");
	// RoadSegment
	private final static IRI nextPathSegmentIRI = IRI.create(controlOntoIri + "#nextPathSegment");
	// Data property: 
	private final static IRI dataPropertypathSegmentIdIRI = IRI.create(controlOntoIri + "#pathSegmentID");
	// Object property
	private final static IRI collisionWarningWithIRI = IRI.create(MyPathDataUtils.controlOntoIri + "#collisionWarningWith");
	private final static IRI overSpeedWarningRespectToIRI = IRI.create(Main.myOntologyIRI + "#overSpeedWarningRespectTo");
	private final static IRI giveWayIRI = IRI.create(MyPathDataUtils.controlOntoIri + "#giveWay");
	private final static MyPathData pathData = MyPathData.getInstance();
	/**
	 * Creates: start node and end node.
	 * @param ontology OWLOntologyWithTools
	 * @throws LoadedOntoNotSelected 
	 */
	private static void addStartAndEndNodeOfThePath(OWLOntologyWithTools ontology, OWLNamedIndividual startRoadSegment, OWLNamedIndividual endRoadSegment) throws LoadedOntoNotSelected {
		OWLClass classStartNode = OntologyUtils.createClass(startNodeClassIRI);
		OWLClass classEndNode = OntologyUtils.createClass(endNodeClassIRI);
		pathData.setStartLane(classStartNode);
		pathData.setEndLane(classEndNode);
		ontology.addAxiom(OntologyUtils.createClassAssertionAxiom(classStartNode, startRoadSegment));
		ontology.addAxiom(OntologyUtils.createClassAssertionAxiom(classEndNode, endRoadSegment));
	}

	private static void addStartAndEndNodeOfTheTramPath(OWLOntologyWithTools ontology, OWLNamedIndividual startRoadSegment, OWLNamedIndividual endRoadSegment) throws LoadedOntoNotSelected {
		OWLClass classStartNode = OntologyUtils.createClass(startNodeTramClassIRI);
		OWLClass classEndNode = OntologyUtils.createClass(endNodeTramClassIRI);
		pathData.setTramStartLane(classStartNode);
		pathData.setTramEndLane(classEndNode);
		ontology.addAxiom(OntologyUtils.createClassAssertionAxiom(classStartNode, startRoadSegment));
		ontology.addAxiom(OntologyUtils.createClassAssertionAxiom(classEndNode, endRoadSegment));
	}
	
	/**
	 * Creates the path segments sequence, from startNode to endNode.
	 * @param ontology OWLOntologyWithTools
	 * @param orderedSegments ordered sequence from startNode to endNode.
	 * @throws LoadedOntoNotSelected 
	 */
	public static void addSequenceOfPathSegments(OWLOntologyWithTools ontology, List<OWLNamedIndividual> orderedSegments) throws LoadedOntoNotSelected {
		OWLObjectProperty nextPathSegment = OntologyUtils.createObjectProperty(nextPathSegmentIRI);
		OWLDataProperty dpPathSegmentID = OntologyUtils.createDataProperty(dataPropertypathSegmentIdIRI);
		pathData.setNextPathSegment(nextPathSegment);
		
		// Add start and end road segments for the path 
		addStartAndEndNodeOfThePath(ontology, orderedSegments.get(0), orderedSegments.get(orderedSegments.size()-1));
		
		pathData.setPathSegmentID(dpPathSegmentID);
		for (int i = 0; i < (orderedSegments.size() - 1); i++) {
			ontology.addAxiom(OntologyUtils.createObjectPropertyAssertionAxiom(orderedSegments.get(i), nextPathSegment, orderedSegments.get(i+1)));
			ontology.addAxiom(OntologyUtils.createDataPropertyAxiom(dpPathSegmentID, orderedSegments.get(i), Integer.toString(i)));
		}
		int last = orderedSegments.size() - 1;
		ontology.addAxiom(OntologyUtils.createDataPropertyAxiom(dpPathSegmentID, orderedSegments.get(last), Integer.toString(last)));
		
		// Ego vehicle is running on the start node.
		MyCarDataUtils.addEgoVehiclePosition(ontology, orderedSegments.get(0));
	}

	/**
	 * Creates the path segments sequence, from startNode to endNode.
	 * @param ontology OWLOntologyWithTools
	 * @param orderedSegments ordered sequence from startNode to endNode.
	 * @throws LoadedOntoNotSelected
	 */
	public static void addSequenceOfPathSegmentsForTramVehicle(OWLOntologyWithTools ontology, List<OWLNamedIndividual> orderedSegments) throws LoadedOntoNotSelected {
		OWLObjectProperty nextPathSegment = OntologyUtils.createObjectProperty(nextPathSegmentIRI);
		OWLDataProperty dpPathSegmentID = OntologyUtils.createDataProperty(dataPropertypathSegmentIdIRI);
		pathData.setNextPathSegment(nextPathSegment);

		// Add start and end road segments for the path
		addStartAndEndNodeOfThePath(ontology, orderedSegments.get(0), orderedSegments.get(orderedSegments.size()-1));

		pathData.setPathSegmentID(dpPathSegmentID);
		for (int i = 0; i < (orderedSegments.size() - 1); i++) {
			ontology.addAxiom(OntologyUtils.createObjectPropertyAssertionAxiom(orderedSegments.get(i), nextPathSegment, orderedSegments.get(i+1)));
			ontology.addAxiom(OntologyUtils.createDataPropertyAxiom(dpPathSegmentID, orderedSegments.get(i), Integer.toString(i)));
		}
		int last = orderedSegments.size() - 1;
		ontology.addAxiom(OntologyUtils.createDataPropertyAxiom(dpPathSegmentID, orderedSegments.get(last), Integer.toString(last)));

		SpecialVehicleDataUtils.addTramVehiclePosition(ontology, orderedSegments.get(0));
	}
	
	/**
	 * Creates the object properties and the classes for the swrl rules.
	 * @param ontology OWLOntologyWithTools
	 * @throws LoadedOntoNotSelected 
	 */
	public static void addClassAndObjectPropertyForSwrlRules(OWLOntologyWithTools ontology) throws LoadedOntoNotSelected {

		// Overspeed warning
		OWLClass overSpeedWarningClass = OntologyUtils.createClass(classOverSpeedWarningIRI);
		OWLObjectProperty overSpeedWarningRespectTo = OntologyUtils.createObjectProperty(overSpeedWarningRespectToIRI);
		pathData.setOverSpeedWarning(overSpeedWarningClass);
		pathData.setOverSpeedWarningRespectTo(overSpeedWarningRespectTo);

		// Collision warning
		OWLClass collisionWarningClass = OntologyUtils.createClass(classCollisionWarningIRI);
		OWLObjectProperty collisionWarningWith = OntologyUtils.createObjectProperty(collisionWarningWithIRI);
		pathData.setCollisionWarning(collisionWarningClass);
		pathData.setCollisionWarningWith(collisionWarningWith);
		
		// Driving directions and actions
		OWLClass turnLeft = OntologyUtils.createClass(turnLeftClassIRI);
		OWLClass turnRight = OntologyUtils.createClass(turnRightClassIRI);
		OWLClass goForward = OntologyUtils.createClass(goForwardClassIRI);
		OWLClass go = OntologyUtils.createClass(goClassIRI);
		OWLClass stop = OntologyUtils.createClass(stopClassIRI);
		
		pathData.setTurnLeft(turnLeft);
		pathData.setTurnRight(turnRight);
		pathData.setGoForward(goForward);
		pathData.setGo(go);
		pathData.setStop(stop);
		
		// GiveWay
		OWLObjectProperty giveWay = OntologyUtils.createObjectProperty(giveWayIRI);
		pathData.setGiveWay(giveWay);
	}
}
