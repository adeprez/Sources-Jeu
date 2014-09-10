package exception;

public class ServeurFullException extends Throwable {
	private static final long serialVersionUID = 1L;

	
	public ServeurFullException() {
		super("Le serveur est plein");
	}
	
}
