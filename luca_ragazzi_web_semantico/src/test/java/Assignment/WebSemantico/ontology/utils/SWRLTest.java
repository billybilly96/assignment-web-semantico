package Assignment.WebSemantico.ontology.utils;

import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;

import Assignment.WebSemantico.utils.ontology.LoadMainOntology;
import Assignment.WebSemantico.utils.ontology.OWLOntologyWithTools;
import Assignment.WebSemantico.utils.ontology.OntologyLoadUtils;
import Assignment.WebSemantico.utils.ontology.OntologyUtils;
import Assignment.WebSemantico.utils.ontology.SWRLUtils;
import Assignment.WebSemantico.utils.ontology.exception.LoadedOntoNotSelected;

public class SWRLTest {
	private final static String base = "http://www.ws.it/ontologies/swrlexamples";
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
		
	@Before public void loadTestOntology() {
		ontology = OntologyLoadUtils.createEmptyOntology(IRI.create(base)).get();	
		LoadMainOntology.selectOntology(ontology);
	}
	
	@Test public void testAddSwrlRule() throws LoadedOntoNotSelected {
		OWLClass tigerClass = OntologyUtils.createClass(IRI.create(base, classTiger));
        OWLClass animalClass = OntologyUtils.createClass(IRI.create(base, classAnimal));
        
        SWRLVariable varX = SWRLUtils.createSwrlVariable(IRI.create(base, swrlVarX));
        
        //tiger(?x) -> animal(?x)
        SWRLRule rule = SWRLUtils.createAnonymousSwrlRule(Collections.singleton(SWRLUtils.createSwrlClassAtom(tigerClass, varX)),	 
        												  Collections.singleton(SWRLUtils.createSwrlClassAtom(animalClass, varX))); 
              
        assertTrue("Add swrl rule should return 'true'", ontology.addAxiom(rule));
		assertTrue("Get swrl rules number should return '1'",  ontology.getSwrlRuleNumber() == 1);
        
                
        OWLObjectProperty opHasFather = OntologyUtils.createObjectProperty(IRI.create(base, hasFather));
        OWLObjectProperty opHhasGrandfather = OntologyUtils.createObjectProperty(IRI.create(base, hasGrandfather));
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
		assertTrue("Get swrl rules number should return '2'",  ontology.getSwrlRuleNumber() == 2);
    }
	
	@Test(expected = LoadedOntoNotSelected.class) 
	public void testExceptionIfDontSelectOnto() throws LoadedOntoNotSelected {
		SWRLUtils.selectOntology(null);
		SWRLUtils.createSwrlVariable(IRI.create(base, swrlVarX));
    }
}
