package model.world;

import java.util.ArrayList;

import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;
import model.effects.Stun;

public class AntiHero extends Champion {

	public AntiHero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}

	public void useLeaderAbility(ArrayList<Champion> targets){
		for(Champion c : targets) {
//			ArrayList<Effect> appliedEffects = c.getAppliedEffects();
//			appliedEffects.add(new Stun(2));
			Stun stun = new Stun(2);
			stun.apply(c);
		}
	}

}
