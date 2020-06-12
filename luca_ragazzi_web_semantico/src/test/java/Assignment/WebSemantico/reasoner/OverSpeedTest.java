package Assignment.WebSemantico.reasoner;

import Assignment.WebSemantico.Main;
import Assignment.WebSemantico.cardata.MyCarData;
import Assignment.WebSemantico.cardata.SpeedProfileData;
import Assignment.WebSemantico.cardata.SpeedProfileDataUtils;
import Assignment.WebSemantico.intersection.uncontrolled.rules.SWRLGenericRules;
import Assignment.WebSemantico.mapdata.MyMapData;
import Assignment.WebSemantico.mapdata.MyMapDataUtils;
import Assignment.WebSemantico.pathdata.MyPathData;
import Assignment.WebSemantico.pathdata.MyPathDataUtils;
import Assignment.WebSemantico.utils.ontology.LoadMainOntology;
import Assignment.WebSemantico.utils.ontology.OWLOntologyWithTools;
import Assignment.WebSemantico.utils.ontology.OntologyLoadUtils;
import Assignment.WebSemantico.utils.ontology.OntologyUtils;
import Assignment.WebSemantico.utils.ontology.exception.LoadedOntoNotSelected;
import Assignment.WebSemantico.utils.ontology.exception.OntologyCreationOrLoadingFailed;

import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.OWLEntityRemover;

import static org.junit.Assert.assertTrue;

public class OverSpeedTest {

    private final static String base = "http://www.ws.it/reasoner/uncontrolled/intersection/onto";
    private final static IRI swrlVarXIRI = IRI.create(base + "#X");
    private final static IRI swrlVarYIRI = IRI.create(base + "#Y");
    private final static IRI swrlVarLaneIRI = IRI.create(base + "#LANE");

    private final static MyCarData carData = MyCarData.getInstance();
    private final static MyPathData pathData = MyPathData.getInstance();
    private final static MyMapData mapData = MyMapData.getInstance();
    private final static SpeedProfileData speedProfileData = SpeedProfileData.getInstance();

    @Test
    public void testRespectRoadSpeedLimitRuleWithoutOverSpeedWarning() throws OntologyCreationOrLoadingFailed, LoadedOntoNotSelected {
        OWLOntologyWithTools o = OntologyLoadUtils.createEmptyOntology(IRI.create(base)).get();
        LoadMainOntology.selectOntology(o);
        o = Main.createMyPathDataOntology();
        Main.setEgoVehiclePathFromSouthToWest(o);
        Main.addGeneralSwrlRulesToOntology(o);

        SWRLGenericRules.respectRoadSpeedLimitRule(o, swrlVarXIRI, swrlVarYIRI, swrlVarLaneIRI, false);

        OWLReasoner reasoner = ReasonerUtils.createReasoner(o.getOntology());

        assertTrue("Ego vehicle should run on somewhere.",
                ReasonerUtils.getEgoVehiclePosition(reasoner).entities().count() == 1);

        assertTrue("Ego vehicle should be able to accelerate.",
                ReasonerUtils.getIndividualsByClass(reasoner, speedProfileData.getClassAcceleration()).containsEntity(carData.getEgoVehicle()));

        assertTrue("Ego vehicle should not be able to keep a constant speed.",
                ReasonerUtils.getIndividualsByClass(reasoner, speedProfileData.getClassConstantSpeed()).isEmpty());

        reasoner.dispose();
    }

    @Test
    public void testRespectRoadSpeedLimitRuleWithOverSpeedWarning() throws OntologyCreationOrLoadingFailed, LoadedOntoNotSelected {
        OWLOntologyWithTools o = OntologyLoadUtils.createEmptyOntology(IRI.create(base)).get();
        LoadMainOntology.selectOntology(o);
        o = Main.createMyPathDataOntology();
        Main.setEgoVehiclePathFromSouthToWest(o);
        Main.addGeneralSwrlRulesToOntology(o);

        MyPathDataUtils.addClassAndObjectPropertyForSwrlRules(o);
        SpeedProfileDataUtils.addSpeedProfileClasses();

        MyMapDataUtils.addRoadSpeedLimit();

        OWLEntityRemover remover = new OWLEntityRemover(o.getOntology());
        speedProfileData.getClassAcceleration().accept(remover);
        o.getManager().applyChanges(remover.getChanges());

        SWRLGenericRules.respectRoadSpeedLimitRule(o, swrlVarXIRI, swrlVarYIRI, swrlVarLaneIRI, true);

        OWLObjectPropertyAssertionAxiom egoVehicle_overSpeedWarningRespectTo_speedLimit = OntologyUtils.createObjectPropertyAssertionAxiom(carData.getEgoVehicle(),
                pathData.getOverSpeedWarningRespectTo(), mapData.getSpeedLimit());

        o.addAxiom(egoVehicle_overSpeedWarningRespectTo_speedLimit);

        OWLReasoner reasoner = ReasonerUtils.createReasoner(o.getOntology());

        assertTrue("Ego vehicle should run on somewhere.",
                ReasonerUtils.getEgoVehiclePosition(reasoner).entities().count() == 1);

        assertTrue("Ego vehicle should receive an overspeed warning.",
                reasoner.getObjectPropertyValues(carData.getEgoVehicle(), pathData.getOverSpeedWarningRespectTo()).containsEntity(mapData.getSpeedLimit()));

        assertTrue("Ego vehicle should keep a constant speed.",
                ReasonerUtils.getIndividualsByClass(reasoner, speedProfileData.getClassConstantSpeed()).containsEntity(carData.getEgoVehicle()));

        assertTrue("Ego vehicle should not be able to accelerate.",
                ReasonerUtils.getIndividualsByClass(reasoner, speedProfileData.getClassAcceleration()).isEmpty());

        reasoner.dispose();
    }

}
