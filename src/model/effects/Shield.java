package model.effects;

import java.util.ArrayList;

import model.abilities.Ability;
import model.world.Champion;
import model.world.Condition;

public class Shield extends Effect {

	public Shield( int duration) {
		super("Shield", duration, EffectType.BUFF);
		
	}
	
	public void apply(Champion c) {
		int speed = (int)(0.02 * c.getSpeed() + c.getSpeed());
		c.setSpeed(speed);
		// another stuff to be added
		
		ArrayList<Effect> effects = c.getAppliedEffects();
		effects.add(this);
	}

	
	public void remove(Champion c) {
		int speed = (int)(c.getSpeed() / (1.02));
		c.setSpeed(speed);
		// another stuff to be added
		
		ArrayList<Effect> effects = c.getAppliedEffects();
		effects.remove(this);
	}
}
