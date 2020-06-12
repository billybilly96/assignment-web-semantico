package Assignment.WebSemantico.cardata;

import org.semanticweb.owlapi.model.OWLClass;

public class SpeedProfileData {
    // Class
    private OWLClass acceleration;
    private OWLClass constantSpeed;
    private OWLClass deceleration;

    // Speed profile ontology
    private static SpeedProfileData speedProfileDataOnto;

    // Singleton
    public static SpeedProfileData getInstance() {
        if (speedProfileDataOnto == null) {
            speedProfileDataOnto = new SpeedProfileData();
        }
        return speedProfileDataOnto;
    }

    public OWLClass getClassAcceleration() {
        return acceleration;
    }

    public void setClassAcceleration(OWLClass acceleration) {
        this.acceleration = acceleration;
    }

    public OWLClass getClassConstantSpeed() {
        return constantSpeed;
    }

    public void setClassConstantSpeed(OWLClass constantSpeed) {
        this.constantSpeed = constantSpeed;
    }

    public OWLClass getClassDeceleration() {
        return deceleration;
    }

    public void setClassDeceleration(OWLClass deceleration) {
        this.deceleration = deceleration;
    }
}
