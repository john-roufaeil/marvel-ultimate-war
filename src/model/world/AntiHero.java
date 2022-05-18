package model.world;

import java.util.ArrayList;

import model.effects.Stun;

public class AntiHero extends Champion {

	public AntiHero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);
	}
	
	// All champions on the board except for the leaders of each team will be stunned for 2 turns
	
	public void useLeaderAbility(ArrayList<Champion> targets) {
		for (Champion c : targets) {
			Stun stun = new Stun(2);
			c.getAppliedEffects().add(stun);
			stun.apply(c);
		} 
	}
}
