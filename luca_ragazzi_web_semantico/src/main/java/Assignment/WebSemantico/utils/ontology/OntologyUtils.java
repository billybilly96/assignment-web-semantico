package Assignment.WebSemantico.utils.ontology;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;

import Assignment.WebSemantico.utils.logger.Logger;
import Assignment.WebSemantico.utils.logger.MessageType;
import Assignment.WebSemantico.utils.ontology.exception.LoadedOntoNotSelected;

public class OntologyUtils extends LoadMainOntology {
	private static String className = "OntologyUtils";
	
	public static <T> IRI toIRI(T elem) {
		return ((OWLEntity) elem).getIRI();
	}
	
	//##################CREATION##################
	////##Those methods can be used only after have called the selectOntology.
		
	public static OWLNamedIndividual createIndividual(IRI individualIRI) throws LoadedOntoNotSelected {
		return createIndividual(getLoadedOnto(), individualIRI);
	}
	
	public static OWLObjectProperty createObjectProperty(IRI objectPropertyIRI) throws LoadedOntoNotSelected {
		return createObjectProperty(getLoadedOnto(), objectPropertyIRI);
	}
	
	public static OWLSymmetricObjectPropertyAxiom createSymmetricObjectProperty(OWLObjectProperty symmetricObjectProperty) throws LoadedOntoNotSelected {
		return createSymmetricObjectProperty(getLoadedOnto(), symmetricObjectProperty);
	}
	
	public static OWLObjectPropertyAssertionAxiom createObjectPropertyAssertionAxiom(OWLIndividual subject, OWLObjectProperty property, OWLIndividual object) throws LoadedOntoNotSelected {
		return createObjectPropertyAssertionAxiom(getLoadedOnto(), subject, property, object);
	}
	
	public static OWLInverseObjectPropertiesAxiom createInverseObjectProperty(OWLObjectProperty forwardProperty, OWLObjectProperty inverseProperty) throws LoadedOntoNotSelected {
		return createInverseObjectProperty(getLoadedOnto(), forwardProperty, inverseProperty);
	}
	
	public static OWLClass createClass(IRI classIRI) throws LoadedOntoNotSelected {
		return createClass(getLoadedOnto(), classIRI);
	}
	
	public static OWLClassAssertionAxiom createClassAssertionAxiom(OWLClass owlClass, OWLIndividual individual) throws LoadedOntoNotSelected {
		return createClassAssertionAxiom(getLoadedOnto(), owlClass, individual);
	}
	
	public static OWLDeclarationAxiom createDeclarationAxiom(OWLClass owlClass) throws LoadedOntoNotSelected {
		return createDeclarationAxiom(getLoadedOnto(), owlClass);
	}
	
	public static OWLDeclarationAxiom createDeclarationAxiom(OWLNamedIndividual individual) throws LoadedOntoNotSelected {
		return createDeclarationAxiom(getLoadedOnto(), individual);
	}
	
	public static OWLDeclarationAxiom createIndividualAndHisDeclarationAxiom(IRI individualIRI) throws LoadedOntoNotSelected {
		return createIndividualAndHisDeclarationAxiom(getLoadedOnto(), individualIRI);
	}
	
	public static OWLDeclarationAxiom createDeclarationAxiom(OWLEntity declaredEntity) throws LoadedOntoNotSelected {
		return createDeclarationAxiom(getLoadedOnto(), declaredEntity);
	}
	
	public static OWLNamedIndividual createIndividualAndSetHisType(IRI individualIri, OWLClass individualType) throws LoadedOntoNotSelected {
		return createIndividualAndSetHisType(getLoadedOnto(), individualIri, individualType);
	}
	
	public static OWLDataProperty createDataProperty(IRI dataPropertyIRI) throws LoadedOntoNotSelected {
		return createDataProperty(getLoadedOnto(), dataPropertyIRI);
	}
	
	public static OWLDataPropertyAssertionAxiom createDataPropertyAxiom(OWLDataProperty dataProperty, OWLIndividual individual, String value) throws LoadedOntoNotSelected {
		return createDataPropertyAxiom(getLoadedOnto(), dataProperty, individual, value);
	}
	
	//##################CREATION##################	
	public static OWLNamedIndividual createIndividual(OWLOntologyWithTools ontology, IRI individualIRI) {
		Logger.log(MessageType.DEBUG, className, "Creating individual: " + individualIRI + " for ontology: " + ontology.getOntologyIRIAsString());
		return ontology.getDataFactory().getOWLNamedIndividual(individualIRI);
	}
	
	public static OWLObjectProperty createObjectProperty(OWLOntologyWithTools ontology, IRI objectPropertyIRI) {
		Logger.log(MessageType.DEBUG, className, "Creating object property: " + objectPropertyIRI + " for ontology: " + ontology.getOntologyIRIAsString());
		return ontology.getDataFactory().getOWLObjectProperty(objectPropertyIRI);
	}
	
