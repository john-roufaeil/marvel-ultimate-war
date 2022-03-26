package exceptions;

public class NotEnoughResourcesException extends GameActionException {
	/**
	 * perform action in the wrong turn
	 */
	private static final long serialVersionUID = 1L;

	public NotEnoughResourcesException() {
		super();
	}
	
	public NotEnoughResourcesException(String s) {
		super(s);
	}
}
