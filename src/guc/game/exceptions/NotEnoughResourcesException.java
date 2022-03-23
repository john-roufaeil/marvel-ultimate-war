package guc.game.exceptions;

public class NotEnoughResourcesException extends GameActionException {
	public NotEnoughResourcesException() {
		super();
	}
	
	public NotEnoughResourcesException(String s) {
		super(s);
	}
}
