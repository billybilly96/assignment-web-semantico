package Assignment.WebSemantico.utils.ontology;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.search.Searcher;
import org.semanticweb.owlapi.util.OWLEntityRemover;

import Assignment.WebSemantico.utils.logger.Logger;
import Assignment.WebSemantico.utils.logger.MessageType;

public class OWLOntologyWithTools extends OntologySearchUtils {
	private static String className = "OWLOntologyWithTools";
	private OWLEntityRemover entityRemover;
	private OWLDataFactory dataFactory;
	
	public OWLOntologyWithTools(OWLOntology ontology) {
		super(ontology);
		this.entityRemover = new OWLEntityRemover(Collections.singleton(ontology));
		this.dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
	}
	
	public OWLDataFactory getDataFactory() {
		return this.dataFactory;
	}
	
	public OWLEntityRemover getEntityRemover() {
		return this.entityRemover;
	}
	
	public boolean removeElements() {
		Logger.log(MessageType.DEBUG, className, "Remove elements.");
		return manager.applyChanges(entityRemover.getChanges()).equals(ChangeApplied.SUCCESSFULLY);
	}
	
	public Optional<OWLNamedIndividual> getNamedIndividualByIRI(IRI individual) {
		return ontology.individualsInSignature().filter(indiv -> indiv.getIRI().equals(individual)).findFirst();
	}
	
	public boolean importAnOntology(IRI IriOntologyToImport) {
		OWLImportsDeclaration importDeclaration= dataFactory.getOWLImportsDeclaration(IriOntologyToImport);
		return manager.applyChange(new AddImport(ontology, importDeclaration)).equals(ChangeApplied.SUCCESSFULLY);
	}
			
	public boolean addAxiom(OWLAxiom axiomToAdd) {
		Logger.log(MessageType.DEBUG, className, "Added a new axiom: " + axiomToAdd.toString());
		return manager.applyChange(new AddAxiom(ontology, axiomToAdd)).equals(ChangeApplied.SUCCESSFULLY);
	}
	
	/**
	 * Delete an individual.
	 * @param individual OWLNamedIndividual
	 * @return true if can found the individual and if can delete it. 
	 */
	public boolean deleteIndividual(OWLNamedIndividual individual) {
		Logger.log(MessageType.DEBUG, className, "Deleting individual: " + OntologyUtils.toIRI(individual) + 
													" from ontology: " + getOntologyIRIAsString());
		return searchIndividualByIRI(individual)
				.map(indiv -> {
					indiv.accept(entityRemover);
					Logger.log(MessageType.DEBUG, className, "Starting to remove the founded individual");
					return this.removeElements();
				}).orElseGet(() -> {
					Logger.log(MessageType.ERROR, className, "Cannot remove the individual because it doesn't exists.");
					return false;
				});
	}
	
	/**
	 * Delete an individual.
	 * @param individual OWLNamedIndividual
	 * @return true if can delete it.
	 */
	public boolean deleteIndividualWithoutExistenceCheck(OWLNamedIndividual individual) {
		Logger.log(MessageType.DEBUG, className, "Deleting individual: " + OntologyUtils.toIRI(individual) + 
													" from ontology: " + getOntologyIRIAsString());
		individual.accept(this.entityRemover);
		return this.removeElements();
	}
	
	/**
	 * Delete all the object property that have the specified subject or object.
	 * @param subject Optional<OWLNamedIndividual> can be empty
	 * @param object Optional<OWLNamedIndividual> can be empty
	 * @return true if can found at least one object property and if can delete it.
	 */
	public boolean deleteObjectPropertyAxiomsBySubjectOrObject(Optional<OWLNamedIndividual> subject, Optional<OWLNamedIndividual> object) {
		return deleteObjectProperty(subject, object, filterByObjectOrSubject(subject, object));
	}
	
	/**
	 * Delete all the object property that have the specified subject and object.
	 * @param subject Optional<OWLNamedIndividual> cannot be empty
	 * @param object Optional<OWLNamedIndividual> cannot be empty
	 * @return true if can found at least one object property and if can delete it.
	 */
	public boolean deleteObjectPropertyAxiomsBySubjectAndObject(Optional<OWLNamedIndividual> subject, Optional<OWLNamedIndividual> object) {
		return deleteObjectProperty(subject, object, filterByObjectAndSubject(subject, object));
	}
	
	public boolean deleteAxiom(OWLAxiom axiom) {
		Logger.log(MessageType.DEBUG, className, "Deleting axiom from ontology: " + getOntologyIRIAsString());
		Logger.log(MessageType.DEBUG, className, " - axiom: " + axiom.toString());
		return ontology.remove(axiom) != null;
	}
	
	/**
	 * Retrieve objects from a collection of assertions and then delete all.
	 * @param property OWLObjectPropertyExpression
	 * @return boolean
	 */
	public boolean deleteAxiomsByProperty(OWLObjectPropertyExpression property) {
		Logger.log(MessageType.DEBUG, className, "Deleting axiom by property from ontology: " + getOntologyIRIAsString());
		Logger.log(MessageType.DEBUG, className, " - property: " + property.toString());
		List<OWLIndividual> axiomsToRemove = Searcher.values(ontology.axioms(AxiomType.OBJECT_PROPERTY_ASSERTION), property).collect(Collectors.toList());
		return axiomsToRemove.stream()
							  .map(indiv -> deleteIndividualWithoutExistenceCheck((OWLNamedIndividual) indiv))
							  .allMatch(deletionResult -> deletionResult == true);
	}
	
	private boolean deleteObjectProperty(Optional<OWLNamedIndividual> subject, Optional<OWLNamedIndividual> object, 
																Predicate<OWLObjectPropertyAssertionAxiom> predicate) {
		Logger.log(MessageType.DEBUG, className, "Deleting axioms from ontology: " + getOntologyIRIAsString());
		Logger.log(MessageType.DEBUG, className, " - subject: " + (subject.isPresent() ? OntologyUtils.toIRI(subject.get()) : "Not specified."));
		Logger.log(MessageType.DEBUG, className, " - object: " + (object.isPresent() ? OntologyUtils.toIRI(object.get()) : "Not specified."));
		List<OWLObjectPropertyAssertionAxiom> foundedAxioms = searchObjectPropertyBySubjectAndOrObject(subject, object, predicate);
		Logger.log(MessageType.ERROR, className, " - THE RESEARCH HAS RETURNED 0 AXIOMS!");
		return !foundedAxioms.isEmpty() && foundedAxioms.stream()
													   .map(axiom -> ontology.remove(axiom) != null)
													   .allMatch(res -> res == true);
	}
	
	public long getSwrlRuleNumber() {
		return ontology.axioms(AxiomType.SWRL_RULE).count();
	}
}
