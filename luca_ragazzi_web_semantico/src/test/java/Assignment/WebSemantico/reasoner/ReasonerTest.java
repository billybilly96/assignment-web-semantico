package Assignment.WebSemantico.reasoner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import Assignment.WebSemantico.Main;
import Assignment.WebSemantico.cardata.MyCarData;
import Assignment.WebSemantico.cardata.MyCarDataUtils;
import Assignment.WebSemantico.intersection.uncontrolled.rules.SWRLGenericRules;
import Assignment.WebSemantico.mapdata.MyMapData;
import Assignment.WebSemantico.pathdata.MyPathData;
import Assignment.WebSemantico.pathdata.MyPathDataUtils;
import Assignment.WebSemantico.utils.ontology.LoadMainOntology;
import Assignment.WebSemantico.utils.ontology.OWLOntologyWithTools;
import Assignment.WebSemantico.utils.ontology.OntologyLoadUtils;
import Assignment.WebSemantico.utils.ontology.OntologyUtils;
import Assignment.WebSemantico.utils.ontology.SWRLUtils;
import Assignment.WebSemantico.utils.ontology.exception.LoadedOntoNotSelected;
import Assignment.WebSemantico.utils.ontology.exception.OntologyCreationOrLoadingFailed;

public class ReasonerTest {
	private final static String base = "http://www.ws.it/ontologies/individualsexample";
	
	// Individuals
	private final String shereKhanId = "#ShereKhan";
	private final String individualAndrea = "#Andrea";
	private final String individualRoberto = "#Roberto";
	private final String individualMarco = "#Marco";
	private final String individualCar1 = "#Car1";
	private final String individualCar2 = "#Car2";
	//Object properties
	private final String hasFather = "#hasFather";
	private final String hasGrandfather = "#hasGrandfather";
	//Classes
	private final String classTiger = "#Tiger";
	private final String classAnimal = "#Animal";
	//SWRL vars
	private final String swrlVarX = "#x";
	private final String swrlVarY = "#y";
	private final String swrlVarZ = "#z";
	
	private static OWLOntologyWithTools ontology;
	private static OWLReasoner reasoner;
	
	@Before public void loadTestOntology() {
		ontology = OntologyLoadUtils.createEmptyOntology(IRI.create(base)).get();
		reasoner = ReasonerUtils.createReasoner(ontology.getOntology());
		LoadMainOntology.selectOntology(ontology);
    }
	
	@Test public void testComputeSwrlRuleClassInfences() throws LoadedOntoNotSelected {
		OWLClass tigerClass = OntologyUtils.createClass(IRI.create(base, classTiger));
        OWLClass animalClass = OntologyUtils.createClass(IRI.create(base, classAnimal));
     	OWLNamedIndividual shereKhan = OntologyUtils.createIndividual(IRI.create(base, shereKhanId));
     	OWLClassAssertionAxiom shereKhan_isA_tiger = OntologyUtils.createClassAssertionAxiom(tigerClass, shereKhan);
     	ontology.addAxiom(shereKhan_isA_tiger);
     	
        SWRLVariable varX = SWRLUtils.createSwrlVariable(IRI.create(base, swrlVarX));
        
        //tiger(?x) -> animal(?x)
        SWRLRule rule = SWRLUtils.createAnonymousSwrlRule(Collections.singleton(SWRLUtils.createSwrlClassAtom(tigerClass, varX)),	 
        												 Collections.singleton(SWRLUtils.createSwrlClassAtom(animalClass, varX))); 
              
        assertTrue("Add swrl rule should return 'true'", ontology.addAxiom(rule));
        reasoner.flush();
        assertTrue("Number of individuals included into the Animals set should be '1'", 
        		ReasonerUtils.getIndividualsByClass(reasoner, animalClass).nodes().count() == 1);
        assertTrue("Shere Khan is a tiger so Shere Khan is an animal.", 
        		ReasonerUtils.getIndividualsByClass(reasoner, animalClass).containsEntity(shereKhan));
    }
	
