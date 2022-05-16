package exceptions;

@SuppressWarnings("serial")
public abstract class GameActionException extends Exception {

	public GameActionException() {
		super();
	}

	public GameActionException(String s) {
		super(s);
	}

}
