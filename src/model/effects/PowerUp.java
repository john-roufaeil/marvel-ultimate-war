package model.effects;

import java.util.ArrayList;

import model.abilities.Ability;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.world.Champion;

public class PowerUp extends Effect {
	public PowerUp(int duration) {
		super("PowerUp", duration, EffectType.BUFF);
	}
	
	/*
	 * Increase damageAmount and healAmount of all damaging and healing abilities
	 *  of the target by 20%.
	 */
	
	public void apply(Champion c) {
		ArrayList<Ability> abilities = c.getAbilities();
		for(Ability a : abilities) {
			if(a instanceof DamagingAbility)
				((DamagingAbility) a).setDamageAmount((int)(((DamagingAbility) a).getDamageAmount()*1.2));    
			else if(a instanceof HealingAbility)
				((HealingAbility) a).setHealAmount((int)(((HealingAbility) a).getHealAmount()*1.2));
		}
	}
	
	public void remove(Champion c) {
		ArrayList<Ability> abilities = c.getAbilities();
		for(Ability a : abilities) {
			if(a instanceof DamagingAbility)
				((DamagingAbility) a).setDamageAmount((int)(((DamagingAbility) a).getDamageAmount() / (1.2)));    
			else if(a instanceof HealingAbility)
				((HealingAbility) a).setHealAmount((int)(((HealingAbility) a).getHealAmount() / (1.2)));
		}
	}
}