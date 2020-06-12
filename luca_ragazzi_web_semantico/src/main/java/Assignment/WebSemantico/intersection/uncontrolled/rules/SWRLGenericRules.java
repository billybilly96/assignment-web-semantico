package Assignment.WebSemantico.intersection.uncontrolled.rules;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import Assignment.WebSemantico.cardata.SpeedProfileData;
import org.apache.commons.lang.ObjectUtils;
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

public class SWRLGenericRules {
	private static final MyPathData pathData = MyPathData.getInstance();
	private static final MyMapData mapData = MyMapData.getInstance();
	private static final MyCarData carData = MyCarData.getInstance();
	private static final SpeedProfileData speedData = SpeedProfileData.getInstance();
	
	public static void collisionWarningRule(OWLOntologyWithTools ontology, IRI firstVarIRI, IRI secondVarIRI) throws LoadedOntoNotSelected {
		SWRLVariable varX = SWRLUtils.createSwrlVariable(firstVarIRI);
		SWRLVariable varY = SWRLUtils.createSwrlVariable(secondVarIRI);
		// collisionWarningWith(?X, ?Y)
		SWRLObjectPropertyAtom propAtom_X_collisionWarningWith_Y = SWRLUtils.createSWRLObjectPropertyAtom(pathData.getCollisionWarningWith(), varX, varY); 
		
		SWRLClassAtom clsAtom_CollisionWarning_X = SWRLUtils.createSwrlClassAtom(pathData.getCollisionWarning(), varX);
		SWRLClassAtom clsAtom_CollisionWarning_Y = SWRLUtils.createSwrlClassAtom(pathData.getCollisionWarning(), varY);
		// CollisionWarning(?X) ^ CollisionWarning(?Y)
		Set<SWRLAtom> head = new HashSet<>();
		head.add(clsAtom_CollisionWarning_X);
		head.add(clsAtom_CollisionWarning_Y);
		// collisionWarningWith(?X, ?Y) -> CollisionWarning(?X) ^ CollisionWarning(?Y) 
	    SWRLRule rule = SWRLUtils.createAnonymousSwrlRule(
	        			Collections.singleton(propAtom_X_collisionWarningWith_Y),	 
	        			head
	        			);
	    ontology.addAxiom(rule);
	}
	
	public static void drivingDirectionRule(OWLOntologyWithTools ontology, IRI varIntersectionIRI, IRI varXIRI, IRI laneIRI, IRI nextLaneIRI, DrivingDirection drivingDirection) throws LoadedOntoNotSelected {
		SWRLVariable varIntersection = SWRLUtils.createSwrlVariable(varIntersectionIRI);
		SWRLVariable varX = SWRLUtils.createSwrlVariable(varXIRI);
		SWRLVariable varLane = SWRLUtils.createSwrlVariable(laneIRI);
		SWRLVariable varNextLane = SWRLUtils.createSwrlVariable(nextLaneIRI);
		// Intersection(?intersection)
		SWRLClassAtom clsAtom_Intersection_intersection = SWRLUtils.createSwrlClassAtom(mapData.getClassIntersection(), varIntersection);
		// isRunningOn(?X, ?Lane)
		SWRLObjectPropertyAtom propAtom_X_isRunningOn_Lane = SWRLUtils.createSWRLObjectPropertyAtom(carData.getIsRunningOn(), varX, varLane); 
		
		SWRLObjectPropertyAtom propAtom_Lane_NextLane = null;
		SWRLClassAtom head_clsAtom_TurnRight_X = null;
		
		switch(drivingDirection) {
		case RIGHT:
			// turnRightTo(?lane, ?nextLane)
			propAtom_Lane_NextLane = SWRLUtils.createSWRLObjectPropertyAtom(mapData.getTurnRightTo(), varLane, varNextLane);
			// TurnRight(?X)
			head_clsAtom_TurnRight_X = SWRLUtils.createSwrlClassAtom(pathData.getTurnRight(), varX);
			break;
		case LEFT:
			// turnLeftTo(?lane, ?nextLane)
			propAtom_Lane_NextLane = SWRLUtils.createSWRLObjectPropertyAtom(mapData.getTurnLeftTo(), varLane, varNextLane);
			// TurnLeft(?X)
			head_clsAtom_TurnRight_X = SWRLUtils.createSwrlClassAtom(pathData.getTurnLeft(), varX);
			break;
		case STRAIGHT:
			// goStraightTo(?lane, ?nextLane)
			propAtom_Lane_NextLane = SWRLUtils.createSWRLObjectPropertyAtom(mapData.getGoStraightTo(), varLane, varNextLane);
			// GoForward(?X)
			head_clsAtom_TurnRight_X = SWRLUtils.createSwrlClassAtom(pathData.getGoForward(), varX);
			break;
		default:
			throw new InvalidParameterException("The input driving direction " + drivingDirection + " is currently not managed by the application!");
		}
		
		// nextPathSegment(?lane, ?intersection)
		SWRLObjectPropertyAtom propAtom_Lane_nextPathSegment_Intersection = SWRLUtils.createSWRLObjectPropertyAtom(MyPathData.getInstance().getNextPathSegment(), varLane, varIntersection); 
		// nextPathSegment(?intersection, ?nextLane)
		SWRLObjectPropertyAtom propAtom_Intersection_nextPathSegment_NextLane = SWRLUtils.createSWRLObjectPropertyAtom(MyPathData.getInstance().getNextPathSegment(), varIntersection, varNextLane); 
		
		// Intersection(?intersection) ^ isRunningOn(?X, ?Lane) ^ turnRightTo(?lane, ?nextLane) ^ nextPathSegment(?lane, ?intersection) ^ nextPathSegment(?intersection, ?nextLane)
		Set<SWRLAtom> body = new HashSet<>();
		body.add(clsAtom_Intersection_intersection);
		body.add(propAtom_X_isRunningOn_Lane);
		body.add(propAtom_Lane_NextLane);
		body.add(propAtom_Lane_nextPathSegment_Intersection);
		body.add(propAtom_Intersection_nextPathSegment_NextLane);
		// body -> TurnRight(?X)
	    SWRLRule rule = SWRLUtils.createAnonymousSwrlRule(
	        			body,	 
	        			Collections.singleton(head_clsAtom_TurnRight_X)
	        			);
	    ontology.addAxiom(rule);
	}

