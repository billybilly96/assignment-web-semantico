package Assignment.WebSemantico.utils.ontology.exception;

public class OntologyCreationOrLoadingFailed extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3238863495698900978L;
	
	@Override
	public String getMessage() {
		return "Failed to load or create the ontology!";
	}

}
