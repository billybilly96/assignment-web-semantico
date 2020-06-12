package Assignment.WebSemantico.utils.ontology.exception;

public class LoadedOntoNotSelected extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6945618514742377846L;
	
	@Override
	public String getMessage() {
		return "Before using that method you must select an ontology!";
	}
}
