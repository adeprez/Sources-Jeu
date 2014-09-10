package exception;

public class InvalideException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalideException() {}
	
	public InvalideException(String message) {
		super(message);
	}
	
}