	@Test public void testComputeSwrlRuleObjectPropertyInference() throws LoadedOntoNotSelected {
		// Individuals
     	OWLNamedIndividual andrea = OntologyUtils.createIndividual(IRI.create(base, individualAndrea));
     	OWLNamedIndividual roberto = OntologyUtils.createIndividual(IRI.create(base, individualRoberto));
     	OWLNamedIndividual marco = OntologyUtils.createIndividual(IRI.create(base, individualMarco));
     	OWLObjectProperty opHasFather = OntologyUtils.createObjectProperty(IRI.create(base, hasFather));
     	
     	// Marco hasFather Roberto
     	OWLObjectPropertyAssertionAxiom marcoHasFatherRoberto = OntologyUtils.createObjectPropertyAssertionAxiom(marco, opHasFather, roberto);
     	// Roberto hasFather Andrea
     	OWLObjectPropertyAssertionAxiom robertoHasFatherAndrea = OntologyUtils.createObjectPropertyAssertionAxiom(roberto, opHasFather, andrea);
     	
     	ontology.addAxiom(marcoHasFatherRoberto);
     	ontology.addAxiom(robertoHasFatherAndrea);
			        
        OWLObjectProperty opHhasGrandfather = OntologyUtils.createObjectProperty(IRI.create(base, hasGrandfather));
        SWRLVariable varX = SWRLUtils.createSwrlVariable(IRI.create(base, swrlVarX));
        SWRLVariable varY = SWRLUtils.createSwrlVariable(IRI.create(base, swrlVarY));
        SWRLVariable varZ = SWRLUtils.createSwrlVariable(IRI.create(base, swrlVarZ));
        
        // P(x, y) where P is an OWL objectproperty (expression) and x and y are are either individuals or variables for individuals.
        SWRLObjectPropertyAtom propAtom_X_hasFather_Y = SWRLUtils.createSWRLObjectPropertyAtom(opHasFather, varX, varY); 
        SWRLObjectPropertyAtom propAtom_Y_hasFather_Z = SWRLUtils.createSWRLObjectPropertyAtom(opHasFather, varY, varZ);
        SWRLObjectPropertyAtom propAtom_X_hasGrandfather_Z = SWRLUtils.createSWRLObjectPropertyAtom(opHhasGrandfather, varX, varZ);
        
        Set<SWRLAtom> antecedent = new HashSet<SWRLAtom>(); //Atoms can either be in the head (concequent) or body (antecedent) of the rule. 
        antecedent.add(propAtom_X_hasFather_Y);
        antecedent.add(propAtom_Y_hasFather_Z);
        // hasFather(?y, ?z) ^ hasFather(?x, ?y) -> hasGrandfather(?x, ?z)
        SWRLRule rule2 = SWRLUtils.createAnonymousSwrlRule(antecedent, Collections.singleton(propAtom_X_hasGrandfather_Z));
        
        assertTrue("Add swrl rule should return 'true'", ontology.addAxiom(rule2));
        reasoner.flush();
        assertTrue("Number of values for the property hasGrandfather of marco should be '1'", reasoner.getObjectPropertyValues(marco, opHhasGrandfather).entities().count() == 1);
        assertTrue("The grandfather of marco should be andrea", reasoner.getObjectPropertyValues(marco, opHhasGrandfather).containsEntity(andrea));   
	}
	
	@Test public void testMyCollisionWarning() throws LoadedOntoNotSelected {
		MyCarDataUtils.addEgoVehicle();
		MyPathDataUtils.addClassAndObjectPropertyForSwrlRules(ontology);
		// Individuals
     	OWLNamedIndividual car1 = OntologyUtils.createIndividual(IRI.create(base, individualCar1));
     	OWLNamedIndividual car2 = OntologyUtils.createIndividual(IRI.create(base, individualCar2));
     	
     	OWLObjectPropertyAssertionAxiom car1_CollisionWarningWith_car2 = OntologyUtils.createObjectPropertyAssertionAxiom(car1, 
     																				MyPathData.getInstance().getCollisionWarningWith(), car2);
     		        
        SWRLGenericRules.collisionWarningRule(ontology, IRI.create(base, swrlVarX), IRI.create(base, swrlVarY));
        ontology.addAxiom(car1_CollisionWarningWith_car2);
        reasoner.flush();
       
		assertTrue("Get swrl rules number should return '1'",  ontology.getSwrlRuleNumber() == 1);
        
		assertFalse("car1 shouldn't have a running position.", 
        		ReasonerUtils.vehicleIsRunningOn(reasoner, car1, MyMapData.getInstance().getClassOneWayLane()));
		
        assertTrue("car1 should have a collision warning.", 
        		ReasonerUtils.getIndividualsByClass(reasoner, MyPathData.getInstance().getCollisionWarning()).containsEntity(car1));   
        assertTrue("car2 should have a collision warning.", 
        		ReasonerUtils.getIndividualsByClass(reasoner, MyPathData.getInstance().getCollisionWarning()).containsEntity(car2));
	}
	
