package model.effects;

import java.util.ArrayList;

import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.DamagingAbility;
import model.world.Champion;

public class Dodge extends Effect {

	public Dodge(int duration) {
		super("Dodge", duration, EffectType.BUFF);
	}

	
	public void apply(Champion c) {
		// not sure if the speed should be double or int
		// I casted it to int as setSpeed() takes int as parameter
		int speed = (int)(c.getSpeed() + (0.05 * c.getSpeed()));
		c.setSpeed(speed);
		ArrayList<Effect> effects = c.getAppliedEffects();
		effects.add(this);
	}
	
	public void remove(Champion c) {
		// not sure if the speed should be double or int
		// I casted it to int as setSpeed() takes int as parameter
		int speed = (int)(c.getSpeed() / (1.05));
		c.setSpeed(speed);
		ArrayList<Effect> effects = c.getAppliedEffects();
		effects.remove(this);
	}
	
}
