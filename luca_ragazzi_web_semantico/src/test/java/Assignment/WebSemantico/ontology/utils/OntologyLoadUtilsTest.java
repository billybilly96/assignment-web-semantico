package Assignment.WebSemantico.ontology.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Test;

import Assignment.WebSemantico.utils.file.FilesUtils;
import Assignment.WebSemantico.utils.ontology.OWLOntologyWithTools;
import Assignment.WebSemantico.utils.ontology.OntologyLoadUtils;

public class OntologyLoadUtilsTest {
	private final String webCarOntology = "https://www.toyota-ti.ac.jp/Lab/Denshi/COIN/Ontology/TTICore-0.1/TTICarOnto.owl"; 
	private final String webWrongOntologyUrl = "https://www.google.it/WrongOnto.owl"; 
	private final static String fsSavedOntologyName = "Test_save_onto.owl";
	private final String fsUnexistentOntologyName = "Unex" + FilesUtils.fileSeparator + "Ontology.owl";
		
	@Test public void testCreateEmptyOntology() {
		assertTrue("Create empty ontology should return 'true'", 
				OntologyLoadUtils.loadOntologyFromTheWeb(webCarOntology).isPresent());
    }
	
	@Test public void testLoadOntologyFromWeb() {
        assertTrue("Load ontology from web should return 'true'", 
        		OntologyLoadUtils.loadOntologyFromTheWeb(webCarOntology).isPresent());
        assertFalse("Load inexistent ontology from web should return 'false'", 
        		OntologyLoadUtils.loadOntologyFromTheWeb(webWrongOntologyUrl).isPresent());
    }
	
	@Test public void testLoadOntologyFromResources() throws IOException {
		assertTrue("Load ontology from file system should return 'true'", 
				OntologyLoadUtils.loadOntologyFromResources("ABox", "TTIPathData.owl").isPresent());
        assertFalse("Load inexistent ontology from file system should return 'false'", 
        		OntologyLoadUtils.loadOntologyFromResources("ABox", "UnexistingOnto.owl").isPresent());
    }
	
	@Test public void testSaveAndLoadOntologyIntoFileSystem() {
		OWLOntologyWithTools ontology = OntologyLoadUtils.loadOntologyFromTheWeb(webCarOntology).get();
		ontology.printOntologyInfo();
		assertTrue("Save ontology into file system should return 'true'", 
				OntologyLoadUtils.saveOntologyIntoFileWithTurtleSyntax(OntologyLoadUtils.ontologyPath, fsSavedOntologyName, ontology));
				
		assertFalse("Save unexistent ontology into file system should return 'false'", 
				OntologyLoadUtils.saveOntologyIntoFileWithTurtleSyntax(OntologyLoadUtils.ontologyPath, fsUnexistentOntologyName, ontology));
		
		assertTrue("Load ontology from file system should return 'true'", 
				OntologyLoadUtils.loadOntologyFromFileSystem(OntologyLoadUtils.ontologyPath, fsSavedOntologyName).isPresent());
		
        assertFalse("Load inexistent ontology from file system should return 'false'", 
        		OntologyLoadUtils.loadOntologyFromFileSystem(OntologyLoadUtils.ontologyPath, fsUnexistentOntologyName).isPresent());
    }
	
	@AfterClass
	public static void clean() {
		FilesUtils.deleteFile(new File(OntologyLoadUtils.ontologyPath, fsSavedOntologyName));
	}
}
