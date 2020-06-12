package Assignment.WebSemantico.cardata;

import Assignment.WebSemantico.utils.ontology.OntologyUtils;
import Assignment.WebSemantico.utils.ontology.exception.LoadedOntoNotSelected;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;

public class SpeedProfileDataUtils {
    private final static String carOntoIri = "http://www.toyota-ti.ac.jp/Lab/Denshi/COIN/Car";
    // Classes
    private final static IRI accelerationIRI = IRI.create(carOntoIri + "#Acceleration");
    private final static IRI constantSpeedIRI = IRI.create(carOntoIri + "#ConstantSpeed");
    private final static IRI decelerationIRI = IRI.create(carOntoIri + "#Deceleration");

    private final static SpeedProfileData speedProfileData = SpeedProfileData.getInstance();

    /**
     * Set the classes needed to monitor the vehicle speed.
     * @throws LoadedOntoNotSelected
     */
    public static void addSpeedProfileClasses() throws LoadedOntoNotSelected {
        // Driving directions and actions
        OWLClass acceleration = OntologyUtils.createClass(accelerationIRI);
        OWLClass constantSpeed = OntologyUtils.createClass(constantSpeedIRI);
        OWLClass deceleration = OntologyUtils.createClass(decelerationIRI);

        speedProfileData.setClassAcceleration(acceleration);
        speedProfileData.setClassConstantSpeed(constantSpeed);
        speedProfileData.setClassDeceleration(deceleration);
    }

}
