package model.effects;

import model.world.Champion;

public class Embrace extends Effect {
	public Embrace(int duration) {
		super("Embrace", duration, EffectType.BUFF);
	}
	
	/*
	 * Permanently add 20% from maxHP to currentHP,
	 * Permanently increase mana by 20%,
	 * Increase speed and attackDamage by 20%.
	 */
	
	public void apply(Champion c) {
		c.setCurrentHP((c.getCurrentHP() + (int)(0.2 * c.getMaxHP())));
		c.setMana((int)(c.getMana() * 1.2));
		c.setSpeed((int)(c.getSpeed() * 1.2));
		c.setAttackDamage((int)(c.getAttackDamage() * 1.2));		
	}

	public void remove(Champion c) {
		c.setSpeed((int)(c.getSpeed() / 1.2));
		c.setAttackDamage((int)(c.getAttackDamage() / 1.2));
	}
}