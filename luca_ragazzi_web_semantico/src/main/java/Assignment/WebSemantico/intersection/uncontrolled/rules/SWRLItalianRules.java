package Assignment.WebSemantico.intersection.uncontrolled.rules;

import java.util.HashSet;
import java.util.Set;

import Assignment.WebSemantico.cardata.SpecialVehicleData;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLClassAtom;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;

import Assignment.WebSemantico.cardata.MyCarData;
import Assignment.WebSemantico.mapdata.MyMapData;
import Assignment.WebSemantico.pathdata.MyPathData;
import Assignment.WebSemantico.utils.ontology.OWLOntologyWithTools;
import Assignment.WebSemantico.utils.ontology.SWRLUtils;
import Assignment.WebSemantico.utils.ontology.exception.LoadedOntoNotSelected;

public class SWRLItalianRules {
	private static final MyPathData pathData = MyPathData.getInstance();
	private static final MyMapData mapData = MyMapData.getInstance();
	private static final MyCarData carData = MyCarData.getInstance();
	private static final SpecialVehicleData specialVehicleData = SpecialVehicleData.getInstance();
	
	public static void giveWayToVehicleThatIsRunningOnIntersection(OWLOntologyWithTools ontology, IRI firstVarIRI, IRI secondVarIRI, IRI varIntersectionIRI, IRI varLaneIRI) throws LoadedOntoNotSelected {
		SWRLVariable varX = SWRLUtils.createSwrlVariable(firstVarIRI);
		SWRLVariable varY = SWRLUtils.createSwrlVariable(secondVarIRI);
		SWRLVariable varInt = SWRLUtils.createSwrlVariable(varIntersectionIRI);
		SWRLVariable varLane = SWRLUtils.createSwrlVariable(varLaneIRI);
		
		SWRLClassAtom clsAtom_CollisionWarning_X = SWRLUtils.createSwrlClassAtom(pathData.getCollisionWarning(), varX);
		SWRLClassAtom clsAtom_CollisionWarning_Y = SWRLUtils.createSwrlClassAtom(pathData.getCollisionWarning(), varY);
		SWRLClassAtom clsAtom_Intersection_Int = SWRLUtils.createSwrlClassAtom(mapData.getClassIntersection(), varInt);
		SWRLClassAtom clsAtom_OneWayLane_Lane = SWRLUtils.createSwrlClassAtom(mapData.getClassOneWayLane(), varLane);
		
		SWRLObjectPropertyAtom propAtom_X_isRunningOn_Lane = SWRLUtils.createSWRLObjectPropertyAtom(carData.getIsRunningOn(), varX, varLane); 
		SWRLObjectPropertyAtom propAtom_Y_isRunningOn_Intersection = SWRLUtils.createSWRLObjectPropertyAtom(carData.getIsRunningOn(), varY, varInt); 
				
		SWRLClassAtom clsAtom_Stop_X = SWRLUtils.createSwrlClassAtom(pathData.getStop(), varX);
		SWRLObjectPropertyAtom propAtom_X_giveWay_Y = SWRLUtils.createSWRLObjectPropertyAtom(pathData.getGiveWay(), varX, varY); 
		
		// CollisionWarning(?X) ^ CollisionWarning(?Y) ^ isRunningOn(?Y, ?INT) ^ Intersection(?INT) ^ isRunningOn(?X, ?LANE) ^ OneWayLane(?LANE)
		Set<SWRLAtom> body = new HashSet<>();
		body.add(clsAtom_CollisionWarning_X);
		body.add(clsAtom_CollisionWarning_Y);
		body.add(clsAtom_Intersection_Int);
		body.add(clsAtom_OneWayLane_Lane);
		body.add(propAtom_X_isRunningOn_Lane);
		body.add(propAtom_Y_isRunningOn_Intersection);
		// Stop(?X) ^ giveWay(?X, ?Y) 
		Set<SWRLAtom> head = new HashSet<>();
		head.add(clsAtom_Stop_X);
		head.add(propAtom_X_giveWay_Y);
		// body -> head
	    SWRLRule rule = SWRLUtils.createAnonymousSwrlRule(body,	head);
	    ontology.addAxiom(rule);
	}
	
