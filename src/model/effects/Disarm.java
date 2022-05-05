package model.effects;

import model.abilities.AreaOfEffect;
import model.abilities.DamagingAbility;
import model.world.Champion;

public class Disarm extends Effect {
	

	public Disarm( int duration) {
		super("Disarm", duration, EffectType.DEBUFF);
		
	}

	@Override
	public void apply(Champion c) {
		// target cannot use normal attacks
		DamagingAbility d = new DamagingAbility("Punch", 0, 1, 1, AreaOfEffect.SELFTARGET, 1,50);
		c.getAppliedEffects().add(d);
	}

	@Override
	public void remove(Champion c) {
		// TODO Auto-generated method stub
		
	}
	
}
