package model.effects;

import model.world.Champion;

public class Shield extends Effect {

	public Shield( int duration) {
		super("Shield", duration, EffectType.BUFF);
	}
	
	/*
	 * Block the next attack or damaging ability cast on target.
	 * Once an attack or ability is blocked, the effect should be removed.
     * Increase speed by 2%.
	 */

	public void apply(Champion c) {
		c.setSpeed((int)(c.getSpeed() * 1.02));
	}
	
	public void remove(Champion c) {
		c.setSpeed((int)(c.getSpeed() / 1.02));
	}
}