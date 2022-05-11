package exceptions;

@SuppressWarnings("serial")
public class LeaderAbilityAlreadyUsedException extends GameActionException {

	public LeaderAbilityAlreadyUsedException() {
		super();
	}

	public LeaderAbilityAlreadyUsedException(String s) {
		super(s);
	}

}
