package exceptions;

public class LeaderAbilityAlreadyUsedException extends GameActionException {
	/**
	 * reusing a leader ability
	 */
	private static final long serialVersionUID = 1L;

	public LeaderAbilityAlreadyUsedException() {
		super();
	}
	
	public LeaderAbilityAlreadyUsedException(String s) {
		super(s);
	}
}
