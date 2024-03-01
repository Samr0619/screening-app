package in.deloitte.screening.app.exceptions;


public class AuthorizationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1919390763733323112L;

	public AuthorizationException(String message) {
		super(message);
	}
}
