package Assignment.WebSemantico.cardata;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class MyCarData {
	private OWLClass classMyCar;
	private OWLNamedIndividual egoVehicle;
	private OWLObjectProperty isRunningOn;
	private OWLDataProperty carId;
	
	private static MyCarData myCarDataOnto;
	
	private MyCarData() {}
	
	public static MyCarData getInstance() {
		if (myCarDataOnto == null) {
			myCarDataOnto = new MyCarData();
		}
		return myCarDataOnto;
	}
	
	public OWLObjectProperty getIsRunningOn() {
		return isRunningOn;
	}

	public void setIsRunningOn(OWLObjectProperty isRunningOn) {
		this.isRunningOn = isRunningOn;
	}

	public OWLNamedIndividual getEgoVehicle() {
		return egoVehicle;
	}

	public void setEgoVehicle(OWLNamedIndividual egoVehicle) {
		this.egoVehicle = egoVehicle;
	}

	public OWLClass getClassMyCar() {
		return classMyCar;
	}

	public void setClassMyCar(OWLClass classMyCar) {
		this.classMyCar = classMyCar;
	}

	public OWLDataProperty getCarId() {
		return carId;
	}

	public void setCarId(OWLDataProperty carId) {
		this.carId = carId;
	}

	public static MyCarData getMyCarDataOnto() {
		return myCarDataOnto;
	}

	public static void setMyCarDataOnto(MyCarData myCarDataOnto) {
		MyCarData.myCarDataOnto = myCarDataOnto;
	}
}
