package Assignment.WebSemantico.utils.ontology;

import Assignment.WebSemantico.utils.ontology.exception.LoadedOntoNotSelected;

public class LoadMainOntology {
	private  static OWLOntologyWithTools loadedOnto;
	
	/**
	 * Select an ontology allow the use of the methods without the need of specify the ontology into the signature.
	 * @param ontology OWLOntologyWithTools.
	 */
	public static void selectOntology(OWLOntologyWithTools ontology) {
		loadedOnto = ontology;
	}
	
	public static OWLOntologyWithTools getLoadedOnto() throws LoadedOntoNotSelected {
		checkLoadedOnto();
		return loadedOnto;
	}
	
	protected static void checkLoadedOnto() throws LoadedOntoNotSelected {
		if(loadedOnto == null) {
			throw new LoadedOntoNotSelected();
		}
	}
}
