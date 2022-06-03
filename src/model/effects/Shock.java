package model.effects;

import model.world.Champion;

public class Shock extends Effect {

	public Shock(int duration) {
		super("Shock", duration, EffectType.DEBUFF);
	}

	/*
	 * Decrease target speed by 10%
	 * Decrease the target’s normal attack damage by 10%
 	 * Decrease max action points per turn and current action points by 1.
	 */
	
	public void apply(Champion c) {
		c.setSpeed((int)(c.getSpeed() * 0.9));
		c.setAttackDamage((int)(c.getAttackDamage() * 0.9));
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn() - 1);
		c.setCurrentActionPoints((c.getCurrentActionPoints()-1));
	}
	
	public void remove(Champion c) {
		c.setSpeed((int)(c.getSpeed() / 0.9));
		c.setAttackDamage((int)(c.getAttackDamage() / 0.9));
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn() + 1);
		c.setCurrentActionPoints((c.getCurrentActionPoints() + 1));
	}
}