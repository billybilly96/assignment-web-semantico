package Assignment.WebSemantico.intersection.rules;

import static org.junit.Assert.assertTrue;

import Assignment.WebSemantico.cardata.SpecialVehicleData;
import Assignment.WebSemantico.cardata.SpecialVehicleDataUtils;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import Assignment.WebSemantico.Main;
import Assignment.WebSemantico.cardata.MyCarData;
import Assignment.WebSemantico.intersection.uncontrolled.rules.SWRLItalianRules;
import Assignment.WebSemantico.mapdata.MyMapData;
import Assignment.WebSemantico.pathdata.MyPathData;
import Assignment.WebSemantico.reasoner.ReasonerUtils;
import Assignment.WebSemantico.utils.ontology.LoadMainOntology;
import Assignment.WebSemantico.utils.ontology.OWLOntologyWithTools;
import Assignment.WebSemantico.utils.ontology.OntologyLoadUtils;
import Assignment.WebSemantico.utils.ontology.OntologyUtils;
import Assignment.WebSemantico.utils.ontology.exception.LoadedOntoNotSelected;
import Assignment.WebSemantico.utils.ontology.exception.OntologyCreationOrLoadingFailed;

public class ReasonerUncontrolledIntersection {
	private final static String base = "http://www.ws.it/reasoner/uncontrolled/intersection/onto";
	private final static IRI swrlVarXIRI = IRI.create(base + "#X");
	private final static IRI swrlVarYIRI = IRI.create(base + "#Y");
	private final static IRI swrlVarIntIRI = IRI.create(base + "#INT");
	private final static IRI swrlVarLaneIRI = IRI.create(base + "#LANE");
	private final static IRI swrlVarLane2IRI = IRI.create(base + "#LANE2");
	private final static IRI swrlVarRoadSegIRI = IRI.create(base + "#RS");
	private final static IRI swrlVarRoadSeg2IRI = IRI.create(base + "#RS2");
	private final static IRI individualCar1IRI = IRI.create(base + "#Car1");
	
	private final static MyMapData mapData = MyMapData.getInstance();
	private final static MyCarData carData = MyCarData.getInstance();
	private final static MyPathData pathData = MyPathData.getInstance();
	private final static SpecialVehicleData specialVehicleData = SpecialVehicleData.getInstance();
	
	@Test public void testGiveWayToVehicleThatIsRunningOnIntersection() throws OntologyCreationOrLoadingFailed, LoadedOntoNotSelected {
		OWLOntologyWithTools o = OntologyLoadUtils.createEmptyOntology(IRI.create(base)).get();
		LoadMainOntology.selectOntology(o);
		o = Main.createMyPathDataOntology();
		Main.setEgoVehiclePathFromSouthToWest(o);
		Main.addGeneralSwrlRulesToOntology(o);
		
		SWRLItalianRules.giveWayToVehicleThatIsRunningOnIntersection(o, swrlVarXIRI, swrlVarYIRI, swrlVarIntIRI, swrlVarLaneIRI);
		
		// Individuals
     	OWLNamedIndividual car1 = OntologyUtils.createIndividual(individualCar1IRI);
     	// collisionWarningWith(egoVehicle, car1)
     	OWLObjectPropertyAssertionAxiom vehicleEgo_hasCollisionWarningWith_car1 = 
     			OntologyUtils.createObjectPropertyAssertionAxiom(carData.getEgoVehicle(), 
     					                                            pathData.getCollisionWarningWith(), car1);
		o.addAxiom(vehicleEgo_hasCollisionWarningWith_car1);
		// car1 isRunningOn intersection
		OWLObjectPropertyAssertionAxiom car1_isRunningOn_intersection = OntologyUtils.createObjectPropertyAssertionAxiom(car1, carData.getIsRunningOn(), 
																																  mapData.getIntersection());
		o.addAxiom(car1_isRunningOn_intersection);
		
		OWLReasoner reasoner = ReasonerUtils.createReasoner(o.getOntology());
				
		assertTrue("Ego vehicle should run on somewhere.", 
        		ReasonerUtils.getEgoVehiclePosition(reasoner).entities().count() == 1);
	
		assertTrue("Ego vehicle should run on a lane.", 
				ReasonerUtils.vehicleIsRunningOn(reasoner, carData.getEgoVehicle(), mapData.getClassOneWayLane()));
		
		assertTrue("Car1 should run on somewhere.", 
        		reasoner.getObjectPropertyValues(car1, carData.getIsRunningOn()).entities().count() == 1);
	
		assertTrue("Car1 should run on the intersection.",
				ReasonerUtils.vehicleIsRunningOn(reasoner, car1, mapData.getClassIntersection()));
		
		assertTrue("Ego vehicle should stop.", 
				ReasonerUtils.getIndividualsByClass(reasoner, pathData.getStop()).containsEntity(carData.getEgoVehicle()));
		
		assertTrue("Ego vehicle should giveWay to car1.", 
				reasoner.getObjectPropertyValues(carData.getEgoVehicle(), pathData.getGiveWay()).containsEntity(car1));
		
		reasoner.dispose();
	}
	
