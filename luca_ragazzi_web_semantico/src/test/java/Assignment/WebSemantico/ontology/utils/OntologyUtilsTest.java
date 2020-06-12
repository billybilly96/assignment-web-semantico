package Assignment.WebSemantico.ontology.utils;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

import Assignment.WebSemantico.utils.ontology.LoadMainOntology;
import Assignment.WebSemantico.utils.ontology.OWLOntologyWithTools;
import Assignment.WebSemantico.utils.ontology.OntologyLoadUtils;
import Assignment.WebSemantico.utils.ontology.OntologyUtils;
import Assignment.WebSemantico.utils.ontology.exception.LoadedOntoNotSelected;

public class OntologyUtilsTest {
	private final static String base = "http://www.ws.it/ontologies/individualsexample";
	private final String individualAndrea = "#andrea";
	private final String individualRoberto = "#roberto";
	private final String objectProperty = "#hasFather";
	private final String dataProperty = "#hasMoney";
	private final String classPerson = "#Person";
	private static OWLOntologyWithTools ontology;
	
	@BeforeClass public static void loadTestOntology() {
		ontology = OntologyLoadUtils.createEmptyOntology(IRI.create(base)).get();
    }
	
	@Test(expected = LoadedOntoNotSelected.class)
	public void testLoadedOntoNotSelectedException() throws LoadedOntoNotSelected {
		LoadMainOntology.selectOntology(null);
		OntologyUtils.createIndividual(IRI.create(base, individualAndrea));
	}
		
	@Test public void testCreateIndividual() {
		assertTrue("Create individual should return an 'OWLIndividual'", 
				OntologyUtils.createIndividual(ontology, IRI.create(base, individualAndrea)) instanceof OWLIndividual);
    }
	
	@Test public void testCreateObjectProperty() {
		assertTrue("Create object property should return an 'OWLObjectProperty'", 
				OntologyUtils.createObjectProperty(ontology, IRI.create(base, objectProperty)) instanceof OWLObjectProperty);
    }
	
	@Test public void testCreateObjectPropertyAssertionAxiom() {
		OWLIndividual subject = OntologyUtils.createIndividual(ontology, IRI.create(base, individualAndrea));
		OWLObjectProperty property = OntologyUtils.createObjectProperty(ontology, IRI.create(base, objectProperty));
		OWLIndividual object = OntologyUtils.createIndividual(ontology, IRI.create(base, individualRoberto));
				
		assertTrue("Create object property assertion axiom should return an 'OWLObjectPropertyAssertionAxiom'", 
				OntologyUtils.createObjectPropertyAssertionAxiom(ontology, subject, property, object) instanceof OWLObjectPropertyAssertionAxiom);
    }
	
	@Test public void testCreateClass() {
		assertTrue("Create class should return an 'OWLClass'", 
				OntologyUtils.createClass(ontology, IRI.create(base, classPerson)) instanceof OWLClass);
    }
	
	@Test public void testCreateClassAssertionAxiom() {
		OWLClass personClass = OntologyUtils.createClass(ontology, IRI.create(base, classPerson));
		OWLIndividual personInstance = OntologyUtils.createIndividual(ontology, IRI.create(base, individualAndrea));
				
		assertTrue("Create class assertion axiom should return an 'OWLClassAssertionAxiom'", 
				OntologyUtils.createClassAssertionAxiom(ontology, personClass, personInstance) instanceof OWLClassAssertionAxiom);
    }
	
	@Test public void testCreateDataProperty() {
		assertTrue("Create data property should return an 'OWLDataProperty'", 
				OntologyUtils.createDataProperty(ontology, IRI.create(base, dataProperty)) instanceof OWLDataProperty);
    }
}
