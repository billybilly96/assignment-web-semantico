package Assignment.WebSemantico.cardata;

import Assignment.WebSemantico.Main;
import Assignment.WebSemantico.utils.ontology.OWLOntologyWithTools;
import Assignment.WebSemantico.utils.ontology.OntologyUtils;
import Assignment.WebSemantico.utils.ontology.exception.LoadedOntoNotSelected;
import org.semanticweb.owlapi.model.*;

public class SpecialVehicleDataUtils {
    private final static String carOntoIri = "http://www.toyota-ti.ac.jp/Lab/Denshi/COIN/Car";
    // Class
    private final static IRI specialVehicleIRI = IRI.create(carOntoIri + "#SpecialVehicle");
    // Individual
    private final static IRI indivTramVehicleIRI = IRI.create(Main.myOntologyIRI + "#tramVehicle");
    // Object Property
    private final static IRI objectPropertyIsRunningOnIRI = IRI.create(carOntoIri + "#isRunningOn");
    // Data Property
    private final static IRI dataPropertyCarIdIRI = IRI.create(carOntoIri + "#carID");

    private final static SpecialVehicleData specialVehicleData = SpecialVehicleData.getInstance();

    /**
     * Creates the tram vehicle and assign its type (SpecialVehicle).
     * @throws LoadedOntoNotSelected
     */
    public static void addTramVehicle() throws LoadedOntoNotSelected {
        OWLClass classSpecialVehicle = OntologyUtils.createClass(specialVehicleIRI);
        specialVehicleData.setClassSpecialVehicle(classSpecialVehicle);
        specialVehicleData.setTramVehicle(OntologyUtils.createIndividualAndSetHisType(indivTramVehicleIRI, classSpecialVehicle));
    }

    /**
     * Adds to the tram vehicle the dataProperty CarID.
     * @param ontology OWLOntologyWithTools
     * @throws LoadedOntoNotSelected
     */
    public static void addDataPropertyCarIdToTramVehicle(OWLOntologyWithTools ontology) throws LoadedOntoNotSelected {
        OWLDataProperty dpCarID = OntologyUtils.createDataProperty(dataPropertyCarIdIRI);
        specialVehicleData.setCarId(dpCarID);
        ontology.addAxiom(OntologyUtils.createDataPropertyAxiom(dpCarID, specialVehicleData.getTramVehicle(), "1"));
    }

    /**
     * Set the initial position of the tram vehicle.
     * @throws LoadedOntoNotSelected
     */
    public static void addTramVehiclePosition() throws LoadedOntoNotSelected {
        OWLObjectProperty isRunningOn = OntologyUtils.createObjectProperty(objectPropertyIsRunningOnIRI);
        specialVehicleData.setIsRunningOn(isRunningOn);
    }


    /**
     * Set the initial position of the tram vehicle.
     * @param ontology OWLOntologyWithTools
     * @throws LoadedOntoNotSelected
     */
    public static void addTramVehiclePosition(OWLOntologyWithTools ontology, OWLNamedIndividual startNode) throws LoadedOntoNotSelected {
        OWLObjectProperty isRunningOn = OntologyUtils.createObjectProperty(objectPropertyIsRunningOnIRI);
        specialVehicleData.setIsRunningOn(isRunningOn);
        ontology.addAxiom(OntologyUtils.createObjectPropertyAssertionAxiom(specialVehicleData.getTramVehicle(), isRunningOn, startNode));
    }


}
