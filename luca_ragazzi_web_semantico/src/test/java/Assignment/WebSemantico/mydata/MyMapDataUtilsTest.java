package Assignment.WebSemantico.mydata;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;

import Assignment.WebSemantico.mapdata.MyMapDataUtils;
import Assignment.WebSemantico.utils.ontology.LoadMainOntology;
import Assignment.WebSemantico.utils.ontology.OWLOntologyWithTools;
import Assignment.WebSemantico.utils.ontology.OntologyLoadUtils;
import Assignment.WebSemantico.utils.ontology.exception.LoadedOntoNotSelected;

public class MyMapDataUtilsTest {
	
	@Test public void testAddIntersection() throws LoadedOntoNotSelected {
		OWLOntologyWithTools ontology = OntologyLoadUtils.createEmptyOntology(IRI.create("http:\\myOntoTestIntersection")).get();	
		LoadMainOntology.selectOntology(ontology);
		assertTrue("Get axioms number should return '0'", ontology.getOntology().axioms().count() == 0);
		assertTrue("Get individuals number should return '0'", ontology.getOntology().individualsInSignature().count() == 0);
		assertTrue("Get classes number should return '0'", ontology.getOntology().classesInSignature().count() == 0);
		MyMapDataUtils.addIntersection(ontology);
		assertTrue("Get axioms number should return '1'", ontology.getOntology().axioms().count() == 1);
		assertTrue("Get individuals number should return '1'", ontology.getOntology().individualsInSignature().count() == 1);
		assertTrue("Get classes number should return '1'", ontology.getOntology().classesInSignature().count() == 1);
    }
	
	@Test public void testAddRoadSegmentsLanes() throws LoadedOntoNotSelected {
		OWLOntologyWithTools ontology = OntologyLoadUtils.createEmptyOntology(IRI.create("http:\\myOntoTestRoadSegmentLane")).get();
		LoadMainOntology.selectOntology(ontology);
		assertTrue("Get axioms number should return '0'", ontology.getOntology().axioms().count() == 0);
		assertTrue("Get individuals number should return '0'", ontology.getOntology().individualsInSignature().count() == 0);
		assertTrue("Get classes number should return '0'", ontology.getOntology().classesInSignature().count() == 0);
		MyMapDataUtils.addRoadSegments(ontology);
		assertTrue("Get axioms number should return '12'", ontology.getOntology().axioms().count() == 12); // 4 road segments and 8 road segment lanes
		assertTrue("Get individuals number should return '12'", ontology.getOntology().individualsInSignature().count() == 12);
		assertTrue("Get classes number should return '1'", ontology.getOntology().classesInSignature().count() == 2);
    }
}