	@Test public void testRightOfWayRule() throws OntologyCreationOrLoadingFailed, LoadedOntoNotSelected {
		OWLOntologyWithTools o = OntologyLoadUtils.createEmptyOntology(IRI.create(base)).get();
		LoadMainOntology.selectOntology(o);
		o = Main.createMyPathDataOntology();
		Main.setEgoVehiclePathFromSouthToNorth(o);
		Main.addGeneralSwrlRulesToOntology(o);
		
		SWRLItalianRules.rightOfWayRule(o, swrlVarXIRI, swrlVarYIRI, swrlVarLaneIRI, swrlVarLane2IRI, swrlVarRoadSegIRI, swrlVarRoadSeg2IRI);
		
		// Individuals
     	OWLNamedIndividual car1 = OntologyUtils.createIndividual(individualCar1IRI);
     	// collisionWarningWith(egoVehicle, car1)
     	OWLObjectPropertyAssertionAxiom vehicleEgo_hasCollisionWarningWith_car1 = 
     			OntologyUtils.createObjectPropertyAssertionAxiom(carData.getEgoVehicle(), 
     					                                            pathData.getCollisionWarningWith(), car1);
		o.addAxiom(vehicleEgo_hasCollisionWarningWith_car1);
		// car1 isRunningOn lane2
		OWLObjectPropertyAssertionAxiom car1_isRunningOn_eastLane1 = OntologyUtils.createObjectPropertyAssertionAxiom(car1, carData.getIsRunningOn(), 
																																  mapData.getEastRoadSegment().getLane1());
		o.addAxiom(car1_isRunningOn_eastLane1);
				
		OWLReasoner reasoner = ReasonerUtils.createReasoner(o.getOntology());
				
		assertTrue("Ego vehicle should run on somewhere.", 
        		ReasonerUtils.getEgoVehiclePosition(reasoner).entities().count() == 1);
	
		assertTrue("Ego vehicle should run on a lane.", 
				ReasonerUtils.vehicleIsRunningOn(reasoner, carData.getEgoVehicle(), mapData.getClassOneWayLane()));
		
		assertTrue("Car1 should run on somewhere.", 
        		reasoner.getObjectPropertyValues(car1, carData.getIsRunningOn()).entities().count() == 1);
	
		assertTrue("Car1 should run on a lane.",
				ReasonerUtils.vehicleIsRunningOn(reasoner, car1, mapData.getClassOneWayLane()));

		assertTrue("Ego vehicle should stop.", 
				ReasonerUtils.getIndividualsByClass(reasoner, pathData.getStop()).containsEntity(carData.getEgoVehicle()));
		
		assertTrue("Ego vehicle should giveWay to car1.", 
				reasoner.getObjectPropertyValues(carData.getEgoVehicle(), pathData.getGiveWay()).containsEntity(car1));
		
		reasoner.dispose();
	}

