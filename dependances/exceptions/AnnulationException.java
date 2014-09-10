package exceptions;

public class AnnulationException extends Exception {
	private static final long serialVersionUID = 1L;

	
	public AnnulationException() {}
	
	public AnnulationException(String cause) {
		super(cause);
	}
	
}
