package model.world;

import java.util.ArrayList;

import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;

public class Hero extends Champion {
	public Hero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);
	}

	//Removes all negative effects from the player’s entire team and adds an Embrace effect to them which lasts for 2 turns
	public void useLeaderAbility(ArrayList<Champion> targets) {
		for (Champion c : targets) {
			ArrayList<Effect> clonedEffects = (ArrayList<Effect>) c.getAppliedEffects().clone();
			ArrayList<Effect> effects = c.getAppliedEffects();
			for (Effect e : clonedEffects) {
				if (e.getType() == EffectType.DEBUFF) {
					effects.remove(e);
					e.remove(c);
				}
			}
			Embrace embrace = new Embrace(2);
			effects.add(embrace);
			embrace.apply(c);
		}
	}
}
