package model.effects;

import java.util.ArrayList;

import model.abilities.Ability;
import model.world.Champion;
import model.world.Condition;

public class Stun extends Effect {

	public Stun(int duration) {
		super("Stun", duration, EffectType.DEBUFF);
	}
	
	public void apply(Champion c) {
		
		c.setCondition(Condition.INACTIVE);
		// Target is not allowed to play their turn for the duration.
		ArrayList<Effect> effects = c.getAppliedEffects();
		effects.add(this);
	}
	
	public void remove(Champion c) {
		c.setCondition(Condition.ACTIVE);
		// Target is not allowed to play their turn for the duration.
<<<<<<< HEAD
		ArrayList<Effect> effects = c.getAppliedEffects();
		effects.remove(this);
=======
//		ArrayList<Effect> effects = c.getAppliedEffects();
//		effects.remove(this);
>>>>>>> 2adee0810328c8e24d197ccd9af140f976b89927
	}


}
