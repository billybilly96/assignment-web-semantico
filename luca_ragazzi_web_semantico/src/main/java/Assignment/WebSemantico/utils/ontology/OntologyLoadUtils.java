package Assignment.WebSemantico.utils.ontology;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.UnloadableImportException;

import Assignment.WebSemantico.utils.file.FilesUtils;
import Assignment.WebSemantico.utils.logger.Logger;
import Assignment.WebSemantico.utils.logger.MessageType;

public class OntologyLoadUtils {
	private static String className = "OntologyLoadUtils";
	public static final String ontologyPath = FilesUtils.getDirPathIntoUserHome("WSAssignment_Ontologies");
	
	private static OWLOntologyManager createOntologyManager() {
		return OWLManager.createOWLOntologyManager();
	}
	
	public static Optional<OWLOntologyWithTools> createEmptyOntology(IRI ontologyIRI) {
		try {
			return Optional.ofNullable(new OWLOntologyWithTools(createOntologyManager().createOntology(ontologyIRI)));
		} catch (OWLOntologyCreationException e) {
			Logger.log(MessageType.ERROR, className, e.getMessage());
			if(!Logger.disableStackTrace) e.printStackTrace();
		}
		return Optional.empty();
	}
		
	/**
	 * Load an ontology from the web.
	 * @param ontologyURL ontology URL.
	 * @return Optional<OWLOntologyWithTools>.
	 */
	public static Optional<OWLOntologyWithTools> loadOntologyFromTheWeb(String ontologyURL) {
		IRI carOntology = IRI.create(ontologyURL);
		try {
			return Optional.ofNullable(new OWLOntologyWithTools(createOntologyManager().loadOntology(carOntology)));
		} catch (OWLOntologyCreationException e) {
			Logger.log(MessageType.ERROR, className, e.getMessage());
			if(!Logger.disableStackTrace) e.printStackTrace();
		}
		return Optional.empty();
	}
	
	/**
	 * Load an ontology from the File System.
	 * @param filePath
	 * @param fileName
	 * @return Optional<OWLOntologyWithTools>.
	 */
	public static Optional<OWLOntologyWithTools> loadOntologyFromFileSystem(String filePath, String fileName) {
		File file = new File(filePath, fileName);
		try {
			return Optional.ofNullable(new OWLOntologyWithTools(createOntologyManager().loadOntologyFromOntologyDocument(file)));
		} catch (OWLOntologyCreationException | UnloadableImportException e) {
			Logger.log(MessageType.ERROR, className, e.getMessage());
			if(!Logger.disableStackTrace) {
				e.printStackTrace();
				Logger.log(MessageType.ERROR, className, "End of printStackTrace!");
			}			 
		}
		return Optional.empty();
	}
	
	/**
	 * Load an ontology from resources.
	 * @param fileName
	 * @return Optional<OWLOntologyWithTools>.
	 * @throws IOException 
	 */
	public static Optional<OWLOntologyWithTools> loadOntologyFromResources(String resourceDir, String fileName) throws IOException {
		try {
			File file = FilesUtils.getResourceFileAsFile(resourceDir + "/" + fileName);
			return Optional.ofNullable(new OWLOntologyWithTools(createOntologyManager().loadOntologyFromOntologyDocument(file)));
		} catch (OWLOntologyCreationException | UnloadableImportException | IOException | NullPointerException e) {
			Logger.log(MessageType.ERROR, className, e.getMessage());
			if(!Logger.disableStackTrace) {
				e.printStackTrace();
				Logger.log(MessageType.ERROR, className, "End of printStackTrace!");
			}			 
		}
		return Optional.empty();
	}
	
	public static boolean saveOntologyIntoFileUsingTurtleSyntax(String fileName, OWLOntologyWithTools ontology) {
		return saveOntologyIntoFileWithTurtleSyntax(ontologyPath, fileName, ontology);
	}
	
	/**
	 * Save an ontology into a file using turtle syntax.
	 * @param filePath
	 * @param fileName
	 * @param ontology
	 * @return true if save operation ends with success.
	 */
	public static boolean saveOntologyIntoFileWithTurtleSyntax(String filePath, String fileName, OWLOntologyWithTools ontology) {
		FilesUtils.createDirectoryIfDoesntExists(ontologyPath);
	    
		File fileout = new File(filePath, fileName);
		try {
			ontology.getManager().saveOntology(ontology.getOntology(), 
											   new TurtleDocumentFormat(), 
											   new FileOutputStream(fileout));
			return true;
		} catch (OWLOntologyStorageException | FileNotFoundException e) {
			Logger.log(MessageType.ERROR, className, e.getMessage());
			if(!Logger.disableStackTrace)e.printStackTrace();
		}
		return false;
	}
}
