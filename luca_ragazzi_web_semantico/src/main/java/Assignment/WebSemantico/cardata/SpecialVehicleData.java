package Assignment.WebSemantico.cardata;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class SpecialVehicleData {
    // Class
    private OWLClass classSpecialVehicle;
    // Individual
    private OWLNamedIndividual tramVehicle;
    // Object Property
    private OWLObjectProperty isRunningOn;
    // Data Property
    private OWLDataProperty carId;

    // Special vehicle ontology
    private static SpecialVehicleData specialVehicleDataOnto;

    // Singleton
    public static SpecialVehicleData getInstance() {
        if (specialVehicleDataOnto == null) {
            specialVehicleDataOnto = new SpecialVehicleData();
        }
        return specialVehicleDataOnto;
    }

    public OWLClass getClassSpecialVehicle() {
        return classSpecialVehicle;
    }

    public void setClassSpecialVehicle(OWLClass classSpecialVehicle) {
        this.classSpecialVehicle = classSpecialVehicle;
    }

    public OWLNamedIndividual getTramVehicle() {
        return tramVehicle;
    }

    public void setTramVehicle(OWLNamedIndividual tramVehicle) {
        this.tramVehicle = tramVehicle;
    }

    public OWLObjectProperty getIsRunningOn() {
        return isRunningOn;
    }

    public void setIsRunningOn(OWLObjectProperty isRunningOn) {
        this.isRunningOn = isRunningOn;
    }

    public OWLDataProperty getCarId() {
        return carId;
    }

    public void setCarId(OWLDataProperty carId) {
        this.carId = carId;
    }
}
