package Assignment.WebSemantico.ontology.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.search.Searcher;

import Assignment.WebSemantico.utils.ontology.LoadMainOntology;
import Assignment.WebSemantico.utils.ontology.OWLOntologyWithTools;
import Assignment.WebSemantico.utils.ontology.OntologyLoadUtils;
import Assignment.WebSemantico.utils.ontology.OntologyUtils;
import Assignment.WebSemantico.utils.ontology.exception.LoadedOntoNotSelected;

public class OWLOntologyWithToolsTest {
	private final static String base = "http://www.ws.it/ontologies/individualsexample";
	//Individuals
	private final String individualAndrea = "#Andrea";
	private final String individualRoberto = "#Roberto";
	private final String individualMarco = "#Marco";
	//Object properties
	private final String objectProperty = "#hasFather";
	private final String dataProperty = "#hasMoney";
	private final String inverseObjectProperty = "#hasChildren";
	private final String symmetricObjectProperty = "#isFriendOf";
	//Classes
	private final String classPerson = "#Person";
	
	private static OWLOntologyWithTools ontology;
	private OWLClass personClass;
	private OWLObjectProperty hasFatherProperty;
	
	@Before public void loadTestOntology() throws LoadedOntoNotSelected {
		ontology = OntologyLoadUtils.createEmptyOntology(IRI.create(base)).get();
		LoadMainOntology.selectOntology(ontology);
		personClass = OntologyUtils.createClass(IRI.create(base, classPerson));
		hasFatherProperty = OntologyUtils.createObjectProperty(IRI.create(base, objectProperty));
    }
	/**
	 * DON'T CHANGE
	 * Inserts:
	 * 	- 2 named individuals (andreaSubject, robertoObject)
	 *  - 1 class (personClass)
	 *  - 1 Object property (hasFatherProperty)
	 *  - 3 axioms (andreaIsPerson, robertoIsPerson, andreaHasFatherRoberto)
	 * @return number of axioms.
	 * @throws LoadedOntoNotSelected 
	 */
	private long fillOntologyWithData() throws LoadedOntoNotSelected {
		// Individuals
		OWLNamedIndividual andreaSubject = OntologyUtils.createIndividual(IRI.create(base, individualAndrea));
		OWLNamedIndividual robertoObject = OntologyUtils.createIndividual(IRI.create(base, individualRoberto));
		
		//Axioms
		OWLObjectPropertyAssertionAxiom andreaHasFatherRoberto = OntologyUtils.createObjectPropertyAssertionAxiom(andreaSubject, hasFatherProperty, robertoObject);
		OWLClassAssertionAxiom andreaIsPerson = OntologyUtils.createClassAssertionAxiom(personClass, andreaSubject);
		OWLClassAssertionAxiom robertoIsPerson = OntologyUtils.createClassAssertionAxiom(personClass, robertoObject);
		
		// Insert
		ontology.addAxiom(andreaIsPerson);
		ontology.addAxiom(robertoIsPerson);
		ontology.addAxiom(andreaHasFatherRoberto);
		
		return ontology.getOntology().axioms().count();
	}
	
	@Test public void testAddAxiom() throws LoadedOntoNotSelected {
		OWLIndividual subject = OntologyUtils.createIndividual(IRI.create(base, individualAndrea));
		OWLObjectProperty property = OntologyUtils.createObjectProperty(IRI.create(base, objectProperty));
		OWLIndividual object = OntologyUtils.createIndividual(IRI.create(base, individualRoberto));
		OWLObjectPropertyAssertionAxiom opAxiom = OntologyUtils.createObjectPropertyAssertionAxiom(subject, property, object);
		assertTrue("Add axiom should return 'true'", ontology.addAxiom(opAxiom));
		assertTrue("Get axioms number should return '1'", ontology.getOntology().axioms().count() == 1);
    }
	
	@Test public void testAddInverseObjectPropertyAxiom() throws LoadedOntoNotSelected {
		OWLObjectProperty hasChildrenProperty = OntologyUtils.createObjectProperty(IRI.create(base, inverseObjectProperty));
		OWLInverseObjectPropertiesAxiom inverseObjectProp = OntologyUtils.createInverseObjectProperty(hasFatherProperty, hasChildrenProperty);
		assertTrue("Add inverse axiom should return 'true'", ontology.addAxiom(inverseObjectProp));
		assertTrue("Inverse property of hasFather should be 'hasChildren'", hasChildrenProperty.equals(inverseObjectProp.getSecondProperty().getNamedProperty()));	
		assertTrue("Inverse property of hasChildren should be 'hasFather'", hasFatherProperty.equals(inverseObjectProp.getFirstProperty().getNamedProperty()));	
    }
	
