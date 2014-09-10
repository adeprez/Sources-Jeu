package exceptions;

public class TombeException extends HorsLimiteException {
	private static final long serialVersionUID = 1L;

	
	public TombeException() {}

	public TombeException(String message) {
		super(message);
	}
	
}
