package model.world;

import java.util.ArrayList;

public class Villain extends Champion {
	public Villain(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);
	}

	// Immediately eliminates (knocks out) all enemy champions with less than 30% health points
	
	public void useLeaderAbility(ArrayList<Champion> targets) {
		for (Champion c : targets) {
			if (c.getCurrentHP() < (int)(0.3 * c.getMaxHP())) {
				c.setCurrentHP(0);
				c.setCondition(Condition.KNOCKEDOUT);
			}
		}
	}
}