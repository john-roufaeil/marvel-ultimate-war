package exceptions;

public class AbilityUseException extends GameActionException{
	/**
	 * casting invalid ability
	 */
	private static final long serialVersionUID = 1L;
	
	public AbilityUseException() {
		super();
	}
	public AbilityUseException(String s) {
		super(s);
	}
}
