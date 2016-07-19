package au.com.reagroup.security;

public class UnauthorizedException extends Exception {

	public UnauthorizedException() {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnauthorizedException(String message) {
		super(message);
	}


}
