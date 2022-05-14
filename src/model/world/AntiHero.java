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

<<<<<<< HEAD
	public void useLeaderAbility(ArrayList<Champion> targets) throws CloneNotSupportedException{
=======
	public void useLeaderAbility(ArrayList<Champion> targets){
>>>>>>> 2adee0810328c8e24d197ccd9af140f976b89927
		for(Champion c : targets) {
			ArrayList<Effect> appliedEffects = c.getAppliedEffects();
			appliedEffects.add(new Stun(2));
		}
	}

}
