package Assignment.WebSemantico.reasoner;

import java.util.Optional;

import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import Assignment.WebSemantico.cardata.MyCarData;
import Assignment.WebSemantico.cardata.SpecialVehicleData;
import Assignment.WebSemantico.pathdata.MyPathData;

public class ReasonerUtils {
	public static OWLReasoner createReasoner(OWLOntology ontology) {
		return new ReasonerFactory().createReasoner(ontology);
	}
	
	public static NodeSet<OWLNamedIndividual> getStartNode(OWLReasoner reasoner) {
		return reasoner.getInstances(MyPathData.getInstance().getStartLane());
	}
	
	public static NodeSet<OWLNamedIndividual> getEndNode(OWLReasoner reasoner) {
		return reasoner.getInstances(MyPathData.getInstance().getEndLane());
	}
	
	public static NodeSet<OWLNamedIndividual> getEgoVehiclePosition(OWLReasoner reasoner) {
		return getVehiclePosition(reasoner, MyCarData.getInstance().getEgoVehicle());
	}
	
	public static NodeSet<OWLNamedIndividual> getVehiclePosition(OWLReasoner reasoner, OWLNamedIndividual vehicle) {
		return reasoner.getObjectPropertyValues(vehicle, MyCarData.getInstance().getIsRunningOn());
	}

	public static NodeSet<OWLNamedIndividual> getTramVehiclePosition(OWLReasoner reasoner) {
		return reasoner.getObjectPropertyValues(SpecialVehicleData.getInstance().getTramVehicle(), SpecialVehicleData.getInstance().getIsRunningOn());
	}
	
	/**
	 * Checks if the vehicle is running on the specified class of road parts (i.e. OneWayLane, Intersection, ...) 
	 * @param reasoner OWLReasoner.
	 * @param vehicle OWLNamedIndividual.
	 * @param clazz road segment class.
	 * @return boolean
	 */
	public static boolean vehicleIsRunningOn(OWLReasoner reasoner, OWLNamedIndividual vehicle, OWLClass clazz) {
		Optional<OWLNamedIndividual> vehiclePos = getVehiclePosition(reasoner, vehicle).entities().findFirst();
		if (vehiclePos.isPresent()) {
			return getIndividualsByClass(reasoner, clazz).containsEntity(vehiclePos.get());
		}
		return false;
	}
	
	public static NodeSet<OWLNamedIndividual> getIndividualsByClass(OWLReasoner reasoner, OWLClass clazz) {
		return reasoner.getInstances(clazz);
	}	
	
	public static NodeSet<OWLNamedIndividual> getIndividualsByObjectProperties(OWLReasoner reasoner, OWLNamedIndividual subject, OWLObjectProperty property) {
		return reasoner.getObjectPropertyValues(subject, property);
	}
}
