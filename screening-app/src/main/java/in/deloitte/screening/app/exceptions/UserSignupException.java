package in.deloitte.screening.app.exceptions;

public class UserSignupException extends RuntimeException {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserSignupException(String message) {
    	
        super(message);
    }
}