	@Test public void testAddSymmetricObjectPropertyAxiom() throws LoadedOntoNotSelected {
		OWLObjectProperty isFriendOf = OntologyUtils.createObjectProperty(IRI.create(base, symmetricObjectProperty));
		OWLSymmetricObjectPropertyAxiom symmetricObjectProp = OntologyUtils.createSymmetricObjectProperty(isFriendOf);
		assertTrue("Add symmetric axiom should return 'true'", ontology.addAxiom(symmetricObjectProp));
    }
	
	@Test public void testAddDataPropertyAxiom() throws LoadedOntoNotSelected {
		OWLDataProperty isFriendOf = OntologyUtils.createDataProperty(IRI.create(base, dataProperty));
		OWLNamedIndividual individual = OntologyUtils.createIndividual(IRI.create(base, individualAndrea));
		assertTrue("Add symmetric axiom should return 'true'", ontology.addAxiom(OntologyUtils.createDataPropertyAxiom(isFriendOf, individual, "100")));
    }
	
	@Test public void testDeleteAxiom() throws LoadedOntoNotSelected {
		// Add axiom to ontology
		OWLNamedIndividual subject = OntologyUtils.createIndividual(IRI.create(base, individualAndrea));
		OWLObjectProperty property = OntologyUtils.createObjectProperty(IRI.create(base, objectProperty));
		OWLNamedIndividual object = OntologyUtils.createIndividual(IRI.create(base, individualRoberto));
		OWLObjectPropertyAssertionAxiom opAxiom = OntologyUtils.createObjectPropertyAssertionAxiom(subject, property, object);
		OWLClassAssertionAxiom objectIsPerson = OntologyUtils.createClassAssertionAxiom(personClass, object);
		
		assertTrue("Add axiom should return 'true'", ontology.addAxiom(opAxiom));
		assertTrue("Get axioms number should return '1'", ontology.getOntology().axioms().count() == 1);
		assertTrue("Add axiom should return 'true'", ontology.addAxiom(objectIsPerson));
		assertTrue("Get axioms number should return '2'", ontology.getOntology().axioms().count() == 2);
		// Delete the previous axiom
		assertTrue("Delete axiom should return 'true'", ontology.deleteAxiom(opAxiom));
		assertTrue("Get axioms number should return '1'", ontology.getOntology().axioms().count() == 1);
		assertTrue("Delete axiom should return 'true'", ontology.deleteAxiom(objectIsPerson));
		assertTrue("Get axioms number should return '0'", ontology.getOntology().axioms().count() == 0);
    }
	
	@Test public void testDeleteObjectPropertyAxiomBySubject() throws LoadedOntoNotSelected {
		long initAxiomsNumber = this.fillOntologyWithData();
		OWLNamedIndividual subject = OntologyUtils.createIndividual(IRI.create(base, individualAndrea));
		// Delete by subject
		assertTrue("Delete the axiom by subject should return 'true'", 
				ontology.deleteObjectPropertyAxiomsBySubjectOrObject(Optional.ofNullable(subject), Optional.empty()));
		assertTrue("Get axioms number should return " + initAxiomsNumber--, ontology.getOntology().axioms().count() == initAxiomsNumber);
	}
	
	@Test public void testDeleteObjectPropertyAxiomByObject() throws LoadedOntoNotSelected {
		long initAxiomsNumber = this.fillOntologyWithData();
		OWLNamedIndividual object = OntologyUtils.createIndividual(IRI.create(base, individualRoberto));
		OWLNamedIndividual inexistentIndividual = OntologyUtils.createIndividual(IRI.create(base, "#inexistentIndiv"));
		assertFalse("Delete the axiom by inexistent object should return 'false'", 
				ontology.deleteObjectPropertyAxiomsBySubjectOrObject(Optional.empty(), Optional.ofNullable(inexistentIndividual)));
		assertFalse("Delete the axiom by inexistent subject should return 'false'", 
				ontology.deleteObjectPropertyAxiomsBySubjectOrObject(Optional.ofNullable(inexistentIndividual), Optional.empty()));
		// Delete by object
		assertTrue("Delete the axiom by object should return 'true'", 
				ontology.deleteObjectPropertyAxiomsBySubjectOrObject(Optional.empty(), Optional.ofNullable(object)));
		assertTrue("Get axioms number should return " + initAxiomsNumber--, ontology.getOntology().axioms().count() == initAxiomsNumber);
	}
	
