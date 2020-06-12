package Assignment.WebSemantico.mapdata;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

/**
 * 
 * @author Andrea Procucci
 * 
 * ###       |       ###
 * ###       |       ###
 * ### lane2 | lane1 ###
 * ###       |       ###
 * ###       |       ###
 * 
 */
public class RoadSegmentWithTwoLane {
	private OWLNamedIndividual roadSegment;
	private OWLNamedIndividual lane1;
	private OWLNamedIndividual lane2;
	
	public RoadSegmentWithTwoLane(OWLNamedIndividual roadSegment, OWLNamedIndividual lane1, OWLNamedIndividual lane2) {
		this.roadSegment = roadSegment;
		this.lane1 = lane1;
		this.lane2 = lane2;
	}

	public OWLNamedIndividual getRoadSegment() {
		return roadSegment;
	}

	public OWLNamedIndividual getLane1() {
		return lane1;
	}

	public OWLNamedIndividual getLane2() {
		return lane2;
	}
}
