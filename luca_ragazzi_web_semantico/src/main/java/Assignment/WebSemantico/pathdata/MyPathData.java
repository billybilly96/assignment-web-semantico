package Assignment.WebSemantico.pathdata;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class MyPathData {
	private OWLClass startLane;
	private OWLClass endLane;
	private OWLClass startTramLane;
	private OWLClass endTramLane;
	private OWLClass collisionWarning;
	private OWLClass overSpeedWarning;

	// Driving actions
	private OWLClass turnLeft;
	private OWLClass turnRight;
	private OWLClass goForward;
	private OWLClass go;
	private OWLClass stop;
	
	private OWLObjectProperty nextPathSegment;
	private OWLObjectProperty collisionWarningWith;
	private OWLObjectProperty overSpeedWarningRespectTo;
	private OWLDataProperty pathSegmentID;
	private OWLObjectProperty giveWay;
	
	private static MyPathData myPathDataOnto;
	
	private MyPathData() {}
	
	public static MyPathData getInstance() {
		if (myPathDataOnto == null) {
			myPathDataOnto = new MyPathData();
		}
		return myPathDataOnto;
	}

	public OWLClass getTurnLeft() {
		return turnLeft;
	}

	public void setTurnLeft(OWLClass turnLeft) {
		this.turnLeft = turnLeft;
	}

	public OWLClass getTurnRight() {
		return turnRight;
	}

	public void setTurnRight(OWLClass turnRight) {
		this.turnRight = turnRight;
	}

	public OWLClass getGoForward() {
		return goForward;
	}

	public void setGoForward(OWLClass goForward) {
		this.goForward = goForward;
	}

	public OWLClass getGo() {
		return go;
	}

	public void setGo(OWLClass go) {
		this.go = go;
	}

	public OWLClass getStop() {
		return stop;
	}

	public void setStop(OWLClass stop) {
		this.stop = stop;
	}

	public OWLObjectProperty getGiveWay() {
		return giveWay;
	}

	public void setGiveWay(OWLObjectProperty giveWay) {
		this.giveWay = giveWay;
	}

	public OWLClass getStartLane() {
		return startLane;
	}

	public void setStartLane(OWLClass startLane) {
		this.startLane = startLane;
	}

	public OWLClass getEndLane() {
		return endLane;
	}

	public void setEndLane(OWLClass endLane) {
		this.endLane = endLane;
	}

	public OWLClass getTramStartLane() {
		return startTramLane;
	}

	public void setTramStartLane(OWLClass startLane) {
		this.startTramLane = startLane;
	}

	public OWLClass getTramEndLane() {
		return endTramLane;
	}

	public void setTramEndLane(OWLClass endLane) {
		this.endTramLane = endLane;
	}

	public OWLObjectProperty getNextPathSegment() {
		return nextPathSegment;
	}

	public void setNextPathSegment(OWLObjectProperty nextPathSegment) {
		this.nextPathSegment = nextPathSegment;
	}

	public OWLDataProperty getPathSegmentID() {
		return pathSegmentID;
	}

	public void setPathSegmentID(OWLDataProperty pathSegmentID) {
		this.pathSegmentID = pathSegmentID;
	}

	public static MyPathData getMyPathDataOnto() {
		return myPathDataOnto;
	}

	public static void setMyPathDataOnto(MyPathData myPathDataOnto) {
		MyPathData.myPathDataOnto = myPathDataOnto;
	}

	public OWLObjectProperty getCollisionWarningWith() {
		return collisionWarningWith;
	}

	public void setCollisionWarningWith(OWLObjectProperty collisionWarningWith) {
		this.collisionWarningWith = collisionWarningWith;
	}

	public OWLClass getCollisionWarning() {
		return collisionWarning;
	}

	public void setCollisionWarning(OWLClass collisionWarning) {
		this.collisionWarning = collisionWarning;
	}

	public OWLClass getOverSpeedWarning() {
		return overSpeedWarning;
	}

	public void setOverSpeedWarning(OWLClass overSpeedWarning) {
		this.overSpeedWarning = overSpeedWarning;
	}

	public OWLObjectProperty getOverSpeedWarningRespectTo() {
		return overSpeedWarningRespectTo;
	}

	public void setOverSpeedWarningRespectTo(OWLObjectProperty overSpeedWarningRespectTo) {
		this.overSpeedWarningRespectTo = overSpeedWarningRespectTo;
	}
}
