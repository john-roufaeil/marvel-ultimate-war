package exceptions;

@SuppressWarnings("serial")
public class UnallowedMovementException extends GameActionException {

	public UnallowedMovementException() {
		super();
	}

	public UnallowedMovementException(String s) {
		super(s);
	}

}