	@Test public void testMyDrivingDirectionRight() throws OntologyCreationOrLoadingFailed, LoadedOntoNotSelected {
		ontology = Main.createMyPathDataOntology();
		
		// Add the paths segments sequence to reach the endNode.
		List<OWLNamedIndividual> pathSequence = new ArrayList<>();
		pathSequence.add(MyMapData.getInstance().getSouthRoadSegment().getLane1()); // StartNode
		pathSequence.add(MyMapData.getInstance().getIntersection());
		pathSequence.add(MyMapData.getInstance().getEastRoadSegment().getLane2()); // EndNode
		MyPathDataUtils.addSequenceOfPathSegments(ontology, pathSequence);
		
		Main.addGeneralSwrlRulesToOntology(ontology);
		
		OWLReasoner reasoner = ReasonerUtils.createReasoner(ontology.getOntology());
		
		assertTrue("Ego vehicle should turn right.", 
        		ReasonerUtils.getIndividualsByClass(reasoner, MyPathData.getInstance().getTurnRight()).containsEntity( MyCarData.getInstance().getEgoVehicle()));
		assertFalse("Ego vehicle should't turn left.", 
        		ReasonerUtils.getIndividualsByClass(reasoner, MyPathData.getInstance().getTurnLeft()).containsEntity( MyCarData.getInstance().getEgoVehicle()));
		assertFalse("Ego vehicle should't go forward.", 
        		ReasonerUtils.getIndividualsByClass(reasoner, MyPathData.getInstance().getGoForward()).containsEntity( MyCarData.getInstance().getEgoVehicle()));
		reasoner.dispose();
	}
	
	@Test public void testMyDrivingDirectionLeft() throws OntologyCreationOrLoadingFailed, LoadedOntoNotSelected {
		ontology = Main.createMyPathDataOntology();
		
		// Add the paths segments sequence to reach the endNode.
		List<OWLNamedIndividual> pathSequence = new ArrayList<>();
		pathSequence.add(MyMapData.getInstance().getSouthRoadSegment().getLane1()); // StartNode
		pathSequence.add(MyMapData.getInstance().getIntersection());
		pathSequence.add(MyMapData.getInstance().getWestRoadSegment().getLane2()); // EndNode
		MyPathDataUtils.addSequenceOfPathSegments(ontology, pathSequence);
		
		Main.addGeneralSwrlRulesToOntology(ontology);
		OWLReasoner reasoner = ReasonerUtils.createReasoner(ontology.getOntology());
		
		assertTrue("Ego vehicle should turn left.", 
        		ReasonerUtils.getIndividualsByClass(reasoner, MyPathData.getInstance().getTurnLeft()).containsEntity( MyCarData.getInstance().getEgoVehicle()));
		assertFalse("Ego vehicle should't turn right.", 
        		ReasonerUtils.getIndividualsByClass(reasoner, MyPathData.getInstance().getTurnRight()).containsEntity( MyCarData.getInstance().getEgoVehicle()));
		assertFalse("Ego vehicle should't go forward.", 
        		ReasonerUtils.getIndividualsByClass(reasoner, MyPathData.getInstance().getGoForward()).containsEntity( MyCarData.getInstance().getEgoVehicle()));
		reasoner.dispose();
	}
	
	@Test public void testMyDrivingDirectionForward() throws OntologyCreationOrLoadingFailed, LoadedOntoNotSelected {
		// I need a new ontology.
		OWLOntologyWithTools o = OntologyLoadUtils.createEmptyOntology(IRI.create(base)).get();
		o = Main.createMyPathDataOntology();
		
		// Add the paths segments sequence to reach the endNode.
		List<OWLNamedIndividual> pathSequence = new ArrayList<>();
		pathSequence.add(MyMapData.getInstance().getSouthRoadSegment().getLane1()); // StartNode
		pathSequence.add(MyMapData.getInstance().getIntersection());
		pathSequence.add(MyMapData.getInstance().getNorthRoadSegment().getLane2()); // EndNode
		MyPathDataUtils.addSequenceOfPathSegments(o, pathSequence);
		
		Main.addGeneralSwrlRulesToOntology(o);
		OWLReasoner reasoner = ReasonerUtils.createReasoner(o.getOntology());
		
		assertTrue("StartNode should be the first of the list.", 
        		ReasonerUtils.getStartNode(reasoner).containsEntity(pathSequence.get(0)));
		
		assertTrue("StartNode should be the last of the list.", 
        		ReasonerUtils.getEndNode(reasoner).containsEntity(pathSequence.get(pathSequence.size()-1)));
		
		assertTrue("Ego vehicle should turn left.", 
        		ReasonerUtils.getIndividualsByClass(reasoner, MyPathData.getInstance().getGoForward()).containsEntity( MyCarData.getInstance().getEgoVehicle()));
		assertFalse("Ego vehicle should't turn left.", 
        		ReasonerUtils.getIndividualsByClass(reasoner, MyPathData.getInstance().getTurnLeft()).containsEntity( MyCarData.getInstance().getEgoVehicle()));
		assertFalse("Ego vehicle should't turn right.", 
        		ReasonerUtils.getIndividualsByClass(reasoner, MyPathData.getInstance().getTurnRight()).containsEntity( MyCarData.getInstance().getEgoVehicle()));
		reasoner.dispose();
	}
	
	@After
	public void clean() {
		ontology = null;
		reasoner.dispose();
	}
}
