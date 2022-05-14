package model.world;

import java.util.ArrayList;

import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;

public class Hero extends Champion {

	public Hero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}
	
<<<<<<< HEAD
	public void useLeaderAbility(ArrayList<Champion> targets) throws CloneNotSupportedException{
=======
	public void useLeaderAbility(ArrayList<Champion> targets) {
>>>>>>> 2adee0810328c8e24d197ccd9af140f976b89927
		for(Champion c : targets) {
			ArrayList<Effect> appliedEffects = c.getAppliedEffects();
			for(Effect effect :appliedEffects ) {
				if(effect.getType() == EffectType.DEBUFF) {
					appliedEffects.remove(effect);
				}
<<<<<<< HEAD
				
				Embrace embrace = new Embrace(2);
				appliedEffects.add(embrace);
					
			}
=======
			}
			
			// add embrace to each champion in the targets (E.B Amir)
			Embrace embrace = new Embrace(2);
			appliedEffects.add(embrace);
				
			
>>>>>>> 2adee0810328c8e24d197ccd9af140f976b89927
		}
	}
		
}
	

	

