package exceptions;

@SuppressWarnings("serial")
public class NotEnoughResourcesException extends GameActionException {

	public NotEnoughResourcesException() {
		super();
	}

	public NotEnoughResourcesException(String s) {
		super(s);
		
	}
}
