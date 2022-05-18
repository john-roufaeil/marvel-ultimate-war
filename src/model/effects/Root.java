package model.effects;

import java.util.ArrayList;

import model.world.Champion;
import model.world.Condition;

public class Root extends Effect {

	public Root( int duration) {
		super("Root", duration, EffectType.DEBUFF);
		
	}
	
	/*
	 * Target cannot move.
	 */

	public void apply(Champion c) {
		if(c.getCondition() != Condition.INACTIVE)
			c.setCondition(Condition.ROOTED);
	}
	
	public void remove(Champion c) {
		if (c.getCondition() == Condition.INACTIVE)
			return;
		ArrayList<Effect> applied = c.getAppliedEffects();
		boolean rooted = false;
		for (Effect effect : applied) {
			rooted = (effect instanceof Root);
			if (rooted)
				break;
		}
		if (!rooted)
			c.setCondition(Condition.ACTIVE);
	}
}
