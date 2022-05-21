package exceptions;

@SuppressWarnings("serial")
public class InvalidTargetException extends GameActionException {

	public InvalidTargetException() {
super();
	}

	public InvalidTargetException(String s) {
		super(s);	}

}
