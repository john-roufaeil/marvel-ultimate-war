package model.effects;

import java.util.ArrayList;

import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.DamagingAbility;
import model.world.Champion;

public class Disarm extends Effect {
	public Disarm( int duration) {
		super("Disarm", duration, EffectType.DEBUFF);
	}
	
	/*
	 * Target cannot use normal attacks.
	 * Gain a SINGLETARGET damaging ability called “Punch”
	 * costs: 0, damage: 50, cooldown: 1, range: 1, actions: 1
	 */
	
	public void apply(Champion c) {
		DamagingAbility ability = new DamagingAbility("Punch",0,1,1,AreaOfEffect.SINGLETARGET,1,50);
		c.getAbilities().add(ability);
	}
	
	public void remove(Champion c) {	
		ArrayList<Ability> clonedAbilities = (ArrayList<Ability>) c.getAbilities().clone();
		for(Ability a : clonedAbilities) {
			if(a.getName().equals("Punch")) {
				c.getAbilities().remove(a);
				break;
			}	
		}
	}
}