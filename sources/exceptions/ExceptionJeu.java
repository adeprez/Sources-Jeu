package exceptions;

public class ExceptionJeu extends Exception {
	private static final long serialVersionUID = 1L;

	
	public ExceptionJeu() {}
	
	public ExceptionJeu(String message) {
		super(message);
	}
}