	@Test public void testGiveWaySpecialVehicles() throws OntologyCreationOrLoadingFailed, LoadedOntoNotSelected {
		OWLOntologyWithTools o = OntologyLoadUtils.createEmptyOntology(IRI.create(base)).get();
		LoadMainOntology.selectOntology(o);
		o = Main.createMyPathDataOntology();
		Main.setEgoVehiclePathFromSouthToWest(o);
		Main.addGeneralSwrlRulesToOntology(o);

		SpecialVehicleDataUtils.addTramVehicle();
		SpecialVehicleDataUtils.addTramVehiclePosition();

		SWRLItalianRules.rightOfWayRule(o, swrlVarXIRI, swrlVarYIRI, swrlVarLaneIRI, swrlVarLane2IRI, swrlVarRoadSegIRI, swrlVarRoadSeg2IRI);
		SWRLItalianRules.giveWaySpecialVehicles(o, swrlVarXIRI, swrlVarYIRI, swrlVarLaneIRI, swrlVarLane2IRI, swrlVarRoadSegIRI, swrlVarRoadSeg2IRI);

		// collisionWarningWith(egoVehicle, tram)
		OWLObjectPropertyAssertionAxiom vehicleEgo_hasCollisionWarningWith_tram =
				OntologyUtils.createObjectPropertyAssertionAxiom(carData.getEgoVehicle(),
						pathData.getCollisionWarningWith(), specialVehicleData.getTramVehicle());
		o.addAxiom(vehicleEgo_hasCollisionWarningWith_tram);
		// Tram isRunningOn West Road Segment
		OWLObjectPropertyAssertionAxiom tram_isRunningOn_westLane1 = OntologyUtils.createObjectPropertyAssertionAxiom(specialVehicleData.getTramVehicle(), carData.getIsRunningOn(),
				mapData.getWestRoadSegment().getLane1());
		o.addAxiom(tram_isRunningOn_westLane1);

		OWLReasoner reasoner = ReasonerUtils.createReasoner(o.getOntology());

		assertTrue("Ego vehicle should run on somewhere.",
				ReasonerUtils.getEgoVehiclePosition(reasoner).entities().count() == 1);

		assertTrue("Ego vehicle should run on a lane.",
				ReasonerUtils.vehicleIsRunningOn(reasoner, carData.getEgoVehicle(), mapData.getClassOneWayLane()));

		assertTrue("Tram should run on somewhere.",
				reasoner.getObjectPropertyValues(specialVehicleData.getTramVehicle(), specialVehicleData.getIsRunningOn()).entities().count() == 1);

		assertTrue("Tram should run on a lane.",
				ReasonerUtils.vehicleIsRunningOn(reasoner, specialVehicleData.getTramVehicle(), mapData.getClassOneWayLane()));

		assertTrue("Tram should be a special vehicle.",
				ReasonerUtils.getIndividualsByClass(reasoner, specialVehicleData.getClassSpecialVehicle()).containsEntity(specialVehicleData.getTramVehicle()));

		assertTrue("Ego vehicle should stop.",
				ReasonerUtils.getIndividualsByClass(reasoner, pathData.getStop()).containsEntity(carData.getEgoVehicle()));

		assertTrue("Ego vehicle should giveWay to tram.",
				reasoner.getObjectPropertyValues(carData.getEgoVehicle(), pathData.getGiveWay()).containsEntity(specialVehicleData.getTramVehicle()));

		reasoner.dispose();
	}

	@Test public void testGiveWaySpecialVehicles2() throws OntologyCreationOrLoadingFailed, LoadedOntoNotSelected {
		OWLOntologyWithTools o = OntologyLoadUtils.createEmptyOntology(IRI.create(base)).get();
		LoadMainOntology.selectOntology(o);
		o = Main.createMyPathDataOntology();
		Main.setEgoVehiclePathFromSouthToWest(o);
		Main.addGeneralSwrlRulesToOntology(o);

		// Normal Individual
		OWLNamedIndividual car1 = OntologyUtils.createIndividual(individualCar1IRI);

		SpecialVehicleDataUtils.addTramVehicle();
		SpecialVehicleDataUtils.addTramVehiclePosition();

		SWRLItalianRules.rightOfWayRule(o, swrlVarXIRI, swrlVarYIRI, swrlVarLaneIRI, swrlVarLane2IRI, swrlVarRoadSegIRI, swrlVarRoadSeg2IRI);
		SWRLItalianRules.giveWaySpecialVehicles(o, swrlVarXIRI, swrlVarYIRI, swrlVarLaneIRI, swrlVarLane2IRI, swrlVarRoadSegIRI, swrlVarRoadSeg2IRI);

		// collisionWarningWith(egoVehicle, car1)
		OWLObjectPropertyAssertionAxiom vehicleEgo_hasCollisionWarningWith_car1 =
				OntologyUtils.createObjectPropertyAssertionAxiom(carData.getEgoVehicle(),
						pathData.getCollisionWarningWith(), car1);
		o.addAxiom(vehicleEgo_hasCollisionWarningWith_car1);
		// car1 isRunningOn West Road Segment
		OWLObjectPropertyAssertionAxiom car1_isRunningOn_westLane1 = OntologyUtils.createObjectPropertyAssertionAxiom(car1, carData.getIsRunningOn(),
				mapData.getWestRoadSegment().getLane1());
		o.addAxiom(car1_isRunningOn_westLane1);

		OWLReasoner reasoner = ReasonerUtils.createReasoner(o.getOntology());

		assertTrue("Ego vehicle should run on somewhere.",
				ReasonerUtils.getEgoVehiclePosition(reasoner).entities().count() == 1);

		assertTrue("Car1 should run on somewhere.",
				reasoner.getObjectPropertyValues(car1, carData.getIsRunningOn()).entities().count() == 1);

		System.out.println(ReasonerUtils.getIndividualsByClass(reasoner, pathData.getStop()).entities().findFirst());

		assertTrue("Ego vehicle should not stop. Car1 should stop.",
				ReasonerUtils.getIndividualsByClass(reasoner, pathData.getStop()).containsEntity(car1));

		assertTrue("Ego vehicle should not giveWay to car1.",
				reasoner.getObjectPropertyValues(carData.getEgoVehicle(), pathData.getGiveWay()).isEmpty());

		reasoner.dispose();
	}
}
