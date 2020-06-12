package Assignment.WebSemantico.utils.ontology;

import java.util.Optional;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import Assignment.WebSemantico.utils.logger.Logger;
import Assignment.WebSemantico.utils.logger.MessageType;

public class OWLOntologyWithPrints {
	private static String className = "OWLOntologyWithPrints";
	protected OWLOntology ontology;
	protected OWLOntologyManager manager;
		
	protected OWLOntologyWithPrints(OWLOntology ontology) {
		this.manager = ontology.getOWLOntologyManager();
		this.ontology = ontology;
	}

	public OWLOntology getOntology() {
		return this.ontology;
	}

	public OWLOntologyManager getManager() {
		return this.manager;
	}
		
	public long getNumberOfOntologiesImported() {
		return ontology.importsDeclarations().count();
	}
	
	public Optional<IRI> getOntologyIRI() {
		return ontology.getOntologyID().getOntologyIRI();
	}
	
	public String getOntologyIRIAsString() {
		return getOntologyIRI().isPresent() ? getOntologyIRI().get().getIRIString() : "No IRI, anonymous ontology!";
	}
	
	public void printOntologyInfo() {
		printAxiomCount();
		printClassesNumber();
		printIndividualsNumber();
		printObjectPropertyNumber();
		printDataPropertyNumber();
		printLogicalAxiomsNumber();
		printOntologyFormat();
	}
	
	public void printLogicalAxiomsNumber() {
		Logger.log(MessageType.INFO, className, "Logical axioms number: " + ontology.logicalAxioms().count());
	}
	
	public void printIndividualsNumber() {
		Logger.log(MessageType.INFO, className, "Individuals number: " + ontology.individualsInSignature().count());
	}
	
	public void printObjectPropertyNumber() {
		Logger.log(MessageType.INFO, className, "Object property number: " + ontology.objectPropertiesInSignature().count());
	}
	
	public void printDataPropertyNumber() {
		Logger.log(MessageType.INFO, className, "Data property number: " + ontology.dataPropertiesInSignature().count());
	}
	
	public void printClassesNumber() {
		Logger.log(MessageType.INFO, className, "Classes number: " + ontology.classesInSignature().count());
	}
	
	public void printAxiomCount() {
		Logger.log(MessageType.INFO, className, "Axioms: " + ontology.getAxiomCount());
	}
	
	public void printOntologyFormat() {
		Logger.log(MessageType.INFO, className, "Format:" + manager.getOntologyFormat(ontology));
	}
	
	public void printOntology() {
		Logger.log(MessageType.DEBUG, className, "Ontology:" + ontology.toString());
	}
	
	public void printOntologyImports() {
		Optional<String> imports = ontology.importsDeclarations()
		        					.map(importDeclaration -> importDeclaration.toString())
		        					.reduce((elem, res) -> res += elem + "\n");
		Logger.log(MessageType.DEBUG, className, (imports.isPresent()) ? "Ontologies imported: " + imports.get() : "Ontology doesn't import other ontologies!");
	}
	
	public void printWholeOntology() {
		try {
			this.manager.saveOntology(ontology, System.out);
		} catch (OWLOntologyStorageException e) {
			Logger.log(MessageType.ERROR, className, e.getMessage());
			if(!Logger.disableStackTrace) e.printStackTrace();
		}
	}
}
