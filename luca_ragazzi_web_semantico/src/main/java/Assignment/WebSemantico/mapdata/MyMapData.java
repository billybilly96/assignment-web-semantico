package Assignment.WebSemantico.mapdata;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class MyMapData {
	private OWLClass classRoadSegment;
	private OWLClass classIntersection;
	private OWLClass classOneWayLane;
	private OWLClass classSpeedLimit;
	
	private OWLNamedIndividual intersection;
	private OWLNamedIndividual speedLimit;
	
	private RoadSegmentWithTwoLane northRoadSegment;
	private RoadSegmentWithTwoLane eastRoadSegment;
	private RoadSegmentWithTwoLane southRoadSegment;
	private RoadSegmentWithTwoLane westRoadSegment;
	
	private OWLObjectProperty hasLane;
	private OWLObjectProperty isLaneOf;
	private OWLObjectProperty goStraightTo;
	private OWLObjectProperty turnLeftTo;
	private OWLObjectProperty turnRightTo;
	private OWLObjectProperty isConnectedTo;

	private OWLDataProperty speedMax;
	
	private static MyMapData myMapDataOnto;
	
	private MyMapData() {}
	
	public static MyMapData getInstance() {
		if (myMapDataOnto == null) {
			myMapDataOnto = new MyMapData();
		}
		return myMapDataOnto;
	}
	
	public RoadSegmentWithTwoLane getNorthRoadSegment() {
		return northRoadSegment;
	}

	public void setNorthRoadSegment(RoadSegmentWithTwoLane northRoadSegment) {
		this.northRoadSegment = northRoadSegment;
	}

	public RoadSegmentWithTwoLane getEastRoadSegment() {
		return eastRoadSegment;
	}

	public void setEastRoadSegment(RoadSegmentWithTwoLane eastRoadSegment) {
		this.eastRoadSegment = eastRoadSegment;
	}

	public RoadSegmentWithTwoLane getSouthRoadSegment() {
		return southRoadSegment;
	}

	public void setSouthRoadSegment(RoadSegmentWithTwoLane southRoadSegment) {
		this.southRoadSegment = southRoadSegment;
	}

	public RoadSegmentWithTwoLane getWestRoadSegment() {
		return westRoadSegment;
	}

	public void setWestRoadSegment(RoadSegmentWithTwoLane westRoadSegment) {
		this.westRoadSegment = westRoadSegment;
	}

	public static MyMapData getMyMapDataOnto() {
		return myMapDataOnto;
	}

	public static void setMyMapDataOnto(MyMapData myMapDataOnto) {
		MyMapData.myMapDataOnto = myMapDataOnto;
	}

	public OWLObjectProperty getHasLane() {
		return hasLane;
	}

	public void setHasLane(OWLObjectProperty hasLane) {
		this.hasLane = hasLane;
	}

	public OWLObjectProperty getIsLaneOf() {
		return isLaneOf;
	}

	public void setIsLaneOf(OWLObjectProperty isLaneOf) {
		this.isLaneOf = isLaneOf;
	}

	public OWLObjectProperty getGoStraightTo() {
		return goStraightTo;
	}

	public void setGoStraightTo(OWLObjectProperty goStraightTo) {
		this.goStraightTo = goStraightTo;
	}

	public OWLObjectProperty getTurnLeftTo() {
		return turnLeftTo;
	}

	public void setTurnLeftTo(OWLObjectProperty turnLeftTo) {
		this.turnLeftTo = turnLeftTo;
	}

	public OWLObjectProperty getTurnRightTo() {
		return turnRightTo;
	}

	public void setTurnRightTo(OWLObjectProperty turnRightTo) {
		this.turnRightTo = turnRightTo;
	}

	public OWLObjectProperty getIsConnectedTo() {
		return isConnectedTo;
	}

	public void setIsConnectedTo(OWLObjectProperty isConnectedTo) {
		this.isConnectedTo = isConnectedTo;
	}
	
	public void setClassRoadSegment(OWLClass classRoadSegment) {
		this.classRoadSegment = classRoadSegment;
	}

	public void setClassIntersection(OWLClass classIntersection) {
		this.classIntersection = classIntersection;
	}

	public void setClassOneWayLane(OWLClass classOneWayLane) {
		this.classOneWayLane = classOneWayLane;
	}

	public static void setMyOntology(MyMapData myOntology) {
		MyMapData.myMapDataOnto = myOntology;
	}

	public OWLClass getClassRoadSegment() {
		return classRoadSegment;
	}

	public OWLClass getClassIntersection() {
		return classIntersection;
	}

	public OWLClass getClassOneWayLane() {
		return classOneWayLane;
	}

	public static MyMapData getMyOntology() {
		return myMapDataOnto;
	}

	public void setIntersection(OWLNamedIndividual intersection) {
		this.intersection = intersection;
	}

	public OWLNamedIndividual getIntersection() {
		return intersection;
	}

	public void setClassSpeedLimit(OWLClass classSpeedLimit) {
		this.classSpeedLimit = classSpeedLimit;
	}

	public OWLClass getClassSpeedLimit() {
		return classSpeedLimit;
	}

	public void setSpeedLimit(OWLNamedIndividual speedLimit) {
		this.speedLimit = speedLimit;
	}

	public OWLNamedIndividual getSpeedLimit() {
		return speedLimit;
	}

	public OWLDataProperty getSpeedMax() {
		return speedMax;
	}

	public void setSpeedMax(OWLDataProperty speedMax) {
		this.speedMax = speedMax;
	}

}
