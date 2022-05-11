package model.world;

import java.util.ArrayList;

import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;

public class Villain extends Champion {

	public Villain(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}
	
	public void useLeaderAbility(ArrayList<Champion> targets) throws CloneNotSupportedException{
		for(Champion c : targets) {
			
			if (c.getCurrentHP() < 0.3* c.getMaxHP())
				c.setCondition(Condition.KNOCKEDOUT);
		}
	}
	
	
	
}
