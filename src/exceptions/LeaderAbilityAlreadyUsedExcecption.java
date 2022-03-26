package exceptions;

public class LeaderAbilityAlreadyUsedExcecption extends GameActionException {
	/**
	 * reusing a leader ability
	 */
	private static final long serialVersionUID = 1L;

	public LeaderAbilityAlreadyUsedExcecption() {
		super();
	}
	
	public LeaderAbilityAlreadyUsedExcecption(String s) {
		super(s);
	}
}
