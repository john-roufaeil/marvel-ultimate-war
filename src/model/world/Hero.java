package model.world;

import java.util.ArrayList;

import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;

public class Hero extends Champion {

	public Hero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}
	
	public void useLeaderAbility(ArrayList<Champion> targets) {
		for(Champion c : targets) {
			System.out.println("HI");
			ArrayList<Effect> appliedEffects = c.getAppliedEffects();
			for(Effect effect : appliedEffects ) {
				if(effect.getType() == EffectType.DEBUFF) {
					effect.remove(c);
				}
			}
			Embrace embrace = new Embrace(2);
			embrace.apply(c);
		}
	}
		
}
	

	

