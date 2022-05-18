package model.effects;

import java.util.ArrayList;

import model.world.Champion;
import model.world.Condition;

public class Stun extends Effect {
	public Stun(int duration) {
		super("Stun", duration, EffectType.DEBUFF);
	}

	/*
	 * Set target to INACTIVE.
	 * Target is not allowed to play their turn for the duration.
	 */
	
	public void apply(Champion c) {
		c.setCondition(Condition.INACTIVE);
	}
	
	public void remove(Champion c) {
		ArrayList<Effect> applied = c.getAppliedEffects();
		boolean rooted = false;
		boolean stunned = false;
		for (Effect effect : applied) {
			if (effect instanceof Stun) {
				stunned = true;
				break;
			}
			if (effect instanceof Root) {
				rooted = true;
				break;
			}
		}
		if (stunned)
			c.setCondition(Condition.INACTIVE);
		else if (rooted) 
			c.setCondition(Condition.ROOTED);
		else
			c.setCondition(Condition.ACTIVE);
	}

}
