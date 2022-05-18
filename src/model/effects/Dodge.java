package model.effects;

import model.world.Champion;

public class Dodge extends Effect {

	public Dodge(int duration) {
		super("Dodge", duration, EffectType.BUFF);
	}
	
	/*
	 * Target has a 50% chance of dodging normal attacks.
	 * Increase speed by 5%.
	 */
	public void apply(Champion c) {
		c.setSpeed((int)(c.getSpeed() * 1.05));
	}
	
	public void remove(Champion c) {
		c.setSpeed((int)(c.getSpeed() / 1.05));
	}
}
