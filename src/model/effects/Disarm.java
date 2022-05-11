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
	
	
	
	public void apply(Champion c) {
		DamagingAbility ability = new DamagingAbility("Punch",0,1,1,AreaOfEffect.SINGLETARGET,1,50);
		c.getAbilities().add(ability);
		ArrayList<Effect> effects = c.getAppliedEffects();
		effects.add(this);
	}
	
	public void remove(Champion c) {			
		ArrayList<Ability> abilities = c.getAbilities();
		for(Ability a : abilities) {
			if(a.getName().equals("Punch")) {
				abilities.remove(a);
				break;
			}
				
		}
		
		ArrayList<Effect> effects = c.getAppliedEffects();
		effects.remove(this);
		
	}
	
}
