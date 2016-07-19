package au.com.reagroup.security;

public class ForbiddenException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 351522715737572958L;

	public ForbiddenException() {
	}

	public ForbiddenException(String message) {
		super(message);
	}

}
