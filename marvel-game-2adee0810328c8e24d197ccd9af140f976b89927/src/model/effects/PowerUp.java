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
	
	public void apply(Champion c) {
		ArrayList<Ability> abilities = c.getAbilities();
		for(Ability a : abilities) {
			
			if(a instanceof DamagingAbility)
				((DamagingAbility) a).setDamageAmount((int)(((DamagingAbility) a).getDamageAmount()*0.2 + ((DamagingAbility)a).getDamageAmount()));    
			else if(a instanceof HealingAbility)
				((HealingAbility) a).setHealAmount((int)(((HealingAbility) a).getHealAmount()*0.2 + ((HealingAbility)a).getHealAmount()));
		}
		
//		ArrayList<Effect> effects = c.getAppliedEffects();
//		effects.add(this);
		
	}
	
	public void remove(Champion c) {
		ArrayList<Ability> abilities = c.getAbilities();
		for(Ability a : abilities) {
			
			if(a instanceof DamagingAbility)
				((DamagingAbility) a).setDamageAmount((int)(((DamagingAbility) a).getDamageAmount() / (1.2)));    
			else if(a instanceof HealingAbility)
				((HealingAbility) a).setHealAmount((int)(((HealingAbility) a).getHealAmount() / (1.2)));
		}
		
//		ArrayList<Effect> effects = c.getAppliedEffects();
//		effects.remove(this);
	}
	
}