	public static void rightOfWayRule(OWLOntologyWithTools ontology, IRI firstVarIRI, IRI secondVarIRI, IRI firstVarLaneIRI, IRI secondVarLaneIRI, IRI roadSegment1, IRI roadSegment2) throws LoadedOntoNotSelected {
		SWRLVariable varX = SWRLUtils.createSwrlVariable(firstVarIRI);
		SWRLVariable varY = SWRLUtils.createSwrlVariable(secondVarIRI);
		SWRLVariable varLane1 = SWRLUtils.createSwrlVariable(firstVarLaneIRI);
		SWRLVariable varLane2 = SWRLUtils.createSwrlVariable(secondVarLaneIRI);
		SWRLVariable RS1 = SWRLUtils.createSwrlVariable(roadSegment1);
		SWRLVariable RS2 = SWRLUtils.createSwrlVariable(roadSegment2);

		SWRLClassAtom clsAtom_CollisionWarning_X = SWRLUtils.createSwrlClassAtom(pathData.getCollisionWarning(), varX);
		SWRLClassAtom clsAtom_CollisionWarning_Y = SWRLUtils.createSwrlClassAtom(pathData.getCollisionWarning(), varY);
		SWRLClassAtom clsAtom_OneWayLane_Lane1 = SWRLUtils.createSwrlClassAtom(mapData.getClassOneWayLane(), varLane1);
		SWRLClassAtom clsAtom_OneWayLane_Lane2 = SWRLUtils.createSwrlClassAtom(mapData.getClassOneWayLane(), varLane2);
		
		SWRLObjectPropertyAtom propAtom_X_isRunningOn_Lane1 = SWRLUtils.createSWRLObjectPropertyAtom(carData.getIsRunningOn(), varX, varLane1); 
		SWRLObjectPropertyAtom propAtom_Y_isRunningOn_Lane2 = SWRLUtils.createSWRLObjectPropertyAtom(carData.getIsRunningOn(), varY, varLane2); 
		SWRLObjectPropertyAtom propAtom_Lane1_isLaneOf_RS1 = SWRLUtils.createSWRLObjectPropertyAtom(mapData.getIsLaneOf(), varLane1, RS1); 
		SWRLObjectPropertyAtom propAtom_Lane2_isLaneOf_RS2 = SWRLUtils.createSWRLObjectPropertyAtom(mapData.getIsLaneOf(), varLane2, RS2); 
		SWRLObjectPropertyAtom propAtom_RS1_turnRightTo_RS2 = SWRLUtils.createSWRLObjectPropertyAtom(mapData.getTurnRightTo(), RS1, RS2); 
				
		SWRLClassAtom clsAtom_Stop_X = SWRLUtils.createSwrlClassAtom(pathData.getStop(), varX);
		SWRLObjectPropertyAtom propAtom_X_giveWay_Y = SWRLUtils.createSWRLObjectPropertyAtom(pathData.getGiveWay(), varX, varY); 
		
		// CollisionWarning(?X) ^ CollisionWarning(?Y) ^ isRunningOn(?X, ?LANE1) ^ 
		//  OneWayLane(?LANE1) ^ isRunningOn(?Y, ?LANE2) ^ OneWayLane(?LANE2) ^ 
		//   isLaneOf(?LANE1, ?RS1) ^ isLaneOf(?LANE2, ?RS2) ^ turnRightTo(?RS1, ?RS2)
		Set<SWRLAtom> body = new HashSet<>();
		body.add(clsAtom_CollisionWarning_X);
		body.add(clsAtom_CollisionWarning_Y);
		body.add(clsAtom_OneWayLane_Lane1);
		body.add(clsAtom_OneWayLane_Lane2);
		body.add(propAtom_X_isRunningOn_Lane1);
		body.add(propAtom_Y_isRunningOn_Lane2);
		body.add(propAtom_Lane1_isLaneOf_RS1);
		body.add(propAtom_Lane2_isLaneOf_RS2);
		body.add(propAtom_RS1_turnRightTo_RS2);
		// Stop(?X) ^ giveWay(?X, ?Y) 
		Set<SWRLAtom> head = new HashSet<>();
		head.add(clsAtom_Stop_X);
		head.add(propAtom_X_giveWay_Y);
		// body -> head
	    SWRLRule rule = SWRLUtils.createAnonymousSwrlRule(body,	head);
	    ontology.addAxiom(rule);
	}