	public static OWLSymmetricObjectPropertyAxiom createSymmetricObjectProperty(OWLOntologyWithTools ontology, OWLObjectProperty symmetricObjectProperty) {
		Logger.log(MessageType.DEBUG, className, "Creating symmetric object property: " + symmetricObjectProperty.getIRI() + " for ontology: " + ontology.getOntologyIRIAsString());
		return ontology.getDataFactory().getOWLSymmetricObjectPropertyAxiom(symmetricObjectProperty);
	}
	
	public static OWLObjectPropertyAssertionAxiom createObjectPropertyAssertionAxiom(OWLOntologyWithTools ontology, OWLIndividual subject, OWLObjectProperty property, OWLIndividual object) {
		Logger.log(MessageType.DEBUG, className, "Creating an object property assertion axiom for ontology: " + ontology.getOntologyIRIAsString());
		Logger.log(MessageType.DEBUG, className, "  - subject: " + toIRI(subject));
		Logger.log(MessageType.DEBUG, className, "  - property: " + toIRI(property));
		Logger.log(MessageType.DEBUG, className, "  - object: " + toIRI(object));
		return ontology.getDataFactory().getOWLObjectPropertyAssertionAxiom(property, subject, object);
	}
	
	public static OWLInverseObjectPropertiesAxiom createInverseObjectProperty(OWLOntologyWithTools ontology, OWLObjectProperty forwardProperty, OWLObjectProperty inverseProperty) {
		Logger.log(MessageType.DEBUG, className, "Creating inverse object property: " + 
															forwardProperty.getIRI() + " inverse of " + inverseProperty.getIRI()  + 
																	" for ontology: " + ontology.getOntologyIRIAsString());
		return ontology.getDataFactory().getOWLInverseObjectPropertiesAxiom(forwardProperty, inverseProperty);
	}
	
	public static OWLClass createClass(OWLOntologyWithTools ontology, IRI classIRI) {
		Logger.log(MessageType.DEBUG, className, "Creating a class: " + classIRI + " for ontology: " + ontology.getOntologyIRIAsString());
		return ontology.getDataFactory().getOWLClass(classIRI);
	}
	
	public static OWLClassAssertionAxiom createClassAssertionAxiom(OWLOntologyWithTools ontology, OWLClass owlClass, OWLIndividual individual) {
		Logger.log(MessageType.DEBUG, className, "Creating a class assertion axiom for ontology: " + ontology.getOntologyIRIAsString());
		Logger.log(MessageType.DEBUG, className, "  - individual: " + toIRI(individual));
		Logger.log(MessageType.DEBUG, className, "  - class: " + toIRI(owlClass));
		return ontology.getDataFactory().getOWLClassAssertionAxiom(owlClass, individual);
	}
	
	public static OWLDeclarationAxiom createDeclarationAxiom(OWLOntologyWithTools ontology, OWLClass owlClass) {
		return createDeclarationAxiom(ontology, (OWLEntity) owlClass);
	}
	
	public static OWLDeclarationAxiom createDeclarationAxiom(OWLOntologyWithTools ontology, OWLNamedIndividual individual) {
		return createDeclarationAxiom(ontology, (OWLEntity) individual);
	}
	
	public static OWLDeclarationAxiom createIndividualAndHisDeclarationAxiom(OWLOntologyWithTools ontology, IRI individualIRI) {
		return OntologyUtils.createDeclarationAxiom(ontology, createIndividual(ontology, individualIRI));
	}
	
	public static OWLDeclarationAxiom createDeclarationAxiom(OWLOntologyWithTools ontology, OWLEntity declaredEntity) {
		Logger.log(MessageType.DEBUG, className, "Creating a declaration axiom for ontology: " + ontology.getOntologyIRIAsString());
		Logger.log(MessageType.DEBUG, className, "  - declaredEntity: " + declaredEntity.getIRI());
		return ontology.getDataFactory().getOWLDeclarationAxiom(declaredEntity);
	}
	
	public static OWLNamedIndividual createIndividualAndSetHisType(OWLOntologyWithTools ontology, IRI individualIri, 
																						OWLClass individualType) {
		OWLNamedIndividual individual = OntologyUtils.createIndividual(ontology, individualIri);
		ontology.addAxiom(OntologyUtils.createClassAssertionAxiom(ontology, individualType, individual));
		return individual;
	}
	
	public static OWLDataProperty createDataProperty(OWLOntologyWithTools ontology, IRI dataPropertyIRI) {
		return ontology.getDataFactory().getOWLDataProperty(dataPropertyIRI);
	}
	
	public static OWLDataPropertyAssertionAxiom createDataPropertyAxiom(OWLOntologyWithTools ontology, OWLDataProperty dataProperty, OWLIndividual individual, String value) {
		return ontology.getDataFactory().getOWLDataPropertyAssertionAxiom(dataProperty, individual, value);
	}
}
