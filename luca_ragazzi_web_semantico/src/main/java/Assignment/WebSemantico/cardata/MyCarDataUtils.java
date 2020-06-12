package Assignment.WebSemantico.cardata;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import Assignment.WebSemantico.Main;
import Assignment.WebSemantico.utils.ontology.OWLOntologyWithTools;
import Assignment.WebSemantico.utils.ontology.OntologyUtils;
import Assignment.WebSemantico.utils.ontology.exception.LoadedOntoNotSelected;

public class MyCarDataUtils {
	private final static IRI indivEgoVehicleIRI = IRI.create(Main.myOntologyIRI + "#egoVehicle");
		
	private final static String carOntoIri = "http://www.toyota-ti.ac.jp/Lab/Denshi/COIN/Car";
	// Class
	private final static IRI myCarIRI = IRI.create(carOntoIri + "#MyCar");
	// Data Property
	private final static IRI dataPropertyCarIdIRI = IRI.create(carOntoIri + "#carID");
	// Object Property
	private final static IRI objectPropertyIsRunningOnIRI = IRI.create(carOntoIri + "#isRunningOn");
	
	private final static MyCarData carData = MyCarData.getInstance();

	/**
	 * Creates the ego vehicle and assign its type (MyCar).
	 * @throws LoadedOntoNotSelected 
	 */
	public static void addEgoVehicle() throws LoadedOntoNotSelected {
		OWLClass classMyCar = OntologyUtils.createClass(myCarIRI);
		carData.setClassMyCar(classMyCar);
		carData.setEgoVehicle(OntologyUtils.createIndividualAndSetHisType(indivEgoVehicleIRI, classMyCar));
	}
	
	/**
	 * Adds to the ego vehicle the dataProperty CarID.
	 * @param ontology OWLOntologyWithTools
	 * @throws LoadedOntoNotSelected 
	 */
	public static void addDataPropertyCarIdToEgoVehicle(OWLOntologyWithTools ontology) throws LoadedOntoNotSelected {
		OWLDataProperty dpCarID = OntologyUtils.createDataProperty(dataPropertyCarIdIRI);
		carData.setCarId(dpCarID);
		ontology.addAxiom(OntologyUtils.createDataPropertyAxiom(dpCarID, carData.getEgoVehicle(), "0"));
	}
	
	/**
	 * Set the initial position of the ego vehicle.
	 * @param ontology OWLOntologyWithTools
	 * @throws LoadedOntoNotSelected 
	 */
	public static void addEgoVehiclePosition(OWLOntologyWithTools ontology, OWLNamedIndividual startNode) throws LoadedOntoNotSelected {
		OWLObjectProperty isRunningOn = OntologyUtils.createObjectProperty(objectPropertyIsRunningOnIRI);
		carData.setIsRunningOn(isRunningOn);
		ontology.addAxiom(OntologyUtils.createObjectPropertyAssertionAxiom(carData.getEgoVehicle(), isRunningOn, startNode));	
	}
}