	public static void respectRoadSpeedLimitRule(OWLOntologyWithTools ontology, IRI firstVarIRI, IRI secondVarIRI, IRI varLaneIRI, boolean speedWarning) throws LoadedOntoNotSelected {
		SWRLVariable varX = SWRLUtils.createSwrlVariable(firstVarIRI);
		SWRLVariable varY = SWRLUtils.createSwrlVariable(secondVarIRI);
		SWRLVariable varLane = SWRLUtils.createSwrlVariable(varLaneIRI);

		SWRLClassAtom clsAtom_OneWayLane_Lane = SWRLUtils.createSwrlClassAtom(mapData.getClassOneWayLane(), varLane);
		SWRLClassAtom clsAtom_SpeedLimit_Y = SWRLUtils.createSwrlClassAtom(mapData.getClassSpeedLimit(), varY);

		SWRLObjectPropertyAtom propAtom_X_isRunningOn_Lane = SWRLUtils.createSWRLObjectPropertyAtom(carData.getIsRunningOn(), varX, varLane);
		// overSpeedWarningRespectTo(?X, ?Y)
		SWRLObjectPropertyAtom propAtom_X_overSpeedWarningRespectTo_Y = SWRLUtils.createSWRLObjectPropertyAtom(pathData.getOverSpeedWarningRespectTo(), varX, varY);

		Set<SWRLAtom> body = new HashSet<>();
		body.add(propAtom_X_isRunningOn_Lane);
		body.add(clsAtom_OneWayLane_Lane);

		SWRLClassAtom head_clsAtom_SpeedProfile_X = null;

		if (!speedWarning) {
			// isRunningOn(?X, ?LANE) ^ OneWayLane(?LANE) -> acceleration(?X)
			head_clsAtom_SpeedProfile_X = SWRLUtils.createSwrlClassAtom(speedData.getClassAcceleration(), varX);
		} else {
			// isRunningOn(?X, ?LANE) ^ OneWayLane(?LANE) ^ SpeedLimit(?Y) overSpeedWarningRespectTo(?X, ?Y) -> constantSpeed(?X)
			body.add(clsAtom_SpeedLimit_Y);
			body.add(propAtom_X_overSpeedWarningRespectTo_Y);
			head_clsAtom_SpeedProfile_X = SWRLUtils.createSwrlClassAtom(speedData.getClassConstantSpeed(), varX);
		}

		SWRLRule rule = SWRLUtils.createAnonymousSwrlRule(
				body,
				Collections.singleton(head_clsAtom_SpeedProfile_X)
		);
		ontology.addAxiom(rule);
	}
	
}
