package Assignment.WebSemantico.ontology.utils;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;

import Assignment.WebSemantico.utils.ontology.OWLOntologyWithTools;
import Assignment.WebSemantico.utils.ontology.OntologyLoadUtils;

public class ImportOntologiesTest {
	private final static String base = "http://www.ws.it/ontologies/imports";
	
	private final String webCarOntology = "https://www.toyota-ti.ac.jp/Lab/Denshi/COIN/Ontology/TTICore-0.1/TTICarOnto.owl"; 

	private static OWLOntologyWithTools ontology;
	
	@Before public void loadTestOntology() {
		ontology = OntologyLoadUtils.createEmptyOntology(IRI.create(base)).get();
    }
	
	@Test public void testImportOntology() {
		assertTrue("At the beginning the import number should be '0'", ontology.getNumberOfOntologiesImported() == 0);
		assertTrue("Import an ontology should return 'true'", ontology.importAnOntology(IRI.create(webCarOntology)) == true);
		ontology.printOntologyImports();
		assertTrue("Now the import number should be '1'", ontology.getNumberOfOntologiesImported() == 1);
	}
}