	public static void giveWaySpecialVehicles(OWLOntologyWithTools ontology, IRI firstVarIRI, IRI secondVarIRI, IRI firstVarLaneIRI, IRI secondVarLaneIRI, IRI roadSegment1, IRI roadSegment2) throws LoadedOntoNotSelected {
		SWRLVariable varX = SWRLUtils.createSwrlVariable(firstVarIRI);
		SWRLVariable varY = SWRLUtils.createSwrlVariable(secondVarIRI);
		SWRLVariable varLane1 = SWRLUtils.createSwrlVariable(firstVarLaneIRI);
		SWRLVariable varLane2 = SWRLUtils.createSwrlVariable(secondVarLaneIRI);
		SWRLVariable RS1 = SWRLUtils.createSwrlVariable(roadSegment1);
		SWRLVariable RS2 = SWRLUtils.createSwrlVariable(roadSegment2);

		// Y is a SpecialVehicle (e.g. tram)
		SWRLClassAtom clsAtom_SpecialVehicle_Y = SWRLUtils.createSwrlClassAtom(specialVehicleData.getClassSpecialVehicle(), varY);
		SWRLClassAtom clsAtom_CollisionWarning_X = SWRLUtils.createSwrlClassAtom(pathData.getCollisionWarning(), varX);
		SWRLClassAtom clsAtom_CollisionWarning_Y = SWRLUtils.createSwrlClassAtom(pathData.getCollisionWarning(), varY);

		SWRLClassAtom clsAtom_OneWayLane_Lane1 = SWRLUtils.createSwrlClassAtom(mapData.getClassOneWayLane(), varLane1);
		SWRLClassAtom clsAtom_OneWayLane_Lane2 = SWRLUtils.createSwrlClassAtom(mapData.getClassOneWayLane(), varLane2);

		SWRLObjectPropertyAtom propAtom_X_isRunningOn_Lane1 = SWRLUtils.createSWRLObjectPropertyAtom(carData.getIsRunningOn(), varX, varLane1);
		SWRLObjectPropertyAtom propAtom_Y_isRunningOn_Lane2 = SWRLUtils.createSWRLObjectPropertyAtom(specialVehicleData.getIsRunningOn(), varY, varLane2);
		SWRLObjectPropertyAtom propAtom_Lane1_isLaneOf_RS1 = SWRLUtils.createSWRLObjectPropertyAtom(mapData.getIsLaneOf(), varLane1, RS1);
		SWRLObjectPropertyAtom propAtom_Lane2_isLaneOf_RS2 = SWRLUtils.createSWRLObjectPropertyAtom(mapData.getIsLaneOf(), varLane2, RS2);

		SWRLClassAtom clsAtom_Stop_X = SWRLUtils.createSwrlClassAtom(pathData.getStop(), varX);
		SWRLObjectPropertyAtom propAtom_X_giveWay_Y = SWRLUtils.createSWRLObjectPropertyAtom(pathData.getGiveWay(), varX, varY);

		// CollisionWarning(?X) ^ CollisionWarning(?Y) ^ SpecialVehicle(?Y) ^
		// OneWayLane(?LANE1) ^ isRunningOn(?X, ?LANE1) ^ OneWayLane(?LANE2) ^ isRunningOn(?Y, ?LANE2) ^
		// isLaneOf(?LANE1, ?RS1) ^ isLaneOf(?LANE2, ?RS2)
		Set<SWRLAtom> body = new HashSet<>();

		body.add(clsAtom_CollisionWarning_X);
		body.add(clsAtom_CollisionWarning_Y);
		body.add(clsAtom_SpecialVehicle_Y);
		body.add(clsAtom_OneWayLane_Lane1);
		body.add(clsAtom_OneWayLane_Lane2);
		body.add(propAtom_X_isRunningOn_Lane1);
		body.add(propAtom_Y_isRunningOn_Lane2);
		body.add(propAtom_Lane1_isLaneOf_RS1);
		body.add(propAtom_Lane2_isLaneOf_RS2);

		// Stop(?X) ^ giveWay(?X, ?Y)
		Set<SWRLAtom> head = new HashSet<>();
		head.add(clsAtom_Stop_X);
		head.add(propAtom_X_giveWay_Y);
		// body -> head
		SWRLRule rule = SWRLUtils.createAnonymousSwrlRule(body,	head);
		ontology.addAxiom(rule);
	}
}