	@Test public void testDeleteObjectPropertyAxiomByObjectAndSubject() throws LoadedOntoNotSelected {
		long initAxiomsNumber = this.fillOntologyWithData();
		OWLNamedIndividual subject = OntologyUtils.createIndividual(IRI.create(base, individualAndrea));
		OWLNamedIndividual object = OntologyUtils.createIndividual(IRI.create(base, individualRoberto));
		OWLNamedIndividual inexistentIndividual = OntologyUtils.createIndividual(IRI.create(base, "#inexistentIndiv"));
		assertFalse("Delete without specify subject should return 'false'", 
				ontology.deleteObjectPropertyAxiomsBySubjectAndObject(Optional.empty(), Optional.ofNullable(object)));
		assertFalse("Delete without specify object should return 'false'", 
				ontology.deleteObjectPropertyAxiomsBySubjectAndObject(Optional.ofNullable(subject), Optional.empty()));
		assertFalse("Delete the axiom by inexistent subject should return 'false'", 
				ontology.deleteObjectPropertyAxiomsBySubjectAndObject(Optional.ofNullable(subject), Optional.ofNullable(inexistentIndividual)));
		assertFalse("Delete the axiom by inexistent object should return 'false'", 
				ontology.deleteObjectPropertyAxiomsBySubjectAndObject(Optional.ofNullable(inexistentIndividual), Optional.ofNullable(object)));
		// Delete by object
		assertTrue("Delete the axiom by object should return 'true'", 
				ontology.deleteObjectPropertyAxiomsBySubjectAndObject(Optional.ofNullable(subject), Optional.ofNullable(object)));
		assertTrue("Get axioms number should return " + initAxiomsNumber--, ontology.getOntology().axioms().count() == initAxiomsNumber);
	}
	
	@Test public void testDeleteAxiomByProperty() throws LoadedOntoNotSelected {
		long initAxiomsNumber = this.fillOntologyWithData();
		OWLNamedIndividual marco = OntologyUtils.createIndividual(IRI.create(base, individualMarco));
		// Retrieve individual from ontology
		Optional<OWLNamedIndividual> andreaIndividual = ontology.getNamedIndividualByIRI(IRI.create(base, individualAndrea));
		assertTrue("Search Andrea into ontology should return 'true'", andreaIndividual.isPresent());
		
		OWLObjectPropertyAssertionAxiom marcoHasFatherAndrea = OntologyUtils.createObjectPropertyAssertionAxiom(marco, hasFatherProperty, andreaIndividual.get());
		OWLClassAssertionAxiom marcoIsPerson = OntologyUtils.createClassAssertionAxiom(personClass, marco);
		
		assertTrue("Add third axiom should return 'true'", ontology.addAxiom(marcoHasFatherAndrea));
		assertTrue("Get axioms number should return "+ initAxiomsNumber++, ontology.getOntology().axioms().count() == initAxiomsNumber);
		assertTrue("Add third axiom should return 'true'", ontology.addAxiom(marcoIsPerson));
		assertTrue("Get axioms number should return "+ initAxiomsNumber++, ontology.getOntology().axioms().count() == initAxiomsNumber);
				
		long numberOfObjectsToRemove = Searcher.values(ontology.getOntology().axioms(AxiomType.OBJECT_PROPERTY_ASSERTION), hasFatherProperty).count(); 
		long totalAxiomRemoved = numberOfObjectsToRemove * 2; 
		
		System.out.println(numberOfObjectsToRemove);
		
		// Delete by object
		assertTrue("Delete the axiom by property should return 'true'", 
				ontology.deleteAxiomsByProperty(hasFatherProperty));
		
		// Remain only the assertion: Marco is a Persona
		assertTrue("Get axioms number should return "+ (initAxiomsNumber-=totalAxiomRemoved), ontology.getOntology().axioms().count() == initAxiomsNumber);
	}
	
	@Test public void testDeleteIndividual() throws LoadedOntoNotSelected {
		// Add individual declaration into the ontology
		OWLNamedIndividual individual = OntologyUtils.createIndividual(IRI.create(base, individualAndrea));
		OWLDeclarationAxiom declarationAxiom = OntologyUtils.createDeclarationAxiom(individual);
		ontology.addAxiom(declarationAxiom);
		
		assertTrue("Get individuals number before deletion should return '1'", 
				ontology.getOntology().individualsInSignature().count() == 1);
		// Delete individual
		ontology.deleteIndividual(individual);
		assertTrue("Get individuals number after deletion should return '0'", 
				ontology.getOntology().individualsInSignature().count() == 0);
	}
	
	@Test public void testDeleteIndividualWithoutExistenceCheck() throws LoadedOntoNotSelected {
		// Add individual declaration into the ontology
		OWLNamedIndividual individual = OntologyUtils.createIndividual(IRI.create(base, individualAndrea));
		OWLDeclarationAxiom declarationAxiom = OntologyUtils.createDeclarationAxiom(individual);
		ontology.addAxiom(declarationAxiom);
		
		assertTrue("Get individuals number before deletion should return '1'", 
				ontology.getOntology().individualsInSignature().count() == 1);
		// Delete individual
		ontology.deleteIndividualWithoutExistenceCheck(individual);
		assertTrue("Get individuals number after deletion should return '0'", 
				ontology.getOntology().individualsInSignature().count() == 0);
	}
}
