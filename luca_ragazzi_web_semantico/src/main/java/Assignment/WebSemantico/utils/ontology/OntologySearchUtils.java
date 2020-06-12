package Assignment.WebSemantico.utils.ontology;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import Assignment.WebSemantico.utils.logger.Logger;
import Assignment.WebSemantico.utils.logger.MessageType;

public class OntologySearchUtils extends OWLOntologyWithPrints {

	private final String className = "OntologySearchUtils";
	
	protected OntologySearchUtils(OWLOntology ontology) {
		super(ontology);
	}
	
	protected Predicate<OWLObjectPropertyAssertionAxiom> filterByObjectOrSubject(Optional<OWLNamedIndividual> subject, Optional<OWLNamedIndividual> object) {
		return new Predicate<OWLObjectPropertyAssertionAxiom>() {
			@Override
			public boolean test(OWLObjectPropertyAssertionAxiom t) {
				if(subject.isPresent() && t.getSubject().equals(subject.get())) {
	        		return true;
	        	} else if (object.isPresent() && t.getObject().equals(object.get())) {
	        		return true;
	        	}
				return false;
			}
		};
	}
	
	protected Predicate<OWLObjectPropertyAssertionAxiom> filterByObjectAndSubject(Optional<OWLNamedIndividual> subject, Optional<OWLNamedIndividual> object) {
		return new Predicate<OWLObjectPropertyAssertionAxiom>() {
			@Override
			public boolean test(OWLObjectPropertyAssertionAxiom t) {
				if(subject.isPresent() && t.getSubject().equals(subject.get())) {
					if (object.isPresent() && t.getObject().equals(object.get())) {
	            		return true;
	            	}
            	} 
            	return false;
			}
		};
	}
	
	public Optional<OWLNamedIndividual> searchIndividualByIRI(OWLNamedIndividual individual) {
		Logger.log(MessageType.DEBUG, className, "Searching individual: " + OntologyUtils.toIRI(individual) + 
													" from ontology: " + getOntologyIRIAsString());
		return ontology.individualsInSignature()
				       .filter(ind -> OntologyUtils.toIRI(ind) == OntologyUtils.toIRI(individual))
					   .findFirst();
	}
	
	public List<OWLObjectPropertyAssertionAxiom> searchObjectPropertyBySubjectAndOrObject(Optional<OWLNamedIndividual> subject, 
			Optional<OWLNamedIndividual> object, Predicate<OWLObjectPropertyAssertionAxiom> predicate) {
		
		Logger.log(MessageType.DEBUG, className, "Searching object property axioms by subject AND/OR object from ontology: " + getOntologyIRIAsString());
		Logger.log(MessageType.DEBUG, className, " - subject: " + (subject.isPresent() ? OntologyUtils.toIRI(subject.get()) : "Not specified."));
		Logger.log(MessageType.DEBUG, className, " - object: " + (object.isPresent() ? OntologyUtils.toIRI(object.get()) : "Not specified."));
		
        return ontology.axioms(AxiomType.OBJECT_PROPERTY_ASSERTION) // Filter for OWLObjectPropertyAssertionAxiom
					   .map(OWLObjectPropertyAssertionAxiom.class::cast) // Cast to OWLObjectPropertyAssertionAxiom
					   .filter(predicate)
					   .collect(Collectors.toList());
	}
}
