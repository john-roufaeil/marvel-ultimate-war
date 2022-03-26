package exceptions;

public class UnallowedMovementException extends GameActionException {

	/**
	 * perform an unallowed movement
	 */
	private static final long serialVersionUID = 1L;

	public UnallowedMovementException() {
		super();
	}
	
	public UnallowedMovementException(String s) {
		super(s);
	}
}
